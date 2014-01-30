/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package com.jfinal.ext.kit;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

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

    Splitter DASH_SPLITTER = Splitter.on(StringPool.DASH).trimResults().omitEmptyStrings();
}
