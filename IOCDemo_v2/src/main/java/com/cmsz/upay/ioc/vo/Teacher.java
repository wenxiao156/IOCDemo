package com.cmsz.upay.ioc.vo;

public class Teacher {
	private String name;
	private Student student;
	
	public Teacher() {
	}
	
	public Teacher(String name, Student student) {
		this.name = name;
		this.student = student;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

}
