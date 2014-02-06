/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package com.jfinal.sog.initalizer.security;

import com.alibaba.fastjson.annotation.JSONCreator;
import com.alibaba.fastjson.annotation.JSONField;
import com.google.common.base.Objects;

import java.io.Serializable;

/**
 * <p>
 * The Security User.
 * </p>
 *
 * @author walter yang
 * @version 1.0 2013-10-26 9:57 AM
 * @since JDK 1.5
 */
public class SecurityUser<U> implements Serializable {
    private static final long serialVersionUID = -4452393798317565037L;
    /**
     * The Id.
     */
    public final long   id;
    /**
     * The Name.
     */
    public final String name;
    /**
     * The Email.
     */
    public final String email;
    /**
     * The Avator.
     */
    public final U      data;

    /**
     * Instantiates a new Shiro user.
     *
     * @param id       the id
     * @param name     the name
     * @param email    the email
     * @param data     the user information
     */
    @JSONCreator
    public SecurityUser(@JSONField(name = "id") long id
            , @JSONField(name = "name") String name
            , @JSONField(name = "email") String email
            , @JSONField(name = "data") U data) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.data = data;
    }


    /**
     * Gets id.
     *
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * get other information.
     *
     * @return information.
     */
    public U getData() {
        return data;
    }

    /**
     * 本函数输出将作为默认的<shiro:principal/>输出.
     */
    @Override
    public String toString() {
        return name;
    }
}
