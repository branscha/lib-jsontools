/*******************************************************************************
 * Copyright (c) 2006-2013 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.mapper.helper;

import com.sdicons.json.mapper.JSONMapper;
import com.sdicons.json.mapper.MapperException;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.model.JSONValue;

public class EnumMapper
extends AbstractMapper {

    private static final String EM001 = "JSONMapper/EnumMapper/001: JSON->Java. Cannot map JSON class '%s' to Java Enum.";
    private static final String EM002 = "JSONMapper/EnumMapper/002: JSON->Java. Failed to handle a non-enum class '%s'.";
    private static final String EN003 = "JSONMapper/EnumMapper/003: JSON->Java. The enum class '%s' was found but no matching enum value could be found for '%s'.";

    public Class<?> getHelpedClass() {
        return Enum.class;
    }

    public Object toJava(JSONMapper mapper, JSONValue aValue, Class<?> aRequestedClass) throws MapperException {

        if (!aValue.isString())
            throw new MapperException(String.format(EM001, aValue.getClass().getName()));

        if (aRequestedClass.isEnum()) {
            Object[] lEnumVals = aRequestedClass.getEnumConstants();
            for (Object lEnumVal : lEnumVals) {
                if (lEnumVal.toString().equals(((JSONString) aValue).getValue())) return lEnumVal;
            }
        }
        else {
            throw new MapperException(String.format(EM002, aRequestedClass.getName()));
        }

        throw new MapperException(String.format(EN003, aRequestedClass.getName(), ((JSONString) aValue).getValue()));
    }
}
