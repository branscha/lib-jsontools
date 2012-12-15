/*******************************************************************************
 * Copyright (c) 2006-2013 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.validator.predicates;

import com.sdicons.json.model.JSONValue;
import com.sdicons.json.validator.ValidationException;

/**
 * Check if a JSON value is a JSON decimal or not.
 */
public class Decimal
extends Predicate
{
    private static final String DEC001 = "JSONValidator/Decimal/001: The value '%s' is not a JSONDecimal in rule '%s'.";

    public Decimal(String aName)
    {
        super(aName);
    }

    public void validate(JSONValue aValue)
    throws ValidationException
    {
        if(!aValue.isDecimal())
            throw new ValidationException(String.format(DEC001, aValue, this.getName()));
    }
}
