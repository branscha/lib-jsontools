/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.serializer.helper.impl;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.sdicons.json.model.JSONArray;
import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.model.JSONValue;
import com.sdicons.json.serializer.JSONSerializer;
import com.sdicons.json.serializer.SerializerException;
import com.sdicons.json.serializer.helper.SerializerHelper;

// TODO Refactor this class. All the primitive array stuff can be handled genernically using the Array class !

public class ArrayHelper
implements SerializerHelper
{
    // Error messages
    //
    private static final String ARR001 = "JSONSerializer/ArrayHelper/001: JSON->Java. Unknown primitive array type '%s'.";
    private static final String ARR002 = "JSONSerializer/ArrayHelper/002: JSON->Java. Exception while trying to parse an array '%s'.";

    public void renderValue(Object javaArray, JSONObject jsonContainer, JSONSerializer aMarshall, HashMap<Object, Object> aPool)
    throws SerializerException
    {
       
        // TODO Check that aObj is in fact an array of something.
        
//        JSONSerializer.requireStringAttribute(jsonContainer, JSONSerializer.RNDR_ATTR_CLASS);
        
//        final JSONString lComponentAttr = (JSONString) jsonContainer.get(JSONSerializer.RNDR_ATTR_CLASS);
//        final String lComponentName = lComponentAttr.getValue();

        final JSONArray jsonArray = new JSONArray();
        jsonContainer.getValue().put(JSONSerializer.RNDR_ATTR_VALUE, jsonArray);
        
//        Type compoType = Type.getType(lComponentName);
//        compoType.
//        
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

//        if(isPrimitiveArray(lComponentName))
//        {
//            if("I".equals(lComponentName))
//            {
//                int[] lArr = (int[]) javaArray;
//                for(int i = 0; i < lArr.length; i++)
//                    jsonArray.getValue().add(aMarshall.marshal(lArr[i]));
//            }
//            if("C".equals(lComponentName))
//            {
//                char[] lArr = (char[]) javaArray;
//                for(int i = 0; i < lArr.length; i++)
//                    jsonArray.getValue().add(aMarshall.marshal(lArr[i]));
//            }
//            else if("Z".equals(lComponentName))
//            {
//                boolean[] lArr = (boolean[]) javaArray;
//                for(int i = 0; i < lArr.length; i++)
//                    jsonArray.getValue().add(aMarshall.marshal(lArr[i]));
//            }
//            else if("S".equals(lComponentName))
//            {
//                short[] lArr = (short[]) javaArray;
//                for(int i = 0; i < lArr.length; i++)
//                   jsonArray.getValue().add(aMarshall.marshal(lArr[i]));
//            }
//            else if("B".equals(lComponentName))
//            {
//                byte[] lArr = (byte[]) javaArray;
//                for(int i = 0; i < lArr.length; i++)
//                    jsonArray.getValue().add(aMarshall.marshal(lArr[i]));;
//            }
//            else if("J".equals(lComponentName))
//            {
//                long[] lArr = (long[]) javaArray;
//                for(int i = 0; i < lArr.length; i++)
//                    jsonArray.getValue().add(aMarshall.marshal(lArr[i]));
//            }
//            else if("F".equals(lComponentName))
//            {
//                float[] lArr = (float[]) javaArray;
//                for(int i = 0; i < lArr.length; i++)
//                    jsonArray.getValue().add(aMarshall.marshal(lArr[i]));
//            }
//            else if("D".equals(lComponentName))
//            {
//                double[] lArr = (double[]) javaArray;
//                for(int i = 0; i < lArr.length; i++)
//                    jsonArray.getValue().add(aMarshall.marshal(lArr[i]));;
//            }
//        }
//        else
//        {
//            Iterator<?> lIter = Arrays.asList((Object[]) javaArray).iterator();
//            while(lIter.hasNext())
//            {
//                Object lArrEl = lIter.next();
//                jsonArray.getValue().add(aMarshall.marshalImpl(lArrEl, aPool));
//            }
//        }
    }

    public Object parseValue(JSONObject jsonObject, JSONSerializer aMarshall, HashMap<Object, Object> aPool)
    throws SerializerException
    {
        JSONSerializer.requireStringAttribute(jsonObject, JSONSerializer.RNDR_ATTR_CLASS);
        final String lArrClassName =((JSONString) jsonObject.get(JSONSerializer.RNDR_ATTR_CLASS)).getValue();

        // First we fetch all array elements.
        final JSONArray lValues = ((JSONArray) jsonObject.get(JSONSerializer.RNDR_ATTR_VALUE));
        final List<Object> lElements = new LinkedList<Object>();
        for (JSONValue jsonValue : lValues.getValue())
        {
            lElements.add(aMarshall.unmarshalImpl((JSONObject) jsonValue, aPool));
        }
        final int lArrSize = lElements.size();

        if(isPrimitiveArray(lArrClassName))
        {
            if("I".equals(lArrClassName))
            {
                int[] lArr = new int[lArrSize];
                Iterator<?> lIter = lElements.iterator();
                int i = 0;
                while(lIter.hasNext())
                {
                    lArr[i] = ((Integer) lIter.next()).intValue();
                    i++;
                }
                return lArr;
            }
            if("C".equals(lArrClassName))
            {
                char[] lArr = new char[lArrSize];
                Iterator<?> lIter = lElements.iterator();
                int i = 0;
                while(lIter.hasNext())
                {
                    lArr[i] = ((Character) lIter.next()).charValue();
                    i++;
                }
                return lArr;
            }
            else if("Z".equals(lArrClassName))
            {
                boolean[] lArr = new boolean[lArrSize];
                Iterator<?> lIter = lElements.iterator();
                int i = 0;
                while(lIter.hasNext())
                {
                    lArr[i] = ((Boolean) lIter.next()).booleanValue();
                    i++;
                }
                return lArr;
            }
            else if("S".equals(lArrClassName))
            {
                short[] lArr = new short[lArrSize];
                Iterator<?> lIter = lElements.iterator();
                int i = 0;
                while(lIter.hasNext())
                {
                    lArr[i] = ((Short) lIter.next()).shortValue();
                    i++;
                }
                return lArr;
            }
            else if("B".equals(lArrClassName))
            {
                byte[] lArr = new byte[lArrSize];
                Iterator<?> lIter = lElements.iterator();
                int i = 0;
                while(lIter.hasNext())
                {
                    lArr[i] = ((Byte) lIter.next()).byteValue();
                    i++;
                }
                return lArr;
            }
            else if("J".equals(lArrClassName))
            {
                long[] lArr = new long[lArrSize];
                Iterator<?> lIter = lElements.iterator();
                int i = 0;
                while(lIter.hasNext())
                {
                    lArr[i] = ((Long) lIter.next()).longValue();
                    i++;
                }
                return lArr;
            }
            else if("F".equals(lArrClassName))
            {
                float[] lArr = new float[lArrSize];
                Iterator<?> lIter = lElements.iterator();
                int i = 0;
                while(lIter.hasNext())
                {
                    lArr[i] = ((Float) lIter.next()).floatValue();
                    i++;
                }
                return lArr;
            }
            else if("D".equals(lArrClassName))
            {
                double[] lArr = new double[lArrSize];
                Iterator<?> lIter = lElements.iterator();
                int i = 0;
                while(lIter.hasNext())
                {
                    lArr[i] = ((Double) lIter.next()).doubleValue();
                    i++;
                }
                return lArr;
            }
            else
            {
                throw new SerializerException(String.format(ARR001, lArrClassName));
            }
        }
        else
        {
            try
            {
                Class<?> lComponentClass = Class.forName(lArrClassName);
                Object lArr = Array.newInstance(lComponentClass, lArrSize);
                Iterator<?> lIter = lElements.iterator();
                int i = 0;
                while(lIter.hasNext())
                {
                    Array.set(lArr, i, lIter.next());
                    i++;
                }
                return lArr;
            }
            catch(ClassNotFoundException e)
            {
                throw new SerializerException(String.format(ARR002, lArrClassName), e);
            }
        }
    }

    public Class<?> getHelpedClass()
    {
        return null;
    }

    private boolean isPrimitiveArray(String aClassName)
    {
        return ("I".equals(aClassName) || "Z".equals(aClassName) || "S".equals(aClassName) ||
                "B".equals(aClassName) || "J".equals(aClassName) || "F".equals(aClassName) ||
                "D".equals(aClassName) || "C".equals(aClassName));
    }
}
