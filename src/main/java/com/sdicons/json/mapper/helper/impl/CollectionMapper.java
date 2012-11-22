/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.mapper.helper.impl;

import com.sdicons.json.mapper.JSONMapper;
import com.sdicons.json.mapper.MapperException;
import com.sdicons.json.mapper.helper.ComplexMapperHelper;
import com.sdicons.json.model.JSONArray;
import com.sdicons.json.model.JSONValue;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class CollectionMapper
implements ComplexMapperHelper
{
    public Class getHelpedClass()
    {
        return Collection.class;
    }

    public Object toJava(JSONValue aValue, Class aRequestedClass)
    throws MapperException
    {
        return this.toJava(aValue, aRequestedClass, new Type[0]);
    }

    public Object toJava(JSONValue aValue, Class aRawClass, Type[] aTypes)
    throws MapperException
    {
        if (!aValue.isArray()) throw new MapperException("CollectionMapper cannot map: " + aValue.getClass().getName());
        if (!Collection.class.isAssignableFrom(aRawClass))
            throw new MapperException("CollectionMapper cannot map: " + aValue.getClass().getName());
        JSONArray aObject = (JSONArray) aValue;

        Collection lCollObj;

        try
        {
            // First we try to instantiate the correct
            // collection class.
            if(aRawClass.isInterface()){
            	//we still can't deal with some unusual interfaces. 
            	if(aRawClass==Set.class){
            		lCollObj = new HashSet();
            	}else if(aRawClass==SortedSet.class){
            		lCollObj = new TreeSet();
            	}else{
            		lCollObj = new LinkedList();
            	}
            }else{
            	lCollObj = (Collection) aRawClass.newInstance();	
            }
        }
        catch (Exception e)
        {
            // If the requested class cannot create an instance because
            // it is abstract, or an interface, we use a default fallback class.
            // This solution is far from perfect, but we try to make the mapper
            // as convenient as possible.
            lCollObj = new LinkedList();
        }

        if(aTypes.length == 0)
        {
            // Simple, raw collection.
            for (JSONValue lVal : aObject.getValue())
            {
                lCollObj.add(JSONMapper.toJava(lVal));
            }
        }
        else if(aTypes.length == 1)
        {
            // Generic collection, we can make use of the type of the elements.            
            for (JSONValue lVal : aObject.getValue())
            {
                
                if(aTypes[0] instanceof Class)
                	lCollObj.add(JSONMapper.toJava(lVal, (Class) aTypes[0]));
                else
                	lCollObj.add(JSONMapper.toJava(lVal, (ParameterizedType) aTypes[0]));                	               
            }
        }
        else
        {
            // Not possible, a collection cannot have more than two types for
            // its contents.
            throw new MapperException("CollectionMapper cannot map: " + aValue.getClass().getName());
        }

        return lCollObj;
    }

    public JSONValue toJSON(Object aPojo)
    throws MapperException
    {
        JSONArray lArray = new JSONArray();
        if(! Collection.class.isAssignableFrom(aPojo.getClass())) throw new MapperException("CollectionMapper cannot map: " + aPojo.getClass().getName());

        Collection lColl = (Collection) aPojo;
        for(Object lEl : lColl)
        {
            lArray.getValue().add(JSONMapper.toJSON(lEl));
        }
        return lArray;
    }
}
