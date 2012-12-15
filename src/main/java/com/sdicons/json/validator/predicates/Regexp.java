/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.validator.predicates;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.model.JSONValue;
import com.sdicons.json.validator.ValidationException;
import com.sdicons.json.validator.ValidatorUtil;


/**
 * Test if a {@link JSONString}  matches a regexp pattern.
 * <p>
 * <pre>
 * <code>
 * { "name" :"A-B-C validator",
 *   "type" :"regexp",
 *   "pattern" : "a*b*c*" }
 * </code>
 * </pre>
 *
 */
public class Regexp
extends Predicate
{
    private static final String REG001 = "JSONValidator/Regexp/001: Error while compiling the regexp pattern '%s' in rule '%s'.";
    private static final String REG002 = "JSONValidator/Regexp/002: The value '%s' is not a JSONString in rule '%s'.";
    private static final String REG003 = "JSONValidator/Regexp/003: The string value '%s' does not match pattern '%s' in rule '%s'";
    private Pattern pattern;
    private String representation;

    public Regexp(String aName, JSONObject aRule) throws ValidationException {
        super(aName);
        ValidatorUtil.requiresAttribute(aRule, ValidatorUtil.PARAM_PATTERN, JSONString.class);
        String lPattern = ((JSONString) aRule.get(ValidatorUtil.PARAM_PATTERN)).getValue();
        init(lPattern);
    }

    public Regexp(String aName, String aPattern) {
        super(aName);
        init(aPattern);
    }

    private void init(String pattern) {
        try {
            this.pattern = Pattern.compile(pattern);
            this.representation = pattern;
        }
        catch (PatternSyntaxException e) {
            throw new IllegalArgumentException(String.format(REG001, pattern, this.getName()), e);
        }
    }

    public void validate(JSONValue aValue)
    throws ValidationException
    {
        if(!aValue.isString()) throw new ValidationException(String.format(REG002, aValue.toString(), this.getName()));
        final String lString = ((JSONString) aValue).getValue();

        Matcher lMatcher = pattern.matcher(lString);
        if(!lMatcher.matches())
        {
            throw new ValidationException(String.format(REG003, lString, representation, this.getName()));
        }
    }
}
