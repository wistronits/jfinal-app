/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package com.github.sog.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 配置Context,NAME_SAPCE参数拦截器.
 * </p>
 *
 * @author walter yang
 * @version 1.0 2013-12-09 22:10
 * @since JDK 1.5
 */
public class ContextInterceptor implements Interceptor {
    private static final String contextPathKey = "ctx";
//    private static final  String namespaceKey = "NAME_SPACE";


    @Override
    public void intercept(ActionInvocation ai) {
        ai.invoke();
        HttpServletRequest request = ai.getController().getRequest();
        request.setAttribute(contextPathKey, request.getContextPath());
//        request.setAttribute(namespaceKey, ai.getControllerKey());
    }

}
