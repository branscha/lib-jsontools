/*******************************************************************************
 * Copyright (c) 2006-2013 Bruno Ranschaert
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
    private static final String SIM001 = "JSONValidator/Simple/001: The value '%s' is not JSONSimple in rule '%s'.";

    public Simple(String aName)
    {
        super(aName);
    }

    public void validate(JSONValue aValue)
    throws ValidationException
    {
        if(!aValue.isSimple())
            throw new ValidationException(String.format(SIM001, aValue.toString(), this.getName()));
    }
}
