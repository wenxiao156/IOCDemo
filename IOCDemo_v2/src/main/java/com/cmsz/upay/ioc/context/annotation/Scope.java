package com.cmsz.upay.ioc.context.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 相当于&lt;bean scope=""&gt;，未注解或者注解了value="" | value = "singleton"都是单例模式
 * @author liyuanchang
 * @date 2018年8月30日下午2:43:04
 * @version V1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
public @interface Scope {
	String value() default "";
}
