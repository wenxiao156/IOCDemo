package com.cmsz.upay.ioc.context.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于注解将一个bean托管给ioc容器，相当于<bean>标签<br>
 * spring中的@Bean更复杂，参加org.springframework.context.annotation.Bean
 * @author liyuanchang
 * @date 2018年8月30日下午2:14:15
 * @version V1.0
 * @see Lazy
 * @see Scope
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Bean {
	/**
	 * 用于标记bean的name属性，相当于&lt;bean name=""&gt;<br>
	 * 如果没有，默认使用@Bean标记的方法名作为name<br>
	 * @see Component#name()
	 * @return
	 */
	String name() default "";
}
