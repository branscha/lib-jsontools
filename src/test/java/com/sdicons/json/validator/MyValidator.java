/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.validator;

import com.sdicons.json.validator.impl.predicates.CustomValidator;
import com.sdicons.json.model.JSONValue;
import com.sdicons.json.model.JSONObject;

import java.util.HashMap;

public class MyValidator
extends CustomValidator
{
    public MyValidator(String aName, JSONObject aRule, HashMap<String, Validator> aRuleset)
    {
        super(aName, aRule, aRuleset);
    }

    public void validate(JSONValue aValue) throws ValidationException
    {
        System.out.println("**** CUSTOM VALIDATOR *****");
    }
}
