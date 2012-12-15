/*******************************************************************************
 * Copyright (c) 2006-2013 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.serializer;

import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.sdicons.json.annotations.JSONConstructor;
import com.sdicons.json.annotations.JSONConstructorArgs;
import com.sdicons.json.model.JSONObject;

public class UsersJavaBeanTest
{
    public static class MyDate
    {
        private Date theDate;
        private String theTimeZone;

        @JSONConstructor
        public MyDate(long aTime, String aTimeZone)
        {
            theDate = new Date(aTime);
            theTimeZone = aTimeZone;
        }

        @JSONConstructorArgs
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

    JSONSerializer marshall;

    @Before
    public void setUp()
    throws Exception
    {
        marshall = new JSONSerializer();
    }

    @Test
    public void testIt() throws SerializerException
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

        JSONObject lObj = marshall.marshal(lEvil);
        Assert.assertNotNull(lObj.render(true));
        SerializerValue lResult = marshall.unmarshal(lObj);
        Assert.assertTrue(SerializerValue.REFERENCE == lResult.getType());
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
    public void testAnnotatedSerializer() throws SerializerException
    {
        // Map fields, not properties.
        ((JSONSerializer) marshall).usePojoAccess();
        MyDate lMyDate = new MyDate(new Date().getTime(), "CEST");
        JSONObject lObj = marshall.marshal(lMyDate);
        Assert.assertNotNull(lObj.render(true));
        Assert.assertNotNull(marshall.unmarshal(lObj));
    }

    @Test
    public void testDirectHelper() throws SerializerException
    {
        // Map fields, not properties.
        ((JSONSerializer) marshall).usePojoAccess();
        MyPojo lPojo = new MyPojo();
        lPojo.setNames("Homer", "Simpson");
        //
        JSONObject lObj = marshall.marshal(lPojo);
        Assert.assertNotNull(lObj.render(true));
        //
        marshall.unmarshal(lObj);
    }
}
