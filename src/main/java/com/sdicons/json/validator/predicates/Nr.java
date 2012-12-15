/*******************************************************************************
 * Copyright (c) 2006-2013 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.validator.predicates;

import com.sdicons.json.model.JSONDecimal;
import com.sdicons.json.model.JSONInteger;
import com.sdicons.json.model.JSONValue;
import com.sdicons.json.validator.ValidationException;

/**
 * A predicate to test if a {@link JSONValue} is a number representation, in JSON there are two viz. {@link JSONInteger}  and {@link JSONDecimal}.
 *
 */
public class Nr
extends Predicate
{
    private static final String NR001 = "JSONValidator/Nr/001: The value '%s' is not a JSONNumber in rule '%s'.";

    public Nr(String aName)
    {
        super(aName);
    }

    public void validate(JSONValue aValue)
    throws ValidationException
    {
        if(!aValue.isNumber())
            throw new ValidationException(String.format(NR001, aValue.toString(), this.getName()));
    }
}
