/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.serializer;

import junit.framework.*;
import com.sdicons.json.serializer.marshall.Marshall;
import com.sdicons.json.serializer.marshall.MarshallValue;
import com.sdicons.json.serializer.marshall.JSONMarshall;

public class PrimitiveTypesTest
extends TestCase
{

    public PrimitiveTypesTest(String lName)
    {
        super(lName);
    }

    Marshall marshall;

    public void setUp()
    throws Exception
    {
        marshall = new JSONMarshall();
    }

    public void testBoolean1()
    {
        try
        {
            MarshallValue lResult = marshall.unmarshall(marshall.marshall(false));
            Assert.assertTrue(MarshallValue.BOOLEAN == lResult.getType());
            Assert.assertTrue(!lResult.getBoolean());
        }
        catch(Exception e)
        {
             e.printStackTrace(System.out);
            Assert.fail();
        }
    }

    public void testBoolean2()
    {
        try
        {
            MarshallValue lResult = marshall.unmarshall(marshall.marshall(true));
            Assert.assertTrue(MarshallValue.BOOLEAN == lResult.getType());
            Assert.assertTrue(lResult.getBoolean());
        }
        catch(Exception e)
        {
             e.printStackTrace(System.out);
            Assert.fail();
        }
    }

    public void testByte()
    {
        try
        {
            MarshallValue lResult = marshall.unmarshall(marshall.marshall((byte) 7));
            Assert.assertTrue(MarshallValue.BYTE == lResult.getType());
            Assert.assertTrue(lResult.getByte() == 7);
        }
        catch(Exception e)
        {
             e.printStackTrace(System.out);
            Assert.fail();
        }
    }

    public void testShort()
    {
        try
        {
            MarshallValue lResult = marshall.unmarshall(marshall.marshall((short) -13));
            Assert.assertTrue(MarshallValue.SHORT == lResult.getType());
            Assert.assertTrue(lResult.getShort() == -13);
        }
        catch(Exception e)
        {
             e.printStackTrace(System.out);
            Assert.fail();
        }
    }

    public void testChar()
    {
        try
        {
            MarshallValue lResult = marshall.unmarshall(marshall.marshall('q'));
            Assert.assertTrue(MarshallValue.CHAR == lResult.getType());
            Assert.assertTrue(lResult.getChar() == 'q');
        }
        catch(Exception e)
        {
             e.printStackTrace(System.out);
            Assert.fail();
        }
    }

    public void testInteger()
    {
        try
        {
            MarshallValue lResult = marshall.unmarshall(marshall.marshall(177));
            Assert.assertTrue(MarshallValue.INT == lResult.getType());
            Assert.assertTrue(lResult.getInt() == 177);
        }
        catch(Exception e)
        {
             e.printStackTrace(System.out);
            Assert.fail();
        }
    }

    public void testLong()
    {
        try
        {
            MarshallValue lResult = marshall.unmarshall(marshall.marshall((long) 117));
            Assert.assertTrue(MarshallValue.LONG == lResult.getType());
            Assert.assertTrue(lResult.getLong() == 117);
        }
        catch(Exception e)
        {
             e.printStackTrace(System.out);
            Assert.fail();
        }
    }

    public void testFloat()
    {
        try
        {
            MarshallValue lResult = marshall.unmarshall(marshall.marshall((float) 3.14));
            Assert.assertTrue(MarshallValue.FLOAT == lResult.getType());
            Assert.assertTrue(lResult.getFloat() == (float) 3.14);
        }
        catch(Exception e)
        {
             e.printStackTrace(System.out);
            Assert.fail();
        }
    }

    public void testDouble()
    {
        try
        {
            MarshallValue lResult = marshall.unmarshall(marshall.marshall(3.141567));
            Assert.assertTrue(MarshallValue.DOUBLE == lResult.getType());
            Assert.assertTrue(lResult.getDouble() == 3.141567);
        }
        catch(Exception e)
        {
             e.printStackTrace(System.out);
            Assert.fail();
        }
    }
}
