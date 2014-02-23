/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package com.jfinal.render;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import javax.servlet.ServletContext;
import static com.jfinal.core.Const.DEFAULT_FILE_CONTENT_TYPE;
import com.jfinal.kit.PathKit;
import com.jfinal.sog.kit.StringPool;

/**
 * FileRender.
 */
public class FileRender extends Render {
	
	private static final long serialVersionUID = 4293616220202691369L;
	private File file;
	private String fileName;
	private static String fileDownloadPath;
	private static ServletContext servletContext;
	private static String webRootPath;
	
	public FileRender(File file) {
		this.file = file;
	}
	
	public FileRender(String fileName) {
		this.fileName = fileName;
	}
	
	static void init(String fileDownloadPath, ServletContext servletContext) {
		FileRender.fileDownloadPath = fileDownloadPath;
		FileRender.servletContext = servletContext;
		webRootPath = PathKit.getWebRootPath();
	}
	
	public void render() {
		if (fileName != null) {
			if (fileName.startsWith(StringPool.SLASH))
				file = new File(webRootPath + fileName);
			else
				file = new File(fileDownloadPath + fileName);
		}
		
		if (file == null || !file.isFile() || file.length() > Integer.MAX_VALUE) {
            // response.sendError(HttpServletResponse.SC_NOT_FOUND);
            // return;
			
			// throw new RenderException("File not found!");
			RenderFactory.me().getErrorRender(404).setContext(request, response).render();
			return ;
        }
		
		try {
			response.addHeader("Content-disposition", "attachment; filename=" + new String(file.getName().getBytes("GBK"), "ISO8859-1"));
		} catch (UnsupportedEncodingException e) {
			response.addHeader("Content-disposition", "attachment; filename=" + file.getName());
		}
		
        String contentType = servletContext.getMimeType(file.getName());
        if (contentType == null) {
        	contentType = DEFAULT_FILE_CONTENT_TYPE;		// "application/octet-stream";
        }
        
        response.setContentType(contentType);
        response.setContentLength((int)file.length());
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = new BufferedInputStream(new FileInputStream(file));
            outputStream = response.getOutputStream();
            byte[] buffer = new byte[1024];
            for (int n = -1; (n = inputStream.read(buffer)) != -1;) {
                outputStream.write(buffer, 0, n);
            }
            outputStream.flush();
        }
        catch (Exception e) {
        	throw new RenderException(e);
        }
        finally {
            if (inputStream != null) {
                try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
            }
            if (outputStream != null) {
            	try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
            }
        }
	}
}


