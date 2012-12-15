/*******************************************************************************
 * Copyright (c) 2006-2013 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.serializer.helper;

import java.util.HashMap;

import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.serializer.SerializerException;
import com.sdicons.json.serializer.JSONSerializer;

public class FloatSerializer
extends AbstractSerializer
{
    public Object toJava(JSONObject aObjectValue, JSONSerializer serializer, HashMap<Object, Object> aPool)
    throws SerializerException
    {
        JSONSerializer.requireStringAttribute(aObjectValue, JSONSerializer.RNDR_ATTR_VALUE);
        return new Float(((JSONString) aObjectValue.get(JSONSerializer.RNDR_ATTR_VALUE)).getValue());
    }

    public Class<?> getHelpedClass()
    {
        return Float.class;
    }
}
