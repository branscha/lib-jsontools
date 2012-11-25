/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.validator.impl.predicates;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.model.JSONValue;
import com.sdicons.json.validator.ValidationException;
import com.sdicons.json.validator.Validator;
import com.sdicons.json.validator.impl.ValidatorUtil;

public class CustomPredicate
extends Predicate
{
    private Validator validator;

    public CustomPredicate(String aName, JSONObject aRule, HashMap<String, Validator> aRuleset)
    throws ValidationException
    {
        super(aName, aRule);
        ValidatorUtil.requiresAttribute(aRule, ValidatorUtil.PARAM_CLASS, JSONString.class);
        String lClassname = ((JSONString) aRule.get(ValidatorUtil.PARAM_CLASS)).getValue();

        try
        {
            Class<?> lCustomClass = Class.forName(lClassname);
            if(!CustomValidator.class.isAssignableFrom(lCustomClass))
            {
                // Problem, not derived from CustomValidator.
                throw new ValidationException("The custom class is not derived from CustomValidator: " + lClassname, aRule, aName);
            }
            else
            {
                Constructor<?> lConstructor = lCustomClass.getConstructor(String.class, JSONObject.class,HashMap.class);
                validator = (Validator) lConstructor.newInstance(aName, aRule, aRuleset);
            }
        }
        catch (ClassNotFoundException e)
        {
            throw new ValidationException("The custom class was not found: " + lClassname, aRule, aName);
        }
        catch (NoSuchMethodException e)
        {
            throw new ValidationException("Constructor method not found on custom class: " + lClassname, aRule, aName);
        }
        catch (InstantiationException e)
        {
            throw new ValidationException("Error during construction of validtor of class: " + lClassname, aRule, aName);
        }
        catch (IllegalAccessException e)
        {
            throw new ValidationException("Access rights problem during construction of validator of class: " + lClassname, aRule, aName);
        }
        catch (InvocationTargetException e)
        {
            throw new ValidationException("Access rights problem during construction of validator of class: " + lClassname, aRule, aName);
        }
    }

    public void validate(JSONValue aValue)
    throws ValidationException
    {
        validator.validate(aValue);
    }
}
