/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package com.jfinal.render;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.plugin.activerecord.CPI;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * JspRender.
 */
public class JspRender extends Render {

    private static final long serialVersionUID = -688478484751775667L;
    private transient static boolean isSupportActiveRecord = true;

    public static void setSupportActiveRecord(boolean supportActiveRecord) {
        JspRender.isSupportActiveRecord = supportActiveRecord;
    }

    public JspRender(String view) {
        this.view = view;
    }

    public void render() {
        // 在 jsp 页面使用如下指令则无需再指字符集, 否则是重复指定了,与页面指定的不一致时还会出乱码
        // <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
        // response.setContentType(contentType);
        // response.setCharacterEncoding(encoding);

        try {
            if (isSupportActiveRecord)
                supportActiveRecord(request);
            request.getRequestDispatcher(view).forward(request, response);
        } catch (Exception e) {
            throw new RenderException(e);
        }
    }

    private static int DEPTH = 8;

    private void supportActiveRecord(HttpServletRequest request) {
        for (Enumeration<String> attrs = request.getAttributeNames(); attrs.hasMoreElements(); ) {
            String key = attrs.nextElement();
            Object value = request.getAttribute(key);
            request.setAttribute(key, handleObject(value, DEPTH));
        }
    }

    private Object handleObject(Object value, int depth) {
        if (value == null || (depth--) <= 0)
            return value;

        if (value instanceof List)
            return handleList((List) value, depth);
        else if (value instanceof Model)
            return handleMap(CPI.getAttrs((Model) value), depth);
        else if (value instanceof Record)
            return handleMap(((Record) value).getColumns(), depth);
        else if (value instanceof Map)
            return handleMap((Map) value, depth);
        else if (value instanceof Page)
            return handlePage((Page) value, depth);
        else if (value instanceof Object[])
            return handleArray((Object[]) value, depth);
        else
            return value;
    }

    private Map handleMap(Map<String, Object> map, int depth) {
        if (map == null || map.size() == 0)
            return map;

        Map<Object, Object> result = Maps.newHashMap();
        for (Map.Entry<String, Object> e : map.entrySet()) {
            Object key = e.getKey();
            Object value = e.getValue();
            value = handleObject(value, depth);
            result.put(key, value);
        }
        return result;
    }

    private List handleList(List list, int depth) {
        if (list == null || list.size() == 0)
            return list;

        List<Object> result = Lists.newArrayListWithCapacity(list.size());
        for (Object value : list)
            result.add(handleObject(value, depth));
        return result;
    }

    private Object handlePage(Page page, int depth) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("list", handleList(page.getList(), depth));
        result.put("pageNumber", page.getPageNumber());
        result.put("pageSize", page.getPageSize());
        result.put("totalPage", page.getTotalPage());
        result.put("totalRow", page.getTotalRow());
        return result;
    }

    private List handleArray(Object[] array, int depth) {
        if (array == null || array.length == 0)
            return new ArrayList(0);

        List<Object> result = Lists.newArrayListWithCapacity(array.length);
        for (Object anArray : array) result.add(handleObject(anArray, depth));
        return result;
    }
}

/*
    private void handleGetterMethod(Map<String, Object> result, Method[] methods) {
		for (Method method : methods) {
			String methodName = method.getName();
			if (methodName.startsWith("get") && method.getParameterTypes().length == 0) {
				throw new RuntimeException("Not finished!");
			}
		}
	}
*/



