/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.mapper;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.sdicons.json.model.JSONValue;

public class UsersBeanMapperTest
{
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
    
    private JSONMapper mapper;
    
    @Before
    public void setup() {
        mapper = new JSONMapper();
    }

    @Test
    public void testIt() throws MapperException
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

        // Java -> JSON.
        JSONValue lObj = mapper.toJSON(lEvil);

        // JSON -> Java.
        Transportable lLitmus = (Transportable) mapper.toJava(lObj, Transportable.class);
        Assert.assertNotNull(lLitmus);
        Assert.assertEquals(lLitmus.getEventType(), lEvil.getEventType());
        Assert.assertSame(lLitmus.getParentID(), lEvil.getParentID());
        Assert.assertSame(lLitmus.getSubObjectID(), lEvil.getSubObjectID());
        Assert.assertSame(lLitmus.getObjectID(), lEvil.getObjectID());
        Assert.assertEquals(lLitmus.getParam1(), lEvil.getParam1());
        Assert.assertSame(lLitmus.getParam2(), lEvil.getParam2());
    }
}
