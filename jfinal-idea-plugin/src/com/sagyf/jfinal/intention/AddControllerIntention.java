/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2013 sagyf Yang. The Four Group.
 */

package com.sagyf.jfinal.intention;

import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.util.IncorrectOperationException;
import com.sagyf.jfinal.kits.JFinalFileKit;
import org.jetbrains.annotations.NotNull;

/**
 * <p>
 * .
 * </p>
 *
 * @author sagyf yang
 * @version 1.0 2013-12-29 21:33
 * @since JDK 1.6
 */
public class AddControllerIntention implements IntentionAction {
    @NotNull
    @Override
    public String getText() {
        return "Create JFinal Controller";
    }

    @NotNull
    @Override
    public String getFamilyName() {
        return "Create JFinal Controller";
    }

    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile file) {
        if (!(file instanceof PsiJavaFile)) {
            return false;
        }
        int offset = editor.getCaretModel().getOffset();
        PsiElement element = file.findElementAt(offset);
        if (element == null) {
            return false;
        }

        element = element.getParent();
        if (element == null || !(element instanceof PsiClass)) {
            return false;
        }


        PsiClass psiClass = (PsiClass) element;

        return psiClass.getName() != null;
    }

    @Override
    public void invoke(@NotNull Project project, Editor editor, PsiFile file) throws IncorrectOperationException {
        int offset = editor.getCaretModel().getOffset();
        PsiElement element = file.findElementAt(offset);
        if (element != null) {
            element = element.getParent();
            if (!(element instanceof PsiClass)) {
                return;
            }
            PsiDirectory fileDirectory = file.getContainingDirectory();
            if (fileDirectory == null) {
                return;
            }
            Module module = ModuleUtil.findModuleForPsiElement(fileDirectory);
            if (module == null) {
                return;
            }
            PsiPackage psiPackage = JavaDirectoryService.getInstance().getPackage(fileDirectory);
            if (psiPackage == null) {
                return;
            }

            PsiDirectory directory = JFinalFileKit.selectTargetDirectory(psiPackage.getQualifiedName(), project, module);
            if (directory != null) {
                JFinalFileKit.createFileFromTemplate(null, directory, "");
            }
        }
    }

    @Override
    public boolean startInWriteAction() {
        return true;
    }
}
