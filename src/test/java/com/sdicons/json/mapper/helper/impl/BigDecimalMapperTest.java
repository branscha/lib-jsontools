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
import com.sdicons.json.model.JSONArray;
import com.sdicons.json.model.JSONDecimal;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.model.JSONValue;

public class BigDecimalMapperTest {
    
    private BigDecimalMapper helper;
    private JSONMapper mapper;

    @Before
    public void init() {
        helper = new BigDecimalMapper();
        // This constructor creates a mapper without any helpers.
        // We will have to add the helpers ourselves.
        // In this way we have control over the objects under test.
        mapper = new JSONMapper(new ClassMapper[]{});
    }
    
    @Test
    public void bigDecimalTest() throws MapperException {
        BigDecimal bd = new BigDecimal(0.12345);
        JSONValue json  = helper.toJSON(mapper, bd);
        Assert.assertNotNull(json);
        Assert.assertTrue(json.isDecimal());
        //
        BigDecimal back = (BigDecimal) helper.toJava(mapper, json, BigDecimal.class);
        Assert.assertNotNull(back);
        Assert.assertThat(back, is(instanceOf(BigDecimal.class)));
        Assert.assertEquals(bd, back);
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
        helper.toJava(mapper, json, BigDecimal.class);
    }
    
    @Test(expected=MapperException.class)
    public void badInput3() throws MapperException {
        // Target class is not supported by this mapper.
        JSONValue json = new JSONDecimal(new BigDecimal(1.2));
        helper.toJava(mapper, json, String.class);
    }
    
    @Test(expected=MapperException.class)
    public void badInput4() throws MapperException {
        // Source value cannot be converted.
        JSONValue json = new JSONString("aiai");
        helper.toJava(mapper, json, BigDecimal.class);
    }
}
