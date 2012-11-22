/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.validator.impl.predicates;

import com.sdicons.json.validator.ValidationException;
import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONValue;

public class True
extends Predicate
{
    public True(String aName, JSONObject aRule)
    {
        super(aName, aRule);
    }

    public void validate(JSONValue aValue)
    throws ValidationException
    {
        // Do nothing at all.
        // Always succeeds.
    }
}
