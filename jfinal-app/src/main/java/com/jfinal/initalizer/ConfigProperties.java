package com.jfinal.initalizer;

import com.google.common.io.InputSupplier;
import com.google.common.io.Resources;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

/**
 * <p>
 * 属性文件获取，并且当属性文件发生改变时，自动重新加载.
 * <p/>
 * 1. 可配置是否重启应用
 * </p>
 *
 * @author sagyf yang
 * @version 1.0 2014-01-13 10:24
 * @since JDK 1.6
 */
public class ConfigProperties {

    private static final String APPLICATION_PROP = "application.conf";
    private static final ThreadLocal<Properties> configProps = new ThreadLocal<Properties>();

    static {
        readConf();
    }

    /**
     * 重新加载配置文件
     */
    public static void reload() {
        configProps.remove();
        readConf();
    }

    /**
     * 读取配置文件
     */
    private static void readConf() {
        URL url = Resources.getResource(APPLICATION_PROP);
        if (url == null) {
            throw new IllegalArgumentException("Parameter of file can not be blank");
        }
        InputSupplier<InputStream> inputSupplier = Resources.newInputStreamSupplier(url);
        try {
            Properties p = new Properties();
            p.load(inputSupplier.getInput());

            if (checkNullOrEmpty(p)) {
                throw new IllegalArgumentException("Properties file can not be empty. " + url);
            }
            configProps.set(p);
        } catch (IOException e) {
            throw new IllegalArgumentException("Properties file can not be loading: " + url);
        }
    }

    /**
     * 如果属性文件为空或者没有内容，则返回true
     *
     * @param p 属性信息
     * @return 是否为空或者没有内容
     */
    private static boolean checkNullOrEmpty(Properties p) {
        return p == null || p.isEmpty();
    }

    /**
     * 获取系统的配置
     *
     * @return 系统配置信息
     */
    public static Properties getConfigProps() {
        return configProps.get();
    }

    /**
     * 根据配置项获取配置信息
     *
     * @param key 配置项
     * @return 配置信息
     */
    public static String getProp(String key, String default_value) {
        if (checkNullOrEmpty(configProps.get())) {
            readConf();
        }
        return getConfigProps().getProperty(key, default_value);
    }


}
