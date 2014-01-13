

package com.jfinal.aop;

/**
 * ClearLayer indicates ClearIntercptor which layer of interceptor should be cleared.
 * The JFinal interceptor has 3 layers, there are Global, Controller and Action.
 */
public enum ClearLayer {
	
	/**
	 * clear the interceptor of upper layer
	 */
	UPPER,
	
	/**
	 * clear the interceptor of all layers
	 */
	ALL
}


