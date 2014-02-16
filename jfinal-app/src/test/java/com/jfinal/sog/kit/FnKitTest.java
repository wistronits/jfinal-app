/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package com.jfinal.sog.kit;

import com.alibaba.druid.util.JdbcUtils;
import org.junit.Test;

/**
 * <p>
 * .
 * </p>
 *
 * @author sagyf yang
 * @version 1.0 2014-02-07 12:15
 * @since JDK 1.6
 */
public class FnKitTest {
    @Test
    public void testContains() throws Exception {

    }

    @Test
    public void testContainsIgnoreCase() throws Exception {

    }

    @Test
    public void testIndexOf() throws Exception {

    }

    @Test
    public void testDateadd() throws Exception {

    }

    @Test
    public void testDatediff() throws Exception {

    }

    @Test
    public void testJdbcDriver() throws Exception {
        System.out.println(JdbcUtils.getDriverClassName("jdbc:h2:mem:jfinal_example"));

    }
}
