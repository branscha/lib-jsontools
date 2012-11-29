/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.mapper.helper.impl;

import com.sdicons.json.mapper.JSONMapper;
import com.sdicons.json.mapper.MapperException;
import com.sdicons.json.mapper.helper.MapperHelper;
import com.sdicons.json.model.JSONBoolean;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.model.JSONValue;

public class BooleanMapper implements MapperHelper {
    private static final String BOOL001 = "JSONMapper/BooleanMapper/001: JSON->Java. Cannot map JSON class '%s' to Java Boolean.";
    private static final String BOOL002 = "JSONMapper/BooleanMapper/002: Java->JSON. Cannot map Java class '%s' to JSONBoolean.";
    private static final String BOOL003 = "JSONMapper/BooleanMapper/003: JSON->Java. Cannot convert to Java class '%s'.";
    
    public Class<?> getHelpedClass() {
        return Boolean.class;
    }
    
    public Object toJava(JSONMapper mapper, JSONValue aValue, Class<?> aRequestedClass) throws MapperException {
        if (!aRequestedClass.isAssignableFrom(Boolean.class)) throw new MapperException(String.format(BOOL003, aRequestedClass.getName()));
        
        if (aValue.isString()) {
            return Boolean.parseBoolean(((JSONString) aValue).getValue());
        }
        else if (aValue.isBoolean()) {
            return ((JSONBoolean) aValue).getValue();
        }
        else
            throw new MapperException(String.format(BOOL001, aValue.getClass().getName()));
    }
    
    public JSONValue toJSON(JSONMapper mapper, Object aPojo) throws MapperException {
        if (!Boolean.class.isAssignableFrom(aPojo.getClass())) throw new MapperException(String.format(BOOL002, aPojo.getClass().getName()));
        return new JSONBoolean((Boolean) aPojo);
    }
}
