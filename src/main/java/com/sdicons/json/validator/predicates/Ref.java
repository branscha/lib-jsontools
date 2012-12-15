/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.validator.predicates;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.model.JSONValue;
import com.sdicons.json.validator.ValidationException;
import com.sdicons.json.validator.Validator;
import com.sdicons.json.validator.ValidatorUtil;

/**
 * A rule that uses a named rule that was defined earlier. This feature lets you define
 * rules first and then use these rules in more complex constructs.
 * Note that the rule name has to exist at validation time, not at creation time.
 * <p>
 * <pre>
 * <code>
 * {"type":"ref", "*" : "OTHER-RULE" }
 * </code>
 * </pre>
 */
public class Ref
extends Predicate
{
    private static final String REF001 = "JSONValidator/Ref/001: Cannot resolve reference '%s' in rule '%s'.";
    private Map<String, Validator> ruleset;
    private String ref;

    public Ref(String aName, JSONObject aRule, HashMap<String,Validator> aRuleset)
    throws ValidationException
    {
        super(aName);
        ruleset = aRuleset;

        ValidatorUtil.requiresAttribute(aRule, ValidatorUtil.PARAM_REF, JSONString.class);
        ref = ((JSONString) aRule.get(ValidatorUtil.PARAM_REF)).getValue();
    }

    public Ref(String name, String ref, Map<String, Validator> table) {
        super(name);
        this.ref = ref;
        this.ruleset = Collections.unmodifiableMap(table);
    }

    public void validate(JSONValue aValue)
    throws ValidationException
    {
        if(ruleset.containsKey(ref))
        {
            Validator lValidator = ruleset.get(ref);
            lValidator.validate(aValue);
        }
        else throw new ValidationException(String.format(REF001, ref, this.getName()));
    }
}
