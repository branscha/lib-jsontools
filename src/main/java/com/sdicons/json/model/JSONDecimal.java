/*******************************************************************************
 * Copyright (c) 2006-2013 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.model;

import java.math.BigDecimal;

/**
 * Represents a JSON float. JSON only defines "numbers" but during parsing a difference is
 * made between integral types and floating types.
 */
public class JSONDecimal
extends JSONNumber
{
    private BigDecimal value;

    public JSONDecimal(BigDecimal value)
    {
        if(value == null) throw new IllegalArgumentException();
        this.value = value;
    }

    public BigDecimal getValue()
    {
        return value;
    }

    public String toString()
    {
        return "JSONDecimal(" + getLine() + ":" + getCol() + ")[" + value.toString() + "]";
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

        final JSONDecimal that = (JSONDecimal) o;
        return value.equals(that.value);
    }

    public int hashCode()
    {
        return value.hashCode();
    }

    /**
     * Remove all JSON information. In the case of a JSONDecimal this is a BigDecimal.
     * @return A BigDecimal representing the JSONDecimal.
     */
    public Object strip()
    {
        return value;
    }
}
