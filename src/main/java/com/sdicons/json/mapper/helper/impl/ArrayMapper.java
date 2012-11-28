/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.mapper.helper.impl;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.sdicons.json.mapper.JSONMapper;
import com.sdicons.json.mapper.MapperException;
import com.sdicons.json.mapper.helper.SimpleMapperHelper;
import com.sdicons.json.model.JSONArray;
import com.sdicons.json.model.JSONValue;

/**
 * Map native arrays from and to JSON arrays.
 */
public class ArrayMapper
implements SimpleMapperHelper
{
    private static final String ARR001 = "JSONMapper/ArrayMapper/001: JSON->Java. Don't know how to map JSON class'%s' to Java.";
    private static final String ARR002 = "JSONMapper/ArrayMapper/002: JSON->Java. Unknown primitive array type '%s'.";
    private static final String ARR003 = "JSONMapper/ArrayMapper/003: JSON->Java. Class '%s' could not be found.";
    private static final String ARR004 = "JSONMapper/ArrayMapper/004: JSON->Java. Unknown primitive array type '%s'.";
    private static final String ARR005 = "JSONMapper/ArrayMapper/005: JSON->Java. Exception while trying to unmarshal an array of type '%s'.";

    public JSONValue toJSON(JSONMapper mapper, Object aObj)
    throws MapperException
    {
    	final Class<?> lClass = aObj.getClass();
        final String lObjClassName = lClass.getName();

    	 String lComponentName = "unknown";
         if(lObjClassName.startsWith("[L"))
             // Array of objects.
        	 lComponentName = lObjClassName.substring(2, lObjClassName.length() - 1);
         else
             // Array of array; Array of primitive types.
        	 lComponentName = lObjClassName.substring(1);

        final JSONArray lElements = new JSONArray();

        if(isPrimitiveArray(lComponentName))
        {
            if("I".equals(lComponentName))
            {
                int[] lArr = (int[]) aObj;
                for(int i = 0; i < lArr.length; i++)
                    lElements.getValue().add(mapper.toJSON(lArr[i]));
            }
            if("C".equals(lComponentName))
            {
                char[] lArr = (char[]) aObj;
                for(int i = 0; i < lArr.length; i++)
                	lElements.getValue().add(mapper.toJSON(lArr[i]));
            }
            else if("Z".equals(lComponentName))
            {
                boolean[] lArr = (boolean[]) aObj;
                for(int i = 0; i < lArr.length; i++)
                	lElements.getValue().add(mapper.toJSON(lArr[i]));
            }
            else if("S".equals(lComponentName))
            {
                short[] lArr = (short[]) aObj;
                for(int i = 0; i < lArr.length; i++)
                	lElements.getValue().add(mapper.toJSON(lArr[i]));
            }
            else if("B".equals(lComponentName))
            {
                byte[] lArr = (byte[]) aObj;
                for(int i = 0; i < lArr.length; i++)
                	lElements.getValue().add(mapper.toJSON(lArr[i]));
            }
            else if("J".equals(lComponentName))
            {
                long[] lArr = (long[]) aObj;
                for(int i = 0; i < lArr.length; i++)
                	lElements.getValue().add(mapper.toJSON(lArr[i]));
            }
            else if("F".equals(lComponentName))
            {
                float[] lArr = (float[]) aObj;
                for(int i = 0; i < lArr.length; i++)
                	lElements.getValue().add(mapper.toJSON(lArr[i]));
            }
            else if("D".equals(lComponentName))
            {
                double[] lArr = (double[]) aObj;
                for(int i = 0; i < lArr.length; i++)
                	lElements.getValue().add(mapper.toJSON(lArr[i]));
            }
        }
        else
        {
            Iterator<Object> lIter = Arrays.asList((Object[]) aObj).iterator();
            while(lIter.hasNext())
            {
                Object lArrEl = lIter.next();
                lElements.getValue().add(mapper.toJSON(lArrEl));
            }
        }
        return lElements;
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

	public Object toJava(JSONMapper mapper, JSONValue aValue, Class<?> aRequestedClass) throws MapperException {

		if(!aValue.isArray()) throw new MapperException(String.format(ARR001, aValue.getClass().getName()));

        // First we fetch all array elements.
        JSONArray lValues = (JSONArray)aValue;

        final String lObjClassName = aRequestedClass.getName();

    	 String lArrClassName = "unknown";
         if(lObjClassName.startsWith("[L"))
             // Array of objects.
        	 lArrClassName = lObjClassName.substring(2, lObjClassName.length() - 1);
         else
             // Array of array; Array of primitive types.
        	 lArrClassName = lObjClassName.substring(1);

        final List<Object> lElements = new LinkedList<Object>();
        for (JSONValue jsonValue : lValues.getValue())
        {
            try {
            	if(isPrimitiveArray(lArrClassName)){
            		Class<?> primitiveClass = null;
            		if("I".equals(lArrClassName)) primitiveClass=Integer.class;
            		else  if("C".equals(lArrClassName))	primitiveClass=Character.class;
                    else if("Z".equals(lArrClassName)) 	primitiveClass=Boolean.class;
                    else if("S".equals(lArrClassName)) 	primitiveClass=Short.class;
                    else if("B".equals(lArrClassName)) 	primitiveClass=Byte.class;
                    else if("J".equals(lArrClassName)) 	primitiveClass=Long.class;
                    else if("F".equals(lArrClassName)) 	primitiveClass=Float.class;
                    else if("D".equals(lArrClassName))	primitiveClass=Double.class;
                    else {
                        throw new  MapperException(String.format(ARR002, lArrClassName));
                    }
            		lElements.add(mapper.toJava(jsonValue,primitiveClass));
            	}else{
            		lElements.add(mapper.toJava(jsonValue,Class.forName(lArrClassName)));
            	}

			}
            catch (ClassNotFoundException e)
            {
				throw new MapperException(String.format(ARR003, lArrClassName), e);
			}
        }
        final int lArrSize = lElements.size();

        if(isPrimitiveArray(lArrClassName))
        {
            if("I".equals(lArrClassName))
            {
                int[] lArr = new int[lArrSize];
                Iterator<Object> lIter = lElements.iterator();
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
                Iterator<Object> lIter = lElements.iterator();
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
                Iterator<Object> lIter = lElements.iterator();
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
                Iterator<Object> lIter = lElements.iterator();
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
                Iterator<Object> lIter = lElements.iterator();
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
                Iterator<Object> lIter = lElements.iterator();
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
                Iterator<Object> lIter = lElements.iterator();
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
                Iterator<Object> lIter = lElements.iterator();
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
                throw new  MapperException(String.format(ARR004, lArrClassName));
            }
        }
        else
        {
            try
            {
                Class<?> lComponentClass = Class.forName(lArrClassName);
                Object lArr = Array.newInstance(lComponentClass, lArrSize);
                Iterator<Object> lIter = lElements.iterator();
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
                throw new  MapperException(String.format(ARR005, lArrClassName), e);
            }
        }
	}
}
