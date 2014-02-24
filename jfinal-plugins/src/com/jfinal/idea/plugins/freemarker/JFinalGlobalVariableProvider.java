/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package com.jfinal.idea.plugins.freemarker;

import com.intellij.freemarker.psi.files.FtlFile;
import com.intellij.freemarker.psi.files.FtlGlobalVariableProvider;
import com.intellij.freemarker.psi.variables.FtlVariable;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * .
 * </p>
 *
 * @author sagyf yang
 * @version 1.0 2014-02-19 11:58
 * @since JDK 1.6
 */
public class JFinalGlobalVariableProvider extends FtlGlobalVariableProvider {
    @NotNull
    @Override
    public List<? extends FtlVariable> getGlobalVariables(final FtlFile file) {
        final Module module = ModuleUtilCore.findModuleForPsiElement(file);
        if (module == null) {
            return Collections.emptyList();
        }

        return null;
    }
}
