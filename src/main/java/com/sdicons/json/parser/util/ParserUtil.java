/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.parser.util;

public class ParserUtil
{
    public static String hexToChar(String i, String j, String k, String l)
    {
         return Character.toString((char)Integer.parseInt("" + i + j + k + l, 16));
    }

    public static String render(String value, boolean pretty, String indent)
    {
        final StringBuilder lBuf = new StringBuilder();
        if(pretty) lBuf.append(indent);
        lBuf.append("\"");
        for(int i = 0; i < value.length(); i++)
        {
            final char lChar = value.charAt(i);
            if(lChar == '\n') lBuf.append("\\n");
            else if(lChar == '\r') lBuf.append("\\r");
            else if(lChar == '\t') lBuf.append("\\t");
            else if(lChar == '\b') lBuf.append("\\b");
            else if(lChar == '\f') lBuf.append("\\f");
//            else if(lChar == '/') lBuf.append("\\/");
            else if(lChar == '\"') lBuf.append("\\\"");
            else if(lChar == '\\') lBuf.append("\\\\");
            else lBuf.append(lChar);
        }

        lBuf.append("\"");
        return lBuf.toString();
    }
}
