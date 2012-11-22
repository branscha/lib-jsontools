/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.serializer.helper.impl;

import com.sdicons.json.serializer.helper.MarshallHelper;
import com.sdicons.json.serializer.marshall.MarshallException;
import com.sdicons.json.serializer.marshall.JSONMarshall;
import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONString;

import java.util.*;

public abstract class AbstractHelper
implements MarshallHelper
{
    public boolean equals(Object obj)
    {
        return this.getHelpedClass() == ((MarshallHelper) obj).getHelpedClass();
    }

    public void renderValue(Object aObj, JSONObject aParent, JSONMarshall aMarshall, HashMap aPool)
    throws MarshallException
    {
        aParent.getValue().put(JSONMarshall.RNDR_ATTR_VALUE, new JSONString(aObj.toString()));
    }
}
