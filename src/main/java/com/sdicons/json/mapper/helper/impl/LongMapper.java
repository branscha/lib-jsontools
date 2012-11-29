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

public class LongMapper
implements MapperHelper
{
    private static final String FP001 = "JSONMapper/LongMapper/001: JSON->Java. Cannot map value '%s'to a Long.";
    private static final String FP002 = "JSONMapper/LongMapper/002: JSON->Java. Cannot map JSON class '%s' to Java Long.";
    private static final String FP003 = "JSONMapper/LongMapper/003: Java->JSON. Cannot map Java class '%s' to JSONInteger.";
    private static final String FP004 = "JSONMapper/LongMapper/004: JSON->Java. Cannot map to Java class '%s'.";

    public Class<?> getHelpedClass()
    {
        return Long.class;
    }

    public Object toJava(JSONMapper mapper, JSONValue aValue, Class<?> aRequestedClass) throws MapperException
    {
        if(!aRequestedClass.isAssignableFrom(Long.class))
            throw new MapperException(String.format(FP004, aRequestedClass.getName()));

        if(aValue.isString())
        {
            try
            {
                return Long.parseLong(((JSONString)aValue).getValue());
            }
            catch (NumberFormatException e)
            {
                throw new MapperException(String.format(FP001, ((JSONString)aValue).getValue()), e);
            }
        }
        else if(aValue.isInteger()) return ((JSONInteger) aValue).getValue().longValue();
        else throw new MapperException(String.format(FP002, aValue.getClass().getName()));
    }

    public JSONValue toJSON(JSONMapper mapper, Object aPojo)
    throws MapperException
    {
        if(!Long.class.isAssignableFrom(aPojo.getClass())) throw new MapperException(String.format(FP003,aPojo.getClass().getName()));
        return new JSONInteger(new BigInteger(aPojo.toString()));
    }
}
