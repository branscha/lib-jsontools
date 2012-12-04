/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import com.sdicons.json.parser.util.ParserUtil;

/**
 * Representation of a JSON object, a collection (unordered) of name/value pairs.
 * An example: <code>{"name":"Homer", "age":40, "children":["Bart", "Maggie", "Lisa"]}</code>
 */
public class JSONObject
extends JSONComplex
{
    // It is very important that the implementation is based on a linked hash map.
    // The order of JSON object elements should remain in the same order as they are encountered.
    // Especially the serializer relies on it.
    private HashMap<String, JSONValue> map = new LinkedHashMap<String, JSONValue>();

    public int size()
    {
        return map.size();
    }

    public HashMap<String, JSONValue> getValue()
    {
        return map;
    }

    public String toString()
    {
        final StringBuilder lBuf = new StringBuilder();
        lBuf.append("JSONObject(").append(getLine()).append(":").append(getCol()).append(")[");
        Iterator<String> lIter = map.keySet().iterator();
        while(lIter.hasNext())
        {
            final String lKey=lIter.next();
            lBuf.append(lKey).append(":").append(map.get(lKey).toString());
            if(lIter.hasNext()) lBuf.append(", ");
        }
        lBuf.append("]");
        return lBuf.toString();
    }

    protected String render(boolean aPretty, String aIndent)
    {
        final StringBuilder lBuf = new StringBuilder();
        final Iterator<String> lKeyIter = map.keySet().iterator();
        if(aPretty)
        {
            lBuf.append(aIndent).append("{\n");
            final String lIndent = aIndent + "   ";
            final String lIndent2 = aIndent + "      ";
            while(lKeyIter.hasNext())
            {
                final String lKey = lKeyIter.next();
                final JSONValue jsonValue = map.get(lKey);

                lBuf.append(lIndent).append(ParserUtil.render(lKey, false, ""));
                if(jsonValue.isSimple())
                {
                    lBuf.append(" : ").append(jsonValue.render(false, ""));

                }
                else
                {
                    lBuf.append(" :\n").append(jsonValue.render(true, lIndent2));
                }
                if(lKeyIter.hasNext()) lBuf.append(",\n");

                else lBuf.append("\n");
            }
            lBuf.append(aIndent).append("}");
        }
        else
        {
            lBuf.append("{");
            while(lKeyIter.hasNext())
            {
                final String lKey = lKeyIter.next();
                final JSONValue jsonValue = map.get(lKey);

                lBuf.append(ParserUtil.render(lKey, false, "")).append(":").append(jsonValue.render(false));
                if(lKeyIter.hasNext()) lBuf.append(",");
            }
            lBuf.append(aIndent).append("}");
        }
        return lBuf.toString();
    }

    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final JSONObject that = (JSONObject) o;
        return map.equals(that.map);
    }

    public int hashCode()
    {
        return map.hashCode();
    }

    public boolean containsKey(String aKey)
    {
        return map.containsKey(aKey);
    }

    /**
     * Utility method, get the element with specified name without having to
     * retrieve the map first using getValue().
     * @param aKey The key for which you want to retrieve the element.
     * @return The element corresponding to the key or null if the object
     * does not contain a key with this name.
     */
    public JSONValue get(String aKey)
    {
        return map.get(aKey);
    }

    /**
     * Remove all JSON related information. In the case of a JSONObject, a HashMap is returned.
     * The values of the HashMap are stripped as well.
     * @return a HashMap, containing pure Java objects.
     */
    public Object strip()
    {
        HashMap<Object, Object> lResult = new HashMap<Object, Object>();
        for(String lKey : map.keySet())
        {
            lResult.put(lKey, map.get(lKey).strip());
        }
        return lResult;
    }
}
