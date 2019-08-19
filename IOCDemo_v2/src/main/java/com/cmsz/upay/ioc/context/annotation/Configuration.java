package com.cmsz.upay.ioc.context.annotation;

import java.lang.annotation.*;

/**
 * @Configuration表明类是配置类
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Configuration {

}
