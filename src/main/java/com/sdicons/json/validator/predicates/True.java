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
 * This rule always succeeds. We have to include it to make our logic complete.
 * <p>
 * It is as simple as this:
 *
 * <pre>
 * <code>
 * {
 *    "name" :"True test",
 *    "type" :"true"
 * }
 * </code>
 * </pre>
 *
 * @see False
 */
public class True
extends Predicate
{
    public True(String aName, JSONObject aRule)
    {
        super(aName, aRule);
    }

    public void validate(JSONValue aValue)
    throws ValidationException
    {
        // Do nothing at all.
        // Always succeeds.
    }
}
