/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.validator.predicates;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.model.JSONValue;
import com.sdicons.json.validator.ValidationException;
import com.sdicons.json.validator.Validator;
import com.sdicons.json.validator.ValidatorUtil;

/**
 * A predicate that uses a Java implementation of CustomValidator. You
 * can implement your own CustomValidator and then create a rule with the
 * classname in it.
 * <p>
 * Example
 *
 * <pre>
 * <code>
 * {
 *    "name" :"Custom test",
 *    "type" :"custom",
 *    "class" : "com.sdicons.json.validator.MyValidator"
 * }
 * </code>
 * </pre
 *
 * @see CustomValidator
 */
public class CustomPredicate
extends Predicate
{
    private static final String CUST001 = "JSONValidator/CustomPredicate/001: The custom predicate class '%s' is not derived from CustomValidator in rule '%s'.";
    private static final String CUST002 = "JSONValidator/CustomPredicate/002: The custom predicate class '%s' cannot be found  in rule '%s'.";
    private static final String CUST003 = "JSONValidator/CustomPredicate/003: The custom predicate class '%s' does not have a suitable constructor in rule '%s'.";
    private static final String CUST004 = "JSONValidator/CustomPredicate/004: Cannot instantiate predicate class '%s' in rule '%s'.";

    private Validator validator;

    public CustomPredicate(String aName, JSONObject aRule, Map<String, Validator> aRuleset)
    throws ValidationException
    {
        super(aName);
        ValidatorUtil.requiresAttribute(aRule, ValidatorUtil.PARAM_CLASS, JSONString.class);
        String lClassname = ((JSONString) aRule.get(ValidatorUtil.PARAM_CLASS)).getValue();

        try
        {
            Class<?> lCustomClass = Class.forName(lClassname);
            if(!CustomValidator.class.isAssignableFrom(lCustomClass))
            {
                // Problem, not derived from CustomValidator.
                throw new ValidationException(String.format(CUST001, lClassname, this.getName()));
            }
            else
            {
                Constructor<?> lConstructor = lCustomClass.getConstructor(String.class, JSONObject.class, HashMap.class);
                validator = (Validator) lConstructor.newInstance(aName, aRule, aRuleset);
            }
        }
        catch (ClassNotFoundException e)
        {
            throw new ValidationException(String.format(CUST002, lClassname, this.getName()), e);
        }
        catch (NoSuchMethodException e)
        {
            throw new ValidationException(String.format(CUST003, lClassname, this.getName()), e);
        }
        catch (InstantiationException e)
        {
            throw new ValidationException(String.format(CUST004, lClassname, this.getName()), e);
        }
        catch (IllegalAccessException e)
        {
            throw new ValidationException(String.format(CUST004, lClassname, this.getName()), e);
        }
        catch (InvocationTargetException e)
        {
            throw new ValidationException(String.format(CUST004, lClassname, this.getName()), e);
        }
    }

    public void validate(JSONValue aValue)
    throws ValidationException
    {
        validator.validate(aValue);
    }
}
