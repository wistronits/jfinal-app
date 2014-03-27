/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package com.github.sog.kit.format;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

/**
 * Scanf.
 */
public class Scanf {

	protected static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

	/**
	 * Scans input console and returns entered string.
	 */
	public static String scanf() {
		try {
			return in.readLine();
		} catch (IOException ioe) {
			return null;
		}
	}
}
