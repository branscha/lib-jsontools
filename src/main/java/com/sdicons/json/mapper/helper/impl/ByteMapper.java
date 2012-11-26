/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.mapper.helper.impl;

import java.math.BigInteger;

import com.sdicons.json.mapper.JSONMapper;
import com.sdicons.json.mapper.MapperException;
import com.sdicons.json.mapper.helper.SimpleMapperHelper;
import com.sdicons.json.model.JSONInteger;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.model.JSONValue;

public class ByteMapper
implements SimpleMapperHelper
{
    private static final String BYM001 = "JSONMapper/ByteMapper/001: Cannot map value '%s'.";
    private static final String BYM002 = "JSONMapper/ByteMapper/002: Cannot map class '%s'.";

    public Class<?> getHelpedClass()
    {
        return Byte.class;
    }

    public Object toJava(JSONMapper mapper, JSONValue aValue, Class<?> aRequestedClass) throws MapperException
    {
        if(aValue.isString())
        {
            try
            {
                return Byte.parseByte(((JSONString)aValue).getValue());
            }
            catch (NumberFormatException e)
            {
                throw new MapperException(String.format(BYM001, ((JSONString)aValue).getValue()), e);
            }
        }
        else if(aValue.isInteger()) return (byte) ((JSONInteger) aValue).getValue().intValue();
        else throw new MapperException(String.format(BYM002, aValue.getClass().getName()));
    }

    public JSONValue toJSON(JSONMapper mapper, Object aPojo)
    throws MapperException
    {
        if(!Byte.class.isAssignableFrom(aPojo.getClass())) throw new MapperException(String.format(BYM002, aPojo.getClass().getName()));
        return new JSONInteger(new BigInteger(aPojo.toString()));
    }
}
