/*******************************************************************************
 * Copyright (c) 2006-2013 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.model;

/**
 * Superclass of JSON complex types, namely {@link JSONArray} and {@link JSONObject}.
 */
public abstract class JSONComplex
extends JSONValue
{
    public abstract int size();   
}
