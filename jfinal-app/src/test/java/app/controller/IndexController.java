/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package app.controller;

import app.dtos.Work;
import app.module.Task;
import com.jfinal.aop.Before;
import com.github.sog.controller.AjaxMessage;
import com.github.sog.controller.BasicController;
import com.github.sog.annotation.Param;
import com.github.sog.test.ci.ProfilerInterceptor;
import com.github.sog.kit.AppFunc;

import java.util.Date;

/**
 * <p>
 * .
 * </p>
 *
 * @author sagyf yang
 * @version 1.0 2014-02-24 11:00
 * @since JDK 1.6
 */
public class IndexController extends BasicController {

    public void index() {
        Task.dao.findById(1);
        renderJson("abc");
    }

    public void find(String name) {
        renderJson("hello:" + name);
    }

    public void intNumber(String name, int num) {
        renderJson(AjaxMessage.ok(name + num));
    }

    public void longNumber(String name, long num) {
        renderJson(AjaxMessage.ok(name + num));
    }

    public void doubleNumber(String name, double num) {
        renderJson(AjaxMessage.ok(name + num));
    }

    public void floadNumber(String name, float num) {
        renderJson(AjaxMessage.ok(name + num));
    }

    public void date(String name, Date day) {
        renderJson(AjaxMessage.ok("Hi " + name + ",today is " + AppFunc.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS.format(day)));
    }


    @Before(ProfilerInterceptor.class)
    public void json(String name, @Param(json = true) Work work,
                     @Param(json = true) Work d) {
        System.out.println(name);
        renderJson(AjaxMessage.ok(work));
    }


    public void real_name(String name, @Param(json = true, name = "fu") Work work,
                          @Param(json = true) Work d) {
        System.out.println(name);
        renderJson(AjaxMessage.ok(work));
    }
}
