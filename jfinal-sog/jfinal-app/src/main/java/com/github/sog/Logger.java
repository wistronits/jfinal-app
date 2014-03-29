/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package com.github.sog;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.status.OnConsoleStatusListener;
import ch.qos.logback.core.status.StatusManager;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * .
 * </p>
 *
 * @author sagyf yang
 * @version 1.0 2014-03-28 22:51
 * @since JDK 1.6
 */
public class Logger {

    private static org.slf4j.Logger logger;

    public void init(){
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        StatusManager statusManager = lc.getStatusManager();
        OnConsoleStatusListener onConsoleListener = new OnConsoleStatusListener();
        statusManager.add(onConsoleListener);

        logger = LoggerFactory.getLogger("jfinal-app");

    }
}
