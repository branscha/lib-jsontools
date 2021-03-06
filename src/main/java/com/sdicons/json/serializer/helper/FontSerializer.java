/*******************************************************************************
 * Copyright (c) 2006-2013 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.serializer.helper;

import java.awt.Font;
import java.util.HashMap;

import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.serializer.SerializerException;
import com.sdicons.json.serializer.JSONSerializer;

public class FontSerializer
extends AbstractSerializer
{
    public Object toJava(JSONObject aObjectElement, JSONSerializer serializer, HashMap<Object, Object> aPool)
    throws SerializerException
    {
        JSONSerializer.requireStringAttribute(aObjectElement, JSONSerializer.RNDR_ATTR_VALUE);
        return Font.decode(((JSONString) aObjectElement.get(JSONSerializer.RNDR_ATTR_VALUE)).getValue());
    }

    public void toJSON(Object aObj, JSONObject aParent, JSONSerializer serializer, HashMap<Object, Object> aPool) throws SerializerException
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

    public Class<?> getHelpedClass()
    {
        return Font.class;
    }
}
