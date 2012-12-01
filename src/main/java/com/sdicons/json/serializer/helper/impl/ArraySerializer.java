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
import com.sdicons.json.serializer.helper.ClassSerializer;

public class ArraySerializer implements ClassSerializer {
    // Error messages
    //
    private static final String ARR001 = "JSONSerializer/ArraySerializer/001: JSON->Java. Type '%s' is not an array type.";
    private static final String ARR002 = "JSONSerializer/ArraySerializer/002: JSON->Java. Exception while trying to parse an array '%s'.";
    private static final String ARR003 = "JSONSerializer/ArraySerializer/003: Java->JSON. The input object class '%s' is not an array type.";

    public void toJSON(Object javaArray, JSONObject jsonContainer, JSONSerializer serializer, HashMap<Object, Object> aPool) throws SerializerException {
        if (!javaArray.getClass().isArray()) throw new SerializerException(String.format(ARR003, javaArray.getClass().getName()));

        final JSONArray jsonArray = new JSONArray();
        jsonContainer.getValue().put(JSONSerializer.RNDR_ATTR_VALUE, jsonArray);

        if (javaArray.getClass().getComponentType().isPrimitive()) {
            // Primitive values should not be remembered in the pool.
            // Primitives are always atomic.
            //
            for (int i = 0; i < Array.getLength(javaArray); i++)
                jsonArray.getValue().add(serializer.marshal(Array.get(javaArray, i)));
        }
        else {
            // We should bring in the pool, an array can contain loops
            // to other objects and back to itself.
            //
            for (int i = 0; i < Array.getLength(javaArray); i++)
                jsonArray.getValue().add(serializer.marshalImpl(Array.get(javaArray, i), aPool));
        }
    }

    public Object toJava(JSONObject jsonObject, JSONSerializer serializer, HashMap<Object, Object> aPool) throws SerializerException {
        // Check that the class name is present in the JSON object.
        JSONSerializer.requireStringAttribute(jsonObject, JSONSerializer.RNDR_ATTR_CLASS);
        // Get the class name from the JSON object.
        final String arrayClassName = ((JSONString) jsonObject.get(JSONSerializer.RNDR_ATTR_CLASS)).getValue();
        // Try to materialize the class.
        Class<?> arrayClass;
        try {
            arrayClass = Class.forName(arrayClassName);
        }
        catch (ClassNotFoundException e1) {
            throw new SerializerException(String.format(ARR002, arrayClassName), e1);
        }
        // Check on the class itself that it denotes an array type.
        if (!arrayClass.isArray()) throw new SerializerException(String.format(ARR001, arrayClassName));
        // We can fetch the array element type.
        Class<?> compoType = arrayClass.getComponentType();
        //
        final JSONArray jsonArray = ((JSONArray) jsonObject.get(JSONSerializer.RNDR_ATTR_VALUE));
        final List<JSONValue> jsonValues = jsonArray.getValue();
        // Create the new Java array, there should not be any problems after all
        // the checks we did.
        Object javaArray = Array.newInstance(compoType, jsonValues.size());
        for (int i = 0; i < jsonValues.size(); i++) {
            Array.set(javaArray, i, serializer.unmarshalImpl((JSONObject) jsonValues.get(i), aPool));
        }
        return javaArray;
    }

    public Class<?> getHelpedClass() {
        // It does not need to return a class since the ArrayHelper is directly
        // integrated in the JSONSerializer.
        return null;
    }
}
