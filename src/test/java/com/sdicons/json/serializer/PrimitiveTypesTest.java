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
    JSONSerializer marshall;

    @Before
    public void setUp()
    throws Exception
    {
        marshall = new JSONSerializer();
    }

    @Test
    public void testBoolean1() throws JSONSerializeException
    {
        JSONSerializeValue lResult = marshall.unmarshal(marshall.marshal(false));
        Assert.assertTrue(JSONSerializeValue.BOOLEAN == lResult.getType());
        Assert.assertTrue(!lResult.getBoolean());
    }

    @Test
    public void testBoolean2() throws JSONSerializeException
    {
        JSONSerializeValue lResult = marshall.unmarshal(marshall.marshal(true));
        Assert.assertTrue(JSONSerializeValue.BOOLEAN == lResult.getType());
        Assert.assertTrue(lResult.getBoolean());
    }

    @Test
    public void testByte() throws JSONSerializeException
    {
        JSONSerializeValue lResult = marshall.unmarshal(marshall.marshal((byte) 7));
        Assert.assertTrue(JSONSerializeValue.BYTE == lResult.getType());
        Assert.assertTrue(lResult.getByte() == 7);
    }

    @Test
    public void testShort() throws JSONSerializeException
    {
        JSONSerializeValue lResult = marshall.unmarshal(marshall.marshal((short) -13));
        Assert.assertTrue(JSONSerializeValue.SHORT == lResult.getType());
        Assert.assertTrue(lResult.getShort() == -13);
    }

    @Test
    public void testChar() throws JSONSerializeException
    {
        JSONSerializeValue lResult = marshall.unmarshal(marshall.marshal('q'));
        Assert.assertTrue(JSONSerializeValue.CHAR == lResult.getType());
        Assert.assertTrue(lResult.getChar() == 'q');
    }

    @Test
    public void testInteger() throws JSONSerializeException
    {
        JSONSerializeValue lResult = marshall.unmarshal(marshall.marshal(177));
        Assert.assertTrue(JSONSerializeValue.INT == lResult.getType());
        Assert.assertTrue(lResult.getInt() == 177);
    }

    @Test
    public void testLong() throws JSONSerializeException
    {
        JSONSerializeValue lResult = marshall.unmarshal(marshall.marshal((long) 117));
        Assert.assertTrue(JSONSerializeValue.LONG == lResult.getType());
        Assert.assertTrue(lResult.getLong() == 117);
    }

    @Test
    public void testFloat() throws JSONSerializeException
    {
        JSONSerializeValue lResult = marshall.unmarshal(marshall.marshal((float) 3.14));
        Assert.assertTrue(JSONSerializeValue.FLOAT == lResult.getType());
        Assert.assertTrue(lResult.getFloat() == (float) 3.14);
    }

    @Test
    public void testDouble() throws JSONSerializeException
    {
        JSONSerializeValue lResult = marshall.unmarshal(marshall.marshal(3.141567));
        Assert.assertTrue(JSONSerializeValue.DOUBLE == lResult.getType());
        Assert.assertTrue(lResult.getDouble() == 3.141567);
    }
}
