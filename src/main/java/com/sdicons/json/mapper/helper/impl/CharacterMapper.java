/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.mapper.helper.impl;

import com.sdicons.json.model.JSONValue;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.mapper.JSONMapper;
import com.sdicons.json.mapper.MapperException;

public class CharacterMapper
extends AbstractMapper
{
    public Class getHelpedClass()
    {
        return Character.class;
    }

    public Object toJava(JSONMapper mapper, JSONValue aValue, Class aRequestedClass) throws MapperException
    {
        if (!aValue.isString()) throw new MapperException("CharacterMapper cannot map class: " + aValue.getClass().getName());
        final String lRepr = ((JSONString) aValue).getValue();
        if(lRepr.length() != 1) throw new MapperException("CharacterMapper cannot map value: " + lRepr);
        return lRepr.charAt(0);
    }
}
