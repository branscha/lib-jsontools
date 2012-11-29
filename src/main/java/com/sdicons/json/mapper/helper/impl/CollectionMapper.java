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
        
        if (!Collection.class.isAssignableFrom(aRawClass))
            throw new MapperException(String.format(COLL001,aValue.getClass().getName()));
        
        JSONArray aObject = (JSONArray) aValue;

        Collection<Object> lCollObj;

        try
        {
            // First we try to instantiate the correct
            // collection class.
            if(aRawClass.isInterface()){
            	//we still can't deal with some unusual interfaces.
            	if(aRawClass==Set.class){
            		lCollObj = new HashSet<Object>();
            	}
            	else if(aRawClass==SortedSet.class){
            		lCollObj = new TreeSet<Object>();
            	}
            	else{
            		lCollObj = new LinkedList<Object>();
            	}
            }else{
            	lCollObj = (Collection<Object>) aRawClass.newInstance();
            }
        }
        catch (Exception e)
        {
            // If the requested class cannot create an instance because
            // it is abstract, or an interface, we use a default fall back class.
            // This solution is far from perfect, but we try to make the mapper
            // as convenient as possible.
            lCollObj = new LinkedList<Object>();
        }

        if(aTypes.length == 0)
        {
            // Simple, raw collection.
            for (JSONValue lVal : aObject.getValue())
            {
                lCollObj.add(mapper.toJava(lVal));
            }
        }
        else if(aTypes.length == 1)
        {
            // Generic collection, we can make use of the type of the elements.
            for (JSONValue lVal : aObject.getValue())
            {
                if(aTypes[0] instanceof Class)
                	lCollObj.add(mapper.toJava(lVal, (Class<?>) aTypes[0]));
                else
                	lCollObj.add(mapper.toJava(lVal, (ParameterizedType) aTypes[0]));
            }
        }
        else
        {
            // Not possible, a collection cannot have more than two types for
            // its contents.
            throw new MapperException(String.format(COLL001, aValue.getClass().getName()));
        }

        return lCollObj;
    }

    @SuppressWarnings("unchecked")
    public JSONValue toJSON(JSONMapper mapper, Object aPojo)
    throws MapperException
    {
        JSONArray lArray = new JSONArray();
        if(! Collection.class.isAssignableFrom(aPojo.getClass())) throw new MapperException(String.format(COLL002, aPojo.getClass().getName()));

        Collection<Object> lColl = (Collection<Object>) aPojo;
        for(Object lEl : lColl)
        {
            lArray.getValue().add(mapper.toJSON(lEl));
        }
        return lArray;
    }
}
