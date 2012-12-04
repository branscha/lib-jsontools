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

import com.sdicons.json.model.JSONObject;
import com.sdicons.json.repository.ClassHelperRepository;
import com.sdicons.json.serializer.helper.BooleanSerializer;
import com.sdicons.json.serializer.helper.ByteSerializer;
import com.sdicons.json.serializer.helper.CharacterSerializer;
import com.sdicons.json.serializer.helper.ClassSerializer;
import com.sdicons.json.serializer.helper.DateSerializer;
import com.sdicons.json.serializer.helper.DoubleSerializer;
import com.sdicons.json.serializer.helper.FloatSerializer;
import com.sdicons.json.serializer.helper.IntegerSerializer;
import com.sdicons.json.serializer.helper.LongSerializer;
import com.sdicons.json.serializer.helper.ObjectSerializerProps;
import com.sdicons.json.serializer.helper.ShortSerializer;
import com.sdicons.json.serializer.helper.StringSerializer;

public class RepositoryTest
{
    ClassHelperRepository<ClassSerializer> repo;

    @Before
    public void setUp()
    throws Exception
    {
        repo = new ClassHelperRepository<ClassSerializer>();

        repo.addHelper(new ObjectSerializerProps());
        repo.addHelper(new StringSerializer());
        repo.addHelper(new BooleanSerializer());
        repo.addHelper(new ByteSerializer());
        repo.addHelper(new ShortSerializer());
        repo.addHelper(new IntegerSerializer());
        repo.addHelper(new LongSerializer());
        repo.addHelper(new FloatSerializer());
        repo.addHelper(new DoubleSerializer());
        repo.addHelper(new CharacterSerializer());
        repo.addHelper(new DateSerializer());
    }

    @Test
    public void testBasicHelpers()
    {
        {
            Class<?> lClass = String.class;
            ClassSerializer lHelper = repo.findHelper(lClass);
            Assert.assertEquals(lClass, lHelper.getHelpedClass());
        }

        {
            Class<?> lClass = Boolean.class;
            ClassSerializer lHelper = repo.findHelper(lClass);
            Assert.assertEquals(lClass, lHelper.getHelpedClass());
        }
        {
            Class<?> lClass = Byte.class;
            ClassSerializer lHelper = repo.findHelper(lClass);
            Assert.assertEquals(lClass, lHelper.getHelpedClass());
        }
        {
            Class<?> lClass = Short.class;
            ClassSerializer lHelper = repo.findHelper(lClass);
            Assert.assertEquals(lClass, lHelper.getHelpedClass());
        }
        {
            Class<?> lClass = Integer.class;
            ClassSerializer lHelper = repo.findHelper(lClass);
            Assert.assertEquals(lClass, lHelper.getHelpedClass());
        }
        {
            Class<?> lClass = Short.class;
            ClassSerializer lHelper = repo.findHelper(lClass);
            Assert.assertEquals(lClass, lHelper.getHelpedClass());
        }
        {
            Class<?> lClass = Long.class;
            ClassSerializer lHelper = repo.findHelper(lClass);
            Assert.assertEquals(lClass, lHelper.getHelpedClass());
        }
        {
            Class<?> lClass = Float.class;
            ClassSerializer lHelper = repo.findHelper(lClass);
            Assert.assertEquals(lClass, lHelper.getHelpedClass());
        }
        {
            Class<?> lClass = Double.class;
            ClassSerializer lHelper = repo.findHelper(lClass);
            Assert.assertEquals(lClass, lHelper.getHelpedClass());
        }
        {
            Class<?> lClass = Character.class;
            ClassSerializer lHelper = repo.findHelper(lClass);
            Assert.assertEquals(lClass, lHelper.getHelpedClass());
        }
        {
            Class<?> lClass = String.class;
            ClassSerializer lHelper = repo.findHelper(lClass);
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
        implements ClassSerializer
        {
            @SuppressWarnings("rawtypes")
            public void toJSON(Object aObj, JSONObject aObjectElement, JSONSerializer aMarshall, HashMap aPool)
            throws SerializerException
            {
            }

            @SuppressWarnings("rawtypes")
            public Object toJava(JSONObject aObjectElement, JSONSerializer aMarshall, HashMap aPool)
            throws SerializerException
            {
                return null;
            }

            public Class<?> getHelpedClass()
            {
                return A.class;
            }

        }

        class BHelper
        implements ClassSerializer
        {
            @SuppressWarnings("rawtypes")
            public void toJSON(Object aObj, JSONObject aObjectElement, JSONSerializer aMarshall, HashMap aPool)
            throws SerializerException
            {
            }

            @SuppressWarnings("rawtypes")
            public Object toJava(JSONObject aObjectElement, JSONSerializer aMarshall, HashMap aPool)
            throws SerializerException
            {
                return null;
            }

            public Class<?> getHelpedClass()
            {
                return B.class;
            }
        }

        class CHelper
        implements ClassSerializer
        {
            @SuppressWarnings("rawtypes")
            public void toJSON(Object aObj, JSONObject aObjectElement, JSONSerializer aMarshall, HashMap aPool)
            throws SerializerException
            {
            }

            @SuppressWarnings("rawtypes")
            public Object toJava(JSONObject aObjectElement, JSONSerializer aMarshall, HashMap aPool)
            throws SerializerException
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

        ClassSerializer lH1 = repo.findHelper(B.class);
        Assert.assertEquals(B.class, lH1.getHelpedClass());

        ClassSerializer lH2 = repo.findHelper(A.class);
        Assert.assertEquals(A.class, lH2.getHelpedClass());

        ClassSerializer lH3 = repo.findHelper(C.class);
        Assert.assertEquals(C.class, lH3.getHelpedClass());
    }
}
