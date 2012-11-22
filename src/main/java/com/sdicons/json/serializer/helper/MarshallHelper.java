/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.serializer.helper;

import com.sdicons.json.serializer.marshall.MarshallException;
import com.sdicons.json.serializer.marshall.JSONMarshall;
import com.sdicons.json.model.JSONObject;
import com.sdicons.json.helper.Helper;

import java.util.HashMap;

/** A helper can render an instance of a specific class in a custom way.
 * It is the helpers responsability to render instances of a class to/from JSON.
 */
public interface MarshallHelper
extends Helper
{
    /** Convert an element to JSON.
     *
     * @param aObj Instance that should be rendered to JSON.
     * @param aObjectElement The parent element where we have to put the rendered information. A helper is allowed to add
     *                       child elements.
     * @param aMarshall      The marshall we can use to recursively render parts of our own object.
     * @param aPool          A pool of objects already encountered. Is used to resolve references.
     * @throws MarshallException
     */
    public void renderValue(Object aObj, JSONObject aObjectElement, JSONMarshall aMarshall, HashMap aPool)
    throws MarshallException;

    /** Convert JSON representation into an instance of a class.
     *
     * @param aObjectElement The source element we have to convert into an object.
     * @param aMarshall The marshall we can use to convert sub elements into subobjects to compose our target object.
     * @param aPool A pool of objects already encountered. Is used to resolve references.
     * @return The newly created object.
     * @throws MarshallException
     */
    public Object parseValue(JSONObject aObjectElement, JSONMarshall aMarshall, HashMap aPool)
    throws MarshallException;
}
