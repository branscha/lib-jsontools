package com.sdicons.json.mapper.helper.impl;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;

import java.math.BigInteger;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.sdicons.json.mapper.JSONMapper;
import com.sdicons.json.mapper.MapperException;
import com.sdicons.json.mapper.helper.ClassMapper;
import com.sdicons.json.model.JSONArray;
import com.sdicons.json.model.JSONInteger;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.model.JSONValue;

public class BigIntegerMapperTest {
    
    private BigIntegerMapper helper;
    private JSONMapper mapper;

    @Before
    public void init() {
        helper = new BigIntegerMapper();
        mapper = new JSONMapper(new ClassMapper[]{});
    }
    
    @Test
    public void bigIntegerTest() throws MapperException {
        BigInteger bi = new BigInteger("1234567");
        JSONValue json  = helper.toJSON(mapper, bi);
        Assert.assertNotNull(json);
        Assert.assertTrue(json.isInteger());
        //
        BigInteger back = (BigInteger) helper.toJava(mapper, json, BigInteger.class);
        Assert.assertNotNull(back);
        Assert.assertThat(back, is(instanceOf(BigInteger.class)));
        Assert.assertEquals(bi, back);
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
        helper.toJava(mapper, json, BigInteger.class);
    }
    
    @Test(expected=MapperException.class)
    public void badInput3() throws MapperException {
        // Target class is not supported by this mapper.
        JSONValue json = new JSONInteger(new BigInteger("13"));
        helper.toJava(mapper, json, String.class);
    }
    
    @Test(expected=MapperException.class)
    public void badInput4() throws MapperException {
        // Source value cannot be converted.
        JSONValue json = new JSONString("aiai");
        helper.toJava(mapper, json, BigInteger.class);
    }
}
