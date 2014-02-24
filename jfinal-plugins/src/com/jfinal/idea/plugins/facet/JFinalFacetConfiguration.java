/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package com.jfinal.idea.plugins.facet;

import com.intellij.facet.FacetConfiguration;
import com.intellij.facet.ui.FacetEditorContext;
import com.intellij.facet.ui.FacetEditorTab;
import com.intellij.facet.ui.FacetValidatorsManager;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.ModificationTracker;
import com.intellij.openapi.util.WriteExternalException;
import org.jdom.Element;
import org.jetbrains.annotations.NonNls;

/**
 * <p>
 * .
 * </p>
 *
 * @author sagyf yang
 * @version 1.0 2014-02-19 12:00
 * @since JDK 1.6
 */
public class JFinalFacetConfiguration implements FacetConfiguration, ModificationTracker, Disposable {
    // Filesets
    @NonNls
    private static final String FILESET = "fileset";
    @NonNls
    private static final String SET_ID = "id";
    @NonNls
    private static final String SET_NAME = "name";
    @NonNls
    private static final String SET_REMOVED = "removed";
    @NonNls
    private static final String FILE = "file";


    @Override
    public void dispose() {

    }

    @Override
    public FacetEditorTab[] createEditorTabs(FacetEditorContext facetEditorContext, FacetValidatorsManager facetValidatorsManager) {
        return new FacetEditorTab[0];
    }

    @Override
    public void readExternal(Element element) throws InvalidDataException {
        for (final Object setElement : element.getChildren(FILESET)) {
            final String setName = ((Element) setElement).getAttributeValue(SET_NAME);
            final String setId = ((Element) setElement).getAttributeValue(SET_ID);
            final String removed = ((Element) setElement).getAttributeValue(SET_REMOVED);
            if (setName != null && setId != null) {

            }
        }
    }

    @Override
    public void writeExternal(Element element) throws WriteExternalException {

    }

    @Override
    public long getModificationCount() {
        return 0;
    }
}
