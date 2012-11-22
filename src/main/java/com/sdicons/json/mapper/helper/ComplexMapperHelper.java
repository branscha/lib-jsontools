/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.mapper.helper;

import com.sdicons.json.mapper.MapperException;
import com.sdicons.json.model.JSONValue;

import java.lang.reflect.Type;

public interface ComplexMapperHelper
extends SimpleMapperHelper    
{
    Object toJava(JSONValue aValue, Class aRequestedClass, Type[] aTypes)
    throws MapperException;

    JSONValue toJSON(Object aPojo)
    throws MapperException;
}
