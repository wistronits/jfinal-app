/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package com.jfinal.idea.plugins;

import com.intellij.ide.fileTemplates.FileTemplateDescriptor;
import com.intellij.ide.fileTemplates.FileTemplateGroupDescriptor;
import com.intellij.ide.fileTemplates.FileTemplateGroupDescriptorFactory;
import icons.JFinalIcons;
import org.jetbrains.annotations.NonNls;

/**
 * <p>
 * .
 * </p>
 *
 * @author sagyf yang
 * @version 1.0 2014-02-19 14:51
 * @since JDK 1.6
 */
public class JFinalFileTemplateGroupDescriptorFactory implements FileTemplateGroupDescriptorFactory {
    /**
     * Template for {@code 2.0.x}.
     */
    @NonNls
    public static final String JFINAL_APPLICATION_CONF = "application.conf";


    public FileTemplateGroupDescriptor getFileTemplatesDescriptor() {
        final FileTemplateGroupDescriptor group = new FileTemplateGroupDescriptor("jfinal",
                JFinalIcons.JFINAL_LOGO);
        group.addTemplate(new FileTemplateDescriptor(JFINAL_APPLICATION_CONF,
                JFinalIcons.JFINFAL_CONFIG_FILE));
        return group;
    }
}
