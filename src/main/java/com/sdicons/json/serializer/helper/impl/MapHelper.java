/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.serializer.helper.impl;

import java.util.HashMap;
import java.util.Map;

import com.sdicons.json.model.JSONArray;
import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.model.JSONValue;
import com.sdicons.json.serializer.SerializerException;
import com.sdicons.json.serializer.JSONSerializer;
import com.sdicons.json.serializer.helper.SerializerHelper;

public class MapHelper
implements SerializerHelper
{
    // Error messages.
    //
    private static final String MAP001 = "JSONSerializer/MapHelper/001: JSON->Java. IllegalAccessException while trying to instantiate map of type '%s'.";
    private static final String MAP002 = "JSONSerializer/MapHelper/002: JSON->Java. IllegalAccessException while trying to instantiate map of type '%s'.";
    private static final String MAP003 = "JSONSerializer/MapHelper/003: JSON->Java. IllegalAccessException while trying to instantiate map of type '%s'.";

    private static final String ATTR_KEY = "key";
    private static final String ATTR_VALUE = "value";

    public void renderValue(Object aObj, JSONObject aObjectElement, JSONSerializer aMarshall, HashMap<Object, Object> aPool)
    throws SerializerException
    {
        // We create a new JSON array where we will collect the elements of the
        // map. We attach this new array as the parent object value.
        final JSONArray lArray = new JSONArray();
        aObjectElement.getValue().put(JSONSerializer.RNDR_ATTR_VALUE, lArray);

        // We iterate through the keys of the map, render these as
        // JSON values and put these values in the array created above.
        final Map<?,?> lMap = (Map<?,?>) aObj;
//        final Iterator<?> lIter = lMap.keySet().iterator();
        for(Object lKey : lMap.keySet())
        {
            // Get hold of each key-value pair.
            final Object lValue = lMap.get(lKey);

            // We create a JSON object to render the key-value pairs.
            final JSONObject lKeyValuePair = new JSONObject();
            lArray.getValue().add(lKeyValuePair);
            lKeyValuePair.getValue().put(ATTR_KEY, aMarshall.marshalImpl(lKey, aPool));
            lKeyValuePair.getValue().put(ATTR_VALUE, aMarshall.marshalImpl(lValue, aPool));
        }
    }

    @SuppressWarnings("unchecked")
    public Object parseValue(JSONObject aObjectElement, JSONSerializer aMarshall, HashMap<Object, Object> aPool)
    throws SerializerException
    {
        final JSONArray lArray = (JSONArray) aObjectElement.getValue().get(JSONSerializer.RNDR_ATTR_VALUE);

        JSONSerializer.requireStringAttribute(aObjectElement, JSONSerializer.RNDR_ATTR_CLASS);
        String lMapClassName = ((JSONString) aObjectElement.get(JSONSerializer.RNDR_ATTR_CLASS)).getValue();

        try
        {
            Class<?> lMapClass = Class.forName(lMapClassName);
            Map<Object, Object> lMap;

            lMap = (Map<Object, Object>) lMapClass.newInstance();

            for(JSONValue lKeyValue : lArray.getValue())
            {
                Object lKey = aMarshall.unmarshalImpl((JSONObject) ((JSONObject) lKeyValue).getValue().get(ATTR_KEY), aPool);
                Object lValue = aMarshall.unmarshalImpl((JSONObject) ((JSONObject) lKeyValue).getValue().get(ATTR_VALUE), aPool);
                lMap.put(lKey, lValue);
            }
            return lMap;
        }
        catch (IllegalAccessException e)
        {
            throw new SerializerException(String.format(MAP001, lMapClassName), e);
        }
        catch (InstantiationException e)
        {
            throw new SerializerException(String.format(MAP002, lMapClassName), e);
        }
        catch (ClassNotFoundException e)
        {
            throw new SerializerException(String.format(MAP003, lMapClassName), e);
        }
    }

    public Class<?> getHelpedClass()
    {
        return Map.class;
    }
}
