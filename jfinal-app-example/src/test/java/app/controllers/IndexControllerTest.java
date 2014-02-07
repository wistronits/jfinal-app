/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package app.controllers;

import com.jfinal.ext.test.ControllerTestCase;
import com.jfinal.sog.initalizer.AppConfig;
import org.junit.Test;

/**
 * <p>
 * .
 * </p>
 *
 * @author sagyf yang
 * @version 1.0 2014-02-07 12:49
 * @since JDK 1.6
 */
public class IndexControllerTest extends ControllerTestCase<AppConfig> {
    @Test
    public void testIndex() throws Exception {
        String url = "/dts";
        use(url).invoke();
    }
}
