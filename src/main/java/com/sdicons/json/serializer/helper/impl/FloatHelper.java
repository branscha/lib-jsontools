/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.serializer.helper.impl;

import com.sdicons.json.serializer.JSONSerializeException;
import com.sdicons.json.serializer.JSONSerializer;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.model.JSONObject;

import java.util.*;

public class FloatHelper
extends AbstractHelper
{
    public Object parseValue(JSONObject aObjectValue, JSONSerializer aMarshall, HashMap aPool)
    throws JSONSerializeException
    {
        JSONSerializer.requireStringAttribute(aObjectValue, JSONSerializer.RNDR_ATTR_VALUE);
        return new Float(((JSONString) aObjectValue.get(JSONSerializer.RNDR_ATTR_VALUE)).getValue());
    }

    public Class getHelpedClass()
    {
        return Float.class;
    }
}
