/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package com.jfinal.initalizer;

import com.google.common.io.InputSupplier;
import com.google.common.io.Resources;
import org.apache.shiro.util.StringUtils;

import javax.servlet.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
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


    @Override
    public void onStartup(Set<Class<?>> classSet, ServletContext ctx)
            throws ServletException {
        URL url = Resources.getResource(AppConfig.APPLICATION_PROP);
        if (url == null) {
            throw new IllegalArgumentException("Parameter of file can not be blank");
        }

        String app_name;

        InputSupplier<InputStream> inputSupplier = Resources.newInputStreamSupplier(url);
        try {
            Properties p = new Properties();
            p.load(inputSupplier.getInput());
            boolean security = Boolean.getBoolean(p.getProperty("security", "false"));
            if (security) {
                ctx.addListener("org.apache.shiro.web.env.EnvironmentLoaderListener");
                ctx.addFilter("ShiroFilter", "org.apache.shiro.web.servlet.ShiroFilter")
                        .addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
            }
            app_name = p.getProperty("app", StringUtils.EMPTY_STRING);
        } catch (IOException e) {
            throw new IllegalArgumentException("Properties file can not be loading: " + url);
        }

        FilterRegistration.Dynamic jfinalFilter = ctx.addFilter("jfinal@app", "com.jfinal.core.JFinalFilter");

        jfinalFilter.setInitParameter("configClass", "com.jfinal.config.AppConfig");
        jfinalFilter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
        // 支持异步请求处理
        jfinalFilter.setAsyncSupported(true);



        System.out.println("initializer " + app_name + " Application ok!");
    }
}
