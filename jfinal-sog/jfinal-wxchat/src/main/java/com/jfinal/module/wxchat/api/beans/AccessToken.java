package com.jfinal.module.wxchat.api.beans;

import com.alibaba.fastjson.annotation.JSONCreator;

/**
 * <p>
 * AccessToken json 对应实体
 * </p>
 *
 * @author Jerry Ou
 * @version 1.0 2014-02-12 13:41
 * @since JDK 1.6
 */
public class AccessToken extends Expires {
    private  final String access_token;
    private final long expires_in;

    @JSONCreator
    public AccessToken(String access_token, long expires_in) {
        this.access_token = access_token;
        this.expires_in = expires_in;
    }

    public String getAccess_token() {
        return access_token;
    }


    public long getExpires_in() {
        return expires_in;
    }

}
