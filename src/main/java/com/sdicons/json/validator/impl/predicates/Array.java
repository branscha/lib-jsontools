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
 * A predicate to check if a JSON value is an array.
 */
public class Array
extends Predicate
{
    public Array(String aName, JSONObject aRule)
    throws ValidationException
    {
        super(aName, aRule);
    }

    public void validate(JSONValue aValue)
    throws ValidationException
    {
        // First we check if we have an array.
        if(!aValue.isArray()) fail("The value is not a JSONArray.", aValue);
    }
}
