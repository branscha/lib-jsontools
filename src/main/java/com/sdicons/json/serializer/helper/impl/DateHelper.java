/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.serializer.helper.impl;

import com.sdicons.json.serializer.JSONSerializeException;
import com.sdicons.json.serializer.JSONSerializer;
import com.sdicons.json.serializer.helper.SerializeHelper;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.model.JSONObject;

import java.util.*;
import java.text.*;

public class DateHelper
implements SerializeHelper
{
    private SimpleDateFormat dateFormat;

    public DateHelper()
    {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS z");
        dateFormat.setLenient(false);
    }

    public void renderValue(Object aObj, JSONObject aObjectElement, JSONSerializer aMarshall, HashMap aPool)
    throws JSONSerializeException
    {
        aObjectElement.getValue().put(JSONSerializer.RNDR_ATTR_VALUE, new JSONString(dateFormat.format((Date) aObj)));
    }

    public Object parseValue(JSONObject aObjectElement, JSONSerializer aMarshall, HashMap aPool)
    throws JSONSerializeException
    {
        JSONSerializer.requireStringAttribute(aObjectElement, JSONSerializer.RNDR_ATTR_VALUE);

        try
        {
            return dateFormat.parse(((JSONString) aObjectElement.get(JSONSerializer.RNDR_ATTR_VALUE)).getValue().trim());
        }
        catch(Exception e)
        {
            throw new JSONSerializeException(e.getMessage());
        }
    }

    public Class getHelpedClass()
    {
        return Date.class;
    }
}
