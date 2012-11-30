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
import com.sdicons.json.serializer.SerializerException;
import com.sdicons.json.serializer.JSONSerializer;
import com.sdicons.json.serializer.helper.SerializerHelper;

public class DateHelper
implements SerializerHelper
{

    // Error messages.
    //
    private static final String DAT001 = "JSONSerializer/DateHelper/001: JSON->Java. Could not parse the value '%s' to a Date.";

    private SimpleDateFormat dateFormat;

    public DateHelper()
    {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS z");
        dateFormat.setLenient(false);
    }

    public void toJSON(Object aObj, JSONObject aObjectElement, JSONSerializer aMarshall, HashMap<Object, Object> aPool)
    throws SerializerException
    {
        aObjectElement.getValue().put(JSONSerializer.RNDR_ATTR_VALUE, new JSONString(dateFormat.format((Date) aObj)));
    }

    public Object toJava(JSONObject aObjectElement, JSONSerializer aMarshall, HashMap<Object, Object> aPool)
    throws SerializerException
    {
        JSONSerializer.requireStringAttribute(aObjectElement, JSONSerializer.RNDR_ATTR_VALUE);
        String dateRepr = ((JSONString) aObjectElement.get(JSONSerializer.RNDR_ATTR_VALUE)).getValue().trim();

        try
        {
            return dateFormat.parse(dateRepr);
        }
        catch(Exception e)
        {
            throw new SerializerException(String.format(DAT001, dateRepr), e);
        }
    }

    public Class<?> getHelpedClass()
    {
        return Date.class;
    }
}
