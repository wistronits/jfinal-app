

package com.jfinal.aop;

import com.jfinal.core.ActionInvocation;

/**
 * Interceptor.
 */
public interface Interceptor {
	void intercept(ActionInvocation ai);
}
