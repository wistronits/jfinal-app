/**
 * Copyright (c) 2011-2013, James Zhan 詹波 (jfinal@126.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jfinal.core;

import com.jfinal.sog.action.ActionParam;
import com.jfinal.sog.annotation.RequestMethod;
import com.jfinal.aop.Interceptor;
import com.jfinal.reflectasm.MethodAccess;
import org.apache.commons.collections.CollectionUtils;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Action
 */
class Action {

    /**
     * The Controller Calss .
     */
    private final Class<? extends Controller> controllerClass;
    /**
     * The Controller Key.
     */
    private final String                      controllerKey;
    /**
     * The Action Key.
     */
    private final String                      actionKey;
    /**
     * The Action Method.
     */
    private final Method                      method;
    /**
     * The Action method name.
     */
    private final String                      methodName;
    /**
     * The Action used intercetor.
     */
    private final Interceptor[]               interceptors;
    /**
     * The View Path.
     */
    private final String                      viewPath;


    /**
     * The Param list.
     */
    private final List<ActionParam> paramList;
    /**
     * The Reflect asm method access.
     */
    private final MethodAccess      methodAccess;
    /**
     * The parameter types.
     */
    private final Class<?>[]        parameterTypes;
    /**
     * The path http method.
     */
    private final RequestMethod     pathMethod;


    public Action(final String controllerKey, final String actionKey
            , final Class<? extends Controller> controllerClass
            , final Method method, final String methodName
            , final Interceptor[] interceptors
            , final String viewPath
            , final RequestMethod pathMethod) {
        this.controllerKey = controllerKey;
        this.actionKey = actionKey;
        this.controllerClass = controllerClass;
        this.pathMethod = pathMethod;
        this.methodAccess = MethodAccess.get(this.controllerClass);
        this.method = method;
        this.methodName = methodName;
        this.parameterTypes = method.getParameterTypes();
        this.paramList = ActionParam.me(this.method, this.parameterTypes);
        this.interceptors = interceptors;
        this.viewPath = viewPath;
    }

    public Class<? extends Controller> getControllerClass() {
        return controllerClass;
    }

    public String getControllerKey() {
        return controllerKey;
    }

    public String getActionKey() {
        return actionKey;
    }

    public Method getMethod() {
        return method;
    }

    public Interceptor[] getInterceptors() {
        return interceptors;
    }

    public String getViewPath() {
        return viewPath;
    }

    public String getMethodName() {
        return methodName;
    }

    public List<ActionParam> getParamList() {
        return paramList;
    }

    public MethodAccess getMethodAccess() {
        return methodAccess;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public RequestMethod getPathMethod() {
        return pathMethod;
    }

    public boolean hasParam() {
        return CollectionUtils.isNotEmpty(this.getParamList());
    }

}









