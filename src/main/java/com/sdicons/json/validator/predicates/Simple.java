/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.validator.predicates;

import com.sdicons.json.model.JSONBoolean;
import com.sdicons.json.model.JSONDecimal;
import com.sdicons.json.model.JSONInteger;
import com.sdicons.json.model.JSONNull;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.model.JSONValue;
import com.sdicons.json.validator.ValidationException;

/**
 * This predicate tests whether a {@link JSONValue} is a simple value, one of {@link JSONString}, {@link JSONBoolean},
 * {@link JSONDecimal}, {@link JSONInteger} or {@link JSONNull}.
 *
 */
public class Simple
extends Predicate
{
    public Simple(String aName)
    {
        super(aName);
    }

    public void validate(JSONValue aValue)
    throws ValidationException
    {
        if(!aValue.isSimple()) fail("The value is not JSONSimple.", aValue);
    }
}
