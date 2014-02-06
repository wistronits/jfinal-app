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

package com.jfinal.sog.kit;

import com.google.common.io.ByteSource;
import com.google.common.io.Resources;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

/**
 * FileKit.
 */
public class FileKit {
    public static void delete(File file) {
        if (file != null && file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                if (files != null) {
                    for (File file1 : files) {
                        delete(file1);
                    }
                }
            }
            file.delete();
        }
    }

    public static void loadFileInProperties(String file_path, Properties properties){
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

    /**
     * Converts file to URL in a correct way.
     * Returns <code>null</code> in case of error.
     */
    public static URL toURL(File file) throws MalformedURLException {
        return file.toURI().toURL();
    }

}
