package com.thambola.fungola.Utils;

import java.util.Vector;

/**
 * Utility class useful when dealing with string objects. This class is a
 * collection of static functions, and the usage is:
 *
 * StringUtil.method()
 *
 * it is not allowed to create instances of this class
 */
public class StringUtil {

    private static final String HT = "\t";
    private static final String CRLF = "\r\n";

    // This class cannot be instantiated
    public StringUtil() {
    }

    /**
     * Split the string into an array of strings using one of the separator in
     * 'sep'.
     *
     * @param s
     *            the string to tokenize
     * @param sep
     *            a list of separator to use
     *
     * @return the array of tokens (an array of size 1 with the original string
     *         if no separator found)
     */
    public static String[] split(final String s, final String sep) {
        // convert a String s to an Array, the elements
        // are delimited by sep
        final Vector<Integer> tokenIndex = new Vector<Integer>(10);
        final int len = s.length();
        int i;

        // Find all characters in string matching one of the separators in 'sep'
        for (i = 0; i < len; i++)
            if (sep.indexOf(s.charAt(i)) != -1)
                tokenIndex.addElement(new Integer(i));

        final int size = tokenIndex.size();
        final String[] elements = new String[size + 1];

        // No separators: return the string as the first element
        if (size == 0)
            elements[0] = s;
        else {
            // Init indexes
            int start = 0;
            int end = (tokenIndex.elementAt(0)).intValue();
            // Get the first token
            elements[0] = s.substring(start, end);

            // Get the mid tokens
            for (i = 1; i < size; i++) {
                // update indexes
                start = (tokenIndex.elementAt(i - 1)).intValue() + 1;
                end = (tokenIndex.elementAt(i)).intValue();
                elements[i] = s.substring(start, end);
            }
            // Get last token
            start = (tokenIndex.elementAt(i - 1)).intValue() + 1;
            elements[i] = (start < s.length()) ? s.substring(start) : "";
        }

        return elements;
    }

}

