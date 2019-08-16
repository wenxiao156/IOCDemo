package com.cmsz.upay.ioc.vo;

import java.util.*;

public class BeanDefinition {
	private String id;
	private String fullClass;
	private String scope;
	private boolean lazyInit;
	private Map<String, Object> propertys = new HashMap<>();
	private Map<String, Object> constructors = new HashMap<>();
	private Object obj;
	public BeanDefinition(String id, String fullClass, String scope, boolean lazyInit,  Map<String, Object> propertysMap, Map<String, Object> constructorsMap, Object obj) {
		this.id = id;
		this.fullClass = fullClass;
		this.scope = scope;
		this.lazyInit = lazyInit;
		this.propertys = propertysMap;
		this.constructors = constructorsMap;
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
	public Map<String, Object> getPropertys() {
		return propertys;
	}
	public void setPropertys(HashMap<String, Object> propertys) {
		this.propertys = propertys;
	}
	public Map<String, Object> getConstructors() {
		return constructors;
	}
	public void setConstructors(HashMap<String, Object> constructors) {
		this.constructors = constructors;
	}
	
}
