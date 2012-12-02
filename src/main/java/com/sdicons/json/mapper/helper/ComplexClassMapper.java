/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.mapper.helper;

import java.lang.reflect.Type;

import com.sdicons.json.mapper.JSONMapper;
import com.sdicons.json.mapper.MapperException;
import com.sdicons.json.model.JSONValue;

/**
 * The ComplexClassMapper can make use of extra type information from generic declarations.
 *
 */
public interface ComplexClassMapper
extends ClassMapper
{
    Object toJava(JSONMapper mapper, JSONValue json, Class<?> requestedClass, Type[] types)
    throws MapperException;

    JSONValue toJSON(JSONMapper mapper, Object pojo)
    throws MapperException;
}
