/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package com.jfinal.idea.plugins.facet;

import com.intellij.facet.FacetType;
import com.intellij.framework.detection.FacetBasedFrameworkDetector;
import com.intellij.framework.detection.FileContentPattern;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.patterns.ElementPattern;
import com.intellij.util.indexing.FileContent;
import com.jfinal.idea.plugins.JFinalConstants;
import org.jetbrains.annotations.NotNull;

/**
 * <p>
 * .
 * </p>
 *
 * @author sagyf yang
 * @version 1.0 2014-02-19 14:34
 * @since JDK 1.6
 */
public class JFinalFrameworkDetector
        extends FacetBasedFrameworkDetector<JFinalFacet, JFinalFacetConfiguration> {
    public JFinalFrameworkDetector() {
        super("jfinal");
    }

    @Override
    public FacetType<JFinalFacet, JFinalFacetConfiguration> getFacetType() {
        return JFinalFacetType.getInstance();
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return StdFileTypes.PROPERTIES;
    }

    @NotNull
    @Override
    public ElementPattern<FileContent> createSuitableFilePattern() {
        return FileContentPattern.fileContent()
                .withName(JFinalConstants.JFINAL_APPLICATION_CONFIG_FILENAME);
    }
}
