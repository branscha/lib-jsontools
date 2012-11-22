/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.serializer.marshall;

public class MarshallException
extends Exception
{
    public MarshallException(String aComments)
    {
        super(aComments);
    }

    public MarshallException()
    {
        super();
    }
}
