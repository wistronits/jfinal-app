/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package org.sog.cli;

import com.google.common.collect.Lists;
import joptsimple.OptionParser;
import joptsimple.OptionSet;

import java.io.File;
import java.io.IOException;

/**
 * <p>
 * .
 * </p>
 *
 * @author sagyf yang
 * @version 1.0 2014-02-10 14:30
 * @since JDK 1.6
 */
public class Main {

    public static void main(String[] args) throws IOException {
        // n = project name
        OptionParser parser = new OptionParser() {
            {
                accepts("n").withRequiredArg()
                        .ofType(String.class)
                        .describedAs("project name")
                        .defaultsTo("app");
                accepts("o").withOptionalArg()
                        .ofType(File.class)
                        .required()
                        .describedAs("project directory");
                accepts("t").withOptionalArg()
                        .ofType(String.class)
                        .describedAs("project ide type")
                        .defaultsTo("idea");
                acceptsAll(Lists.newArrayList("h", "?"), "show help").forHelp();
            }
        };
        parser.formatHelpWith(new CliHelpFormatter());
        try {
            OptionSet options = parser.parse(args);

            String name = options.valueOf("n").toString();
            File directory = (File) options.valueOf("o");
            String ide = options.valueOf("t").toString();
            new JFinalCli(name, directory, ide).create();
            System.out.println("!Done");
        } catch (Exception e) {
            System.out.println(e.getMessage());
//            parser.printHelpOn(System.out);
        }

    }
}
