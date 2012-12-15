/*******************************************************************************
 * Copyright (c) 2006-2013 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.model;

import com.sdicons.json.parser.ParserUtil;

/**
 * Represents a JSON delimited string.
 * Examples are: <code>"Hello" and "World"</code>; <code>"Hello\nWorld"</code> contains a newline.
 * Strings are always delimited with double quotes.
 */
public class JSONString
extends JSONSimple
{
    private String value;

    public JSONString(String value)
    {
        if(value == null) throw new IllegalArgumentException();
        this.value = value;
    }

    public String getValue()
    {
        return value;
    }

    public String toString()
    {
        return "JSONString(" + getLine() + ":" + getCol() + ")[" + ParserUtil.render(value, false, "") + "]";
    }

    protected String render(boolean pretty, String indent)
    {
        return ParserUtil.render(value, pretty, indent);
    }

    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final JSONString that = (JSONString) o;
        return value.equals(that.value);
    }

    public int hashCode()
    {
        return value.hashCode();
    }

    /**
     * A pure Java object, all JSON information is removed. A JSONString
     * trivially maps to a Java String.
     * @return A Java String representing the contents of a JSONString.
     */
    public Object strip()
    {
        return value;
    }
}
