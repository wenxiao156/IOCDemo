package com.cmsz.upay.ioc.annotationScan.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.cmsz.upay.ioc.component.Apple;
import com.cmsz.upay.ioc.context.ApplicationContext;
import com.cmsz.upay.ioc.context.impl.AnnotationConfigApplicationContext;

public class TestSuit {
	ApplicationContext ctx = null;
	
	/**
	 * 初始化
	 */
	@Before
	public void init() {
		ctx = new AnnotationConfigApplicationContext(Config.class);
	}
	
	@Test
	public void test1() {
		Apple apple = (Apple) ctx.getBean("apple");
		Assert.assertNotNull(apple);
	}
	
	
}
