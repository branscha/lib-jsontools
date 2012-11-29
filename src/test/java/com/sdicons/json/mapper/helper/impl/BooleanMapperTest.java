package com.sdicons.json.mapper.helper.impl;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.sdicons.json.mapper.JSONMapper;
import com.sdicons.json.mapper.MapperException;
import com.sdicons.json.mapper.helper.MapperHelper;
import com.sdicons.json.model.JSONArray;
import com.sdicons.json.model.JSONBoolean;
import com.sdicons.json.model.JSONValue;

public class BooleanMapperTest {

    private BooleanMapper helper;
    private JSONMapper mapper;

    @Before
    public void init() {
        helper = new BooleanMapper();
        mapper = new JSONMapper(new MapperHelper[]{});
    }

    @Test
    public void happy() throws MapperException {
        Boolean b = Boolean.TRUE;
        JSONValue json  = helper.toJSON(mapper, b);
        Assert.assertNotNull(json);
        Assert.assertTrue(json.isBoolean());
        //
        Boolean back = (Boolean) helper.toJava(mapper, json, Boolean.class);
        Assert.assertNotNull(back);
        Assert.assertThat(back, is(instanceOf(Boolean.class)));
        Assert.assertEquals(b, back);
    }

    @Test(expected=MapperException.class)
    public void badInput() throws MapperException {
        // Bad input representation.
        helper.toJSON(mapper, "aiai");
    }

    @Test(expected=MapperException.class)
    public void badInput2() throws MapperException {
        // Source JSON type cannot be converted.
        JSONValue json = new JSONArray();
        helper.toJava(mapper, json, Boolean.class);
    }

    @Test(expected=MapperException.class)
    public void badInput3() throws MapperException {
        // Target class is not supported by this mapper.
        JSONValue json = new JSONBoolean(true);
        helper.toJava(mapper, json, String.class);
    }
}
