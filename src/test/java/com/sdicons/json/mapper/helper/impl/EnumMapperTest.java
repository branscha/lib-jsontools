package com.sdicons.json.mapper.helper.impl;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.sdicons.json.mapper.JSONMapper;
import com.sdicons.json.mapper.MapperException;
import com.sdicons.json.mapper.helper.ClassMapper;
import com.sdicons.json.mapper.helper.EnumMapper;
import com.sdicons.json.model.JSONBoolean;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.model.JSONValue;

public class EnumMapperTest {
    private EnumMapper helper;
    private JSONMapper mapper;

    @Before
    public void init() {
        helper = new EnumMapper();
        mapper = new JSONMapper(new ClassMapper[]{});
    }

    public static enum  Qualification { AWFUL, BAD, NORMAL, GOOD, SUPER };

    @Test
    public void enums() throws MapperException {
        Qualification q = Qualification.NORMAL;
        JSONValue json = helper.toJSON(mapper, q);
        Assert.assertNotNull(json);
        Assert.assertThat(json, is(instanceOf(JSONString.class)));
        //
        Object back = helper.toJava(mapper, json, Qualification.class);
        Assert.assertNotNull(back);
        Assert.assertEquals(q, back);
    }


    @Test(expected=MapperException.class)
    public void badInput() throws MapperException {
        // The string 'AI' cannot be converted to an enum value of Qualification.
        helper.toJava(mapper, new JSONString("AI"), Qualification.class);
    }

    @Test(expected=MapperException.class)
    public void badInput2() throws MapperException {
        // The requested class is not an enum.
        helper.toJava(mapper, new JSONString("SUPER"), Integer.class);
    }

    @Test(expected=MapperException.class)
    public void badInput3() throws MapperException {
        // Bad JSON value, it does not represent an enum.
        helper.toJava(mapper, JSONBoolean.TRUE, Qualification.class);
    }

}
