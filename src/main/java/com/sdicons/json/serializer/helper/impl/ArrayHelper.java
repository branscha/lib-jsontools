/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.serializer.helper.impl;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.sdicons.json.model.JSONArray;
import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.model.JSONValue;
import com.sdicons.json.serializer.SerializerException;
import com.sdicons.json.serializer.JSONSerializer;
import com.sdicons.json.serializer.helper.SerializerHelper;

public class ArrayHelper
implements SerializerHelper
{
    // Error messages
    //
    private static final String ARR001 = "JSONSerializer/ArrayHelper/001: JSON->Java. Unknown primitive array type '%s'.";
    private static final String ARR002 = "JSONSerializer/ArrayHelper/002: JSON->Java. Exception while trying to parse an array '%s'.";

    public void renderValue(Object aObj, JSONObject aObjectElement, JSONSerializer aMarshall, HashMap<Object, Object> aPool)
    throws SerializerException
    {
        JSONSerializer.requireStringAttribute(aObjectElement, JSONSerializer.RNDR_ATTR_CLASS);
        final JSONString lComponentAttr = (JSONString) aObjectElement.get(JSONSerializer.RNDR_ATTR_CLASS);
        final String lComponentName = lComponentAttr.getValue();

        final JSONArray lElements = new JSONArray();
        aObjectElement.getValue().put(JSONSerializer.RNDR_ATTR_VALUE, lElements);

        if(isPrimitiveArray(lComponentName))
        {
            if("I".equals(lComponentName))
            {
                int[] lArr = (int[]) aObj;
                for(int i = 0; i < lArr.length; i++)
                    lElements.getValue().add(aMarshall.marshal(lArr[i]));
            }
            if("C".equals(lComponentName))
            {
                char[] lArr = (char[]) aObj;
                for(int i = 0; i < lArr.length; i++)
                    lElements.getValue().add(aMarshall.marshal(lArr[i]));
            }
            else if("Z".equals(lComponentName))
            {
                boolean[] lArr = (boolean[]) aObj;
                for(int i = 0; i < lArr.length; i++)
                    lElements.getValue().add(aMarshall.marshal(lArr[i]));
            }
            else if("S".equals(lComponentName))
            {
                short[] lArr = (short[]) aObj;
                for(int i = 0; i < lArr.length; i++)
                   lElements.getValue().add(aMarshall.marshal(lArr[i]));
            }
            else if("B".equals(lComponentName))
            {
                byte[] lArr = (byte[]) aObj;
                for(int i = 0; i < lArr.length; i++)
                    lElements.getValue().add(aMarshall.marshal(lArr[i]));;
            }
            else if("J".equals(lComponentName))
            {
                long[] lArr = (long[]) aObj;
                for(int i = 0; i < lArr.length; i++)
                    lElements.getValue().add(aMarshall.marshal(lArr[i]));
            }
            else if("F".equals(lComponentName))
            {
                float[] lArr = (float[]) aObj;
                for(int i = 0; i < lArr.length; i++)
                    lElements.getValue().add(aMarshall.marshal(lArr[i]));
            }
            else if("D".equals(lComponentName))
            {
                double[] lArr = (double[]) aObj;
                for(int i = 0; i < lArr.length; i++)
                    lElements.getValue().add(aMarshall.marshal(lArr[i]));;
            }
        }
        else
        {
            Iterator<?> lIter = Arrays.asList((Object[]) aObj).iterator();
            while(lIter.hasNext())
            {
                Object lArrEl = lIter.next();
                lElements.getValue().add(aMarshall.marshalImpl(lArrEl, aPool));
            }
        }
    }

    public Object parseValue(JSONObject aObjectElement, JSONSerializer aMarshall, HashMap<Object, Object> aPool)
    throws SerializerException
    {
        JSONSerializer.requireStringAttribute(aObjectElement, JSONSerializer.RNDR_ATTR_CLASS);
        final String lArrClassName =((JSONString) aObjectElement.get(JSONSerializer.RNDR_ATTR_CLASS)).getValue();

        // First we fetch all array elements.
        final JSONArray lValues = ((JSONArray) aObjectElement.get(JSONSerializer.RNDR_ATTR_VALUE));
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
