/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package com.jfinal.idea.plugins.facet;

import com.intellij.framework.library.DownloadableLibraryType;
import icons.JFinalIcons;

/**
 * <p>
 * .
 * </p>
 *
 * @author sagyf yang
 * @version 1.0 2014-02-19 14:45
 * @since JDK 1.6
 */
public class JFinalLibraryType extends DownloadableLibraryType {
    public static final String STRUTS_VERSION_CLASS = "com.jfinal.idea.plugins.JFinalConstants";

    private static final String GROUP_ID = "jfinal";

    public JFinalLibraryType() {
        super("JFianl", "jfinal", GROUP_ID, JFinalIcons.JFINAL_LOGO, JFinalLibraryType.class.getResource("struts2.xml"));
    }

    @Override
    public String[] getDetectionClassNames() {
        return new String[]{STRUTS_VERSION_CLASS};
    }
}
