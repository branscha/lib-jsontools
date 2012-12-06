/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.validator.predicates;

import com.sdicons.json.model.JSONArray;
import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONValue;
import com.sdicons.json.validator.ValidationException;
import com.sdicons.json.validator.ValidatorUtil;

import java.util.List;

/**
 * This predicate checks if the JSON value is one of the set of values
 * specified by this rule.
 */
public class Enumeration
extends Predicate
{
    private List<JSONValue> enumValues;

    public Enumeration(String aName, JSONObject aRule)
    throws ValidationException
    {
        super(aName);

       ValidatorUtil.requiresAttribute(aRule, ValidatorUtil.PARAM_VALUES, JSONArray.class);
       enumValues = ((JSONArray) aRule.get(ValidatorUtil.PARAM_VALUES)).getValue();
    }

    public void validate(JSONValue aValue)
    throws ValidationException
    {
        if(!enumValues.contains(aValue)) fail("The enumeration does not contain the value.", aValue);
    }
}
