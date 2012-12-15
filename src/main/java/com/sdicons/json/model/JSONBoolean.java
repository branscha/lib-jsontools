/*******************************************************************************
 * Copyright (c) 2006-2013 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.model;

/**
 * Represents a JSON boolean value.
 * Examples are: <code>true, false.</code>
 */
public class JSONBoolean
extends JSONSimple
{
    public static final JSONBoolean TRUE = new JSONBoolean(true);
    public static final JSONBoolean FALSE = new JSONBoolean(false);

    private boolean value;

    public JSONBoolean(boolean value)
    {
        this.value = value;
    }

    public boolean getValue()
    {
        return value;
    }

    public String toString()
    {
        return "JSONBoolean(" + getLine() + ":" + getCol() + ")[" + value +"]";
    }

    protected String render(boolean pretty, String indent)
    {
        if(pretty) return indent + value;
        else return "" + value;
    }

    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final JSONBoolean that = (JSONBoolean) o;

        return value == that.value;
    }

    public int hashCode()
    {
        return (value ? 1 : 0);
    }

    /**
     * Get the Java object, remove all JSON information. In the case of a JSONBoolean, this
     * is a Java Boolean object.
     * @return Boolean.TRUE or Boolean.FALSE depending on the value of the JSONBoolean.
     */
    public Object strip()
    {
        return value?Boolean.TRUE:Boolean.FALSE;
    }
}
