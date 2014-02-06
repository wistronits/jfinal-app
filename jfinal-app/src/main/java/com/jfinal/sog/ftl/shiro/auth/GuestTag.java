/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package com.jfinal.sog.ftl.shiro.auth;

import java.io.IOException;
import java.util.Map;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;

/**
 * <p>
 * .
 * </p>
 *
 * @author poplar.yfyang
 * @version 1.0 2012-10-27 10:33 AM
 * @since JDK 1.5
 */
public class GuestTag extends SecureTag {


	@Override
	public void render(Environment env, Map params, TemplateDirectiveBody body) throws IOException, TemplateException {
		if (getSubject() == null || getSubject().getPrincipal() == null) {
			if (_logger.isDebugEnabled()) {
				_logger.debug("Subject does not exist or does not have a known identity (aka 'principal').  " +
						"Tag body will be evaluated.");
			}

			renderBody(env, body);
		} else {
			if (_logger.isDebugEnabled()) {
				_logger.debug("Subject exists or has a known identity (aka 'principal').  " +
						"Tag body will not be evaluated.");
			}
		}
	}
}
