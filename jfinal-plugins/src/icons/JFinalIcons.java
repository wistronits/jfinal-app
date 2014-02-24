/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package icons;

import com.intellij.openapi.util.IconLoader;
import com.intellij.ui.LayeredIcon;

import javax.swing.*;

/**
 * <p>
 * .
 * </p>
 *
 * @author sagyf yang
 * @version 1.0 2014-02-19 12:14
 * @since JDK 1.6
 */
public class JFinalIcons {
    private static Icon load(String path) {
        return IconLoader.getIcon(path, JFinalIcons.class);
    }


    /**
     * Icon for application.conf files.
     */
    public static final LayeredIcon JFINFAL_CONFIG_FILE = new LayeredIcon(2);

    public static final Icon JFINAL_LOGO = load("jfinal.png");
}
