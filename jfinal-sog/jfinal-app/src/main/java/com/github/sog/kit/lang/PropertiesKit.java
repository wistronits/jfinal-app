/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package com.github.sog.kit.lang;

import com.google.common.io.ByteSource;
import com.google.common.io.Resources;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

/**
 * <p>
 * .
 * </p>
 *
 * @author sagyf yang
 * @version 1.0 2014-03-27 0:48
 * @since JDK 1.6
 */
public class PropertiesKit {


    public static void loadFileInProperties(String file_path, Properties properties) {
        URL url = Resources.getResource(file_path);
        if (url == null) {
            throw new IllegalArgumentException("Parameter of file can not be blank");
        }

        ByteSource byteSource = Resources.asByteSource(url);
        try {
            properties.load(byteSource.openStream());
        } catch (IOException e) {
            throw new IllegalArgumentException("Properties file can not be loading: " + url);
        }
    }
}
