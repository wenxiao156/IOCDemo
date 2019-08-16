package com.cmsz.upay.ioc.context.impl;

import java.util.HashMap;
import java.util.Map;

import org.dom4j.Document;

import com.cmsz.upay.ioc.beans.exception.BeansException;
import com.cmsz.upay.ioc.beans.factory.config.BeanDefinition;
import com.cmsz.upay.ioc.context.ApplicationContext;
import com.cmsz.upay.ioc.util.BeanDefinitionUtil;
import com.cmsz.upay.ioc.util.Dom4jUtil;

/**
 * <b>实现类</b><br>
 * 参考spring-framework，加载并解析配置文件，完成对象实例化，实现依赖注入<br>
 * 完成具体的功能，可自己添加需要的属性和方法。<br>
 * 需要完成的功能点大致如下：<br>
 * <ol>
 * 	<li>配置文件加载、解析</li>
 * 	<li>Bean实例化</li>
 * 	<li>属性注入/构造器注入</li>
 * 	<li>依赖、循环依赖</li>
 * 	<li>懒加载</li>
 * 	<li>Bean实例的作用域</li>
 * </ol>
 * @author 
 * 
 */
public class ClassPathXmlApplicationContext implements ApplicationContext {
	
	public ClassPathXmlApplicationContext() {
		this("applicationContext.xml");// 默认加载applicationContext.xml
	}
	
	public ClassPathXmlApplicationContext(String configLocation) {
	}

	@Override
	public Object getBean(String name) throws BeansException {
		return null;
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
