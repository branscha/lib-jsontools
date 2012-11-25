/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.serializer.helper.impl;

import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.serializer.JSONSerializeException;
import com.sdicons.json.serializer.JSONSerializer;

import java.awt.*;
import java.util.HashMap;

public class FontHelper
extends AbstractHelper
{
    public Object parseValue(JSONObject aObjectElement, JSONSerializer aMarshall, HashMap aPool)
    throws JSONSerializeException
    {
        JSONSerializer.requireStringAttribute(aObjectElement, JSONSerializer.RNDR_ATTR_VALUE);
        return Font.decode(((JSONString) aObjectElement.get(JSONSerializer.RNDR_ATTR_VALUE)).getValue());
    }

    public void renderValue(Object aObj, JSONObject aParent, JSONSerializer aMarshall, HashMap aPool) throws JSONSerializeException
    {
        final Font lFont = (Font) aObj;
        final int lFontStyle = lFont.getStyle();
        
        String lStyle;
        switch(lFontStyle)
        {
            case Font.PLAIN: lStyle = "PLAIN"; break;
            case Font.BOLD: lStyle =  "BOLD"; break;
            case Font.ITALIC: lStyle = "ITALIC";break;
            case 3: lStyle = "BOLDITALIC";break;
            default: lStyle="PLAIN";
        }
        aParent.getValue().put(JSONSerializer.RNDR_ATTR_VALUE, new JSONString( lFont.getName() + "-"+ lStyle + "-" + lFont.getSize()));
    }

    public Class getHelpedClass()
    {
        return Font.class;
    }
}
