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

import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.primitives.Doubles;
import com.google.common.primitives.Floats;
import com.jfinal.sog.action.ActionParam;
import com.jfinal.sog.annotation.Param;
import com.jfinal.aop.Interceptor;
import com.jfinal.reflectasm.MethodAccess;
import com.jfinal.sog.kit.date.DateProvider;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * ActionInvocation invoke the action
 */
public class ActionInvocation {

    private Controller    controller;
    private Interceptor[] inters;
    private Action        action;
    private int index = 0;

    private static final Object[] NULL_ARGS = new Object[0];    // Prevent new Object[0] by jvm for paras of action invocation.

    // ActionInvocationWrapper need this constructor
    protected ActionInvocation() {

    }

    ActionInvocation(Action action, Controller controller) {
        this.controller = controller;
        this.inters = action.getInterceptors();
        this.action = action;
    }

    /**
     * Invoke the action.
     */
    public void invoke() {


        if (index < inters.length)
            inters[index++].intercept(this);
        else if (index++ == inters.length)    // index++ ensure invoke action only one time
            // try {action.getMethod().invoke(controller, NULL_ARGS);} catch (Exception e) {throw new RuntimeException(e);}
            try {

//                final Method method = action.getMethod();
                final MethodAccess methodAccess = action.getMethodAccess();
                int methodIndex = methodAccess.getIndex(action.getMethodName(), action.getParameterTypes());
                if (action.hasParam()) {
                    List<Object> params = autoParams();
                    methodAccess.invoke(controller, methodIndex, params.toArray());

//                    method.invoke(controller, params.toArray());
                } else {
                    methodAccess.invoke(controller, methodIndex, NULL_ARGS);
//                    method.invoke(controller, NULL_ARGS);
                }

//            } catch (InvocationTargetException e) {
//                Throwable cause = e.getTargetException();
//                if (cause instanceof RuntimeException)
//                    throw (RuntimeException) cause;
//                throw new RuntimeException(e);
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
    }

    private List<Object> autoParams() {
        List<Object> params = Lists.newArrayList();
        for (ActionParam actionParam : action.getParamList()) {
            String name = actionParam.getParamName();
            final Type paramType = actionParam.getParamType();

            final Param param = actionParam.getParam();
            if (param != null) {
                String paramName = param.name();
                if (Strings.isNullOrEmpty(paramName)) {
                    paramName = name;
                }
                String value = controller.getPara(paramName, StringUtils.EMPTY);
                if (param.json()) {
                    params.add(Strings.isNullOrEmpty(value) ? null : JSON.parseObject(value, paramType));
                } else {
                    autoParamBasicTypes(params, paramName, paramType);
                }
            } else {
                autoParamBasicTypes(params, name, paramType);
            }

        }
        return params;
    }

    private void autoParamBasicTypes(List<Object> params, String name, Type paramType) {
        if (paramType == String.class) {
            params.add(controller.getPara(name, StringUtils.EMPTY));
        } else if (paramType == boolean.class || paramType == Boolean.class) {
            params.add(controller.getParaToBoolean(name, false));
        } else if (paramType == int.class || paramType == Integer.class) {
            params.add(controller.getParaToInt(name, 0));
        } else if (paramType == long.class || paramType == Long.class) {
            params.add(controller.getParaToLong(name, 0l));
        } else if (paramType == double.class || paramType == Double.class) {
            String value = controller.getPara(name, StringUtils.EMPTY);
            params.add(Doubles.tryParse(value));
        } else if (paramType == float.class || paramType == Float.class) {
            String value = controller.getPara(name, StringUtils.EMPTY);
            params.add(Floats.tryParse(value));
        } else if (paramType == Number.class) {
            String value = controller.getPara(name, StringUtils.EMPTY);
            params.add(NumberUtils.createNumber(value));
        } else if (paramType == Date.class) {
            String value = controller.getPara(name, StringUtils.EMPTY);
            try {
                params.add(DateUtils.parseDate(value
                        , DateProvider.YYYY_MM_DD_HH_MM_SS
                        , DateProvider.YYYY_MM_DD_HH_MM
                        , DateProvider.YYYY_MM_DD));
            } catch (ParseException e) {
                System.err.println("parse date is error! use current date.");
                params.add(DateProvider.DEFAULT.getCurrentDate());
            }
        }
    }

    /**
     * Return the controller of this action.
     */
    public Controller getController() {
        return controller;
    }

    /**
     * Return the action key.
     * actionKey = controllerKey + methodName
     */
    public String getActionKey() {
        return action.getActionKey();
    }

    /**
     * Return the controller key.
     */
    public String getControllerKey() {
        return action.getControllerKey();
    }

    /**
     * Return the method of this action.
     * <p/>
     * You can getMethod.getAnnotations() to get annotation on action method to do more things
     */
    public Method getMethod() {
        return action.getMethod();
        /*
        try {
			return controller.getClass().getMethod(action.getMethod().getName());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}*/
    }

    /**
     * Return the method name of this action's method.
     */
    public String getMethodName() {
        return action.getMethodName();
    }

    /**
     * Return view path of this controller.
     */
    public String getViewPath() {
        return action.getViewPath();
    }
}
