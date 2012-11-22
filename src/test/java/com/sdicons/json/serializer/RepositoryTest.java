/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.serializer;

import junit.framework.*;

import java.util.*;

import com.sdicons.json.serializer.helper.impl.*;
import com.sdicons.json.serializer.helper.MarshallHelper;
import com.sdicons.json.helper.HelperRepository;
import com.sdicons.json.serializer.marshall.MarshallException;
import com.sdicons.json.serializer.marshall.JSONMarshall;
import com.sdicons.json.model.JSONObject;

public class RepositoryTest
extends TestCase
{
    public RepositoryTest(String lName)
    {
        super(lName);
    }

    HelperRepository<MarshallHelper> repo;

    public void setUp()
    throws Exception
    {
        repo = new HelperRepository<MarshallHelper>();

        repo.addHelper(new ObjectHelper());
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

    public void testBasicHelpers()
    {
        {
            Class lClass = String.class;
            MarshallHelper lHelper = repo.findHelper(lClass);
            Assert.assertEquals(lClass, lHelper.getHelpedClass());
        }

        {
            Class lClass = Boolean.class;
            MarshallHelper lHelper = repo.findHelper(lClass);
            Assert.assertEquals(lClass, lHelper.getHelpedClass());
        }
        {
            Class lClass = Byte.class;
            MarshallHelper lHelper = repo.findHelper(lClass);
            Assert.assertEquals(lClass, lHelper.getHelpedClass());
        }
        {
            Class lClass = Short.class;
            MarshallHelper lHelper = repo.findHelper(lClass);
            Assert.assertEquals(lClass, lHelper.getHelpedClass());
        }
        {
            Class lClass = Integer.class;
            MarshallHelper lHelper = repo.findHelper(lClass);
            Assert.assertEquals(lClass, lHelper.getHelpedClass());
        }
        {
            Class lClass = Short.class;
            MarshallHelper lHelper = repo.findHelper(lClass);
            Assert.assertEquals(lClass, lHelper.getHelpedClass());
        }
        {
            Class lClass = Long.class;
            MarshallHelper lHelper = repo.findHelper(lClass);
            Assert.assertEquals(lClass, lHelper.getHelpedClass());
        }
        {
            Class lClass = Float.class;
            MarshallHelper lHelper = repo.findHelper(lClass);
            Assert.assertEquals(lClass, lHelper.getHelpedClass());
        }
        {
            Class lClass = Double.class;
            MarshallHelper lHelper = repo.findHelper(lClass);
            Assert.assertEquals(lClass, lHelper.getHelpedClass());
        }
        {
            Class lClass = Character.class;
            MarshallHelper lHelper = repo.findHelper(lClass);
            Assert.assertEquals(lClass, lHelper.getHelpedClass());
        }
        {
            Class lClass = String.class;
            MarshallHelper lHelper = repo.findHelper(lClass);
            Assert.assertEquals(lClass, lHelper.getHelpedClass());
        }
    }

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
        implements MarshallHelper
        {
            public void renderValue(Object aObj, JSONObject aObjectElement, JSONMarshall aMarshall, HashMap aPool)
            throws MarshallException
            {
            }

            public Object parseValue(JSONObject aObjectElement, JSONMarshall aMarshall, HashMap aPool)
            throws MarshallException
            {
                return null;
            }

            public Class getHelpedClass()
            {
                return A.class;
            }

        }

        class BHelper
        implements MarshallHelper
        {
            public void renderValue(Object aObj, JSONObject aObjectElement, JSONMarshall aMarshall, HashMap aPool)
            throws MarshallException
            {
            }

            public Object parseValue(JSONObject aObjectElement, JSONMarshall aMarshall, HashMap aPool)
            throws MarshallException
            {
                return null;
            }

            public Class getHelpedClass()
            {
                return B.class;
            }
        }

        class CHelper
        implements MarshallHelper
        {
            public void renderValue(Object aObj, JSONObject aObjectElement, JSONMarshall aMarshall, HashMap aPool)
            throws MarshallException
            {
            }

            public Object parseValue(JSONObject aObjectElement, JSONMarshall aMarshall, HashMap aPool)
            throws MarshallException
            {
                return null;
            }

            public Class getHelpedClass()
            {
                return C.class;
            }

        }

        repo.addHelper(new CHelper()); // C first.
        repo.addHelper(new BHelper()); // B first.
        repo.addHelper(new AHelper()); // C and B will be part of rebalancing.

        MarshallHelper lH1 = repo.findHelper(B.class);
        Assert.assertEquals(B.class, lH1.getHelpedClass());

        MarshallHelper lH2 = repo.findHelper(A.class);
        Assert.assertEquals(A.class, lH2.getHelpedClass());

        MarshallHelper lH3 = repo.findHelper(C.class);
        Assert.assertEquals(C.class, lH3.getHelpedClass());
    }

}
