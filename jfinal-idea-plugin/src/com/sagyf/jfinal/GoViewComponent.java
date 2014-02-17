/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2013 sagyf Yang. The Four Group.
 */

package com.sagyf.jfinal;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataConstants;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

/**
 * <p>
 * .
 * </p>
 *
 * @author sagyf yang
 * @version 1.0 2013-12-25 21:38
 * @since JDK 1.6
 */
public class GoViewComponent extends AnAction {
    private static final Logger logger = Logger.getInstance(GoViewComponent.class.getName());
    @Override
    public void actionPerformed(AnActionEvent event) {
        logger.info("user jfinal goto view");
        Project project = (Project)event.getDataContext().getData(DataConstants.PROJECT);

        VirtualFile[] selectedFile = FileEditorManager.getInstance(project).
                getSelectedFiles();
    }
}
