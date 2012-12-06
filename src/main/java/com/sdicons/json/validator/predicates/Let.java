/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.validator.predicates;

import java.util.HashMap;
import java.util.List;

import com.sdicons.json.model.JSONArray;
import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.model.JSONValue;
import com.sdicons.json.validator.ValidationException;
import com.sdicons.json.validator.Validator;
import com.sdicons.json.validator.ValidatorUtil;

/**
 * It is a convenience rule that lets you specify a list of global shared
 * validation rules in advance before using these later on. It becomes possible
 * to first define a number of recurring types and then give the starting point.
 * It is a utility rule that lets you tackle more complex validations. Note that
 * it makes no sense to define anonymous rules inside the list, it is impossible
 * to refer to these later on.
 * <p>
 * In this example we create a rule that matches a's or b's but not a mix of the
 * two. In order to make the definition of our predicate a bit easier we make
 * use of named sub rules.
 *
 * <pre>
 *  <code>
 * {
 *   "name" :"Let test -  a's or b's",
 *   "type" :"let",
 *   "*" : "start",
 *   "rules" : [{"name":"start", "type":"or", "rules":[{"type":"ref", "*":"a"}, {"type":"ref", "*":"b"}]},
 *              {"name":"a", "type":"regexp", "pattern":"a*"},
 *              {"name":"b", "type":"regexp", "pattern":"b*"}
 *             ]
 * }
 * </code>
 * </pre>
 *
 */
public class Let
extends Predicate
{
    private HashMap<String, Validator> ruleset;
    private String ref;

    public Let(String aName, JSONObject aRule, HashMap<String,Validator> aRuleset)
    throws ValidationException
    {
        super(aName);

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
