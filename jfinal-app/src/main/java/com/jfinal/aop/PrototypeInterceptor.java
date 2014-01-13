
package com.jfinal.aop;

import com.jfinal.core.ActionInvocation;

/**
 * PrototypeInterceptor.
 */
public abstract class PrototypeInterceptor implements Interceptor {
	
	final public void intercept(ActionInvocation ai) {
		try {
			getClass().newInstance().doIntercept(ai);
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	abstract public void doIntercept(ActionInvocation ai);
}
