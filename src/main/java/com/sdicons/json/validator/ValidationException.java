/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.validator;


/**
 * The exception is thrown when a validation fails.
 */
public class ValidationException
extends Exception
{

    private static final long serialVersionUID = 7927493439473661483L;

    public ValidationException() {
        super();
        // TODO Auto-generated constructor stub
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

    public ValidationException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    public ValidationException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }
}
