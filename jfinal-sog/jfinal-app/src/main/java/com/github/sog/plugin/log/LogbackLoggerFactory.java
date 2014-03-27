/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package com.github.sog.plugin.log;

import com.jfinal.log.ILoggerFactory;
import com.jfinal.log.Logger;

/**
 * <p>
 * .
 * </p>
 *
 * @author walter yang
 * @version 1.0 2013-10-23 10:41 PM
 * @since JDK 1.5
 */
public class LogbackLoggerFactory implements ILoggerFactory {
    @Override
    public Logger getLogger(Class<?> clazz) {
        return new Slf4jLogger(clazz);
    }

    @Override
    public Logger getLogger(String name) {
        return new Slf4jLogger(name);
    }
}
