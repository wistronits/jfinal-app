/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */
package com.jfinal.ext.route;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.jfinal.config.Routes;
import com.jfinal.core.Controller;
import com.jfinal.ext.kit.ClassSearcher;
import com.jfinal.kit.StringKit;
import com.jfinal.log.Logger;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;

@SuppressWarnings("UnusedDeclaration")
public class AutoBindRoutes extends Routes {

    private boolean autoScan = true;

    private List<Class<? extends Controller>> excludeClasses = Lists.newArrayList();

    private boolean includeAllJarsInLib;

    private List<String> includeJars = Lists.newArrayList();

    protected final Logger logger = Logger.getLogger(getClass());

    private String suffix = "Controller";

    private final String controller_package;

    public AutoBindRoutes(String controller_package) {
        this.controller_package = controller_package;
    }

    public AutoBindRoutes() {
        this.controller_package = StringUtils.EMPTY;
    }

    public AutoBindRoutes autoScan(boolean autoScan) {
        this.autoScan = autoScan;
        return this;
    }

    public AutoBindRoutes addExcludeClasses(Class<? extends Controller>... clazzes) {
        if (clazzes != null) {
            Collections.addAll(excludeClasses, clazzes);
        }
        return this;
    }

    public AutoBindRoutes addExcludeClasses(List<Class<? extends Controller>> clazzes) {
        excludeClasses.addAll(clazzes);
        return this;
    }

    public AutoBindRoutes addJars(String... jars) {
        if (jars != null) {
            Collections.addAll(includeJars, jars);
        }
        return this;
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void config() {
        ClassSearcher searcher = Strings.isNullOrEmpty(controller_package) ? ClassSearcher.of(Controller.class)
                : ClassSearcher.of(Controller.class, controller_package);
        List<Class<? extends Controller>> controllerClasses = searcher
                .includeAllJarsInLib(includeAllJarsInLib).injars(includeJars).search();
        ControllerBind controllerBind;
        for (Class controller : controllerClasses) {
            if (excludeClasses.contains(controller)) {
                continue;
            }
            controllerBind = (ControllerBind) controller.getAnnotation(ControllerBind.class);
            if (controllerBind == null) {
                if (!autoScan) {
                    continue;
                }
                this.add(controllerKey(controller), controller);
                logger.debug("routes.add(" + controllerKey(controller) + ", " + controller.getName() + ")");
            } else if (StringKit.isBlank(controllerBind.viewPath())) {
                this.add(controllerBind.controllerKey(), controller);
                logger.debug("routes.add(" + controllerBind.controllerKey() + ", " + controller.getName() + ")");
            } else {
                this.add(controllerBind.controllerKey(), controller, controllerBind.viewPath());
                logger.debug("routes.add(" + controllerBind.controllerKey() + ", " + controller + ","
                        + controllerBind.viewPath() + ")");
            }
        }
    }

    private String controllerKey(Class<Controller> clazz) {
        Preconditions.checkArgument(clazz.getSimpleName().endsWith(suffix),
                " does not has a @ControllerBind annotation and it's name is not end with " + suffix);
        String controllerKey = "/" + StringKit.firstCharToLowerCase(clazz.getSimpleName());
        controllerKey = controllerKey.substring(0, controllerKey.indexOf(suffix));
        return controllerKey;
    }

    public AutoBindRoutes includeAllJarsInLib(boolean includeAllJarsInLib) {
        this.includeAllJarsInLib = includeAllJarsInLib;
        return this;
    }

    public AutoBindRoutes suffix(String suffix) {
        this.suffix = suffix;
        return this;
    }

}
