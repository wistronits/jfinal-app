/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package com.github.sog.exceptions;

import java.util.concurrent.atomic.AtomicLong;

/**
 * The super class for all Play! exceptions
 */
public abstract class JFinalAppException extends RuntimeException {

    static AtomicLong atomicLong = new AtomicLong(System.currentTimeMillis());
    String id;

    public JFinalAppException() {
        super();
        setId();
    }

    public JFinalAppException(String message) {
        super(message);
        setId();
    }

    public JFinalAppException(String message, Throwable cause) {
        super(message, cause);
        setId();
    }

    void setId() {
        long nid = atomicLong.incrementAndGet();
        id = Long.toString(nid, 26);
    }

    public abstract String getErrorTitle();

    public abstract String getErrorDescription();


    public Integer getLineNumber() {
        return -1;
    }

    public String getSourceFile() {
        return "";
    }

    public String getId() {
        return id;
    }


    public String getMoreHTML() {
        return null;
    }
}