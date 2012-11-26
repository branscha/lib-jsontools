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

public class ShortMapper
implements SimpleMapperHelper
{
    private static final String SHORT001 = "JSONMapper/ShortMapper/001: Cannot map value '%s'.";
    private static final String SHORT002 = "JSONMapper/ShortMapper/002: Cannot map class '%s'.";

    public Class<?> getHelpedClass()
    {
        return Short.class;
    }

    public Object toJava(JSONMapper mapper, JSONValue aValue, Class<?> aRequestedClass) throws MapperException
    {
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
        if(!Short.class.isAssignableFrom(aPojo.getClass())) throw new MapperException(String.format(SHORT002, aPojo.getClass().getName()));
        return new JSONInteger(new BigInteger(aPojo.toString()));
    }
}
