/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.parser;

/**
 * Utility functions used by the {@link JSONParser}.
 */
public class ParserUtil
{
    private ParserUtil() {
        // Prevent instantiating a utility class.
    }
    
    /**
     * Utility function to render a Java String to its text representation so that
     * it can be parsed again. The special characters have to be converted to their escaped counterparts,
     * this function does the escaping as well.
     * 
     * @param value 
     *        The String that has to be rendered to text.
     * @param pretty
     *        A boolean flag, indicating if we have to insert indentation or not.
     * @param indent
     *        The indentation, only in effect if the pretty flag is set.
     * @return
     */
    public static String render(String value, boolean pretty, String indent)
    {
        final StringBuilder lBuf = new StringBuilder();
        if(pretty) lBuf.append(indent);
        // Opening quotes for the String.
        lBuf.append("\"");
        // Escape special characters.
        for(int i = 0; i < value.length(); i++)
        {
            final char lChar = value.charAt(i);
            if(lChar == '\n') lBuf.append("\\n");
            else if(lChar == '\r') lBuf.append("\\r");
            else if(lChar == '\t') lBuf.append("\\t");
            else if(lChar == '\b') lBuf.append("\\b");
            else if(lChar == '\f') lBuf.append("\\f");
            else if(lChar == '\"') lBuf.append("\\\"");
            else if(lChar == '\\') lBuf.append("\\\\");
            else lBuf.append(lChar);
        }
        // Closing quotes.
        lBuf.append("\"");
        return lBuf.toString();
    }
}
