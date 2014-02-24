/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package com.jfinal.sog.annotation;

import com.jfinal.sog.kit.StringPool;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * </p>
 *
 * @author sagyf yang
 * @version 1.0 2014-02-24 10:23
 * @since JDK 1.6
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Param {


    String name() default StringPool.EMPTY;

    /**
     * is json param.
     *
     * @return ture convert json type.
     */
    boolean json() default false;

}
