/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package com.jfinal.idea.plugins.facet;

import com.intellij.facet.ui.FacetBasedFrameworkSupportProvider;
import com.intellij.framework.library.DownloadableLibraryService;
import com.intellij.framework.library.FrameworkSupportWithLibrary;
import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateUtil;
import com.intellij.ide.util.frameworkSupport.FrameworkSupportConfigurableBase;
import com.intellij.ide.util.frameworkSupport.FrameworkSupportModel;
import com.intellij.ide.util.frameworkSupport.FrameworkSupportProviderBase;
import com.intellij.ide.util.frameworkSupport.FrameworkVersion;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationListener;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.ui.configuration.ModulesConfigurator;
import com.intellij.openapi.roots.ui.configuration.libraries.CustomLibraryDescription;
import com.intellij.openapi.startup.StartupManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.xml.XmlFile;
import com.intellij.util.containers.MultiMap;
import com.intellij.util.ui.UIUtil;
import com.jfinal.idea.plugins.JFinalConstants;
import com.jfinal.idea.plugins.JFinalFileTemplateProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.event.HyperlinkEvent;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * .
 * </p>
 *
 * @author sagyf yang
 * @version 1.0 2014-02-19 14:41
 * @since JDK 1.6
 */
public class JFinalFrameworkSupportProvider extends FacetBasedFrameworkSupportProvider<JFinalFacet> {

    private static final Logger LOG = Logger.getInstance("#com.jfinal.idea.plugins.facet.JFinalFrameworkSupportProvider");

    public JFinalFrameworkSupportProvider() {
        super(JFinalFacetType.getInstance());
    }


    @Override
    public String getTitle() {
        return UIUtil.replaceMnemonicAmpersand("JFinal");
    }



    @NotNull
    @Override
    public FrameworkSupportConfigurableBase createConfigurable(@NotNull final FrameworkSupportModel model) {
        return new JFinalFrameworkSupportConfigurable(this, model, getVersions(), getVersionLabelText());
    }

    @Override
    protected void setupConfiguration(JFinalFacet jFinalFacet, ModifiableRootModel modifiableRootModel, FrameworkVersion frameworkVersion) {

    }

    @Override
    protected void onFacetCreated(final JFinalFacet jFinalFacet,
                                  final ModifiableRootModel modifiableRootModel,
                                  final FrameworkVersion version) {
        final Module module = jFinalFacet.getModule();
        StartupManager.getInstance(module.getProject()).runWhenProjectIsInitialized(new Runnable() {
            public void run() {
                final VirtualFile[] sourceRoots = ModuleRootManager.getInstance(module).getSourceRoots();
                if (sourceRoots.length <= 0) {
                    return;
                }

                final PsiDirectory directory = PsiManager.getInstance(module.getProject()).findDirectory(sourceRoots[0]);
                if (directory == null ||
                        directory.findFile(JFinalConstants.JFINAL_APPLICATION_CONFIG_FILENAME) != null) {
                    return;
                }

                final JFinalFileTemplateProvider templateProvider = new JFinalFileTemplateProvider(module);
                final FileTemplate strutsXmlTemplate = templateProvider.determineFileTemplate();

                try {
                    final JFinalFacetConfiguration strutsFacetConfiguration = jFinalFacet.getConfiguration();

                    // create empty struts.xml & fileset with all found struts-*.xml files (struts2.jar, plugins)
                    final PsiElement psiElement = FileTemplateUtil.createFromTemplate(strutsXmlTemplate,
                            JFinalConstants.JFINAL_APPLICATION_CONFIG_FILENAME,
                            null,
                            directory);






                    final NotificationListener showFacetSettingsListener = new NotificationListener() {
                        public void hyperlinkUpdate(@NotNull final Notification notification,
                                                    @NotNull final HyperlinkEvent event) {
                            if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                                notification.expire();
                                ModulesConfigurator.showFacetSettingsDialog(jFinalFacet, null);
                            }
                        }
                    };

                    Notifications.Bus.notify(
                            new Notification("Jfinal", "JFinal Setup",
                                    "JFinal Facet has been created, please check <a href=\"more\">created fileset</a>",
                                    NotificationType.INFORMATION,
                                    showFacetSettingsListener),
                            module.getProject());

                } catch (Exception e) {
                    LOG.error("error creating struts.xml from template", e);
                }
            }
        });
    }

    private static class JFinalFrameworkSupportConfigurable extends FrameworkSupportConfigurableBase
            implements FrameworkSupportWithLibrary {

        private JFinalFrameworkSupportConfigurable(FrameworkSupportProviderBase frameworkSupportProvider,
                                                    FrameworkSupportModel model,
                                                    @NotNull List<FrameworkVersion> versions,
                                                    @Nullable String versionLabelText) {
            super(frameworkSupportProvider, model, versions, versionLabelText);
        }

        @NotNull
        @Override
        public CustomLibraryDescription createLibraryDescription() {
            return DownloadableLibraryService.getInstance().createDescriptionForType(JFinalLibraryType.class);
        }

        @Override
        public boolean isLibraryOnly() {
            return false;
        }
    }
}
