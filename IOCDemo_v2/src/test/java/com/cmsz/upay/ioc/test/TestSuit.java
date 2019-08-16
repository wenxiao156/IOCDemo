package com.cmsz.upay.ioc.test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.cmsz.upay.ioc.beans.exception.BeansException;
import com.cmsz.upay.ioc.context.ApplicationContext;
import com.cmsz.upay.ioc.context.impl.ClassPathXmlApplicationContext;
import com.cmsz.upay.ioc.vo.Cupcake;
import com.cmsz.upay.ioc.vo.Donut;
import com.cmsz.upay.ioc.vo.Froyo;
import com.cmsz.upay.ioc.vo.Honeycomb;
import com.cmsz.upay.ioc.vo.JellyBean;
import com.cmsz.upay.ioc.vo.Student;
import com.cmsz.upay.ioc.vo.Teacher;

public class TestSuit {
	ApplicationContext ctx = null;
	
	/**
	 * 初始化
	 */
	@Before
	public void init() {
		ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
	}
	
	/**
	 * 测试属性注入
	 */
	@Test
	public void test1() {
		Cupcake cupcake = (Cupcake) ctx.getBean("cupCake");
		Assert.assertNotNull(cupcake);
		Assert.assertEquals("CupCake", cupcake.getName());
		Assert.assertEquals(10, cupcake.getPrice());
	}
	
	/**
	 * 测试构造器注入
	 */
	@Test
	public void test2() {
		Donut donut = ctx.getBean("donut", Donut.class);
		Assert.assertNotNull(donut);
		Assert.assertEquals("Donut", donut.getName());
	}
	
	/**
	 * 测试单例模式
	 */
	@Test
	public void test3() {
		Cupcake cupcake1 = (Cupcake) ctx.getBean("cupCake");
		Cupcake cupcake2 = (Cupcake) ctx.getBean("cupCake");
		Assert.assertNotNull(cupcake1);
		Assert.assertNotNull(cupcake2);
		Assert.assertTrue(cupcake1== cupcake2);// 两个实例相等
	}
	
	/**
	 * 测试prototype模式
	 */
	@Test
	public void test4() {
		Froyo froyo1 = ctx.getBean("froyo", Froyo.class);
		Froyo froyo2 = ctx.getBean("froyo", Froyo.class);
		Assert.assertNotNull(froyo1);
		Assert.assertNotNull(froyo2);
		Assert.assertFalse(froyo1 == froyo2);// 两个实例不相等
	}
	
	/**
	 * 测试懒加载
	 */
	@Test
	public void test5() {
		// 重定向控制台输出
		PrintStream console = System.out;
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		System.setOut(new PrintStream(bao));
		
		// 获取懒加载实例
		Honeycomb honeycomb = ctx.getBean(Honeycomb.class);
		
		// 获取当前的换行符
		String lineSeparator = java.security.AccessController.doPrivileged(
	            new sun.security.action.GetPropertyAction("line.separator"));
		// 预期输出内容
		String output = "Honeycomb Constructor" + lineSeparator;
		
		Assert.assertEquals(output, bao.toString());
		
		// 还原控制台输出
		System.setOut(console);
	}
	
	/**
	 * 测试bean依赖
	 */
	@Test
	public void test6() {
		JellyBean jellyBean = ctx.getBean("jellyBean", JellyBean.class);
		Assert.assertNotNull(jellyBean);
		Assert.assertNotNull(jellyBean.getKitKat());
		Assert.assertEquals("KitKat", jellyBean.getKitKat().getName());
	}
	
	/**
	 * 测试循环依赖
	 */
	@Test
	public void test7() {
		Student student = ctx.getBean("student", Student.class);
		Teacher teacher = ctx.getBean("teacher", Teacher.class);
		Assert.assertNotNull(student);
		Assert.assertNotNull(teacher);
		Assert.assertNotNull(student.getTeacher());
		Assert.assertNotNull(teacher.getStudent());
		Assert.assertEquals("Kimi", teacher.getStudent().getName());
		Assert.assertEquals("Hamilton", student.getTeacher().getName());
	}
	
	/**
	 * 测试循环依赖，构造器注入，懒加载，加载时产生异常
	 */
	@Test(expected = BeansException.class)
	public void test8() {
		Student student = ctx.getBean("student1", Student.class);
		Teacher teacher = ctx.getBean("teacher1", Teacher.class);
	}
	
	/**
	 * 测试获取不明确对象
	 */
	@Test(expected = BeansException.class)
	public void test9() {
		// 配置文件中有两个Student实例(student1、student2)
		Student student = ctx.getBean(Student.class);
	}
}
