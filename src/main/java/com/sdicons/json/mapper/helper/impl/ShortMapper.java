/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.mapper.helper.impl;

import java.math.BigInteger;

import com.sdicons.json.mapper.JSONMapper;
import com.sdicons.json.mapper.MapperException;
import com.sdicons.json.mapper.helper.MapperHelper;
import com.sdicons.json.model.JSONInteger;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.model.JSONValue;

public class ShortMapper
implements MapperHelper
{
    private static final String SHORT001 = "JSONMapper/ShortMapper/001: JSON->Java. Cannot map value '%s' to a Short.";
    private static final String SHORT002 = "JSONMapper/ShortMapper/002: JSON->Java. Cannot map JSON class '%s' to Short.";
    private static final String SHORT003 = "JSONMapper/ShortMapper/003: Java->JSON. Cannot map Java class '%s' to JSONInteger.";
    private static final String SHORT004 = "JSONMapper/ShortMapper/004: JSON->Java. Cannot map to Java class '%s'.";

    public Class<?> getHelpedClass()
    {
        return Short.class;
    }

    public Object toJava(JSONMapper mapper, JSONValue aValue, Class<?> aRequestedClass) throws MapperException
    {
        if(!aRequestedClass.isAssignableFrom(Short.class))
            throw new MapperException(String.format(SHORT004, aRequestedClass.getName()));

        if(aValue.isString())
        {
            try
            {
                return Short.parseShort(((JSONString)aValue).getValue());
            }
            catch (NumberFormatException e)
            {
                throw new MapperException(String.format(SHORT001, ((JSONString)aValue).getValue()), e);
            }
        }
        else if(aValue.isInteger()) return (short) ((JSONInteger) aValue).getValue().intValue();
        else throw new MapperException(String.format(SHORT002, aValue.getClass().getName()));
    }

    public JSONValue toJSON(JSONMapper mapper, Object aPojo)
    throws MapperException
    {
        if(!Short.class.isAssignableFrom(aPojo.getClass())) throw new MapperException(String.format(SHORT003, aPojo.getClass().getName()));
        return new JSONInteger(new BigInteger(aPojo.toString()));
    }
}
