/*******************************************************************************
 * Copyright (c) 2006-2013 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.validator;

import com.sdicons.json.model.JSONValue;

/**
 * A validator inspects a JSONValue. If everything is fine the validator does nothing, and
 * if an error is encounterd the validator throws an exception. Different validators can look
 * at different aspects of JSONValues. 
 */
public interface Validator
{
    /**
     * Validate a JSONValue.
     * @param aValue        The JSONValue that has to be validated.
     * @throws ValidationException If the validation fails.
     */
    public void validate(JSONValue aValue)
    throws ValidationException;
}
