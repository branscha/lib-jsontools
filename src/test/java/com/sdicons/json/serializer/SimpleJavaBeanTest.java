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

public class SimpleJavaBeanTest
extends TestCase
{

    public SimpleJavaBeanTest(String lName)
    {
        super(lName);
    }

    Marshall marshall;

    public void setUp()
    throws Exception
    {
        marshall = new JSONMarshall();
   }

    public void testNull()
    {
        try
        {
            MarshallValue lResult = marshall.unmarshall(marshall.marshall(null));
            Assert.assertTrue(MarshallValue.REFERENCE == lResult.getType());
            Assert.assertTrue(lResult.getReference() == null);
        }
        catch(Exception e)
        {
            Assert.fail();
        }
    }

    public void testJavaBean()
    {
        try
        {
            MyBean lTest1 = new MyBean();
            lTest1.setId(100);
            lTest1.setName("SISE Rules!");
            lTest1.setInt1(new Integer(0));
            lTest1.setInt2(new Integer(0));

            System.out.println(marshall.marshall(lTest1).render(true));
            MarshallValue lResult = marshall.unmarshall(marshall.marshall(lTest1));
            Assert.assertTrue(MarshallValue.REFERENCE == lResult.getType());
            MyBean lTest2 = (MyBean) lResult.getReference();

            // Test if the contents are intact.
            Assert.assertNotNull(lTest2);
            Assert.assertEquals(lTest2.getName(), "SISE Rules!");
            Assert.assertEquals(lTest2.getId(), 100);

            // Different physical objects should remain different, even if they
            // are equal.
            Assert.assertTrue(!(lTest2.getInt1() == lTest2.getInt2()));
        }
        catch(Exception e)
        {
             e.printStackTrace(System.out);
            Assert.fail();
        }
    }

    public void testJavaBeanArray()
    {
        try
        {
            MyBean lTest1 = new MyBean();
            lTest1.setId(100);
            lTest1.setName("SISE Rules!");

            MyBean lTest2 = new MyBean();
            lTest2.setId(200);
            lTest2.setName("S.D.I.-Consulting");

            MarshallValue lResult = marshall.unmarshall(marshall.marshall(new MyBean[]{lTest1, lTest2}));
            Assert.assertTrue(MarshallValue.REFERENCE == lResult.getType());
            MyBean[] lArr = (MyBean[]) lResult.getReference();

            Assert.assertNotNull(lArr);
            Assert.assertTrue(lArr.length == 2);

            Assert.assertEquals(lArr[0].getName(), "SISE Rules!");
            Assert.assertEquals(lArr[0].getId(), 100);

            Assert.assertEquals(lArr[1].getName(), "S.D.I.-Consulting");
            Assert.assertEquals(lArr[1].getId(), 200);
        }
        catch(Exception e)
        {
             e.printStackTrace(System.out);
            Assert.fail();
        }
    }

    public void testJavaBeanArray2()
    {
        try
        {
            MyBean lTest1 = new MyBean();
            lTest1.setId(100);
            lTest1.setName("SISE Rules!");

            MyBean lTest2 = new MyBean();
            lTest2.setId(200);
            lTest2.setName("S.D.I.-Consulting");

            MarshallValue lResult = marshall.unmarshall(marshall.marshall(new MyBean[][] {{lTest1}, {lTest2}}));
            Assert.assertTrue(MarshallValue.REFERENCE == lResult.getType());
            MyBean[][] lArr = (MyBean[][]) lResult.getReference();

            Assert.assertNotNull(lArr);
            Assert.assertTrue(lArr.length == 2);

            Assert.assertEquals(lArr[0][0].getName(), "SISE Rules!");
            Assert.assertEquals(lArr[0][0].getId(), 100);

            Assert.assertEquals(lArr[1][0].getName(), "S.D.I.-Consulting");
            Assert.assertEquals(lArr[1][0].getId(), 200);
        }
        catch(Exception e)
        {
             e.printStackTrace(System.out);
            Assert.fail();
        }
    }
}
