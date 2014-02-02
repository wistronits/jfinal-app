/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package com.jfinal.ext.typeconverter;


/**
 * Type conversion exception.
 */
public class TypeConversionException extends UncheckedException {

    private static final long serialVersionUID = -3125784017562373524L;

    public TypeConversionException(Throwable t) {
		super(t);
	}

	public TypeConversionException(String message) {
		super(message);
	}

	public TypeConversionException(String message, Throwable t) {
		super(message, t);
	}

	public TypeConversionException(Object value) {
		this("Conversion failed: " + value);
	}

	public TypeConversionException(Object value, Throwable t) {
		this("Conversion failed: " + value, t);
	}
}
