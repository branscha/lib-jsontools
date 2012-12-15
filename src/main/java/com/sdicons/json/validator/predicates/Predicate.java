/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.validator.predicates;

import com.sdicons.json.validator.Validator;

/**
 * A super class for our validators.
 *
 */
public abstract class Predicate
implements Validator
{
    private String name = "anonymous";

    protected Predicate(String aName)
    {
        name = aName;
    }

    public String getName()
    {
        return name;
    }
}
