/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package com.jfinal.ext.typeconverter.impl;


import com.jfinal.ext.typeconverter.TypeConversionException;
import com.jfinal.ext.typeconverter.TypeConverter;
import com.jfinal.kit.StringKit;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

/**
 * Converts given object to <code>java.sql.Time</code>.
 * Conversion rules:
 * <ul>
 * <li><code>null</code> value is returned as <code>null</code></li>
 * <li>object of destination type is simply casted</li>
 * <li><code>Calendar</code> object is converted</li>
 * <li><code>Date</code> object is converted</li>
 * <li><code>JDateTime</code> object is converted</li>
 * <li><code>Number</code> is used as number of milliseconds</li>
 * <li>finally, if string value contains only numbers it is parsed as milliseconds;
 * otherwise as JDateTime pattern</li>
 * </ul>
 */
public class SqlTimeConverter implements TypeConverter<Time> {

    public Time convert(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Time) {
            return (Time) value;
        }
        if (value instanceof Calendar) {
            return new Time(((Calendar) value).getTimeInMillis());
        }
        if (value instanceof Date) {
            return new Time(((Date) value).getTime());
        }
        if (value instanceof Number) {
            return new Time(((Number) value).longValue());
        }

        String stringValue = value.toString().trim();

        // try yyyy-mm-dd for valueOf
        if (!StringKit.containsOnlyDigits(stringValue)) {
            try {
                return Time.valueOf(stringValue);
            } catch (IllegalArgumentException iaex) {
                throw new TypeConversionException(value, iaex);
            }
        }

        // assume string to be a number
        try {
            long milliseconds = Long.parseLong(stringValue);
            return new Time(milliseconds);
        } catch (NumberFormatException nfex) {
            throw new TypeConversionException(value, nfex);
        }
    }

}
