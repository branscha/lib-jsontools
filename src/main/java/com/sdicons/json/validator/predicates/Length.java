/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.validator.predicates;

import com.sdicons.json.model.JSONArray;
import com.sdicons.json.model.JSONInteger;
import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.model.JSONValue;
import com.sdicons.json.validator.ValidationException;
import com.sdicons.json.validator.ValidatorUtil;

/**
 * A bounds check on the number of elements in a {@link JSONArray}, the length of a {@link JSONString} or the number of properties in a {@link JSONObject}.
 * <p>
 * An example of a length validator. The JSONValue must be an array and it must have an exact length of 5.
 *
 * <pre>
 * <code>
 * {
 *    "name"  :"Array of length 5",
 *    "type"  :"and",
 *    "rules" : [{"type":"array"}, {"type":"length","min":5,"max":5}]
 * }
 * </code>
 * </pre>
 *
 */
public class Length
extends Predicate
{
    private Integer minLength = null;
    private Integer maxLength = null;

    public Length(String aName, JSONObject aRule)
    throws ValidationException
    {
        super(aName);

        if (aRule.containsKey(ValidatorUtil.PARAM_MIN))
        {
            JSONValue lMin = aRule.get(ValidatorUtil.PARAM_MIN);
            if (!lMin.isInteger())
            {
                final String lMsg = "Minimum length should be specified using an integer.";
                throw new ValidationException(lMsg, aRule, "WRONG TYPE");
            }
            else minLength = ((JSONInteger) lMin).getValue().intValue();
        }

        if (aRule.containsKey(ValidatorUtil.PARAM_MAX))
        {
            JSONValue lMax = aRule.get(ValidatorUtil.PARAM_MAX);
            if (!lMax.isInteger())
            {
                final String lMsg = "Maximum length should be specified using an integer.";
                throw new ValidationException(lMsg, aRule, "WRONG TYPE");
            }
            else maxLength = ((JSONInteger) lMax).getValue().intValue();
        }

    }

    public void validate(JSONValue aValue) throws ValidationException
    {
        int lSize = 0;
        if(aValue.isArray()) lSize = ((JSONArray) aValue).getValue().size();
        else if(aValue.isString()) lSize = ((JSONString) aValue).getValue().length();
        else if(aValue.isObject()) lSize = ((JSONObject) aValue).getValue().size();
        else fail("The value is not a JSONArray, JSONString or JSONObject.", aValue);

        // If there are lenght specs, we check them.
        if(minLength != null)
        {
            if(lSize < minLength) fail("The size (" + lSize +") is smaller then allowed (" + minLength + ").", aValue);
        }
        if(maxLength != null)
        {
            if( lSize > maxLength ) fail("The size (" + lSize +") is larger then allowed (" + maxLength + ").", aValue);
        }
    }
}
