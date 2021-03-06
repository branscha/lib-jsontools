/*******************************************************************************
 * Copyright (c) 2006-2013 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents a JSON array (list), an ordered list of values ...
 * Example: <code>[ "one", "two", "tree" ]</code> is an array of 3 strings.
 */
public class JSONArray
extends JSONComplex
{
    private List<JSONValue> array = new ArrayList<JSONValue>();

    public JSONArray(){
    }


    public JSONArray(Collection<JSONValue> contents) {
        array.addAll(contents);
    }
    /**
     * @return The length of the array (list).
     */
    public int size()
    {
        return array.size();
    }

    /**
     * @return The JSON elements in the array (list).
     */
    public List<JSONValue> getValue()
    {
        return array;
    }

    public String toString()
    {
        final StringBuilder lBuf = new StringBuilder();
        lBuf.append("JSONArray(").append(getLine()).append(":").append(getCol()).append(")[");
        for (int i = 0; i < array.size(); i++)
        {
            JSONValue jsonValue = array.get(i);
            lBuf.append(jsonValue.toString());
            if(i < array.size() - 1) lBuf.append(", ");
        }
        lBuf.append("]");
        return lBuf.toString();
    }

    /**
     * Utility method, get an element at a specific position in the list.
     * You do not have to get the list value first with getValue() first.
     * @param i Index of the element to return.
     * @return Returns the element at the specified position in this list.
     * @throws IndexOutOfBoundsException When there is no element at the specified position.
     */
    public JSONValue get(int i)
    {
        return array.get(i);
    }

    protected String render(boolean aPretty, String aIndent)
    {
        final StringBuilder lBuf = new StringBuilder();
        if(aPretty)
        {
            lBuf.append(aIndent).append("[\n");
            String lIndent = aIndent + "   ";
            for (int i = 0; i < array.size(); i++)
            {
                JSONValue jsonValue = array.get(i);
                lBuf.append(jsonValue.render(true, lIndent));
                if(i < array.size() - 1) lBuf.append(",\n");
                else lBuf.append("\n");
            }
            lBuf.append(aIndent).append("]");
        }
        else
        {
            lBuf.append("[");
            for (int i = 0; i < array.size(); i++)
            {
                JSONValue jsonValue = array.get(i);
                lBuf.append(jsonValue.render(false, ""));
                if(i < array.size() - 1) lBuf.append(",");
            }
            lBuf.append("]");
        }
        return lBuf.toString();
    }

    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final JSONArray jsonArray = (JSONArray) o;

        return array.equals(jsonArray.array);

    }

    public int hashCode()
    {
        return array.hashCode();
    }

    /**
     * Remove all JSON information. In the case of a JSONString, a Java String is returned.
     * The elements of the array are stripped as well.
     * @return A Java String representing the contents of the JSONString.
     */
    public Object strip()
    {
        List<Object> lResult = new LinkedList<Object>();
        for(JSONValue lVal: array)
        {
            lResult.add(lVal.strip());
        }
        return lResult;
    }
}
