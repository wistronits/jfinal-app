/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package com.jfinal.aop;

import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;

import java.lang.reflect.Method;

/**
 * ActionInvocationWrapper invoke the InterceptorStack.
 */
class ActionInvocationWrapper extends ActionInvocation {

    private final Interceptor[] inters;
    private final ActionInvocation actionInvocation;
    private int index = 0;

    ActionInvocationWrapper(ActionInvocation actionInvocation, Interceptor[] inters) {
        this.actionInvocation = actionInvocation;
        this.inters = inters;
    }

    /**
     * Invoke the action
     */
    @Override
    public final void invoke() {
        if (index < inters.length)
            inters[index++].intercept(this);
        else
            actionInvocation.invoke();
    }

    @Override
    public Controller getController() {
        return actionInvocation.getController();
    }

    @Override
    public String getActionKey() {
        return actionInvocation.getActionKey();
    }

    @Override
    public String getControllerKey() {
        return actionInvocation.getControllerKey();
    }

    @Override
    public Method getMethod() {
        return actionInvocation.getMethod();
    }

    @Override
    public String getMethodName() {
        return actionInvocation.getMethodName();
    }

    /**
     * Return view path of this controller
     */
    @Override
    public String getViewPath() {
        return actionInvocation.getViewPath();
    }

	/*
	 * It should be added method below when com.jfinal.core.ActionInvocation add method, otherwise null will be returned.
	 */
}







