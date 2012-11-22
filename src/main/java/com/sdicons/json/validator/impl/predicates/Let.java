/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.validator.impl.predicates;

import com.sdicons.json.validator.Validator;
import com.sdicons.json.validator.ValidationException;
import com.sdicons.json.validator.impl.ValidatorUtil;
import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONArray;
import com.sdicons.json.model.JSONValue;
import com.sdicons.json.model.JSONString;

import java.util.List;
import java.util.HashMap;

public class Let
extends Predicate
{
    private HashMap<String, Validator> ruleset;
    private String ref;

    public Let(String aName, JSONObject aRule, HashMap<String,Validator> aRuleset)
    throws ValidationException
    {
        super(aName, aRule);

        ruleset = aRuleset;
        ValidatorUtil.requiresAttribute(aRule, ValidatorUtil.PARAM_RULES, JSONArray.class);
        List<JSONValue> lRules = ((JSONArray) aRule.get(ValidatorUtil.PARAM_RULES)).getValue();
        for (JSONValue lRule : lRules)
        {
            JSONValue lVal = lRule;
            ValidatorUtil.buildValidator(lVal, ruleset);
        }

        ValidatorUtil.requiresAttribute(aRule, ValidatorUtil.PARAM_REF, JSONString.class);
        ref = ((JSONString) aRule.get(ValidatorUtil.PARAM_REF)).getValue();
    }

    public void validate(JSONValue aValue)
    throws ValidationException
    {
        if(ruleset.containsKey(ref))
        {
            Validator lValidator = ruleset.get(ref);
            lValidator.validate(aValue);
        }
        else fail("Reference to an unexisting rule: \"" + ref + "\"." ,aValue);
    }
}
