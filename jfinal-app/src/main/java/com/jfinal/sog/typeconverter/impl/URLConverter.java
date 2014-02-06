/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package com.jfinal.sog.typeconverter.impl;


import com.jfinal.sog.typeconverter.TypeConversionException;
import com.jfinal.sog.typeconverter.TypeConverter;
import com.jfinal.sog.kit.FileKit;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.net.MalformedURLException;

/**
 * Converts given object to <code>URL</code>.
 * Conversion rules:
 * <ul>
 * <li><code>null</code> value is returned as <code>null</code></li>
 * <li>object of destination type is simply casted</li>
 * <li><code>File</code> is converted</li>
 * <li><code>URI</code> is converted</li>
 * <li><code>String</code> representation is used for creating URL</li>
 * </ul>
 */
public class URLConverter implements TypeConverter<URL> {

	public URL convert(Object value) {
		if (value == null) {
			return null;
		}
		
		if (value instanceof URL) {
			return (URL) value;
		}

		if (value instanceof File) {
			File file = (File) value;
			try {
				return FileKit.toURL(file);
			} catch (MalformedURLException muex) {
				throw new TypeConversionException(value, muex);
			}
		}

		if (value instanceof URI) {
			URI uri = (URI) value;
			try {
				return uri.toURL();
			} catch (MalformedURLException muex) {
				throw new TypeConversionException(value, muex);
			}
		}

		try {
			return new URL(value.toString());
		} catch (MalformedURLException muex) {
			throw new TypeConversionException(value, muex);
		}
	}
}