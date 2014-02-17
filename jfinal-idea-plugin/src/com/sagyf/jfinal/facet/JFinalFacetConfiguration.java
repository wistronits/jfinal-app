/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2013 sagyf Yang. The Four Group.
 */

package com.sagyf.jfinal.facet;

import com.intellij.facet.FacetConfiguration;
import com.intellij.facet.impl.ui.FacetEditorsFactoryImpl;
import com.intellij.facet.ui.FacetEditorContext;
import com.intellij.facet.ui.FacetEditorTab;
import com.intellij.facet.ui.FacetValidatorsManager;
import com.intellij.framework.library.DownloadableLibraryService;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.WriteExternalException;
import com.intellij.util.messages.Topic;
import com.sagyf.jfinal.library.JFinalLibraryType;
import org.jdom.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * .
 * </p>
 *
 * @author sagyf yang
 * @version 1.0 2013-12-29 21:39
 * @since JDK 1.6
 */
public class JFinalFacetConfiguration implements FacetConfiguration {

    List<String> resourceUrls = new ArrayList<String>();


    public static final Topic<Runnable> ADDITIONAL_PATHS_CHANGED = new Topic<Runnable>("additional resource paths changed", Runnable.class);

    @Override
    public FacetEditorTab[] createEditorTabs(FacetEditorContext facetEditorContext, FacetValidatorsManager validatorsManager) {
        validatorsManager.registerValidator(FacetEditorsFactoryImpl.getInstanceImpl().createLibraryValidator(
                DownloadableLibraryService.getInstance().createDescriptionForType(JFinalLibraryType.class),
                facetEditorContext,
                validatorsManager,
                "jfinal"
        ));
        return new FacetEditorTab[]{new JFinalFacetEditorTab(facetEditorContext)};
    }

    @Override
    public void readExternal(Element element) throws InvalidDataException {

    }

    @Override
    public void writeExternal(Element element) throws WriteExternalException {

    }
}
