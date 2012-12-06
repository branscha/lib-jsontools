/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
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
    public Str(String aName)
    {
        super(aName);
    }

    public void validate(JSONValue aValue)
    throws ValidationException
    {
        if(!aValue.isString()) fail("The value is not a JSONString." ,aValue);
    }
}
