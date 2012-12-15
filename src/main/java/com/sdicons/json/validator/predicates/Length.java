/*******************************************************************************
 * Copyright (c) 2006-2013 Bruno Ranschaert
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
    private static final String LEN001 = "JSONValidator/Length/001: Minimum length should be specified using an integer in rule '%s'.";
    private static final String LEN002 = "JSONValidator/Length/002: Maximum length should be specified using an integer in rule '%s'.";
    private static final String LEN003 = "JSONValidator/Length/003: The value '%s' is not a JSONArray, JSONString or JSONObject in rule '%s'.";
    private static final String LEN004 = "JSONValidator/Length/004: The size %s from '%s' is smaller then minimum %s in rule '%s'.";
    private static final String LEN005 = "JSONValidator/Length/005: The size %s from '%s' is larger then maximum %s in rule '%s'.";

    private Integer minLength = null;
    private Integer maxLength = null;

    public Length(String aName, JSONObject aRule)
    throws ValidationException
    {
        super(aName);

        if (aRule.containsKey(ValidatorUtil.PARAM_MIN))
        {
            JSONValue lMin = aRule.get(ValidatorUtil.PARAM_MIN);
            if (!lMin.isInteger()) throw new ValidationException(String.format(LEN001, this.getName()));
            else minLength = ((JSONInteger) lMin).getValue().intValue();
        }

        if (aRule.containsKey(ValidatorUtil.PARAM_MAX))
        {
            JSONValue lMax = aRule.get(ValidatorUtil.PARAM_MAX);
            if (!lMax.isInteger()) throw new ValidationException(String.format(LEN002, this.getName()));
            else maxLength = ((JSONInteger) lMax).getValue().intValue();
        }

    }

    public Length(String aName, Integer min, Integer max) {
        super(aName);
        minLength = min;
        maxLength = max;
    }

    public void validate(JSONValue aValue) throws ValidationException
    {
        int lSize = 0;
        if(aValue.isArray()) lSize = ((JSONArray) aValue).getValue().size();
        else if(aValue.isString()) lSize = ((JSONString) aValue).getValue().length();
        else if(aValue.isObject()) lSize = ((JSONObject) aValue).getValue().size();
        else throw new ValidationException(String.format(LEN003, aValue.toString(), this.getName()));

        // If there are length specifications, we check them.
        if(minLength != null)
        {
            if(lSize < minLength)
                throw new ValidationException(String.format(LEN004, lSize, aValue.toString(),  minLength, this.getName()));
        }
        if(maxLength != null)
        {
            if( lSize > maxLength )
                throw new ValidationException(String.format(LEN005, lSize, aValue.toString(),  maxLength, this.getName()));
        }
    }
}
