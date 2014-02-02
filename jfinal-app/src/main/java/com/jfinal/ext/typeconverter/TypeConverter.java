/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package com.jfinal.ext.typeconverter;

/**
 * Object converter interface.
 *
 * @see jodd.typeconverter.TypeConverterManagerBean
 */
public interface TypeConverter<T> {

	/**
	 * Converts object received as parameter into object of another class.
	 * For example, an <code>Integer</code> converter tries to convert given objects
	 * into target <code>Integer</code> object. Converters should try all reasonable
	 * ways of conversion into target object, depending on target type.
	 *
	 * @param value object to convert from
	 *
	 * @return resulting object converted to target type
	 * @throws TypeConversionException if conversion fails
	 */
	T convert(Object value);

}
