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

public class IntegerHelper
extends AbstractHelper
{
    public Object parseValue(JSONObject aObjectElement, JSONSerializer aMarshall, HashMap aPool)
    throws JSONSerializeException
    {
        JSONSerializer.requireStringAttribute(aObjectElement, JSONSerializer.RNDR_ATTR_VALUE);
        return new Integer(((JSONString) aObjectElement.get(JSONSerializer.RNDR_ATTR_VALUE)).getValue());
    }

    public Class getHelpedClass()
    {
        return Integer.class;
    }
}
