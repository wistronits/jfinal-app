/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package com.jfinal.ext.typeconverter;

import com.jfinal.ext.kit.ExceptionKit;

import java.io.PrintStream;
import java.io.PrintWriter;

import static com.jfinal.ext.kit.ExceptionKit.getRootCause;

/**
 * <p>
 * .
 * </p>
 *
 * @author sagyf yang
 * @version 1.0 2014-01-31 17:47
 * @since JDK 1.6
 */
public class UncheckedException extends RuntimeException {

    private static final long serialVersionUID = -422781015326888620L;
    protected final Throwable cause;

    /**
     * Divider between causes printouts.
     */
    protected static final String CAUSE_DIV = "---[cause]------------------------------------------------------------------------";

    /**
     * If set to <code>true</code> stack trace will be enhanced with cause's stack traces.
     */
    protected final boolean showCauseDetails;

    // ---------------------------------------------------------------- constructors

    public UncheckedException(Throwable t) {
        super(t.getMessage());
        cause = t;
        this.showCauseDetails = true;
    }

    public UncheckedException(Throwable t, boolean showCauseDetails) {
        super(t.getMessage());
        cause = t;
        this.showCauseDetails = showCauseDetails;
    }

    public UncheckedException() {
        super();
        cause = null;
        this.showCauseDetails = false;
    }

    public UncheckedException(String message) {
        super(message);
        cause = null;
        this.showCauseDetails = false;
    }

    public UncheckedException(String message, Throwable t) {
        super(message, t);
        cause = t;
        this.showCauseDetails = true;
    }

    public UncheckedException(String message, Throwable t, boolean showCauseDetails) {
        super(message, t);
        cause = t;
        this.showCauseDetails = showCauseDetails;
    }

    // ---------------------------------------------------------------- stack trace

    @Override
    public void printStackTrace() {
        printStackTrace(System.err);
    }

    @Override
    public void printStackTrace(PrintStream ps) {
        synchronized (ps) {
            super.printStackTrace(ps);
            if ((cause != null) && showCauseDetails) {
                Throwable rootCause = getRootCause(cause);
                ps.println(CAUSE_DIV);
                rootCause.printStackTrace(ps);
            }
        }
    }

    @Override
    public void printStackTrace(PrintWriter pw) {
        synchronized (pw) {
            super.printStackTrace(pw);
            if ((cause != null) && showCauseDetails) {
                Throwable rootCause = getRootCause(cause);
                pw.println(CAUSE_DIV);
                rootCause.printStackTrace(pw);
            }
        }
    }

    // ---------------------------------------------------------------- txt

    /**
     * Returns the detail message, including the message from the nested exception if there is one.
     */
    @Override
    public String getMessage() {
        return ExceptionKit.buildMessage(super.getMessage(), cause);
    }

    // ---------------------------------------------------------------- wrap

    /**
     * Wraps checked exceptions in a <code>UncheckedException</code>.
     * Unchecked exceptions are not wrapped.
     */
    public static RuntimeException wrapChecked(Throwable t) {
        if (t instanceof RuntimeException) {
            return (RuntimeException) t;
        }
        return new UncheckedException(t);
    }

    /**
     * Wraps all exceptions in a <code>UncheckedException</code>
     */
    public static RuntimeException wrap(Throwable t) {
        return new UncheckedException(t);
    }

    /**
     * Wraps all exceptions in a <code>UncheckedException</code>
     */
    public static RuntimeException wrap(Throwable t, String message) {
        return new UncheckedException(message, t);
    }


    // ---------------------------------------------------------------- cause

    /**
     * Re-throws cause if exists.
     */
    public void rethrow() throws Throwable {
        if (cause == null) {
            return;
        }
        throw cause;
    }

    /**
     * Returns exception cause.
     */
    @Override
    public Throwable getCause() {
        return cause;
    }

}
