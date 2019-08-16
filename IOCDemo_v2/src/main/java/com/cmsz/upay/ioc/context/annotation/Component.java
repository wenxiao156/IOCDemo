package com.cmsz.upay.ioc.context.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 被@Component标记的类相当于一个组件，托管给ioc容器管理<br>
 * spring中的@Component注解详见org.springframework.stereotype.Component
 * @author liyuanchang
 * @date 2018年8月30日下午2:32:38
 * @version V1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Component {
	/**
	 * 相当于&lt;bean name=""&gt;的name，位使用此属性，莫用使用类名，但首字母为小写<br>
	 * eg. 类Apple则默认name为apple，EchoService默认name为echoService
	 * @return
	 */
	String name() default "";
}
