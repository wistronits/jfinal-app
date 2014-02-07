/**
 * Copyright (c) 2011-2013, James Zhan 詹波 (jfinal@126.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jfinal.kit;

import com.jfinal.sog.kit.lang.CharKit;
import org.apache.commons.lang3.StringUtils;

/**
 * StringKit.
 */
public class StringKit {
	
	/**
	 * 首字母变小写
	 */
	public static String firstCharToLowerCase(String str) {
		Character firstChar = str.charAt(0);
		String tail = str.substring(1);
		str = Character.toLowerCase(firstChar) + tail;
		return str;
	}
	
	/**
	 * 首字母变大写
	 */
	public static String firstCharToUpperCase(String str) {
		Character firstChar = str.charAt(0);
		String tail = str.substring(1);
		str = Character.toUpperCase(firstChar) + tail;
		return str;
	}
	
	/**
	 * 字符串为 null 或者为  "" 时返回 true
	 */
	public static boolean isBlank(String str) {
		return str == null || "".equals(str.trim());
	}
	
	/**
	 * 字符串不为 null 而且不为  "" 时返回 true
	 */
	public static boolean notBlank(String str) {
		return str == null || "".equals(str.trim()) ? false : true;
	}
	
	public static boolean notBlank(String... strings) {
		if (strings == null)
			return false;
		for (String str : strings)
			if (str == null || "".equals(str.trim()))
				return false;
		return true;
	}
	
	public static boolean notNull(Object... paras) {
		if (paras == null)
			return false;
		for (Object obj : paras)
			if (obj == null)
				return false;
		return true;
	}


    /**
     * Returns <code>true</code> if string contains only digits.
     */
    public static boolean containsOnlyDigits(String string) {
        int size = string.length();
        for (int i = 0; i < size; i++) {
            char c = string.charAt(i);
            if (!CharKit.isDigit(c)) {
                return false;
            }
        }
        return true;
    }



    // ---------------------------------------------------------------- char based


    /**
     * @see #indexOfChars(String, String, int)
     */
    public static int indexOfChars(String string, String chars) {
        return indexOfChars(string, chars, 0);
    }

    /**
     * Returns the very first index of any char from provided string, starting from specified index offset.
     * Returns index of founded char, or <code>-1</code> if nothing found.
     */
    public static int indexOfChars(String string, String chars, int startindex) {
        int stringLen = string.length();
        int charsLen = chars.length();
        if (startindex < 0) {
            startindex = 0;
        }
        for (int i = startindex; i < stringLen; i++) {
            char c = string.charAt(i);
            for (int j = 0; j < charsLen; j++) {
                if (c == chars.charAt(j)) {
                    return i;
                }
            }
        }
        return -1;
    }

    public static int indexOfChars(String string, char[] chars) {
        return indexOfChars(string, chars, 0);
    }

    /**
     * Returns the very first index of any char from provided string, starting from specified index offset.
     * Returns index of founded char, or <code>-1</code> if nothing found.
     */
    public static int indexOfChars(String string, char[] chars, int startindex) {
        int stringLen = string.length();
        for (int i = startindex; i < stringLen; i++) {
            char c = string.charAt(i);
            for (char aChar : chars) {
                if (c == aChar) {
                    return i;
                }
            }
        }
        return -1;
    }



    /**
     * Returns if string starts with given character.
     */
    public static boolean startsWithChar(String s, char c) {
        return s.length() != 0 && s.charAt(0) == c;
    }



    /**
     * Returns <code>true</code> if string {@link #containsOnlyDigits(String) contains only digits}
     * or signs plus or minus.
     */
    public static boolean containsOnlyDigitsAndSigns(String string) {
        int size = string.length();
        for (int i = 0; i < size; i++) {
            char c = string.charAt(i);
            if ((!CharKit.isDigit(c)) && (c != '-') && (c != '+')) {
                return false;
            }
        }
        return true;
    }

    /**
     * Splits a string in several parts (tokens) that are separated by delimiter
     * characters. Delimiter may contains any number of character and it is
     * always surrounded by two strings.
     *
     * @param src    source to examine
     * @param d      string with delimiter characters
     *
     * @return array of tokens
     */
    public static String[] splitc(String src, String d) {
        if ((d.length() == 0) || (src.length() == 0) ) {
            return new String[] {src};
        }
        char[] delimiters = d.toCharArray();
        char[] srcc = src.toCharArray();

        int maxparts = srcc.length + 1;
        int[] start = new int[maxparts];
        int[] end = new int[maxparts];

        int count = 0;

        start[0] = 0;
        int s = 0, e;
        if (CharKit.equalsOne(srcc[0], delimiters)) {	// string starts with delimiter
            end[0] = 0;
            count++;
            s = CharKit.findFirstDiff(srcc, 1, delimiters);
            if (s == -1) {							// nothing after delimiters
                return new String[] {StringUtils.EMPTY, StringUtils.EMPTY};
            }
            start[1] = s;							// new start
        }
        while (true) {
            // find new end
            e = CharKit.findFirstEqual(srcc, s, delimiters);
            if (e == -1) {
                end[count] = srcc.length;
                break;
            }
            end[count] = e;

            // find new start
            count++;
            s = CharKit.findFirstDiff(srcc, e, delimiters);
            if (s == -1) {
                start[count] = end[count] = srcc.length;
                break;
            }
            start[count] = s;
        }
        count++;
        String[] result = new String[count];
        for (int i = 0; i < count; i++) {
            result[i] = src.substring(start[i], end[i]);
        }
        return result;
    }

    /**
     * Splits a string in several parts (tokens) that are separated by single delimiter
     * characters. Delimiter is always surrounded by two strings.
     *
     * @param src           source to examine
     * @param delimiter     delimiter character
     *
     * @return array of tokens
     */
    public static String[] splitc(String src, char delimiter) {
        if (src.length() == 0) {
            return new String[] {StringUtils.EMPTY};
        }
        char[] srcc = src.toCharArray();

        int maxparts = srcc.length + 1;
        int[] start = new int[maxparts];
        int[] end = new int[maxparts];

        int count = 0;

        start[0] = 0;
        int s = 0, e;
        if (srcc[0] == delimiter) {	// string starts with delimiter
            end[0] = 0;
            count++;
            s = CharKit.findFirstDiff(srcc, 1, delimiter);
            if (s == -1) {							// nothing after delimiters
                return new String[] {StringUtils.EMPTY, StringUtils.EMPTY};
            }
            start[1] = s;							// new start
        }
        while (true) {
            // find new end
            e = CharKit.findFirstEqual(srcc, s, delimiter);
            if (e == -1) {
                end[count] = srcc.length;
                break;
            }
            end[count] = e;

            // find new start
            count++;
            s = CharKit.findFirstDiff(srcc, e, delimiter);
            if (s == -1) {
                start[count] = end[count] = srcc.length;
                break;
            }
            start[count] = s;
        }
        count++;
        String[] result = new String[count];
        for (int i = 0; i < count; i++) {
            result[i] = src.substring(start[i], end[i]);
        }
        return result;
    }
}




