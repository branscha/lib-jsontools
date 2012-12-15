/*******************************************************************************
 * Copyright (c) 2006-2013 Bruno Ranschaert
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
    private static final String VAL001 = "JSONValidator/001: Attribute '%s'not present in '%s'.";
    private static final String VAL002 = "JSONValidator/002: Expected type '%s' but received value '%s'.";
    private static final String VAL003 = "JSONValidator/003: Expected a JSONObject but received value '%s'.";
    private static final String VAL004 = "JSONValidator/004: Unknown validator type '%s' in rule '%s'; complete rule '%s'.";

    private ValidatorUtil() {
        // Prevent the utility class from being instantiated.
    }

    public static final String TYPE_SWITCH = "switch";
    public static final String TYPE_CUSTOM = "custom";
    public static final String TYPE_LET = "let";
    public static final String TYPE_RANGE = "range";
    public static final String TYPE_ENUM = "enum";
    public static final String TYPE_REGEXP = "regexp";
    public static final String TYPE_PROPERTIES = "properties";
    public static final String TYPE_CONTENT = "content";
    public static final String TYPE_LENGTH = "length";
    public static final String TYPE_DECIMAL = "decimal";
    public static final String TYPE_INT = "int";
    public static final String TYPE_COMPLEX = "complex";
    public static final String TYPE_NUMBER = "number";
    public static final String TYPE_STRING = "string";
    public static final String TYPE_BOOL = "bool";
    public static final String TYPE_NULL = "null";
    public static final String TYPE_SIMPLE = "simple";
    public static final String TYPE_OBJECT = "object";
    public static final String TYPE_ARRAY = "array";
    public static final String TYPE_REF = "ref";
    public static final String TYPE_NOT = "not";
    public static final String TYPE_AND = "and";
    public static final String TYPE_OR = "or";
    public static final String TYPE_FALSE = "false";
    public static final String TYPE_TRUE = "true";
    //
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
            throw new ValidationException(String.format(VAL001, aAttrib, aTarget.toString()));
        }

        if (!(aValueType.isInstance(aTarget.get(aAttrib))))
        {
            throw new ValidationException(String.format(VAL002, aValueType.getName(), aTarget.toString()));
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
            throw new ValidationException(String.format(VAL003, aVal.toString()));
        }

        JSONObject lRule = (JSONObject) aVal;
        //ValidatorUtil.requiresAttribute(lRule, PARAM_NAME, JSONString.class);
        ValidatorUtil.requiresAttribute(lRule, PARAM_TYPE, JSONString.class);

        String lRuleName = ANONYMOUS_RULE;
        if(lRule.containsKey(PARAM_NAME) && lRule.get(PARAM_NAME).isString())
            lRuleName = ((JSONString) lRule.get(PARAM_NAME)).getValue();

        final String lRuleType = ((JSONString) lRule.get(PARAM_TYPE)).getValue();
        Validator lNewValidator = null;

        if(TYPE_TRUE.equals(lRuleType))        lNewValidator = new True(lRuleName);
        else if(TYPE_FALSE.equals(lRuleType))  lNewValidator = new False(lRuleName);
        else if(TYPE_OR.equals(lRuleType))     lNewValidator = new Or(lRuleName, lRule, aRuleset);
        else if(TYPE_AND.equals(lRuleType))    lNewValidator = new And(lRuleName, lRule, aRuleset);
        else if(TYPE_NOT.equals(lRuleType))    lNewValidator = new Not(lRuleName, lRule, aRuleset);
        else if(TYPE_REF.equals(lRuleType))    lNewValidator = new Ref(lRuleName, lRule, aRuleset);
        else if(TYPE_COMPLEX.equals(lRuleType))lNewValidator = new Complex(lRuleName);
        else if(TYPE_ARRAY.equals(lRuleType))  lNewValidator = new Array(lRuleName);
        else if(TYPE_OBJECT.equals(lRuleType)) lNewValidator = new Object(lRuleName);
        else if(TYPE_SIMPLE.equals(lRuleType)) lNewValidator = new Simple(lRuleName);
        else if(TYPE_NULL.equals(lRuleType))   lNewValidator = new Null(lRuleName);
        else if(TYPE_BOOL.equals(lRuleType))   lNewValidator = new Bool(lRuleName);
        else if(TYPE_STRING.equals(lRuleType)) lNewValidator = new Str(lRuleName);
        else if(TYPE_NUMBER.equals(lRuleType)) lNewValidator = new Nr(lRuleName);
        else if(TYPE_INT.equals(lRuleType))    lNewValidator = new Int(lRuleName);
        else if(TYPE_DECIMAL.equals(lRuleType))lNewValidator = new Decimal(lRuleName);
        else if(TYPE_LENGTH.equals(lRuleType)) lNewValidator = new Length(lRuleName, lRule);
        else if(TYPE_CONTENT.equals(lRuleType))lNewValidator = new Content(lRuleName, lRule, aRuleset);
        else if(TYPE_PROPERTIES.equals(lRuleType))lNewValidator = new Properties(lRuleName, lRule, aRuleset);
        else if(TYPE_REGEXP.equals(lRuleType)) lNewValidator = new Regexp(lRuleName, lRule);
        else if(TYPE_ENUM.equals(lRuleType)) lNewValidator = new Enumeration(lRuleName, lRule);
        else if(TYPE_RANGE.equals(lRuleType)) lNewValidator = new Range(lRuleName, lRule);
        else if(TYPE_LET.equals(lRuleType)) lNewValidator = new Let(lRuleName, lRule, aRuleset);
        else if(TYPE_CUSTOM.equals(lRuleType)) lNewValidator = new CustomPredicate(lRuleName, lRule, aRuleset);
        else if(TYPE_SWITCH.equals(lRuleType)) lNewValidator = new Switch(lRuleName, lRule, aRuleset);
        else
        {
            throw new ValidationException(String.format(VAL004, lRuleType, lRuleName, lRule.toString()));
        }

        // You cannot refer to anonymous rules. It would leave the door
        // open for twisted rules relying on this feature.
        if(lRuleName != ANONYMOUS_RULE) aRuleset.put(lRuleName, lNewValidator);
        return lNewValidator;
    }
}
