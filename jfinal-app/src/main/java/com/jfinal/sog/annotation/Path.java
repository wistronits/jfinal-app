/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package com.jfinal.sog.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * 标注资源类或方法的相对路径.
 * </p>
 *
 * @author sagyf yang
 * @version 1.0 2014-02-24 10:13
 * @since JDK 1.6
 */
@Inherited
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Path {

    /**
     * 对应的url相对路径
     * 可以使用正则表达式
     */
    String value();


    RequestMethod method() default RequestMethod.ALL;

}
