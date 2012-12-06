/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.validator;

import java.util.HashMap;

import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.model.JSONValue;
import com.sdicons.json.validator.predicates.And;
import com.sdicons.json.validator.predicates.Array;
import com.sdicons.json.validator.predicates.Bool;
import com.sdicons.json.validator.predicates.Complex;
import com.sdicons.json.validator.predicates.Content;
import com.sdicons.json.validator.predicates.CustomPredicate;
import com.sdicons.json.validator.predicates.Decimal;
import com.sdicons.json.validator.predicates.Enumeration;
import com.sdicons.json.validator.predicates.False;
import com.sdicons.json.validator.predicates.Int;
import com.sdicons.json.validator.predicates.Length;
import com.sdicons.json.validator.predicates.Let;
import com.sdicons.json.validator.predicates.Not;
import com.sdicons.json.validator.predicates.Nr;
import com.sdicons.json.validator.predicates.Null;
import com.sdicons.json.validator.predicates.Object;
import com.sdicons.json.validator.predicates.Or;
import com.sdicons.json.validator.predicates.Properties;
import com.sdicons.json.validator.predicates.Range;
import com.sdicons.json.validator.predicates.Ref;
import com.sdicons.json.validator.predicates.Regexp;
import com.sdicons.json.validator.predicates.Simple;
import com.sdicons.json.validator.predicates.Str;
import com.sdicons.json.validator.predicates.Switch;
import com.sdicons.json.validator.predicates.True;

/**
 * Utility functions used by the {@link JSONValidator}.
 */
public class ValidatorUtil
{
    private ValidatorUtil() {
        // Prevent the utility class from being instantiated.
    }
    
    public static final String PARAM_NAME = "name";
    public static final String PARAM_TYPE = "type";
    public static final String PARAM_RULES = "rules";
    public static final String PARAM_RULE = "rule";
    public static final String PARAM_REF = "*";
    public static final String PARAM_MIN = "min";
    public static final String PARAM_MAX = "max";
    public static final String PARAM_PAIRS = "pairs";
    public static final String PARAM_KEY = "key";
    public static final String PARAM_OPTIONAL = "optional";
    public static final String PARAM_PATTERN="pattern";
    public static final String PARAM_VALUES="values";
    public static final String PARAM_CLASS="class";
    public static final String PARAM_CASE="case";

    public static final String ANONYMOUS_RULE = "[anonymous rule]";

    public static JSONObject createRule(String name, String type) {
        JSONObject rule = new JSONObject();
        rule.getValue().put(PARAM_NAME, new JSONString(name));
        rule.getValue().put(PARAM_TYPE, new JSONString(type));
        return rule;
    }

    public static void requiresAttribute(JSONObject aTarget, String aAttrib, Class<?> aValueType)
    throws ValidationException
    {
        if(!aTarget.containsKey(aAttrib))
        {
            final String lMsg = "Attribute not present: \"" + aAttrib + "\"";
            throw new ValidationException(lMsg, aTarget, "MISSING ATTRIBUTE");
        }

        if (!(aValueType.isInstance(aTarget.get(aAttrib))))
        {
            final String lMsg = "Expected other type: \"" + aValueType.getName() + "\"";
            throw new ValidationException(lMsg, aTarget, "UNEXPECTED TYPE");
        }
    }

    public static Validator buildValidator(JSONValue aVal)
    throws ValidationException
    {
        return buildValidator(aVal, new HashMap<String, Validator>());
    }

    public static Validator buildValidator(JSONValue aVal, HashMap<String, Validator> aRuleset)
    throws ValidationException
    {
        if(! aVal.isObject())
        {
            final String lMsg = "A rule should have object type.";
            throw new ValidationException(lMsg, aVal, "OBJECT REQUIRED");
        }

        JSONObject lRule = (JSONObject) aVal;
        //ValidatorUtil.requiresAttribute(lRule, PARAM_NAME, JSONString.class);
        ValidatorUtil.requiresAttribute(lRule, PARAM_TYPE, JSONString.class);

        String lRuleName = ANONYMOUS_RULE;
        if(lRule.containsKey(PARAM_NAME) && lRule.get(PARAM_NAME).isString())
            lRuleName = ((JSONString) lRule.get(PARAM_NAME)).getValue();

        final String lRuleType = ((JSONString) lRule.get(PARAM_TYPE)).getValue();
        Validator lNewValidator = null;

        if("true".equals(lRuleType))        lNewValidator = new True(lRuleName, lRule);
        else if("false".equals(lRuleType))  lNewValidator = new False(lRuleName, lRule);
        else if("or".equals(lRuleType))     lNewValidator = new Or(lRuleName, lRule, aRuleset);
        else if("and".equals(lRuleType))    lNewValidator = new And(lRuleName, lRule, aRuleset);
        else if("not".equals(lRuleType))    lNewValidator = new Not(lRuleName, lRule, aRuleset);
        else if("ref".equals(lRuleType))    lNewValidator = new Ref(lRuleName, lRule, aRuleset);
        else if("complex".equals(lRuleType))lNewValidator = new Complex(lRuleName, lRule);
        else if("array".equals(lRuleType))  lNewValidator = new Array(lRuleName, lRule);
        else if("object".equals(lRuleType)) lNewValidator = new Object(lRuleName, lRule);
        else if("simple".equals(lRuleType)) lNewValidator = new Simple(lRuleName, lRule);
        else if("null".equals(lRuleType))   lNewValidator = new Null(lRuleName, lRule);
        else if("bool".equals(lRuleType))   lNewValidator = new Bool(lRuleName);
        else if("string".equals(lRuleType)) lNewValidator = new Str(lRuleName, lRule);
        else if("number".equals(lRuleType)) lNewValidator = new Nr(lRuleName, lRule);
        else if("int".equals(lRuleType))    lNewValidator = new Int(lRuleName, lRule);
        else if("decimal".equals(lRuleType))lNewValidator = new Decimal(lRuleName, lRule);
        else if("length".equals(lRuleType)) lNewValidator = new Length(lRuleName, lRule);
        else if("content".equals(lRuleType))lNewValidator = new Content(lRuleName, lRule, aRuleset);
        else if("properties".equals(lRuleType))lNewValidator = new Properties(lRuleName, lRule, aRuleset);
        else if("regexp".equals(lRuleType)) lNewValidator = new Regexp(lRuleName, lRule);
        else if("enum".equals(lRuleType)) lNewValidator = new Enumeration(lRuleName, lRule);
        else if("range".equals(lRuleType)) lNewValidator = new Range(lRuleName, lRule);
        else if("let".equals(lRuleType)) lNewValidator = new Let(lRuleName, lRule, aRuleset);
        else if("custom".equals(lRuleType)) lNewValidator = new CustomPredicate(lRuleName, lRule, aRuleset);
        else if("switch".equals(lRuleType)) lNewValidator = new Switch(lRuleName, lRule, aRuleset);
        else
        {
            final String lMsg = "Unknown validator type: \""  + lRuleType + "\" for rule: \""  + lRuleName + "\"";
            throw new ValidationException(lMsg, lRule, "UNKNOWN VALIDATION TYPE");
        }

        // You cannot refer to anonymous rules. It would leave the door
        // open for twisted rules relying on this feature.
        if(lRuleName != ANONYMOUS_RULE) aRuleset.put(lRuleName, lNewValidator);
        return lNewValidator;
    }
}
