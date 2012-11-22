/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.validator.impl.predicates;

import com.sdicons.json.validator.Validator;
import com.sdicons.json.validator.ValidationException;
import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONValue;

public abstract class Predicate
implements Validator
{
    private String name;
    private JSONObject rule;

    protected Predicate(String aName, JSONObject aRule)
    {
        name = aName;
        rule = aRule;
    }

    protected void fail(JSONValue aValue)
    throws ValidationException
    {
        throw new ValidationException(aValue, name);
    }

    protected void fail(String aMessage, JSONValue aValue)
    throws ValidationException
    {
        throw new ValidationException(aMessage, aValue, name);
    }

    public String getName()
    {
        return name;
    }

    public JSONObject getRule()
    {
        return rule;
    }
}
