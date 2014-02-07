/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package com.jfinal.ext.plugin.sqlinxml;

import com.jfinal.log.Logger;
import com.jfinal.sog.kit.map.JaxbKit;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.Map;

/**
 * <p>
 * .
 * </p>
 *
 * @author sagyf yang
 * @version 1.0 2014-02-06 23:45
 * @since JDK 1.6
 */
public class SqlXmlFileListener extends FileAlterationListenerAdaptor {
    final Map<String, String> sqlMap;
    protected static final Logger logger = Logger.getLogger(SqlXmlFileListener.class);


    public SqlXmlFileListener(Map<String, String> sqlMap) {
        this.sqlMap = sqlMap;
    }


    private void reload(File change_file, boolean remove) {
        SqlGroup group;
        if (change_file.isFile()) {
            group = JaxbKit.unmarshal(change_file, SqlGroup.class);
            String name = group.name;
            if (StringUtils.isBlank(name)) {
                name = change_file.getName();
            }
            for (SqlItem sqlItem : group.sqlItems) {
                if (remove) {
                    SqlKit.remove(name + "." + sqlItem.id);
                } else {
                    SqlKit.putOver(name + "." + sqlItem.id, sqlItem.value);
                }
            }
            if (logger.isDebugEnabled()) {
                if (remove) {
                    logger.debug("delete file." + change_file.getAbsolutePath());
                } else {
                    logger.debug("reload file." + change_file.getAbsolutePath());
                }
            }

        }
    }


    @Override
    public void onFileCreate(File file) {
        reload(file, false);
    }

    @Override
    public void onFileChange(File file) {
        reload(file, false);
    }

    @Override
    public void onFileDelete(File file) {

        reload(file, true);
    }
}
