package com.jfinal.module.wxchat.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 *     正则表达式工具类
 * </p>
 *
 * @author Jerry Ou
 * @version 1.0 2014-03-21 21:03
 * @since JDK 1.6
 */
public final class RegexUtil {
    /** 邮箱验证正则 */
    private static final String EMAIL_PATTERN = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
    /** 手机验证正则 */
    private static final String MOBILE_PATTERN = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
    /** 数字验证正则 */
    private static final String NUMBER_PATTERN = "^([1-9]\\d*)|(0)$";

    /**
     * 验证邮箱地址是否正确
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        boolean flag;
        try {
            Pattern regex = Pattern.compile(EMAIL_PATTERN);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * 验证手机号码
     *
     * @param mobiles
     * @return [0-9]{5,9}
     */
    public static boolean isMobile(String mobiles) {
        boolean flag;
        try {
            Pattern p = Pattern.compile(MOBILE_PATTERN);
            Matcher m = p.matcher(mobiles);
            flag = m.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * 验证数字
     *
     * @param number
     * @return
     */
    public static boolean isNumber(String number) {
        boolean flag;
        try {
            Pattern p = Pattern.compile(NUMBER_PATTERN);
            Matcher m = p.matcher(number);
            flag = m.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

}
