/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package com.jfinal.render;

import com.jfinal.config.Routes;
import com.jfinal.core.Const;
import com.jfinal.sog.initalizer.AppConfig;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;

/**
 * Render.
 */
public abstract class Render implements Serializable {

    private static final long serialVersionUID = 4055676662365675029L;
    protected String view;
    protected transient HttpServletRequest request;
    protected transient HttpServletResponse response;

    private transient static String encoding = Const.DEFAULT_ENCODING;
    private transient static boolean devMode;


    static void init(String encoding, boolean devMode) {
        Render.encoding = encoding;
        Render.devMode = devMode;
    }

    public static String getEncoding() {
        return encoding;
    }

    public static boolean getDevMode() {
        return devMode;
    }

    public final Render setContext(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
        return this;
    }

    public final Render setContext(HttpServletRequest request, HttpServletResponse response, String viewPath) {
        this.request = request;
        this.response = response;
        // sog modify by 2013-01-13 15:15
        if( view != null ){
            if(!view.startsWith(Routes.PREFIX)){
                view = viewPath + view;
            } else if (AppConfig.isSetViewPath() && view.startsWith(Routes.PREFIX)) {
                view = AppConfig.getBaseViewPath() + view.replaceFirst(Routes.PREFIX, StringUtils.EMPTY);
            }
        }

        return this;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    /**
     * Render to client
     */
    public abstract void render();
}
