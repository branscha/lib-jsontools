/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.mapper.helper.impl;

import java.math.BigDecimal;

import com.sdicons.json.mapper.JSONMapper;
import com.sdicons.json.mapper.MapperException;
import com.sdicons.json.mapper.helper.MapperHelper;
import com.sdicons.json.model.JSONDecimal;
import com.sdicons.json.model.JSONInteger;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.model.JSONValue;

public class BigDecimalMapper
implements MapperHelper
{
    private static final String BDM001 = "JSONMapper/BigDecimalMapper/001: JSON->Java. Cannot map value '%s' to a BigDecimal.";
    private static final String BDM002 = "JSONMapper/BigDecimalMapper/002: JSON->Java. Cannot map JSON class '%s' to Java BigDecimal.";
    private static final String BDM003 = "JSONMapper/BigDecimalMapper/003: Java->JSON. Cannot map Java class '%s' to JSONDecimal.";

    public Class<?> getHelpedClass()
    {
        return BigDecimal.class;
    }

    public Object toJava(JSONMapper mapper, JSONValue aValue, Class<?> aRequestedClass) throws MapperException
    {
        if(aValue.isString())
        {
            try
            {
                return new BigDecimal(((JSONString)aValue).getValue());
            }
            catch (NumberFormatException e)
            {
                throw new MapperException(String.format(BDM001, ((JSONString)aValue).getValue()), e);
            }
        }
        else if(aValue.isDecimal()) return ((JSONDecimal) aValue).getValue();
        else if(aValue.isInteger()) return new BigDecimal(((JSONInteger)aValue).getValue());
        else throw new MapperException(String.format(BDM002, aValue.getClass().getName()));
    }

    public JSONValue toJSON(JSONMapper mapper, Object aPojo)
    throws MapperException
    {
        if(!BigDecimal.class.isAssignableFrom(aPojo.getClass())) throw new MapperException(String.format(BDM003, aPojo.getClass().getName()));
        return new JSONDecimal(new BigDecimal(aPojo.toString()));
    }
}
