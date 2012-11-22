/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.validator.impl.predicates;

import com.sdicons.json.validator.ValidationException;
import com.sdicons.json.validator.Validator;
import com.sdicons.json.validator.impl.ValidatorUtil;
import com.sdicons.json.model.JSONValue;
import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONArray;

import java.util.List;
import java.util.LinkedList;
import java.util.HashMap;

public class And
extends Predicate
{
    private List<Validator> rules = new LinkedList<Validator>();

    public And(String aName, JSONObject aRule, HashMap<String,Validator> aRuleset)
    throws ValidationException
    {
        super(aName, aRule);
        ValidatorUtil.requiresAttribute(aRule, ValidatorUtil.PARAM_RULES, JSONArray.class);

        List<JSONValue> lRules = ((JSONArray) aRule.get(ValidatorUtil.PARAM_RULES)).getValue();
        for (JSONValue lRule : lRules)
        {
            Validator lValidator = ValidatorUtil.buildValidator(lRule, aRuleset);
            rules.add(lValidator);
        }
    }

    public void validate(JSONValue aValue)
    throws ValidationException
    {
        for (Validator rule1 : rules)
        {
            rule1.validate(aValue);
        }
    }
}
