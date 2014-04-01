package com.jfinal.module.wxchat.api.beans;

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
    private String access_token;
    private long expires_in;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String accessToken) {
        access_token = accessToken;
    }

    public long getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(long expiresIn) {
        expires_in = expiresIn;
    }
}
