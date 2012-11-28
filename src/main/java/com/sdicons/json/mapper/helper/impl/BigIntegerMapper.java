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

public class BigIntegerMapper
implements MapperHelper
{
    private static final String BIM001 = "JSONMapper/BigIntegerMapper/001: JSON->Java. Cannot map value '%s' to a BigInteger.";
    private static final String BIM002 = "JSONMapper/BigIntegerMapper/002: JSON->Java. Cannot map JSON class '%s' to Java BigInteger.";
    private static final String BIM003 = "JSONMapper/BigIntegerMapper/003: Java->JSON. Cannot map Java class '%s' to JSONInteger.";

    public Class<?> getHelpedClass()
    {
        return BigInteger.class;
    }

    public Object toJava(JSONMapper mapper, JSONValue aValue, Class<?> aRequestedClass) throws MapperException
    {
        if(aValue.isString())
        {
            try
            {
                return new BigInteger(((JSONString)aValue).getValue());
            }
            catch (NumberFormatException e)
            {
                throw new MapperException(String.format(BIM001, ((JSONString)aValue).getValue()), e);
            }
        }
        else if(aValue.isInteger()) return ((JSONInteger) aValue).getValue();
        else throw new MapperException(String.format(BIM002, aValue.getClass().getName()));
    }

    public JSONValue toJSON(JSONMapper mapper, Object aPojo)
    throws MapperException
    {
        if(!BigInteger.class.isAssignableFrom(aPojo.getClass())) throw new MapperException(String.format(BIM003, aPojo.getClass().getName()));
        return new JSONInteger(new BigInteger(aPojo.toString()));
    }
}
