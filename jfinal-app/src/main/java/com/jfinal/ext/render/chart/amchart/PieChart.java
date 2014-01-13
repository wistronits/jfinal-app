/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */
package com.jfinal.ext.render.chart.amchart;

import com.jfinal.ext.kit.KeyLabel;

import java.util.List;


public class PieChart {

    private List<KeyLabel> pies;

    public PieChart() {

    }

    public PieChart(List<KeyLabel> pies) {
        this.pies = pies;
    }

    public List<KeyLabel> getPies() {
        return pies;
    }

    public void setPies(List<KeyLabel> pies) {
        this.pies = pies;
    }

}
