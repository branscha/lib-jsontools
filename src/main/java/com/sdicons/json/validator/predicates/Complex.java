/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.validator.predicates;

import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONValue;
import com.sdicons.json.validator.ValidationException;

/**
 * A predicate to check if a JSON value is a JSONObject or a JSONArray. It fails if 
 * the JSON value is a JSONInteger, JSONBoolean, JSONString or JSONDecimal.
 */
public class Complex
extends Predicate
{
    public Complex(String aName, JSONObject aRule)
    {
        super(aName);
    }

    public void validate(JSONValue aValue)
    throws ValidationException
    {
        if(!aValue.isComplex()) fail("The value is not a JSONCOmplex", aValue);
    }
}
