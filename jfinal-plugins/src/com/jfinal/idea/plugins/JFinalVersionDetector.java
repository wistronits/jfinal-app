/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package com.jfinal.idea.plugins;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.vfs.JarFile;
import com.intellij.openapi.vfs.JarFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.GlobalSearchScope;
import com.jfinal.idea.plugins.facet.JFinalLibraryType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sun.security.pkcs11.Secmod;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * <p>
 * .
 * </p>
 *
 * @author sagyf yang
 * @version 1.0 2014-02-19 14:49
 * @since JDK 1.6
 */
public class JFinalVersionDetector {


    private JFinalVersionDetector() {
    }

    @Nullable
    public static String detectJfinalVersion(@NotNull final Module module) {
        try {
            final JarFile strutsJar = getStrutsJar(module);
            if (strutsJar == null) {
                return null;
            }

            final JarFile.JarEntry zipEntry = strutsJar.getEntry("META-INF/maven/org.apache.struts/struts2-core/pom.properties");
            if (zipEntry == null) {
                return null;
            }

            final InputStream inputStream = strutsJar.getInputStream(zipEntry);
            final Properties properties = new Properties();
            properties.load(inputStream);
            return properties.getProperty("version");
        } catch (IOException e) {
            return null;
        }
    }

    @Nullable
    private static JarFile getStrutsJar(final Module module) throws IOException {
        final GlobalSearchScope scope = GlobalSearchScope.moduleRuntimeScope(module, false);
        final JavaPsiFacade psiManager = JavaPsiFacade.getInstance(module.getProject());

        final VirtualFile virtualFile = getStrutsClass(scope, psiManager);
        if (virtualFile == null || !(virtualFile.getFileSystem() instanceof JarFileSystem)) {
            return null;
        }

        return JarFileSystem.getInstance().getJarFile(virtualFile);
    }

    @Nullable
    private static VirtualFile getStrutsClass(final GlobalSearchScope scope, final JavaPsiFacade psiManager) {
        final PsiClass psiClass = psiManager.findClass(JFinalLibraryType.STRUTS_VERSION_CLASS, scope);
        if (psiClass == null) {
            return null;
        }

        final PsiFile psiFile = psiClass.getContainingFile();
        if (psiFile == null) {
            return null;
        }

        return psiFile.getVirtualFile();
    }
}
