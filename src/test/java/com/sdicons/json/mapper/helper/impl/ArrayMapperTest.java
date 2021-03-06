/*******************************************************************************
 * Copyright (c) 2006-2013 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.mapper.helper.impl;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.sdicons.json.mapper.JSONMapper;
import com.sdicons.json.mapper.MapperException;
import com.sdicons.json.mapper.helper.ArrayMapper;
import com.sdicons.json.mapper.helper.BooleanMapper;
import com.sdicons.json.mapper.helper.ByteMapper;
import com.sdicons.json.mapper.helper.CharacterMapper;
import com.sdicons.json.mapper.helper.ClassMapper;
import com.sdicons.json.mapper.helper.DoubleMapper;
import com.sdicons.json.mapper.helper.FloatMapper;
import com.sdicons.json.mapper.helper.IntegerMapper;
import com.sdicons.json.mapper.helper.LongMapper;
import com.sdicons.json.mapper.helper.ObjectMapperFields;
import com.sdicons.json.mapper.helper.ShortMapper;
import com.sdicons.json.model.JSONArray;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.model.JSONValue;

public class ArrayMapperTest {

    private ArrayMapper helper;
    private JSONMapper mapper;

    @Before
    public void init() {
        helper = new ArrayMapper();
        // This constructor creates a mapper without any helpers.
        // We will have to add the helpers ourselves.
        // In this way we have control over the objects under test.
        mapper = new JSONMapper(new ClassMapper[]{});
    }

    @Test
    public void intArray() throws MapperException {
        mapper.addHelper(new IntegerMapper());
        int[] arr = new int[]{-9, -8, -7, -6, -5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        JSONValue json = helper.toJSON(mapper,  arr);
        Assert.assertThat(json, is(instanceOf(JSONArray.class)));
        //
        Object back = helper.toJava(mapper, json, int[].class);
        Assert.assertArrayEquals(arr, (int[])back);
    }

    @Test
    public void charArray() throws MapperException {
        mapper.addHelper(new CharacterMapper());
        char[] arr = new char[]{'a', 'b', 'c'};
        JSONValue json = helper.toJSON(mapper,  arr);
        Assert.assertThat(json, is(instanceOf(JSONArray.class)));
        //
        Object back = helper.toJava(mapper, json, char[].class);
        Assert.assertArrayEquals(arr, (char[])back);
    }

    @Test
    public void boolArray() throws MapperException {
        mapper.addHelper(new BooleanMapper());
        boolean[] arr = new boolean[]{true, false, false, true, true};
        JSONValue json = helper.toJSON(mapper,  arr);
        Assert.assertThat(json, is(instanceOf(JSONArray.class)));
        //
        Object back = helper.toJava(mapper, json, boolean[].class);
        Assert.assertTrue(Arrays.equals(arr, (boolean[]) back));
    }

    @Test
    public void byteArray() throws MapperException {
        mapper.addHelper(new ByteMapper());
        byte[] arr = new byte[]{-9, -8, -7, -6, -5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        JSONValue json = helper.toJSON(mapper,  arr);
        Assert.assertThat(json, is(instanceOf(JSONArray.class)));
        //
        Object back = helper.toJava(mapper, json, byte[].class);
        Assert.assertArrayEquals(arr, (byte[])back);
    }

    @Test
    public void shortArray() throws MapperException {
        mapper.addHelper(new ShortMapper());
        short[] arr = new short[]{-9, -8, -7, -6, -5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        JSONValue json = helper.toJSON(mapper,  arr);
        Assert.assertThat(json, is(instanceOf(JSONArray.class)));
        //
        Object back = helper.toJava(mapper, json, short[].class);
        Assert.assertArrayEquals(arr, (short[])back);
    }

    @Test
    public void longArray() throws MapperException {
        mapper.addHelper(new LongMapper());
        long[] arr = new long[]{-9, -8, -7, -6, -5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        JSONValue json = helper.toJSON(mapper,  arr);
        Assert.assertThat(json, is(instanceOf(JSONArray.class)));
        //
        Object back = helper.toJava(mapper, json, long[].class);
        Assert.assertArrayEquals(arr, (long[])back);
    }

    @Test
    public void floatArray() throws MapperException {
        mapper.addHelper(new FloatMapper());
        float[] arr = new float[]{-9, -8, -7, -6, -5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        JSONValue json = helper.toJSON(mapper,  arr);
        Assert.assertThat(json, is(instanceOf(JSONArray.class)));
        //
        Object back = helper.toJava(mapper, json, float[].class);
        Assert.assertTrue(Arrays.equals(arr, (float[]) back));
    }

    @Test
    public void doubleArray() throws MapperException {
        mapper.addHelper(new DoubleMapper());
        double[] arr = new double[]{-9, -8, -7, -6, -5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        JSONValue json = helper.toJSON(mapper,  arr);
        Assert.assertThat(json, is(instanceOf(JSONArray.class)));
        //
        Object back = helper.toJava(mapper, json, double[].class);
        Assert.assertTrue(Arrays.equals(arr, (double[]) back));
    }

    @Test
    public void intObjArray() throws MapperException {
        mapper.addHelper(new IntegerMapper());
        Integer[] arr = new Integer[]{-9, -8, -7, -6, -5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        JSONValue json = helper.toJSON(mapper,  arr);
        Assert.assertThat(json, is(instanceOf(JSONArray.class)));
        //
        Object back = helper.toJava(mapper, json, Integer[].class);
        Assert.assertTrue(Arrays.equals(arr, (Integer[]) back));
    }

    public static class A {};

    @Test
    public void objArray() throws MapperException {
        mapper.addHelper(new ObjectMapperFields());
        A[] arr = new A[]{new A(), new A(), new A(), new A(), new A()};
        JSONValue json = helper.toJSON(mapper,  arr);
        Assert.assertThat(json, is(instanceOf(JSONArray.class)));
        //
        Object back = helper.toJava(mapper, json, A[].class);
        A[] backa = (A[]) back;
        Assert.assertTrue(backa.length == arr.length);
        for(int i = 0; i < backa.length; i++)
            Assert.assertNotNull(backa[i]);
    }

    @Test(expected = MapperException.class)
    public void badInput() throws MapperException {
        mapper.addHelper(new IntegerMapper());
        helper.toJSON(mapper, new A());
    }

    @Test(expected = MapperException.class)
    public void badInput2() throws MapperException {
        mapper.addHelper(new IntegerMapper());
        helper.toJava(mapper, new JSONString("ai"), int[].class);
    }

    @Test(expected = MapperException.class)
    public void badInput3() throws MapperException {
        mapper.addHelper(new IntegerMapper());
        helper.toJava(mapper, new JSONArray(), A.class);
    }
}
