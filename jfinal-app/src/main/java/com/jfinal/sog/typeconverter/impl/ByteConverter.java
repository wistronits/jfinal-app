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
 * Converts given object to <code>Byte</code>.
 * Conversion rules:
 * <ul>
 * <li><code>null</code> value is returned as <code>null</code></li>
 * <li>object of destination type is simply casted</li>
 * <li>object is converted to string, trimmed, and then converted if possible.</li>
 * </ul>
 * Number string may start with plus and minus sign.
 */
public class ByteConverter implements TypeConverter<Byte> {

    public Byte convert(Object value) {
        if (value == null) {
            return null;
        }
        if (value.getClass() == Byte.class) {
            return (Byte) value;
        }
        if (value instanceof Number) {
            return ((Number) value).byteValue();
        }
        try {
            String stringValue = value.toString().trim();
            if (StringKit.startsWithChar(stringValue, '+')) {
                stringValue = stringValue.substring(1);
            }
            return Byte.valueOf(stringValue);
        } catch (NumberFormatException nfex) {
            throw new TypeConversionException(value, nfex);
        }
    }

}
