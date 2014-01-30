/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */
package com.jfinal.ext.interceptor.pageinfo;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.core.Controller;
import com.jfinal.ext.kit.ModelExt;
import com.jfinal.ext.kit.Reflect;
import com.jfinal.ext.kit.StringPool;
import com.jfinal.kit.StringKit;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.*;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @author kid create 2013-10-24
 */
public class PageInfoKit {

    protected final static Logger LOG = Logger.getLogger(PageInfoKit.class);

    private static final String OPERATOR_SUFFIX = "_op";

    private static final String FILTER_PREFIX = "f_";

    @SuppressWarnings("rawtypes")
    public static Page populate(PageInfo pageInfo, PageInfoInterceptor pageInfoInterceptor) {
        Class<? extends Model<?>> model = pageInfoInterceptor.model();
        TableInfo tableinfo = TableInfoMapping.me().getTableInfo(model);
        Map<String, Class<?>> columnTypeMap = Reflect.on(tableinfo).get("columnTypeMap");
        String select = "select ";
        if (StringKit.isBlank(pageInfoInterceptor.columns())) {
            Set<String> set = columnTypeMap.keySet();
            for (String item : set) {
                select += item + StringPool.COMMA;
            }
            if (!pageInfoInterceptor.relations().isEmpty()) {
                for (RelationInfo relation : pageInfoInterceptor.relations()) {
                    Class<? extends Model<?>> modelClass = relation.getModel();
                    TableInfo relationTableinfo = TableInfoMapping.me().getTableInfo(modelClass);
                    Map<String, Class<?>> relationColumnTypeMap = Reflect.on(relationTableinfo).get("columnTypeMap");
                    set = relationColumnTypeMap.keySet();
                    // 如果设置了前缀表明或者字段冲突的时候
                    for (String item : set) {
                        if (pageInfoInterceptor.useColumnLabel()
                                || columnConflict(item, model, modelClass, pageInfoInterceptor.relations())) {
                            if (!select.contains(StringKit.firstCharToLowerCase(model.getSimpleName()) + "." + item)) {
                                if (select.contains(StringPool.COMMA+ item + StringPool.COMMA)) {
                                    select = select.replace(StringPool.COMMA+ item + StringPool.COMMA,
                                            StringPool.COMMA+ StringKit.firstCharToLowerCase(model.getSimpleName()) + "." + item
                                                    + StringPool.COMMA);
                                }
                                if (select.contains(" " + item + StringPool.COMMA)) {
                                    select = select.replace(" " + item + StringPool.COMMA,
                                            " " + StringKit.firstCharToLowerCase(model.getSimpleName()) + "." + item
                                                    + StringPool.COMMA);
                                }
                            }
                            select += StringKit.firstCharToLowerCase(modelClass.getSimpleName()) + "." + item + " "
                                    + StringKit.firstCharToLowerCase(modelClass.getSimpleName()) + "_" + item + StringPool.COMMA;
                        } else {
                            select += item + StringPool.COMMA;
                        }
                    }
                }
            }
            select = select.substring(0, select.length() - 1);
        } else {
            select += pageInfoInterceptor.columns();
        }
        List<Object> paras = Lists.newArrayList();
        String sqlExceptSelect = "from " + tableinfo.getTableName();
        for (RelationInfo relationInfo : pageInfoInterceptor.relations()) {
            String tableName = TableInfoMapping.me().getTableInfo(relationInfo.getModel()).getTableName();
            String val = relationInfo.getCondition();
            sqlExceptSelect += " left join " + tableName + " on ( " + val + ") ";
        }
        sqlExceptSelect += " where 1=1 ";
        List<Filter> filters = pageInfo.getFilters();
        for (Filter filter : filters) {
            List<Condition> conditions = filter.getConditions();
            sqlExceptSelect += filter.getRelation();
            sqlExceptSelect += " ( 1=1 ";
            for (Condition condition : conditions) {
                String fieldName = condition.getFieldName();
                Object value = condition.getValue();
                if (value == null) {
                    continue;
                }
                if (condition.getOperater().equals(Filter.OPERATOR_LIKE)) {
                    paras.add("%" + value + "%");
                } else if (!condition.getOperater().equals(Filter.OPERATOR_NULL) &&
                        !condition.getOperater().equals(Filter.OPERATOR_NOT_NULL)) {
                    paras.add(value);
                }
                if (condition.getOperater().equals(Filter.OPERATOR_NULL)) {
                    sqlExceptSelect += condition.getRelation() + " " + fieldName + " IS NULL ";
                } else if (condition.getOperater().equals(Filter.OPERATOR_NOT_NULL)) {
                    sqlExceptSelect += condition.getRelation() + " " + fieldName + " IS NOT NULL ";
                } else {
                    sqlExceptSelect += condition.getRelation() + " " + fieldName + " " + condition.getOperater() + " ? ";
                }
            }
            sqlExceptSelect += " ) ";
        }
        String sorterField = pageInfo.getSorterField();
        if (sorterField != null) {
            sqlExceptSelect += " order by " + sorterField + " " + pageInfo.getSorterDirection();
        }
        // select = select.substring(0, select.length());
        if (pageInfoInterceptor.relations().isEmpty()) {
            Model modelInstance = Reflect.on(model).create().get();
            return modelInstance.paginate(pageInfo.getPageNumber(), pageInfo.getPageSize(), select, sqlExceptSelect,
                    paras.toArray(new Object[paras.size()]));
        } else {
            return Db.paginate(pageInfo.getPageNumber(), pageInfo.getPageSize(), select, sqlExceptSelect,
                    paras.toArray(new Object[paras.size()]));
        }
    }

    private static boolean columnConflict(String item, Class<? extends Model<?>> mainModel,
                                          Class<? extends Model<?>> currentModel, List<RelationInfo> relations) {
        if (TableInfoMapping.me().getTableInfo(mainModel).hasColumnLabel(item)) {
            return true;
        }
        for (RelationInfo relationInfo : relations) {
            if (currentModel != relationInfo.getModel()
                    && TableInfoMapping.me().getTableInfo(relationInfo.getModel()).hasColumnLabel(item)) {
                return true;
            }
        }
        return false;
    }

    public static PageInfo injectPageInfo(Class<? extends Model<?>> modelClass, Controller controller,
                                          List<RelationInfo> relations) {
        Map<String, Record> modelAttrs = Maps.newHashMap();
        List<String> modelNames = Lists.newArrayList();
        Map<String, String> models = Maps.newHashMap();
        PageInfo pageInfo = new PageInfo();
        String modelName = StringKit.firstCharToLowerCase(modelClass.getSimpleName());
        pageInfo.setPageNumber(controller.getParaToInt("pageNumber", 1));
        pageInfo.setPageSize(controller.getParaToInt("pageSize", PageInfo.DEFAULT_PAGE_SIZE));
        modelNames.add(modelName);
        modelAttrs.put(modelName, new Record());
        for (RelationInfo relationInfo : relations) {
            String tableName = TableInfoMapping.me().getTableInfo(relationInfo.getModel()).getTableName();
            modelNames.add(tableName);
            modelAttrs.put(StringKit.firstCharToLowerCase(relationInfo.getModel().getSimpleName()), new Record());
        }

        Map<String, String[]> parasMap = controller.getRequest().getParameterMap();
        for (Entry<String, String[]> e : parasMap.entrySet()) {
            String paraKey = e.getKey();
            for (String entry : modelNames) {
                if (paraKey.startsWith(entry + ".")) {
                    String[] paraValue = e.getValue();
                    String value = paraValue[0] != null ? paraValue[0] + "" : null;
                    models.put(paraKey, value);
                }
            }
        }
        // filter
        Map<String, String> filter = Maps.newLinkedHashMap();
        Set<Entry<String, String>> entries = models.entrySet();
        for (Entry<String, String> entry : entries) {
            String key = entry.getKey();
            for (String item : modelNames) {
                if (key.startsWith(item + "." + FILTER_PREFIX)) { // 过滤条件
                    int index = key.indexOf(FILTER_PREFIX);
                    String value = entry.getValue();
                    if (StringKit.isBlank(value)) {
                        continue;
                    }
                    // int manyIndex = propertyName.lastIndexOf("0");
                    // if(manyIndex < 0)
                    // filterName = propertyName.substring("f_".length());
                    // else
                    // filterName = propertyName.substring("f_".length(),manyIndex);
                    filter.put(key.substring(0, index) + key.substring(index + FILTER_PREFIX.length()), value);
                }
            }
        }
        List<Filter> filters = Lists.newArrayList();
        for (Entry<String, String> entry : filter.entrySet()) {
            String key = entry.getKey();
            if (key.endsWith(OPERATOR_SUFFIX)) { // 操作符
                continue;
            }
            String operater = filter.get(key + OPERATOR_SUFFIX);
            if (StringKit.isBlank(operater)) {
                operater = Filter.OPERATOR_EQ;
            }
            int index = key.indexOf(".");
            modelAttrs.get(key.substring(0, index)).set(FILTER_PREFIX + key.substring(index + 1, key.length()),
                    entry.getValue());
            Filter newFilter = new Filter();
            newFilter.addConditions(key, entry.getValue(), operater);
            filters.add(newFilter);
        }
        pseudoDelete(modelClass, modelName, filters);
        for (RelationInfo relationInfo : relations) {
            pseudoDelete(relationInfo.getModel(), modelName, filters);
        }
        pageInfo.setFilters(filters);

        addSorter(controller, pageInfo);
        for (Entry<String, Record> item : modelAttrs.entrySet()) {
            controller.setAttr(item.getKey(), item.getValue());
        }
        return pageInfo;
    }

    private static void addSorter(Controller controller, PageInfo pageInfo) {
        String sorterField = controller.getRequest().getParameter("sorterField");
        if (StringKit.notBlank(sorterField)) {
            String sorterDirection = controller.getRequest().getParameter("sorterDirection");
            if (StringKit.isBlank(sorterDirection)) {
                sorterDirection = "desc";
            }
            pageInfo.setSorterField(sorterField);
            pageInfo.setSorterDirection(sorterDirection);
        }
    }

    private static void pseudoDelete(Class<? extends Model<?>> modelClass, String modelName, List<Filter> filters) {
        if (ModelExt.class.isAssignableFrom(modelClass)) {
            ModelExt<?> modelExt = Reflect.on(modelClass).create().get();
            if (modelExt.pseudoDelete()) {
                Filter deletefilter = new Filter();
                deletefilter.addConditions(modelName + ".deleteflag", 0, Filter.OPERATOR_EQ);
                deletefilter.addConditions(modelName + ".deleteflag", 0, Filter.OPERATOR_NULL, Filter.RELATION_OR);
                filters.add(deletefilter);
            }
        }
    }
}
