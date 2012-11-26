package com.sdicons.json.serializer.helper.impl;

import java.util.HashMap;

import com.sdicons.json.model.JSONObject;
import com.sdicons.json.serializer.SerializerException;
import com.sdicons.json.serializer.JSONSerializer;
import com.sdicons.json.serializer.helper.SerializerHelper;

public class ObjectHelperMeta implements SerializerHelper {

    private ObjectHelperFields fieldSerializer = new ObjectHelperFields();
    private ObjectHelperProps propertySerializer = new ObjectHelperProps();

    @Override
    public Class<?> getHelpedClass() {
        return Object.class;
    }

    @Override
    public void renderValue(Object aObj, JSONObject aObjectElement, JSONSerializer aMarshall, HashMap<Object, Object> aPool) throws SerializerException {
        final String option = (String) aMarshall.getSerializeOption(JSONSerializer.OPTION_OBJECTSERIALIZE, "property");
        if("property".equalsIgnoreCase(option)) propertySerializer.renderValue(aObj, aObjectElement, aMarshall, aPool);
        else fieldSerializer.renderValue(aObj, aObjectElement, aMarshall, aPool);
    }

    @Override
    public Object parseValue(JSONObject aObjectElement, JSONSerializer aMarshall, HashMap<Object, Object> aPool) throws SerializerException {
        final String option = (String) aMarshall.getSerializeOption(JSONSerializer.OPTION_OBJECTSERIALIZE, "property");
        if("property".equalsIgnoreCase(option)) return propertySerializer.parseValue(aObjectElement, aMarshall, aPool);
        else return fieldSerializer.parseValue(aObjectElement, aMarshall, aPool);
    }
}
