/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package com.jfinal.ext.typeconverter.impl;


import com.jfinal.ext.kit.LocaleKit;
import com.jfinal.ext.typeconverter.TypeConverter;

import java.util.Locale;

/**
 * Converts given object to Java <code>Locale</code>.
 * <ul>
 * <li><code>null</code> value is returned as <code>null</code></li>
 * <li>object of destination type is simply casted</li>
 * <li>finally, string representation of the object is used for getting the locale</li>
 * </ul>
 */
public class LocaleConverter implements TypeConverter<Locale> {

	public Locale convert(Object value) {
		if (value == null) {
			return null;
		}

		if (value.getClass() == Locale.class) {
			return (Locale) value;
		}

		return LocaleKit.getLocale(value.toString());
	}

}
