package com.sdicons.json.validator;

import org.junit.Before;
import org.junit.Test;

import com.sdicons.json.parser.JSONParser;

public class ValidatorBuilderTest {
    
    ValidatorBuilder vb;
    JSONValidator v;
    JSONParser p;
    
    @Before
    public void init() {
        vb = new ValidatorBuilder();
    }

    @Test
    public void boolTest() {
    }
}
