/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.mapper.helper;

import com.sdicons.json.helper.ClassHelper;
import com.sdicons.json.mapper.JSONMapper;
import com.sdicons.json.mapper.MapperException;
import com.sdicons.json.model.JSONValue;

public interface SimpleMapperHelper
extends ClassHelper
{
    Object toJava(JSONMapper mapper, JSONValue aValue, Class<?> aRequestedClass)
    throws MapperException;

    JSONValue toJSON(JSONMapper mapper, Object aPojo)
    throws MapperException;
}
