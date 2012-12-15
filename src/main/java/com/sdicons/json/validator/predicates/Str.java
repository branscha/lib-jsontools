/*******************************************************************************
 * Copyright (c) 2006-2013 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.validator.predicates;

import com.sdicons.json.model.JSONString;
import com.sdicons.json.model.JSONValue;
import com.sdicons.json.validator.ValidationException;

/**
 * Test to see if a {@link JSONValue} is a {@link JSONString}.
 * <p>
 * <pre>
 * <code>
 * {"type":"string"}
 * </code>
 * </pre>
 *
 */
public class Str
extends Predicate
{
    private static final String STR001 = "JSONValidator/String/001: The value '%s' is not a JSONString in rule '%s'.";

    public Str(String aName)
    {
        super(aName);
    }

    public void validate(JSONValue aValue)
    throws ValidationException
    {
        if(!aValue.isString())
            throw new ValidationException(String.format(STR001, aValue.toString(), this.getName()));
    }
}
