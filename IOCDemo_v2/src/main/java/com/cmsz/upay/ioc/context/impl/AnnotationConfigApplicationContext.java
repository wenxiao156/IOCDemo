package com.cmsz.upay.ioc.context.impl;

import com.cmsz.upay.ioc.beans.exception.BeansException;
import com.cmsz.upay.ioc.context.ApplicationContext;
import com.cmsz.upay.ioc.context.annotation.*;
import com.cmsz.upay.ioc.vo.BeanDefinition;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AnnotationConfigApplicationContext implements ApplicationContext {
    /** 保存componentScan给定的包下的类的路径*/
    private ArrayList<String> classPaths = new ArrayList<>();

    /** 存储id及对应的BeanDefinition*/
    private Map<String, BeanDefinition> beanIdMap = new HashMap<>();

    /** 正在创建依赖的bean对象*/
    private ArrayList<String> refObject = new ArrayList<>();

    /**
     * 通过给定的类查找是否有ComponentScan和Configuration的注解
     */
    public AnnotationConfigApplicationContext(Class<?>... annotatedClasses) {
        for (int i = 0; i < annotatedClasses.length; i++) {
            if (annotatedClasses[i].isAnnotationPresent(ComponentScan.class)) {
                ComponentScan scanComponent = annotatedClasses[i].getAnnotation(ComponentScan.class);
                String[] fullClassValue = scanComponent.value();
                for (int j = 0; j < fullClassValue.length; j++) {
                    scanPackge(fullClassValue[j]);
                }
            } else if (annotatedClasses[i].isAnnotationPresent(Configuration.class)) {
                doBean(annotatedClasses[i]);
            }
        }
    }

    /**
     * 为Component注解下的类创建实例，保存进beanIdMap中
     */
    private void scanPackge(String packagePath) {
        //项目根目录
        String classpath = System.getProperty("user.dir") + "\\src\\main\\java\\";
        String filePath = packagePath.replace(".", File.separator);
        //绝对路径
        String absolutePath = classpath + filePath;
        doPath(new File(absolutePath));
        for (String path : classPaths) {
            path = path.replace(classpath.replace("/", "\\").replaceFirst("\\\\", ""), "").replace("\\", ".").replace(".java", "");
            path = path.substring(path.indexOf("src.main.") + "src.main.".length());
            System.out.println(path);
            try {
                Class cls = Class.forName(path);
                if (cls.isAnnotationPresent(Component.class)) {
                    Component com = (Component) cls.getAnnotation(Component.class);
                    String id = com.name();
                    if (id.isEmpty()) {
                        String idStr = path.substring(path.lastIndexOf(".") + 1);
                        String first = String.valueOf(idStr.charAt(0));
                        id = idStr.replaceFirst(first, first.toLowerCase());
                    }
                    Object obj = cls.newInstance();
                    BeanDefinition beanDefinition = new BeanDefinition(id, obj.getClass(), null, false, obj, null, null, null);
                    beanIdMap.put(id, beanDefinition);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 递归调用doPath方法，把scanCompenet下的文件路径保存进classPaths中
     */
    private void doPath(File file) {
        if (file.isDirectory()) {//文件夹
            File[] fileList = file.listFiles();
            for (File f : fileList) {
                doPath(f);
            }
        } else {
            classPaths.add(file.getPath());
        }
    }

    /**
     * 查找类中有@bean注解的方法，bean没有设置名称默认为方法名
     */
    private void doBean(Class cls) {
        Method[] methods = cls.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            Bean bean = methods[i].getAnnotation(Bean.class);
            if (bean != null) {
                Object obj = null;
                String id = bean.name();
                if (id.isEmpty()) {
                    id = methods[i].getName();
                }
                scanBean(id, methods[i], cls);
            }
        }
    }

    /**
     * 将bean对象保存进beanIdMap中
     * 当有@Lazy或者@Scope("prototype")注解时，保存的obj实例为null
     */
    private Object scanBean(String id, Method method, Class cls) throws BeansException {
        Object obj = null;
        Scope scope = method.getAnnotation(Scope.class);
        Lazy lazy = method.getAnnotation(Lazy.class);
        String scopeStr = "";
        boolean lazyInit = false;
        BeanDefinition beanDefinition = null;
        if (scope != null && "prototype".equals(scope.value())) {
            scopeStr = "prototype";
        }
        if (lazy != null) {
            lazyInit = true;
        }
        if (!(scope != null && "prototype".equals(scope.value())) && !lazyInit) {
            obj = returnBean(id, method, cls);
            beanDefinition = new BeanDefinition(id, obj.getClass(), scopeStr, lazyInit,  obj, null, null, null);
        } else {
            beanDefinition = new BeanDefinition(id, method.getReturnType(), scopeStr, lazyInit, obj, method, cls, method.getTypeParameters());
        }
        beanIdMap.put(id, beanDefinition);
        return beanDefinition.getObj();
    }

    /**
     * 根据反射创建bean实例
     * 将有依赖的bean对象的id保存进refObject中，当想要再次创建该bean对象时抛出异常
     */
    private Object returnBean(String beanName, Method method, Class cls) throws BeansException {
        try {
            if (method.getParameterCount() == 0) {
                return method.invoke(cls.newInstance());
            }
            Class[] paraClass = method.getParameterTypes();
            Parameter[] parameters = method.getParameters();
            Object[] para = new Object[method.getParameterCount()];
            for (int i = 0; i < parameters.length; i++) {
                String id = parameters[i].getName();
                if (beanIdMap.containsKey(id)) {
                    if (beanIdMap.get(id).getObj() != null) {
                        para[i] = beanIdMap.get(id).getObj();
                    } else {
                        refObject.add(beanName);
                        if (refObject.contains(id)) {
                            throw new BeansException("循环依赖");
                        } else {
                            para[i] = returnBean(id, beanIdMap.get(id).getMethod(), beanIdMap.get(id).getMethodClass());
                        }
                    }
                } else {
                    refObject.add(beanName);
                    if (refObject.contains(id)) {
                        throw new BeansException("循环依赖");
                    } else {
                        para[i] = scanConfig(id, cls);
                    }
                }
            }
            Object obj = method.invoke(cls.newInstance(), para);
            return obj;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 返回依赖的bean对象
     */
    private Object scanConfig(String id, Class cls) {
        Method[] methods = cls.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            Bean bean = methods[i].getAnnotation(Bean.class);
            String beanName = bean.name();
            if (beanName.isEmpty()) {
                beanName = methods[i].getName();
            }
            if (bean != null && beanName.equals(id)) {
                return scanBean(id, methods[i], cls);
            }
        }
        return null;
    }

    /**
     * 通过id查找bean对象
     * 当scope为prototype或者lazyinit为true时，根据保存的方法创建bean对象
     */
    @Override
    public Object getBean(String name) throws BeansException {
        BeanDefinition bean = beanIdMap.get(name);
        if ("prototype".equals(bean.getScope()) || bean.isLazyInit()) {
            return returnBean(name, bean.getMethod(), bean.getMethodClass());
        }
        return bean.getObj();
    }

    /**
     * 通过id和class查找bean对象
     */
    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return (T) getBean(name);
    }

    /**
     * 通过class查找bean对象
     * 遍历beanIdMap，当返回的对象的个数大于1时抛出异常
     */
    @Override
    public <T> T getBean(Class<T> requiredType) throws BeansException {
        ArrayList<Object> objList = new ArrayList<>();
        for (String id : beanIdMap.keySet()) {
            Class cls = beanIdMap.get(id).getFullClass();
            if (cls == requiredType) {
                objList.add(getBean(id));
            }
        }
        if (objList.size() > 1) {
            throw new BeansException("对象不确定");
        } else {
            return (T) objList.get(0);
        }
    }

}
