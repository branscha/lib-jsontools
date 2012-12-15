/*******************************************************************************
 * Copyright (c) 2006-2013 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
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

/**
 * A builder class to create atomic and composite JSON validators.
 * The validators can be constructed manually but it is a much easier job
 * using this builder.
 *
 * <pre>
 * <code>
 * Validator v = vb.range(50, 100);
 * p = new JSONParser("55");
 * json = p.nextValue();
 * v.validate(json);
 * </code>
 * </pre>
 *
 */
public class ValidatorBuilder {

    private HashMap<String,Validator> ruleSet = new HashMap<String, Validator>();

    /**
     * Get a named validator that was previously built using this builder.
     *
     * @param ruleName
     *        The name of the validator rule.
     * @return
     *        The requested validator rule.
     */
    public Validator getRule(String ruleName) {
        if(ruleSet.containsKey(ruleName)) return ruleSet.get(ruleName);
        else throw new IllegalArgumentException(String.format("EXCxxx: Rule '%s' does not exist.", ruleName));
    }

    /**
     * Create an anonymous {@link Bool} validator.
     *
     * @return A validator that validates boolean values.
     */
    public Validator bool() {
        return new Bool("bool");
    }

    /**
     * Create a named {@link Bool} validator.
     *
     * @param ruleName
     *        The name of the rule you are building.
     * @return
     *        A named validator that validates boolean values.
     */
    public Validator bool(String ruleName) {
        Validator validator = new Bool(ruleName);
        ruleSet.put(ruleName, validator);
        return validator;
    }

    /**
     * An anonymous {@link Range} validator.
     *
     * @param min
     *        The lower bound (inclusive). Value null means that there is no
     *        lower boundary.
     * @param max
     *        The upper bound (inclusive). Value null means that there is no
     *        upper boundary.
     * @return
     *        The range validator.
     */
    public Validator range(Integer min, Integer max) {
        return new Range("range", min, max);
    }

    /**
     * Create a named {@link Range} validator.
     *
     * @param ruleName
     *        The name of the rule being created.
     * @param min
     *        The lower boundary (inclusive). Value null means that there is no lower boundary.
     * @param max
     *        The upper bound (inclusive). Value null means that there is no upper boundary.
     * @return
     *        The named validator.
     */
    public Validator range(String ruleName, Integer min, Integer max) {
        Validator validator = new Range(ruleName, min, max);
        ruleSet.put(ruleName,  validator);
        return validator;
    }

    /**
     * Create a named and-combination {@link And}of a series of validators.
     * The validation process halts when the first failure is encountered.
     *
     * @param ruleName
     *        The name of the rule under construction.
     * @param validators
     *        The array of validators that will be combined using the and operator.
     * @return
     *        The requested validator.
     */
    public Validator and(String ruleName, Validator ...validators) {
        Validator validator = new And(ruleName, validators);
        ruleSet.put(ruleName, validator);
        return validator;
    }

    /**
     * Create an anonymous and-combination {@link And} of a series of validators.
     * The validation process halts when the first failure is encountered.
     *
     * @param validators
     *        The array of validators that will be combined using the and operator.
     * @return
     *        The requested validator.
     */
    public Validator and(Validator ...validators) {
        return  new And("and", validators);
    }

    /**
     * Create a named {@link Ref} validator. It creates an alias for another
     * named rule.
     * It is useful to refer to another rule inside a composite validator.
     *
     * @param ruleName
     *        The name of the validator under construction.
     * @param ref
     *        The name of the rule you are referring to.
     * @return
     *        The new validator.
     */
    public Validator ref(String ruleName, String ref) {
        Validator validator = new Ref(ruleName, ref, ruleSet);
        ruleSet.put(ruleName, validator);
        return validator;
    }

    /**
     * Create an anonymous {@link Ref} validator.
     * It is useful to refer to another rule inside a composite validator.
     *
     * @param ref
     *        The name of the rule you are referring to.
     * @return
     *        The new validator.
     */
    public Validator ref(String ref) {
        return new Ref("ref", ref, ruleSet);
    }

    /**
     * Create a named {@link Array} validator.
     *
     * @param ruleName
     *        The name of the rule under construction.
     * @return
     *        The newly created validator.
     */
    public Validator array(String ruleName) {
        Validator validator = new Array(ruleName);
        ruleSet.put(ruleName, validator);
        return validator;
    }

    /**
     * Create an anonymous {@link Array} validator.
     *
     * @return
     *        The newly created validator.
     */
    public Validator array() {
        return new Array("array");
    }

    /**
     * Create a named {@link Complex} validator to check if a value is
     * an array or an object.
     *
     * @param ruleName
     *        The name of the validation rule.
     * @return The validator.
     */
    public Validator complex(String ruleName) {
        Validator validator = new Complex(ruleName);
        ruleSet.put(ruleName, validator);
        return validator;
    }

    /**
     * Create an anonymus {@link Complex} validator to check if a value is
     * an array or an object.
     *
     * @return The newly created validator.
     */
    public Validator complex() {
        return new Complex("complex");
    }

    /**
     * Create a named {@link Content} validator that puts constraints on
     * the contents of an array or an object.
     *
     * @param ruleName
     *        The name of the content validation rule.
     * @param rule
     *        The validation rule that applies to the elements of the array or object.
     * @return The new content validator.
     */
    public Validator content(String ruleName, Validator rule) {
        Validator validator = new Content(ruleName, rule);
        ruleSet.put(ruleName, validator);
        return validator;
    }

    /**
     * Create an anonymous {@link Content} validator that puts constraints on
     * the contents of an array or an object.
     *
     * @param rule
     *        The validation rule that applies to the elements of the array or object.
     * @return The new content validator.
     */
    public Validator content(Validator rule) {
        return new Content("content", rule);
    }

    /**
     * Create a named {@link Decimal} validator to check if a value is a decimal.
     *
     * @param ruleName
     *        The name of the validation rule.
     * @return The new validation rule.
     */
    public Validator decimal(String ruleName) {
        Validator validator = new Decimal(ruleName);
        ruleSet.put(ruleName, validator);
        return validator;
    }

    /**
     * Create an anonymous {@link Decimal} validator to check if a value contains a decimal.
     *
     * @return The new validation rule.
     */
    public Validator decimal() {
        return new Decimal("decimal");
    }

    /**
     * Create a named {@link Enumeration} validation rule. Check if a value belongs
     * to a predefined set of values.
     *
     * @param ruleName
     *        The name of the validation rule.
     * @param values
     *        The values of the enumeration.
     * @return The enumeration validation rule.
     */
    public Validator enumeration(String ruleName, JSONValue ... values) {
        Validator validator = new Enumeration(ruleName, values);
        ruleSet.put(ruleName, validator);
        return validator;
    }

    /**
     * Create an anonymous {@link Enumeration} validation rule. Check if a value belongs
     * to a predefined set of values.
     * @param values
     *
     * @return The enumeration validation rule.
     */
    public Validator enumeration(JSONValue ... values) {
        return new Enumeration("enumeration", values);
    }

    /**
     * A convenience method to create a named {@link Enumeration} validation using a set of String values.
     * The elements of the enumeration are {@link JSONValue} instances, but this method lets you
     * construct the enumeration using plain Strings.
     *
     * @param ruleName
     *        The name of the validation rule.
     * @param values
     *        The elements of the enumeration set, they will be converted to {@link JSONString} values.
     * @return The enumeration validation rule.
     */
    public Validator enumeration(String ruleName, String ... values) {
        List<JSONValue> strings = new ArrayList<JSONValue>();
        for(int i = 0; i < values.length; i++) {
            strings.add(new JSONString(values[i]));
        }

        Validator validator = new Enumeration(ruleName, strings.toArray(new JSONValue[]{}));
        ruleSet.put(ruleName, validator);
        return validator;
    }

    /**
     * A convenience method to create a named {@link Enumeration} validation using a set of String values.
     * The elements of the enumeration are {@link JSONValue} instances, but this method lets you
     * construct the enumeration using plain Strings.
     *
     * @param values
     *        The String values of the enumeration, they will be converted to JSONStrings.
     * @return The enumeration validation rule.
     */
    public Validator enumeration(String ... values) {
        List<JSONValue> strings = new ArrayList<JSONValue>();
        for(int i = 0; i < values.length; i++) {
            strings.add(new JSONString(values[i]));
        }
        return new Enumeration("enumeration", strings.toArray(new JSONValue[]{}));
    }

    /**
     * Create a named {@link True} validation rule that always succeeds.
     * We need to include this for the logic system to be complete.
     *
     * @param ruleName The name of the validation.
     * @return The newly created validation rule.
     */
    public Validator truep(String ruleName) {
        Validator validator = new True(ruleName);
        ruleSet.put(ruleName, validator);
        return validator;
    }

    /**
     * Create an anonymous {@link True} validation rule that always succeeds.
     * We need to include this for the logic system to be complete.
     *
     * @return The newly created validation rule.
     */
    public Validator truep() {
        return new True("true");
    }

    /**
     * Create a named {@link False} validation rule that always fails.
     * We need to include this for the logic system to be complete.
     *
     * @param ruleName
     *        The name of our validation rule.
     * @return The new validation rule.
     */
    public Validator falsep(String ruleName) {
        Validator validator = new False(ruleName);
        ruleSet.put(ruleName, validator);
        return validator;
    }

    /**
     * Create an anonymous {@link False}  validatioin rule that always fails.
     * We need to include this for the logic system to be complete.
     *
     * @return The new validation rule.
     */
    public Validator falsep() {
        return new False("false");
    }

    /**
     * Create a named {@link Int} validation rule.
     *
     * @param ruleName
     *        The name for our new rule.
     * @return The new validation rule.
     */
    public Validator intp(String ruleName) {
        Validator validator = new Int(ruleName);
        ruleSet.put(ruleName, validator);
        return validator;
    }

    /**
     * Create an anonymous {@link Int} validation rule.
     *
     * @return The new validation rule.
     */
    public Validator intp() {
        return new Int("int");
    }

    /**
     * Create an anonymous {@link Length} validation rule to check the length of an array or a string.
     *
     * @param min
     *        The minimum length for the value (string or array).
     * @param max
     *        The maximum length the value can have (string or array).
     * @return The new validator.
     */
    public Validator length(Integer min, Integer max) {
        return new Length("length", min, max);
    }

    /**
     * Create a named {@link Length} validator to check the length of an array or a string.
     *
     * @param ruleName
     *        The name of the validation rule.
     * @param min
     *        The minimum length for the value (string or array).
     * @param max
     *        The maximum length for the value (string or array).
     * @return The new length validator.
     */
    public Validator length(String ruleName, Integer min, Integer max) {
        Validator validator = new Length(ruleName, min, max);
        ruleSet.put(ruleName,  validator);
        return validator;
    }

    /**
     * Create a named logical {@link Not} validation which flips the result of an inner validation.
     *
     * @param ruleName
     *        The name of our validation rule.
     * @param rule
     *        The inner validation, the result will be reversed.
     * @return The new validator.
     */
    public Validator not(String ruleName, Validator rule) {
        Validator validator = new Not(ruleName, rule);
        ruleSet.put(ruleName, validator);
        return validator;
    }

    /**
     * Create an anonymous logical {@link Not} validation which reverses the result of an inner validation.
     *
     * @param rule
     *        The inner validation, the result of it will be reversed.
     * @return The new validator.
     */
    public Validator not(Validator rule) {
        return new Not("not", rule);
    }

    /**
     * Create a named {@link Nr} validator to see if a value is a number.
     *
     * @param ruleName
     *        The name of the validation rule.
     * @return The new validator.
     */
    public Validator nr(String ruleName) {
        Validator validator = new Nr(ruleName);
        ruleSet.put(ruleName, validator);
        return validator;
    }

    /**
     * Create an anonymous {@link Nr} validator to see if a value is a number.
     *
     * @return The new validator.
     */
    public Validator nr() {
        return new Nr("nr");
    }

    /**
     * Create a named {@link Null} checking validator.
     *
     * @param ruleName
     *        A name for the validation rule.
     * @return The new validator.
     */
    public Validator nullp(String ruleName) {
        Validator validator = new Null(ruleName);
        ruleSet.put(ruleName, validator);
        return validator;
    }

    /**
     * Create an anonymous {@link Null} checking validator.
     *
     * @return The new validator.
     */
    public Validator nullp() {
        return new Null("null");
    }

    /**
     * Create a named {@link com.sdicons.json.validator.predicates.Object} validator to check if a value
     * is an object.
     *
     * @param ruleName
     *        The name of the rule.
     * @return The new validator.
     */
    public Validator object(String ruleName) {
        Validator validator = new com.sdicons.json.validator.predicates.Object(ruleName);
        ruleSet.put(ruleName, validator);
        return validator;
    }

    /**
     * Create an anonymous {@link com.sdicons.json.validator.predicates.Object} validator to check if a value
     * is an object
     *
     * @return The new validator.
     */
    public Validator object() {
        return new  com.sdicons.json.validator.predicates.Object("object");
    }

    /**
     * Create a named composite {@link Or} validation. The value must match against one of the inner rules.
     *
     * @param ruleName
     *        The name for our rule.
     * @param validators
     *        The inner validators, one of it must succeed on the value for the complete matcher to succeed.
     * @return The new validator.
     */
    public Validator or(String ruleName, Validator ...validators) {
        Validator validator = new Or(ruleName, validators);
        ruleSet.put(ruleName, validator);
        return validator;
    }

    /**
     * Create an anonymous {@link Or} validation rule. The value must match against one of the inner rules.
     *
     * @param validators
     *        The inner validators, one of it must succeed on the value for the complete matcher to succeed.
     * @return The new validator.
     */
    public Validator or(Validator ...validators) {
        return  new Or("or", validators);
    }

    /**
     * Create a named {@link Regexp} validator to validate strings against regular expressions.
     * @param ruleName
     *        The name of the regular expression rule.
     * @param pattern
     *        The regular expression itself.
     * @return The new validator.
     */
    public Validator regexp(String ruleName, String pattern) {
        Validator validator = new Regexp(ruleName, pattern);
        ruleSet.put(ruleName, validator);
        return validator;
    }

    /**
     * Create an anonymous {@link Regexp} validator to validate strings against regular expressions.
     *
     * @param pattern
     *        The regular expression.
     * @return The new validator.
     */
    public Validator regexp(String pattern) {
        return new Regexp("regexp", pattern);
    }

    /**
     * Create a named {@link Simple} validator to check that a value is atomic.
     *
     * @param ruleName
     *        The name of the new validator.
     * @return The new validator.
     */
    public Validator simple(String ruleName) {
        Validator validator = new Simple(ruleName);
        ruleSet.put(ruleName, validator);
        return validator;
    }

    /**
     * Create an anonymous {@link Simple} validator to check that a value is atomic.
     *
     * @return the new validator.
     */
    public Validator simple() {
        return new Simple("simple");
    }

    /**
     * Create a named {@link Str} validator to check if a value is a string.
     *
     * @param ruleName
     *        The name of the validator.
     * @return The new validator.
     */
    public Validator string(String ruleName) {
        Validator validator = new Str(ruleName);
        ruleSet.put(ruleName, validator);
        return validator;
    }

    /**
     * Create an anonymous {@link Str} validator to check if a value is a string.
     *
     * @return The new validator.
     */
    public Validator string() {
        return new Str("string");
    }

    /**
     * Create a named {@link Properties} validator to check the key/value pairs of an object.
     *
     * @param ruleName
     *        The name of the validator.
     * @param rules
     *        An array of {@link PropRule}, each rule can validate a specific property of the object.
     *        You can use the {@link ValidatorBuilder#propRule(String, Validator, boolean)} method to
     *        quickly create the property rules.
     * @return The new validator.
     */
    public Validator properties(String ruleName, PropRule ... rules){
        Validator validator = new Properties(ruleName, ruleSet, rules);
        ruleSet.put(ruleName, validator);
        return validator;
    }

    /**
     * Create an anonymous {@link Properties} validator to check the key/value pairs of an object.
     *
     * @param rules
     *        An array of {@link PropRule}, each rule can validate a specific property of the object.
     *        You can use the {@link ValidatorBuilder#propRule(String, Validator, boolean)} method to
     *        quickly create the property rules.
     * @return The new validator.
     */
    public Validator properties(PropRule ... rules){
        return new Properties("properties", ruleSet, rules);
    }

    /**
     * Create a named {@link Switch} validator.
     *
     * @param ruleName
     *        The name of the rule.
     * @param discriminator
     *        The name of the field that will be used to select a {@link SwitchCase}.
     * @param cases
     *        An array of {@link SwitchCase} elements.
     *        You can use the {@link ValidatorBuilder#switchcase(Validator, JSONValue...)} method to quickly create a case.
     * @return The new validator.
     */
    public Validator switchrule(String ruleName, String discriminator, SwitchCase ...cases) {
        Validator validator = new Switch(ruleName, discriminator, ruleSet, cases);
        ruleSet.put(ruleName, validator);
        return validator;
    }

    /**
     * Create an anonymous {@link Switch} validator.
     *
     * @param discriminator
     *        The name of the field that will be used to select a {@link SwitchCase}.
     * @param cases
     *        An array of {@link SwitchCase} elements.
     *        You can use the {@link ValidatorBuilder#switchcase(Validator, JSONValue...)} method to quickly create a case.
     * @return The new validator.
     */
    public Validator switchrule(String discriminator, SwitchCase ...cases) {
        return new Switch("switch", discriminator, ruleSet, cases);
    }

    /**
     * Create a {@link SwitchCase} that can be used to build a {@link Switch} validator.
     *
     * @param rule
     *        The internal validator.
     * @param values
     *        The discriminating values. If the discriminator is one of these values, the rule
     *        will be applied to the complete object.
     * @return The SwitchCase.
     */
    public static SwitchCase switchcase(Validator rule, JSONValue ...values) {
        return new SwitchCase(Arrays.asList(values), rule);
    }

    /**
     * Create a {@link PropRule} that is used to build a {@link Properties} validator for objects.
     *
     * @param propName
     *        The name of the object property to which this rule applies.
     * @param rule
     *        The validator for this property.
     * @param optional
     *        A flag to indicate if the property is optional or not. If it is not optional, a validation
     *        error will occur if the object does not have a property with that name.
     * @return The property rule.
     */
    public static PropRule propRule(String propName, Validator rule, boolean optional){
        return new PropRule(propName, rule, optional);
    }
}
