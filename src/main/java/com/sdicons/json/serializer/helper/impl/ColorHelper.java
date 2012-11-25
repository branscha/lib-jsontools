/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.serializer.helper.impl;

import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.serializer.JSONSerializeException;
import com.sdicons.json.serializer.JSONSerializer;

import java.awt.*;
import java.util.HashMap;

public class ColorHelper
extends AbstractHelper
{
    public Object parseValue(JSONObject aObjectElement, JSONSerializer aMarshall, HashMap aPool)
    throws JSONSerializeException
    {
        JSONSerializer.requireStringAttribute(aObjectElement, JSONSerializer.RNDR_ATTR_VALUE);
        return Color.decode(((JSONString) aObjectElement.get(JSONSerializer.RNDR_ATTR_VALUE)).getValue());
    }

    public void renderValue(Object aObj, JSONObject aParent, JSONSerializer aMarshall, HashMap aPool) throws JSONSerializeException
    {
        final Color lColor = (Color) aObj;
        aParent.getValue().put(JSONSerializer.RNDR_ATTR_VALUE, new JSONString("" + lColor.getRGB()));
    }

    public Class getHelpedClass()
    {
        return Color.class;
    }
}
