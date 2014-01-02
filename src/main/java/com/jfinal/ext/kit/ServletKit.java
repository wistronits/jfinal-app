/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */
package com.jfinal.ext.kit;

import javax.servlet.http.HttpServletRequest;

import com.jfinal.kit.StringKit;

public class ServletKit {
    public static String getIp(HttpServletRequest request) {
        String remoteAddr = request.getHeader("X-Real-IP");
        if (remoteAddr == null) {
            remoteAddr = request.getHeader("X-Forwarded-For");
        }
        if (remoteAddr == null) {
            remoteAddr = request.getRemoteAddr();
        }
        return remoteAddr;
    }

    public static String getUrl(HttpServletRequest request) {
        String url = request.getRequestURL().toString();
        String parmas = request.getQueryString();
        if (StringKit.notBlank(parmas)) {
            url = url + "?" + parmas;
        }
        return url;
    }
}
