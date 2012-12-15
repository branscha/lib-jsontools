/*******************************************************************************
 * Copyright (c) 2006-2013 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.mapper.helper.impl;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.sdicons.json.mapper.JSONMapper;
import com.sdicons.json.mapper.MapperException;
import com.sdicons.json.mapper.helper.CharacterMapper;
import com.sdicons.json.mapper.helper.ClassMapper;
import com.sdicons.json.model.JSONArray;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.model.JSONValue;

public class CharacterMapperTest {
    
    private CharacterMapper helper;
    private JSONMapper mapper;

    @Before
    public void init() {
        helper = new CharacterMapper();
        mapper = new JSONMapper(new ClassMapper[]{});
    }
    
    @Test
    public void charTest() throws MapperException {
        Character ch = new Character('A');
        JSONValue json  = helper.toJSON(mapper, ch);
        Assert.assertNotNull(json);
        Assert.assertTrue(json.isString());
        //
        Character back = (Character) helper.toJava(mapper, json, Character.class);
        Assert.assertNotNull(back);
        Assert.assertThat(back, is(instanceOf(Character.class)));
        Assert.assertEquals(ch, back);
    }
    
    @Test(expected=MapperException.class)
    public void badInput2() throws MapperException {
        // Source JSON type cannot be converted.
        JSONValue json = new JSONArray();
        helper.toJava(mapper, json, Character.class);
    }
    
    @Test(expected=MapperException.class)
    public void badInput3() throws MapperException {
        // Target class is not supported by this mapper.
        JSONValue json = new JSONString("B");
        helper.toJava(mapper, json, Boolean.class);
    }
    
    @Test(expected=MapperException.class)
    public void badInput4() throws MapperException {
        // Source value cannot be converted.
        JSONValue json = new JSONString("aiai");
        helper.toJava(mapper, json, Character.class);
    }
}
