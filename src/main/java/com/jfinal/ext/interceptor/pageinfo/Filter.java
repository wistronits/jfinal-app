/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */
package com.jfinal.ext.interceptor.pageinfo;

import java.util.List;

import com.google.common.collect.Lists;

public class Filter {

    public static final String OPERATOR_LIKE = "LIKE";

    public static final String OPERATOR_EQ = "=";

    public static final String OPERATOR_NOT_EQ = "<>";
    

    public static final String OPERATOR_GREATER_THAN = ">";

    public static final String OPERATOR_LESS_THEN = "<";

    public static final String OPERATOR_GREATER_EQ = ">=";

    public static final String OPERATOR_LESS_EQ = "<=";

    public static final String OPERATOR_NULL = "NULL";
    
    public static final String OPERATOR_NOT_NULL = "NOTNULL";
    
    public static final String RELATION_AND = "AND";

    public static final String RELATION_OR = "OR";

    public static final String RELATION_NOT = "NOT";

    List<Condition> conditions = Lists.newArrayList();

    String relation;

    public Filter() {
        this.relation = RELATION_AND;
    }

    public Filter(String relation) {
        this.relation = relation;
    }
    public void addConditions(String fieldName, Object value, String operater, String relation) {
        conditions.add(new Condition(fieldName, value, operater, relation));
    }

    public void addConditions(String fieldName, Object value, String operater) {
        conditions.add(new Condition(fieldName, value, operater, Filter.RELATION_AND));
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public List<Condition> getConditions() {
        return conditions;
    }

}
