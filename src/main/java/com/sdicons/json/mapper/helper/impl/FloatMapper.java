/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.mapper.helper.impl;

import java.math.BigDecimal;

import com.sdicons.json.mapper.JSONMapper;
import com.sdicons.json.mapper.MapperException;
import com.sdicons.json.mapper.helper.SimpleMapperHelper;
import com.sdicons.json.model.JSONDecimal;
import com.sdicons.json.model.JSONInteger;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.model.JSONValue;

public class FloatMapper
implements SimpleMapperHelper
{
    public Class<?> getHelpedClass()
    {
        return Float.class;
    }

    public Object toJava(JSONMapper mapper, JSONValue aValue, Class<?> aRequestedClass) throws MapperException
    {
        if(aValue.isString())
        {
            try
            {
                return Float.parseFloat(((JSONString)aValue).getValue());
            }
            catch (NumberFormatException e)
            {
                throw new MapperException("FloatMapper cannot map value: " + ((JSONString)aValue).getValue());
            }
        }
        else if(aValue.isDecimal()) return ((JSONDecimal) aValue).getValue().floatValue();
        else if(aValue.isInteger()) return ((JSONInteger)aValue).getValue().floatValue();
        else throw new MapperException("FloatMapper cannot map: " + aValue.getClass().getName());
    }

    public JSONValue toJSON(JSONMapper mapper, Object aPojo)
    throws MapperException
    {
        if(!Float.class.isAssignableFrom(aPojo.getClass())) throw new MapperException("FloatMapper cannot map: " + aPojo.getClass().getName());
        return new JSONDecimal(new BigDecimal(aPojo.toString()));
    }
}
