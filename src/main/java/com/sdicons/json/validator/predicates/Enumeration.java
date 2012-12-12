/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.validator.predicates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sdicons.json.model.JSONArray;
import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONValue;
import com.sdicons.json.validator.ValidationException;
import com.sdicons.json.validator.ValidatorUtil;

/**
 * This predicate checks if the JSON value is one of the set of values
 * specified by this rule.
 */
public class Enumeration
extends Predicate
{
    private static final String ENUM001 = "JSONValidator/Enumeration/001: The enumeration does not contain the value '%s' in rule '%s'.";

    private List<JSONValue> enumValues;

    public Enumeration(String aName, JSONObject aRule)
    throws ValidationException
    {
        super(aName);

       ValidatorUtil.requiresAttribute(aRule, ValidatorUtil.PARAM_VALUES, JSONArray.class);
       enumValues = ((JSONArray) aRule.get(ValidatorUtil.PARAM_VALUES)).getValue();
    }

    public Enumeration(String aName, JSONValue ... values) {
        super(aName);
        enumValues = new ArrayList<JSONValue>(Arrays.asList(values));
    }

    public void validate(JSONValue aValue)
    throws ValidationException
    {
        if(!enumValues.contains(aValue))
            throw new ValidationException(String.format(ENUM001, aValue.toString(), this.getName()));
    }
}
