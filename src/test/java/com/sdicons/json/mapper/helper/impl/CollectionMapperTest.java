package com.sdicons.json.mapper.helper.impl;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;

import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.sdicons.json.mapper.JSONMapper;
import com.sdicons.json.mapper.MapperException;
import com.sdicons.json.mapper.helper.MapperHelper;
import com.sdicons.json.model.JSONArray;
import com.sdicons.json.model.JSONInteger;
import com.sdicons.json.model.JSONValue;

public class CollectionMapperTest {
    private CollectionMapper helper;
    private JSONMapper mapper;
    
    @Before
    public void init() {
        helper = new CollectionMapper();
        mapper = new JSONMapper(new MapperHelper[]{});
    }
    
    @Test
    public void lists() throws MapperException {
        mapper.addHelper(new StringMapper());
        List<String> lst = new LinkedList<String>();
        lst.addAll(Arrays.asList("uno", "duo","tres"));
        //
        JSONValue json = helper.toJSON(mapper, lst);
        Assert.assertNotNull(json);
        Assert.assertTrue(json.isArray());
        //
        // We request a LinkedList.
        Object back = helper.toJava(mapper, json, LinkedList.class);
        Assert.assertThat(back, is(instanceOf(LinkedList.class)));
        Assert.assertEquals(lst, back);
        //
        // We request an ArrayList.
        back = helper.toJava(mapper, json, ArrayList.class);
        Assert.assertThat(back, is(instanceOf(ArrayList.class)));
        Assert.assertEquals(lst, back);
        //
        // The requested class cannot be instantiated, the helper should
        // find an alternative.
        back = helper.toJava(mapper, json, Collections.EMPTY_LIST.getClass());
        Assert.assertThat(back, is(instanceOf(List.class)));
        Assert.assertEquals(lst, back);
        //
        // Use type information.
        back = helper.toJava(mapper, json, LinkedList.class, new Type[]{String.class});
        Assert.assertThat(back, is(instanceOf(LinkedList.class)));
        Assert.assertEquals(lst, back);
    }
    
    @Test
    public void sets() throws MapperException {
        mapper.addHelper(new StringMapper());
        Set<String> set = new HashSet<String>();
        set.addAll(Arrays.asList("uno", "duo","tres"));
        //
        JSONValue json = helper.toJSON(mapper, set);
        Assert.assertNotNull(json);
        Assert.assertTrue(json.isArray());
        //
        // We request a HashSet.
        Object back = helper.toJava(mapper, json, HashSet.class);
        Assert.assertThat(back, is(instanceOf(HashSet.class)));
        Assert.assertEquals(set, back);
        //
        // We request an TreeSet.
        back = helper.toJava(mapper, json, TreeSet.class);
        Assert.assertThat(back, is(instanceOf(TreeSet.class)));
        Assert.assertEquals(set, back);
        //
        // The requested class cannot be instantiated, the helper should
        // find an alternative.
        back = helper.toJava(mapper, json, SortedSet.class);
        Assert.assertThat(back, is(instanceOf(SortedSet.class)));
        Assert.assertEquals(set, back);
        //
        back = helper.toJava(mapper, json, Set.class);
        Assert.assertThat(back, is(instanceOf(Set.class)));
        Assert.assertEquals(set, back);
    }
    
    @Test(expected=MapperException.class)
    public void badInput() throws MapperException{
        // Cannot map this String with the collection mapper.
        //
        helper.toJSON(mapper, "aiai");
    }
    
    @Test(expected=MapperException.class)
    public void badInput2() throws MapperException {
        // Cannot map a JSON integer to a set.
        //
        helper.toJava(mapper, new JSONInteger(new BigInteger("5")), HashSet.class);
    }
    
    @Test(expected=MapperException.class)
    public void badInput3() throws MapperException {
        // Cannot map a JSON list to a string with the collection mapper.
        //
        helper.toJava(mapper, new JSONArray(), String.class);
    }
}
