/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.validator.impl.predicates;


import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONValue;
import com.sdicons.json.validator.ValidationException;

/**
 * A predicate to check if a value is a boolean.
 */
public class Bool
extends Predicate
{
    public Bool(String aName, JSONObject aRule)
    {
        super(aName, aRule);
    }

    public void validate(JSONValue aValue)
    throws ValidationException
    {
        if(!aValue.isBoolean()) fail("The value is not a JSONBoolean", aValue);
    }
}
