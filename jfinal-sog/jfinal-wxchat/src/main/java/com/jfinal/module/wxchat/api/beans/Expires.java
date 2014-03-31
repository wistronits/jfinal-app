package com.jfinal.module.wxchat.api.beans;

/**
 * <p>
 *     可以有有效期的对象
 * </p>
 *
 * @author Jerry Ou
 * @version 1.0 2014-02-12 13:41
 * @since JDK 1.6
 */
public abstract class Expires {
    private long access; //访问时间

    public long getAccess() {
        return access;
    }

    public void setAccess(long access) {
        this.access = access;
    }

    public abstract long getExpires_in();


}
