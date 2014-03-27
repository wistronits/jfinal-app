/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package com.github.sog.controller.session.ref;

/**
 * <p>
 * .
 * </p>
 *
 * @author sagyf yang
 * @version 1.0 2014-01-31 2:06
 * @since JDK 1.6
 */
public enum ReferenceType {

    /**
     * Prevents referent from being reclaimed by the garbage collector.
     */
    STRONG,

    /**
     * Referent reclaimed in an LRU fashion when the VM runs low on memory and
     * no strong references exist.
     *
     * @see java.lang.ref.SoftReference
     */
    SOFT,

    /**
     * Referent reclaimed when no strong or soft references exist.
     *
     * @see java.lang.ref.WeakReference
     */
    WEAK,

    /**
     * Similar to weak references except the garbage collector doesn't actually
     * reclaim the referent. More flexible alternative to finalization.
     *
     * @see java.lang.ref.PhantomReference
     */
    PHANTOM,
}
