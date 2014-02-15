/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package com.jfinal.plugin.activerecord.dialect;

/**
 * <p>
 * H2 Database.
 * </p>
 *
 * @author sagyf yang
 * @version 1.0 2014-02-15 23:47
 * @since JDK 1.6
 */
public class H2Dialect extends AnsiSqlDialect {
    public boolean isSupportReturning() {
        return true;
    }
}
