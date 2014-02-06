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

    Joiner COMMA_JOINER = Joiner.on(StringPool.COMMA).skipNulls();

    Splitter COMMA_SPLITTER = Splitter.on(StringPool.COMMA).trimResults().omitEmptyStrings();
    Joiner   DASH_JOINER    = Joiner.on(StringPool.DASH).skipNulls();

    Splitter         DASH_SPLITTER                = Splitter.on(StringPool.DASH).trimResults().omitEmptyStrings();
    SimpleDateFormat DATE_FORMAT_YYYY_MM_DD_HH_MM = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    SimpleDateFormat DATE_FORMAT_YYYY_MM_DD_HH_MM_SS = new SimpleDateFormat(DateProvider.YYYY_MM_DD_HH_MM_SS);

    DateTimeFormatter DATE_TIME_PATTERN_YYYY_MM_DD_HH_MM_SS = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
}
