package com.sdicons.json.validator;

import org.junit.Before;
import org.junit.Test;

import com.sdicons.json.model.JSONValue;
import com.sdicons.json.parser.JSONParser;
import com.sdicons.json.parser.ParserException;

public class ValidatorBuilderTest {
    
    ValidatorBuilder vb;
    JSONValidator v;
    JSONParser p;
    
    @Before
    public void init() {
        vb = new ValidatorBuilder();
    }

    @Test
    public void boolTest() throws ParserException, ValidationException {
        Validator v = vb.bool();
        p = new JSONParser("true");
        JSONValue json = p.nextValue();
        v.validate(json);
    }
    
    @Test(expected=ValidationException.class)
    public void boolUnhappyTest() throws ParserException, ValidationException {
        Validator v = vb.bool();
        p = new JSONParser("123");
        JSONValue json = p.nextValue();
        v.validate(json);
    }
    
    @Test
    public void decimalTest() throws ParserException, ValidationException {
        Validator v = vb.decimal();
        p = new JSONParser("123.5e-3");
        JSONValue json = p.nextValue();
        v.validate(json);
    }
    
    @Test(expected=ValidationException.class)
    public void decimalUnhappyTest() throws ParserException, ValidationException {
        Validator v = vb.decimal();
        p = new JSONParser("false");
        JSONValue json = p.nextValue();
        v.validate(json);
    }
    
    @Test
    public void enumTest() throws ParserException, ValidationException {
        Validator v = vb.enumeration(new String[]{"RED", "GREEN", "BLUE"});
        p = new JSONParser("\"GREEN\"");
        JSONValue json = p.nextValue();
        v.validate(json);
    }
    
    @Test(expected=ValidationException.class)
    public void enumUnhappyTest() throws ParserException, ValidationException {
        Validator v = vb.enumeration(new String[]{"RED", "GREEN", "BLUE"});
        p = new JSONParser("\"ORANGE\"");
        JSONValue json = p.nextValue();
        v.validate(json);
    }
    
    @Test(expected=ValidationException.class)
    public void falseTest() throws ParserException, ValidationException {
        Validator v = vb.falsep();
        p = new JSONParser("false");
        JSONValue json = p.nextValue();
        v.validate(json);
    }
    
    @Test(expected=ValidationException.class)
    public void falseUnhappyTest() throws ParserException, ValidationException {
        Validator v = vb.falsep();
        p = new JSONParser("123");
        JSONValue json = p.nextValue();
        v.validate(json);
    }
    
    @Test
    public void integerTest() throws ParserException, ValidationException {
        Validator v = vb.intp();
        p = new JSONParser("12345");
        JSONValue json = p.nextValue();
        v.validate(json);
    }
    
    @Test(expected=ValidationException.class)
    public void integerUnhappyTest() throws ParserException, ValidationException {
        Validator v = vb.intp();
        p = new JSONParser("{}");
        JSONValue json = p.nextValue();
        v.validate(json);
    }
    
    @Test
    public void nullTest() throws ParserException, ValidationException {
        Validator v = vb.nullp();
        p = new JSONParser("null");
        JSONValue json = p.nextValue();
        v.validate(json);
    }
    
    @Test(expected=ValidationException.class)
    public void nullUnhappyTest() throws ParserException, ValidationException {
        Validator v = vb.nullp();
        p = new JSONParser("1234");
        JSONValue json = p.nextValue();
        v.validate(json);
    }
}
