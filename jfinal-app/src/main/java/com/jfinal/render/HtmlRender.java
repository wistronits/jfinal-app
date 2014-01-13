/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package com.jfinal.render;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * HtmlRender.
 */
public class HtmlRender extends Render {

    private static final long serialVersionUID = 8145891655998147406L;
    private static final String contentType = "text/html;charset=" + getEncoding();
    private final String text;

    public HtmlRender(String text) {
        this.text = text;
    }

    public void render() {
        PrintWriter writer = null;
        try {
            response.setHeader("Pragma", "no-cache");    // HTTP/1.0 caches might not implement Cache-Control and might only implement Pragma: no-cache
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);

            response.setContentType(contentType);
            writer = response.getWriter();
            writer.write(text);
            writer.flush();
        } catch (IOException e) {
            throw new RenderException(e);
        } finally {
            if (writer != null)
                writer.close();
        }
    }
}




