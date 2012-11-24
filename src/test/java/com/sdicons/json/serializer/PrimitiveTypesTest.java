/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.serializer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.sdicons.json.serializer.marshall.JSONMarshall;
import com.sdicons.json.serializer.marshall.Marshall;
import com.sdicons.json.serializer.marshall.MarshallException;
import com.sdicons.json.serializer.marshall.MarshallValue;

public class PrimitiveTypesTest
{
    Marshall marshall;

    @Before
    public void setUp()
    throws Exception
    {
        marshall = new JSONMarshall();
    }

    @Test
    public void testBoolean1() throws MarshallException
    {
        MarshallValue lResult = marshall.unmarshall(marshall.marshall(false));
        Assert.assertTrue(MarshallValue.BOOLEAN == lResult.getType());
        Assert.assertTrue(!lResult.getBoolean());
    }

    @Test
    public void testBoolean2() throws MarshallException
    {
        MarshallValue lResult = marshall.unmarshall(marshall.marshall(true));
        Assert.assertTrue(MarshallValue.BOOLEAN == lResult.getType());
        Assert.assertTrue(lResult.getBoolean());
    }

    @Test
    public void testByte() throws MarshallException
    {
        MarshallValue lResult = marshall.unmarshall(marshall.marshall((byte) 7));
        Assert.assertTrue(MarshallValue.BYTE == lResult.getType());
        Assert.assertTrue(lResult.getByte() == 7);
    }

    @Test
    public void testShort() throws MarshallException
    {
        MarshallValue lResult = marshall.unmarshall(marshall.marshall((short) -13));
        Assert.assertTrue(MarshallValue.SHORT == lResult.getType());
        Assert.assertTrue(lResult.getShort() == -13);
    }

    @Test
    public void testChar() throws MarshallException
    {
        MarshallValue lResult = marshall.unmarshall(marshall.marshall('q'));
        Assert.assertTrue(MarshallValue.CHAR == lResult.getType());
        Assert.assertTrue(lResult.getChar() == 'q');
    }

    @Test
    public void testInteger() throws MarshallException
    {
        MarshallValue lResult = marshall.unmarshall(marshall.marshall(177));
        Assert.assertTrue(MarshallValue.INT == lResult.getType());
        Assert.assertTrue(lResult.getInt() == 177);
    }

    @Test
    public void testLong() throws MarshallException
    {
        MarshallValue lResult = marshall.unmarshall(marshall.marshall((long) 117));
        Assert.assertTrue(MarshallValue.LONG == lResult.getType());
        Assert.assertTrue(lResult.getLong() == 117);
    }

    @Test
    public void testFloat() throws MarshallException
    {
        MarshallValue lResult = marshall.unmarshall(marshall.marshall((float) 3.14));
        Assert.assertTrue(MarshallValue.FLOAT == lResult.getType());
        Assert.assertTrue(lResult.getFloat() == (float) 3.14);
    }

    @Test
    public void testDouble() throws MarshallException
    {
        MarshallValue lResult = marshall.unmarshall(marshall.marshall(3.141567));
        Assert.assertTrue(MarshallValue.DOUBLE == lResult.getType());
        Assert.assertTrue(lResult.getDouble() == 3.141567);
    }
}
