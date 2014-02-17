/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2013 sagyf Yang. The Four Group.
 */

package com.sagyf.jfinal;

import com.intellij.codeInspection.InspectionToolProvider;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.components.ServiceManager;
import org.jetbrains.annotations.NotNull;

/**
 * <p>
 * .
 * </p>
 *
 * @author sagyf yang
 * @version 1.0 2013-12-29 21:17
 * @since JDK 1.6
 */
public class JFinalApplicationComponent implements ApplicationComponent, InspectionToolProvider {

    public JFinalApplicationComponent() {
    }

    @Override
    public void initComponent() {

    }

    @Override
    public void disposeComponent() {

    }

    @Override
    public Class[] getInspectionClasses() {
        return new Class[0];
    }

    @NotNull
    @Override
    public String getComponentName() {
        return "jfinalApplicationComponent";
    }

    public static JFinalApplicationComponent get() {
        return ServiceManager.getService(JFinalApplicationComponent.class);
    }
}
