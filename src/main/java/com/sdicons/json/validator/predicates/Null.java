/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.validator.predicates;


import com.sdicons.json.model.JSONNull;
import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONValue;
import com.sdicons.json.validator.ValidationException;

/**
 * A predicate to test if a {@link JSONValue} is the {@link JSONNull}.
 */
public class Null
extends Predicate
{
    public Null(String aName, JSONObject aRule)
    {
        super(aName, aRule);
    }

    public void validate(JSONValue aValue)
    throws ValidationException
    {
        if(!aValue.isNull()) fail("The value is not a JSONNull." ,aValue);
    }
}
