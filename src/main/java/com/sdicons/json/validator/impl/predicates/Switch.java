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
import java.util.LinkedList;
import java.util.HashMap;

public class Switch
extends Predicate
{
    private List<Case> rules = new LinkedList<Case>();
    private String key;

    private static class Case
    {
        private List<JSONValue> values;
        private Validator validator;

        public Case(Validator validator, List<JSONValue> values)
        {
            this.validator = validator;
            this.values = values;
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

    public Switch(String aName, JSONObject aRule, HashMap<String,Validator> aRuleset)
    throws ValidationException
    {
        super(aName, aRule);

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

            Case lNewCase = new Case(lValidator, lVals.getValue());
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

        for (Case aCase : rules)
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
