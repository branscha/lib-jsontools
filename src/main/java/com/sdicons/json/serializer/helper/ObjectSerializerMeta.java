package com.sdicons.json.serializer.helper;

import java.util.HashMap;

import com.sdicons.json.model.JSONObject;
import com.sdicons.json.serializer.JSONSerializer;
import com.sdicons.json.serializer.SerializerException;

public class ObjectSerializerMeta implements ClassSerializer {

    private ObjectSerializerFields fieldSerializer = new ObjectSerializerFields();
    private ObjectSerializerProps propertySerializer = new ObjectSerializerProps();

    @Override
    public Class<?> getHelpedClass() {
        return Object.class;
    }

    @Override
    public void toJSON(Object aObj, JSONObject aObjectElement, JSONSerializer serializer, HashMap<Object, Object> aPool) throws SerializerException {
        final String option = (String) serializer.getSerializeOption(JSONSerializer.OPT_OBJSERIALIZE, JSONSerializer.OBJSERIALIZE_PROPERTY);
        if("property".equalsIgnoreCase(option)) propertySerializer.toJSON(aObj, aObjectElement, serializer, aPool);
        else fieldSerializer.toJSON(aObj, aObjectElement, serializer, aPool);
    }

    @Override
    public Object toJava(JSONObject aObjectElement, JSONSerializer serializer, HashMap<Object, Object> aPool) throws SerializerException {
        final String option = (String) serializer.getSerializeOption(JSONSerializer.OPT_OBJSERIALIZE, JSONSerializer.OBJSERIALIZE_PROPERTY);
        if("property".equalsIgnoreCase(option)) return propertySerializer.toJava(aObjectElement, serializer, aPool);
        else return fieldSerializer.toJava(aObjectElement, serializer, aPool);
    }
}
