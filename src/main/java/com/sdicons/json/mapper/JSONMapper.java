/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.mapper;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.sdicons.json.mapper.helper.ClassMapper;
import com.sdicons.json.mapper.helper.ComplexClassMapper;
import com.sdicons.json.mapper.helper.impl.ArrayMapper;
import com.sdicons.json.mapper.helper.impl.BigDecimalMapper;
import com.sdicons.json.mapper.helper.impl.BigIntegerMapper;
import com.sdicons.json.mapper.helper.impl.BooleanMapper;
import com.sdicons.json.mapper.helper.impl.ByteMapper;
import com.sdicons.json.mapper.helper.impl.CharacterMapper;
import com.sdicons.json.mapper.helper.impl.CollectionMapper;
import com.sdicons.json.mapper.helper.impl.DateMapper;
import com.sdicons.json.mapper.helper.impl.DoubleMapper;
import com.sdicons.json.mapper.helper.impl.EnumMapper;
import com.sdicons.json.mapper.helper.impl.FloatMapper;
import com.sdicons.json.mapper.helper.impl.IntegerMapper;
import com.sdicons.json.mapper.helper.impl.LongMapper;
import com.sdicons.json.mapper.helper.impl.MapMapper;
import com.sdicons.json.mapper.helper.impl.ObjectMapperMeta;
import com.sdicons.json.mapper.helper.impl.ShortMapper;
import com.sdicons.json.mapper.helper.impl.StringMapper;
import com.sdicons.json.model.JSONNull;
import com.sdicons.json.model.JSONValue;
import com.sdicons.json.repository.ClassHelperRepository;
import com.sdicons.json.serializer.JSONSerializer;

/**
 * The mapper class is able to convert a JSON representation to/from a Java
 * representation. The mapper's goal is to produce a nice and clean JSON output
 * which can easily be used in e.g. Javascript context. As a consequence, not
 * all Java constructs are preserved during the conversion to/from JSON. The
 * mapper is the best choice in an application where the clean JSON format is
 * central. If the emphasis is on exact Java serialization where types are
 * preserved, take a look at the serializer tool.
 *
 * The main difference between the serializer and the mapper is that the
 * serializer keeps as much type information and structure information in the
 * JSON data where the mapper uses the type information in the provided Java
 * classes to interpret the JSON data.
 *
 * <br>Example: The Java model
 *
 * <pre>
 * <code>
 * public class Person {
 *   private String name;
 *   private String phoneNumber;
 *   private int age;
 *   // Getters and setters omitted.
 *   // ...
 * }
 * </code>
 * </pre>
 *
 * The mapping code
 *
 * <pre>
 * <code>
 * // Create a person.
 * Person p = new Person();
 * p.setName(&quot;Mr. Jason Tools&quot;);
 * p.setPhoneNumber(&quot;0123456789&quot;);
 * p.setAge(40);
 * // Map and print.
 * JSONMapper mapper = new JSONMapper();
 * JSONValue json = mapper.toJSON(p);
 * System.out.println(json.render(true));
 * </code>
 * </pre>
 *
 * The resulting JSON text
 *
 * <pre>
 * <code>
 * {
 *    "age" : 40,
 *    "name" : "Mr. Jason Tools",
 *    "phoneNumber" : "0123456789"
 * }
 * </code>
 * </pre>
 *
 * @see JSONSerializer
 */
public class JSONMapper
{
    private static final String MAPPER001 = "JSONMapper/001: JSON->Java. Mapper does not support null values.";
    private static final String MAPPER002 = "JSONMapper/002: JSON->Java. Could not find a mapper helper for parameterized type '%s'.";
    private static final String MAPPER003 = "JSONMapper/003: JSON<->Java. Could not find a mapper helper for class '%s'.";

    /**
     * The mapping option OBJMAPPING indicates whether property mapping of field mapping will be used.
     * There are two possible values {@link JSONMapper#OBJMAPPING_FIELD} or {@link JSONMapper#OBJMAPPING_PROPERTY}.
     * @see JSONMapper#setMappingOption(String, Object)
     */
    public static final String OPT_OBJMAPPING = "com.sdicons.json.mapper.helper.impl.ObjectMapperMeta";
    /**
     * The object mapper will use field access to access the contents of an instance.
     * @see JSONMapper#OPT_OBJMAPPING
     */
    public static final String OBJMAPPING_FIELD = "field";
    /**
     * The object mapper will use JavaBean getters and setters to access the contents of an instance.
     * @see JSONMapper#OPT_OBJMAPPING
     */
    public static final String OBJMAPPING_PROPERTY = "property";
    /**
     * The mapping option DATAFORMAT indicates which date pattern will be used by the DateMapper to convert String
     * values to Dates and vice versa.
     * @see JSONMapper#DATEFORMAT_DEFAULT
     */
    public static final String OPT_DATEFORMAT = "com.sdicons.json.mapper.helper.impl.DateMapper";
    /**
     * The default pattern used by the DateMapper to convert String representations into Java dates.
     * You can modify the pattern using the OPT_DATEFORMAT mapper option.
     * @see JSONMapper#OPT_DATEFORMAT
     * @see JSONMapper#setMappingOption(String, Object)
     */
    public static final String DATEFORMAT_DEFAULT = "yyyy-MM-dd HH:mm:ss";

    private ClassHelperRepository<ClassMapper> repo = new ClassHelperRepository<ClassMapper>();
    private Map<String, Object> options = new HashMap<String, Object>();

    public JSONMapper(ClassMapper ... helpers) {
        for(ClassMapper helper:helpers) {
            repo.addHelper(helper);
        }
    }

    public JSONMapper()
    {
        repo.addHelper(new ObjectMapperMeta());
        repo.addHelper(new StringMapper());
        repo.addHelper(new BooleanMapper());
        repo.addHelper(new ByteMapper());
        repo.addHelper(new ShortMapper());
        repo.addHelper(new IntegerMapper());
        repo.addHelper(new LongMapper());
        repo.addHelper(new FloatMapper());
        repo.addHelper(new DoubleMapper());
        repo.addHelper(new BigIntegerMapper());
        repo.addHelper(new BigDecimalMapper());
        repo.addHelper(new CharacterMapper());
        repo.addHelper(new DateMapper());
        repo.addHelper(new CollectionMapper());
        repo.addHelper(new MapMapper());
        repo.addHelper(new EnumMapper());
    }

    /**
     * Map a JSON representation to a Java object.
     * @param aValue The JSON value that has to be mapped.
     * @param aPojoClass The class to which the JSON object should be mapped.
     * @return  The resulting Java object, the POJO representation.
     * @throws MapperException when an error occurs during mapping.
     */
    public Object toJava(JSONValue aValue, Class<?> aPojoClass)
    throws MapperException
    {
        // Null references are not allowed.
        if(aValue == null)
        {
            throw new MapperException(MAPPER001);
        }
        // But null representations are.
        else if(aValue.isNull()) return null;
        if(aPojoClass.isArray()){
        	ArrayMapper arrayMapper=new ArrayMapper();
        	return arrayMapper.toJava(this, aValue, aPojoClass);
        }
        // Use the class helpers for built in types.
        if(aPojoClass == Boolean.TYPE) aPojoClass = Boolean.class;
        else if(aPojoClass == Byte.TYPE) aPojoClass = Byte.class;
        else if(aPojoClass == Short.TYPE) aPojoClass = Short.class;
        else if(aPojoClass == Integer.TYPE) aPojoClass = Integer.class;
        else if(aPojoClass == Long.TYPE) aPojoClass = Long.class;
        else if(aPojoClass == Float.TYPE) aPojoClass = Float.class;
        else if(aPojoClass == Double.TYPE) aPojoClass = Double.class;
        else if(aPojoClass == Character.TYPE) aPojoClass = Character.class;

        // Find someone who can map it.
        final ClassMapper lHelperSimple = repo.findHelper(aPojoClass);

        if(lHelperSimple == null)
        {
            throw new MapperException(String.format(MAPPER003, aPojoClass.getName()));
        }
        else return lHelperSimple.toJava(this, aValue, aPojoClass);
    }

    /**
     * Map a JSON representation to a Java object.
     * @param json The JSON value that has to be mapped.
     * @param genericType A type indication to help the mapper map the JSON text.
     * @return The resulting Java POJO.
     * @throws MapperException When the JSON text cannot be mapped to POJO.
     */
    public Object toJava(JSONValue json, ParameterizedType genericType)
    throws MapperException
    {
        // Null references are not allowed.
        if(json == null)
        {
            throw new MapperException(MAPPER001);
        }
        // But null representations are.
        else if(json.isNull()) return null;

        // First decompose the type in its raw class and the classes of the parameters.
        final Class<?> lRawClass = (Class<?>) genericType.getRawType();
        final Type[] lTypes = genericType.getActualTypeArguments();

        // Find someone who can map it.
        final ClassMapper lMapperHelper = repo.findHelper(lRawClass);

        if(lMapperHelper == null)
        {
            throw new MapperException(String.format(MAPPER002, genericType.toString()));
        }
        else
        {
            if(lMapperHelper instanceof ComplexClassMapper) return ((ComplexClassMapper) lMapperHelper).toJava(this, json, lRawClass, lTypes);
            else return lMapperHelper.toJava(this, json, lRawClass);
        }
    }

    /**
     * Map a JSON representation to a Java object. Since no class nor type hint is passed to the
     * mapper, this method can only handle the most basic mappings.
     * @param json The JSON value that has to be mapped.
     * @return he resulting Java POJO.
     * @throws MapperException When the JSON text cannot be mapped to POJO.
     */
    public Object toJava(JSONValue json)
    throws MapperException
    {
        if(json.isArray()) return toJava(json, LinkedList.class);
        else if(json.isBoolean()) return toJava(json, Boolean.class);
        else if(json.isDecimal()) return toJava(json, BigDecimal.class);
        else if(json.isInteger()) return toJava(json, BigInteger.class);
        else if(json.isString()) return toJava(json, String.class);
        else return toJava(json, Object.class);
    }

    /**
     * Map a POJO to the JSON representation.
     * @param pojo to be mapped to JSON.
     * @return The JSON representation.
     * @throws MapperException If something goes wrong during mapping.
     */
    public JSONValue toJSON(Object pojo)
    throws MapperException
    {
        if(pojo == null) return JSONNull.NULL;
        final Class<?> lObjectClass =  pojo.getClass();
        if(lObjectClass.isArray()){
            final ArrayMapper arrayMapper = new ArrayMapper();
        	return arrayMapper.toJSON(this, pojo);
        }

        final ClassMapper lHelperSimple = repo.findHelper(pojo.getClass());

        if(lHelperSimple == null)
        {
            throw new MapperException(String.format(MAPPER003, pojo.getClass().getName()));
        }
        return lHelperSimple.toJSON(this, pojo);
    }

    /**
     * Add custom helper class.
     *
     * @param helper the custom helper you want to add to the mapper.
     */
    public void addHelper(ClassMapper helper)
    {
        repo.addHelper(helper);
    }

    /**
     * The objects that fall back on the general object mapper will be mapped by
     * using their fields directly. Without further annotations, the default
     * constructor without arguments will be used in the POJO. If this is not sufficient,
     * the @JSONConstructor and @JSONConstructorArgs annotations can be used as well in the mapped POJO to
     * indicate which constructor has to be used.
     */
    public void usePojoAccess()
    {
        setMappingOption(OPT_OBJMAPPING, JSONMapper.OBJMAPPING_FIELD);
    }

    /**
     * The objects that fall back on the general object mapper will be mapped by
     * using their JavaBean properties. The mapped JavaBean always needs a
     * default constructor without arguments.
     */
    public void useJavaBeanAccess()
    {
        setMappingOption(OPT_OBJMAPPING, JSONMapper.OBJMAPPING_PROPERTY);
    }

    /**
     * Add a mapping option. The mapping options are passed to all the class mappers, this is the
     * way to configure the class mappers. It can influence the mapping behavior.
     *
     * @param key The name of the option.
     * @param value The value of the option.
     *
     * @see JSONMapper#OPT_DATEFORMAT
     * @see JSONMapper#OPT_OBJMAPPING
     */
    public void setMappingOption(String key, Object value) {
        options.put(key,  value);
    }

    /**
     * Get the value of a mapping option.
     * If you write your own parameterized mapper you can use this method to fetch the parameters.
     *
     * @param key The option name.
     * @param defaultValue The default value that will be returned if the option is not set.
     * @return The value of the option or the default value if the option was not set explicitly.
     *
     *  @see JSONMapper#OPT_DATEFORMAT
     *  @see JSONMapper#OPT_OBJMAPPING
     */
    public Object getMappingOption(String key, Object defaultValue) {
        if(options.containsKey(key)) return options.get(key);
        else return defaultValue;
    }

    /**
     * Check to see if an option was set explicitly or not.
     * If you write your own parameterized mapper, it can check its options using this method.
     *
     * @param key The option name.
     * @return A boolean indicating whether the option was explicitly set or not.
     */
    public boolean hasMappingOption(String key) {
        return options.containsKey(key);
    }
}
