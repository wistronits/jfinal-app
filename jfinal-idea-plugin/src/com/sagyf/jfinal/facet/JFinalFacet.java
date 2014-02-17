/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2013 sagyf Yang. The Four Group.
 */

package com.sagyf.jfinal.facet;

import com.intellij.facet.Facet;
import com.intellij.facet.FacetManager;
import com.intellij.facet.FacetType;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.pointers.VirtualFilePointer;
import com.intellij.openapi.vfs.pointers.VirtualFilePointerManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * .
 * </p>
 *
 * @author sagyf yang
 * @version 1.0 2013-12-29 21:38
 * @since JDK 1.6
 */
public class JFinalFacet extends Facet<JFinalFacetConfiguration> {

    private List<VirtualFilePointer> resourcePaths;

    public JFinalFacet(@NotNull FacetType facetType, @NotNull Module module, @NotNull String name, @NotNull JFinalFacetConfiguration configuration, Facet underlyingFacet) {
        super(facetType, module, name, configuration, underlyingFacet);
    }

    @Nullable
    public static JFinalFacet getInstance(@NotNull final Module module) {
        return FacetManager.getInstance(module).getFacetByType(JFinalFacetType.ID);
    }

    @NotNull
    public List<VirtualFilePointer> getResourcePaths() {
        if (resourcePaths == null) {
            resourcePaths = new ArrayList<VirtualFilePointer>();
            for (String resourceUrl : getConfiguration().resourceUrls) {
                resourcePaths.add(VirtualFilePointerManager.getInstance().create(resourceUrl, getModule(), null));
            }
        }
        return resourcePaths;
    }


    public void setResourcePaths(@NotNull List<VirtualFilePointer> resourcePaths) {
        this.resourcePaths = resourcePaths;
        JFinalFacetConfiguration configuration = getConfiguration();
        configuration.resourceUrls.clear();
        for (VirtualFilePointer virtualFilePointer : resourcePaths) {
            configuration.resourceUrls.add(FileUtil.toSystemIndependentName(virtualFilePointer.getUrl()));
        }
    }

}
