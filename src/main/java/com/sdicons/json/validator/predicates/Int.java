/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.validator.predicates;

import com.sdicons.json.model.JSONValue;
import com.sdicons.json.validator.ValidationException;

/**
 * A predicate to check if a JSON value is an integer or not.
 */
public class Int
extends Predicate
{
    private static final String INT001 = "JSONValidator/Int/001: The value '%s' is not a JSONInteger in rule '%s'.";

    public Int(String aName)
    {
        super(aName);
    }

    public void validate(JSONValue aValue)
    throws ValidationException
    {
        if(!aValue.isInteger())
            throw new ValidationException(String.format(INT001, aValue.toString(), this.getName()));
    }
}
