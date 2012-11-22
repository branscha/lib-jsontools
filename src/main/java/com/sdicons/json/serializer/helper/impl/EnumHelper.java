/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.serializer.helper.impl;

import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.serializer.marshall.JSONMarshall;
import com.sdicons.json.serializer.marshall.MarshallException;

import java.util.HashMap;

public class EnumHelper
extends AbstractHelper
{
    public Object parseValue(JSONObject aObjectElement, JSONMarshall aMarshall, HashMap aPool)
    throws MarshallException
    {
        JSONMarshall.requireStringAttribute(aObjectElement, JSONMarshall.RNDR_ATTR_CLASS);
        final String lEnumClassName = ((JSONString) aObjectElement.get(JSONMarshall.RNDR_ATTR_CLASS)).getValue();

        final Class lEnumClass;
        try
        {
            lEnumClass = Class.forName(lEnumClassName);
        }
        catch (ClassNotFoundException e)
        {
            final String lMsg = "The class cannot be instantiated, it cannot be found in the classpath: " + lEnumClassName;
            throw new MarshallException(lMsg);
        }

        if(lEnumClass.isEnum())
        {
            Object[] lEnumVals = lEnumClass.getEnumConstants();
            for(Object lEnumVal: lEnumVals)
            {
                JSONMarshall.requireStringAttribute(aObjectElement, JSONMarshall.RNDR_ATTR_VALUE);
                final String lVal = ((JSONString) aObjectElement.get(JSONMarshall.RNDR_ATTR_VALUE)).getValue();
                if(lEnumVal.toString().equals(lVal)) return lEnumVal;
            }
        }
        else
        {
            final String lMsg = "Enum helper tried to handle a non-enum class: " + lEnumClassName;
            throw new MarshallException(lMsg);
        }

        final String lMsg = "The enum class *is found* but no matching value could be found." + lEnumClassName;
        throw new MarshallException(lMsg);
    }

    public Class getHelpedClass()
    {
        return Enum.class;
    }
}
