/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.validator.impl.predicates;

import com.sdicons.json.validator.ValidationException;
import com.sdicons.json.model.JSONValue;
import com.sdicons.json.model.JSONObject;

/**
 * This rule always fails. We have to include it to make our logic complete.
 */
public class False
extends Predicate
{
    public False(String aName, JSONObject aRule)
    {
        super(aName, aRule);
    }

    public void validate(JSONValue aValue)
    throws ValidationException
    {
        fail("Always false.", aValue);
    }
}
