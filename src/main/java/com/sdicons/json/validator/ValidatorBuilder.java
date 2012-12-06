package com.sdicons.json.validator;

import java.util.HashMap;

import com.sdicons.json.validator.predicates.Bool;

public class ValidatorBuilder {
    
    private HashMap<String,Validator> ruleSet = new HashMap<String, Validator>();
    
    public Validator bool() {
        return new Bool("bool");
    }
    
    public Validator bool(String ruleName) {
        Validator validator = new Bool(ruleName);
        ruleSet.put(ruleName, validator);
        return validator;
    }
    
}
