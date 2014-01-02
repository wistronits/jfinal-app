/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package com.jfinal.config.interceptors;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.ext.plugin.sqlinxml.SqlKit;

/**
 * <p>
 * 为方便开发，增加刷新SqlInXml机制得拦截器，当启动开发模式并且启动了数据库得SqlInXml时，该拦截器启用.
 * </p>
 *
 * @author sagyf yang
 * @version 1.0 2013-12-16 17:14
 * @since JDK 1.6
 */
public class SqlInXmlInterceptor implements Interceptor {
    @Override
    public void intercept(ActionInvocation ai) {
        SqlKit.reload();
        ai.invoke();
    }
}
