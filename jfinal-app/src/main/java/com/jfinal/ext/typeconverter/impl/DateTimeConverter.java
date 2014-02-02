/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package com.jfinal.ext.typeconverter.impl;



import com.jfinal.core.Const;
import com.jfinal.ext.typeconverter.TypeConversionException;
import com.jfinal.ext.typeconverter.TypeConverter;
import com.jfinal.kit.StringKit;
import org.joda.time.DateTime;

import java.util.Calendar;
import java.util.Date;

/**
 * Converts object to {@link org.joda.time.DateTime}.
 * Conversion rules:
 * <ul>
 * <li><code>null</code> value is returned as <code>null</code></li>
 * <li>object of destination type is simply casted</li>
 * <li><code>Calendar</code> object is converted</li>
 * <li><code>Date</code> object is converted</li>
 * <li><code>Number</code> is used as number of milliseconds</li>
 * <li>finally, if string value contains only numbers it is parsed as milliseconds;
 * otherwise as JDateTime pattern</li>
 * </ul>
 */
public class DateTimeConverter implements TypeConverter<DateTime> {

	public DateTime convert(Object value) {
		if (value == null) {
			return null;
		}

		if (value instanceof DateTime) {
			return (DateTime) value;
		}
		if (value instanceof Calendar) {
			return new DateTime(value);
		}
		if (value instanceof Date) {
			return new DateTime((Date) value);
		}

		if (value instanceof Number) {
			return new DateTime(((Number) value).longValue());
		}


		String stringValue = value.toString().trim();

		if (!StringKit.containsOnlyDigits(stringValue)) {
           return DateTime.parse(stringValue, Const.DATE_TIME_FORMATTER);
		}
		try {
			long milliseconds = Long.parseLong(stringValue);
			return new DateTime(milliseconds);
		} catch (NumberFormatException nfex) {
			throw new TypeConversionException(value, nfex);
		}
	}

}
