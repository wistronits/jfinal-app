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

package com.jfinal.kit;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.sog.kit.cst.StringPool;
import com.jfinal.sog.kit.date.DateProvider;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Convert object to json string.
 * <p/>
 * Json     			java
 * string			java.lang.String
 * number			java.lang.Number
 * true|false		java.lang.Boolean
 * null				null
 * array			java.util.List
 * object			java.util.Map
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class JsonKit {

    private static int    convertDepth     = 8;
    private static String timestampPattern = DateProvider.YYYY_MM_DD_HH_MM_SS;
    private static String datePattern      = DateProvider.YYYY_MM_DD;

    public static void setConvertDepth(int convertDepth) {
        if (convertDepth < 2)
            throw new IllegalArgumentException("convert depth can not less than 2.");
        JsonKit.convertDepth = convertDepth;
    }

    public static void setTimestampPattern(String timestampPattern) {
        if (timestampPattern == null || "".equals(timestampPattern.trim()))
            throw new IllegalArgumentException("timestampPattern can not be blank.");
        JsonKit.timestampPattern = timestampPattern;
    }

    public static void setDatePattern(String datePattern) {
        if (datePattern == null || "".equals(datePattern.trim()))
            throw new IllegalArgumentException("datePattern can not be blank.");
        JsonKit.datePattern = datePattern;
    }

    public static String mapToJson(Map map, int depth) {
        if (map == null)
            return StringPool.NULL;

        StringBuilder sb = new StringBuilder();
        boolean first = true;
        Iterator iter = map.entrySet().iterator();

        sb.append('{');
        while (iter.hasNext()) {
            if (first)
                first = false;
            else
                sb.append(',');

            Map.Entry entry = (Map.Entry) iter.next();
            toKeyValue(String.valueOf(entry.getKey()), entry.getValue(), sb, depth);
        }
        sb.append('}');
        return sb.toString();
    }

    private static String toKeyValue(String key, Object value, StringBuilder sb, int depth) {
        sb.append('\"');
        if (key == null)
            sb.append(StringPool.NULL);
        else
            escape(key, sb);
        sb.append('\"').append(':');

        sb.append(toJson(value, depth));

        return sb.toString();
    }

    public static String listToJson(List list, int depth) {
        if (list == null)
            return StringPool.NULL;

        boolean first = true;
        StringBuilder sb = new StringBuilder();
        Iterator iter = list.iterator();

        sb.append('[');
        while (iter.hasNext()) {
            if (first)
                first = false;
            else
                sb.append(',');

            Object value = iter.next();
            if (value == null) {
                sb.append(StringPool.NULL);
                continue;
            }
            sb.append(toJson(value, depth));
        }
        sb.append(']');
        return sb.toString();
    }

    /**
     * Escape quotes, \, /, \r, \n, \b, \f, \t and other control characters (U+0000 through U+001F).
     */
    private static String escape(String s) {
        if (s == null)
            return null;
        StringBuilder sb = new StringBuilder();
        escape(s, sb);
        return sb.toString();
    }

    private static void escape(String s, StringBuilder sb) {
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            switch (ch) {
                case '"':
                    sb.append("\\\"");
                    break;
                case '\\':
                    sb.append("\\\\");
                    break;
                case '\b':
                    sb.append("\\b");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                case '/':
                    sb.append("\\/");
                    break;
                default:
                    if ((ch >= '\u0000' && ch <= '\u001F') || (ch >= '\u007F' && ch <= '\u009F') || (ch >= '\u2000' && ch <= '\u20FF')) {
                        String str = Integer.toHexString(ch);
                        sb.append("\\u");
                        for (int k = 0; k < 4 - str.length(); k++) {
                            sb.append('0');
                        }
                        sb.append(str.toUpperCase());
                    } else {
                        sb.append(ch);
                    }
            }
        }
    }

    public static String toJson(Object value) {
        return toJson(value, convertDepth);
    }

    public static String toJson(Object value, int depth) {
        if (value == null || (depth--) <= 0)
            return StringPool.NULL;

        if (value instanceof String)
            return StringPool.QUOTE + escape((String) value) + StringPool.QUOTE;

        if (value instanceof Double) {
            if (((Double) value).isInfinite() || ((Double) value).isNaN())
                return StringPool.NULL;
            else
                return value.toString();
        }

        if (value instanceof Float) {
            if (((Float) value).isInfinite() || ((Float) value).isNaN())
                return StringPool.NULL;
            else
                return value.toString();
        }

        if (value instanceof Number)
            return value.toString();

        if (value instanceof Boolean)
            return value.toString();

        if (value instanceof java.util.Date) {
            if (value instanceof java.sql.Timestamp)
                return StringPool.QUOTE + new SimpleDateFormat(timestampPattern).format(value) + StringPool.QUOTE;
            if (value instanceof java.sql.Time)
                return StringPool.QUOTE + value.toString() + StringPool.QUOTE;
            return StringPool.QUOTE + new SimpleDateFormat(datePattern).format(value) + StringPool.QUOTE;
        }

        if (value instanceof Map) {
            return mapToJson((Map) value, depth);
        }

        if (value instanceof List) {
            return listToJson((List) value, depth);
        }

        String result = otherToJson(value, depth);
        if (result != null)
            return result;

        // 类型无法处理时当作字符串处理,否则ajax调用返回时js无法解析
        // return value.toString();
        return StringPool.QUOTE + escape(value.toString()) + StringPool.QUOTE;
    }

    private static String otherToJson(Object value, int depth) {
        if (value instanceof Character) {
            return StringPool.QUOTE + escape(value.toString()) + StringPool.QUOTE;
        }

        if (value instanceof Model) {
            Map map = com.jfinal.plugin.activerecord.CPI.getAttrs((Model) value);
            return mapToJson(map, depth);
        }
        if (value instanceof Record) {
            Map map = ((Record) value).getColumns();
            return mapToJson(map, depth);
        }
        if (value instanceof Object[]) {
            Object[] arr = (Object[]) value;
            List list = new ArrayList(arr.length);
            Collections.addAll(list, arr);
            return listToJson(list, depth);
        }
        if (value instanceof Enum) {
            return StringPool.QUOTE + ((Enum) value).name() + StringPool.QUOTE;
        }

        return beanToJson(value, depth);
    }

    private static String beanToJson(Object model, int depth) {
        Map map = new HashMap();
        Method[] methods = model.getClass().getMethods();
        for (Method m : methods) {
            String methodName = m.getName();
            int indexOfGet = methodName.indexOf("get");
            if (indexOfGet == 0 && methodName.length() > 3) {    // Only getter
                String attrName = methodName.substring(3);
                if (!attrName.equals("Class")) {                // Ignore Object.getClass()
                    Class<?>[] types = m.getParameterTypes();
                    if (types.length == 0) {
                        try {
                            Object value = m.invoke(model);
                            map.put(StringKit.firstCharToLowerCase(attrName), value);
                        } catch (Exception e) {
                            throw new RuntimeException(e.getMessage(), e);
                        }
                    }
                }
            } else {
                int indexOfIs = methodName.indexOf("is");
                if (indexOfIs == 0 && methodName.length() > 2) {
                    String attrName = methodName.substring(2);
                    Class<?>[] types = m.getParameterTypes();
                    if (types.length == 0) {
                        try {
                            Object value = m.invoke(model);
                            map.put(StringKit.firstCharToLowerCase(attrName), value);
                        } catch (Exception e) {
                            throw new RuntimeException(e.getMessage(), e);
                        }
                    }
                }
            }
        }
        return mapToJson(map, depth);
    }
}





