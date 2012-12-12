/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.validator.predicates;

import java.math.BigDecimal;

import com.sdicons.json.model.JSONDecimal;
import com.sdicons.json.model.JSONInteger;
import com.sdicons.json.model.JSONNumber;
import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONValue;
import com.sdicons.json.validator.ValidationException;
import com.sdicons.json.validator.ValidatorUtil;

/**
 * A predicate to see if a {@link JSONNumber} falls in the required range.
 * <p>
 * This examples allows numbers between 50 and 100.
 * <pre>
 * <code>
 * { "name" :"Range validator",
 *   "type" :"range",
 *   "min" : 50,
 *   "max" : 100
v  }
 * </code>
 * </pre>
 *
 */
public class Range
extends Predicate
{
    private static final String RAN001 = "JSONValidator/Range/001: Minimum length should be specified using a number in rule '%s'.";
    private static final String RAN002 = "JSONValidator/Range/002: Maximum length should be specified using an integer in rule '%s'.";
    private static final String RAN003 = "JSONValidator/Range/003: The value '%s' is not a JSONNumber in rule '%s'.";
    private static final String RAN004 = "JSONValidator/Range/004: The value %s from '%s' is below the lower boundary %s in rule '%s'.";
    private static final String RAN005 = "JSONValidator/Range/005: The value %s from '%s' is above the upper boundary %s in rule '%s'.";

    private BigDecimal minValue = null;
    private BigDecimal maxValue = null;

    public Range(String aName, Integer min, Integer max) {
        super(aName);
        minValue = maxValue = null;
        if(min != null) minValue = new BigDecimal(min);
        if(max != null) maxValue = new BigDecimal(max);
    }

    public Range(String aName, BigDecimal min, BigDecimal max) {
        super(aName);
        this.minValue = min;
        this.maxValue = max;
    }

    public Range(String aName, JSONObject aRule)
    throws ValidationException
    {
        super(aName);

        if (aRule.containsKey(ValidatorUtil.PARAM_MIN))
        {
            JSONValue lMin = aRule.get(ValidatorUtil.PARAM_MIN);
            if (!lMin.isNumber())
            {
                throw new ValidationException(String.format(RAN001, aName));
            }
            else minValue = cvtNumber((JSONNumber) lMin);
        }

        if (aRule.containsKey(ValidatorUtil.PARAM_MAX))
        {
            JSONValue lMax = aRule.get(ValidatorUtil.PARAM_MAX);
            if (!lMax.isInteger())
            {
                throw new ValidationException(String.format(RAN002, aName));
            }
            else maxValue = cvtNumber((JSONNumber) lMax);
        }
    }

    public void validate(JSONValue aValue) throws ValidationException
    {
        if(!aValue.isNumber())
            throw new ValidationException(String.format(RAN003, aValue.toString(), this.getName()));
        BigDecimal lSize = cvtNumber((JSONNumber) aValue);

        // If there are length specs, we check them.
        if(minValue != null)
        {
            if(lSize.compareTo(minValue) < 0)
                throw new ValidationException(String.format(RAN004, lSize, aValue.toString(), minValue, this.getName()));
        }
        if(maxValue != null)
        {
            if(lSize.compareTo(maxValue) > 0 )
                throw new ValidationException(String.format(RAN005, lSize, aValue.toString(), maxValue, this.getName()));
        }
    }

    private BigDecimal cvtNumber(JSONNumber aNum)
    {
        if (aNum.isInteger()) return new BigDecimal(((JSONInteger) aNum).getValue());
        else return ((JSONDecimal) aNum).getValue();
    }
}
