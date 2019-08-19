package com.cmsz.upay.ioc.vo;

import java.lang.reflect.Method;
import java.util.*;

public class BeanDefinition {
    private String id;
    private Class fullClass;
    private String scope;
    private boolean lazyInit;
    private Object obj;
    private Method method;
    private Class methodClass;
    private Object[] methodParameters;

    public BeanDefinition(String id, Class fullClass, String scope, boolean lazyInit, Object obj,Method method,Class methodClass,Object[] methodParameters) {
        this.id = id;
        this.fullClass = fullClass;
        this.scope = scope;
        this.lazyInit = lazyInit;
        this.obj = obj;
        this.method = method;
        this.methodClass = methodClass;
        this.methodParameters = methodParameters;
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
    public Class getFullClass() {
        return fullClass;
    }
    public void setFullClass(Class fullClass) {
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
    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }
    public Class getMethodClass() {
        return methodClass;
    }

    public void setMethodClass(Class methodClass) {
        this.methodClass = methodClass;
    }

    public Object[] getMethodParameters() {
        return methodParameters;
    }

    public void setMethodParameters(Object[] methodParameters) {
        this.methodParameters = methodParameters;
    }
}
