/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.model;

import java.math.BigInteger;

/**
 * Represents a JSON int. JSON only defines "numbers" but during parsing a difference is
 * made between integral types and floating types.
 */
public class JSONInteger
extends JSONNumber
{
    private BigInteger value;

    public JSONInteger(BigInteger value)
    {
        if(value == null) throw new IllegalArgumentException();
        this.value = value;
    }

    public BigInteger getValue()
    {
        return value;
    }

    public String toString()
    {
        return "JSONInteger(" + getLine() + ":" + getCol() + ")[" + value.toString() + "]";
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

        final JSONInteger that = (JSONInteger) o;
        return value.equals(that.value);
    }

    public int hashCode()
    {
        return value.hashCode();
    }

    /**
     * Remove all JSON information, in the case of a JSONInteger this means a BigInteger.
     * @return A BigInteger representing the value of the JSONInteger object.
     */
    public Object strip()
    {
        return value;
    }
}
