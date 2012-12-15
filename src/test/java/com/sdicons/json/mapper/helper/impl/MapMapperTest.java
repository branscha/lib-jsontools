/*******************************************************************************
 * Copyright (c) 2006-2013 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.mapper.helper.impl;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.sdicons.json.mapper.JSONMapper;
import com.sdicons.json.mapper.MapperException;
import com.sdicons.json.mapper.helper.ClassMapper;
import com.sdicons.json.mapper.helper.MapMapper;
import com.sdicons.json.mapper.helper.StringMapper;
import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.model.JSONValue;

public class MapMapperTest {
    private MapMapper helper;
    private JSONMapper mapper;
    
    @Before
    public void init() {
        helper = new MapMapper();
        mapper = new JSONMapper(new ClassMapper[]{});
    }
    
    @SuppressWarnings("rawtypes")
    @Test
    public void hashMaps() throws MapperException {
        mapper.addHelper(new StringMapper());
        Map<String, String> map = new HashMap<String, String>();
        map.put("uno" , "one");
        map.put("duo", "two" );
        map.put("tres", "three");
        JSONValue json = helper.toJSON(mapper, map);
        Assert.assertNotNull(json);
        Assert.assertThat(json, is(instanceOf(JSONObject.class)));
        //
        // Back to HashMap.
        Object back = helper.toJava(mapper, json, HashMap.class);
        Assert.assertNotNull(back);
        Assert.assertThat(back, is(instanceOf(HashMap.class)));
        Assert.assertTrue(((Map)back).containsKey("uno"));
        Assert.assertEquals("one", ((Map)back).get("uno" ));
        //
        // Try a TreeMap.
        back = helper.toJava(mapper, json, TreeMap.class);
        Assert.assertNotNull(back);
        Assert.assertThat(back, is(instanceOf(TreeMap.class)));
        Assert.assertTrue(((Map)back).containsKey("uno"));
        Assert.assertEquals("one", ((Map)back).get("uno" ));
        //
        // Try to ask for an unmodifiable map that cannot be instantiated.
        // The helper should create some other map.
        back = helper.toJava(mapper, json, Collections.EMPTY_MAP.getClass());
        Assert.assertNotNull(back);
        Assert.assertTrue(((Map)back).containsKey("uno"));
        Assert.assertEquals("one", ((Map)back).get("uno" ));
    }
    
    @Test(expected=MapperException.class)
    public void unhappy() throws MapperException {
        // The input JSON is not an object. The mapper cannot do this.
        helper.toJava(mapper, new JSONString("ai"), HashMap.class);
    }
    
    @Test(expected=MapperException.class)
    public void unhappy2() throws MapperException {
        // The requested class is not a map.
        helper.toJava(mapper, new JSONObject(), Integer.class);
    }
    
    @Test(expected=MapperException.class)
    public void unhappy3() throws MapperException {
        // You cannot map a string to a map.
        helper.toJSON(mapper, 3);
    }
    
}
