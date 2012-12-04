/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.validator;

import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONValue;
import com.sdicons.json.validator.util.ValidatorUtil;

/**
 * A validator that accepts a validator description in JSON format.
 * 
 * <p>
 * In this example the validator itself is stored in a JSON text file as well. This is not
 * a requirement, you can build the validator in code as well.
 * 
 * <pre>
 * <code>
 * final JSONParser parser = new JSONParser(ValidatorTest.class.getResourceAsStream("/rules/range-validator.json"));
 * final Validator validator = new JSONValidator((JSONObject) parser.nextValue());
 * validator.validate(new JSONInteger(new BigDecimal("5")));
 * </code>
 * </pre>
 * 
 * The validator file looks like this. It is a range check.
 * 
 * <pre>
 * <code>
 * {
 *    "name" :"Range Test",
 *    "type" :"range",
 *    "min" : 50,
 *    "max" : 100
 * }
 * </code>
 * </pre>
 */
public class JSONValidator
implements Validator
{
    private Validator validator;

    /**
     * Construct the validator based on the JSON description.
     * @param aValidation   The JSON description of the validator.
     * @throws ValidationException If the JSON description did not represent a validator.
     */
    public JSONValidator(JSONObject aValidation)
    throws ValidationException
    {
        validator = ValidatorUtil.buildValidator(aValidation);
    }

    /**
     * Validate a JSON value according to the rules described in the
     * validator rules.
     * @param aValue
     * @throws ValidationException
     */
    public void validate(JSONValue aValue)
    throws ValidationException
    {
        validator.validate(aValue);
    }
}
