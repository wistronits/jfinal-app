/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package com.jfinal.sog.ftl.shiro;

import com.jfinal.sog.ftl.shiro.auth.*;
import com.jfinal.sog.ftl.shiro.permission.HasPermissionTag;
import com.jfinal.sog.ftl.shiro.permission.LacksPermissionTag;
import com.jfinal.sog.ftl.shiro.role.HasAnyRolesTag;
import com.jfinal.sog.ftl.shiro.role.HasRoleTag;
import com.jfinal.sog.ftl.shiro.role.LacksRoleTag;
import freemarker.template.SimpleHash;

/**
 * <p>
 * Shirio权限验证 Freemarker 标签.
 * </p>
 *
 * @author poplar.yfyang
 * @version 1.0 2012-10-27 10:42 AM
 * @since JDK 1.5
 */
public class ShiroTags extends SimpleHash {

    /**
     * Constructs an empty hash that uses the default wrapper set in
     * {@link freemarker.template.WrappingTemplateModel#setDefaultObjectWrapper(freemarker.template.ObjectWrapper)}.
     */
    public ShiroTags() {
        put("authenticated", new AuthenticatedTag());
        put("guest", new GuestTag());
        put("hasAnyRoles", new HasAnyRolesTag());
        put("hasPermission", new HasPermissionTag());
        put("hasRole", new HasRoleTag());
        put("lacksPermission", new LacksPermissionTag());
        put("lacksRole", new LacksRoleTag());
        put("notAuthenticated", new NotAuthenticatedTag());
        put("principal", new PrincipalTag());
        put("user", new UserTag());
    }
}
