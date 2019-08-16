package com.cmsz.upay.ioc.annotation.test;

import com.cmsz.upay.ioc.context.annotation.Bean;
import com.cmsz.upay.ioc.context.annotation.Lazy;
import com.cmsz.upay.ioc.context.annotation.Scope;
import com.cmsz.upay.ioc.vo.Cupcake;
import com.cmsz.upay.ioc.vo.Donut;
import com.cmsz.upay.ioc.vo.Froyo;
import com.cmsz.upay.ioc.vo.Honeycomb;
import com.cmsz.upay.ioc.vo.JellyBean;
import com.cmsz.upay.ioc.vo.KitKat;
import com.cmsz.upay.ioc.vo.Student;
import com.cmsz.upay.ioc.vo.Teacher;

public class Config {
	/**
	 * 属性注入，单例模式
	 * @return
	 */
	@Bean
	public Cupcake cupcake() {
		Cupcake cupcake = new Cupcake();
		cupcake.setName("CupCake");
		cupcake.setPrice(10);
		return cupcake;
	}
	
	/**
	 * 构造器注入
	 * @return
	 */
	@Bean(name = "donut")
	public Donut donutBean() {
		return new Donut("Donut");
	}
	
	/**
	 * prototype模式
	 * @return
	 */
	@Bean
	@Scope("prototype")
	public Froyo froyo() {
		return new Froyo();
	}
	
	/**
	 * 懒加载
	 * @return
	 */
	@Bean
	@Lazy
	public Honeycomb honeycomb() {
		return new Honeycomb();
	}
	
	/**
	 * 依赖
	 * @param kitKat
	 * @return
	 */
	@Bean
	public JellyBean jellyBean(KitKat kitKat) {
		JellyBean jellyBean = new JellyBean();
		jellyBean.setKitKat(kitKat);
		return jellyBean;
	}
	@Bean
	public KitKat kitKat() {
		KitKat kitKat = new KitKat();
		kitKat.setName("kitkat");
		return kitKat;
	}
	
	/**
	 * 循环依赖，属性注入
	 * @return
	 */
	@Bean
	public Student student() {
		Student student = new Student();
		student.setName("kimi");
		student.setTeacher(teacher());
		return student;
	}
	@Bean
	public Teacher teacher() {
		Teacher teacher = new Teacher();
		teacher.setName("Hamilton");
		teacher.setStudent(student());
		return teacher;
	}
	
	/**
	 * 循环依赖，构造器注入，懒加载，加载时产生异常
	 * @return
	 */
	@Bean
	@Lazy
	public Student student1() {
		Student student = new Student();
		student.setName("kimi");
		student.setTeacher(teacher1());
		return student;
	}
	@Bean
	@Lazy
	public Teacher teacher1() {
		Teacher teacher = new Teacher();
		teacher.setName("Hamilton");
		teacher.setStudent(student1());
		return teacher;
	}
}
