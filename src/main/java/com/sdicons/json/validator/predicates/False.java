/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.validator.predicates;

import com.sdicons.json.model.JSONValue;
import com.sdicons.json.validator.ValidationException;

/**
 * This rule always fails. We have to include it to make our logic complete.
 * <p>
 * It is as simple as this:
 *
 * <pre>
 * <code>
 * {
 *    "name" :"False test",
 *    "type" :"false"
 * }
 * </code>
 * </pre>
 *
 * @see True
 */
public class False
extends Predicate
{
    private static final String FALSE001 = "JSONValidator/False/001: This rule '%s' always fails by definition independent of the value ('%s').";

    public False(String aName)
    {
        super(aName);
    }

    public void validate(JSONValue aValue)
    throws ValidationException
    {
        throw new ValidationException(String.format(FALSE001, this.getName(), aValue.toString()));
    }
}
