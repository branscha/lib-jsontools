/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
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

public class DoubleMapper
implements ClassMapper
{
    private static final String DM001 = "JSONMapper/DoubleMapper/001: JSON->Java. Cannot map value '%s' to a Double.";
    private static final String DM002 = "JSONMapper/DoubleMapper/002: JSON->Java. Cannot map JSON class '%s' to Java Double.";
    private static final String DM003 = "JSONMapper/DoubleMapper/003: JSON->Java. Cannot map Java class '%s' to JSONDecimal.";
    private static final String DM004 = "JSONMapper/DoubleMapper/004: JSON->Java. Cannot map to Java class '%s'.";

    public Class<?> getHelpedClass()
    {
        return Double.class;
    }

    public Object toJava(JSONMapper mapper, JSONValue aValue, Class<?> aRequestedClass) throws MapperException
    {
        if(!aRequestedClass.isAssignableFrom(Double.class))
            throw new MapperException(String.format(DM004, aRequestedClass.getName()));
        
        if(aValue.isString())
        {
            try
            {
                return Double.parseDouble(((JSONString)aValue).getValue());
            }
            catch (NumberFormatException e)
            {
                throw new MapperException(String.format(DM001, ((JSONString)aValue).getValue()), e);
            }
        }
        else if(aValue.isDecimal()) return ((JSONDecimal) aValue).getValue().doubleValue();
        else if(aValue.isInteger()) return ((JSONInteger)aValue).getValue().doubleValue();
        else throw new MapperException(String.format(DM002, aValue.getClass().getName()));
    }

    public JSONValue toJSON(JSONMapper mapper, Object aPojo)
    throws MapperException
    {
        if(!Double.class.isAssignableFrom(aPojo.getClass())) throw new MapperException(String.format(DM003, aPojo.getClass().getName()));
        return new JSONDecimal(new BigDecimal(aPojo.toString()));
    }
}
