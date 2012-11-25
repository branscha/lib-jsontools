/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.mapper.helper.impl;

import com.sdicons.json.mapper.JSONMapper;
import com.sdicons.json.mapper.MapperException;
import com.sdicons.json.mapper.helper.SimpleMapperHelper;
import com.sdicons.json.model.JSONInteger;
import com.sdicons.json.model.JSONValue;
import com.sdicons.json.model.JSONString;

import java.math.BigInteger;

public class IntegerMapper
implements SimpleMapperHelper
{
    public Class getHelpedClass()
    {
        return Integer.class;
    }

    public Object toJava(JSONMapper mapper, JSONValue aValue, Class aRequestedClass) throws MapperException
    {
        if(aValue.isString())
        {
            try
            {
                return Integer.parseInt(((JSONString)aValue).getValue());
            }
            catch (NumberFormatException e)
            {
                throw new MapperException("IntegerMapper cannot map value: " + ((JSONString)aValue).getValue());
            }
        }
        else if(aValue.isInteger()) return ((JSONInteger) aValue).getValue().intValue();
        else throw new MapperException("IntegerMapper cannot map: " + aValue.getClass().getName());
    }

    public JSONValue toJSON(JSONMapper mapper, Object aPojo)
    throws MapperException
    {
        if(!Integer.class.isAssignableFrom(aPojo.getClass())) throw new MapperException("IntegerMapper cannot map: " + aPojo.getClass().getName());
        return new JSONInteger(new BigInteger(aPojo.toString()));
    }
}
