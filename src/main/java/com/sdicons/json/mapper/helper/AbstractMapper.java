/*******************************************************************************
 * Copyright (c) 2006-2013 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.mapper.helper;

import com.sdicons.json.mapper.JSONMapper;
import com.sdicons.json.mapper.MapperException;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.model.JSONValue;

public abstract class AbstractMapper
implements ClassMapper
{
    public JSONValue toJSON(JSONMapper mapper, Object aPojo) throws MapperException
    {
        return new JSONString(aPojo.toString());
    }
}
