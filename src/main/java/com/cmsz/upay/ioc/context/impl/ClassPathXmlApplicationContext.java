package com.cmsz.upay.ioc.context.impl;

import com.cmsz.upay.ioc.beans.exception.BeansException;
import com.cmsz.upay.ioc.context.ApplicationContext;
import com.cmsz.upay.ioc.vo.BeanDefinition;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;
import org.dom4j.*;
import org.dom4j.io.SAXReader;

/**
 * <b>实现类</b><br>
 * 参考spring-framework，加载并解析配置文件，完成对象实例化，实现依赖注入<br>
 * 完成具体的功能，可自己添加需要的属性和方法。<br>
 * 需要完成的功能点大致如下：<br>
 * <ol>
 * <li>配置文件加载、解析</li>
 * <li>Bean实例化</li>
 * <li>属性注入/构造器注入</li>
 * <li>依赖、循环依赖</li>
 * <li>懒加载</li>
 * <li>Bean实例的作用域</li>
 * </ol>
 * 
 * @author
 * 
 */
public class ClassPathXmlApplicationContext implements ApplicationContext {
	/**
	 * 存储id及对应的BeanDefinition
	 */
	private Map<String, BeanDefinition> beanIdMap = new HashMap<>();

	private ArrayList<String> isConstructingList = new ArrayList<>();

	Document document = null;

	public ClassPathXmlApplicationContext() {
		this("applicationContext.xml");// 默认加载applicationContext.xml
	}

	/**
	 * 读取<beans>下的每个<bean>,将bean的属性和下面的property元素和constructor-arg元素保存进BeanDefinition中
	 * 当scope为prototype以及lazy-init为true不创建对象实例，在调用处再创建
	 */
	public ClassPathXmlApplicationContext(String configLocation) {
		SAXReader reader = new SAXReader();
		try {
			document = reader.read("src/main/resources/" + configLocation);
			Element beans = document.getRootElement();
			Iterator it = beans.elementIterator();
			while (it.hasNext()) {
				Element beanElement = (Element) it.next();
				String id = beanElement.attributeValue("id");
				scanBean(id, beanElement, false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 扫描bean元素获取属性和其子元素设置进beanIdMap和beanClassMap中
	 */
	private BeanDefinition scanBean(String id, Element beanElement, boolean isConstructor) throws BeansException {
		try {
			String fullClass = beanElement.attributeValue("class");
			Class cls = Class.forName(fullClass);
			String scope = beanElement.attributeValue("scope");
			boolean lazyInit = Boolean.valueOf(beanElement.attributeValue("lazy-init"));
			Map<String, Object> propertysMap = new LinkedHashMap<>();
			Map<String, Object> constructorsMap = new LinkedHashMap<>();
			Object obj = null;
			Iterator beanIt = beanElement.elementIterator();
			while (beanIt.hasNext()) {
				Element child = (Element) beanIt.next();
				if (child.getName().equals("property")) {
					Object value = getMapValue(child);
					if (!lazyInit || scope == null || scope.equals("singleton")) {
						if (obj == null) {
							obj = cls.newInstance();
						}
					}
					propertysMap.put(child.attributeValue("name"), value);
				} else if (child.getName().equals("constructor-arg")) {
					constructorsMap.put(child.attributeValue("name"), getMapValue(child));
				}
			}
			if (constructorsMap.size() > 0 && !lazyInit && (scope == null || scope.equals("singleton"))) {
				obj = constructorNewInstance(cls, id, constructorsMap);
			} else if (obj == null && !lazyInit && (scope == null || scope.equals("singleton"))) {
				obj = cls.newInstance();
			}
			BeanDefinition beanDefinition = new BeanDefinition(id, fullClass, scope, lazyInit, propertysMap,
					constructorsMap, obj);
			beanIdMap.put(id, beanDefinition);
			return beanDefinition;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 返回propertysMap中name对应的值
	 */
	private Object getMapValue(Element child) {
		Object obj = null;
		if (child.attributeValue("ref") != null) {
			if (beanIdMap.containsKey(child.attributeValue("ref"))) {
				obj = beanIdMap.get(child.attributeValue("ref")).getObj();
			} else {
				obj = child.attributeValue("ref");
			}
		} else {
			obj = child.attributeValue("value") == null ? child.getText() : child.attributeValue("value");
		}
		return obj;
	}

	// private Object getConstructorsMapValue(Element child) throws
	// BeansException {
	// Object obj = null;
	// if (child.attributeValue("ref") != null) {
	// if (beanIdMap.containsKey(child.attributeValue("ref"))) {
	// obj = beanIdMap.get(child.attributeValue("ref")).getObj();
	// } else {
	// Element beans = document.getRootElement();
	// Iterator it = beans.elementIterator();
	// while (it.hasNext()) {
	// Element beanElement = (Element) it.next();
	// String id = beanElement.attributeValue("id");
	// if (id.equals(child.attributeValue("ref"))) {
	// isConstructingList.add(id);
	// if (isConstructingList.contains(child.attributeValue("ref"))) {
	// throw new BeansException("循环依赖，构造器注入");
	// } else {
	// obj = scanBean(child.attributeValue("ref"), beanElement, true);
	// }
	// break;
	// }
	// }
	// }
	// } else {
	// obj = child.attributeValue("value") == null ? child.getText() :
	// child.attributeValue("value");
	// }
	// return obj;
	// }

	private Object constructorNewInstance(Class cls, String id, Map<String, Object> constructorsMap) throws BeansException{
		Class[] typeList = new Class[constructorsMap.size()];
		Object[] objList = new Object[constructorsMap.size()];
		try {
			int i = 0;
			for (String key : constructorsMap.keySet()) {
				Field f = cls.getDeclaredField(key);
				typeList[i] = f.getType();
				if (!(typeList[i].toString().endsWith("int") || typeList[i].toString().endsWith("Integer")
						|| typeList[i].toString().endsWith("String"))) {
					Element beans = document.getRootElement();
					Iterator it = beans.elementIterator();
					while (it.hasNext()) {
						Element beanElement = (Element) it.next();
						String beanId = beanElement.attributeValue("id");
						Iterator beanIt = beanElement.elementIterator();
						if (beanId.equals(constructorsMap.get(key))) {
							isConstructingList.add(id);
							while (beanIt.hasNext()) {
								Element child = (Element) beanIt.next();
								if (child.attributeValue("ref") != null
										&& isConstructingList.contains(child.attributeValue("ref"))) {
									throw new BeansException("循环依赖，构造器注入");
								}
							}
							objList[i] = scanBean(constructorsMap.get(key).toString(), beanElement, true).getObj();
						}
					}
				} else {
					objList[i] = constructorsMap.get(key);
				}
				i++;
			}
			Constructor cons = cls.getConstructor(typeList);
			Object obj = cons.newInstance(objList);
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Object getBean(String name) throws BeansException {
		Object obj = beanIdMap.get(name).getObj();
		if (obj == null) {
			obj = prototypeOrLazy(beanIdMap.get(name), obj);
		}
		try {
			Class cls = Class.forName(beanIdMap.get(name).getFullClass());
			setProperty(beanIdMap.get(name), cls, obj);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return obj;
	}

	@Override
	public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
		Object obj = beanIdMap.get(name).getObj();
		if (obj == null) {
			obj = prototypeOrLazy(beanIdMap.get(name), obj);
		}
		try {
			Class cls = Class.forName(beanIdMap.get(name).getFullClass());
			setProperty(beanIdMap.get(name), cls, obj);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return (T) obj;
	}

	@Override
	public <T> T getBean(Class<T> requiredType) throws BeansException {
		ArrayList<Object> objList = new ArrayList<>();
		try {
			for (String id : beanIdMap.keySet()) {
				Class cls = Class.forName(beanIdMap.get(id).getFullClass());
				if (cls == requiredType) {
					Object obj = beanIdMap.get(id).getObj();
					if (obj == null) {
						prototypeOrLazy(beanIdMap.get(id), obj);
					}
					setProperty(beanIdMap.get(id), requiredType, obj);
					objList.add(beanIdMap.get(id).getObj());
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		if (objList.size() > 1) {
			throw new BeansException("对象不确定");
		} else {
			return (T) objList.get(0);
		}
	}

	/**
	 * 在实例化后再设置属性，方便循环依赖
	 */
	private void setProperty(BeanDefinition beanDef, Class cls, Object obj){
		Map<String, Object> propertysMap = beanDef.getPropertys();
		try {
			if (propertysMap.size() > 0) {
				for (String key : propertysMap.keySet()) {
					Field f = cls.getDeclaredField(key);
					String type = f.getType().toString();
					f.setAccessible(true);
					if (type.endsWith("int") || type.endsWith("Integer")) {
						f.set(obj, Integer.valueOf(propertysMap.get(key).toString()));
					} else {
						if (beanIdMap.containsKey(propertysMap.get(key))) {
							BeanDefinition ref = beanIdMap.get(propertysMap.get(key));
							setProperty(ref, Class.forName(ref.getFullClass()), ref.getObj());
							f.set(obj, ref.getObj());
						} else {
							f.set(obj, propertysMap.get(key));
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 当scope为prototype或者懒加载时，在getbean时才创建对象实例
	 */
	public Object prototypeOrLazy(BeanDefinition beanDef, Object obj)  throws BeansException{
		try {
			Class cls = Class.forName(beanDef.getFullClass());
			Map<String, Object> constructorsMap = beanDef.getConstructors();
			if (constructorsMap.size() < 1) {
				obj = cls.newInstance();
			} else {
				obj = constructorNewInstance(cls, beanDef.getId(), constructorsMap);
				// Object[] constructorValue = new
				// Object[constructorsMap.size()];
				// Class[] type = new Class[constructorsMap.size()];
				// int i = 0;
				// for (String key : constructorsMap.keySet()) {
				// Field f = cls.getDeclaredField(key);
				// type[i] = f.getType();
				// constructorValue[i] = constructorsMap.get(key);
				// i++;
				// }
				// Constructor cons = cls.getConstructor(type);
				// obj = cons.newInstance(constructorValue);
			}
		}  catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} 
		return obj;
	}
}
