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

import com.google.common.collect.Maps;
import com.jfinal.sog.annotation.Path;
import com.jfinal.sog.annotation.RequestMethod;
import com.jfinal.aop.Interceptor;
import com.jfinal.config.Interceptors;
import com.jfinal.config.Routes;
import com.jfinal.sog.kit.StringPool;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import static com.jfinal.sog.kit.StringPool.SLASH;

/**
 * ActionMapping
 */
final class ActionMapping {
    private final static Logger logger = LoggerFactory.getLogger(ActionMapping.class);

    private final Routes       routes;
    private final Interceptors interceptors;

    private final Map<String, Action> mapping = Maps.newHashMap();

    ActionMapping(Routes routes, Interceptors interceptors) {
        this.routes = routes;
        this.interceptors = interceptors;
    }

    private Set<String> buildExcludedMethodName() {
        Set<String> excludedMethodName = new HashSet<String>();
        Method[] methods = Controller.class.getMethods();
        for (Method m : methods) {
            if (m.getParameterTypes().length == 0)
                excludedMethodName.add(m.getName());
        }
        return excludedMethodName;
    }

    void buildActionMapping() {
        mapping.clear();
        Set<String> excludedMethodName = buildExcludedMethodName();
        InterceptorBuilder interceptorBuilder = new InterceptorBuilder();
        Interceptor[] defaultInters = interceptors.getInterceptorArray();
        interceptorBuilder.addToInterceptorsMap(defaultInters);
        for (Entry<String, Class<? extends Controller>> entry : routes.getEntrySet()) {
            Class<? extends Controller> controllerClass = entry.getValue();
            Interceptor[] controllerInters = interceptorBuilder.buildControllerInterceptors(controllerClass);
//            Method[] methods = controllerClass.getMethods();
            // sogyf : only current controller method.
            final Method[] methods = controllerClass.getDeclaredMethods();
            // end sogyf : only current controller method.

            for (Method method : methods) {
                // sogyf: filter public method.
                if (!Modifier.isPublic(method.getModifiers())) {
                    return;
                }
                // end sogyf: filter public method.

                String methodName = method.getName();
                if (!excludedMethodName.contains(methodName) /*&& method.getParameterTypes().length == 0*/) {
                    Interceptor[] methodInters = interceptorBuilder.buildMethodInterceptors(method);
                    Interceptor[] actionInters = interceptorBuilder.buildActionInterceptors(defaultInters, controllerInters, controllerClass, methodInters, method);
                    String controllerKey = entry.getKey();


                    Path ak = method.getAnnotation(Path.class);
                    if (ak != null) {
                        String actionKey = ak.value().trim();
                        if (StringUtils.isBlank(actionKey))
                            throw new IllegalArgumentException(controllerClass.getName() + StringPool.DOT + methodName + "(): The argument of ActionKey can not be blank.");

                        if (!actionKey.startsWith(SLASH))
                            actionKey = SLASH + actionKey;

                        if (mapping.containsKey(actionKey)) {
                            warnning(actionKey, controllerClass, method);
                            continue;
                        }

                        final Action action = new Action(controllerKey, actionKey, controllerClass, method, methodName, actionInters, routes.getViewPath(controllerKey), ak.method());
                        if(logger.isDebugEnabled()){
                            logger.debug("The JFinal Action {} init...", actionKey);
                        }
                        mapping.put(actionKey, action);
                    } else if (methodName.equals("index")) {

                        Action action = new Action(controllerKey, controllerKey, controllerClass, method, methodName, actionInters, routes.getViewPath(controllerKey), RequestMethod.ALL);
                        if(logger.isDebugEnabled()){
                            logger.debug("The JFinal Action {} init...", controllerKey);
                        }
                        action = mapping.put(controllerKey, action);

                        if (action != null) {
                            warnning(action.getActionKey(), action.getControllerClass(), action.getMethod());
                        }
                    } else {
                        String actionKey = controllerKey.equals(SLASH) ? SLASH + methodName : controllerKey + SLASH + methodName;

                        if (mapping.containsKey(actionKey)) {
                            warnning(actionKey, controllerClass, method);
                            continue;
                        }

                        final Action action = new Action(controllerKey, actionKey, controllerClass, method, methodName, actionInters, routes.getViewPath(controllerKey), RequestMethod.ALL);
                        if(logger.isDebugEnabled()){
                            logger.debug("The JFinal Action {} init...", actionKey);
                        }
                        mapping.put(actionKey, action);
                    }
                }
            }
        }

        // support url = controllerKey + urlParas with "/" of controllerKey
        Action actoin = mapping.get(SLASH);
        if (actoin != null)
            mapping.put(StringPool.EMPTY, actoin);
    }

    private static void warnning(String actionKey, Class<? extends Controller> controllerClass, Method method) {
        System.out.println("--------------------------------------------------------------------------------\nWarnning!!!\n"
                + "ActionKey already used: \"" + actionKey + "\" \n" + "Action can not be mapped: \""
                + controllerClass.getName() + StringPool.DOT + method.getName() + "()\" \n"
                + "--------------------------------------------------------------------------------");
    }

    /**
     * Support four types of url
     * 1: http://abc.com/controllerKey                 ---> 00
     * 2: http://abc.com/controllerKey/para            ---> 01
     * 3: http://abc.com/controllerKey/method          ---> 10
     * 4: http://abc.com/controllerKey/method/para     ---> 11
     */
    Action getAction(String url, String[] urlPara) {
        Action action = mapping.get(url);
        if (action != null) {
            return action;
        }

        // --------
        int i = url.lastIndexOf(SLASH);
        if (i != -1) {
            action = mapping.get(url.substring(0, i));
            urlPara[0] = url.substring(i + 1);
        }

        return action;
    }

    List<String> getAllActionKeys() {
        List<String> allActionKeys = new ArrayList<String>(mapping.keySet());
        Collections.sort(allActionKeys);
        return allActionKeys;
    }
}





