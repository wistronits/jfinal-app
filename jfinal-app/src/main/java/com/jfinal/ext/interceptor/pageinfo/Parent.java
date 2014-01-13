/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */
package com.jfinal.ext.interceptor.pageinfo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.jfinal.plugin.activerecord.Model;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Parent {
    public Class<? extends Model<?>> model();
    public String foreignKey();
}