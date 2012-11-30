/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.serializer.helper.impl;

/**
 * The ArrayHelper is a pseudo helper, it is not selected from the repository as the other helpers are, but it is directly
 * integrated in and invoked by the JSONSerializer.
 */
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.List;

import com.sdicons.json.model.JSONArray;
import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.model.JSONValue;
import com.sdicons.json.serializer.JSONSerializer;
import com.sdicons.json.serializer.SerializerException;
import com.sdicons.json.serializer.helper.SerializerHelper;

public class ArrayHelper
implements SerializerHelper
{
    // Error messages
    //
    private static final String ARR001 = "JSONSerializer/ArrayHelper/001: JSON->Java. Type '%s' is not an array type.";
    private static final String ARR002 = "JSONSerializer/ArrayHelper/002: JSON->Java. Exception while trying to parse an array '%s'.";

    public void toJSON(Object javaArray, JSONObject jsonContainer, JSONSerializer aMarshall, HashMap<Object, Object> aPool)
    throws SerializerException
    {
        // TODO Check that aObj is in fact an array of something.

        final JSONArray jsonArray = new JSONArray();
        jsonContainer.getValue().put(JSONSerializer.RNDR_ATTR_VALUE, jsonArray);

        if (javaArray.getClass().getComponentType().isPrimitive()) {
            // Primitive values should not be remembered in the pool.
            // Primitives are always atomic.
            //
            for (int i = 0; i < Array.getLength(javaArray); i++)
                jsonArray.getValue().add(aMarshall.marshal(Array.get(javaArray, i)));
        }
        else {
            // We should bring in the pool, an array can contain loops
            // to other objects and back to itself.
            //
            for (int i = 0; i < Array.getLength(javaArray); i++)
                jsonArray.getValue().add(aMarshall.marshalImpl(Array.get(javaArray, i), aPool));
        }
    }

    public Object toJava(JSONObject jsonObject, JSONSerializer aMarshall, HashMap<Object, Object> aPool)
    throws SerializerException
    {
        JSONSerializer.requireStringAttribute(jsonObject, JSONSerializer.RNDR_ATTR_CLASS);
        final String lArrClassName =((JSONString) jsonObject.get(JSONSerializer.RNDR_ATTR_CLASS)).getValue();

        Class<?> arrayClass;
        try {
            arrayClass = Class.forName(lArrClassName);
        }
        catch (ClassNotFoundException e1) {
            throw new SerializerException(String.format(ARR002, lArrClassName), e1);
        }

        if(!arrayClass.isArray())
            throw new SerializerException(String.format(ARR001, lArrClassName));

        Class<?> compoType = arrayClass.getComponentType();

        // First we fetch all array elements.
        final JSONArray jsonArray = ((JSONArray) jsonObject.get(JSONSerializer.RNDR_ATTR_VALUE));
        final List<JSONValue> jsonValues = jsonArray.getValue();

        Object javaArray = Array.newInstance(compoType, jsonValues.size());
        for(int i =0; i < jsonValues.size(); i ++) {
            Array.set(javaArray, i, aMarshall.unmarshalImpl((JSONObject) jsonValues.get(i), aPool));
        }
        return javaArray;
    }

    public Class<?> getHelpedClass()
    {
        // It does not need to return a class since the ArrayHelper is directly integrated in the JSONSerializer.
        return null;
    }
}
