/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.validator;

import com.sdicons.json.validator.impl.ValidatorUtil;
import com.sdicons.json.model.JSONValue;
import com.sdicons.json.model.JSONObject;

/**
 * A validator that accepts a validator description in JSON format.
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
