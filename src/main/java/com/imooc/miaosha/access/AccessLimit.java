package com.imooc.miaosha.access;


import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Retention(RUNTIME)
@Target({METHOD})
public @interface AccessLimit {
	int seconds();
	int maxCount();
	boolean needLogin() default true;
}
