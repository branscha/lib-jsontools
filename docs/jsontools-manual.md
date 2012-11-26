# 1. JSON Tools
## 1.1. Introduction

JSON (JavaScript Object Notation) is a file format to represent data. It is similar to XML but has different characteristics. It is suited to represent configuration information, implement communication protocols and so on.  XML is more suited to represent annotated documents. JSON parsing is very fast, the parser can be kept lean and mean. It is easy for humans to read and write. It is based on a subset of the [JavaScript](http://www.ecma-international.org/publications/standards/Ecma-262.htm) programming language.  JSON is a text format that is completely language independent but uses conventions that are familiar to programmers of the C-family of languages.  These properties make JSON an ideal data-interchange language. The format is specified on the [JSON web site](http://www.json.org/), for the details please visit this site.

JSON is a very simple format. As a result, the parsing and rendering is fast and easy, you can concentrate on  the content of the file in stead of the format. In XML it is often difficult to fully understand all features (e.g. name spaces, validation, ...). As a result, XML tends to become part of the problem i.s.o. the solution. In JSON everything is well defined, all aspects of the representation are clear, you can concentrate on how you are going to represent your application concepts. The following example comes from the [JSON example page](http://www.json.org/example.html).

	{ "widget" : {
	      "debug" : "on",
	      "text" : {
	         "onMouseUp" : "sun1.opacity = (sun1.opacity / 100) * 90;",
	         "hOffset" : 250,
	         "data" : "Click Here",
	         "alignment" : "center",
	         "style" : "bold",
	         "size" : 36,
	         "name" : "text1",
	         "vOffset" : 100 },
	      "image" : {
	         "hOffset" : 250,
	         "alignment" : "center",
	         "src" : "Images/Sun.png",
	         "name" : "sun1",
	         "vOffset" : 250 },
	      "window" : {
	         "width" : 500,
	         "height" : 500,
	         "title" : "Sample Konfabulator Widget",
	         "name" : "main_window" } } }

This project wants to provide the tools to manipulate and use the format in a Java application. 

## 1.2. Acknowledgments

The JSON Tools library is the result of many suggestions, contributions and reviews from the users. Without the feedback the library would not be as versatile and stable as it is today. Thank you for all the feedback that makes the library better.


## 1.4. License

The library is released under the free MIT license.

## 1.5. JSON Extensions

Comments. I added line comments which start with "\#". It is easier for the examples to be able to put comments in the file. The comments are not retained, they are skipped and ignored.

# 2. The Tools

## 2.1. Parsing - Reading JSON

The most important tool in the tool set is the parser, it enables you to convert a JSON file or stream into a Java model. All JSON objects remember the position in the file (line, column), so if you are doing post processing of the data you can always refer to the position in the original file. 

Invoking the parser is very simple as you can see in this example:

	JSONParser lParser = new JSONParser(JSONTest.class.getResourceAsStream("/config.json"));
	JSONValue lValue = lParser.nextValue();

The JSON model is a hierarchy of types, the hierarchy looks like this:

	JSONValue
	   JSONComplex
	      JSONObject
	      JSONArray
	   JSONSimple
	      JSONNull
	      JSONBoolean
	      JSONString
	      JSONNumber
	         JSONInteger
	         JSonDecimal

## 2.2. Rendering - Writing JSON

The classes in the JSON model can render themselves to a String. You can choose to render to a pretty form, nicely indented and easily readable by humans, or you can render to a compact form, no spaces or indentations are provided. This is suited to use on a communications channel when you are implementing a communication protocol.

In the introduction we already saw a pretty rendering of some widget data. The same structure can be rendered without pretty printing in order to reduce white space. This can be an interesting feature when space optimization is very important, e.g. communication protocols.

## 2.3. Mapping
### When to choose mapping

Both mapping tool (this section) and serialization tool can be used to convert Java into JSON and vice versa. These tools have different goals. The goals of the mapper are:

*  The JSON text should be clean and straightforward. So no meta information can be stored.
*  The data contained in the JSON text should not be dependent on the Java programming language. You do not need to know that the data was produced in Java or that it will be parsed in Java.
*  Not all Java data structures have to be mapped: recursive structures, differentiation between primitives and reference types are less important. If a trade-off has to be made, it will be in favor of the JSON format.

The JSON from the mapper can be easily interpreted in another language. An example could be JavaScript in the context of an [AJAX](http://en.wikipedia.org/wiki/AJAX) communication with the server. The service could be talking some JSON protocol. It is not difficult to map Java data to JSON. It can be done like this:

	import com.sdicons.json.mapper.*;
	...
	JSONMapper mapper = new JSONMapper();
	JSONValue obj = mapper.toJSON(myPojo);

Converting back to Java is done like this:

	import com.sdicons.json.mapper.*;
	...
	JSONMapper mapper = new JSONMapper();
	MyBean bean = (MyBean) mapper.toJava(obj, MyBean.class);

Note that the mapper needs some help to convert JSON into Java. As we stated in the goals of the mapper, we cannot store meta information in the JSON text. As a result the mapper cannot know how the JSON text should be mapped. Therefore we pass a class to the mapper (line 3) so that the mapper can exploit this information. In fact, there are two kinds of information the mapper can work with (1) classes as in the example and (2) types e.g. List<Integer>. The rationale for this might be illustrated by the following example. Consider a JSON text 

	[ "01/12/2006", "03/12/2007", ... ]


This list could be interpreted as a list of Strings, but also as a list of Dates. The mapper has no idea what to do with it. When we pass the type LinkedList<Date> or the type LinkedList<String>, the mapper can exploit this type information and do the right thing. Also note that the mapper automatically exploits this information when the outer layer is a bean, and the list is one of the beans properties.

### The mapping process ###

The mapper uses a repository of helpers. Each helper is specialized in mapping instances of a certain class or interface. The mappers are organized in the repository in a hierarchical way, ordered according to the class hierarchy. When mapping an object, the mapper will try to find the most specific helper available. The default hierarchy looks like this:

	// Calling this method:
	System.out.println(mapper.getRepository().prettyPrint());
	
	// Results in this output:
	java.lang.Object
	   java.lang.String
	   java.lang.Boolean
	   java.lang.Byte
	   java.lang.Short
	   java.lang.Integer
	   java.lang.Long
	   java.lang.Float
	   java.lang.Double
	   java.math.BigInteger
	   java.math.BigDecimal
	   java.lang.Character
	   java.util.Date
	   java.util.Collection
	   java.util.Map
	
The basic Java types (byte, int, char, ..., arrays) are handled internally by the mapper, no helpers are used for this. For all reference types, the repository is used to find an appropriate handler. If there is no specific helper available, the mapper will eventually use the root mapper. Currently there are two flavors available of the root mapper that handles java.lang.Object.

*  ObjectMapper is the default helper for objects that have no specific helper. It tries to access the object as a JavaBean. The object has to have an empty constructor, and the helper will only look at the getters/setters to retrieve the contents of the bean. This helper is the default root helper for compatibility reasons with earlier versions of the JSON Tools. The JavaBean helper can be explicitly activated by calling the method JSONMapper.useJavaBeanAccess().
*  ObjectMaperDirect is optional, this helper will access the fields directly, no getters or setters are needed. The fields can even be private. This POJO helper can be activated by calling the method JSONMapper.usePojoAccess();.


It is also possible to add your own mapper helpers to the repository. As you can see, the default repository is only two levels deep, but it can be much more specialized according to the business needs. There are two different ways to create a helper or to influence the mapping process.

*  @JSONMap, @JSONConstruct. If the ObjectHelperDirect is activated as described above, then the class that you want to map can simply annotate two methods with these annotations. The @JSONMap annotation has to be used to mark a method that returns an object array. This method will be called by the mapping process when an instance of the class is mapped from the Java model to JSON. These values will be used by the mapper when the instance is mapped from JSON to Java by invoking the constructor that is annotated with the @JSONConstruct annotation.
*  Another way to create a helper is to create a new class, derived from SimpleMapperHelper, and add it to the mapper repository by calling the method JSONMapper.addHelper(myHelper).


Here is an example of an annotated class. It is the first solution, in combination with ObjectMapperDirect. Do not forget to activate the POJO mapper.


	public class MyDate
	{
	    // The fields will be mapped as well, independent of the 
	    // constructor values.
	    private Date theDate;
	    private String theTimeZone;
	    
	    // Because of this annotation, the ObjectMapperDirect will call this 
	    // function and serialize the values in the object array. These values 
	    // will be used later on to call the annotated constructor. 
	    @JSONMap   
	    public Object[] getTime()
	    {
	         return new Object[]{theDate.getTime(), theTimeZone};
	    }
	
	    // This constructor will be called with the same values that were 
	    // provided by the other annotated method.
	    @JSONConstruct
	    public MyDate(long aTime, String aTimeZone)
	    {
	        theDate = new Date(aTime);
	        theTimeZone = aTimeZone;
	    }        
	}

## 2.4. Serialization ##

Both mapping tool and serialization tool (this section) can be used to convert Java into JSON and vice versa. These tools have different goals. The goals of the serializer are:


*  The serialization tool could be an alternative for native [serialization](http://java.sun.com/j2se/1.5.0/docs/guide/serialization/) (regarding functionality). This does not mean that all kinds of classes are supported out of the box, but it means that the general mechanism should be there and there should be an easy way to extend the mechanism so that we can deal with all classes.
*  The serialization tool should preserve the difference between reference types and primitive types.
*  Recursive types should be supported without putting the (de)serializer into an infinite loop.
*  Instance identity should be preserved. If the same instance is referenced from other instances, the same structure should be reconstructed during de-serialization. There should only be one instance representing the original referenced instance.
*  The content of the JSON text can contain meta information which can help de-serialization. We are allowed to add extra information in the JSON text in order to accomplish the other goals.


This tool enables you to render POJO's to a JSON file. It is similar to the XML serialization in Java or the XML Stream library, but it uses the JSON format. The result is a very fast text serialization, you can customize it if you want.  The code is based on the SISE project, it was adjusted to make use of and benefit from the JSON format. Marshaling (converting from Java to JSON) as well as un-marshaling is very straightforward:

	import com.sdicons.json.serializer.marshal.*;
	...
	myTestObject = ...
	JSONSerializer marshal = new JSONSerializer();
	JSONObject result = marshal.marshal(myTestObject);

And the other way around:
 
	import com.sdicons.json.serializer.marshal.*;
	...
	JSONObject myJSONObject = ...
	JSONSerializeValue lResult = marshal.unmarshal(myJSONObject);
	... = lResult.getReference()

You might wonder what the JSONSerializeValue is all about, why is un-marshaling giving an extra object back? The answer is that we went to great lengths to provide marshaling or un-marshaling for both Java reference types as Java basic types. A basic type needs to be fetched using specific methods (there is no other way). In order to provide these specific methods we need an extra class.

### Primitive Types ###

Primitive types are represented like this.

	{ ">" : "P",
	  "=" : "1",
	  "t" : "int" }

The ">"  attribute with value "P"  indicates a primitive type. The "="  attribute contains the representation of the value and the "t" attribute contains the original Java type.

### Reference Types ###

An array is defined recursively like this. We can see the ``>'' attribute this time with the "A"  value, indicating that the object represents an array. The "C" attribute contains the type representation for arrays as it is defined in Java. The ``=''  attribute contains a list of the values.

	{ ">" : "A",
	  "c" : "I",
	  "=" :
	     [
	         {
	            ">" : "P",
	            "=" : "0",
	            "t" : "int" },
	         {
	            ">" : "P",
	            "=" : "1",
	            "t" : "int" },
	         {
	            ">" : "P",
	            "=" : "2",
	            "t" : "int" },
	         {
	            ">" : "P",
	            "=" : "3",
	            "t" : "int" },
	         {
	            ">" : "P",
	            "=" : "4",
	            "t" : "int" },
	         {
	            ">" : "P",
	            "=" : "5",
	            "t" : "int" } ] }

An object is represented like this.

	{
	   ">" : "O",
	   "c" : "com.sdicons.json.serializer.MyBean",
	   "&" : "id0",
	   "=" : {
	      "int2" :
	        { ">" : "null" },
	      "ptr" :
	        { ">" : "R",
	          "*" : "id0" },
	      "name" :
	        { ">" : "O",
	          "c" : "java.lang.String",
	          "&" : "id2",
	          "=" : "This is a test..." },
	      "int1" :
	        { ">" : "null" },
	      "id" :
	        { ">" : "O",
	          "c" : "java.lang.Integer",      
	          "&" : "id1",
	          "=" : "1003" } } }

The ``>'' marker contains "O" for object this time. The "C" attribute contains a fully qualified class name. The ``\&'' contains a unique id, it can be used to refer to the object so that we are able to represent recursive data structures. The ``=''  attribute contains a JSON object having a property for each JavaBean property.  The property value is recursively a representation of a Java object. Note that there is a special notation to represent Java null values.

	{ ">" : "null" }

Also note that you can refer to other objects with the reference object which looks like this:

	{ ">" : "R",
	  "*" : "id0" }

### The serialization process ###

The serialization process uses the same mechanism as the mapping process, but the repository contains serialization helpers in stead of mapping helpers. There are also two different flavors of root serializers available:

*  ObjectHelper Serializes an instance as a JavaBean. This is the default for compatibility reasons. You can explicitly activate it by calling ((JSONSerializer) marshal).useJavaBeanAccess().
*  ObjectHelperDirect Serializes an instance as a POJO. You an activate this by calling the method ((JSONSerializer) marshal).usePojoAccess().


You can customize the serializer for your own business model in two ways.

*  @JSONSerialize, @JSONConstruct in combination with the ObjectHelperDirect.
*  Deriving your own helper class from SerializeHelper and adding it with the method call  ((JSONSerializer) marshal).addHelper(myHelper).

Here is an example of an annotated class.

	public class MyDate
	{
	    // These private fields will be serialized in addition to the
	    // constructor values.
	    private Date theDate;
	    private String theTimeZone;
	
	    // This method will be called during serialization to obtain the
	    // values that can later be used to call the constructor.
	    @JSONSerialize
	    public Object[] getTime()
	    {
	        return new Object[]{theDate.getTime(), theTimeZone};
	    }
	    
	    // This constructor will be called with the values that were provided
	    // by the other annotated method.
	    @JSONConstruct
	    public MyDate(long aTime, String aTimeZone)
	    {
	        theDate = new Date(aTime);
	        theTimeZone = aTimeZone;
	    }
	}

The result of the serialization looks like the following listing. As you can see, there are two extra artificial fields cons-0 and cons-1 which are generated automatically by the serializer, these properties contain the values which were provided by the method which was annotated with @JSONSerialize. These same properties will be used for calling the @JSONConstruct annotated constructor.

	{ ">" : "O",
	  "&" : "id0",
	  "c" : "MyDate",
	  "=" : {
	     "cons-0" : {    
	        ">" : "O",
	        "&" : "id1",
	        "c" : "java.lang.Long",
	        "=" : "1212717107857" },
	     "cons-1" : {          
	        ">" : "O",
	        "&" : "id2",
	        "c" : "java.lang.String",
	        "=" : "CEST" },
	     "theDate" : {
	        ">" : "O",
	        "&" : "id3",
	        "c" : "java.util.Date",
	        "=" : "2008-06-06 03:51:47,857 CEST" },
	     "theTimeZone" : {
	        ">" : "R",
	        "*" : "id2" } } }

## 2.5. Validation ##

This tool enables you to validate your JSON files. You can specify which content you expect, the validator can check these constraints for you. The system is straightforward to use and extend. You can add your own rules if you have specific needs. The validation definition is in JSON - as you would expect. Built-in rules:

	{ "name" : "Some rule name",
	  "type" : "<built-in-type>" }

A validation document consists of a validation rule. This rule will be applied to the JSONValue that has to be validated. The validation rules can be nested, so it is possible to create complex rules out of simpler ones. The "type" attribute is obligatory.  The  "name" is optional, it will be used during error reporting and for re-use.  The predefined rules are listed below. The name can come in handy while debugging. The name of the failing validation will be available in the exception. If you give each rule its own name or number, you can quickly find out on which predicate the validation fails. Here is an example of how you can create a validator.

	// First we create a parser to read the validator specification which is 
	// defined using the (what did you think) JSON format.
	// The validator definition is located in the "my-validator.json" resource in the
	// class path.
	JSONParser lParser = 
	   new JSONParser(
	      MyClass.class.getResourceAsStream("my-validator.json"));
	
	// We parse the validator spec and convert it into a Java representation.
	JSONObject lValidatorObject = (JSONObject) lParser.nextValue();
	
	// Finally we can convert our validator using the Java model.
	Validator lValidator = new JSONValidator(lValidatorObject);
	
	And now that you have the validator, you can start validating your data.
	
	// First we create a parser to read the data. 
	JSONParser lParser = new JSONParser(MyClass.class.getResourceAsStream("data.json"));
	
	// We parse the data file and convert it into a Java representation.
	JSONValue lMyData = lParser.nextValue();
	
	// Now we can use the validator to check on our data. We can test if the data has the 
	// correct format or not. 
	lValidator.validate(lMyData);

### Basic Rules ###

These rules are the basic rules in boolean logic. 

#### "type":"true"
 
This rule always succeeds.

A validator that will succeed on all JSON data structures.	

	{ "name" :"This validator validates *everything*",
	  "type" :"true" }

#### "type":"false"

This rule always fails.

A validator that rejects all data structures.

	{ "name" :"This validator rejects all",
	  "type" :"false" }

#### "type":"and"

All nested rules have to hold for the and rule to succeed.


*  **rules** Array of nested rules.

A validator that succeeds if the object under scrutiny is both a list and has content consisting of integers.

	{ "name" :"List of integers",
	  "type" :"and",
	  "rules" : [ {"type":"array"}, {"type":"content","rule":{"type":"int"} } ] }

#### "type":"or"

One of the nested rules has to succeed for this rule to succeed.


*  **rules** Array of nested rules.

A validator that validates booleans or integers.

	{ "name" :"Null or int",
	  "type" :"or",
	  "rules" : [ {"type":"int"}, {"type":"bool"} ] }

#### "type":"not"

The rule succeeds if the nested rule fails and vice versa.


*  **rule** A single nested rule. 

### Type Rules  ###

These rules are predefined rules which allows you to specify type restrictions on the JSON data elements. The meaning of these predicates is obvious, they will not be discussed. See the examples for more information. Following type clauses can be used: 

*   "type":"complex"
*   "type":"array"
*   "type":"object"
*   "type":"simple"
*   "type":"null"
*   "type":"bool"
*   "type":"string"
*   "type":"number"
*   "type":"int"
*   "type":"decimal"

###  Attribute Rules  ###

These rules check for attributes of certain types.

#### "type":"length"

Applicable to complex objects and string objects. The rule will fail if the object under investigation has another type. For array objects the number of elements is counted, for objects the number of properties and for strings, the length of its value in Java (not the JSON representation; "$\backslash$n" in the file counts as a single character).

*  **min** (optional) The minimal length of the array.
*  **max** (optional) The maximal length of the array.

A validator that only wants arrays of length 5.

	{ "name"  :"Array of length 5",
	  "type"  :"and",
	  "rules" : [{"type":"array"}, {"type":"length","min":5,"max":5}] }

#### "type":"range"

Applicable to JSONNumbers, i.e. JSONInteger and JSONDecimal.}

*  **min** (optional) The minimal value.
*  **max** (optional) The maximal value.

Allow numbers between 50 and 100.

	{ "name" :"Range validator",
	  "type" :"range",
	  "min" : 50,
	  "max" : 100 }


#### "type":"enum"

The value has to occur in the provided list. The list can contain simple types as well as complex nested types.

*  **values** An array of JSON values. 

An enum validator.

	{ "name" :"Enum validator",
	  "type" :"enum",
	  "values" : [13, 17, "JSON", 123.12, [1, 2, 3], {"key":"value"}] }


#### "type":"regexp"

For strings, requires a predefined format according to the regular expression.}


*  **pattern** A regular expression pattern.

A validator that validates strings containing a sequence of a's , b's and c's.

	{ "name" :"A-B-C validator",
	  "type" :"regexp",
	  "pattern" : "a*b*c*" }

#### "type":"content"

Note that in contrast with the "properties" rule (for objects), you can specify in a single rule what all property values of an object should look like.

*  **rule** The rule that specifies how the content of a complex structure -  an array or the property values of an object -  should behave.

#### "type":"properties"

This predicate is only applicable (and only has meaning) on object data structures. It will fail on any other type.

*  **pairs** A list of ``key/value'' pair descriptions. Note that in contrast with the content rule above you can specify a rule per attribute. Each description contains three properties:  
*  **key** The key string. 
*  **optional** A boolean indicating whether this property is optional or not. 
*  **rule** A validation rule that should be applied to the properties value. 

It will validate objects looking like this:
 
	Example data structure that will be validated:
	{{"name":"Bruno Ranschaert", "country":"Belgium", "salary":13.0 }}
	
	The validator looks like this:
	{ "name" :"Contact spec.",
	  "type" :"properties",
	  "pairs" : [{"key":"name", "optional":false, "rule":{"type":"string"}},
	              {"key":"country", "optional":false, "rule":{"type":"string"}},
	              {"key":"salary", "optional":true, "rule":{"type":"decimal" } } ] }

### Structural Rules  ###
#### "type":"ref"

This rule lets you specify recursive rules. Be careful not to create infinite validations which is quite possible using this rule. The containing rule will be fetched just before validation, there will be no error message during construction when the containing rule is not found. The rule will fail in this case. If there are several rules with the same name, only the last one with that name is remembered and the last one will be used.

* "*" The name of the rule to invoke.

A validator that validates nested lists of integers. A ref is needed to enable recursion in the validator.

	{ "name" :"Nested list of integers",
	  "type" :"and",
	  "rules" : [ 
	     {"type":"array"},
	     {"type":"content",
	      "rule": {
	         "type" : "or",
	         "rules": [
	            {"type":"int"}, 
	            {"type":"ref", "*" : "Nested list of integers" } ] } } ] }

#### "type":"let"

Lets you specify a number of named rules in advance. It is a convenience rule that lets you specify a list of global shared validation rules in advance before using these later on. It becomes possible to first define a number of recurring types and then give the starting point. It is a utility rule that lets you tackle more complex validations. Note that it  makes no sense to define anonymous rules inside the list, it is impossible to refer to these later on.

* **rules** A list of rules.
* "*" : The name of the rule that should be used.

Example

	{ "name" :"Let test -  a's or b's",
	  "type" :"let",
	  "*"    : "start",
	  "rules" : 
	     [{"name":"start", "type":"or", "rules":[{"type":"ref", "*":"a"}, 
	                                             {"type":"ref", "*":"b"}]},
	      {"name":"a", "type":"regexp", "pattern":"a*"},
	      {"name":"b", "type":"regexp", "pattern":"b*" } ] }


The validator class looks like this:

	public class MyValidator
	extends CustomValidator
	{
	    public MyValidator(
	       String aName, JSONObject aRule,
	       HashMap<String, Validator> aRuleset)
	    {
	        super(aName, aRule, aRuleset);
	    }
	
	    public void validate(JSONValue aValue) 
	    throws ValidationException
	    {
	        // Do whatever you need to do on aValue ...
	        // If validation is ok, simply return.
	        // If validation fails, you can use:
	        // fail(JSONValue aValue) or 
	        //    fail(String aReason, JSONValue aValue)
	        // to throw the Validation exception for you.
	    }
	}

#### "type":"custom"

An instance of this validator will be created and will be given a hash map of validations.  A custom validator should be derived from CustomValidator.


*  **class** The fully qualified class name of the validator.}

Example

	{ "name" :"Custom test",
	  "type" :"custom",
	  "class" : "com.sdicons.json.validator.MyValidator" }

#### "type":"switch"

The switch validator is a convenience one. It is a subset of the or validator, but the problem with the or validator is that it does a bad job for error reporting when things go wrong. The reason is that all rules fail and it is not always clear why, because the reason a rule fails might be some levels deeper. The switch validator selects a validator based on the value of  a property encountered in the value being validated. The error produced will be the one of the selected validator.  The first applicable validator is used, the following ones are ignored.
Example: The top level rule in the validator for validators contains a switch that could have been described by an or, but the switch gives better error messages.


*  **key** The key name of the object that will act as the discriminator. 
*  **case**  A list of objects containing the parameters "values" and "rule". The first one is a list of values the second one a validator rule.

#  3. Validator for Validators 

 This example validator is able to validate validators. The example is a bit contrived because the validators really don't need validation because it is built-in in the construction. It is interesting because it can serve as a definition of how to construct a validator.
 
	{
	   "name":"Validator validator",
	   "type":"let",
	   "*":"rule",
	   "rules":
	   [
	      ########## START ##########
	      {
	         "name":"rule",
	         "type":"switch",
	         "key":"type",
	         "case":
	         [
	            {"values":["true", "false", "null"], "rule":{"type":"ref","*":"atom-rule"}},
	            {"values":["int", "complex", "array", "object", "simple",
	                       "null", "bool", "string", "number", "decimal"],
	                       "rule":{"type":"ref","*":"type-rule"}},
	            {"values":["not", "content"], "rule":{"type":"ref","*":"rules-rule"}},
	            {"values":["and", "or"], "rule":{"type":"ref","*":"ruleset-rule"}},
	            {"values":["length", "range"], "rule":{"type":"ref","*":"minmax-rule"}},
	            {"values":["ref"], "rule":{"type":"ref","*":"ref-rule"}},
	            {"values":["custom"], "rule":{"type":"ref","*":"custom-rule"}},
	            {"values":["enum"], "rule":{"type":"ref","*":"enum-rule"}},
	            {"values":["let"], "rule":{"type":"ref","*":"let-rule"}},
	            {"values":["regexp"], "rule":{"type":"ref","*":"regexp-rule"}},
	            {"values":["properties"], "rule":{"type":"ref","*":"properties-rule"}},
	            {"values":["switch"], "rule": {"type":"ref","*":"switch-rule"}}
	         ]
	      },
	      ########## RULESET ##########
	      {
	         "name":"ruleset",
	         "type":"and",
	         "rules":[{"type":"array"},{"type":"content","rule":{"type":"ref","*":"rule"}}]
	      },
	      ########## PAIRS ##########
	      {
	         "name":"pairs",
	         "type":"and",
	         "rules":[{"type":"array"},{"type":"content","rule":{"type":"ref","*":"pair"}}]
	      },
	      ########## PAIR ##########
	      {
	         "name":"pair",
	         "type":"properties",
	         "pairs" :
	          [{"key":"key",      "optional":false, "rule":{"type":"string"}},
	           {"key":"optional", "optional":false, "rule":{"type":"bool"}},
	           {"key":"rule",     "optional":false, "rule":{"type":"ref","*":"rule"}}
	          ]
	      },
	      ########## CASES ##########
	      {
	         "name":"cases",
	         "type":"and",
	         "rules":[{"type":"array"},{"type":"content","rule":{"type":"ref","*":"case"}}]
	      },
	      ########## CASE ##########
	      {
	         "name":"case",
	         "type":"properties",
	         "pairs" :
	          [{"key":"values",   "optional":false, "rule":{"type":"array"}},
	           {"key":"rule",     "optional":false, "rule":{"type":"ref","*":"rule"}}
	          ]
	      },
	      ########## ATOM ##########
	      {
	         "name":"atom-rule",
	         "type":"properties",
	         "pairs" :
	          [{"key":"name", "optional":true, "rule":{"type":"string"}},
	           {"key":"type", "optional":false, "rule":
	                          {"type":"enum","values":["true", "false", "null"]}}
	          ]
	      },
	      ########## RULESET-RULE ##########
	      {
	         "name":"ruleset-rule",
	         "type":"properties",
	         "pairs" :
	          [{"key":"name",  "optional":true,  "rule":{"type":"string"}},
	           {"key":"type",  "optional":false, "rule":{"type":"enum","values":["and", "or"]}},
	           {"key":"rules", "optional":false, "rule":{"type":"ref","*":"ruleset"}}
	          ]
	      },
	      ########## RULES-RULE ##########
	      {
	         "name":"rules-rule",
	         "type":"properties",
	         "pairs" :
	          [{"key":"name", "optional":true,  "rule":{"type":"string"}},
	           {"key":"type", "optional":false, "rule":{"type":"enum","values":["not", "content"]}},
	           {"key":"rule", "optional":false, "rule":{"type":"ref","*":"rule"}}
	          ]
	      },
	      ########## TYPE ##########
	      {
	         "name":"type-rule",
	         "type":"properties",
	         "pairs" :
	          [{"key":"name", "optional":true, "rule":{"type":"string"}},
	           {"key":"type", "optional":false, "rule":{"type":"enum",
	                    "values":["int", "complex", "array", "object", 
	                    "simple", "null", "bool", "string", "number", 
	                    "decimal"]}}
	          ]
	      },
	      ########## MINMAX ##########
	      {
	         "name":"minmax-rule",
	         "type":"properties",
	         "pairs" :
	          [{"key":"name", "optional":true, "rule":{"type":"string"}},
	           {"key":"type", "optional":false, "rule":{"type":"enum","values":["length", "range"]}},
	           {"key":"min", "optional":true, "rule":{"type":"number"}},
	           {"key":"max", "optional":true, "rule":{"type":"number"}}
	          ]
	      },
	      ########## REF ##########
	      {
	         "name":"ref-rule",
	         "type":"properties",
	         "pairs" :
	          [{"key":"name", "optional":true, "rule":{"type":"string"}},
	           {"key":"type", "optional":false, "rule":{"type":"enum","values":["ref"]}},
	           {"key":"*",    "optional":false, "rule":{"type":"string"}}
	          ]
	      },
	      ########## CUSTOM ##########
	      {
	         "name":"custom-rule",
	         "type":"properties",
	         "pairs" :
	          [{"key":"name", "optional":true, "rule":{"type":"string"}},
	           {"key":"type", "optional":false, "rule":{"type":"enum","values":["custom"]}},
	           {"key":"class", "optional":true, "rule":{"type":"string"}}
	          ]
	      },
	      ########## ENUM ##########
	      {
	         "name":"enum-rule",
	         "type":"properties",
	         "pairs" :
	          [{"key":"name", "optional":true, "rule":{"type":"string"}},
	           {"key":"type", "optional":false, "rule":{"type":"enum","values":["enum"]}},
	           {"key":"values", "optional":true, "rule":{"type":"array"}}
	          ]
	      },
	      ########## LET ##########
	      {
	         "name":"let-rule",
	         "type":"properties",
	         "pairs" :
	          [{"key":"name",  "optional":true,  "rule":{"type":"string"}},
	           {"key":"type",  "optional":false, "rule":{"type":"enum","values":["let"]}},
	           {"key":"rules", "optional":false, "rule":{"type":"ref","*":"ruleset"}},
	           {"key":"*",     "optional":false, "rule":{"type":"string"}}
	          ]
	      },
	      ########## REGEXP ##########
	      {
	         "name":"regexp-rule",
	         "type":"properties",
	         "pairs" :
	          [{"key":"name", "optional":true, "rule":{"type":"string"}},
	           {"key":"type", "optional":false, "rule":{"type":"enum","values":["regexp"]}},
	           {"key":"pattern", "optional":false, "rule":{"type":"string"}}
	          ]
	      },
	      ########## PROPERTIES ##########
	      {
	         "name":"properties-rule",
	         "type":"properties",
	         "pairs" :
	          [{"key":"name",  "optional":true, "rule":{"type":"string"}},
	           {"key":"type",  "optional":false, "rule":{"type":"enum","values":["properties"]}},
	           {"key":"pairs", "optional":false, "rule":{"type":"ref","*":"pairs"}}
	          ]
	      },
	      ########## SWITCH ##########
	      {
	         "name":"switch-rule",
	         "type":"properties",
	         "pairs" :
	          [{"key":"name",  "optional":true, "rule":{"type":"string"}},
	           {"key":"type",  "optional":false, "rule":{"type":"enum","values":["switch"]}},
	           {"key":"key",   "optional":false, "rule":{"type":"string"}},
	           {"key":"case",  "optional":false, "rule":{"type":"ref","*":"cases"}}
	          ]
	      }
	   ]
	}

