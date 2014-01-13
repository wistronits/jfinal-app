/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package app.controllers;

import com.jfinal.config.BasicController;
import org.joda.time.DateTime;

/**
 * <p>
 * .
 * </p>
 *
 * @author sagyf yang
 * @version 1.0 2014-01-13 14:35
 * @since JDK 1.6
 */
public class IndexController extends BasicController{

    public void index(){
        setAttr("now", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
        render("/index");
    }
}
