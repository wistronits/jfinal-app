package com.jfinal.ext.interceptor.autoscan;

import com.jfinal.aop.Interceptor;
import com.jfinal.config.Interceptors;
import com.jfinal.ctxbox.ClassBox;
import com.jfinal.ctxbox.ClassType;
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

    private static final Logger logger = Logger.getLogger(AutoOnLoadInterceptor.class);


    public AutoOnLoadInterceptor(Interceptors interceptors) {
        this.interceptors = interceptors;
    }

    public void load() {
        List<Class> interceptorClass = ClassBox.getInstance().getClasses(ClassType.AOP);
        if (interceptorClass != null && !interceptorClass.isEmpty()) {
            AppInterceptor interceptor;

            for (Class interceptorClas : interceptorClass) {
                interceptor = (AppInterceptor) interceptorClas.getAnnotation(AppInterceptor.class);
                if (interceptor != null) {
                    try {
                        interceptors.add((Interceptor) interceptorClas.newInstance());
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
}
