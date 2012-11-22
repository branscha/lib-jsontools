/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.serializer.helper.impl;

import com.sdicons.json.serializer.helper.MarshallHelper;
import com.sdicons.json.serializer.marshall.MarshallException;
import com.sdicons.json.serializer.marshall.JSONMarshall;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.model.JSONObject;

import java.util.*;
import java.text.*;

public class DateHelper
implements MarshallHelper
{
    private SimpleDateFormat dateFormat;

    public DateHelper()
    {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS z");
        dateFormat.setLenient(false);
    }

    public void renderValue(Object aObj, JSONObject aObjectElement, JSONMarshall aMarshall, HashMap aPool)
    throws MarshallException
    {
        aObjectElement.getValue().put(JSONMarshall.RNDR_ATTR_VALUE, new JSONString(dateFormat.format((Date) aObj)));
    }

    public Object parseValue(JSONObject aObjectElement, JSONMarshall aMarshall, HashMap aPool)
    throws MarshallException
    {
        JSONMarshall.requireStringAttribute(aObjectElement, JSONMarshall.RNDR_ATTR_VALUE);

        try
        {
            return dateFormat.parse(((JSONString) aObjectElement.get(JSONMarshall.RNDR_ATTR_VALUE)).getValue().trim());
        }
        catch(Exception e)
        {
            throw new MarshallException(e.getMessage());
        }
    }

    public Class getHelpedClass()
    {
        return Date.class;
    }
}
