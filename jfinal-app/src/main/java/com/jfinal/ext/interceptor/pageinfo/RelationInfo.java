/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */
package com.jfinal.ext.interceptor.pageinfo;

import com.jfinal.plugin.activerecord.Model;

/**
 * @author kid create 2013-10-20 
 */
public class RelationInfo {
    private RelationType type;
    
    private String condition;
    
    private Class<? extends Model<?>> model;

    
    public RelationInfo(RelationType type, String condition, Class<? extends Model<?>> model) {
        this.type = type;
        this.condition = condition;
        this.model = model;
    }

    public RelationType getType() {
        return type;
    }

    public void setType(RelationType type) {
        this.type = type;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public Class<? extends Model<?>> getModel() {
        return model;
    }

    public void setModel(Class<? extends Model<?>> model) {
        this.model = model;
    }
    
    
}

