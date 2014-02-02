/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package com.jfinal.ext.typeconverter;

import com.google.common.collect.Maps;
import com.jfinal.ext.kit.Reflect;
import com.jfinal.ext.typeconverter.impl.*;
import org.joda.time.DateTime;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Provides dynamic object conversion to a type.
 * Contains a map of registered converters. User may add new converters.
 * Instantiable version of {@link TypeConverterManager}.
 */
public class TypeConverterManagerBean {

    private final HashMap<Class, TypeConverter> converters = Maps.newHashMapWithExpectedSize(70);

    // ---------------------------------------------------------------- converter

    protected ConvertBean convertBean = new ConvertBean();

    /**
     * Returns {@link ConvertBean}.
     */
    public ConvertBean getConvertBean() {
        return convertBean;
    }

    // ---------------------------------------------------------------- methods

    public TypeConverterManagerBean() {
        registerDefaults();
    }

    /**
     * Registers default set of converters.
     */
    @SuppressWarnings({"UnnecessaryFullyQualifiedName"})
    public void registerDefaults() {
        register(String.class, new StringConverter());
        register(String[].class, new StringArrayConverter(this));

        IntegerConverter integerConverter = new IntegerConverter();
        register(Integer.class, integerConverter);
        register(int.class, integerConverter);

        ShortConverter shortConverter = new ShortConverter();
        register(Short.class, shortConverter);
        register(short.class, shortConverter);

        LongConverter longConverter = new LongConverter();
        register(Long.class, longConverter);
        register(long.class, longConverter);

        ByteConverter byteConverter = new ByteConverter();
        register(Byte.class, byteConverter);
        register(byte.class, byteConverter);

        FloatConverter floatConverter = new FloatConverter();
        register(Float.class, floatConverter);
        register(float.class, floatConverter);

        DoubleConverter doubleConverter = new DoubleConverter();
        register(Double.class, doubleConverter);
        register(double.class, doubleConverter);

        BooleanConverter booleanConverter = new BooleanConverter();
        register(Boolean.class, booleanConverter);
        register(boolean.class, booleanConverter);

        CharacterConverter characterConverter = new CharacterConverter();
        register(Character.class, characterConverter);
        register(char.class, characterConverter);

        register(byte[].class, new ByteArrayConverter(this));
        register(short[].class, new ShortArrayConverter(this));
        register(int[].class, new IntegerArrayConverter(this));
        register(long[].class, new LongArrayConverter(this));
        register(float[].class, new FloatArrayConverter(this));
        register(double[].class, new DoubleArrayConverter(this));
        register(boolean[].class, new BooleanArrayConverter(this));
        register(char[].class, new CharacterArrayConverter(this));

        // we don't really need these, but converters will be cached and not created every time
        register(Integer[].class, new ArrayConverter<Integer>(this, Integer.class));
        register(Long[].class, new ArrayConverter<Long>(this, Long.class));
        register(Byte[].class, new ArrayConverter<Byte>(this, Byte.class));
        register(Short[].class, new ArrayConverter<Short>(this, Short.class));
        register(Float[].class, new ArrayConverter<Float>(this, Float.class));
		register(Double[].class, new ArrayConverter<Double>(this, Double.class));
		register(Boolean[].class, new ArrayConverter<Boolean>(this, Boolean.class));
		register(Character[].class, new ArrayConverter<Character>(this, Character.class));


		register(BigDecimal.class, new BigDecimalConverter());
		register(BigInteger.class, new BigIntegerConverter());
		register(BigDecimal[].class, new ArrayConverter<BigDecimal>(this, BigDecimal.class));
		register(BigInteger[].class, new ArrayConverter<BigInteger>(this, BigInteger.class));

		register(java.util.Date.class, new DateConverter());
		register(Time.class, new SqlTimeConverter());
		register(Timestamp.class, new SqlTimestampConverter());
		register(Calendar.class, new CalendarConverter());
		register(GregorianCalendar.class, new CalendarConverter());
		register(DateTime.class, new DateTimeConverter());


		register(Class[].class, new ClassArrayConverter(this));

		register(URI.class, new URIConverter());
		register(URL.class, new URLConverter());

		register(Locale.class, new LocaleConverter());
		register(TimeZone.class, new TimeZoneConverter());
	}

	/**
	 * Registers a converter for specified type.
	 * User must register converter for all super-classes as well.
	 *
	 * @param type		class that converter is for
	 * @param typeConverter	converter for provided class
	 */
	public void register(Class type, TypeConverter typeConverter) {
		convertBean.register(type, typeConverter);
		converters.put(type, typeConverter);
	}

	/**
	 * Un-registers converter for given type.
	 */
	public void unregister(Class type) {
		convertBean.register(type, null);
		converters.remove(type);
	}

	// ---------------------------------------------------------------- lookup

	/**
	 * Retrieves converter for provided type. Only registered types are matched,
	 * therefore subclasses must be also registered.
	 *
	 * @return founded converter or <code>null</code>
	 */
	public TypeConverter lookup(Class type) {
		return converters.get(type);
	}

	// ---------------------------------------------------------------- convert

	/**
	 * Converts an object to destination type. If type is registered, it's
	 * {@link TypeConverter} will be used. If not, it scans of destination is
	 * an array or enum, as those two cases are handled in a special way.
	 * <p>
	 * If destination type is one of common types, consider using {@link Convert}
	 * instead for somewhat faster approach (no lookup).
	 */
	@SuppressWarnings({"unchecked"})
	public <T> T convertType(Object value, Class<T> destinationType) {
		TypeConverter converter = lookup(destinationType);

		if (converter != null) {
			return (T) converter.convert(value);
		}

		// no converter

		if (value == null) {
			return null;
		}

		// handle destination arrays
		if (destinationType.isArray()) {
			ArrayConverter<T> arrayConverter = new ArrayConverter(this, destinationType.getComponentType());

			return (T) arrayConverter.convert(value);
		}

		// handle enums
		if (destinationType.isEnum()) {
			Object[] enums = destinationType.getEnumConstants();
			String valStr = value.toString();
			for (Object e : enums) {
				if (e.toString().equals(valStr)) {
					return (T) e;
				}
			}
		}

		// check same instances
		if (Reflect.isInstanceOf(value, destinationType)) {
			return (T) value;
		}

		// fail
		throw new TypeConversionException("Conversion failed: " + destinationType.getName());
	}

}