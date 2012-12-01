/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.serializer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class PrimitiveTypesTest
{
    JSONSerializer serializer;

    @Before
    public void setUp()
    throws Exception
    {
        serializer = new JSONSerializer();
    }

    @Test
    public void testBoolean1() throws SerializerException
    {
        SerializerValue lResult = serializer.unmarshal(serializer.marshal(false));
        Assert.assertTrue(SerializerValue.BOOLEAN == lResult.getType());
        Assert.assertTrue(!lResult.getBoolean());
    }

    @Test
    public void testBoolean2() throws SerializerException
    {
        SerializerValue lResult = serializer.unmarshal(serializer.marshal(true));
        Assert.assertTrue(SerializerValue.BOOLEAN == lResult.getType());
        Assert.assertTrue(lResult.getBoolean());
    }

    @Test
    public void testByte() throws SerializerException
    {
        SerializerValue lResult = serializer.unmarshal(serializer.marshal((byte) 7));
        Assert.assertTrue(SerializerValue.BYTE == lResult.getType());
        Assert.assertTrue(lResult.getByte() == 7);
    }

    @Test
    public void testShort() throws SerializerException
    {
        SerializerValue lResult = serializer.unmarshal(serializer.marshal((short) -13));
        Assert.assertTrue(SerializerValue.SHORT == lResult.getType());
        Assert.assertTrue(lResult.getShort() == -13);
    }

    @Test
    public void testChar() throws SerializerException
    {
        SerializerValue lResult = serializer.unmarshal(serializer.marshal('q'));
        Assert.assertTrue(SerializerValue.CHAR == lResult.getType());
        Assert.assertTrue(lResult.getChar() == 'q');
    }

    @Test
    public void testInteger() throws SerializerException
    {
        SerializerValue lResult = serializer.unmarshal(serializer.marshal(177));
        Assert.assertTrue(SerializerValue.INT == lResult.getType());
        Assert.assertTrue(lResult.getInt() == 177);
    }

    @Test
    public void testLong() throws SerializerException
    {
        SerializerValue lResult = serializer.unmarshal(serializer.marshal((long) 117));
        Assert.assertTrue(SerializerValue.LONG == lResult.getType());
        Assert.assertTrue(lResult.getLong() == 117);
    }

    @Test
    public void testFloat() throws SerializerException
    {
        SerializerValue lResult = serializer.unmarshal(serializer.marshal((float) 3.14));
        Assert.assertTrue(SerializerValue.FLOAT == lResult.getType());
        Assert.assertTrue(lResult.getFloat() == (float) 3.14);
    }

    @Test
    public void testDouble() throws SerializerException
    {
        SerializerValue lResult = serializer.unmarshal(serializer.marshal(3.141567));
        Assert.assertTrue(SerializerValue.DOUBLE == lResult.getType());
        Assert.assertTrue(lResult.getDouble() == 3.141567);
    }
}
