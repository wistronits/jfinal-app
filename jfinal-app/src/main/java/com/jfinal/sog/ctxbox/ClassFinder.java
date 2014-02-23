/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package com.jfinal.sog.ctxbox;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.jfinal.ext.kit.Reflect;
import com.jfinal.kit.PathKit;
import com.jfinal.sog.kit.StringPool;

import java.io.File;
import java.util.List;

/**
 * <p>
 * Class Finder.
 * </p>
 *
 * @author sagyf yang
 * @version 1.0 2014-01-19 22:32
 * @since JDK 1.6
 */
public class ClassFinder {

    /**
     * find class files.
     */
    public static void find() {
        String class_path = PathKit.getRootClassPath();
        FluentIterable<File> iterable = Files.fileTreeTraverser().breadthFirstTraversal(new File(class_path));
        List<String> classFileList = findClassFile(iterable);
        for (String classFile : classFileList) {
            Class<?> classInFile = Reflect.on(classFile).get();
            ClassBox.getInstance().push(classInFile);
        }
    }
    /**
     * find class files.
     */
    public static void findWithTest() {
        String class_path = PathKit.getRootClassPath();
        class_path = class_path.replace("test-", "");
        FluentIterable<File> iterable = Files.fileTreeTraverser().breadthFirstTraversal(new File(class_path));
        List<String> classFileList = findClassFile(iterable);
        for (String classFile : classFileList) {
            Class<?> classInFile = Reflect.on(classFile).get();
            ClassBox.getInstance().push(classInFile);
        }
    }

    /**
     * Find files use Guava way to find the Class file under classpath.
     *
     * @param fileFluentIterable file flunent iterable.
     * @return class file list.
     */
    private static List<String> findClassFile(FluentIterable<File> fileFluentIterable) {
        final List<String> files = Lists.newArrayList();
        for (File f : fileFluentIterable) {
            if (f.exists() && !f.isDirectory() && f.getName().endsWith(StringPool.DOT_CLASS)) {
                String classname;
                String tem = f.getAbsoluteFile().toString().replaceAll("\\\\", StringPool.SLASH);
                classname = tem.substring(tem.indexOf("/classes") + "/classes".length() + 1,
                        tem.indexOf(StringPool.DOT_CLASS));
                files.add(classname.replaceAll(StringPool.SLASH, StringPool.DOT));
            }
        }
        return files;
    }
}
