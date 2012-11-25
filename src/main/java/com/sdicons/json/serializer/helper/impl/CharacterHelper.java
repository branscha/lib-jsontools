/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.serializer.helper.impl;

import java.util.HashMap;

import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.serializer.JSONSerializeException;
import com.sdicons.json.serializer.JSONSerializer;

public class CharacterHelper
extends AbstractHelper
{
    public Object parseValue(JSONObject aObjectElement, JSONSerializer aMarshall, HashMap<Object, Object> aPool)
    throws JSONSerializeException
    {
        JSONSerializer.requireStringAttribute(aObjectElement, JSONSerializer.RNDR_ATTR_VALUE);
        final String lValue = ((JSONString) aObjectElement.get(JSONSerializer.RNDR_ATTR_VALUE)).getValue();
        if(lValue.length() < 1) throw new JSONSerializeException("Length of value too short: " + lValue);
        return lValue.charAt(0);
    }

    public Class<?> getHelpedClass()
    {
        return Character.class;
    }
}
