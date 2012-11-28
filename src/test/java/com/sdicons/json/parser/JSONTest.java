/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.parser;

import java.io.StringReader;

import org.junit.Assert;
import org.junit.Test;

import com.sdicons.json.model.JSONArray;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.model.JSONValue;

public class JSONTest
{
    @Test
    public void testExamples() throws ParserException
    {
        final JSONParser lParser = new JSONParser(JSONTest.class.getResourceAsStream("/data/config.json"));
        final JSONValue lConfig = lParser.nextValue();
        Assert.assertTrue(lConfig.isArray());
        final JSONArray lConfigArray = (JSONArray) lConfig;

        for (JSONValue lJSONValue : lConfigArray.getValue()) {
            Assert.assertTrue(lJSONValue.isString());
            final JSONString lEntryResource = (JSONString) lJSONValue;
            //
            final JSONParser lExampleParser = new JSONParser(JSONTest.class.getResourceAsStream(lEntryResource.getValue()));
            JSONValue lExampleVal = lExampleParser.nextValue();
            Assert.assertNotNull(lExampleVal.render(true));
            //
            JSONValue.decorate(lExampleVal.strip());
        }
    }

    @Test(expected=ParserException.class)
    public void testAntiExamples() throws ParserException
    {
        // final String lErrorExample = "{ \"fld1\" : \"val1\" ["; // This
        // string contains an error.
        final String lErrorExample2 = "[1, 2, 3 {";

        final JSONParser lParser = new JSONParser(new StringReader(lErrorExample2));
        lParser.nextValue();
        Assert.fail("An exception should be thrown when an error is found.");
    }

}

