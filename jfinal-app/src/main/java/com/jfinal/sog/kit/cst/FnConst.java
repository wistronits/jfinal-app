/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package com.jfinal.sog.kit.cst;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.jfinal.sog.kit.date.DateProvider;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.SimpleDateFormat;

/**
 * <p>
 * 一些函数常量.
 * </p>
 *
 * @author sagyf yang
 * @version 1.0 2014-01-31 2:16
 * @since JDK 1.6
 */
public interface FnConst {

    /**
     * 将集合类合并为逗号分割的字符串 处理函数
     */
    Joiner COMMA_JOINER = Joiner.on(StringPool.COMMA).skipNulls();
    /**
     * 将集合类合并为-分割的字符串 处理函数
     */
    Joiner DASH_JOINER  = Joiner.on(StringPool.DASH).skipNulls();

    /**
     * 将逗号分割的字符传转换为集合的处理函数。
     */
    Splitter COMMA_SPLITTER = Splitter.on(StringPool.COMMA).trimResults().omitEmptyStrings();
    /**
     * 将-分割的字符传转换为集合的处理函数。
     */
    Splitter DASH_SPLITTER  = Splitter.on(StringPool.DASH).trimResults().omitEmptyStrings();


    /**
     * 日期格式化方法
     * <pre>
     *     yyyy-MM-dd HH:mm
     * </pre>
     */
    SimpleDateFormat DATE_FORMAT_YYYY_MM_DD_HH_MM = new SimpleDateFormat(DateProvider.YYYY_MM_DD_HH_MM);

    /**
     * 日期格式化方法
     * <pre>
     *     yyyy-MM-dd HH:mm:ss
     * </pre>
     */
    SimpleDateFormat DATE_FORMAT_YYYY_MM_DD_HH_MM_SS = new SimpleDateFormat(DateProvider.YYYY_MM_DD_HH_MM_SS);

    /**
     * Joda的日期格式化方法
     * <pre>
     *     yyyy-MM-dd HH:mm:ss
     * </pre>
     */
    DateTimeFormatter DATE_TIME_PATTERN_YYYY_MM_DD_HH_MM_SS = DateTimeFormat.forPattern(DateProvider.YYYY_MM_DD_HH_MM_SS);
    /**
     * Joda的日期格式化方法
     * <pre>
     *     yyyy-MM-dd HH:mm
     * </pre>
     */
    DateTimeFormatter DATE_TIME_PATTERN_YYYY_MM_DD_HH_MM    = DateTimeFormat.forPattern(DateProvider.YYYY_MM_DD_HH_MM);
    /**
     * Joda的日期格式化方法
     * <pre>
     *     yyyy-MM-dd
     * </pre>
     */
    DateTimeFormatter DATE_TIME_PATTERN_YYYY_MM_DD          = DateTimeFormat.forPattern(DateProvider.YYYY_MM_DD);
}
