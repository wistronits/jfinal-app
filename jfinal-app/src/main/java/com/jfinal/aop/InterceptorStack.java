

package com.jfinal.aop;

import com.google.common.collect.Lists;
import com.jfinal.core.ActionInvocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * InterceptorStack.
 */
public abstract class InterceptorStack implements Interceptor {

    private final Interceptor[] inters;
    private List<Interceptor> interList;

    public InterceptorStack() {
        config();

        if (interList == null)
            throw new RuntimeException("You must invoke addInterceptors(...) to config your InterceptorStack");

        inters = interList.toArray(new Interceptor[interList.size()]);
        interList.clear();
        interList = null;
    }

    protected final InterceptorStack addInterceptors(Interceptor... interceptors) {
        if (interceptors == null || interceptors.length == 0)
            throw new IllegalArgumentException("Interceptors can not be null");

        if (interList == null)
            interList = Lists.newArrayList();

        Collections.addAll(interList, interceptors);

        return this;
    }

    public final void intercept(ActionInvocation ai) {
        new ActionInvocationWrapper(ai, inters).invoke();
    }

    public abstract void config();
}



