/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.validator.predicates;

import com.sdicons.json.model.JSONDecimal;
import com.sdicons.json.model.JSONInteger;
import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONValue;
import com.sdicons.json.validator.ValidationException;

/**
 * A predicate to test if a {@link JSONValue} is a number representation, in JSON there are two viz. {@link JSONInteger}  and {@link JSONDecimal}.
 *
 */
public class Nr
extends Predicate
{
    public Nr(String aName, JSONObject aRule)
    {
        super(aName, aRule);
    }

    public void validate(JSONValue aValue)
    throws ValidationException
    {
        if(!aValue.isNumber()) fail("The value is not a JSONNumber.", aValue);
    }
}
