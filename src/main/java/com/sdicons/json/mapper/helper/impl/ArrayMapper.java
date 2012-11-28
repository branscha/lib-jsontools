/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.mapper.helper.impl;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import com.sdicons.json.mapper.JSONMapper;
import com.sdicons.json.mapper.MapperException;
import com.sdicons.json.mapper.helper.MapperHelper;
import com.sdicons.json.model.JSONArray;
import com.sdicons.json.model.JSONValue;

/**
 * Map native arrays from and to JSON arrays. It is a pseudo mapper, it is not accessed from the repository but it is directly
 * integrated in the JSONMapper. So this mapper has a special status.
 */
public class ArrayMapper
implements MapperHelper
{
    private static final String ARR001 = "JSONMapper/ArrayMapper/001: JSON->Java. Don't know how to map JSON class '%s' to a Java array.";
    private static final String ARR002 = "JSONMapper/ArrayMapper/002: Java->JSON. Cannot map Java class '%s' to a JSONArray.";
    private static final String ARR003 = "JSONMapper/ArrayMapper/003: JSON->Java. The requested Java type '%s' is not an array type.";

    public JSONValue toJSON(JSONMapper mapper, Object aObj) throws MapperException {
        final Class<?> lClass = aObj.getClass();
        final String lObjClassName = lClass.getName();

        if (!lClass.isArray()) {
            throw new MapperException(String.format(ARR002, lObjClassName));
        }

        final JSONArray jsonArray = new JSONArray();
        final List<JSONValue> jsonContents = jsonArray.getValue();
        for(int i = 0; i < Array.getLength(aObj); i++) {
            jsonContents.add(mapper.toJSON(Array.get(aObj, i)));
        }
        return jsonArray;
    }

    public Class<?> getHelpedClass()
    {
        // We can afford to return null, the ArrayMapper is not in the repository, the JSONMapper uses this
        // class directly. Since this mapper is not retrieved from the repository, we don't have to return a class name.
        return null;
    }

    public Object toJava(JSONMapper mapper, JSONValue aValue, Class<?> aRequestedClass) throws MapperException {

        // Check if the JSON object represents a JSONArray
        if (!aValue.isArray()) {
            throw new MapperException(String.format(ARR001, aValue.getClass().getName()));
        }

        // Check if the requested Java type is an array type as well.
        if (!aRequestedClass.isArray()) {
            throw new MapperException(String.format(ARR003, aRequestedClass.getName()));
        }

        Class<?> compoType = aRequestedClass.getComponentType();

        // First we fetch all array elements.
        JSONArray jsonArray = (JSONArray) aValue;

        // PART 1. First we convert all the values in the JSON array to the
        // target type.
        // The resulting values will be collected in the lElements list.
        // //////////////////////////////////////////////////////////////////////////////

        final List<Object> objArray = new ArrayList<Object>(jsonArray.size());
        for (JSONValue jsonValue : jsonArray.getValue())
            objArray.add(mapper.toJava(jsonValue, compoType));

        // PART 2. Now we will convert the object values in lElements into
        // the real Java arrays.
        // /////////////////////////////////////////////////////////////////

        final int lArrSize = objArray.size();
        Object test = Array.newInstance(compoType, lArrSize);
        int itest = 0;
        for (Object el : objArray) {
            Array.set(test, itest++, el);
        }
        return test;
    }
}
