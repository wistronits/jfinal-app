/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2013 sagyf Yang. The Four Group.
 */

package com.sagyf.jfinal;

import com.intellij.codeInsight.intention.IntentionManager;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.sagyf.jfinal.intention.AddControllerIntention;
import org.jetbrains.annotations.NotNull;

/**
 * <p>
 * .
 * </p>
 *
 * @author sagyf yang
 * @version 1.0 2013-12-29 21:20
 * @since JDK 1.6
 */
public class JFinalProjectComponent implements ProjectComponent {
    public JFinalProjectComponent(Project project) {
    }
    @Override
    public void projectOpened() {

    }

    @Override
    public void projectClosed() {

    }

    @Override
    public void initComponent() {
        IntentionManager intentionManager = IntentionManager.getInstance();
        intentionManager.registerIntentionAndMetaData(new AddControllerIntention(), Constants.INTENSION_INSPECTION_GROUPNAME);
    }

    @Override
    public void disposeComponent() {

    }

    @NotNull
    @Override
    public String getComponentName() {
        return "JFinal Project Component";
    }
}
