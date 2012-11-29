package com.sdicons.json.mapper.helper.impl;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;

import java.math.BigInteger;
import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.sdicons.json.mapper.JSONMapper;
import com.sdicons.json.mapper.MapperException;
import com.sdicons.json.mapper.helper.MapperHelper;
import com.sdicons.json.model.JSONInteger;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.model.JSONValue;

public class DateMapperTest {
    private DateMapper helper;
    private JSONMapper mapper;
    
    @Before
    public void init() {
        helper = new DateMapper();
        mapper = new JSONMapper(new MapperHelper[]{});
    }
    
    @Test
    public void dates() throws MapperException {
        Date date = new Date();
        JSONValue json = helper.toJSON(mapper, date);
        Assert.assertNotNull(json);
        Assert.assertThat(json, is(instanceOf(JSONString.class)));
        //
        Object back = helper.toJava(mapper, json, Date.class);
        Assert.assertNotNull(back);
        Assert.assertThat(back, is(instanceOf(Date.class)));
        //
        // we need to divide because we only map seconds in the 
        // standard pattern.
        Assert.assertEquals(date.getTime() / 1000, ((Date) back).getTime() / 1000);
        Assert.assertNotEquals(date.getTime(), (Date) back);
        //
        // Here we use a pattern where millis are included.
        // We should have normal equality here.
        mapper.setMappingOption(JSONMapper.OPT_DATEFORMAT, "yyMMddHHmmssSSSZ");
        json = helper.toJSON(mapper, date);
        back = helper.toJava(mapper, json, Date.class);
        Assert.assertNotNull(back);
        Assert.assertThat(back, is(instanceOf(Date.class)));
        Assert.assertEquals(date, back);
    }
    
    @Test(expected=MapperException.class)
    public void badInput() throws MapperException {
        helper.toJSON(mapper, "ai");
    }
    
    @Test(expected=MapperException.class)
    public void badInput2() throws MapperException {
        helper.toJava(mapper, new JSONString("ai"), Date.class);
    }
    
    @Test(expected=MapperException.class)
    public void badInput3() throws MapperException {
        helper.toJava(mapper, new JSONString("" + new Date()), Integer.class);
    }
    
    @Test(expected=MapperException.class)
    public void badInput4() throws MapperException {
        helper.toJava(mapper, new JSONInteger(new BigInteger("13")), Date.class);
    }
}
