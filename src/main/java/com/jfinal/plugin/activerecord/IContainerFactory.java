/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package com.jfinal.plugin.activerecord;

import java.util.Map;
import java.util.Set;

public interface IContainerFactory {
    Map getAttrsMap();

    Map<String, Object> getColumnsMap();

    Set<String> getModifyFlagSet();
}
