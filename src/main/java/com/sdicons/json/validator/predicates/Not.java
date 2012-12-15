/*******************************************************************************
 * Copyright (c) 2006-2013 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.validator.predicates;

import java.util.HashMap;

import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONValue;
import com.sdicons.json.validator.ValidationException;
import com.sdicons.json.validator.Validator;
import com.sdicons.json.validator.ValidatorUtil;

/**
 * This predicate is a negation of another one.
 *
 */
public class Not
extends Predicate
{
    private static final String NOT001 = "JSONValidator/Not/001: This rule '%s'fails because internal rule succeeds on value '%s'.";
    private Validator rule;

    public Not(String aName, JSONObject aRule, HashMap<String,Validator> aRuleset)
    throws ValidationException
    {
        super(aName);
        ValidatorUtil.requiresAttribute(aRule, ValidatorUtil.PARAM_RULE, JSONObject.class);
        rule = ValidatorUtil.buildValidator(aRule.get(ValidatorUtil.PARAM_RULE), aRuleset);
    }

    public Not(String aName, Validator rule) {
        super(aName);
        this.rule = rule;
    }

    public void validate(JSONValue aValue)
    throws ValidationException
    {
        try
        {
            rule.validate(aValue);
        }
        catch (ValidationException e)
        {
            // It is ok if the inner validation failed.
            // We undo that failure by returning.
            return;
        }
        // If we get here, then the inner validation succeeded.
        // In that case we fail by raising an exception.
        throw new ValidationException(String.format(NOT001, this.getName(), aValue.toString()));
    }
}
