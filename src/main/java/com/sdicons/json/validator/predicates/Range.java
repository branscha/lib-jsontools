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
    private BigDecimal minValue = null;
    private BigDecimal maxValue = null;

    public Range(String aName, JSONObject aRule)
    throws ValidationException
    {
        super(aName, aRule);

        if (aRule.containsKey(ValidatorUtil.PARAM_MIN))
        {
            JSONValue lMin = aRule.get(ValidatorUtil.PARAM_MIN);
            if (!lMin.isNumber())
            {
                final String lMsg = "Minimum length should be specified using a number.";
                throw new ValidationException(lMsg, aRule, "WRONG TYPE");
            }
            else minValue = cvtNumber((JSONNumber) lMin);
        }

        if (aRule.containsKey(ValidatorUtil.PARAM_MAX))
        {
            JSONValue lMax = aRule.get(ValidatorUtil.PARAM_MAX);
            if (!lMax.isInteger())
            {
                final String lMsg = "Maximum length should be specified using an integer.";
                throw new ValidationException(lMsg, aRule, "WRONG TYPE");
            }
            else maxValue = cvtNumber((JSONNumber) lMax);
        }
    }

    public void validate(JSONValue aValue) throws ValidationException
    {
        if(!aValue.isNumber()) fail("The value is not a JSONNumber.", aValue);
        BigDecimal lSize = cvtNumber((JSONNumber) aValue);

        // If there are length specs, we check them.
        if(minValue != null)
        {
            if(lSize.compareTo(minValue) < 0) fail("The size (" + lSize +") is smaller then allowed (" + minValue + ").", aValue);
        }
        if(maxValue != null)
        {
            if(lSize.compareTo(maxValue) > 0 ) fail("The size (" + lSize +") is larger then allowed (" + maxValue + ").", aValue);
        }
    }

    private BigDecimal cvtNumber(JSONNumber aNum)
    {
        if (aNum.isInteger()) return new BigDecimal(((JSONInteger) aNum).getValue());
        else return ((JSONDecimal) aNum).getValue();
    }
}
