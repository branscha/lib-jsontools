/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.validator.predicates;

import com.sdicons.json.validator.ValidationException;
import com.sdicons.json.validator.Validator;
import com.sdicons.json.validator.ValidatorUtil;
import com.sdicons.json.model.JSONValue;
import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONArray;

import java.util.List;
import java.util.LinkedList;
import java.util.HashMap;

/**
 * This predicate represents the logical AND combination of a number of other predicates.
 * If one of the predicates fails the evaluation of the predicate will fail immediately.
 */
public class And
extends Predicate
{
    private List<Validator> rules = new LinkedList<Validator>();

    /**
     * Create the AND predicate.
     * 
     * @param aName
     *        The name of the rule. If the rule fails we can find it back using this name.
     * @param aRule
     *        The JSON object that contains information about this rule.
     * @param aRuleset
     *        The set of named validation rules that were already encountered, they can 
     *        be used recursively.
     * @throws ValidationException
     */
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

    /**
     * Execute the And predicate.
     */
    public void validate(JSONValue aValue)
    throws ValidationException
    {
        for (Validator rule1 : rules)
        {
            rule1.validate(aValue);
        }
    }
}
