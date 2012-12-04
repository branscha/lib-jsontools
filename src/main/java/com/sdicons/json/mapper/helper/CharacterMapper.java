/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.mapper.helper;

import com.sdicons.json.mapper.JSONMapper;
import com.sdicons.json.mapper.MapperException;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.model.JSONValue;

public class CharacterMapper
extends AbstractMapper
{
    private static final String CHM002 = "JSONMapper/CharacterMapper/001: JSON->Java. Cannot map value '%s' to a Character.";
    private static final String CHM001 = "JSONMapper/CharacterMapper/002: JSON->Java. Cannot map JSON class: '%s' to Java Character.";
    private static final String CHM003 = "JSONMapper/CharacterMapper/003: JSON->Java. Cannot map to Java class '%s'.";

    public Class<?> getHelpedClass()
    {
        return Character.class;
    }

    public Object toJava(JSONMapper mapper, JSONValue aValue, Class<?> aRequestedClass) throws MapperException
    {
        if(!aRequestedClass.isAssignableFrom(Character.class))
            throw new MapperException(String.format(CHM003, aRequestedClass.getName()));
        
        if (!aValue.isString()) throw new MapperException(String.format(CHM001, aValue.getClass().getName()));
        final String lRepr = ((JSONString) aValue).getValue();

        if(lRepr.length() != 1) throw new MapperException(String.format(CHM002, lRepr));
        return lRepr.charAt(0);
    }
}
