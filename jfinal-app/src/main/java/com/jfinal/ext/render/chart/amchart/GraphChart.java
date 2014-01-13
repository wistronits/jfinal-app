/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */
package com.jfinal.ext.render.chart.amchart;

import java.util.List;

public class GraphChart {
    /**
     * 每条数据的说明（x轴）
     */
    private List<String> seriesNames;

    /**
     * 数据描述
     */
    private List<?> values;

    public List<String> getSeriesNames() {
        return seriesNames;
    }

    public void setSeriesNames(List<String> seriesNames) {
        this.seriesNames = seriesNames;
    }

    public List<?> getValues() {
        return values;
    }

    public void setValues(List<?> values) {
        this.values = values;
    }

}
