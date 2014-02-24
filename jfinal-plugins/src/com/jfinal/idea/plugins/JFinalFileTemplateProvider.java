/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package com.jfinal.idea.plugins;

import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.openapi.module.Module;
import com.intellij.util.text.VersionComparatorUtil;
import org.jetbrains.annotations.NotNull;

/**
 * <p>
 * .
 * </p>
 *
 * @author sagyf yang
 * @version 1.0 2014-02-19 14:48
 * @since JDK 1.6
 */
public class JFinalFileTemplateProvider {


    private final String  myVersionName;
    private final boolean my21orNewer;

    public JFinalFileTemplateProvider(Module module) {
        myVersionName = JFinalVersionDetector.detectJfinalVersion(module);
        my21orNewer = isNewerThan("2.1");
    }

    @NotNull
    public FileTemplate determineFileTemplate() {

        String template = JFinalFileTemplateGroupDescriptorFactory.JFINAL_APPLICATION_CONF;

        final FileTemplateManager fileTemplateManager = FileTemplateManager.getInstance();
        return fileTemplateManager.getJ2eeTemplate(template);
    }


    private boolean isNewerThan(String versionName) {
        return VersionComparatorUtil.compare(myVersionName, versionName) > 0;
    }
}
