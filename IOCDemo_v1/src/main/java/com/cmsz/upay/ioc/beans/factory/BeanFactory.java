package com.cmsz.upay.ioc.beans.factory;

import com.cmsz.upay.ioc.beans.exception.BeansException;

/**
 * 用于注入实例对象。<br>
 * <b>答题者无需修改此类</b>
 * @author 
 *
 */
public interface BeanFactory {
	Object getBean(String name) throws BeansException;
	
	<T> T getBean(String name, Class<T> requiredType) throws BeansException;
	
	<T> T getBean(Class<T> requiredType) throws BeansException;
}
