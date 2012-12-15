/*******************************************************************************
 * Copyright (c) 2006-2013 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.mapper.helper;

import java.math.BigDecimal;

import com.sdicons.json.mapper.JSONMapper;
import com.sdicons.json.mapper.MapperException;
import com.sdicons.json.model.JSONDecimal;
import com.sdicons.json.model.JSONInteger;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.model.JSONValue;

public class FloatMapper
implements ClassMapper
{
    private static final String FM001 = "JSONMapper/FloatMapper/001: JSON->Java. Cannot map value '%s' to a Float.";
    private static final String FM002 = "JSONMapper/FloatMapper/002: JSON->Java. Cannot map JSON class '%s' to Java Float.";
    private static final String FM003 = "JSONMapper/FloatMapper/003: Java->JSON. Cannot map Java class '%s' to JSONDecimal.";
    private static final String FM004 = "JSONMapper/FloatMapper/004: JSON->Java. Cannot map to Java class '%s'.";

    public Class<?> getHelpedClass()
    {
        return Float.class;
    }

    public Object toJava(JSONMapper mapper, JSONValue aValue, Class<?> aRequestedClass) throws MapperException
    {
        if(!aRequestedClass.isAssignableFrom(Float.class))
            throw new MapperException(String.format(FM004, aRequestedClass.getName()));

        if(aValue.isString())
        {
            try
            {
                return Float.parseFloat(((JSONString)aValue).getValue());
            }
            catch (NumberFormatException e)
            {
                throw new MapperException(String.format(FM001, ((JSONString)aValue).getValue()), e);
            }
        }
        else if(aValue.isDecimal()) return ((JSONDecimal) aValue).getValue().floatValue();
        else if(aValue.isInteger()) return ((JSONInteger)aValue).getValue().floatValue();
        else throw new MapperException(String.format(FM002, aValue.getClass().getName()));
    }

    public JSONValue toJSON(JSONMapper mapper, Object aPojo)
    throws MapperException
    {
        if(!Float.class.isAssignableFrom(aPojo.getClass())) throw new MapperException(String.format(FM003, aPojo.getClass().getName()));
        return new JSONDecimal(new BigDecimal(aPojo.toString()));
    }
}
