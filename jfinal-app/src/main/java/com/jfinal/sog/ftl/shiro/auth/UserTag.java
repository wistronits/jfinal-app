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
import org.apache.shiro.subject.Subject;

/**
 * <p>
 * .
 * </p>
 *
 * @author poplar.yfyang
 * @version 1.0 2012-10-27 10:28 AM
 * @see org.apache.shiro.web.tags.UserTag
 * @since JDK 1.5
 */
public class UserTag extends SecureTag {

	@Override
	public void render(Environment env, Map params, TemplateDirectiveBody body) throws IOException, TemplateException {
		final Subject subject = getSubject();
		if (subject != null && subject.getPrincipal() != null) {
//			_logger.debug("Subject has known identity (aka 'principal'). Tag body will be evaluated.");
			renderBody(env, body);
		} else {
			_logger.debug("Subject does not exist or have a known identity (aka 'principal'). Tag body will not be evaluated.");
		}
	}
}
