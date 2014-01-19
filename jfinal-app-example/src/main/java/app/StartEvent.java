/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package app;

import com.jfinal.initalizer.JFinalAfterLoadEvent;

/**
 * <p>
 * .
 * </p>
 *
 * @author sagyf yang
 * @version 1.0 2014-01-19 23:23
 * @since JDK 1.6
 */
public class StartEvent implements JFinalAfterLoadEvent {
    @Override
    public void load() {
        System.out.println("OK");
    }
}
