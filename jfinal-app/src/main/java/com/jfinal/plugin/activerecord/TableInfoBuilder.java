/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package com.jfinal.plugin.activerecord;

import com.google.common.collect.Lists;
import com.jfinal.sog.kit.AppFunc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;

/**
 * TableInfoBuilder build the mapping of model class and table info.
 */
class TableInfoBuilder {

    private static final Logger logger = LoggerFactory.getLogger(TableInfoBuilder.class);

    static boolean buildTableInfo(List<TableInfo> tableMappings) {
        boolean succeed = true;
        Connection conn;
        try {
            conn = DbKit.getDataSource().getConnection();
        } catch (SQLException e) {
            throw new ActiveRecordException(e);
        }

        try {
            final TableInfoMapping tim = TableInfoMapping.me();
            for (TableInfo mapping : tableMappings) {
                try {
                    final TableInfo tableInfo = doBuildTableInfo(mapping, conn);
                    tim.putTableInfo(mapping.getModelClass(), tableInfo);
                } catch (Exception e) {
                    succeed = false;
                    logger.error("Can not build TableInfo, maybe the table {} is not exists.", mapping.getTableName());
//				throw new ActiveRecordException(e);
                }
            }
        } finally {
            DbKit.close(conn);
        }
        return succeed;
    }

    private static TableInfo doBuildTableInfo(TableInfo tableInfo, Connection conn) throws SQLException {

        String sql = DbKit.getDialect().forTableInfoBuilderDoBuildTableInfo(tableInfo.getTableName());
        Statement stm = conn.createStatement();
        ResultSet rs = stm.executeQuery(sql);
        ResultSetMetaData rsmd = rs.getMetaData();

        final int columnCount = rsmd.getColumnCount();

        final List<String> columns = Lists.newArrayListWithCapacity(columnCount);

        for (int i = 1; i <= columnCount; i++) {
            final String colName = rsmd.getColumnName(i);
            String colClassName = rsmd.getColumnClassName(i);
            if ("java.lang.String".equals(colClassName)) {
                // varchar, char, enum, set, text, tinytext, mediumtext, longtext
                tableInfo.addInfo(colName, String.class);
            } else if ("java.lang.Integer".equals(colClassName)) {
                // int, integer, tinyint, smallint, mediumint
                tableInfo.addInfo(colName, Integer.class);
            } else if ("java.lang.Long".equals(colClassName)) {
                // bigint
                tableInfo.addInfo(colName, Long.class);
            }
            // else if ("java.util.Date".equals(colClassName)) {		// java.util.Data can not be returned
            // java.sql.Date, java.sql.Time, java.sql.Timestamp all extends java.util.Data so getDate can return the three types data
            // result.addInfo(colName, java.util.Date.class);
            // }
            else if ("java.sql.Date".equals(colClassName)) {
                // date, year
                tableInfo.addInfo(colName, java.sql.Date.class);
            } else if ("java.lang.Double".equals(colClassName)) {
                // real, double
                tableInfo.addInfo(colName, Double.class);
            } else if ("java.lang.Float".equals(colClassName)) {
                // float
                tableInfo.addInfo(colName, Float.class);
            } else if ("java.lang.Boolean".equals(colClassName)) {
                // bit
                tableInfo.addInfo(colName, Boolean.class);
            } else if ("java.sql.Time".equals(colClassName)) {
                // time
                tableInfo.addInfo(colName, java.sql.Time.class);
            } else if ("java.sql.Timestamp".equals(colClassName)) {
                // timestamp, datetime
                tableInfo.addInfo(colName, java.sql.Timestamp.class);
            } else if ("java.math.BigDecimal".equals(colClassName)) {
                // decimal, numeric
                tableInfo.addInfo(colName, java.math.BigDecimal.class);
            } else if ("[B".equals(colClassName)) {
                // binary, varbinary, tinyblob, blob, mediumblob, longblob
                // qjd project: print_info.content varbinary(61800);
                tableInfo.addInfo(colName, byte[].class);
            } else {
                int type = rsmd.getColumnType(i);
                if (type == Types.BLOB) {
                    tableInfo.addInfo(colName, byte[].class);
                } else if (type == Types.CLOB || type == Types.NCLOB) {
                    tableInfo.addInfo(colName, String.class);
                } else {
                    tableInfo.addInfo(colName, String.class);
                }
                // core.TypeConverter
                // throw new RuntimeException("You've got new type to mapping. Please add code in " + TableInfoBuilder.class.getName() + ". The ColumnClassName can't be mapped: " + colClassName);
            }

            columns.add(colName);
        }

        rs.close();
        stm.close();

        // added by sogyf
        tableInfo.setColumnNames(columns);
        tableInfo.setColumnName(AppFunc.COMMA_JOINER.join(columns));

        return tableInfo;
    }
}

