/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.mapper.helper;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

import com.sdicons.json.mapper.JSONMapper;
import com.sdicons.json.mapper.MapperException;
import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONValue;

public class MapMapper
implements ComplexClassMapper
{
    private static final String MAP001 = "JSONMapper/MapMapper/001: JSON->Java. Cannot map JSON class '%s' to a Java Map.";
    private static final String MAP002 = "JSONMapper/MapMapper/002: JSON->Java. Currently only supports String keys.";
    private static final String MAP003 = "JSONMapper/MapMapper/003: Java->JSON. Cannot map Java class '%s' to JSONObject that represents a map.";
    private static final String MAP004 = "JSONMapper/MapMapper/004: JSON->Java. Cannot map to Java class '%s'.";


    public Class<?> getHelpedClass()
    {
        return Map.class;
    }

    public Object toJava(JSONMapper mapper, JSONValue aValue, Class<?> aRequestedClass)
    throws MapperException
    {
        return this.toJava(mapper, aValue, aRequestedClass, new Type[0]);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Object toJava(JSONMapper mapper, JSONValue aValue, Class<?> aRawClass, Type[] aTypes)
    throws MapperException
    {
        if (!aValue.isObject())
            throw new MapperException(String.format(MAP001, aValue.getClass().getName()));

        // The requested class should be derived from Map.
        // We will try to create the requested class, therefore we will attempt
        // to return the correct type.
        //
        if (!Map.class.isAssignableFrom(aRawClass))
            throw new MapperException(String.format(MAP004, aRawClass.getName()));

        JSONObject aObject = (JSONObject) aValue;

        Map lMapObj;

        try
        {
            // First we try to instantiate the correct
            // collection class.
            lMapObj = (Map) aRawClass.newInstance();
        }
        catch (Exception e)
        {
            // If the requested class cannot create an instance because
            // it is abstract, or an interface, we use a default fall back class.
            // This solution is far from perfect, but we try to make the mapper
            // as convenient as possible.
            lMapObj = new LinkedHashMap();
        }

        if(aTypes.length == 0)
        {
            // Simple, raw collection.
            for (String lKey : aObject.getValue().keySet())
            {
                JSONValue lVal = aObject.getValue().get(lKey);
                lMapObj.put(lKey, mapper.toJava(lVal));
            }
        }
        else if(aTypes.length == 2)
        {
            // Generic map, we can make use of the type of the elements.
            if(!aTypes[0].equals(String.class)) throw new MapperException(MAP002);
            else
            {
                for (String lKey : aObject.getValue().keySet())
                {
                    JSONValue lVal = aObject.getValue().get(lKey);
                    if(aTypes[1] instanceof Class)
                    	lMapObj.put(lKey, mapper.toJava(lVal, (Class) aTypes[1]));
                    else if(aTypes[1] instanceof ParameterizedType)
                    	lMapObj.put(lKey, mapper.toJava(lVal, (ParameterizedType) aTypes[1]));
                    else
                        lMapObj.put(lKey, mapper.toJava(lVal));

                }
            }
        }
        else
        {
            // Not possible, a collection cannot have more than two types for
            // its contents.
            throw new MapperException(String.format(MAP001, aValue.getClass().getName()));
        }
        return lMapObj;
    }

    @SuppressWarnings("rawtypes")
    public JSONValue toJSON(JSONMapper mapper, Object aPojo)
    throws MapperException
    {
        final JSONObject lObj = new JSONObject();

        if(! Map.class.isAssignableFrom(aPojo.getClass()))
            throw new MapperException(String.format(MAP003, aPojo.getClass().getName()));

        Map lMap = (Map) aPojo;
        for(Object lKey : lMap.keySet())
        {
            lObj.getValue().put(lKey.toString(), mapper.toJSON(lMap.get(lKey)));
        }
        return lObj;
    }
}
