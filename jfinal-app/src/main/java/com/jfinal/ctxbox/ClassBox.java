/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package com.jfinal.ctxbox;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.core.Controller;
import com.jfinal.initalizer.JFinalAfterLoadEvent;
import com.jfinal.plugin.activerecord.Model;
import org.quartz.Job;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * .
 * </p>
 *
 * @author sagyf yang
 * @version 1.0 2014-01-19 22:18
 * @since JDK 1.6
 */
public class ClassBox {
    /**
     * 获取单例对象,如果要调用该单例的使用,只能通过该方法获取.
     */
    public static ClassBox getInstance() {
        return ClassBoxHolder.instance;
    }

    private static final Map<ClassType, List<Class>> CLASS_BOX_MAP = Maps.newHashMap();

    public List<Class> getClasses(ClassType classType) {
        return CLASS_BOX_MAP.get(classType);
    }

    void push(Class<?> cls) {
        // Check the class categories.
        if (Model.class.isAssignableFrom(cls)) {
            putModel(cls);
        } else if (Controller.class.isAssignableFrom(cls)) {
            putController(cls);
        } else if (Job.class.isAssignableFrom(cls)) {
            putJob(cls);
        } else if (JFinalAfterLoadEvent.class.isAssignableFrom(cls)) {
            pubApp(cls);
        } else {
            //ignore
        }
    }

    private void pubApp(Class<?> cls) {
        List<Class> apps = CLASS_BOX_MAP.get(ClassType.APP);
        if (apps == null) {
            apps = Lists.newArrayList();
        } else {
            if (apps.contains(cls)) {
                return;
            }
        }
        apps.add(cls);
        CLASS_BOX_MAP.put(ClassType.APP, apps);
    }

    private void putModel(Class<?> cls) {
        List<Class> models = CLASS_BOX_MAP.get(ClassType.MODEL);
        if (models == null) {
            models = Lists.newArrayList();
        } else {
            if (models.contains(cls)) {
                return;
            }
        }
        models.add(cls);
        CLASS_BOX_MAP.put(ClassType.MODEL, models);
    }

    private void putJob(Class<?> cls) {
        List<Class> jobs = CLASS_BOX_MAP.get(ClassType.JOB);
        if (jobs == null) {
            jobs = Lists.newArrayList();
        } else {
            if (jobs.contains(cls)) {
                return;
            }
        }
        jobs.add(cls);
        CLASS_BOX_MAP.put(ClassType.JOB, jobs);
    }

    private void putController(Class<?> cls) {
        List<Class> controllers = CLASS_BOX_MAP.get(ClassType.CONTROLLER);
        if (controllers == null) {
            controllers = Lists.newArrayList();
        } else {
            if (controllers.contains(cls)) {
                return;
            }
        }
        controllers.add(cls);
        CLASS_BOX_MAP.put(ClassType.CONTROLLER, controllers);
    }

    /**
     * 私有构造函数,确保对象只能通过单例方法来调用.
     */
    private ClassBox() {
    }

    /**
     * lazy 加载的内部类,用于实例化单例对象.
     */
    private static class ClassBoxHolder {
        static ClassBox instance = new ClassBox();
    }
}
