/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.serializer;

public class JSONSerializeException
extends Exception
{
    public JSONSerializeException(String aComments)
    {
        super(aComments);
    }

    public JSONSerializeException()
    {
        super();
    }
}
