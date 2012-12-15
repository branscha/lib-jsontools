/*******************************************************************************
 * Copyright (c) 2006-2013 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.serializer.helper;

import java.util.HashMap;

import com.sdicons.json.model.JSONObject;
import com.sdicons.json.repository.ClassHelper;
import com.sdicons.json.serializer.SerializerException;
import com.sdicons.json.serializer.JSONSerializer;

/** A helper can render an instance of a specific class in a custom way.
 * It is the helpers responsibility to render instances of a class to/from JSON.
 */
public interface ClassSerializer
extends ClassHelper
{
    /** Convert an element to JSON.
     *
     * @param javaObj Instance that should be rendered to JSON.
     * @param jsonContainer The parent element where we have to put the rendered information. A helper is allowed to add
     *                       child elements.
     * @param serializer      The marshal we can use to recursively render parts of our own object.
     * @param aPool          A pool of objects already encountered. Is used to resolve references.
     * @throws SerializerException
     */
    public void toJSON(Object javaObj, JSONObject jsonContainer, JSONSerializer serializer, HashMap<Object, Object> aPool)
    throws SerializerException;

    /** Convert JSON representation into an instance of a class.
     *
     * @param jsonObj The source element we have to convert into an object.
     * @param serializer The marshal we can use to convert sub elements into sub objects to compose our target object.
     * @param aPool A pool of objects already encountered. Is used to resolve references.
     * @return The newly created object.
     * @throws SerializerException
     */
    public Object toJava(JSONObject jsonObj, JSONSerializer serializer, HashMap<Object, Object> aPool)
    throws SerializerException;
}
