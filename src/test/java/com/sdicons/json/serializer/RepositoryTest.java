/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.serializer;

import java.util.HashMap;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.sdicons.json.helper.HelperRepository;
import com.sdicons.json.model.JSONObject;
import com.sdicons.json.serializer.helper.SerializeHelper;
import com.sdicons.json.serializer.helper.impl.BooleanHelper;
import com.sdicons.json.serializer.helper.impl.ByteHelper;
import com.sdicons.json.serializer.helper.impl.CharacterHelper;
import com.sdicons.json.serializer.helper.impl.DateHelper;
import com.sdicons.json.serializer.helper.impl.DoubleHelper;
import com.sdicons.json.serializer.helper.impl.FloatHelper;
import com.sdicons.json.serializer.helper.impl.IntegerHelper;
import com.sdicons.json.serializer.helper.impl.LongHelper;
import com.sdicons.json.serializer.helper.impl.ObjectHelperProps;
import com.sdicons.json.serializer.helper.impl.ShortHelper;
import com.sdicons.json.serializer.helper.impl.StringHelper;

public class RepositoryTest
{
    HelperRepository<SerializeHelper> repo;

    @Before
    public void setUp()
    throws Exception
    {
        repo = new HelperRepository<SerializeHelper>();

        repo.addHelper(new ObjectHelperProps());
        repo.addHelper(new StringHelper());
        repo.addHelper(new BooleanHelper());
        repo.addHelper(new ByteHelper());
        repo.addHelper(new ShortHelper());
        repo.addHelper(new IntegerHelper());
        repo.addHelper(new LongHelper());
        repo.addHelper(new FloatHelper());
        repo.addHelper(new DoubleHelper());
        repo.addHelper(new CharacterHelper());
        repo.addHelper(new DateHelper());
    }

    @Test
    public void testBasicHelpers()
    {
        {
            Class<?> lClass = String.class;
            SerializeHelper lHelper = repo.findHelper(lClass);
            Assert.assertEquals(lClass, lHelper.getHelpedClass());
        }

        {
            Class<?> lClass = Boolean.class;
            SerializeHelper lHelper = repo.findHelper(lClass);
            Assert.assertEquals(lClass, lHelper.getHelpedClass());
        }
        {
            Class<?> lClass = Byte.class;
            SerializeHelper lHelper = repo.findHelper(lClass);
            Assert.assertEquals(lClass, lHelper.getHelpedClass());
        }
        {
            Class<?> lClass = Short.class;
            SerializeHelper lHelper = repo.findHelper(lClass);
            Assert.assertEquals(lClass, lHelper.getHelpedClass());
        }
        {
            Class<?> lClass = Integer.class;
            SerializeHelper lHelper = repo.findHelper(lClass);
            Assert.assertEquals(lClass, lHelper.getHelpedClass());
        }
        {
            Class<?> lClass = Short.class;
            SerializeHelper lHelper = repo.findHelper(lClass);
            Assert.assertEquals(lClass, lHelper.getHelpedClass());
        }
        {
            Class<?> lClass = Long.class;
            SerializeHelper lHelper = repo.findHelper(lClass);
            Assert.assertEquals(lClass, lHelper.getHelpedClass());
        }
        {
            Class<?> lClass = Float.class;
            SerializeHelper lHelper = repo.findHelper(lClass);
            Assert.assertEquals(lClass, lHelper.getHelpedClass());
        }
        {
            Class<?> lClass = Double.class;
            SerializeHelper lHelper = repo.findHelper(lClass);
            Assert.assertEquals(lClass, lHelper.getHelpedClass());
        }
        {
            Class<?> lClass = Character.class;
            SerializeHelper lHelper = repo.findHelper(lClass);
            Assert.assertEquals(lClass, lHelper.getHelpedClass());
        }
        {
            Class<?> lClass = String.class;
            SerializeHelper lHelper = repo.findHelper(lClass);
            Assert.assertEquals(lClass, lHelper.getHelpedClass());
        }
    }

    @Test
    public void testInheritance()
    {
        class A
        {
        }

        class B
        extends A
        {
        }

        class C
        extends A
        {
        }

        class AHelper
        implements SerializeHelper
        {
            @SuppressWarnings("rawtypes")
            public void renderValue(Object aObj, JSONObject aObjectElement, JSONSerializer aMarshall, HashMap aPool)
            throws JSONSerializeException
            {
            }

            @SuppressWarnings("rawtypes")
            public Object parseValue(JSONObject aObjectElement, JSONSerializer aMarshall, HashMap aPool)
            throws JSONSerializeException
            {
                return null;
            }

            public Class<?> getHelpedClass()
            {
                return A.class;
            }

        }

        class BHelper
        implements SerializeHelper
        {
            @SuppressWarnings("rawtypes")
            public void renderValue(Object aObj, JSONObject aObjectElement, JSONSerializer aMarshall, HashMap aPool)
            throws JSONSerializeException
            {
            }

            @SuppressWarnings("rawtypes")
            public Object parseValue(JSONObject aObjectElement, JSONSerializer aMarshall, HashMap aPool)
            throws JSONSerializeException
            {
                return null;
            }

            public Class<?> getHelpedClass()
            {
                return B.class;
            }
        }

        class CHelper
        implements SerializeHelper
        {
            @SuppressWarnings("rawtypes")
            public void renderValue(Object aObj, JSONObject aObjectElement, JSONSerializer aMarshall, HashMap aPool)
            throws JSONSerializeException
            {
            }

            @SuppressWarnings("rawtypes")
            public Object parseValue(JSONObject aObjectElement, JSONSerializer aMarshall, HashMap aPool)
            throws JSONSerializeException
            {
                return null;
            }

            public Class<?> getHelpedClass()
            {
                return C.class;
            }

        }

        repo.addHelper(new CHelper()); // C first.
        repo.addHelper(new BHelper()); // B first.
        repo.addHelper(new AHelper()); // C and B will be part of rebalancing.

        SerializeHelper lH1 = repo.findHelper(B.class);
        Assert.assertEquals(B.class, lH1.getHelpedClass());

        SerializeHelper lH2 = repo.findHelper(A.class);
        Assert.assertEquals(A.class, lH2.getHelpedClass());

        SerializeHelper lH3 = repo.findHelper(C.class);
        Assert.assertEquals(C.class, lH3.getHelpedClass());
    }
}
