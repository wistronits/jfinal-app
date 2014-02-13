/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package org.sog.cli;

import com.google.common.io.CharStreams;
import com.google.common.io.Files;
import org.apache.http.client.fluent.Request;

import java.io.*;
import java.util.List;

/**
 * <p>
 * .
 * </p>
 *
 * @author sagyf yang
 * @version 1.0 2014-02-10 14:58
 * @since JDK 1.6
 */
public final class JFinalCli {


    public static final String APPLICATION_EXAMLE_URL = "https://raw2.github.com/sogyf/jfinal-app/master/jfinal-app-example/";
    public static final String UTF_8                  = "UTF-8";
    private final LayoutFile layoutFile;

    private final List<String> deps;

    final String tmp = "app";

    private final String  name;
    private final IdeType ide;

    public JFinalCli(String name, File directory, String ide) throws IOException {
        if (directory.isFile()) {
            System.err.println("The Project Directory is File!");
            throw new RuntimeException("");
        }

        String directory_path = directory.getAbsolutePath() + File.separator + name;
        String main_path = directory_path + File.separator + "src" + File.separator + "main" + File.separator;
        this.layoutFile = new LayoutFile(main_path);
        this.name = name;
        InputStream is = Thread.currentThread().getClass().getResourceAsStream("/deps.txt");
        deps = CharStreams.readLines(new BufferedReader(new InputStreamReader(is)));
        this.ide = IdeType.valueOf(ide);
    }

    public void create() {
        // 1. 创建项目所有的文件布局路径
        //    标准WAVEN的工程约定目录
        createProjectLayout();

        // 2. 下载模版文件
        downTmpFile();

        // 3. 下载相关依赖文件
        downDeps();

        // 4. 生成项目文件
        createIde();
    }

    private void createIde() {

        switch (ide) {
            case IDEA:
                createProjectIdea();
                break;
            case ECLIPSE:
                break;
        }
    }

    private void createProjectIdea() {
        String ipr_path = layoutFile.getMain_path() + this.name + ".ipr";
        String iml_path = layoutFile.getMain_path() + this.name + ".iml";
        String iws_path = layoutFile.getMain_path() + this.name + ".iws";


    }

    private void downDeps() {
        try {
            final String lib_path = layoutFile.getWebapp_path() + "lib" + File.separator;
            Files.createParentDirs(new File(lib_path + tmp));
            for (String dep : deps) {
                downJar(lib_path, dep);
            }
        } catch (IOException e) {
            System.err.println("down library is error!" + e.getMessage());
        }
    }

    private void downJar(String lib_path, String down_url) throws IOException {
        String file_name = down_url.substring(down_url.lastIndexOf("/") + 1);
        final File dest = new File(lib_path + file_name);
        if (dest.exists()) {
            return;
        }
        Request.Get(down_url)
                .useExpectContinue()
                .execute()
                .saveContent(dest);
    }

    private void downTmpFile() {
        try {
            final File conf = new File(layoutFile.getResource_path() + "application.conf");
            Request.Get(APPLICATION_EXAMLE_URL + "src/main/resources/application.conf")
                    .useExpectContinue()
                    .execute()
                    .saveContent(conf);
            // todo update conf file.

        } catch (IOException e) {
            System.err.println("down application.conf is error!");
        }

    }

    private void createProjectLayout() {

        try {
            Files.createParentDirs(new File(layoutFile.getJava_path() + tmp));
            Files.createParentDirs(new File(layoutFile.getResource_path() + tmp));
            Files.createParentDirs(new File(layoutFile.getWebapp_path() + tmp));
            Files.createParentDirs(new File(layoutFile.getWebapp_path() + "static" + tmp));
            Files.createParentDirs(new File(layoutFile.getWebapp_path() + "WEB-INF" + File.separator
                    + "views" + File.separator + tmp));
        } catch (IOException e) {
            System.err.println("create project layout directory is error!" + e.getMessage());
        }
    }
}
