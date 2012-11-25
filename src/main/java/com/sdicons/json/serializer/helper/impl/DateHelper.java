/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.serializer.helper.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.serializer.JSONSerializeException;
import com.sdicons.json.serializer.JSONSerializer;
import com.sdicons.json.serializer.helper.SerializeHelper;

public class DateHelper
implements SerializeHelper
{

    // Error messages.
    //
    private static final String DAT001 = "JSONSerializer/DateHelper/001: Could not parse the date '%s'.";

    private SimpleDateFormat dateFormat;

    public DateHelper()
    {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS z");
        dateFormat.setLenient(false);
    }

    public void renderValue(Object aObj, JSONObject aObjectElement, JSONSerializer aMarshall, HashMap<Object, Object> aPool)
    throws JSONSerializeException
    {
        aObjectElement.getValue().put(JSONSerializer.RNDR_ATTR_VALUE, new JSONString(dateFormat.format((Date) aObj)));
    }

    public Object parseValue(JSONObject aObjectElement, JSONSerializer aMarshall, HashMap<Object, Object> aPool)
    throws JSONSerializeException
    {
        JSONSerializer.requireStringAttribute(aObjectElement, JSONSerializer.RNDR_ATTR_VALUE);
        String dateRepr = ((JSONString) aObjectElement.get(JSONSerializer.RNDR_ATTR_VALUE)).getValue().trim();

        try
        {
            return dateFormat.parse(dateRepr);
        }
        catch(Exception e)
        {
            throw new JSONSerializeException(String.format(DAT001, dateRepr), e);
        }
    }

    public Class<?> getHelpedClass()
    {
        return Date.class;
    }
}
