/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.validator;

import org.junit.Assert;
import org.junit.Test;

import com.sdicons.json.model.JSONArray;
import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.model.JSONValue;
import com.sdicons.json.parser.JSONParser;
import com.sdicons.json.parser.ParserException;

public class ValidatorTest
{
    @Test
    public void testSimple() throws ParserException, ValidationException
    {
        // This is a good illustration of the use of a validator.
        // The code is kept as simple and complete as possible.
        final JSONParser parser = new JSONParser(ValidatorTest.class.getResourceAsStream("/rules/simple.json"));
        final Validator validator = new JSONValidator((JSONObject) parser.nextValue());
        validator.validate(new JSONObject());                  
    }

    @Test
    public void testValidations() throws ParserException, ValidationException
    {
        final JSONParser lParser1 = new JSONParser(ValidatorTest.class.getResourceAsStream("/validator-validator.json"));
        final JSONObject lValidatorObject = (JSONObject) lParser1.nextValue();
        final Validator lValidatorValidator = new JSONValidator(lValidatorObject);
        
        final JSONParser lParser = new JSONParser(ValidatorTest.class.getResourceAsStream("/rules/config.json"));
        final JSONValue lConfig = lParser.nextValue();
        
        final JSONArray lConfigArray = (JSONArray) lConfig;
        
        for (final JSONValue lEntry : lConfigArray.getValue()) {
            final JSONString lEntryResource = (JSONString) lEntry;
//            System.out.println(" --------- " + lEntryResource.getValue());
            testCase(lEntryResource.getValue(), lValidatorValidator);
        }
    }

    private void testCase(String aResource, Validator aValidatorValidator) throws ValidationException, ParserException
    {
        final JSONParser lParser = new JSONParser(ValidatorTest.class.getResourceAsStream(aResource));
        final JSONObject lTestData = (JSONObject) lParser.nextValue();
        final JSONObject lCheckerObject = (JSONObject) lTestData.get("validator");
        
        // Note that in practice it is not necessary to validate a validator.
        // The validator code
        // is independent of the validator mechanism, otherwise we would have a
        // chicken-egg problem.
        // The check is here to test the validator-validator definition.
        aValidatorValidator.validate(lCheckerObject);
        
        final Validator lChecker = new JSONValidator(lCheckerObject);
        final JSONArray lGoods = (JSONArray) lTestData.get("good");
        final JSONArray lBads = (JSONArray) lTestData.get("bad");
        
        for (JSONValue jsonValue : lGoods.getValue()) {
            lChecker.validate(jsonValue);
        }
        
        for (JSONValue jsonValue1 : lBads.getValue()) {
            try {
                lChecker.validate(jsonValue1);
                // Failure if success.
                Assert.fail("Should have failed: " + jsonValue1.toString());
            }
            catch (ValidationException eIgnore) {
            }
        }
    }
}
