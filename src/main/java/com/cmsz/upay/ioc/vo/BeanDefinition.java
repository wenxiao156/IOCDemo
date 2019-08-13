package com.cmsz.upay.ioc.vo;

import java.util.*;

public class BeanDefinition {
	private String id;
	private String fullClass;
	private String scope;
	private boolean lazyInit;
	private Object ref;
	private HashMap<String, String> propertys = new HashMap<>();
	private HashMap<String, String> constructors = new HashMap<>();
	private Object obj;
	public BeanDefinition(String id, String fullClass, String scope, boolean lazyInit,  HashMap<String, String> propertys, HashMap<String, String> constructors, Object obj) {
		this.id = id;
		this.fullClass = fullClass;
		this.scope = scope;
		this.lazyInit = lazyInit;
//		this.ref = ref;
		this.propertys = propertys;
		this.constructors = constructors;
		this.obj = obj;
	}
	public Object getObj() {
		return obj;
	}
	public void setObj(Object obj) {
		this.obj = obj;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFullClass() {
		return fullClass;
	}
	public void setFullClass(String fullClass) {
		this.fullClass = fullClass;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	public boolean isLazyInit() {
		return lazyInit;
	}
	public void setLazyInit(boolean lazyInit) {
		this.lazyInit = lazyInit;
	}
	public Object getRef() {
		return ref;
	}
	public void setRef(Object ref) {
		this.ref = ref;
	}
	public HashMap<String, String> getPropertys() {
		return propertys;
	}
	public void setPropertys(HashMap<String, String> propertys) {
		this.propertys = propertys;
	}
	public HashMap<String, String> getConstructors() {
		return constructors;
	}
	public void setConstructors(HashMap<String, String> constructors) {
		this.constructors = constructors;
	}
	
}
