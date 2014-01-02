/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package com.jfinal.config;

import com.jfinal.kit.PathKit;

import javax.servlet.*;
import java.io.*;
import java.util.EnumSet;
import java.util.Properties;
import java.util.Set;

/**
 * <p>
 * 通过Servlet 3.0 的动态加载方式加载JFinal，免去Web.xml的配置.
 * </p>
 *
 * @author sagyf yang
 * @version 1.0 2014-01-02 15:06
 * @since JDK 1.6
 */
public class JFinalApplicationInitializer implements ServletContainerInitializer {

    private static final String APPLICATION_PROP = "application.properties";

    @Override
    public void onStartup(Set<Class<?>> classSet, ServletContext ctx) throws ServletException {
        System.out.println("start initializer Application...");


        InputStream inputStream = null;

        String fullFile = PathKit.getWebRootPath() + File.separator + "WEB-INF" + File.separator + APPLICATION_PROP;
        try {
            inputStream = new FileInputStream(new File(fullFile));
            Properties p = new Properties();
            p.load(inputStream);
            boolean security = Boolean.getBoolean(p.getProperty("security", "false"));
            if (security) {
                ctx.addListener("org.apache.shiro.web.env.EnvironmentLoaderListener");
                ctx.addFilter("ShiroFilter", "org.apache.shiro.web.servlet.ShiroFilter")
                        .addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
            }
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Properties file not found: " + fullFile);
        } catch (IOException e) {
            throw new IllegalArgumentException("Properties file can not be loading: " + fullFile);
        } finally {
            try {
                if (inputStream != null) inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        FilterRegistration.Dynamic jfinalFilter = ctx.addFilter("jfinal@app", "com.jfinal.core.JFinalFilter");

        jfinalFilter.setInitParameter("configClass", "com.jfinal.config.AppConfig");
        jfinalFilter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");

        System.out.println("end initializer Application...");
    }
}
