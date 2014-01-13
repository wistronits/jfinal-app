/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */
package com.jfinal.ext.plugin.sqlinxml;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.jfinal.ext.kit.JaxbKit;
import com.jfinal.log.Logger;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class SqlKit {

    protected static final Logger logger = Logger.getLogger(SqlKit.class);

    private static final Map<String, String> sqlMap = Maps.newConcurrentMap();

    private static final String CONFIG_SUFFIX = "sql.xml";

    public static String sql(String groupNameAndsqlId) {

        return sqlMap.get(groupNameAndsqlId);
    }

    static void clearSqlMap() {
        sqlMap.clear();
    }

    public static void reload() {
        sqlMap.clear();
        init();
    }

    static void init() {
        final URL resource = SqlKit.class.getClassLoader().getResource("");
        if (resource == null) {
            throw new NullPointerException("the resources is null.");
        }
        FluentIterable<File> iterable = Files.fileTreeTraverser().breadthFirstTraversal(new File(resource.getFile()));
        final List<File> files = Lists.newArrayList();
        for (File f : iterable) {
            if (f.getName().endsWith(CONFIG_SUFFIX)) {
                files.add(f);
            }
        }

        SqlGroup group;
        for (File xmlfile : files) {
            group = JaxbKit.unmarshal(xmlfile, SqlGroup.class);
            String name = group.name;
            if (StringUtils.isBlank(name)) {
                name = xmlfile.getName();
            }
            for (SqlItem sqlItem : group.sqlItems) {
                sqlMap.put(name + "." + sqlItem.id, sqlItem.value);
            }
        }
        logger.debug("sqlMap" + sqlMap);
    }
}
