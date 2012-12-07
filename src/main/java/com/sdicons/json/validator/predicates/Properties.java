/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.validator.predicates;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.sdicons.json.model.JSONArray;
import com.sdicons.json.model.JSONBoolean;
import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.model.JSONValue;
import com.sdicons.json.validator.ValidationException;
import com.sdicons.json.validator.Validator;
import com.sdicons.json.validator.ValidatorUtil;

/**
 * Predicate to subject the properties of a {@link JSONObject} to some tests.
 * <ul>
 *    <li>We can indicate if a property is optional or not.</li>
 *    <li>we can subject each property to a separate validator rule.</li>
 * </ul>
 * <p>
 * Example
 *
 * <pre>
 * <code>
 * {
 *    "name" :"Contact spec.",
 *    "type" :"properties",
 *    "pairs" : [{"key":"name", "optional":false, "rule":{"type":"string"}},
 *               {"key":"country", "optional":false, "rule":{"type":"string"}},
 *               {"key":"salary", "optional":true, "rule":{"type":"decimal"}}
 *              ]
 * }
 * </code>
 * </pre>
 */
public class Properties
extends Predicate
{
    public static class PropRule
    {
        private String key;
        private Validator rule;
        private boolean optional;

        public PropRule(String key, Validator rule, boolean optional)
        {
            this.key = key;
            this.rule = rule;
        }

        public String getKey()
        {
            return key;
        }

        public Validator getRule()
        {
            return rule;
        }
        
        public boolean isOptional() {
            return optional;
        }
    }

    private List<PropRule> required = new LinkedList<PropRule>();
    private HashMap<String, PropRule> all = new HashMap<String, PropRule>();

    public Properties(String aName, HashMap<String,Validator> aRuleset, PropRule ...rules) {
        super(aName);
        for(PropRule rule: rules) {
            all.put(rule.getKey(), rule);
            if(!rule.isOptional()) required.add(rule);
        }
    }
    
    public Properties(String aName, JSONObject aRule, HashMap<String,Validator> aRuleset)
    throws ValidationException
    {
        super(aName);
        ValidatorUtil.requiresAttribute(aRule, ValidatorUtil.PARAM_PAIRS, JSONArray.class);

        List<JSONValue> lPairs = ((JSONArray) aRule.get(ValidatorUtil.PARAM_PAIRS)).getValue();
        for (JSONValue lPair : lPairs)
        {
            if(!lPair.isObject())
            {
                final String lMsg = "A pair should be described by a JSONObject.";
                throw new ValidationException(lMsg, lPair, "JSONObject EXPECTED");
            }

            final JSONObject lObj = (JSONObject) lPair;

            ValidatorUtil.requiresAttribute(lObj, ValidatorUtil.PARAM_KEY, JSONString.class);
            final String lKeyname = ((JSONString) lObj.get(ValidatorUtil.PARAM_KEY)).getValue();

            Validator lValrule = new True(ValidatorUtil.ANONYMOUS_RULE);
            if (lObj.containsKey(ValidatorUtil.PARAM_RULE))
            {
                ValidatorUtil.requiresAttribute(lObj, ValidatorUtil.PARAM_RULE, JSONObject.class);
                lValrule = ValidatorUtil.buildValidator(lObj.get(ValidatorUtil.PARAM_RULE), aRuleset);
            }

            boolean lOptional = false;
            if (lObj.containsKey(ValidatorUtil.PARAM_OPTIONAL))
            {
                ValidatorUtil.requiresAttribute(lObj, ValidatorUtil.PARAM_OPTIONAL, JSONBoolean.class);
                lOptional = ((JSONBoolean) lObj.get(ValidatorUtil.PARAM_OPTIONAL)).getValue();
            }

            PropRule lRule = new PropRule(lKeyname, lValrule, lOptional);
            all.put(lKeyname, lRule);
            if(!lOptional) required.add(lRule);
        }
    }

    public void validate(JSONValue aValue)
    throws ValidationException
    {
        // Only for objects.
        if(!aValue.isObject()) fail("The value is not a JSONObject.", aValue);
        JSONObject lObj = (JSONObject) aValue;

        // First we check if required keys are there.
        for(PropRule aRequired : required)
        {
            if(!lObj.containsKey(aRequired.getKey())) fail("The object lacks a required key: \"" + aRequired.getKey() + "\".", aValue);
        }

        // Now we iterate over all keys in the object and lookup the spec.
        for(String lKey : lObj.getValue().keySet())
        {
            if(!all.containsKey(lKey)) fail("The object contains an unspecified key: \"" + lKey + "\".", aValue);
            PropRule lRule = all.get(lKey);
            try
            {
                lRule.getRule().validate(lObj.get(lKey));
            }
            catch(ValidationException e)
            {
                fail("The object property: \"" + lKey + "\" has invalid content. Internal message: " + e.getMessage(), aValue);
            }
        }
    }
}
