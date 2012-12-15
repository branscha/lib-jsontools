/*******************************************************************************
 * Copyright (c) 2006-2013 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.mapper.helper;

import com.sdicons.json.mapper.JSONMapper;
import com.sdicons.json.mapper.MapperException;
import com.sdicons.json.model.JSONValue;
import com.sdicons.json.repository.ClassHelper;

/**
 * It is a ClassHelper which knows how to map the instances of a range of classes from JSON to Java and vice versa.
 * The ClassMapper instances will be kept in a repository inside the JSONMapper, the mapper will locate the most specific ClassMapper and use this.
 * If you want to create your own mapper for your class, you should implement this interface.
 *
 */
public interface ClassMapper
extends ClassHelper
{
    /**
     * Map an instance from the JSON model to Java.
     * @param mapper The JSONMapper is passed as an argument for recursive mappings or to access the mapping options.
     * @param json The JSON object that we want to convert.
     * @param requestedClass The target Java class in which we want to convert the JSON object.
     * @return The Java instance.
     * @throws MapperException If the mapping cannot be done.
     */
    Object toJava(JSONMapper mapper, JSONValue json, Class<?> requestedClass)
    throws MapperException;

    /**
     * Map a Java instance to an instance of the JSON model.
     * @param mapper The JSONMapper is passed as an argument for recursive mappings or to access the mapping options.
     * @param pojo The Java instance that we want to convert the JSON model.
     * @return The JSON instance in which we want to map the Java pojo.
     * @throws MapperException If the mapping cannot be done.
     */
    JSONValue toJSON(JSONMapper mapper, Object pojo)
    throws MapperException;
}
