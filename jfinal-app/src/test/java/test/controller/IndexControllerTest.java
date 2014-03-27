/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package test.controller;

import app.dtos.Work;
import com.alibaba.fastjson.JSON;
import com.github.sog.test.ControllerTestCase;
import com.github.sog.config.AppConfig;
import org.junit.Test;

/**
 * <p>
 * .
 * </p>
 *
 * @author sagyf yang
 * @version 1.0 2014-02-24 11:02
 * @since JDK 1.6
 */
public class IndexControllerTest extends ControllerTestCase<AppConfig> {


    @Test
    public void testIndex() throws Exception {
        String url = "/index";
        String resp = use(url).invoke();
        System.out.println(resp);
    }
    @Test
    public void testAutoParamWithString() throws Exception {
        String url = "/index/find?name=jfinal-app";
        String resp = use(url).invoke();
        System.out.println(resp);
    }

    @Test
    public void testParamToDate() throws Exception {
        String url = "/index/date?name=jfinal-app&day=2014-02-23 14:20:20";
        String resp = use(url).invoke();
        System.out.println(resp);
    }

    @Test
    public void testParamToDouble() throws Exception {
        String url = "/index/doubleNumber?name=jfinal-app&num=3.141592";
        String resp = use(url).invoke();
        System.out.println(resp);
    }

    @Test
    public void testParamToFloat() throws Exception {
        String url = "/index/floadNumber?name=jfinal-app&num=3.11";
        String resp = use(url).invoke();
        System.out.println(resp);
    }

    @Test
    public void testParamToInt() throws Exception {
        String url = "/index/intNumber?name=jfinal-app&num="+Integer.MAX_VALUE;
        String resp = use(url).invoke();
        System.out.println(resp);
    }

    @Test
    public void testParamToLong() throws Exception {
        String url = "/index/longNumber?name=jfinal-app&num="+Long.MAX_VALUE;
        String resp = use(url).invoke();
        System.out.println(resp);
    }

    @Test
    public void testParamToJson() throws Exception {
        Work work = new Work("jfinal", 1);
        String params = JSON.toJSONString(work);

        String url = "/index/json?work="+params;
        String resp = use(url).invoke();
        System.out.println(resp);
    }
}
