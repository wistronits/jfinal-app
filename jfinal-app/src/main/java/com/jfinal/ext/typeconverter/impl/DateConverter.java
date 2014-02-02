/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package com.jfinal.ext.typeconverter.impl;



import com.jfinal.core.Const;
import com.jfinal.ext.kit.StringPool;
import com.jfinal.ext.typeconverter.TypeConversionException;
import com.jfinal.ext.typeconverter.TypeConverter;
import com.jfinal.kit.StringKit;
import org.joda.time.DateTime;

import java.util.Calendar;
import java.util.Date;

/**
 * Converts given object to <code>java.util.Date</code>.
 * Conversion rules:
 * <ul>
 * <li><code>null</code> value is returned as <code>null</code></li>
 * <li>object of destination type is simply casted</li>
 * <li><code>Calendar</code> object is converted</li>
 * <li><code>JDateTime</code> object is converted</li>
 * <li><code>Number</code> is used as number of milliseconds</li>
 * <li>finally, if string value contains only numbers it is parsed as milliseconds;
 * otherwise as JDateTime pattern</li>
 * </ul>
 */
public class DateConverter implements TypeConverter<Date> {


    public Date convert(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Date) {
            return (Date) value;
        }
        if (value instanceof Calendar) {
            return new Date(((Calendar) value).getTimeInMillis());
        }
        if (value instanceof DateTime) {
            return ((DateTime) value).toDate();
        }
        if (value instanceof Number) {
            return new Date(((Number) value).longValue());
        }

        String stringValue = value.toString().trim();

        if (!StringKit.containsOnlyDigits(stringValue)) {
            // try to parse default string format
            return DateTime.parse(stringValue, Const.DATE_TIME_FORMATTER).toDate();
        }

        try {
            long milliseconds = Long.parseLong(stringValue);
            return new Date(milliseconds);
        } catch (NumberFormatException nfex) {
            throw new TypeConversionException(value, nfex);
        }
    }

}
