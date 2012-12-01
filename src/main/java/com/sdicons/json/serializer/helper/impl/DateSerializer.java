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
import com.sdicons.json.serializer.JSONSerializer;
import com.sdicons.json.serializer.SerializerException;
import com.sdicons.json.serializer.helper.ClassSerializer;

public class DateSerializer
implements ClassSerializer
{

    // Error messages.
    //
    private static final String DAT001 = "JSONSerializer/DateHelper/001: JSON->Java. Could not parse the value '%s' to a Date.";

    private SimpleDateFormat df = new SimpleDateFormat(JSONSerializer.DATESERIALIZE_DEFAULT);

    public void toJSON(Object aObj, JSONObject aObjectElement, JSONSerializer serializer, HashMap<Object, Object> aPool)
    throws SerializerException
    {
        initFormat(serializer);
        aObjectElement.getValue().put(JSONSerializer.RNDR_ATTR_VALUE, new JSONString(df.format((Date) aObj)));
    }

    public Object toJava(JSONObject aObjectElement, JSONSerializer serializer, HashMap<Object, Object> aPool)
    throws SerializerException
    {
        JSONSerializer.requireStringAttribute(aObjectElement, JSONSerializer.RNDR_ATTR_VALUE);
        String dateRepr = ((JSONString) aObjectElement.get(JSONSerializer.RNDR_ATTR_VALUE)).getValue().trim();

        initFormat(serializer);

        try
        {
            return df.parse(dateRepr);
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

    private void initFormat(JSONSerializer  serializer) {
        if(serializer.hasSerializeOption(JSONSerializer.DATESERIALIZE_DEFAULT)) {
            String format = (String) serializer.getSerializeOption(JSONSerializer.OPT_DATESERIALIZE, JSONSerializer.DATESERIALIZE_DEFAULT);
            if(!df.toPattern().equals(format)) {
                df = new SimpleDateFormat(format);
            }
        }
    }
}
