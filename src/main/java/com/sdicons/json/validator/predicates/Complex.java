/*******************************************************************************
 * Copyright (c) 2006-2013 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.validator.predicates;

import com.sdicons.json.model.JSONValue;
import com.sdicons.json.validator.ValidationException;

/**
 * A predicate to check if a JSON value is a JSONObject or a JSONArray. It fails if
 * the JSON value is a JSONInteger, JSONBoolean, JSONString or JSONDecimal.
 */
public class Complex
extends Predicate
{
    private static final String CPLX001 = "JSONValidator/Complex/001: The value '%s' is not a JSONComplex in rule '%s'.";

    public Complex(String aName)
    {
        super(aName);
    }

    public void validate(JSONValue aValue)
    throws ValidationException
    {
        if(!aValue.isComplex())
            throw new ValidationException(String.format(CPLX001, aValue.toString(), this.getName()));
    }
}
