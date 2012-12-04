/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.validator.impl.predicates;

import com.sdicons.json.model.JSONObject;
import com.sdicons.json.validator.Validator;

import java.util.HashMap;

/**
 * The super class of all custom Java validators. You can implement your
 * own Java validator by deriving from this class and creating a CustomPredicate.
 * 
 * @see CustomPredicate
 */
public abstract class CustomValidator
extends Predicate
{
    private HashMap<String,Validator> ruleset;

    protected CustomValidator(String aName, JSONObject aRule, HashMap<String, Validator> aRuleset)
    {
        super(aName, aRule);
        ruleset = aRuleset;
    }

    protected HashMap<String, Validator> getRuleset()
    {
        return ruleset;
    }
}
