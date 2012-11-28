/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.mapper.helper.impl;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
    private static final String ARR001 = "JSONMapper/ArrayMapper/001: JSON->Java. Don't know how to map JSON class'%s' to a Java array.";
    private static final String ARR003 = "JSONMapper/ArrayMapper/003: JSON->Java. Class '%s' could not be found.";
    private static final String ARR004 = "JSONMapper/ArrayMapper/004: JSON->Java. Unknown primitive array type '%s'.";

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

        final JSONArray jsonArray = new JSONArray();

        if (isPrimitiveArray(lComponentName)) {
            char arrType = lComponentName.charAt(0);
            switch (arrType) {
                case 'I': {
                    int[] lArr = (int[]) aObj;
                    for (int i = 0; i < lArr.length; i++)
                        jsonArray.getValue().add(mapper.toJSON(lArr[i]));
                    break;
                }
                case 'C': {
                    char[] lArr = (char[]) aObj;
                    for (int i = 0; i < lArr.length; i++)
                        jsonArray.getValue().add(mapper.toJSON(lArr[i]));
                    break;
                }
                case 'Z': {
                    boolean[] lArr = (boolean[]) aObj;
                    for (int i = 0; i < lArr.length; i++)
                        jsonArray.getValue().add(mapper.toJSON(lArr[i]));
                    break;
                }
                case 'S': {
                    short[] lArr = (short[]) aObj;
                    for (int i = 0; i < lArr.length; i++)
                        jsonArray.getValue().add(mapper.toJSON(lArr[i]));
                    break;
                }
                case 'B': {
                    byte[] lArr = (byte[]) aObj;
                    for (int i = 0; i < lArr.length; i++)
                        jsonArray.getValue().add(mapper.toJSON(lArr[i]));
                    break;
                }
                case 'J': {
                    long[] lArr = (long[]) aObj;
                    for (int i = 0; i < lArr.length; i++)
                        jsonArray.getValue().add(mapper.toJSON(lArr[i]));
                    break;
                }
                case 'F': {
                    float[] lArr = (float[]) aObj;
                    for (int i = 0; i < lArr.length; i++)
                        jsonArray.getValue().add(mapper.toJSON(lArr[i]));
                    break;
                }
                case 'D': {
                    double[] lArr = (double[]) aObj;
                    for (int i = 0; i < lArr.length; i++)
                        jsonArray.getValue().add(mapper.toJSON(lArr[i]));
                    break;
                }
            }
        }
        else {
            for(Object el : (Object[]) aObj) {
                jsonArray.getValue().add(mapper.toJSON(el));
            }
        }
        return jsonArray;
    }

    public Class<?> getHelpedClass()
    {
        // We can afford to return null, the ArrayMapper is not in the repository, the JSONMapper uses this
        // class directly. Since this mapper is not retrieved from the repository, we don't have to return a class name.
        return null;
    }

    private static final Set<String> pimitiveIndicators = new HashSet<String>(Arrays.asList("I", "Z", "S", "B", "J", "F", "D", "C"));
    private boolean isPrimitiveArray(String aClassName) {
        return pimitiveIndicators.contains(aClassName);
    }

	public Object toJava(JSONMapper mapper, JSONValue aValue, Class<?> aRequestedClass) throws MapperException {

        if (!aValue.isArray()) throw new MapperException(String.format(ARR001, aValue.getClass().getName()));

        // First we fetch all array elements.
        JSONArray jsonArray = (JSONArray) aValue;

        final String lObjClassName = aRequestedClass.getName();

        String lArrClassName = "unknown";
        if (lObjClassName.startsWith("[L"))
            // Array of objects.
            lArrClassName = lObjClassName.substring(2, lObjClassName.length() - 1);
        else
            // Array of array; Array of primitive types.
            lArrClassName = lObjClassName.substring(1);


        // PART 1. First we convert all the values in the JSON array to the target type.
        // The resulting values will be collected in the lElements list.
        ////////////////////////////////////////////////////////////////////////////////

        Class<?> primitiveClass = null;
        Class<?> nonPrimitveClass = null;
        final List<Object> objArray = new ArrayList<Object>(jsonArray.size());

        if (isPrimitiveArray(lArrClassName)) {
            char arrType = lArrClassName.charAt(0);
            switch (arrType) {
                case 'I':
                    primitiveClass = Integer.class;
                    break;
                case 'C':
                    primitiveClass = Character.class;
                    break;
                case 'Z':
                    primitiveClass = Boolean.class;
                    break;
                case 'S':
                    primitiveClass = Short.class;
                    break;
                case 'B':
                    primitiveClass = Byte.class;
                    break;
                case 'J':
                    primitiveClass = Long.class;
                    break;
                case 'F':
                    primitiveClass = Float.class;
                    break;
                case 'D':
                    primitiveClass = Double.class;
                    break;
            }

            // Fill the primitive array.
            //
            for (JSONValue jsonValue : jsonArray.getValue())
                objArray.add(mapper.toJava(jsonValue, primitiveClass));
        }
        else {
            try {
                nonPrimitveClass =  Class.forName(lArrClassName);
            }
            catch (ClassNotFoundException e) {
                throw new MapperException(String.format(ARR003, lArrClassName), e);
            }

            // Fill the non-primitive array
            //
            for (JSONValue jsonValue : jsonArray.getValue())
                 objArray.add(mapper.toJava(jsonValue, nonPrimitveClass));
        }

        // PART 2. Now we will convert the object values in lElements into
        // the real Java arrays.
        ///////////////////////////////////////////////////////////////////

        final int lArrSize = objArray.size();
        if(primitiveClass != null)
        {
            char arrType = lArrClassName.charAt(0);
            switch (arrType) {
                case 'I': {
                    int[] lArr = new int[lArrSize];
                    Iterator<Object> lIter = objArray.iterator();
                    int i = 0;
                    while (lIter.hasNext()) {
                        lArr[i] = ((Integer) lIter.next()).intValue();
                        i++;
                    }
                    return lArr;
                }
                case 'C': {
                    char[] lArr = new char[lArrSize];
                    Iterator<Object> lIter = objArray.iterator();
                    int i = 0;
                    while (lIter.hasNext()) {
                        lArr[i] = ((Character) lIter.next()).charValue();
                        i++;
                    }
                    return lArr;
                }
                case 'Z': {
                    boolean[] lArr = new boolean[lArrSize];
                    Iterator<Object> lIter = objArray.iterator();
                    int i = 0;
                    while (lIter.hasNext()) {
                        lArr[i] = ((Boolean) lIter.next()).booleanValue();
                        i++;
                    }
                    return lArr;
                }
                case 'S': {
                    short[] lArr = new short[lArrSize];
                    Iterator<Object> lIter = objArray.iterator();
                    int i = 0;
                    while (lIter.hasNext()) {
                        lArr[i] = ((Short) lIter.next()).shortValue();
                        i++;
                    }
                    return lArr;
                }
                case 'B': {
                    byte[] lArr = new byte[lArrSize];
                    Iterator<Object> lIter = objArray.iterator();
                    int i = 0;
                    while (lIter.hasNext()) {
                        lArr[i] = ((Byte) lIter.next()).byteValue();
                        i++;
                    }
                    return lArr;
                }
                case 'J': {
                    long[] lArr = new long[lArrSize];
                    Iterator<Object> lIter = objArray.iterator();
                    int i = 0;
                    while (lIter.hasNext()) {
                        lArr[i] = ((Long) lIter.next()).longValue();
                        i++;
                    }
                    return lArr;
                }
                case 'F': {
                    float[] lArr = new float[lArrSize];
                    Iterator<Object> lIter = objArray.iterator();
                    int i = 0;
                    while (lIter.hasNext()) {
                        lArr[i] = ((Float) lIter.next()).floatValue();
                        i++;
                    }
                    return lArr;
                }
                case 'D': {
                    double[] lArr = new double[lArrSize];
                    Iterator<Object> lIter = objArray.iterator();
                    int i = 0;
                    while (lIter.hasNext()) {
                        lArr[i] = ((Double) lIter.next()).doubleValue();
                        i++;
                    }
                    return lArr;
                }
                default:
                    throw new MapperException(String.format(ARR004, lArrClassName));
            }
        }
        else {
            Object resultArray = Array.newInstance(nonPrimitveClass, lArrSize);
            Iterator<Object> lIter = objArray.iterator();
            int i = 0;
            while (lIter.hasNext()) {
                Array.set(resultArray, i, lIter.next());
                i++;
            }
            return resultArray;
        }
	}
}
