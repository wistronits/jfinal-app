/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */
package com.github.sog.interceptor.syslog;

import java.util.Map;

import com.jfinal.core.Controller;

/**
 * 日志处理器
 */
public interface LogProcessor {

    void process(SysLog sysLog);

    String getUsername(Controller c);

    String formatMessage(final LogConfig config, Map<String, String> message);
}
