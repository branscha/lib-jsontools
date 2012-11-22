/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.model;

/**
 * Represents a JSON null value.
 * The only valid example is: <code>null.</code>
 */
public class JSONNull
extends JSONSimple
{
    public static final JSONNull NULL = new JSONNull();

    public JSONNull()
    {
    }

    public String toString()
    {
        return "JSONNull(" + getLine() + ":" + getCol() + ")";
    }

    protected String render(boolean pretty, String indent)
    {
        if(pretty) return indent + "null";
        else return "null";
    }

    public boolean equals(Object obj)
    {
        return obj != null && obj instanceof JSONNull;
    }

    /**
     * Strip all JSON information. In the case of a JSONNull object, only null remains...
     * @return null.
     */
    public Object strip()
    {
        return null;
    }
}
