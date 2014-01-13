/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */
package com.jfinal.ext.plugin.tablebind;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.jfinal.ext.kit.ClassSearcher;
import com.jfinal.kit.StringKit;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.IDataSourceProvider;
import com.jfinal.plugin.activerecord.Model;
import org.apache.commons.lang3.StringUtils;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.List;

public class AutoTableBindPlugin extends ActiveRecordPlugin {

    protected final Logger log = Logger.getLogger(getClass());

    @SuppressWarnings("rawtypes")
    private final List<Class<? extends Model>> excludeClasses = Lists.newArrayList();
    private final List<String> includeJars = Lists.newArrayList();
    private boolean autoScan = true;
    private boolean includeAllJarsInLib;
    private final INameStyle nameStyle;

    private final String module_package;

    public AutoTableBindPlugin(DataSource dataSource) {
        this(dataSource, SimpleNameStyles.DEFAULT, StringUtils.EMPTY);
    }

    public AutoTableBindPlugin(DataSource dataSource, INameStyle nameStyle, String module_package) {
        super(dataSource);
        this.nameStyle = nameStyle;
        this.module_package = module_package;
    }

    public AutoTableBindPlugin(IDataSourceProvider dataSourceProvider) {
        this(dataSourceProvider, SimpleNameStyles.DEFAULT, StringUtils.EMPTY);
    }

    public AutoTableBindPlugin(IDataSourceProvider dataSourceProvider, INameStyle nameStyle, String module_package) {
        super(dataSourceProvider);
        this.nameStyle = nameStyle;
        this.module_package = module_package;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public AutoTableBindPlugin addExcludeClasses(Class<? extends Model>... clazzes) {
        Collections.addAll(excludeClasses, clazzes);
        return this;
    }

    @SuppressWarnings("rawtypes")
    public AutoTableBindPlugin addExcludeClasses(List<Class<? extends Model>> clazzes) {
        if (clazzes != null) {
            excludeClasses.addAll(clazzes);
        }
        return this;
    }

    public AutoTableBindPlugin addJars(List<String> jars) {
        if (jars != null) {
            includeJars.addAll(jars);
        }
        return this;
    }

    public AutoTableBindPlugin addJars(String... jars) {
        if (jars != null) {
            Collections.addAll(includeJars, jars);
        }
        return this;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public boolean start() {

        ClassSearcher searcher = Strings.isNullOrEmpty(module_package) ? ClassSearcher.of(Model.class)
                : ClassSearcher.of(Model.class, module_package);
        List<Class<? extends Model>> modelClasses = searcher
                .injars(includeJars).includeAllJarsInLib(includeAllJarsInLib).search();
        TableBind tb;
        for (Class modelClass : modelClasses) {
            if (excludeClasses.contains(modelClass)) {
                continue;
            }
            tb = (TableBind) modelClass.getAnnotation(TableBind.class);
            String tableName;
            if (tb == null) {
                if (!autoScan) {
                    continue;
                }
                tableName = nameStyle.name(modelClass.getSimpleName());
                this.addMapping(tableName, modelClass);
                log.debug("addMapping(" + tableName + ", " + modelClass.getName() + ")");
            } else {
                tableName = tb.tableName();
                if (StringKit.notBlank(tb.pkName())) {
                    this.addMapping(tableName, tb.pkName(), modelClass);
                    log.debug("addMapping(" + tableName + ", " + tb.pkName() + "," + modelClass.getName() + ")");
                } else {
                    this.addMapping(tableName, modelClass);
                    log.debug("addMapping(" + tableName + ", " + modelClass.getName() + ")");
                }
            }
        }
        return super.start();
    }

    @Override
    public boolean stop() {
        return super.stop();
    }

    public AutoTableBindPlugin autoScan(boolean autoScan) {
        this.autoScan = autoScan;
        return this;
    }

    public AutoTableBindPlugin includeAllJarsInLib(boolean includeAllJarsInLib) {
        this.includeAllJarsInLib = includeAllJarsInLib;
        return this;
    }
}
