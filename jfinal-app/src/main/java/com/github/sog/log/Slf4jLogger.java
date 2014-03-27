/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package com.github.sog.log;

import com.jfinal.log.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * .
 * </p>
 *
 * @author walter yang
 * @version 1.0 2013-10-23 10:43 PM
 * @since JDK 1.5
 */
public class Slf4jLogger extends Logger {


    private static final String callerFQCN = Slf4jLogger.class.getName();
    private org.slf4j.Logger log;

    Slf4jLogger(Class<?> clazz) {
        log = LoggerFactory.getLogger(clazz);
    }

    Slf4jLogger(String name) {
        log = LoggerFactory.getLogger(name);
    }

    public void info(String message) {
        log.info(message);
    }

    public void info(String message, Throwable t) {
        log.info(message, t);
    }

    public void debug(String message) {
        log.debug(message);
    }

    public void debug(String message, Throwable t) {
        log.debug(message, t);
    }

    public void warn(String message) {
        log.warn(message);
    }

    public void warn(String message, Throwable t) {
        log.warn(message, t);
    }

    public void error(String message) {
        log.error(message);
    }

    public void error(String message, Throwable t) {
        log.error(message, t);
    }

    public void fatal(String message) {
        log.trace(message);
    }

    public void fatal(String message, Throwable t) {
        log.trace(message, t);
    }

    public boolean isDebugEnabled() {
        return log.isDebugEnabled();
    }

    public boolean isInfoEnabled() {
        return log.isInfoEnabled();
    }

    public boolean isWarnEnabled() {
        return log.isWarnEnabled();
    }

    public boolean isErrorEnabled() {
        return log.isErrorEnabled();
    }

    public boolean isFatalEnabled() {
        return log.isTraceEnabled();
    }
}
