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

public class ComplexJavaBeanTest
extends TestCase
{

    public ComplexJavaBeanTest(String lName)
    {
        super(lName);
    }

    Marshall marshall;

    public void setUp()
    throws Exception
    {
        marshall = new JSONMarshall();
    }

    public void testSelfReference()
    {
        try
        {
            MyBean lTest1 = new MyBean();
            lTest1.setId(113);
            lTest1.setName("SELF-POINTING");
            lTest1.setPtr(lTest1);

            MarshallValue lResult = marshall.unmarshall(marshall.marshall(lTest1));
            Assert.assertTrue(MarshallValue.REFERENCE == lResult.getType());
            MyBean lTest2 = (MyBean) lResult.getReference();
            Assert.assertNotNull(lTest2);
            Assert.assertEquals(lTest2.getName(), "SELF-POINTING");
            Assert.assertEquals(lTest2.getId(), 113);
            Assert.assertTrue(lTest2 == lTest2.getPtr());
        }
        catch(Exception e)
        {
            Assert.fail();
        }
    }

    public void testCycle()
    {
        try
        {
            MyBean[] lTest1 = new MyBean[10];
            for(int i = 0; i < 10; i++)
            {
                lTest1[i] = new MyBean();
                lTest1[i].setName("CYCLE-" + i);
                lTest1[i].setId(i);
            }

            lTest1[0].setPtr(lTest1[9]);
            for(int i = 1; i < 10; i++)
                lTest1[i].setPtr(lTest1[i-1]);

            MarshallValue lResult = marshall.unmarshall(marshall.marshall(lTest1));
            Assert.assertTrue(MarshallValue.REFERENCE == lResult.getType());
            MyBean[] lTest2 = (MyBean[]) lResult.getReference();

            Assert.assertTrue(lTest2[0].getPtr() == lTest2[9]);
            for(int i = 1; i < 10; i++)
                Assert.assertTrue(lTest2[i].getPtr() == lTest2[i-1]);
        }
        catch(Exception e)
        {
            Assert.fail();
        }
    }
}
