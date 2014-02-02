/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package com.jfinal.ext.typeconverter.impl;


import com.jfinal.ext.typeconverter.TypeConversionException;
import com.jfinal.ext.typeconverter.TypeConverter;

import static com.jfinal.ext.kit.StringPool.*;

/**
 * Converts given object to <code>Boolean</code>.
 * Conversion rules:
 * <ul>
 * <li><code>null</code> value is returned as <code>null</code></li>
 * <li>object of destination type is simply casted</li>
 * <li>object is converted to string, trimmed. Then common boolean strings are matched:
 * "yes", "y", "true", "on", "1" for <code>true</code>; and opposite values
 * for <code>false</code>.</li>
 * </ul>
 */
public class BooleanConverter implements TypeConverter<Boolean> {

    public Boolean convert(Object value) {
        if (value == null) {
            return null;
        }

        if (value.getClass() == Boolean.class) {
            return (Boolean) value;
        }

        String stringValue = value.toString().trim().toLowerCase();
        if (stringValue.equals(YES) ||
                stringValue.equals(Y) ||
                stringValue.equals(TRUE) ||
                stringValue.equals(ON) ||
                stringValue.equals(ONE)) {
            return Boolean.TRUE;
        }
        if (stringValue.equals(NO) ||
                stringValue.equals(N) ||
                stringValue.equals(FALSE) ||
                stringValue.equals(OFF) ||
                stringValue.equals(ZERO)) {
            return Boolean.FALSE;
        }

        throw new TypeConversionException(value);
    }

}