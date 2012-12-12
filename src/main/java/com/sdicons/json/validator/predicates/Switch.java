/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.validator.predicates;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.sdicons.json.model.JSONArray;
import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.model.JSONValue;
import com.sdicons.json.validator.ValidationException;
import com.sdicons.json.validator.Validator;
import com.sdicons.json.validator.ValidatorUtil;

/**
 * The switch validator is a convenience one. It is a subset of the or validator, but the problem with the or validator is
 * that it does a bad job for error reporting when things go wrong.
 * The reason is that all rules fail and it is not always clear why, because the reason a rule fails might be some levels deeper.
 * The switch validator selects a validator based on the value of  a property encountered in the value being validated.
 * The error produced will be the one of the selected validator.  The first applicable validator is used, the following ones are ignored.
 *  <p>
 *  <pre>
 *  <code>
 *  {
 *     "name" :"Switch test",
 *     "type" :"switch",
 *     "key":"discriminator",
 *     "case" : [{"values":["a", "b", "c", 1, 2, 3], "rule":{"type":"true"}}]
 * }
 *  </code>
 *  </pre>
 */
public class Switch
extends Predicate
{
    private List<SwitchCase> rules = new LinkedList<SwitchCase>();
    private String key;

    public static class SwitchCase
    {
        private List<JSONValue> values;
        private Validator validator;

        public SwitchCase(List<JSONValue> when, Validator validator)
        {
            this.validator = validator;
            this.values = when;
        }
        
        public SwitchCase(JSONValue when, Validator validator)
        {
            this.values = new ArrayList<JSONValue>();
            this.values.add(when);
            this.validator = validator;
        }

        public Validator getValidator()
        {
            return validator;
        }

        public boolean isApplicable(JSONValue aVal)
        {
            return values.contains(aVal);
        }
    }
    
    public Switch(String aName, String discriminator, HashMap<String,Validator> aRuleset, SwitchCase ... cases) {
        super(aName);
        this.key = discriminator;
        for(SwitchCase cas : cases){
            rules.add(cas);
        }
    }

    public Switch(String aName, JSONObject aRule, HashMap<String,Validator> aRuleset)
    throws ValidationException
    {
        super(aName);

        ValidatorUtil.requiresAttribute(aRule, ValidatorUtil.PARAM_CASE, JSONArray.class);
        List<JSONValue> lCases = ((JSONArray) aRule.get(ValidatorUtil.PARAM_CASE)).getValue();
        for (JSONValue lCase : lCases)
        {
            if(!lCase.isObject()) fail("A case in a swicht should be an object type.", lCase);
            JSONObject lObjCase = (JSONObject) lCase;

            ValidatorUtil.requiresAttribute(lObjCase, ValidatorUtil.PARAM_RULE, JSONObject.class);
            JSONObject lRule = (JSONObject) lObjCase.get(ValidatorUtil.PARAM_RULE);
            Validator lValidator = ValidatorUtil.buildValidator(lRule, aRuleset);

            ValidatorUtil.requiresAttribute(lObjCase, ValidatorUtil.PARAM_VALUES, JSONArray.class);
            JSONArray lVals = (JSONArray) lObjCase.get(ValidatorUtil.PARAM_VALUES);

            SwitchCase lNewCase = new SwitchCase(lVals.getValue(), lValidator);
            rules.add(lNewCase);
        }

        ValidatorUtil.requiresAttribute(aRule, ValidatorUtil.PARAM_KEY, JSONString.class);
        key = ((JSONString) aRule.get(ValidatorUtil.PARAM_KEY)).getValue();
    }

    public void validate(JSONValue aValue)
    throws ValidationException
    {
        if(!aValue.isObject()) fail("The value is not a JSONObject.", aValue);
        JSONObject lObj = (JSONObject) aValue;

        if(!lObj.containsKey(key)) fail("The object does not contain the key: \"" + key + "\".", lObj);
        JSONValue lVal = lObj.get(key);

        for (SwitchCase aCase : rules)
        {
            if(aCase.isApplicable(lVal))
            {
                aCase.getValidator().validate(lObj);
                return;
            }
        }
        fail("No applicable rule found for key: \"" + key + "\", value: " + lVal.toString(), aValue);
    }
}
