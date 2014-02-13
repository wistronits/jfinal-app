/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package org.sog.cli;

import java.io.File;

/**
 * <p>
 * .
 * </p>
 *
 * @author sagyf yang
 * @version 1.0 2014-02-10 15:14
 * @since JDK 1.6
 */
public class LayoutFile {

    private final String java_path;
    private final String resource_path;
    private final String webapp_path;

    private final String main_path;


    public LayoutFile(String main_path) {
        this.main_path = main_path;
        java_path = main_path + "java" + File.separator;
        resource_path = main_path + "resources" + File.separator;
        webapp_path = main_path + "webapp" + File.separator;
    }

    public String getJava_path() {
        return java_path;
    }

    public String getResource_path() {
        return resource_path;
    }

    public String getWebapp_path() {
        return webapp_path;
    }

    public String getMain_path() {
        return main_path;
    }
}
