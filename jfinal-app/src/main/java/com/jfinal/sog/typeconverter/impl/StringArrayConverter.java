/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package com.jfinal.sog.typeconverter.impl;


import com.jfinal.sog.typeconverter.TypeConverterManagerBean;

/**
 * Converts given object to <code>String[]</code>.
 * Based on {@link ArrayConverter}, but optimized for String arrays.
 */
public class StringArrayConverter extends ArrayConverter<String> {

	public StringArrayConverter(TypeConverterManagerBean typeConverterManagerBean) {
		super(typeConverterManagerBean, String.class);
	}

	@Override
	protected String[] createArray(int length) {
		return new String[length];
	}

	@Override
	protected String[] convertPrimitiveArrayToArray(Object value, Class primitiveComponentType) {
		String[] result = null;

		if (primitiveComponentType == int.class) {
			int[] array = (int[]) value;
			result = createArray(array.length);
			for (int i = 0; i < array.length; i++) {
				result[i] = String.valueOf(array[i]);
			}
		}
		else if (primitiveComponentType == long.class) {
			long[] array = (long[]) value;
			result = createArray(array.length);
			for (int i = 0; i < array.length; i++) {
				result[i] = String.valueOf(array[i]);
			}
		}
		else if (primitiveComponentType == float.class) {
			float[] array = (float[]) value;
			result = createArray(array.length);
			for (int i = 0; i < array.length; i++) {
				result[i] = String.valueOf(array[i]);
			}
		}
		else if (primitiveComponentType == double.class) {
			double[] array = (double[]) value;
			result = createArray(array.length);
			for (int i = 0; i < array.length; i++) {
				result[i] = String.valueOf(array[i]);
			}
		}
		else if (primitiveComponentType == short.class) {
			short[] array = (short[]) value;
			result = createArray(array.length);
			for (int i = 0; i < array.length; i++) {
				result[i] = String.valueOf(array[i]);
			}
		}
		else if (primitiveComponentType == byte.class) {
			byte[] array = (byte[]) value;
			result = createArray(array.length);
			for (int i = 0; i < array.length; i++) {
				result[i] = String.valueOf(array[i]);
			}
		}
		else if (primitiveComponentType == char.class) {
			char[] array = (char[]) value;
			result = createArray(array.length);
			for (int i = 0; i < array.length; i++) {
				result[i] = String.valueOf(array[i]);
			}
		}
		else if (primitiveComponentType == boolean.class) {
			boolean[] array = (boolean[]) value;
			result = createArray(array.length);
			for (int i = 0; i < array.length; i++) {
				result[i] = String.valueOf(array[i]);
			}
		}
		return result;
	}
}