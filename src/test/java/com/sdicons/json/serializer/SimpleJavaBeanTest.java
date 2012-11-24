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

public class SimpleJavaBeanTest
{
    Marshall marshall;

    @Before
    public void setUp()
    throws Exception
    {
        marshall = new JSONMarshall();
   }

    @Test
    public void testNull() throws MarshallException
    {
        MarshallValue lResult = marshall.unmarshall(marshall.marshall(null));
        Assert.assertTrue(MarshallValue.REFERENCE == lResult.getType());
        Assert.assertTrue(lResult.getReference() == null);
    }

    @Test
    public void testJavaBean() throws MarshallException
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

    @Test
    public void testJavaBeanArray() throws MarshallException
    {
        MyBean lTest1 = new MyBean();
        lTest1.setId(100);
        lTest1.setName("SISE Rules!");
        
        MyBean lTest2 = new MyBean();
        lTest2.setId(200);
        lTest2.setName("S.D.I.-Consulting");
        
        MarshallValue lResult = marshall.unmarshall(marshall.marshall(new MyBean[] { lTest1, lTest2 }));
        Assert.assertTrue(MarshallValue.REFERENCE == lResult.getType());
        MyBean[] lArr = (MyBean[]) lResult.getReference();
        
        Assert.assertNotNull(lArr);
        Assert.assertTrue(lArr.length == 2);
        
        Assert.assertEquals(lArr[0].getName(), "SISE Rules!");
        Assert.assertEquals(lArr[0].getId(), 100);
        
        Assert.assertEquals(lArr[1].getName(), "S.D.I.-Consulting");
        Assert.assertEquals(lArr[1].getId(), 200);
    }

    @Test
    public void testJavaBeanArray2() throws MarshallException
    {
        MyBean lTest1 = new MyBean();
        lTest1.setId(100);
        lTest1.setName("SISE Rules!");
        
        MyBean lTest2 = new MyBean();
        lTest2.setId(200);
        lTest2.setName("S.D.I.-Consulting");
        
        MarshallValue lResult = marshall.unmarshall(marshall.marshall(new MyBean[][] { { lTest1 }, { lTest2 } }));
        Assert.assertTrue(MarshallValue.REFERENCE == lResult.getType());
        MyBean[][] lArr = (MyBean[][]) lResult.getReference();
        
        Assert.assertNotNull(lArr);
        Assert.assertTrue(lArr.length == 2);
        
        Assert.assertEquals(lArr[0][0].getName(), "SISE Rules!");
        Assert.assertEquals(lArr[0][0].getId(), 100);
        
        Assert.assertEquals(lArr[1][0].getName(), "S.D.I.-Consulting");
        Assert.assertEquals(lArr[1][0].getId(), 200);
    }
}
