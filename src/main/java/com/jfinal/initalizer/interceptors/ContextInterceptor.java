/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package com.jfinal.initalizer.interceptors;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.kit.StringKit;

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
    private String contextPathKey = "ctx";
    private String namespaceKey = "NAME_SPACE";

    public ContextInterceptor() {
    }

    public ContextInterceptor(String base) {
        if (StringKit.isBlank(base)) {
            throw new IllegalArgumentException(
                    "contextPathKey can not be blank.");
        }
        this.contextPathKey = base;
    }

    public ContextInterceptor(String base, String namespace) {
        if (!StringKit.notBlank(base, namespace)) {
            throw new IllegalArgumentException(
                    "contextPathKey can not be blank.");
        }
        this.contextPathKey = base;
        this.namespaceKey = namespace;
    }

    @Override
    public void intercept(ActionInvocation ai) {
        ai.invoke();
        HttpServletRequest request = ai.getController().getRequest();
        request.setAttribute(contextPathKey, request.getContextPath());
        request.setAttribute(namespaceKey, ai.getControllerKey());
    }

}
