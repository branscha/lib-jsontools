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
 * This predicate tests if a {@link JSONValue} is a {@link JSONObject} or not.
 * The {@link Properties} predicate can be used for content related checking.
 */
public class Object
extends Predicate
{
    private static final String OBJ001 = "JSONValidator/Object/001: The value '%s' is not a JSONObject in rule '%s'.";

    public Object(String aName)
    {
        super(aName);
    }

    public void validate(JSONValue aValue)
    throws ValidationException
    {
        // First we check if we have an array.
        if(!aValue.isObject())
            throw new ValidationException(String.format(OBJ001, aValue.toString(), this.getName()));
    }
}
