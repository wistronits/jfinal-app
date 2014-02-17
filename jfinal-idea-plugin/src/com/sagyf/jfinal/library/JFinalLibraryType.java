/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2013 sagyf Yang. The Four Group.
 */

package com.sagyf.jfinal.library;

import com.intellij.framework.library.DownloadableLibraryType;
import com.sagyf.jfinal.Constants;
import org.jetbrains.annotations.NotNull;

import java.net.URL;

/**
 * <p>
 * .
 * </p>
 *
 * @author sagyf yang
 * @version 1.0 2013-12-29 21:49
 * @since JDK 1.6
 */
public class JFinalLibraryType extends DownloadableLibraryType {

    public JFinalLibraryType() {
        super("Jfinal", "jfinal", "jfinal", Constants.JFINAL_ICON, getUrl("jfinal"));
    }

    @NotNull
    private static URL getUrl(@NotNull String lib) {
        return JFinalLibraryType.class.getResource("/resources/" + lib + ".xml");

    }

    @Override
    protected String[] getDetectionClassNames() {
        return new String[0];
    }
}
