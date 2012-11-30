/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.mapper.helper.impl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.sdicons.json.mapper.JSONMapper;
import com.sdicons.json.mapper.MapperException;
import com.sdicons.json.mapper.helper.ComplexMapperHelper;
import com.sdicons.json.model.JSONArray;
import com.sdicons.json.model.JSONValue;

public class CollectionMapper
implements ComplexMapperHelper
{
    private static final String COLL001 = "JSONMapper/CollectionMapper/001: JSON->Java. Cannot map class JSON '%s' to Java Collection.";
    private static final String COLL002 = "JSONMapper/CollectionMapper/002: Java->JSON. Cannot map Java class '%s' to JSONArray.";
    private static final String COLL003 = "JSONMapper/CollectionMapper/003: JSON->Java. Cannot map to Java class '%s'.";

    public Class<?> getHelpedClass()
    {
        return Collection.class;
    }

    public Object toJava(JSONMapper mapper, JSONValue aValue, Class<?> aRequestedClass)
    throws MapperException
    {
        return this.toJava(mapper, aValue, aRequestedClass, new Type[0]);
    }

    @SuppressWarnings("unchecked")
    public Object toJava(JSONMapper mapper, JSONValue aValue, Class<?> aRawClass, Type[] aTypes)
    throws MapperException
    {
        if (!aValue.isArray()) 
            throw new MapperException(String.format(COLL001, aValue.getClass().getName()));

        // The requested class should be derived from Collection.
        // We will try to create the requested class, therefore we will attempt
        // to return the correct type.
        //
        if (!Collection.class.isAssignableFrom(aRawClass))
            throw new MapperException(String.format(COLL003, aRawClass.getName()));
        
        JSONArray jsonArray = (JSONArray) aValue;
        List<JSONValue> jsonColl = jsonArray.getValue();
        Collection<Object> javaColl;
        
        try {
            // First try to instantiate the raw class.
            // It can fail for eg. unmodifiable collections.
            // If it fails we will try other things.
            //
            javaColl = (Collection<Object>) aRawClass.newInstance();
        }
        catch(Exception e){
            // We could not instantiate the requested class, but 
            // maybe we can provide a substitute.
            if(SortedSet.class.isAssignableFrom(aRawClass)) {
                javaColl = new TreeSet<Object>();
            }
            else if(Set.class.isAssignableFrom(aRawClass)){
                javaColl = new HashSet<Object>(jsonColl.size());
            }
            else {
                javaColl = new LinkedList<Object>();
            }
        }

        if(aTypes.length == 0)
        {
            // Simple, raw collection.
            for (JSONValue lVal : jsonArray.getValue())
            {
                javaColl.add(mapper.toJava(lVal));
            }
        }
        else if(aTypes.length == 1)
        {
            // Generic collection, we can make use of the type of the elements.
            for (JSONValue lVal : jsonArray.getValue())
            {
                if(aTypes[0] instanceof Class)
                	javaColl.add(mapper.toJava(lVal, (Class<?>) aTypes[0]));
                else
                	javaColl.add(mapper.toJava(lVal, (ParameterizedType) aTypes[0]));
            }
        }
        else
        {
            // Not possible, a collection cannot have more than two types for
            // its contents.
            throw new MapperException(String.format(COLL001, aValue.getClass().getName()));
        }
        return javaColl;
    }

    @SuppressWarnings("unchecked")
    public JSONValue toJSON(JSONMapper mapper, Object aPojo)
    throws MapperException
    {
        if(!Collection.class.isAssignableFrom(aPojo.getClass())) 
            throw new MapperException(String.format(COLL002, aPojo.getClass().getName()));

        JSONArray jsonArray = new JSONArray();
        List<JSONValue> jsonColl = jsonArray.getValue();
        
        Collection<Object> objColl = (Collection<Object>) aPojo;
        for(Object obj : objColl)
        {
            // Convert each element and add it 
            // to our JSON array.
            jsonColl.add(mapper.toJSON(obj));
        }
        return jsonArray;
    }
}
