/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.validator.impl.predicates;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.model.JSONValue;
import com.sdicons.json.validator.ValidationException;
import com.sdicons.json.validator.impl.ValidatorUtil;


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
    private Pattern pattern;
    private String representation;

    public Regexp(String aName, JSONObject aRule)
    throws ValidationException
    {
        super(aName, aRule);

        ValidatorUtil.requiresAttribute(aRule, ValidatorUtil.PARAM_PATTERN, JSONString.class);
        String lPattern = ((JSONString) aRule.get(ValidatorUtil.PARAM_PATTERN)).getValue();

        try
        {
            pattern = Pattern.compile(lPattern);
            representation = lPattern;
        }
        catch (PatternSyntaxException e)
        {
           fail("Error while compiling the pattern: " + e.getMessage(), aRule);
        }
    }

    public void validate(JSONValue aValue)
    throws ValidationException
    {
        if(!aValue.isString()) fail("The value is not a JSONString." ,aValue);
        final String lString = ((JSONString) aValue).getValue();

        Matcher lMatcher = pattern.matcher(lString);
        if(!lMatcher.matches())
        {
            fail("The string: \"" + lString + "\" does not match the pattern: \"" + representation + "\".", aValue);
        }
    }
}
