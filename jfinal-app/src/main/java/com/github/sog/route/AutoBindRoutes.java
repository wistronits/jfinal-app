/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */
package com.github.sog.route;

import com.google.common.base.Preconditions;
import com.jfinal.config.Routes;
import com.jfinal.core.Controller;
import com.github.sog.initalizer.ctxbox.ClassBox;
import com.github.sog.initalizer.ctxbox.ClassType;
import com.github.sog.config.StringPool;
import com.jfinal.kit.StringKit;
import com.jfinal.log.Logger;

import java.util.List;

@SuppressWarnings("UnusedDeclaration")
public class AutoBindRoutes extends Routes {

    private boolean autoScan = true;

    protected final Logger logger = Logger.getLogger(getClass());

    private String suffix = "Controller";


    public AutoBindRoutes() {
    }

    public AutoBindRoutes autoScan(boolean autoScan) {
        this.autoScan = autoScan;
        return this;
    }


    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void config() {
        List<Class> controllerClasses = ClassBox.getInstance().getClasses(ClassType.CONTROLLER);
        if (controllerClasses != null && !controllerClasses.isEmpty()) {
            ControllerBind controllerBind;
            for (Class controller : controllerClasses) {
                controllerBind = (ControllerBind) controller.getAnnotation(ControllerBind.class);
                if (controllerBind == null) {
                    if (!autoScan) {
                        continue;
                    }
                    this.add(controllerKey(controller), controller);
                    logger.debug("routes.add(" + controllerKey(controller) + ", " + controller.getName() + StringPool.RIGHT_BRACKET);
                } else if (StringKit.isBlank(controllerBind.viewPath())) {
                    this.add(controllerBind.controllerKey(), controller);
                    logger.debug("routes.add(" + controllerBind.controllerKey() + ", " + controller.getName() + StringPool.RIGHT_BRACKET);
                } else {
                    this.add(controllerBind.controllerKey(), controller, controllerBind.viewPath());
                    logger.debug("routes.add(" + controllerBind.controllerKey() + ", " + controller + StringPool.COMMA
                            + controllerBind.viewPath() + StringPool.RIGHT_BRACKET);
                }
            }
        }
    }

    private String controllerKey(Class<Controller> clazz) {
        Preconditions.checkArgument(clazz.getSimpleName().endsWith(suffix),
                " does not has a @ControllerBind annotation and it's name is not end with " + suffix);
        String controllerKey = StringPool.SLASH + StringKit.firstCharToLowerCase(clazz.getSimpleName());
        controllerKey = controllerKey.substring(0, controllerKey.indexOf(suffix));
        return controllerKey;
    }

    public AutoBindRoutes suffix(String suffix) {
        this.suffix = suffix;
        return this;
    }

}
