/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.serializer.helper.impl;

import com.sdicons.json.serializer.marshall.MarshallException;
import com.sdicons.json.serializer.marshall.JSONMarshall;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.model.JSONObject;

import java.util.*;

public class FloatHelper
extends AbstractHelper
{
    public Object parseValue(JSONObject aObjectValue, JSONMarshall aMarshall, HashMap aPool)
    throws MarshallException
    {
        JSONMarshall.requireStringAttribute(aObjectValue, JSONMarshall.RNDR_ATTR_VALUE);
        return new Float(((JSONString) aObjectValue.get(JSONMarshall.RNDR_ATTR_VALUE)).getValue());
    }

    public Class getHelpedClass()
    {
        return Float.class;
    }
}
