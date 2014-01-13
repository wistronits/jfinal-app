/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */
package com.jfinal.ext.interceptor.pageinfo;

import java.util.List;

import com.google.common.collect.Lists;

public class PageInfo {

    public static final int DEFAULT_PAGE_SIZE = 10;

    List<Filter> filters = Lists.newArrayList();

    int pageNumber;

    int pageSize;

    String sorterField;

    String sorterDirection;

    public List<Filter> getFilters() {
        return filters;
    }

    public void setFilters(List<Filter> filters) {
        this.filters = filters;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNum) {
        this.pageNumber = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getSorterField() {
        return sorterField;
    }

    public void setSorterField(String sorterField) {
        this.sorterField = sorterField;
    }

    public String getSorterDirection() {
        return sorterDirection;
    }

    public void setSorterDirection(String sorterDirection) {
        this.sorterDirection = sorterDirection;
    }

}
