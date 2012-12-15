/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.validator.predicates;


import com.sdicons.json.model.JSONValue;
import com.sdicons.json.validator.ValidationException;

/**
 * A predicate to check if a value is a boolean.
 * <p>
 * <pre>
 * <code>
 * {"type":"bool"}
 * </code>
 * </pre>
 */
public class Bool
extends Predicate
{
    private static final String BOOL001 = "JSONValidator/Bool/001: The value '%s' is not a JSONBoolean in rule '%s'.";

    public Bool(String aName)
    {
        super(aName);
    }

    public void validate(JSONValue aValue)
    throws ValidationException
    {
        if(!aValue.isBoolean())
            throw new ValidationException(String.format(BOOL001, aValue.toString(), this.getName()));
    }
}
