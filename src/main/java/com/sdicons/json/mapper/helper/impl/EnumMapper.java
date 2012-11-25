/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.mapper.helper.impl;

import com.sdicons.json.mapper.JSONMapper;
import com.sdicons.json.mapper.MapperException;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.model.JSONValue;

public class EnumMapper
extends AbstractMapper
{
    public Class getHelpedClass()
    {
        return Enum.class;
    }

    public Object toJava(JSONMapper mapper, JSONValue aValue, Class aRequestedClass)
    throws MapperException
    {

        if (!aValue.isString()) throw new MapperException("EnumMapper cannot map class: " + aValue.getClass().getName());

        if(aRequestedClass.isEnum())
        {
            Object[] lEnumVals = aRequestedClass.getEnumConstants();
            for(Object lEnumVal: lEnumVals)
            {
                if(lEnumVal.toString().equals(((JSONString)aValue).getValue())) return lEnumVal;
            }
        }
        else
        {
            final String lMsg = "Enum mapper tried to handle a non-enum class: " + aRequestedClass;
            throw new MapperException(lMsg);
        }

        final String lMsg = "The enum class *is found* but no matching value could be found. Enum Class: " + aRequestedClass + ", unknown value: " + ((JSONString)aValue).getValue();
        throw new MapperException(lMsg);
    }
}
