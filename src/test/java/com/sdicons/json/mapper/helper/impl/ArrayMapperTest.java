package com.sdicons.json.mapper.helper.impl;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.mock;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.sdicons.json.mapper.JSONMapper;
import com.sdicons.json.mapper.MapperException;
import com.sdicons.json.model.JSONArray;
import com.sdicons.json.model.JSONValue;

public class ArrayMapperTest {
    
    ArrayMapper helper;
    JSONMapper mapper;
    
    @Before
    public void init() {
        helper = new ArrayMapper();
        mapper = mock(JSONMapper.class);
    }
    
    @Test
    public void intArray() throws MapperException {
        int[] arr = new int[]{-9, -8, -7, -6, -5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        JSONValue json = helper.toJSON(mapper,  arr);
        Assert.assertThat(json, is(instanceOf(JSONArray.class)));
        
//        Object back = helper.toJava(mapper, json, int[].class);
//        Assert.assertArrayEquals(arr, (int[])back);
    }
    
}
