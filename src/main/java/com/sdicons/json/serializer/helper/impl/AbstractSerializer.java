/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.serializer.helper.impl;

import java.util.HashMap;

import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.serializer.SerializerException;
import com.sdicons.json.serializer.JSONSerializer;
import com.sdicons.json.serializer.helper.ClassSerializer;

public abstract class AbstractSerializer
implements ClassSerializer
{
    public boolean equals(Object obj)
    {
        return this.getHelpedClass() == ((ClassSerializer) obj).getHelpedClass();
    }

    public void toJSON(Object aObj, JSONObject aParent, JSONSerializer serializer, HashMap<Object, Object> aPool)
    throws SerializerException
    {
        aParent.getValue().put(JSONSerializer.RNDR_ATTR_VALUE, new JSONString(aObj.toString()));
    }
}
