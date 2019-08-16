package com.cmsz.upay.ioc.context.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 扫描指定包下面的被@Componenet标记的类，交由ioc容器托管
 * @author liyuanchang
 * @date 2018年8月30日下午2:44:41
 * @version V1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface ComponentScan {
	String[] value() default {};
}
