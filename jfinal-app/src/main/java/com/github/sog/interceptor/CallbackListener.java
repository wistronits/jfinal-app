/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package com.github.sog.interceptor;

import com.jfinal.plugin.activerecord.Model;

public interface CallbackListener {

    void beforeSave(Model<?> m);

    void afterSave(Model<?> m);

    void beforeUpdate(Model<?> m);

    void afterUpdate(Model<?> m);

    void beforeDelete(Model<?> m);

    void afterDelete(Model<?> m);
}
