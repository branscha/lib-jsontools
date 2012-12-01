/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.parser;

import java.io.StringReader;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Assert;
import org.junit.Test;

import com.sdicons.json.model.JSONArray;
import com.sdicons.json.model.JSONObject;
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

    @Test(expected=ParserException.class)
    public void bug18118() throws ParserException {
        final String example = "{\"lots\":[ [\"01\",\"JDX004.CSPS.JDX4D002.JDX001.D110101\"]]}\r\n" +
        		"{\"rows\":[ [\"01000000C3\"],[\"01000000C4\"],[\"01000000C5\"],[\"01000000C6\"],[\"01000000C7\"],[\"01000000C8\"],[\"01000000C9\"],[\"01000000CA\"],[\"01000000CB\"],[\"01000000CC\"],[\"01000\"]";

        final JSONParser lParser = new JSONParser(new StringReader(example));
        lParser.nextValue();
        // This should fail ...
        lParser.nextValue();
    }

    @Test(expected=ParserException.class)
    public void bug17351() throws ParserException {
        final String example = "{\"key\":\"M";
        final JSONParser lParser = new JSONParser(new StringReader(example));
        lParser.nextValue();
    }

    @Test
    public void bug016634() throws ParserException  {
        JSONObject parsed = (JSONObject) new JSONParser(new StringReader("{\"bigNumber\":-4.569565E+7}")).nextValue();
        Assert.assertEquals(new BigDecimal(-4.569565E+7), new BigDecimal((BigInteger)parsed.get("bigNumber").strip()));
    }

}

