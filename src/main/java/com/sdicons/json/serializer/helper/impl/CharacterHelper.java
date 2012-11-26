/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.serializer.helper.impl;

import java.util.HashMap;

import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.serializer.SerializerException;
import com.sdicons.json.serializer.JSONSerializer;

public class CharacterHelper
extends AbstractHelper
{
    // Error messages
    //
    private static final String CHAR001 = "JSONSerializer/CharacterHelper/001: JSON->Java. Length of the character value should be > 0";

    public Object parseValue(JSONObject aObjectElement, JSONSerializer aMarshall, HashMap<Object, Object> aPool)
    throws SerializerException
    {
        JSONSerializer.requireStringAttribute(aObjectElement, JSONSerializer.RNDR_ATTR_VALUE);
        final String lValue = ((JSONString) aObjectElement.get(JSONSerializer.RNDR_ATTR_VALUE)).getValue();
        if(lValue.length() < 1) throw new SerializerException(CHAR001);
        return lValue.charAt(0);
    }

    public Class<?> getHelpedClass()
    {
        return Character.class;
    }
}
