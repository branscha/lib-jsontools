package com.sdicons.json.validator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.sdicons.json.model.JSONString;
import com.sdicons.json.model.JSONValue;
import com.sdicons.json.validator.predicates.And;
import com.sdicons.json.validator.predicates.Array;
import com.sdicons.json.validator.predicates.Bool;
import com.sdicons.json.validator.predicates.Complex;
import com.sdicons.json.validator.predicates.Content;
import com.sdicons.json.validator.predicates.Decimal;
import com.sdicons.json.validator.predicates.Enumeration;
import com.sdicons.json.validator.predicates.False;
import com.sdicons.json.validator.predicates.Int;
import com.sdicons.json.validator.predicates.Length;
import com.sdicons.json.validator.predicates.Not;
import com.sdicons.json.validator.predicates.Nr;
import com.sdicons.json.validator.predicates.Null;
import com.sdicons.json.validator.predicates.Or;
import com.sdicons.json.validator.predicates.Properties;
import com.sdicons.json.validator.predicates.Properties.PropRule;
import com.sdicons.json.validator.predicates.Range;
import com.sdicons.json.validator.predicates.Ref;
import com.sdicons.json.validator.predicates.Regexp;
import com.sdicons.json.validator.predicates.Simple;
import com.sdicons.json.validator.predicates.Str;
import com.sdicons.json.validator.predicates.Switch;
import com.sdicons.json.validator.predicates.Switch.SwitchCase;
import com.sdicons.json.validator.predicates.True;

public class ValidatorBuilder {

    private HashMap<String,Validator> ruleSet = new HashMap<String, Validator>();

    public Validator getRule(String ruleName) {
        if(ruleSet.containsKey(ruleName)) return ruleSet.get(ruleName);
        else throw new IllegalArgumentException(String.format("EXCxxx: Rule '%s' does not exist.", ruleName));
    }
    
    public Validator bool() {
        return new Bool("bool");
    }

    public Validator bool(String ruleName) {
        Validator validator = new Bool(ruleName);
        ruleSet.put(ruleName, validator);
        return validator;
    }

    public Validator range(Integer min, Integer max) {
        return new Range("range", min, max);
    }

    public Validator range(String ruleName, Integer min, Integer max) {
        Validator validator = new Range(ruleName, min, max);
        ruleSet.put(ruleName,  validator);
        return validator;
    }

    public Validator and(String ruleName, Validator ...validators) {
        Validator validator = new And(ruleName, validators);
        ruleSet.put(ruleName, validator);
        return validator;
    }

    public Validator and(Validator ...validators) {
        return  new And("and", validators);
    }

    public Validator ref(String ruleName, String ref) {
        Validator validator = new Ref(ruleName, ref, ruleSet);
        ruleSet.put(ruleName, validator);
        return validator;
    }

    public Validator ref(String ref) {
        return new Ref("ref", ref, ruleSet);
    }

    public Validator array(String ruleName) {
        Validator validator = new Array(ruleName);
        ruleSet.put(ruleName, validator);
        return validator;
    }

    public Validator array() {
        return new Array("array");
    }

    public Validator complex(String ruleName) {
        Validator validator = new Complex(ruleName);
        ruleSet.put(ruleName, validator);
        return validator;
    }

    public Validator complex() {
        return new Complex("complex");
    }

    public Validator content(String ruleName, Validator rule) {
        Validator validator = new Content(ruleName, rule);
        ruleSet.put(ruleName, validator);
        return validator;
    }

    public Validator content(Validator rule) {
        return new Content("content", rule);
    }

    public Validator decimal(String ruleName) {
        Validator validator = new Decimal(ruleName);
        ruleSet.put(ruleName, validator);
        return validator;
    }

    public Validator decimal() {
        return new Decimal("decimal");
    }

    public Validator enumeration(String ruleName, JSONValue ... values) {
        Validator validator = new Enumeration(ruleName, values);
        ruleSet.put(ruleName, validator);
        return validator;
    }

    public Validator enumeration(JSONValue ... values) {
        return new Enumeration("enumeration", values);
    }

    public Validator enumeration(String ruleName, String ... values) {
        List<JSONValue> strings = new ArrayList<JSONValue>();
        for(int i = 0; i < values.length; i++) {
            strings.add(new JSONString(values[i]));
        }

        Validator validator = new Enumeration(ruleName, strings.toArray(new JSONValue[]{}));
        ruleSet.put(ruleName, validator);
        return validator;
    }

    public Validator enumeration(String ... values) {
        List<JSONValue> strings = new ArrayList<JSONValue>();
        for(int i = 0; i < values.length; i++) {
            strings.add(new JSONString(values[i]));
        }
        return new Enumeration("enumeration", strings.toArray(new JSONValue[]{}));
    }

    public Validator truep(String ruleName) {
        Validator validator = new True(ruleName);
        ruleSet.put(ruleName, validator);
        return validator;
    }

    public Validator truep() {
        return new True("true");
    }

    public Validator falsep(String ruleName) {
        Validator validator = new False(ruleName);
        ruleSet.put(ruleName, validator);
        return validator;
    }

    public Validator falsep() {
        return new False("false");
    }

    public Validator intp(String ruleName) {
        Validator validator = new Int(ruleName);
        ruleSet.put(ruleName, validator);
        return validator;
    }

    public Validator intp() {
        return new Int("int");
    }

    public Validator length(Integer min, Integer max) {
        return new Length("length", min, max);
    }

    public Validator length(String ruleName, Integer min, Integer max) {
        Validator validator = new Length(ruleName, min, max);
        ruleSet.put(ruleName,  validator);
        return validator;
    }

    public Validator not(String ruleName, Validator rule) {
        Validator validator = new Not(ruleName, rule);
        ruleSet.put(ruleName, validator);
        return validator;
    }

    public Validator not(Validator rule) {
        return new Not("not", rule);
    }

    public Validator nr(String ruleName) {
        Validator validator = new Nr(ruleName);
        ruleSet.put(ruleName, validator);
        return validator;
    }

    public Validator nr() {
        return new Nr("nr");
    }

    public Validator nullp(String ruleName) {
        Validator validator = new Null(ruleName);
        ruleSet.put(ruleName, validator);
        return validator;
    }

    public Validator nullp() {
        return new Null("null");
    }

    public Validator object(String ruleName) {
        Validator validator = new com.sdicons.json.validator.predicates.Object(ruleName);
        ruleSet.put(ruleName, validator);
        return validator;
    }

    public Validator object() {
        return new  com.sdicons.json.validator.predicates.Object("object");
    }

    public Validator or(String ruleName, Validator ...validators) {
        Validator validator = new Or(ruleName, validators);
        ruleSet.put(ruleName, validator);
        return validator;
    }

    public Validator or(Validator ...validators) {
        return  new Or("or", validators);
    }

    public Validator regexp(String ruleName, String pattern) {
        Validator validator = new Regexp(ruleName, pattern);
        ruleSet.put(ruleName, validator);
        return validator;
    }

    public Validator regexp(String pattern) {
        return new Regexp("regexp", pattern);
    }

    public Validator simple(String ruleName) {
        Validator validator = new Simple(ruleName);
        ruleSet.put(ruleName, validator);
        return validator;
    }

    public Validator simple() {
        return new Simple("simple");
    }

    public Validator string(String ruleName) {
        Validator validator = new Str(ruleName);
        ruleSet.put(ruleName, validator);
        return validator;
    }

    public Validator string() {
        return new Str("string");
    }
    
    public Validator properties(String ruleName, PropRule ... rules){
        Validator validator = new Properties(ruleName, ruleSet, rules);
        ruleSet.put(ruleName, validator);
        return validator;
    }
    
    public Validator properties(PropRule ... rules){
        return new Properties("properties", ruleSet, rules);
    }
    
    public Validator switchrule(String ruleName, String discriminator, SwitchCase ...cases) {
        Validator validator = new Switch(ruleName, discriminator, ruleSet, cases);
        ruleSet.put(ruleName, validator);
        return validator;
    }
    
    public Validator switchrule(String discriminator, SwitchCase ...cases) {
        return new Switch("switch", discriminator, ruleSet, cases);
    }
    
    public static SwitchCase switchcase(Validator rule, JSONValue ...values) {
        return new SwitchCase(Arrays.asList(values), rule);
    }
    
    public static PropRule propRule(String propName, Validator rule, boolean optional){
        return new PropRule(propName, rule, optional);
    }
}