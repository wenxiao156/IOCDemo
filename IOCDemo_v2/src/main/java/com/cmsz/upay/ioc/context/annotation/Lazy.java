package com.cmsz.upay.ioc.context.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 使用此注解标记的bean，除非value=false，否则采用懒加载
 * @author liyuanchang
 * @date 2018年8月30日下午2:40:11
 * @version V1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
public @interface Lazy {
	boolean value() default true;
}
