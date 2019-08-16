package com.cmsz.upay.ioc.context.impl;

import com.cmsz.upay.ioc.beans.exception.BeansException;
import com.cmsz.upay.ioc.context.ApplicationContext;
import com.cmsz.upay.ioc.context.annotation.Component;
import com.cmsz.upay.ioc.context.annotation.ComponentScan;
import com.cmsz.upay.ioc.vo.BeanDefinition;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AnnotationConfigApplicationContext implements ApplicationContext {
    /** 保存componentScan给定的包下的类的路径 */
    ArrayList<String> classPaths = new ArrayList<>();

    /** 存储id及对应的BeanDefinition */
    private Map<String, BeanDefinition> beanIdMap = new HashMap<>();

    public AnnotationConfigApplicationContext(Class<?>... annotatedClasses) {
        for (int i = 0; i < annotatedClasses.length; i++) {
            if (annotatedClasses[i].isAnnotationPresent(ComponentScan.class)) {
                ComponentScan scanComponent = annotatedClasses[i].getAnnotation(ComponentScan.class);
                String[] fullClassValue = scanComponent.value();
                findComponent(fullClassValue);
            }
        }
    }
    /**
     * 找到scanComponent给定的包对应的路径
     */
    private void findComponent(String[] fullClassValue) {
        for (int i = 0; i < fullClassValue.length; i++) {
            scanPackge(fullClassValue[i]);
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
                    if(id.isEmpty()) {
                        id = path.substring(path.lastIndexOf(".")+1).toLowerCase();
                    }
                    Object obj = cls.newInstance();
                    BeanDefinition beanDefinition = new BeanDefinition(id, path, null, false, null,
                            null, obj);
                    beanIdMap.put(id,beanDefinition);
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

    @Override
    public Object getBean(String name) throws BeansException {
        return beanIdMap.get(name).getObj();
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return null;
    }

    @Override
    public <T> T getBean(Class<T> requiredType) throws BeansException {
        return null;
    }

}
