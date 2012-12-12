/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.validator.predicates;


import com.sdicons.json.model.JSONNull;
import com.sdicons.json.model.JSONValue;
import com.sdicons.json.validator.ValidationException;

/**
 * A predicate to test if a {@link JSONValue} is the {@link JSONNull}.
 */
public class Null
extends Predicate
{
    private static final String NULL001 = "The value '%s' is not a JSONNull in rule '%s'.";

    public Null(String aName)
    {
        super(aName);
    }

    public void validate(JSONValue aValue)
    throws ValidationException
    {
        if(!aValue.isNull())
            throw new ValidationException(String.format(NULL001, aValue.toString(), this.getName()));
    }
}
