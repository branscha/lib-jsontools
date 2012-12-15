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
    Validator v;
    JSONParser p;
    JSONValue json;

    @Before
    public void init() {
        vb = new ValidatorBuilder();
    }

    @Test
    public void boolTest() throws ParserException, ValidationException {
        v = vb.bool();
        p = new JSONParser("true");
        json = p.nextValue();
        v.validate(json);
    }

    @Test(expected=ValidationException.class)
    public void boolUnhappyTest() throws ParserException, ValidationException {
        v = vb.bool();
        p = new JSONParser("123");
        json = p.nextValue();
        v.validate(json);
    }
    
    @Test
    public void complexTest() throws ParserException, ValidationException {
        v = vb.complex();
        p = new JSONParser("{}");
        json = p.nextValue();
        v.validate(json);
        
        p = new JSONParser("[]");
        json = p.nextValue();
        v.validate(json);
    }
    
    @Test(expected=ValidationException.class)
    public void complexUnhappyTest() throws ParserException, ValidationException {
        v = vb.complex();
        p = new JSONParser("true");
        json = p.nextValue();
        v.validate(json);
    }

    @Test
    public void decimalTest() throws ParserException, ValidationException {
        v = vb.decimal();
        p = new JSONParser("123.5e-3");
        json = p.nextValue();
        v.validate(json);
    }

    @Test(expected=ValidationException.class)
    public void decimalUnhappyTest() throws ParserException, ValidationException {
        v = vb.decimal();
        p = new JSONParser("false");
        json = p.nextValue();
        v.validate(json);
    }

    @Test
    public void enumTest() throws ParserException, ValidationException {
        v = vb.enumeration(new String[]{"RED", "GREEN", "BLUE"});
        p = new JSONParser("\"GREEN\"");
        json = p.nextValue();
        v.validate(json);
    }

    @Test(expected=ValidationException.class)
    public void enumUnhappyTest() throws ParserException, ValidationException {
        v = vb.enumeration(new String[]{"RED", "GREEN", "BLUE"});
        p = new JSONParser("\"ORANGE\"");
        json = p.nextValue();
        v.validate(json);
    }

    @Test(expected=ValidationException.class)
    public void falseTest() throws ParserException, ValidationException {
        // It always fails.
        v = vb.falsep();
        p = new JSONParser("false");
        json = p.nextValue();
        v.validate(json);
    }

    @Test
    public void integerTest() throws ParserException, ValidationException {
        v = vb.intp();
        p = new JSONParser("12345");
        json = p.nextValue();
        v.validate(json);
    }

    @Test(expected=ValidationException.class)
    public void integerUnhappyTest() throws ParserException, ValidationException {
        v = vb.intp();
        p = new JSONParser("{}");
        json = p.nextValue();
        v.validate(json);
    }

    @Test
    public void nullTest() throws ParserException, ValidationException {
        v = vb.nullp();
        p = new JSONParser("null");
        json = p.nextValue();
        v.validate(json);
    }

    @Test(expected=ValidationException.class)
    public void nullUnhappyTest() throws ParserException, ValidationException {
        v = vb.nullp();
        p = new JSONParser("1234");
        json = p.nextValue();
        v.validate(json);
    }

    @Test
    public void numberTest() throws ParserException, ValidationException {
        v = vb.nr();
        p = new JSONParser("12345");
        json = p.nextValue();
        v.validate(json);

        p = new JSONParser("3.1415");
        json = p.nextValue();
        v.validate(json);
    }

    @Test(expected=ValidationException.class)
    public void numberUnhappyTest() throws ParserException, ValidationException {
        v = vb.nr();
        p = new JSONParser("{}");
        json = p.nextValue();
        v.validate(json);
    }

    @Test
    public void objectTest() throws ParserException, ValidationException {
        v = vb.object();
        p = new JSONParser("{\"key\": 123}");
        json = p.nextValue();
        v.validate(json);
    }

    @Test(expected=ValidationException.class)
    public void objectUnhappyTest() throws ParserException, ValidationException {
        v = vb.object();
        p = new JSONParser("false");
        json = p.nextValue();
        v.validate(json);
    }

    @Test
    public void simpleTest() throws ParserException, ValidationException {
        v = vb.simple();
        p = new JSONParser("123");
        json = p.nextValue();
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
        v = vb.simple();
        p = new JSONParser("[1, 2, 3]");
        json = p.nextValue();
        v.validate(json);
    }

    @Test
    public void stringTest() throws ParserException, ValidationException {
        v = vb.string();
        p = new JSONParser("\"abcdef\"");
        json = p.nextValue();
        v.validate(json);
    }

    @Test(expected=ValidationException.class)
    public void stringUnhappyTest() throws ParserException, ValidationException {
        v = vb.string();
        p = new JSONParser("{}");
        json = p.nextValue();
        v.validate(json);
    }

    @Test
    public void trueTest() throws ParserException, ValidationException {
        // It always succeeds.
        v = vb.truep();
        p = new JSONParser("[]");
        json = p.nextValue();
        v.validate(json);
    }

    @Test
    public void arrayTest() throws ParserException, ValidationException {
        v = vb.array();
        p = new JSONParser("[1, 2, 3]");
        json = p.nextValue();
        v.validate(json);
    }

    @Test(expected=ValidationException.class)
    public void arrayUnhappyTest() throws ParserException, ValidationException {
        v = vb.array();
        p = new JSONParser("{}");
        json = p.nextValue();
        v.validate(json);
    }

    @Test
    public void andTest() throws ParserException, ValidationException {
        v = vb.and(vb.simple(), vb.nr(), vb.intp());
        p = new JSONParser("123");
        json = p.nextValue();
        v.validate(json);
    }

    @Test
    public void contentTest() throws ParserException, ValidationException {
        v = vb.content(vb.bool());
        p = new JSONParser("[true, false, false, true, true, false]");
        json = p.nextValue();
        v.validate(json);
    }

    @Test(expected=ValidationException.class)
    public void contentUnhappyTest() throws ParserException, ValidationException {
        v = vb.content(vb.bool());
        p = new JSONParser("[true, false, 3, true, true, false]");
        json = p.nextValue();
        v.validate(json);
    }

    @Test
    public void lengthTest() throws ParserException, ValidationException {
        v = vb.length(3, 8);
        p = new JSONParser("[1, 2, 3, 4]");
        json = p.nextValue();
        v.validate(json);
    }

    @Test
    public void lengthTest2() throws ParserException, ValidationException {
        v = vb.length(3, 8);
        p = new JSONParser("\"abcde\"");
        json = p.nextValue();
        v.validate(json);
    }

    @Test(expected=ValidationException.class)
    public void lengthUnhappyTest() throws ParserException, ValidationException {
        v = vb.length(3, 8);
        p = new JSONParser("[1, 2]");
        json = p.nextValue();
        v.validate(json);
    }

    @Test(expected=ValidationException.class)
    public void lengthUnhappyTest2() throws ParserException, ValidationException {
        v = vb.length(3, 8);
        p = new JSONParser("\"abcdefghij\"");
        json = p.nextValue();
        v.validate(json);
    }

    @Test
    public void letTest() throws ParserException, ValidationException {
        vb.intp("all-integers");
        v = vb.content("content-integers", vb.ref("all-integers"));
        p = new JSONParser("[2, 3, 5, 7]");
        json = p.nextValue();
        v.validate(json);
    }

    @Test
    public void notTest() throws ValidationException, ParserException {
        v = vb.not(vb.intp());
        p = new JSONParser("true");
        json = p.nextValue();
        v.validate(json);
    }

    @Test(expected=ValidationException.class)
    public void notUnhappyTest() throws ValidationException, ParserException {
        v = vb.not(vb.intp());
        p = new JSONParser("17");
        json = p.nextValue();
        v.validate(json);
    }

    @Test
    public void orTest() throws ParserException, ValidationException {
        v = vb.or(vb.intp(), vb.bool());
        p = new JSONParser("17");
        json = p.nextValue();
        v.validate(json);

        p = new JSONParser("true");
        json = p.nextValue();
        v.validate(json);
    }

    @Test(expected=ValidationException.class)
    public void orUnhappyTest() throws ParserException, ValidationException {
        v = vb.or(vb.intp(), vb.bool());
        p = new JSONParser("17.67");
        json = p.nextValue();
        v.validate(json);
    }

    @Test
    public void propertiesTest() throws ParserException, ValidationException {
        v = vb.properties(
                new PropRule("name", vb.string(), false),
                new PropRule("age", vb.intp(), false));
        p = new JSONParser("{\"name\":\"Jack\", \"age\": 23}");
        json = p.nextValue();
        v.validate(json);
    }

    @Test(expected=ValidationException.class)
    public void propertiesUnhappyTest() throws ParserException, ValidationException {
        v = vb.properties(
                new PropRule("name", vb.string(), false),
                new PropRule("age", vb.intp(), false));
        p = new JSONParser("{\"name\":\"Jack\"}");
        json = p.nextValue();
        v.validate(json);
    }

    @Test
    public void rangeTest() throws ParserException, ValidationException{
        v = vb.range(5, 10);
        p = new JSONParser("7");
        json = p.nextValue();
        v.validate(json);
    }

    @Test(expected=ValidationException.class)
    public void rangeUnhappyTest() throws ParserException, ValidationException{
        v = vb.range(5, 10);
        p = new JSONParser("13");
        json = p.nextValue();
        v.validate(json);
    }

    @Test
    public void regexpTest() throws ParserException, ValidationException {
        v = vb.regexp("a+b+c+");
        p = new JSONParser("\"aaaaabbbbbbbc\"");
        json = p.nextValue();
        v.validate(json);
    }

    @Test(expected=ValidationException.class)
    public void regexpUnhappyTest() throws ParserException, ValidationException {
        v = vb.regexp("a+b+c+");
        p = new JSONParser("\"aaaaabbbbbbbcXX\"");
        json = p.nextValue();
        v.validate(json);
    }

    @Test
    public void switchTest() throws ValidationException, ParserException{
        v = vb.switchrule("flag",
                new SwitchCase(new JSONString("hey"), vb.falsep()),
                new SwitchCase(new JSONString("ho"), vb.truep()));
        p = new JSONParser("{\"flag\": \"ho\"}");
        json = p.nextValue();
        v.validate(json);
    }

    @Test(expected=ValidationException.class)
    public void switchUnhappyTest() throws ValidationException, ParserException{
        v = vb.switchrule("flag",
                new SwitchCase(new JSONString("hey"), vb.falsep()),
                new SwitchCase(new JSONString("ho"), vb.truep()));
        p = new JSONParser("{\"flag\": \"hey\"}");
        json = p.nextValue();
        v.validate(json);
    }
}
