package com.cmsz.upay.ioc.context.impl;

import com.cmsz.upay.ioc.beans.exception.BeansException;
import com.cmsz.upay.ioc.context.ApplicationContext;

public class AnnotationConfigApplicationContext implements ApplicationContext {
	
	public AnnotationConfigApplicationContext(Class<?>... annotatedClasses) {
		
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
