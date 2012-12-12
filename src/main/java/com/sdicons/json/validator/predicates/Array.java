/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.validator.predicates;

import com.sdicons.json.model.JSONValue;
import com.sdicons.json.validator.ValidationException;

/**
 * A predicate to check if a JSON value is an array.
 * <p>
 * An example of an array validator. The JSONValue must be an array and it must contain integers.
 *
 * <pre>
 * <code>
 * {
 *    "name" :"List of integers",
 *    "type" :"and",
 *    "rules" : [{"type":"array"},{"type":"content","rule":{"type":"int"}}]
 * }
 * </code>
 * </pre>
 */
public class Array
extends Predicate
{
    private static final String ARR001 = "JSONValidator/Array/001: The value '%s' is not a JSONArray in rule '%s'.";

    public Array(String aName)
    {
        super(aName);
    }

    public void validate(JSONValue aValue)
    throws ValidationException
    {
        // First we check if we have an array.
        if(!aValue.isArray())
            throw new ValidationException(String.format(ARR001, aValue.toString(), this.getName()));
    }
}
