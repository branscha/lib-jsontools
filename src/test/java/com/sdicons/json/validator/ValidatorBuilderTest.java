package com.sdicons.json.validator;

import org.junit.Before;
import org.junit.Test;

import com.sdicons.json.model.JSONString;
import com.sdicons.json.model.JSONValue;
import com.sdicons.json.parser.JSONParser;
import com.sdicons.json.parser.ParserException;
import com.sdicons.json.validator.predicates.Properties.PropRule;
import com.sdicons.json.validator.predicates.Switch.SwitchCase;

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
        // It always fails.
        Validator v = vb.falsep();
        p = new JSONParser("false");
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
    
    @Test
    public void numberTest() throws ParserException, ValidationException {
        Validator v = vb.nr();
        p = new JSONParser("12345");
        JSONValue json = p.nextValue();
        v.validate(json);
        
        p = new JSONParser("3.1415");
        json = p.nextValue();
        v.validate(json);
    }
    
    @Test(expected=ValidationException.class)
    public void numberUnhappyTest() throws ParserException, ValidationException {
        Validator v = vb.nr();
        p = new JSONParser("{}");
        JSONValue json = p.nextValue();
        v.validate(json);
    }
    
    @Test
    public void objectTest() throws ParserException, ValidationException {
        Validator v = vb.object();
        p = new JSONParser("{\"key\": 123}");
        JSONValue json = p.nextValue();
        v.validate(json);
    }
    
    @Test(expected=ValidationException.class)
    public void objectUnhappyTest() throws ParserException, ValidationException {
        Validator v = vb.object();
        p = new JSONParser("false");
        JSONValue json = p.nextValue();
        v.validate(json);
    }
    
    @Test
    public void simpleTest() throws ParserException, ValidationException {
        Validator v = vb.simple();
        p = new JSONParser("123");
        JSONValue json = p.nextValue();
        v.validate(json);
        
        p = new JSONParser("true");
        json = p.nextValue();
        v.validate(json);
        
        p = new JSONParser("null");
        json = p.nextValue();
        v.validate(json);
    }
    
    @Test(expected=ValidationException.class)
    public void simpleUnhappyTest() throws ParserException, ValidationException {
        Validator v = vb.simple();
        p = new JSONParser("[1, 2, 3]");
        JSONValue json = p.nextValue();
        v.validate(json);
    }
    
    @Test
    public void stringTest() throws ParserException, ValidationException {
        Validator v = vb.string();
        p = new JSONParser("\"abcdef\"");
        JSONValue json = p.nextValue();
        v.validate(json);
    }
    
    @Test(expected=ValidationException.class)
    public void stringUnhappyTest() throws ParserException, ValidationException {
        Validator v = vb.string();
        p = new JSONParser("{}");
        JSONValue json = p.nextValue();
        v.validate(json);
    }
    
    @Test
    public void trueTest() throws ParserException, ValidationException {
        // It always succeeds.
        Validator v = vb.truep();
        p = new JSONParser("[]");
        JSONValue json = p.nextValue();
        v.validate(json);
    }
    
    @Test
    public void arrayTest() throws ParserException, ValidationException {
        Validator v = vb.array();
        p = new JSONParser("[1, 2, 3]");
        JSONValue json = p.nextValue();
        v.validate(json);
    }
    
    @Test(expected=ValidationException.class)
    public void arrayUnhappyTest() throws ParserException, ValidationException {
        Validator v = vb.array();
        p = new JSONParser("{}");
        JSONValue json = p.nextValue();
        v.validate(json);
    }
    
    @Test
    public void andTest() throws ParserException, ValidationException {
        Validator v = vb.and(vb.simple(), vb.nr(), vb.intp());
        p = new JSONParser("123");
        JSONValue json = p.nextValue();
        v.validate(json);
    }
    
    @Test
    public void contentTest() throws ParserException, ValidationException {
        Validator v = vb.content(vb.bool());
        p = new JSONParser("[true, false, false, true, true, false]");
        JSONValue json = p.nextValue();
        v.validate(json);
    }
    
    @Test(expected=ValidationException.class)
    public void contentUnhappyTest() throws ParserException, ValidationException {
        Validator v = vb.content(vb.bool());
        p = new JSONParser("[true, false, 3, true, true, false]");
        JSONValue json = p.nextValue();
        v.validate(json);
    }
    
    @Test
    public void lengthTest() throws ParserException, ValidationException {
        Validator v = vb.length(3, 8);
        p = new JSONParser("[1, 2, 3, 4]");
        JSONValue json = p.nextValue();
        v.validate(json);
    }
    
    @Test
    public void lengthTest2() throws ParserException, ValidationException {
        Validator v = vb.length(3, 8);
        p = new JSONParser("\"abcde\"");
        JSONValue json = p.nextValue();
        v.validate(json);
    }
    
    @Test(expected=ValidationException.class)
    public void lengthUnhappyTest() throws ParserException, ValidationException {
        Validator v = vb.length(3, 8);
        p = new JSONParser("[1, 2]");
        JSONValue json = p.nextValue();
        v.validate(json);
    }
    
    @Test(expected=ValidationException.class)
    public void lengthUnhappyTest2() throws ParserException, ValidationException {
        Validator v = vb.length(3, 8);
        p = new JSONParser("\"abcdefghij\"");
        JSONValue json = p.nextValue();
        v.validate(json);
    }
    
    @Test
    public void letTest() throws ParserException, ValidationException {
        vb.intp("all-integers");
        Validator v = vb.content("content-integers", vb.ref("all-integers"));
        p = new JSONParser("[2, 3, 5, 7]");
        JSONValue json = p.nextValue();
        v.validate(json);
    }
    
    @Test
    public void notTest() throws ValidationException, ParserException {
        Validator v = vb.not(vb.intp());
        p = new JSONParser("true");
        JSONValue json = p.nextValue();
        v.validate(json);
    }
    
    @Test(expected=ValidationException.class)
    public void notUnhappyTest() throws ValidationException, ParserException {
        Validator v = vb.not(vb.intp());
        p = new JSONParser("17");
        JSONValue json = p.nextValue();
        v.validate(json);
    }
    
    @Test
    public void orTest() throws ParserException, ValidationException {
        Validator v = vb.or(vb.intp(), vb.bool());
        p = new JSONParser("17");
        JSONValue json = p.nextValue();
        v.validate(json);
        
        p = new JSONParser("true");
        json = p.nextValue();
        v.validate(json);
    }
    
    @Test(expected=ValidationException.class)
    public void orUnhappyTest() throws ParserException, ValidationException {
        Validator v = vb.or(vb.intp(), vb.bool());
        p = new JSONParser("17.67");
        JSONValue json = p.nextValue();
        v.validate(json);
    }
    
    @Test
    public void propertiesTest() throws ParserException, ValidationException {
        Validator v = vb.properties(
                new PropRule("name", vb.string(), false),
                new PropRule("age", vb.intp(), false));
        p = new JSONParser("{\"name\":\"Jack\", \"age\": 23}");
        JSONValue json = p.nextValue();
        v.validate(json);
    }
    
    @Test(expected=ValidationException.class)
    public void propertiesUnhappyTest() throws ParserException, ValidationException {
        Validator v = vb.properties(
                new PropRule("name", vb.string(), false),
                new PropRule("age", vb.intp(), false));
        p = new JSONParser("{\"name\":\"Jack\"}");
        JSONValue json = p.nextValue();
        v.validate(json);
    }
    
    @Test
    public void rangeTest() throws ParserException, ValidationException{
        Validator v = vb.range(5, 10);
        p = new JSONParser("7");
        JSONValue json = p.nextValue();
        v.validate(json);
    }
    
    @Test(expected=ValidationException.class)
    public void rangeUnhappyTest() throws ParserException, ValidationException{
        Validator v = vb.range(5, 10);
        p = new JSONParser("13");
        JSONValue json = p.nextValue();
        v.validate(json);
    }
    
    @Test
    public void regexpTest() throws ParserException, ValidationException {
        Validator v = vb.regexp("a+b+c+");
        p = new JSONParser("\"aaaaabbbbbbbc\"");
        JSONValue json = p.nextValue();
        v.validate(json);
    }
    
    @Test(expected=ValidationException.class)
    public void regexpUnhappyTest() throws ParserException, ValidationException {
        Validator v = vb.regexp("a+b+c+");
        p = new JSONParser("\"aaaaabbbbbbbcXX\"");
        JSONValue json = p.nextValue();
        v.validate(json);
    }
    
    @Test
    public void switchTest() throws ValidationException, ParserException{
        Validator v = vb.switchrule("flag", 
                new SwitchCase(new JSONString("hey"), vb.intp()),
                new SwitchCase(new JSONString("ho"), vb.truep()));
        p = new JSONParser("{\"flag\": \"ho\"}");
        JSONValue json = p.nextValue();
        v.validate(json);
    }
}
