/*******************************************************************************
 * Copyright (c) 2006-2013 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.mapper;

public class MapperException
extends Exception
{
    private static final long serialVersionUID = -31540606669332242L;

    public MapperException()
    {
    }

    public MapperException(Throwable cause)
    {
        super(cause);
    }

    public MapperException(String message)
    {
        super(message);
    }

    public MapperException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
