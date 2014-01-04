/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */
package com.jfinal.ext.kit;

import com.google.common.base.Strings;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.jfinal.kit.PathKit;
import com.jfinal.log.Logger;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassSearcher {

    protected static final Logger LOG = Logger.getLogger(ClassSearcher.class);

    private static <T> List<Class<? extends T>> extraction(Class<T> clazz, List<String> classFileList) {
        List<Class<? extends T>> classList = Lists.newArrayList();
        for (String classFile : classFileList) {
            Class<?> classInFile = Reflect.on(classFile).get();
            if (clazz.isAssignableFrom(classInFile) && clazz != classInFile) {
                classList.add((Class<? extends T>) classInFile);
            }
        }

        return classList;
    }

    public static ClassSearcher of(Class target) {
        return new ClassSearcher(target);
    }

    public static ClassSearcher of(Class target, String package_name) {
        return new ClassSearcher(target, package_name);
    }

    private String classpath = PathKit.getRootClassPath();

    private boolean includeAllJarsInLib = false;

    private List<String> includeJars = Lists.newArrayList();

    private String libDir = PathKit.getWebRootPath() + File.separator + "WEB-INF" + File.separator + "lib";

    private final Class target;

    private final String package_name;

    public ClassSearcher(Class target, String package_name) {
        this.target = target;
        this.package_name = package_name;
    }

    public ClassSearcher(Class target) {
        this.target = target;
        this.package_name = StringUtils.EMPTY;
    }

    public ClassSearcher injars(List<String> jars) {
        if (jars != null && !jars.isEmpty()) {
            includeJars.addAll(jars);
        }
        return this;
    }

    public ClassSearcher inJars(String... jars) {
        if (jars != null) {
            Collections.addAll(includeJars, jars);
        }
        return this;
    }

    public ClassSearcher classpath(String classpath) {
        this.classpath = classpath;
        return this;
    }

    public <T> List<Class<? extends T>> search() {
        String class_path = Strings.isNullOrEmpty(package_name) ? classpath
                : classpath + File.separator + (package_name.replace(".", File.separator));
        FluentIterable<File> iterable = Files.fileTreeTraverser().breadthFirstTraversal(new File(class_path));
        List<String> classFileList = findClassFile(iterable);
//        List<String> classFileList = findFiles(class_path, "*.class");
        if (classFileList != null && !classFileList.isEmpty()) {
            classFileList.addAll(findjarFiles(libDir, includeJars));
            return extraction(target, classFileList);
        } else {
            return Lists.newArrayList();
        }
    }

    private static final String CLASS_FILE_EXAT = ".class";

    public List<String> findClassFile(FluentIterable<File> fileFluentIterable) {
        final List<String> files = Lists.newArrayList();
        for (File f : fileFluentIterable) {
            if (f.exists() && !f.isDirectory() && f.getName().endsWith(CLASS_FILE_EXAT)) {
                String classname;
                String tem = f.getAbsoluteFile().toString().replaceAll("\\\\", "/");
                classname = tem.substring(tem.indexOf("/classes") + "/classes".length() + 1,
                        tem.indexOf(".class"));
                files.add(classname.replaceAll("/", "."));
            }
        }
        return files;
    }

    /**
     * 查找jar包中的class
     *
     * @param baseDirName jar路径
     * @param includeJars jar文件地址 <a href="http://my.oschina.net/u/556800" target="_blank" rel="nofollow">@return</a>
     */
    private List<String> findjarFiles(String baseDirName, final List<String> includeJars) {
        List<String> classFiles = Lists.newArrayList();
        try {
            // 判断目录是否存在
            File baseDir = new File(baseDirName);
            if (!baseDir.exists() || !baseDir.isDirectory()) {
                LOG.error("file serach error：" + baseDirName + " is not a dir！");
            } else {
                String[] filelist = baseDir.list(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        return includeAllJarsInLib || includeJars.contains(name);
                    }
                });
                for (String aFilelist : filelist) {
                    JarFile localJarFile = new JarFile(new File(baseDirName + File.separator + aFilelist));
                    Enumeration<JarEntry> entries = localJarFile.entries();
                    while (entries.hasMoreElements()) {
                        JarEntry jarEntry = entries.nextElement();
                        String entryName = jarEntry.getName();
                        if (!jarEntry.isDirectory() && entryName.endsWith(".class")) {
                            String className = entryName.replaceAll("/", ".").substring(0, entryName.length() - 6);
                            classFiles.add(className);
                        }
                    }
                    localJarFile.close();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return classFiles;

    }

    public ClassSearcher includeAllJarsInLib(boolean includeAllJarsInLib) {
        this.includeAllJarsInLib = includeAllJarsInLib;
        return this;
    }

    public ClassSearcher libDir(String libDir) {
        this.libDir = libDir;
        return this;
    }

}
