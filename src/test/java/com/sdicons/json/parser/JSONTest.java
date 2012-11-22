/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.parser;

import com.sdicons.json.model.JSONArray;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.model.JSONValue;
import junit.framework.TestCase;

import java.io.StringReader;

public class JSONTest
extends TestCase
{
    public void testExamples()
    {
        try
        {
            final JSONParser lParser = new JSONParser(JSONTest.class.getResourceAsStream("/data/config.json"));
            final JSONValue lConfig = lParser.nextValue();
            assertTrue(lConfig.isArray());
            final JSONArray lConfigArray = (JSONArray) lConfig;

            for(JSONValue lJSONValue : lConfigArray.getValue())
            {
                TestCase.assertTrue(lJSONValue.isString());
                final JSONString lEntryResource = (JSONString) lJSONValue;

                try
                {
                    System.out.println(" --------- " + lEntryResource.getValue());
                    final JSONParser lExampleParser = new JSONParser(JSONTest.class.getResourceAsStream(lEntryResource.getValue()));
                    JSONValue lExampleVal = lExampleParser.nextValue();
                    System.out.println(lExampleVal.render(true));
                    JSONValue.decorate(lExampleVal.strip());
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                    TestCase.fail(e.getMessage());
                }
            }
        }
        catch(JSONParserException e)
        {
            e.printStackTrace();
            TestCase.fail(e.getMessage());
        }
    }

    public void testAntiExamples()
    {
        try
        {
            // final String lErrorExample = "{ \"fld1\" : \"val1\" ["; // This string contains an error.
            final String lErrorExample2 = "[1, 2, 3 {";

            final JSONParser lParser = new JSONParser(new StringReader(lErrorExample2));
            final JSONValue lConfig = lParser.nextValue();
            System.out.println(lConfig);
            TestCase.fail("An exception should be thrown when an error is found.");

        }
        catch(Exception e)
        {
            // This is correct, an exception should be thrown.
            // This test has to arrive here.
        }
    }

}

