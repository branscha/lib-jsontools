/*******************************************************************************
 * Copyright (c) 2006-2013 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.serializer.helper;

import java.util.HashMap;

import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.serializer.JSONSerializer;
import com.sdicons.json.serializer.SerializerException;

public class EnumSerializer
extends AbstractSerializer
{
    // Error messages.
    //
    private static final String ENUM001 = "JSONSerializer/EnumSerializer/001: JSON->Java. The class '%s' cannot be instantiated, it cannot be found in the classpath.";
    private static final String ENUM002 = "JSONSerializer/EnumSerializer/002: JSON->Java. Failed to handle a non-enum class '%s'.";
    private static final String ENUM003 = "JSONSerializer/EnumSerializer/003: JSON->Java. The enum class '%s' is found but no matching value for '%s' could be found.";

    public Object toJava(JSONObject aObjectElement, JSONSerializer serializer, HashMap<Object, Object> aPool)
    throws SerializerException
    {
        JSONSerializer.requireStringAttribute(aObjectElement, JSONSerializer.RNDR_ATTR_CLASS);
        final String lEnumClassName = ((JSONString) aObjectElement.get(JSONSerializer.RNDR_ATTR_CLASS)).getValue();

        final Class<?> lEnumClass;
        try
        {
            lEnumClass = Class.forName(lEnumClassName);
        }
        catch (ClassNotFoundException e)
        {
            throw new SerializerException(String.format(ENUM001, lEnumClassName), e);
        }

        JSONSerializer.requireStringAttribute(aObjectElement, JSONSerializer.RNDR_ATTR_VALUE);
        final String lVal = ((JSONString) aObjectElement.get(JSONSerializer.RNDR_ATTR_VALUE)).getValue();

        if(lEnumClass.isEnum())
        {
            Object[] lEnumVals = lEnumClass.getEnumConstants();
            for(Object lEnumVal: lEnumVals)
            {
                if(lEnumVal.toString().equals(lVal)) return lEnumVal;
            }
        }
        else
        {
            throw new SerializerException(String.format(ENUM002, lEnumClassName));
        }

        throw new SerializerException(String.format(ENUM003, lEnumClassName, lVal));
    }

    public Class<?> getHelpedClass()
    {
        return Enum.class;
    }
}
