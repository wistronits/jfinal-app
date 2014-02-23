/**
 * Copyright (c) 2011-2013, James Zhan 詹波 (jfinal@126.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jfinal.plugin.activerecord.dialect;

import com.google.common.collect.Lists;
import com.jfinal.plugin.activerecord.*;
import com.jfinal.sog.kit.StringPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * AnsiSqlDialect. Try to use ANSI SQL dialect with ActiveRecordPlugin.
 * <p/>
 * A clever person solves a problem. A wise person avoids it.
 */
public class AnsiSqlDialect extends Dialect {



    public String forTableInfoBuilderDoBuildTableInfo(String tableName) {
        return "SELECT * FROM " + tableName + " WHERE 1 = 2";
    }

    public void forModelSave(TableInfo tableInfo, Map<String, Object> attrs, StringBuilder sql, List<Object> paras) {
        sql.append("INSERT INTO ").append(tableInfo.getTableName()).append(StringPool.LEFT_BRACKET);
        StringBuilder temp = new StringBuilder(") VALUES(");
        for (Entry<String, Object> e : attrs.entrySet()) {
            String colName = e.getKey();
            if (tableInfo.hasColumnLabel(colName)) {
                if (paras.size() > ZERO) {
                    sql.append(", ");
                    temp.append(", ");
                }
                sql.append(colName);
                temp.append(StringPool.QUESTION_MARK);
                paras.add(e.getValue());
            }
        }
        sql.append(temp.toString()).append(StringPool.RIGHT_BRACKET);
    }

    public String forModelDeleteById(TableInfo tInfo) {
        String pKey = tInfo.getPrimaryKey();
        return "DELETE FROM " + tInfo.getTableName() + SQL_WHERE + pKey + SQL_EQUAL;
    }

    public void forModelUpdate(TableInfo tableInfo, Map<String, Object> attrs, Set<String> modifyFlag, String pKey, Object id, StringBuilder sql, List<Object> paras) {
        sql.append(SQL_UPDATE).append(tableInfo.getTableName()).append(" SET ");
        for (Entry<String, Object> e : attrs.entrySet()) {
            String colName = e.getKey();
            if (!pKey.equalsIgnoreCase(colName) && modifyFlag.contains(colName) && tableInfo.hasColumnLabel(colName)) {
                if (paras.size() > ZERO)
                    sql.append(", ");
                sql.append(colName).append(" = ? ");
                paras.add(e.getValue());
            }
        }
        sql.append(SQL_WHERE).append(pKey).append(SQL_EQUAL);
        paras.add(id);
    }

    public String forModelFindById(TableInfo tInfo, String columns) {
        StringBuilder sql = new StringBuilder("SELECT ");
        if (columns.trim().equals(StringPool.ASTERISK)) {
//            sql.append(columns);
            sql.append(tInfo.getColumnName());
        } else {
            String[] columnsArray = columns.split(StringPool.COMMA);
            for (int i = ZERO; i < columnsArray.length; i++) {
                if (i > ZERO)
                    sql.append(", ");
                sql.append(columnsArray[i].trim());
            }
        }
        sql.append(SQL_FROM);
        sql.append(tInfo.getTableName());
        sql.append(SQL_WHERE).append(tInfo.getPrimaryKey()).append(SQL_EQUAL);
        return sql.toString();
    }

    public String forDbFindById(String tableName, String primaryKey, String columns) {
        StringBuilder sql = new StringBuilder("SELECT ");
        if (columns.trim().equals(StringPool.ASTERISK)) {
            sql.append(columns);
        } else {
            String[] columnsArray = columns.split(StringPool.COMMA);
            for (int i = ZERO; i < columnsArray.length; i++) {
                if (i > ZERO)
                    sql.append(", ");
                sql.append(columnsArray[i].trim());
            }
        }
        sql.append(SQL_FROM);
        sql.append(tableName.trim());
        sql.append(SQL_WHERE).append(primaryKey).append(SQL_EQUAL);
        return sql.toString();
    }

    public String forDbDeleteById(String tableName, String primaryKey) {
        return "DELETE FROM " + tableName.trim() + SQL_WHERE + primaryKey + SQL_EQUAL;
    }

    public void forDbSave(StringBuilder sql, List<Object> paras, String tableName, Record record) {
        sql.append("INSERT INTO ");
        sql.append(tableName.trim()).append(StringPool.LEFT_BRACKET);
        StringBuilder temp = new StringBuilder();
        temp.append(") VALUES(");

        for (Entry<String, Object> e : record.getColumns().entrySet()) {
            if (paras.size() > ZERO) {
                sql.append(", ");
                temp.append(", ");
            }
            sql.append(e.getKey());
            temp.append(StringPool.QUESTION_MARK);
            paras.add(e.getValue());
        }
        sql.append(temp.toString()).append(StringPool.RIGHT_BRACKET);
    }

    public void forDbUpdate(String tableName, String primaryKey, Object id, Record record, StringBuilder sql, List<Object> paras) {
        sql.append(SQL_UPDATE).append(tableName.trim()).append(" set ");
        for (Entry<String, Object> e : record.getColumns().entrySet()) {
            String colName = e.getKey();
            if (!primaryKey.equalsIgnoreCase(colName)) {
                if (paras.size() > ZERO) {
                    sql.append(", ");
                }
                sql.append(colName).append(" = ? ");
                paras.add(e.getValue());
            }
        }
        sql.append(SQL_WHERE).append(primaryKey).append(SQL_EQUAL);
        paras.add(id);
    }

    /**
     * SELECT * FROM subject t1 WHERE (SELECT count(*) FROM subject t2 WHERE t2.id < t1.id AND t2.key = '123') > = 10 AND (SELECT count(*) FROM subject t2 WHERE t2.id < t1.id AND t2.key = '123') < 20 AND t1.key = '123'
     */
    public void forPaginate(StringBuilder sql, int pageNumber, int pageSize, String select, String sqlExceptSelect) {
        throw new ActiveRecordException("Your should not invoke this method because takeOverDbPaginate(...) will take over it.");
    }

    public boolean isTakeOverDbPaginate() {
        return true;
    }

    @SuppressWarnings("rawtypes")
    public Page<Record> takeOverDbPaginate(Connection conn, int pageNumber, int pageSize, String select, String sqlExceptSelect, Object... paras) throws SQLException {
        long totalRow;
        int totalPage;
        List result = CPI.query(conn, "SELECT count(1) " + DbKit.replaceFormatSqlOrderBy(sqlExceptSelect), paras);
        int size = result.size();
        if (size == 1)
            totalRow = ((Number) result.get(ZERO)).longValue();
        else if (size > 1)
            totalRow = result.size();
        else
            return new Page<Record>(new ArrayList<Record>(ZERO), pageNumber, pageSize, ZERO, ZERO);

        totalPage = (int) (totalRow / pageSize);
        if (totalRow % pageSize != ZERO) {
            totalPage++;
        }

        PreparedStatement pst = conn.prepareStatement(select + StringPool.SPACE + sqlExceptSelect, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        for (int i = ZERO; i < paras.length; i++) {
            pst.setObject(i + 1, paras[i]);
        }
        ResultSet rs = pst.executeQuery();

        // move the cursor to the start
        int offset = pageSize * (pageNumber - 1);
        for (int i = ZERO; i < offset; i++)
            if (!rs.next())
                break;

        List<Record> list = buildRecord(rs, pageSize);
        if (rs != null) rs.close();
        if (pst != null) pst.close();
        return new Page<Record>(list, pageNumber, pageSize, totalPage, (int) totalRow);
    }

    private List<Record> buildRecord(ResultSet rs, int pageSize) throws SQLException {
        List<Record> result = new ArrayList<Record>();
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        String[] labelNames = new String[columnCount + 1];
        int[] types = new int[columnCount + 1];
        buildLabelNamesAndTypes(rsmd, labelNames, types);
        for (int k = ZERO; k < pageSize && rs.next(); k++) {
            Record record = new Record();
            Map<String, Object> columns = record.getColumns();
            for (int i = 1; i <= columnCount; i++) {
                Object value;
                if (types[i] < Types.BLOB)
                    value = rs.getObject(i);
                else if (types[i] == Types.CLOB)
                    value = ModelBuilder.handleClob(rs.getClob(i));
                else if (types[i] == Types.NCLOB)
                    value = ModelBuilder.handleClob(rs.getNClob(i));
                else if (types[i] == Types.BLOB)
                    value = ModelBuilder.handleBlob(rs.getBlob(i));
                else
                    value = rs.getObject(i);

                columns.put(labelNames[i], value);
            }
            result.add(record);
        }
        return result;
    }

    private void buildLabelNamesAndTypes(ResultSetMetaData rsmd, String[] labelNames, int[] types) throws SQLException {
        for (int i = 1; i < labelNames.length; i++) {
            labelNames[i] = rsmd.getColumnLabel(i).toLowerCase();
            types[i] = rsmd.getColumnType(i);
        }
    }

    public boolean isTakeOverModelPaginate() {
        return true;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public Page<? extends Model> takeOverModelPaginate(Class<? extends Model> modelClass, int pageNumber, int pageSize, String select, String sqlExceptSelect, Object... paras) {
        Connection conn = null;
        try {
            conn = DbKit.getConnection();
            long totalRow;
            int totalPage;
            List result = CPI.query(conn, "SELECT count(1) " + DbKit.replaceFormatSqlOrderBy(sqlExceptSelect), paras);
            int size = result.size();
            if (size == 1)
                totalRow = ((Number) result.get(ZERO)).longValue();        // totalRow = (Long)result.get(0);
            else if (size > 1)
                totalRow = result.size();
            else
                return new Page(new ArrayList(ZERO), pageNumber, pageSize, ZERO, ZERO);    // totalRow = 0;

            totalPage = (int) (totalRow / pageSize);
            if (totalRow % pageSize != ZERO) {
                totalPage++;
            }

            // --------
            PreparedStatement pst = conn.prepareStatement(select + StringPool.SPACE + sqlExceptSelect, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            for (int i = ZERO; i < paras.length; i++) {
                pst.setObject(i + 1, paras[i]);
            }
            ResultSet rs = pst.executeQuery();

            // move the cursor to the start
            int offset = pageSize * (pageNumber - 1);
            for (int i = ZERO; i < offset; i++)
                if (!rs.next())
                    break;

            List list = buildModel(rs, modelClass, pageSize);
            if (rs != null) rs.close();
            if (pst != null) pst.close();
            return new Page(list, pageNumber, pageSize, totalPage, (int) totalRow);
        } catch (Exception e) {
            throw new ActiveRecordException(e);
        } finally {
            DbKit.close(conn);
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public final <T> List<T> buildModel(ResultSet rs, Class<? extends Model> modelClass, int pageSize) throws SQLException, InstantiationException, IllegalAccessException {
        List<T> result = Lists.newArrayList();
        final ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        String[] labelNames = new String[columnCount + 1];
        int[] types = new int[columnCount + 1];
        buildLabelNamesAndTypes(rsmd, labelNames, types);
        for (int k = ZERO; k < pageSize && rs.next(); k++) {
            Model<?> ar = modelClass.newInstance();
            Map<String, Object> attrs = CPI.getAttrs(ar);
            for (int i = 1; i <= columnCount; i++) {
                Object value;
                if (types[i] < Types.BLOB)
                    value = rs.getObject(i);
                else if (types[i] == Types.CLOB)
                    value = ModelBuilder.handleClob(rs.getClob(i));
                else if (types[i] == Types.NCLOB)
                    value = ModelBuilder.handleClob(rs.getNClob(i));
                else if (types[i] == Types.BLOB)
                    value = ModelBuilder.handleBlob(rs.getBlob(i));
                else
                    value = rs.getObject(i);

                attrs.put(labelNames[i], value);
            }
            result.add((T) ar);
        }
        return result;
    }
}
