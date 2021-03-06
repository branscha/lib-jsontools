/*******************************************************************************
 * Copyright (c) 2006-2013 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.serializer.helper;

import java.util.HashMap;

import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.serializer.JSONSerializer;
import com.sdicons.json.serializer.SerializerException;

public abstract class AbstractSerializer
implements ClassSerializer
{
    public boolean equals(Object obj)
    {
        return this.getHelpedClass() == ((ClassSerializer) obj).getHelpedClass();
    }

    public void toJSON(Object javaObj, JSONObject jsonContainer, JSONSerializer serializer, HashMap<Object, Object> aPool)
    throws SerializerException
    {
        jsonContainer.getValue().put(JSONSerializer.RNDR_ATTR_VALUE, new JSONString(javaObj.toString()));
    }
}
