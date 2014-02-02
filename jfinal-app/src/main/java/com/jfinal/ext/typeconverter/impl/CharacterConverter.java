/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package com.jfinal.ext.typeconverter.impl;


import com.jfinal.ext.typeconverter.TypeConversionException;
import com.jfinal.ext.typeconverter.TypeConverter;
import com.jfinal.kit.StringKit;

/**
 * Converts given object to <code>Character</code>.
 * Conversion rules:
 * <ul>
 * <li><code>null</code> value is returned as <code>null</code></li>
 * <li>object of destination type is simply casted</li>
 * <li><code>Number</code> is converted to <code>char</code> value</li>
 * <li>finally, <code>toString()</code> value of length 1 is converted to <code>char</code></li>
 * <li>if string is longer, and made of digits, try to convert it to int first</li>
 * </ul>
 */
public class CharacterConverter implements TypeConverter<Character> {

	public Character convert(Object value) {
		if (value == null) {
			return null;
		}
		if (value.getClass() == Character.class) {
			return (Character) value;
		}
		if (value instanceof Number) {
			char c = (char) ((Number) value).intValue();
			return Character.valueOf(c);
		}
		try {
			String s = value.toString();
			if (s.length() != 1) {
				s = s.trim();
				if (!StringKit.containsOnlyDigitsAndSigns(s)) {
					throw new TypeConversionException(value);
				}

				try {
					char c = (char) Integer.parseInt(s);
					return Character.valueOf(c);
				} catch (NumberFormatException nfex) {
					throw new TypeConversionException(value, nfex);
				}
			}
			return Character.valueOf(s.charAt(0));
		} catch (IndexOutOfBoundsException ioobex) {
			throw new TypeConversionException(value, ioobex);
		}
	}

}