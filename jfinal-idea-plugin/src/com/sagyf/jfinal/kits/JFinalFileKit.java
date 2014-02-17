/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2013 sagyf Yang. The Four Group.
 */

package com.sagyf.jfinal.kits;

import com.intellij.CommonBundle;
import com.intellij.codeInsight.CodeInsightBundle;
import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.ide.fileTemplates.FileTemplateUtil;
import com.intellij.ide.util.PackageUtil;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.application.Result;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.pointers.VirtualFilePointer;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import com.intellij.refactoring.PackageWrapper;
import com.intellij.refactoring.move.moveClassesOrPackages.MoveClassesOrPackagesUtil;
import com.intellij.refactoring.util.RefactoringMessageUtil;
import com.intellij.refactoring.util.RefactoringUtil;
import com.intellij.util.IncorrectOperationException;
import com.intellij.util.SmartList;
import com.sagyf.jfinal.Constants;
import com.sagyf.jfinal.facet.JFinalFacet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * <p>
 * .
 * </p>
 *
 * @author sagyf yang
 * @version 1.0 2013-12-29 21:37
 * @since JDK 1.6
 */
public final class JFinalFileKit {

    private JFinalFileKit() {
    }

    @NotNull
    public static VirtualFile[] getResourceRoots(@NotNull Module module) {
        // all module source roots
        VirtualFile[] result = ModuleRootManager.getInstance(module).getSourceRoots();
        // alternate paths
        JFinalFacet jFinalFacet = JFinalFacet.getInstance(module);
        if (jFinalFacet != null) {
            List<VirtualFile> alternateFiles = new SmartList<VirtualFile>();
            // add all valid alternate paths to list
            for (VirtualFilePointer virtualFilePointer : jFinalFacet.getResourcePaths()) {
                VirtualFile virtualFile = virtualFilePointer.getFile();
                if (virtualFile != null && virtualFile.isValid()) {
                    alternateFiles.add(virtualFile);
                }
            }
            // if we have valid alternate paths
            if (!alternateFiles.isEmpty()) {
                // add all module source roots and list as new result
                alternateFiles.addAll(Arrays.asList(result));
                result = alternateFiles.toArray(new VirtualFile[alternateFiles.size()]);
            }
        }
        //
        return result;
    }

    /**
     * @param packageName PackageName like 'com.foo.bar'
     * @param project     Project
     * @param module      Module
     * @return Selected Directory or null if canceled/error
     */
    @Nullable
    public static PsiDirectory selectTargetDirectory(@NotNull final String packageName, @NotNull final Project project, @NotNull final Module module) {
        final PackageWrapper targetPackage = new PackageWrapper(PsiManager.getInstance(project), packageName);

        final VirtualFile selectedRoot = new ReadAction<VirtualFile>() {
            @Override
            protected void run(Result<VirtualFile> result) throws Throwable {
                VirtualFile[] roots = getResourceRoots(module);
                if (roots.length == 0) return;

                if (roots.length == 1) {
                    result.setResult(roots[0]);
                } else {
                    PsiDirectory defaultDir = PackageUtil.findPossiblePackageDirectoryInModule(module, packageName);
                    result.setResult(MoveClassesOrPackagesUtil.chooseSourceRoot(targetPackage, new SmartList<VirtualFile>(roots), defaultDir));
                }
            }
        }.execute().getResultObject();

        if (selectedRoot == null) {
            return null;
        }

        try {
            return new WriteCommandAction<PsiDirectory>(project, CodeInsightBundle.message("create.directory.command")) {
                @Override
                protected void run(Result<PsiDirectory> result) throws Throwable {
                    result.setResult(RefactoringUtil.createPackageDirectoryInSourceRoot(targetPackage, selectedRoot));
                }
            }.execute().getResultObject();
        } catch (IncorrectOperationException e) {
            Messages.showMessageDialog(project, e.getMessage(), CommonBundle.getErrorTitle(), Messages.getErrorIcon());
            return null;
        }
    }

    /**
     * Creates and returns the file for the passed PsiClass.
     *
     * @param fileName     the name of the file to create
     * @param directory    the directory to create in
     * @param templateName the Markup Template name
     * @return the created Element from Template
     */
    @Nullable
    public static PsiElement createFileFromTemplate(@NotNull String fileName, @NotNull PsiDirectory directory, @NotNull String templateName) {
        String errorMessage = RefactoringMessageUtil.checkCanCreateFile(directory, fileName);
        if (errorMessage != null) {
            Messages.showMessageDialog(directory.getProject(), errorMessage, CommonBundle.getErrorTitle(), Messages.getErrorIcon());
            return null;
        }

        final FileTemplate template = FileTemplateManager.getInstance().getJ2eeTemplate(templateName);

        Properties props = FileTemplateManager.getInstance().getDefaultProperties();
        try {
            return FileTemplateUtil.createFromTemplate(template, fileName, props, directory);
        } catch (Exception e) {
            throw new RuntimeException("Unable to create template for '" + fileName + "'", e);
        }
    }

    /**
     * @param vf
     * @return true if file is in library
     */
    public static boolean isInLibrary(@NotNull VirtualFile vf, @NotNull Project project) {
        ProjectFileIndex projectFileIndex = ProjectRootManager.getInstance(project).getFileIndex();
        return projectFileIndex.isInLibrarySource(vf) || projectFileIndex.isInLibraryClasses(vf);
    }
}
