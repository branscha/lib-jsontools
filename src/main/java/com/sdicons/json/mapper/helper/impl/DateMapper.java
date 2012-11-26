/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.mapper.helper.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.sdicons.json.mapper.JSONMapper;
import com.sdicons.json.mapper.MapperException;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.model.JSONValue;

public class DateMapper
extends AbstractMapper
{
    private static final String DM001 = "JSONMapper/DateMapper/001: JSON->Java. Cannot map value '%s' to a Date using pattern '%s'.";
    private static final String DM002 = "JSONMapper/DateMapper/002: JSON->Java. Cannot map JSON class '%s' to Java Date.";
    private static final String DM003 = "JSONMapper/DateMapper/003: Java->JSON. Cannot map Java class '%s' to JSONString.";

    private SimpleDateFormat df = new SimpleDateFormat(JSONMapper.DATEFORMAT_DEFAULT);

    public Object toJava(JSONMapper mapper, JSONValue aValue, Class<?> aRequestedClass) throws MapperException {
        if (!aValue.isString()) throw new MapperException(String.format(DM002, aValue.getClass().getName()));
        initFormat(mapper);
        try {
            return df.parse(((JSONString)aValue).getValue());
        }
        catch (ParseException e) {
            throw new MapperException(String.format(DM001, ((JSONString)aValue).getValue(), df.toPattern()), e);
        }
    }

    public JSONValue toJSON(JSONMapper mapper, Object aPojo)
    throws MapperException {
        if(!Date.class.isAssignableFrom(aPojo.getClass())) throw new MapperException(String.format(DM003, aPojo.getClass().getName()));
        initFormat(mapper);
        return new JSONString(df.format(aPojo));
    }

    private void initFormat(JSONMapper mapper) {
        if(mapper.hasMappingOption(JSONMapper.OPT_DATEFORMAT)) {
            String format = (String) mapper.getMappingOption(JSONMapper.OPT_DATEFORMAT, JSONMapper.DATEFORMAT_DEFAULT);
            if(!df.toPattern().equals(format)) {
                df = new SimpleDateFormat(format);
            }
        }
    }

    public Class<?> getHelpedClass()
    {
        return Date.class;
    }
}
