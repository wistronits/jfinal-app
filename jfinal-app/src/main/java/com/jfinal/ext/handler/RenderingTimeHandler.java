/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */
package com.jfinal.ext.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jfinal.handler.Handler;
import com.jfinal.log.Logger;

/**
 * <a href = http://www.oschina.net/question/173052_62229 />
 */
public class RenderingTimeHandler extends Handler {

    protected final Logger logger = Logger.getLogger(getClass());

    @Override
    public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
        long start = System.currentTimeMillis();
        nextHandler.handle(target, request, response, isHandled);
        long end = System.currentTimeMillis();
        logger.debug("rending time:" + (end - start) + "ms");
    }

}
