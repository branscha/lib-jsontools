/*******************************************************************************
 * Copyright (c) 2006-2013 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.mapper.helper.impl;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.sdicons.json.mapper.JSONMapper;
import com.sdicons.json.mapper.MapperException;
import com.sdicons.json.mapper.helper.ClassMapper;
import com.sdicons.json.mapper.helper.FloatMapper;
import com.sdicons.json.model.JSONArray;
import com.sdicons.json.model.JSONDecimal;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.model.JSONValue;

public class FloatMapperTest {

    private FloatMapper helper;
    private JSONMapper mapper;

    @Before
    public void init() {
        helper = new FloatMapper();
        mapper = new JSONMapper(new ClassMapper[]{});
    }

    @Test
    public void happy() throws MapperException {
        Float by = new Float(3.141592653589);
        JSONValue json  = helper.toJSON(mapper, by);
        Assert.assertNotNull(json);
        Assert.assertTrue(json.isDecimal());
        //
        Object back = helper.toJava(mapper, json, Float.class);
        Assert.assertNotNull(back);
        Assert.assertThat(back, is(instanceOf(Float.class)));
        Assert.assertEquals(by, back);
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
        helper.toJava(mapper, json, Float.class);
    }

    @Test(expected=MapperException.class)
    public void badInput3() throws MapperException {
        // Target class is not supported by this mapper.
        JSONValue json = new JSONDecimal(new BigDecimal("0.5445454"));
        helper.toJava(mapper, json, String.class);
    }

    @Test(expected=MapperException.class)
    public void badInput4() throws MapperException {
        // Source value cannot be converted.
        JSONValue json = new JSONString("aiai");
        helper.toJava(mapper, json, Float.class);
    }
}
