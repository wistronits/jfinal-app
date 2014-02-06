/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package com.jfinal.sog.typeconverter.impl;


import com.jfinal.core.Const;
import com.jfinal.sog.typeconverter.TypeConversionException;
import com.jfinal.sog.typeconverter.TypeConverter;
import com.jfinal.kit.StringKit;
import org.joda.time.DateTime;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Converts given object to <code>Byte</code>.
 * Conversion rules:
 * <ul>
 * <li><code>null</code> value is returned as <code>null</code></li>
 * <li>object of destination type is simply casted</li>
 * <li><code>Date</code> object is converted</li>
 * <li><code>JDateTime</code> object is converted</li>
 * <li><code>Number</code> is used as number of milliseconds</li>
 * <li>finally, if string value contains only numbers it is parsed as milliseconds;
 * otherwise as JDateTime pattern</li>
 * </ul>
 */
public class CalendarConverter implements TypeConverter<Calendar> {

    public Calendar convert(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Calendar) {
            return (Calendar) value;
        }
        if (value instanceof Date) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime((Date) value);
            return calendar;
        }
        if (value instanceof DateTime) {
            return ((DateTime) value).toCalendar(Locale.getDefault());
        }

        if (value instanceof Number) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(((Number) value).longValue());
            return calendar;
        }

        String stringValue = value.toString().trim();

        if (!StringKit.containsOnlyDigits(stringValue)) {
            // try to parse default string format
            return DateTime.parse(stringValue, Const.DATE_TIME_FORMATTER).toCalendar(Locale.getDefault());
        }

        try {
            long milliseconds = Long.parseLong(stringValue);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(milliseconds);
            return calendar;
        } catch (NumberFormatException nfex) {
            throw new TypeConversionException(value, nfex);
        }
    }

}
