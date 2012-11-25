package com.sdicons.json.mapper.helper.impl;

import com.sdicons.json.mapper.JSONMapper;
import com.sdicons.json.mapper.MapperException;
import com.sdicons.json.mapper.helper.SimpleMapperHelper;
import com.sdicons.json.model.JSONValue;

public class ObjectMapperMeta implements SimpleMapperHelper {

    private ObjectMapperFields fieldMapper = new ObjectMapperFields();
    private ObjectMapperProps propertyMapper = new ObjectMapperProps();

    @Override
    public Class<?> getHelpedClass() {
        return Object.class;
    }

    @Override
    public Object toJava(JSONMapper mapper, JSONValue aValue, Class<?> aRequestedClass) throws MapperException {
        final String option = (String) mapper.getMappingOption(JSONMapper.OPTION_OBJECTMAPPING, "property");
        if("property".equalsIgnoreCase(option)) return propertyMapper.toJava(mapper, aValue, aRequestedClass);
        else return fieldMapper.toJava(mapper, aValue, aRequestedClass);
    }

    @Override
    public JSONValue toJSON(JSONMapper mapper, Object aPojo) throws MapperException {
        final String option = (String) mapper.getMappingOption(JSONMapper.OPTION_OBJECTMAPPING, "property");
        if("property".equalsIgnoreCase(option)) return propertyMapper.toJSON(mapper, aPojo);
        else return fieldMapper.toJSON(mapper, aPojo);
    }
}
