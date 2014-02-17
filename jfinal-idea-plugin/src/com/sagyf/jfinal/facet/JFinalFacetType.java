/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2013 sagyf Yang. The Four Group.
 */

package com.sagyf.jfinal.facet;

import com.intellij.facet.Facet;
import com.intellij.facet.FacetType;
import com.intellij.facet.FacetTypeId;
import com.intellij.openapi.module.JavaModuleType;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleType;
import com.sagyf.jfinal.Constants;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * <p>
 * .
 * </p>
 *
 * @author sagyf yang
 * @version 1.0 2013-12-29 21:40
 * @since JDK 1.6
 */
public class JFinalFacetType extends FacetType<JFinalFacet, JFinalFacetConfiguration> {

    private static final String STRING_ID = "jfinal";
    private static final String PRESENTABLE_NAME = "JFinal";
    public static final FacetTypeId<JFinalFacet> ID = new FacetTypeId<JFinalFacet>(STRING_ID);
    public static final JFinalFacetType INSTANCE = new JFinalFacetType();

    public JFinalFacetType() {
        super(ID, STRING_ID, PRESENTABLE_NAME);
    }

    @Override
    public JFinalFacetConfiguration createDefaultConfiguration() {
        return new JFinalFacetConfiguration();
    }

    @Override
    public JFinalFacet createFacet(@NotNull Module module, String name,
                                   @NotNull JFinalFacetConfiguration configuration,
                                   @Nullable Facet underlyingFacet) {
        return new JFinalFacet(this, module, name, configuration, underlyingFacet);
    }

    @Override
    public boolean isSuitableModuleType(ModuleType moduleType) {
        return (moduleType instanceof JavaModuleType);
    }

    @Override
    public boolean isOnlyOneFacetAllowed() {
        return true;
    }

    @Override
    public Icon getIcon() {
        return Constants.JFINAL_ICON;
    }
}
