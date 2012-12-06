/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.validator.predicates;

import com.sdicons.json.model.JSONValue;
import com.sdicons.json.validator.ValidationException;
import com.sdicons.json.validator.Validator;

/**
 * A super class for our validators.
 *
 */
public abstract class Predicate
implements Validator
{
    private String name;

    protected Predicate(String aName)
    {
        name = aName;
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
}
