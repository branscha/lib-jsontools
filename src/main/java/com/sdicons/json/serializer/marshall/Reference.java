/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.serializer.marshall;

class Reference
{
    private Object encapsulated;

    public Reference(Object aRef)
    {
        encapsulated = aRef;
    }

    public Object getRef()
    {
        return encapsulated;
    }

    public int hashCode()
    {
        return encapsulated.hashCode();
    }

    public boolean equals(Object obj)
    {
        return (encapsulated == ((Reference) obj).encapsulated);
    }
}
