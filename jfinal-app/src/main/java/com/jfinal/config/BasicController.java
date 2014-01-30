/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package com.jfinal.config;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.jfinal.core.Controller;
import com.jfinal.ext.kit.tpl.FreemarkerKit;
import com.jfinal.ext.render.FreeMarkerXMLRender;
import com.jfinal.ext.render.datatables.core.DataSet;
import com.jfinal.ext.render.datatables.core.DatatablesCriterias;
import com.jfinal.ext.render.datatables.core.DatatablesResponse;
import com.jfinal.plugin.activerecord.Page;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Enumeration;
import java.util.Map;

/**
 * <p>
 * 基本控制器.
 * </p>
 *
 * @author walter yang
 * @version 1.0 2013-12-12 14:23
 * @since JDK 1.5
 */
public abstract class BasicController extends Controller {

    protected static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 在jfinal基础上增加渲染XML格式的数据.
     *
     * @param view 视图，这里是基于freemarker的，所以必须为freemarker模板
     */
    protected void renderXml(String view) {
        render(new FreeMarkerXMLRender(view));
    }

    /**
     * 渲染错误提示信息，以Json格式。
     *
     * @param error 错误信息
     */
    protected void renderError(String error) {
        renderJson(AjaxMessage.error(error));
    }

    protected void renderFailure(String failure) {
        renderJson(AjaxMessage.failure(failure));
    }

    /**
     * 渲染成功提示信息,以JSON格式
     *
     * @param message 提示信息
     */
    protected void renderSuccess(String message) {
        renderJson(AjaxMessage.ok(message));
    }

    protected void renderSuccess() {
        renderJson(AjaxMessage.ok());
    }

    /**
     * 渲染错误提示信息，以Json格式。
     *
     * @param error 错误信息
     * @param e     异常
     */
    protected void renderError(String error, Exception e) {
        renderJson(AjaxMessage.error(error, e));
    }

    /**
     * 渲染视图为字符串
     *
     * @param view 视图模版
     * @return 视图渲染后的字符串
     */
    protected String renderTpl(String view) {
        final Enumeration<String> attrs = getAttrNames();
        final Map<String, Object> root = Maps.newHashMap();
        while (attrs.hasMoreElements()) {
            String attrName = attrs.nextElement();
            root.put(attrName, getAttr(attrName));
        }
        return FreemarkerKit.processString(view, root);
    }

    /**
     * 渲染todo提示
     */
    protected void todo() {
        renderJson(AjaxMessage.developing());
    }

    /**
     * 从Request中获取参数并封装为对象进行处理
     *
     * @return jquery DataTables参数信息
     */
    protected DatatablesCriterias getCriterias() {
        return DatatablesCriterias.criteriasWithRequest(getRequest());
    }


    protected <E> void renderDataTables(Page<E> datas, DatatablesCriterias criterias) {
        Preconditions.checkNotNull(criterias, "datatable criterias is must be not null.");
        DataSet<E> dataSet = DataSet.newSet(datas.getList(), datas.getTotalRow(), datas.getTotalRow());
        DatatablesResponse<E> response = DatatablesResponse.build(dataSet, criterias);
        renderJson(response);
    }
}
