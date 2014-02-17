/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2013 sagyf Yang. The Four Group.
 */

package com.sagyf.jfinal.annotators;

import com.intellij.ide.util.PsiElementListCellRenderer;
import com.intellij.psi.PsiElement;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.DomManager;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Renderer for "GoTo"-gutter-popups.
 *
 * @param <T> Navigation target DOM element type.
 * @author Dmitry Avdeev
 * @author Yann C&eacute;bron
 */
abstract class DomElementListCellRenderer<T extends DomElement> extends PsiElementListCellRenderer<XmlTag> {

  private final String unknownElementText;

  protected DomElementListCellRenderer(@Nls final String unknownElementText) {
    this.unknownElementText = unknownElementText;
  }

  /**
   * Gets an additional location string.
   *
   * @param domElement Target navigation element.
   * @return Empty String if not suitable.
   */
  @NotNull
  @NonNls
  public abstract String getAdditionalLocation(final T domElement);

  public String getElementText(final XmlTag element) {
    final DomElement domElement = getDomElement(element);
    if (domElement == null) {
      return element.getName();
    }

    final String elementName = domElement.getPresentation().getElementName();
    return elementName == null ? unknownElementText : elementName;
  }

  protected String getContainerText(final XmlTag element, final String name) {
    final String containingFile = " (" + element.getContainingFile().getName() + ')';

    final T domElement = getDomElement(element);
    if (domElement == null) {
      return containingFile;
    }

    return getAdditionalLocation(domElement) + containingFile;
  }

  protected int getIconFlags() {
    return 0;
  }

  protected Icon getIcon(final PsiElement element) {
    final DomElement domElement = getDomElement((XmlTag) element);
    if (domElement != null) {
      final Icon icon = domElement.getPresentation().getIcon();
      if (icon != null) {
        return icon;
      }
    }

    return super.getIcon(element);
  }

  @SuppressWarnings({"unchecked"})
  @Nullable
  private T getDomElement(@NotNull final XmlTag tag) {
    return (T) DomManager.getDomManager(tag.getProject()).getDomElement(tag);
  }

}