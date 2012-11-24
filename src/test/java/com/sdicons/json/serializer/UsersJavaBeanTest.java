/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.serializer;

import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.sdicons.json.helper.JSONConstruct;
import com.sdicons.json.helper.JSONSerialize;
import com.sdicons.json.model.JSONObject;
import com.sdicons.json.serializer.marshall.JSONMarshall;
import com.sdicons.json.serializer.marshall.Marshall;
import com.sdicons.json.serializer.marshall.MarshallException;
import com.sdicons.json.serializer.marshall.MarshallValue;

public class UsersJavaBeanTest
{
    public static class MyDate
    {
        private Date theDate;
        private String theTimeZone;

        @JSONConstruct
        public MyDate(long aTime, String aTimeZone)
        {
            theDate = new Date(aTime);
            theTimeZone = aTimeZone;
        }

        @JSONSerialize
        public Object[] getTime()
        {
            return new Object[]{theDate.getTime(), theTimeZone};
        }
    }

    public static class MyPojo
    {
        private String firstName;
        private String lastName;

        public void setNames(String aFirst, String aLast)
        {
            firstName = aFirst;
            lastName = aLast;
        }

        public String toString()
        {
            return "MyPojo{" +
                    "firstName='" + firstName + '\'' +
                    ", lastName='" + lastName + '\'' +
                    '}';
        }
    }

    public static class Transportable
    {
        private String param1;
        private String param2;
        private Integer objectID;
        private Integer parentID;
        private Integer subObjectID;
        private Integer transportCode;
        private Integer eventType;

        public Integer getEventType()
        {
            return eventType;
        }

        public void setEventType(Integer eventType)
        {
            this.eventType = eventType;
        }

        public Integer getObjectID()
        {
            return objectID;
        }

        public void setObjectID(Integer objectID)
        {
            this.objectID = objectID;
        }

        public String getParam1()
        {
            return param1;
        }

        public void setParam1(String param1)
        {
            this.param1 = param1;
        }

        public String getParam2()
        {
            return param2;
        }

        public void setParam2(String param2)
        {
            this.param2 = param2;
        }

        public Integer getParentID()
        {
            return parentID;
        }

        public void setParentID(Integer parentID)
        {
            this.parentID = parentID;
        }

        public Integer getSubObjectID()
        {
            return subObjectID;
        }

        public void setSubObjectID(Integer subObjectID)
        {
            this.subObjectID = subObjectID;
        }

        public Integer getTransportCode()
        {
            return transportCode;
        }

        public void setTransportCode(Integer transportCode)
        {
            this.transportCode = transportCode;
        }
    }

    Marshall marshall;

    @Before
    public void setUp()
    throws Exception
    {
        marshall = new JSONMarshall();
    }

    @Test
    public void testIt() throws MarshallException
    {
        Transportable lEvil = new Transportable();
        Integer lID = 13;
        lEvil.setEventType(lID);
        lEvil.setParentID(lID);
        lEvil.setSubObjectID(lID);
        lEvil.setObjectID(lID);
        String lStr = "Test";
        lEvil.setParam1(lStr);
        lEvil.setParam2(lStr);
        
        JSONObject lObj = marshall.marshall(lEvil);
        System.out.println(lObj.render(true));
        MarshallValue lResult = marshall.unmarshall(lObj);
        Assert.assertTrue(MarshallValue.REFERENCE == lResult.getType());
        Transportable lLitmus = (Transportable) lResult.getReference();
        
        // Test if the contents are intact.
        Assert.assertNotNull(lLitmus);
        Assert.assertEquals(lLitmus.getEventType(), lID);
        Assert.assertTrue(lLitmus.getEventType() == lLitmus.getParentID());
        Assert.assertTrue(lLitmus.getEventType() == lLitmus.getSubObjectID());
        Assert.assertTrue(lLitmus.getEventType() == lLitmus.getObjectID());
        Assert.assertEquals(lLitmus.getParam1(), lStr);
        Assert.assertTrue(lLitmus.getParam1() == lLitmus.getParam2());
    }

    @Test
    public void testAnnotatedSerializer() throws MarshallException
    {
        // Map fields, not properties.
        ((JSONMarshall) marshall).usePojoAccess();
        MyDate lMyDate = new MyDate(new Date().getTime(), "CEST");
        JSONObject lObj = marshall.marshall(lMyDate);
        System.out.println(lObj.render(true));
        
        Object javaObj = marshall.unmarshall(lObj);
    }

    @Test
    public void testDirectHelper() throws MarshallException
    {
        // Map fields, not properties.
        ((JSONMarshall) marshall).usePojoAccess();
        MyPojo lPojo = new MyPojo();
        lPojo.setNames("Homer", "Simpson");
        JSONObject lObj = marshall.marshall(lPojo);
        System.out.println(lObj.render(true));
        
        Object javaObj = marshall.unmarshall(lObj);
        System.out.println(javaObj.toString());
    }
}
