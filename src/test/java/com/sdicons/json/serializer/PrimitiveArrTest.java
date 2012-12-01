/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.serializer;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class PrimitiveArrTest
{

    JSONSerializer serializer;

    @Before
    public void setUp()
    throws Exception
    {
        serializer = new JSONSerializer();
    }

    @Test
    public void testBoolean() throws SerializerException
    {
        SerializerValue lResult = serializer.unmarshal(serializer.marshal(new boolean[] { true, false, true, false }));
        Assert.assertTrue(SerializerValue.REFERENCE == lResult.getType());
        Assert.assertTrue(java.util.Arrays.equals((boolean[])lResult.getReference(), new boolean[]{true, false, true, false}));
    }

    @Test
    public void testByte() throws SerializerException
    {
        SerializerValue lResult = serializer.unmarshal(serializer.marshal(new byte[] { -1, 0, 1, 2 }));
        Assert.assertTrue(SerializerValue.REFERENCE == lResult.getType());
        Assert.assertTrue(java.util.Arrays.equals((byte[])lResult.getReference(), new byte[]{-1, 0, 1, 2}));
    }

    @Test
    public void testShort() throws SerializerException
    {
        SerializerValue lResult = serializer.unmarshal(serializer.marshal(new short[] { -1, 0, 11, 2 }));
        Assert.assertTrue(SerializerValue.REFERENCE == lResult.getType());
        Assert.assertTrue(java.util.Arrays.equals((short[])lResult.getReference(), new short[]{-1, 0, 11, 2}));
    }

    @Test
    public void testChar() throws SerializerException
    {
        SerializerValue lResult = serializer.unmarshal(serializer.marshal(new char[] { 'a', 'b', 'A', 'Z' }));
        Assert.assertTrue(SerializerValue.REFERENCE == lResult.getType());
        Assert.assertTrue(java.util.Arrays.equals((char[])lResult.getReference(), new char[]{'a', 'b', 'A', 'Z'}));
    }

    @Test
    public void testInt() throws SerializerException
    {
        SerializerValue lResult = serializer.unmarshal(serializer.marshal(new int[] { -1003, 0, 1003, 2310 }));
        Assert.assertTrue(SerializerValue.REFERENCE == lResult.getType());
        Assert.assertTrue(java.util.Arrays.equals((int[])lResult.getReference(), new int[]{-1003, 0, 1003, 2310}));
    }

    @Test
    public void testLong() throws SerializerException
    {
        SerializerValue lResult = serializer.unmarshal(serializer.marshal(new long[] { -1003, 0, 1003, 2311 }));
        Assert.assertTrue(SerializerValue.REFERENCE == lResult.getType());
        Assert.assertTrue(java.util.Arrays.equals((long[]) lResult.getReference(), new long[] { -1003, 0, 1003, 2311 }));
    }

    @Test
    public void testFloat() throws SerializerException
    {
        SerializerValue lResult = serializer.unmarshal(serializer.marshal(new float[] { -1.13f, 0.0f, 1.13f, 2310.1f }));
        Assert.assertTrue(SerializerValue.REFERENCE == lResult.getType());
        Assert.assertTrue(java.util.Arrays.equals((float[])lResult.getReference(), new float[]{-1.13f, 0.0f, 1.13f, 2310.1f}));
    }

    @Test
    public void testDouble() throws SerializerException
    {
        SerializerValue lResult = serializer.unmarshal(serializer.marshal(new double[] { -1.14, 0.0, 1.13, 2310.2 }));
        Assert.assertTrue(SerializerValue.REFERENCE == lResult.getType());
        Assert.assertTrue(java.util.Arrays.equals((double[])lResult.getReference(), new double[]{-1.14, 0.0, 1.13, 2310.2}));
    }
}
