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

public class IntegerMapper
implements MapperHelper
{
    private static final String IM001 = "JSONMapper/IntegerMapper/001: JSON->Java. Cannot map value '%s'to an Integer.";
    private static final String IM002 = "JSONMapper/IntegerMapper/002: JSON->Java. Cannot map JSON class '%s' to Java Integer.";
    private static final String IM003 = "JSONMapper/IntegerMapper/003: Java->JSON. Cannot map Java class '%s' to JSONInteger.";

    public Class<?> getHelpedClass()
    {
        return Integer.class;
    }

    public Object toJava(JSONMapper mapper, JSONValue aValue, Class<?> aRequestedClass) throws MapperException
    {
        if(aValue.isString())
        {
            try
            {
                return Integer.parseInt(((JSONString)aValue).getValue());
            }
            catch (NumberFormatException e)
            {
                throw new MapperException(String.format(IM001, ((JSONString)aValue).getValue()), e);
            }
        }
        else if(aValue.isInteger()) return ((JSONInteger) aValue).getValue().intValue();
        else throw new MapperException(String.format(IM002, aValue.getClass().getName()));
    }

    public JSONValue toJSON(JSONMapper mapper, Object aPojo)
    throws MapperException
    {
        if(!Integer.class.isAssignableFrom(aPojo.getClass())) throw new MapperException(String.format(IM003, aPojo.getClass().getName()));
        return new JSONInteger(new BigInteger(aPojo.toString()));
    }
}
