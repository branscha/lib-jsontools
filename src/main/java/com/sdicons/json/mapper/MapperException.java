/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.mapper;

public class MapperException
extends Exception
{
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
