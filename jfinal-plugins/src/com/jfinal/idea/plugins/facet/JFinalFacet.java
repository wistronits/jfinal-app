/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package com.jfinal.idea.plugins.facet;

import com.intellij.facet.Facet;
import com.intellij.facet.FacetManager;
import com.intellij.facet.FacetType;
import com.intellij.facet.FacetTypeId;
import com.intellij.javaee.web.facet.WebFacet;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.util.Disposer;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * <p>
 * .
 * </p>
 *
 * @author sagyf yang
 * @version 1.0 2014-02-19 12:00
 * @since JDK 1.6
 */
public class JFinalFacet extends Facet<JFinalFacetConfiguration> {

    public static final FacetTypeId<JFinalFacet> FACET_TYPE_ID = new FacetTypeId<JFinalFacet>("jfinal");

    public JFinalFacet(@NotNull FacetType facetType, @NotNull Module module, @NotNull String name, @NotNull JFinalFacetConfiguration configuration, Facet underlyingFacet) {
        super(facetType, module, name, configuration, underlyingFacet);
        Disposer.register(this, configuration);
    }


    /**
     * Gets the StrutsFacet for the given module.
     *
     * @param module Module to check.
     * @return Instance or <code>null</code> if none configured.
     */
    @Nullable
    public static JFinalFacet getInstance(@NotNull final Module module) {
        return FacetManager.getInstance(module).getFacetByType(FACET_TYPE_ID);
    }


    /**
     * Gets the StrutsFacet for the module containing the given PsiElement.
     *
     * @param element Element to check.
     * @return Instance or <code>null</code> if none configured.
     */
    @Nullable
    public static JFinalFacet getInstance(@NotNull final PsiElement element) {
        final Module module = ModuleUtilCore.findModuleForPsiElement(element);
        return module != null ? getInstance(module) : null;
    }


    /**
     * Returns the underlying WebFacet.
     *
     * @return WebFacet.
     */
    @NotNull
    public WebFacet getWebFacet() {
        return (WebFacet) getUnderlyingFacet();
    }

}
