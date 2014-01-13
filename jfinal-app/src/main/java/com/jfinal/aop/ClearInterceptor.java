

package com.jfinal.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ClearInterceptor is used to clear interceptors of different level.
 * It clear the upper layer interceptors by default and clear 
 * all layers with parameter ClearLayer.ALL
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface ClearInterceptor {
	ClearLayer value() default ClearLayer.UPPER;
}



