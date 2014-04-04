package com.jfinal.module.wxchat.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * <p>
 *     微信公众帐号配置文件
 * </p>
 *
 * @author Jerry Ou
 * @version 1.0 2014-03-26 00:03
 * @since JDK 1.6
 */
public class Configuration {
    protected Logger logger = LoggerFactory.getLogger(Configuration.class);

    public static final String DEFAULR_TOKEN = "token";

    private String appid;
    private String appsecret;
    private String token = DEFAULR_TOKEN;
    private String menu;

    public final static Configuration me = new Configuration();

    private Properties properties;

    private Configuration() {

    }

    /**
     * 初始化配置
     *
     * @param properties
     */
    public void init(Properties properties) {
        this.properties = properties;

        logger.info("init properties configuration");
        appid = (String) properties.get(Constants.WECHAT_APPID);
        appsecret = (String) properties.get(Constants.WECHAT_APPSECRET);
        token = (String) properties.get(Constants.WECHAT_TOKEN);
        menu = (String)  properties.get(Constants.WECHAT_MENU);
    }

    /**
     * 获取属性方法
     * @param key
     * @return
     */
    public String get(String key) {
        return (String) properties.get(key);
    }

    public String getAppid() {
        return appid;
    }

    public String getAppsecret() {
        return appsecret;
    }

    public String getToken() {
        return token;
    }

    public String getMenu() {
        return menu;
    }
}
