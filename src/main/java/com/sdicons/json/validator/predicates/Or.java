/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.validator.predicates;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.sdicons.json.model.JSONArray;
import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONValue;
import com.sdicons.json.validator.ValidationException;
import com.sdicons.json.validator.Validator;
import com.sdicons.json.validator.ValidatorUtil;

/**
 * This predicate represents the logical OR combination of a number of other predicates.
 * At least one of the nested rules has to succeed for this rule to succeed.
 *
 */
public class Or
extends Predicate
{
    private List<Validator> rules = new LinkedList<Validator>();

    public Or(String aName, JSONObject aRule, HashMap<String,Validator> aRuleset)
    throws ValidationException
    {
        super(aName);
        ValidatorUtil.requiresAttribute(aRule, ValidatorUtil.PARAM_RULES, JSONArray.class);

        List<JSONValue> lRules = ((JSONArray) aRule.get(ValidatorUtil.PARAM_RULES)).getValue();
        for (JSONValue lRule : lRules)
        {
            Validator lValidator = ValidatorUtil.buildValidator(lRule, aRuleset);
            rules.add(lValidator);
        }
    }

    public Or(String aName, Validator ... validators) {
        super(aName);
        rules.addAll(Arrays.asList(validators));
    }

    public void validate(JSONValue aValue)
    throws ValidationException
    {
        for (Validator rule1 : rules)
        {
            try
            {
                rule1.validate(aValue);
                // If we get here, the current validator succeeded.
                // We only need a single success!
                return;
            }
            catch (ValidationException e)
            {
                // This rule failed. Ignore for the time being.
            }
        }
        // If we get here, then all rules failed.
        // If all rules fail, we fail as well.
        fail("All or rules failed.", aValue);
    }
}
