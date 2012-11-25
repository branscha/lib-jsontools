/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.serializer.helper.impl;

import com.sdicons.json.serializer.JSONSerializeException;
import com.sdicons.json.serializer.JSONSerializer;
import com.sdicons.json.serializer.helper.SerializeHelper;
import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONString;

import java.util.*;

public abstract class AbstractHelper
implements SerializeHelper
{
    public boolean equals(Object obj)
    {
        return this.getHelpedClass() == ((SerializeHelper) obj).getHelpedClass();
    }

    public void renderValue(Object aObj, JSONObject aParent, JSONSerializer aMarshall, HashMap aPool)
    throws JSONSerializeException
    {
        aParent.getValue().put(JSONSerializer.RNDR_ATTR_VALUE, new JSONString(aObj.toString()));
    }
}
