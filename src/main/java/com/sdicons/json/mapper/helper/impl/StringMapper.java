/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.mapper.helper.impl;

import com.sdicons.json.mapper.JSONMapper;
import com.sdicons.json.mapper.MapperException;
import com.sdicons.json.model.JSONDecimal;
import com.sdicons.json.model.JSONInteger;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.model.JSONValue;

public class StringMapper
extends AbstractMapper
{
    public Class<?> getHelpedClass()
    {
        return String.class;
    }

    public Object toJava(JSONMapper mapper, JSONValue aValue, Class aRequestedClass) throws MapperException
    {
    	//lenient to the data to be converted.
    	if(aValue.isDecimal()) return ((JSONDecimal)aValue).getValue().toString();
    	if(aValue.isInteger()) return ((JSONInteger)aValue).getValue().toString();
        if (!aValue.isString()) throw new MapperException("StringMapper cannot map class: " + aValue.getClass().getName());
        return ((JSONString) aValue).getValue();
    }
}
