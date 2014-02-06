/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package com.jfinal.sog.typeconverter.impl;


import com.jfinal.sog.typeconverter.TypeConversionException;
import com.jfinal.sog.typeconverter.TypeConverter;
import com.jfinal.kit.StringKit;

/**
 * Converts given object to an <code>Integer</code>.
 * Conversion rules:
 * <ul>
 * <li><code>null</code> value is returned as <code>null</code></li>
 * <li>object of destination type is simply casted</li>
 * <li>object is converted to string, trimmed, and then converted if possible.</li>
 * </ul>
 * Number string may start with plus and minus sign.
 */
public class IntegerConverter implements TypeConverter<Integer> {

    public Integer convert(Object value) {
        if (value == null) {
            return null;
        }

        if (value.getClass() == Integer.class) {
            return (Integer) value;
        }
        if (value instanceof Number) {
            return Integer.valueOf(((Number) value).intValue());
        }
        try {
            String stringValue = value.toString().trim();
            if (StringKit.startsWithChar(stringValue, '+')) {
                stringValue = stringValue.substring(1);
            }
            return Integer.valueOf(stringValue);
        } catch (NumberFormatException nfex) {
            throw new TypeConversionException(value, nfex);
        }
    }

}
