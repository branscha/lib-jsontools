/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.validator.predicates;

import java.util.HashMap;
import java.util.Iterator;

import com.sdicons.json.model.JSONArray;
import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONValue;
import com.sdicons.json.validator.ValidationException;
import com.sdicons.json.validator.Validator;
import com.sdicons.json.validator.ValidatorUtil;

/**
 * A predicate that checks if all elements of a complex JSON structure JSONObject or
 * JSONArray comply to another named predicate.
 * It is a predicate to put a restriction on the elements of a JSON object.
 * <p>
 * An example of a content validator. The JSONValue must be an array and it must contain integers.
 *
 * <pre>
 * <code>
 * {
 *    "name" :"List of integers",
 *    "type" :"and",
 *    "rules" : [{"type":"array"},{"type":"content","rule":{"type":"int"}}]
 * }
 * </code>
 * </pre>
 *
 */
public class Content
extends Predicate
{
    private Validator rule;

    public Content(String aName, JSONObject aRule, HashMap<String, Validator> aRuleset)
    throws ValidationException
    {
        super(aName, aRule);

        rule = new True(aName, aRule);
        if (aRule.containsKey(ValidatorUtil.PARAM_RULE))
        {
            ValidatorUtil.requiresAttribute(aRule, ValidatorUtil.PARAM_RULE, JSONObject.class);
            rule = ValidatorUtil.buildValidator(aRule.get(ValidatorUtil.PARAM_RULE), aRuleset);
        }
    }

    public void validate(JSONValue aValue)
    throws ValidationException
    {
        Iterator<JSONValue> lIter = null;
        if(aValue.isArray()) lIter = ((JSONArray) aValue).getValue().iterator();
        else if(aValue.isObject()) lIter = ((JSONObject) aValue).getValue().values().iterator();
        else fail("The value is not a JSONComplex, it has no content.", aValue);

        // Finally we apply the internal rule to all elements.
        if(lIter != null)
        {
            while(lIter.hasNext())
            {
                JSONValue lVal = (JSONValue) lIter.next();
                rule.validate(lVal);
            }
        }
    }
}
