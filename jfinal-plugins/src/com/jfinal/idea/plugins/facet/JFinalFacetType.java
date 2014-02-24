/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package com.jfinal.idea.plugins.facet;

import com.intellij.facet.Facet;
import com.intellij.facet.FacetType;
import com.intellij.javaee.web.facet.WebFacet;
import com.intellij.openapi.module.JavaModuleType;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleType;
import icons.JFinalIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * <p>
 * .
 * </p>
 *
 * @author sagyf yang
 * @version 1.0 2014-02-19 12:05
 * @since JDK 1.6
 */
public class JFinalFacetType extends FacetType<JFinalFacet, JFinalFacetConfiguration> {
    JFinalFacetType() {
        super(JFinalFacet.FACET_TYPE_ID, "JFinal", "JFinal", WebFacet.ID);
    }

    public static FacetType<JFinalFacet, JFinalFacetConfiguration> getInstance() {
        return findInstance(JFinalFacetType.class);
    }

    @Override
    public JFinalFacetConfiguration createDefaultConfiguration() {
        return new JFinalFacetConfiguration();
    }

    @Override
    public JFinalFacet createFacet(@NotNull final Module module,
                                   final String name,
                                   @NotNull final JFinalFacetConfiguration configuration,
                                   @Nullable final Facet underlyingFacet) {
        return new JFinalFacet(this, module, name, configuration, underlyingFacet);
    }


    @Override
    public boolean isSuitableModuleType(final ModuleType moduleType) {
        return moduleType instanceof JavaModuleType;
    }

    @Override
    public Icon getIcon() {
        return JFinalIcons.JFINAL_LOGO;
    }
}
