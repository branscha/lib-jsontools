/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.validator.predicates;

import com.sdicons.json.validator.ValidationException;
import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONValue;

/**
 * A predicate to check if a JSON value is an integer or not.
 */
public class Int
extends Predicate
{
    public Int(String aName, JSONObject aRule)
    {
        super(aName, aRule);
    }

    public void validate(JSONValue aValue)
    throws ValidationException
    {
        if(!aValue.isInteger()) fail("The value is not a JSONInteger.", aValue);
    }
}
