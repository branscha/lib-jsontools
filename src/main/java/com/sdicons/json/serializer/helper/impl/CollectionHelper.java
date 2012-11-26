/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.serializer.helper.impl;

import java.util.Collection;
import java.util.HashMap;

import com.sdicons.json.model.JSONArray;
import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.model.JSONValue;
import com.sdicons.json.serializer.JSONSerializeException;
import com.sdicons.json.serializer.JSONSerializer;
import com.sdicons.json.serializer.helper.SerializeHelper;

public class CollectionHelper
implements SerializeHelper
{
    // Error messages.
    //
    private static final String COLL001 = "JSONSerializer/CollectionHelper/001: JSON->Java. IllegalAccessException while trying to instantiate collection '%s'.";
    private static final String COLL002 = "JSONSerializer/CollectionHelper/001: JSON->Java. InstantiationException while trying to instantiate collection '%s'.";
    private static final String COLL003 = "JSONSerializer/CollectionHelper/001: JSON->Java. ClassNotFoundException while trying to instantiate collection '%s'.";

    public CollectionHelper()
    {
    }

    public void renderValue(Object aObj, JSONObject aObjectElement, JSONSerializer aMarshall, HashMap<Object, Object> aPool)
    throws JSONSerializeException
    {
        // We create a new JSON array where we will collect the elements of the
        // collection. We attach this new array as the parent object value.
        final JSONArray lArray = new JSONArray();
        aObjectElement.getValue().put(JSONSerializer.RNDR_ATTR_VALUE, lArray);

        // We iterate through the elements of the collection, render these as
        // JSON values and put these values in the array created above.
        final Collection<?> lCollection = (Collection<?>) aObj;
        for(Object lColEl : lCollection)
        {
            lArray.getValue().add(aMarshall.marshalImpl(lColEl, aPool));
        }
    }

    @SuppressWarnings("unchecked")
    public Object parseValue(JSONObject aObjectElement, JSONSerializer aMarshall, HashMap<Object, Object> aPool)
    throws JSONSerializeException
    {
        final JSONArray lArray = (JSONArray) aObjectElement.getValue().get(JSONSerializer.RNDR_ATTR_VALUE);

        JSONSerializer.requireStringAttribute(aObjectElement, JSONSerializer.RNDR_ATTR_CLASS);
        String lCollectionClassName = ((JSONString) aObjectElement.get(JSONSerializer.RNDR_ATTR_CLASS)).getValue();

        try
        {
            Class<?> lCollectionClass = Class.forName(lCollectionClassName);
            Collection<Object> lCollection;

            lCollection = (Collection<Object>) lCollectionClass.newInstance();
//            if (lId != null) aPool.put(lId, lCollection);

            for(JSONValue lVal : lArray.getValue())
            {
                lCollection.add(aMarshall.unmarshalImpl((JSONObject)lVal, aPool));
            }
            return lCollection;
        }
        catch (IllegalAccessException e)
        {
            throw new JSONSerializeException(String.format(COLL001, lCollectionClassName), e);
        }
        catch (InstantiationException e)
        {
            throw new JSONSerializeException(String.format(COLL002, lCollectionClassName), e);
        }
        catch (ClassNotFoundException e)
        {
            throw new JSONSerializeException(String.format(COLL003, lCollectionClassName), e);
        }
    }

    public Class<?> getHelpedClass()
    {
        return Collection.class;
    }
}
