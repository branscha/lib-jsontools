package com.sdicons.json.examples;

import org.junit.Test;

import com.sdicons.json.mapper.JSONMapper;
import com.sdicons.json.mapper.MapperException;
import com.sdicons.json.serializer.JSONSerializer;
import com.sdicons.json.serializer.SerializerException;

public class SerializerVersusMapper {

    @Test
    public void serialize() throws SerializerException {
        Person p = new Person();
        p.setName("Mr. Jason Tools");
        p.setPhoneNumber("0123456789");
        p.setAge(40);

        JSONSerializer serializer = new JSONSerializer();
        serializer.marshal(p);
//        JSONValue json = serializer.marshal(p);
//        System.out.println(json.render(true));
    }

    @Test
    public void map() throws MapperException {
        Person p = new Person();
        p.setName("Mr. Jason Tools");
        p.setPhoneNumber("0123456789");
        p.setAge(40);

        JSONMapper mapper = new JSONMapper();
        mapper.toJSON(p);
//        JSONValue json = mapper.toJSON(p);
        //System.out.println(json.render(true));

    }
}


