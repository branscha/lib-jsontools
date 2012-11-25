/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.validator;

import com.sdicons.json.model.JSONValue;

/**
 * The exception is thrown when a validation fails.
 */
public class ValidationException
extends Exception
{
    private static final long serialVersionUID = 6567006977842147331L;

    private JSONValue culprit;
    private String validationRule;

    public ValidationException(String aComments, JSONValue aCulprit, String aRule)
    {
        super(aComments);
        culprit = aCulprit;
        validationRule = aRule;
    }

    public ValidationException(JSONValue aCulprit, String aRule)
    {
        super();
        culprit = aCulprit;
        validationRule = aRule;
    }

    public JSONValue getCulprit()
    {
        return culprit;
    }

    public String getValidationRule()
    {
        return validationRule;
    }

    public String getMessage()
    {
        return "Validation failed on rule \"" + validationRule + "\": " + super.getMessage() + " Offending part: " + culprit;
    }
}
