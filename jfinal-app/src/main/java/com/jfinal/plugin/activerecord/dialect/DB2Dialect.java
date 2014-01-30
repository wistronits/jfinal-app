/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package com.jfinal.plugin.activerecord.dialect;

import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.TableInfo;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 扩展对DB2的支持.
 * </p>
 *
 * @author sagyf yang
 * @version 1.0 2014-01-22 16:30
 * @since JDK 1.6
 */
public class DB2Dialect extends Dialect {
    @Override
    public String forTableInfoBuilderDoBuildTableInfo(String tableName) {
        return null;
    }

    @Override
    public void forModelSave(TableInfo tableInfo, Map<String, Object> attrs, StringBuilder sql, List<Object> paras) {

    }

    @Override
    public String forModelDeleteById(TableInfo tInfo) {
        return null;
    }

    @Override
    public void forModelUpdate(TableInfo tableInfo, Map<String, Object> attrs, Set<String> modifyFlag, String pKey, Object id, StringBuilder sql, List<Object> paras) {

    }

    @Override
    public String forModelFindById(TableInfo tInfo, String columns) {
        return null;
    }

    @Override
    public String forDbFindById(String tableName, String primaryKey, String columns) {
        return null;
    }

    @Override
    public String forDbDeleteById(String tableName, String primaryKey) {
        return null;
    }

    @Override
    public void forDbSave(StringBuilder sql, List<Object> paras, String tableName, Record record) {

    }

    @Override
    public void forDbUpdate(String tableName, String primaryKey, Object id, Record record, StringBuilder sql, List<Object> paras) {

    }

    @Override
    public void forPaginate(StringBuilder sql, int pageNumber, int pageSize, String select, String sqlExceptSelect) {

    }
}
