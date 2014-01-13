package com.jfinal.ext.interceptor.autoscan;

import com.jfinal.aop.Interceptor;
import com.jfinal.config.Interceptors;
import com.jfinal.ext.kit.ClassSearcher;
import com.jfinal.log.Logger;

import java.util.List;

/**
 * <p>
 * .
 * </p>
 *
 * @author sagyf yang
 * @version 1.0 2014-01-04 13:11
 * @since JDK 1.6
 */
public class AutoOnLoadInterceptor {

    private final Interceptors interceptors;

    private final String aop_package;

    private static final Logger logger = Logger.getLogger(AutoOnLoadInterceptor.class);


    public AutoOnLoadInterceptor(Interceptors interceptors, String aop_package) {
        this.interceptors = interceptors;
        this.aop_package = aop_package;
    }

    public void load() {
        ClassSearcher searcher = ClassSearcher.of(Interceptor.class, aop_package);
        List<Class<? extends Interceptor>> interceptorClass = searcher.search();
        com.jfinal.ext.interceptor.autoscan.Interceptor interceptor;

        for (Class<? extends Interceptor> interceptorClas : interceptorClass) {
            interceptor = interceptorClas.getAnnotation(com.jfinal.ext.interceptor.autoscan.Interceptor.class);
            if (interceptor != null) {
                try {
                    interceptors.add(interceptorClas.newInstance());
                } catch (InstantiationException e) {
                    logger.error("instance aop interceptor is error!", e);
                    throw new IllegalArgumentException("instance aop interceptor is error!");
                } catch (IllegalAccessException e) {
                    logger.error("instance aop interceptor is error!", e);
                    throw new IllegalArgumentException("instance aop interceptor is error!");
                }
            }
        }


    }
}
