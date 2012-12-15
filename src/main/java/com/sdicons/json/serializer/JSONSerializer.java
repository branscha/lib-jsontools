/*******************************************************************************
 * Copyright (c) 2006-2013 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.serializer;

import java.util.HashMap;
import java.util.Map;

import com.sdicons.json.mapper.JSONMapper;
import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.repository.ClassHelperRepository;
import com.sdicons.json.serializer.helper.ArraySerializer;
import com.sdicons.json.serializer.helper.BigDecimalSerializer;
import com.sdicons.json.serializer.helper.BigIntegerSerializer;
import com.sdicons.json.serializer.helper.BooleanSerializer;
import com.sdicons.json.serializer.helper.ByteSerializer;
import com.sdicons.json.serializer.helper.CharacterSerializer;
import com.sdicons.json.serializer.helper.ClassSerializer;
import com.sdicons.json.serializer.helper.CollectionSerializer;
import com.sdicons.json.serializer.helper.ColorSerializer;
import com.sdicons.json.serializer.helper.DateSerializer;
import com.sdicons.json.serializer.helper.DoubleSerializer;
import com.sdicons.json.serializer.helper.EnumSerializer;
import com.sdicons.json.serializer.helper.FloatSerializer;
import com.sdicons.json.serializer.helper.FontSerializer;
import com.sdicons.json.serializer.helper.IntegerSerializer;
import com.sdicons.json.serializer.helper.LongSerializer;
import com.sdicons.json.serializer.helper.MapSerializer;
import com.sdicons.json.serializer.helper.ObjectSerializerMeta;
import com.sdicons.json.serializer.helper.ShortSerializer;
import com.sdicons.json.serializer.helper.StringSerializer;

/**
 * Convert a JSON representation to/from a
 * Java representation. The serializer's goal is to preserve as much Java structure as
 * possible: primitive types, arrays, object clusters containing recursive references and so on.
 * As a consequence, the generated JSON is rather terse. The serializer is the best choice in a
 * Java centric application where you want to write some object structures to JSON in a readable form.
 * If the emphasis is on clean JSON, take a look at the Mapper tool, which does not support all Java constructs
 * but which emits nice JSON which can easily be parsed in e.g. Javascript.
 *
 * * The main difference between the serializer and the mapper is that the serializer keeps as much
 * type information and structure information in the JSON data where the mapper uses the type information
 * in the provided Java classes to interpret the JSON data.
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
 * JSONSerializer serializer = new JSONSerializer();
 * JSONValue json = serializer.marshal(p);
 * System.out.println(json.render(true));
 * </code>
 * </pre>
 *
 * The resulting JSON text
 *
 * <pre>
 * <code>
 * {
 *    ">" : "O",
 *    "&" : "id0",
 *    "c" : "com.sdicons.json.examples.Person",
 *    "=" :
 *       {
 *          "age" :
 *             {
 *                ">" : "O",
 *                "&" : "id1",
 *                "c" : "java.lang.Integer",
 *                "=" : "40"
 *             },
 *          "name" :
 *             {
 *                ">" : "O",
 *                "&" : "id2",
 *                "c" : "java.lang.String",
 *                "=" : "Mr. Jason Tools"
 *             },
 *          "phoneNumber" :
 *             {
 *                ">" : "O",
 *                "&" : "id3",
 *                "c" : "java.lang.String",
 *                "=" : "0123456789"
 *             }
 *       }
 * }
 * </code>
 * </pre>
 *
 *  @see JSONMapper
 *
 */
public class JSONSerializer
{

    // Error messages.
    //
    private static final String SER001 = "JSONSerializer/001: Unknown primitive encountered while unmarshaling class '%s' from line %d, column %d.";
    private static final String SER002 = "JSONSerializer/002: Unknown reference '%s'.";
    private static final String SER003 = "JSONSerializer/003: Attribute value '%s' is missing.";
    private static final String SER004 = "JSONSerializer/004: Tried to unmarshal an unknown class '%s'.";
    private static final String SER005 = "JSONSerializer/005: Unknown type '%s' encountered.";
    private static final String SER006 = "JSONSerializer/006: Unknown primitive type '%s' encountered.";
    private static final String SER007 = "JSONSerializer/007: Error while unmarshalling primitive type '%s' with value '%s'.";
    private static final String SER008 = "JSONSerializer/008: Attribute '%s' value is missing for object at line %d, col %d.";
    private static final String SER009 = "JSONSerializer/009: Attribute '%s' is not a string value for object at line %d, col %d.";

    // Options.
    // The OBJMAPPING option indicates whether property mapping of field mapping will be used.
    public static final String OPT_OBJSERIALIZE = "com.sdicons.json.serializer.helper.impl.ObjectHelperMeta";
    public static final String OBJSERIALIZE_FIELD = "field";
    public static final String OBJSERIALIZE_PROPERTY = "property";
    // The Date format the JSON text.
    public static final String OPT_DATESERIALIZE = "com.sdicons.json.serializer.helper.impl.DateHelper";
    public static final String DATESERIALIZE_DEFAULT = "yyyy-MM-dd HH:mm:ss,SSS z";

    private static final String IDPREFIX = "id";
    private long idCounter = 0;

    public static final String RNDR_NULL = "null";
    public static final String RNDR_OBJ = "O";
    public static final String RNDR_OBJREF = "R";
    public static final String RNDR_PRIM = "P";
    public static final String RNDR_ARR = "A";

    public static final String RNDR_ATTR_ID = "&";
    public static final String RNDR_ATTR_KIND = ">";
    public static final String RNDR_ATTR_TYPE = "t";
    public static final String RNDR_ATTR_VALUE = "=";
    public static final String RNDR_ATTR_CLASS = "c";
    public static final String RNDR_ATTR_REF = "*";

    public static final String RNDR_PRTITYP_BOOLEAN = "boolean";
    public static final String RNDR_PRTITYP_BYTE = "byte";
    public static final String RNDR_PRTITYP_CHAR = "char";
    public static final String RNDR_PRTITYP_SHORT = "short";
    public static final String RNDR_PRTITYP_INT = "int";
    public static final String RNDR_PRTITYP_LONG = "long";
    public static final String RNDR_PRTITYP_FLOAT = "float";
    public static final String RNDR_PRTITYP_DOUBLE = "double";

    public static final String ERR_MISSINGATTR = "Attribute is missing: ";
    public static final String ERR_MISSINGATTRVAL = "Attribute value is missing: ";
    public static final String ERR_MISSINGSTRING = "Attribute is not a string value: ";

    private ClassHelperRepository<ClassSerializer> repo = new ClassHelperRepository<ClassSerializer>();
    private Map<String, Object> options = new HashMap<String, Object>();


    public JSONSerializer()
    {
        repo.addHelper(new ObjectSerializerMeta());
        repo.addHelper(new StringSerializer());
        repo.addHelper(new BooleanSerializer());
        repo.addHelper(new ByteSerializer());
        repo.addHelper(new ShortSerializer());
        repo.addHelper(new IntegerSerializer());
        repo.addHelper(new LongSerializer());
        repo.addHelper(new FloatSerializer());
        repo.addHelper(new DoubleSerializer());
        repo.addHelper(new BigIntegerSerializer());
        repo.addHelper(new BigDecimalSerializer());
        repo.addHelper(new CharacterSerializer());
        repo.addHelper(new DateSerializer());
        repo.addHelper(new CollectionSerializer());
        repo.addHelper(new MapSerializer());
        repo.addHelper(new ColorSerializer());
        repo.addHelper(new FontSerializer());
        repo.addHelper(new EnumSerializer());
    }

    public JSONSerializer(ClassSerializer ... serializers){
        for(ClassSerializer serializer : serializers) {
            repo.addHelper(serializer);
        }
    }

    /**
     * Convert a boolean primitive to JSON.
     * @param aValue
     * @return An JSON representation of the boolean primitive.
     */
    public JSONObject marshal(boolean aValue)
    {
        return marshalPrimitive(RNDR_PRTITYP_BOOLEAN, "" + aValue);
    }

    /**
     * Convert a byte primitive to JSON.
     * @param aValue
     * @return An JSON representation of the byte primitive.
     */
    public JSONObject marshal(byte aValue)
    {
        return marshalPrimitive(RNDR_PRTITYP_BYTE, "" + aValue);
    }

    /**
     * Convert a short primitive to JSON.
     * @param aValue
     * @return An JSON representation of the short primitive.
     */
    public JSONObject marshal(short aValue)
    {
        return marshalPrimitive(RNDR_PRTITYP_SHORT, "" + aValue);
    }

    /**
     * Convert a char primitive to JSON.
     * @param aValue
     * @return An JSON representation of the char primitive.
     */
    public JSONObject marshal(char aValue)
    {
        return marshalPrimitive(RNDR_PRTITYP_CHAR, "" + aValue);
    }

    /**
     * Convert an int primitive to JSON.
     * @param aValue
     * @return An JSON representation of the int primitive.
     */
    public JSONObject marshal(int aValue)
    {
        return marshalPrimitive(RNDR_PRTITYP_INT, "" + aValue);
    }

    /**
     * Convert a long primitive to JSON.
     * @param aValue
     * @return An JSON representation of the long primitive.
     */
    public JSONObject marshal(long aValue)
    {
        return marshalPrimitive(RNDR_PRTITYP_LONG, "" + aValue);
    }

    /**
     * Convert a float primitive to JSON.
     * @param aValue
     * @return An JSON representation of the float primitive.
     */
    public JSONObject marshal(float aValue)
    {
        return marshalPrimitive(RNDR_PRTITYP_FLOAT, "" + aValue);
    }

    /**
     * Convert a double primitive to JSON.
     * @param aValue
     * @return An JSON representation of the double primitive.
     */
    public JSONObject marshal(double aValue)
    {
        return marshalPrimitive(RNDR_PRTITYP_DOUBLE, "" + aValue);
    }

    private JSONObject marshalPrimitive(String aType, String aValue)
    {
        final JSONObject lElement = new JSONObject();
        lElement.getValue().put(RNDR_ATTR_KIND, new JSONString(RNDR_PRIM));
        lElement.getValue().put(RNDR_ATTR_TYPE, new JSONString(aType));
        lElement.getValue().put(RNDR_ATTR_VALUE, new JSONString(aValue));
        return lElement;
    }

    /**
     * Convert a Java object to JSON.
     * @param aObj
     * @return The JSON representation of the Java object.
     * @throws SerializerException An error occurred while converting the Java object to JSON.
     */
    public JSONObject marshal(Object aObj)
    throws SerializerException
    {
        return marshalImpl(aObj, new HashMap<Object, Object>());
    }

    public JSONObject marshalImpl(Object aObj, HashMap<Object, Object> aPool)
    throws SerializerException
    {
        // Handle null references quickly.
        if(aObj == null)
        {
            final JSONObject lElement = new JSONObject();
            lElement.getValue().put(RNDR_ATTR_KIND, new JSONString(RNDR_NULL));
            return lElement;
        }
        else
        {
            final Class<?> lObjectClass = aObj.getClass();
            final String lObjectClassName = lObjectClass.getName();
            // A reference to the object will be used for storage as a key in the
            // hash table for identity reasons. Two objects are different if they
            // are different in memory, even if they are equal().
            // Serialization should not change the original layout of the objects.
            final Reference lRef = new Reference(aObj);

            if(aPool.containsKey(lRef))
            {
                // We already rendered this object in the past.
                // We have to render to a reference.
                final JSONObject lElement = new JSONObject();
                lElement.getValue().put(RNDR_ATTR_KIND, new JSONString(RNDR_OBJREF));
                lElement.getValue().put(RNDR_ATTR_REF, new JSONString((String) aPool.get(lRef)));
                return lElement;
            }
            else
            {
                // We did not encounter this object before.
                // We generate a new key and associate it with the object.
                final String lObjectId = generateId();
                aPool.put(lRef, lObjectId);

                if(lObjectClass.isArray()) return marshalImplArray(aObj, aPool);
                else return marshalImplObj(aObj, lObjectId, lObjectClass, lObjectClassName, aPool);
            }
        }
    }

    private ArraySerializer arrayHelper = new ArraySerializer();

    private JSONObject marshalImplArray(Object aObj, HashMap<Object, Object> aPool)
    throws SerializerException
    {
        final Class<?> lClass = aObj.getClass();
        final String lObjClassName = lClass.getName();
        final JSONObject lArrElement = new JSONObject();
        lArrElement.getValue().put(RNDR_ATTR_KIND, new JSONString(RNDR_ARR));
        lArrElement.getValue().put(RNDR_ATTR_CLASS, new JSONString(lObjClassName));
        arrayHelper.toJSON(aObj, lArrElement, this, aPool);
        return lArrElement;
    }

    private JSONObject marshalImplObj(Object aObj, String aObjId, Class<?> aObjClass, String aObjClassName, HashMap<Object, Object> aPool)
    throws SerializerException
    {
        final JSONObject lObjElement = new JSONObject();
        lObjElement.getValue().put(RNDR_ATTR_KIND, new JSONString(RNDR_OBJ));
        lObjElement.getValue().put(RNDR_ATTR_ID, new JSONString(aObjId));
        lObjElement.getValue().put(RNDR_ATTR_CLASS, new JSONString(aObjClassName));

        final ClassSerializer lHelper = repo.findHelper(aObjClass);
        lHelper.toJSON(aObj, lObjElement, this, aPool);
        return lObjElement;
    }

    private String generateId()
    {
        String lId = IDPREFIX + idCounter;
        idCounter++;
        return lId;
    }

    /**
     * Convert a JSON representation to the Java primitive or reference.
     * @param aElement
     * @return The Java representation of the JSON. This value can represent a Java primitive value or
     *         it can represent a Java reference.
     * @throws SerializerException An error occured while trying to convert the JSON representation into a
     *         Java representation.
     */
    public SerializerValue unmarshal(JSONObject aElement)
    throws SerializerException
    {
        requireStringAttribute(aElement, RNDR_ATTR_KIND);
        final String lElementKind = ((JSONString) aElement.get(RNDR_ATTR_KIND)).getValue();
        final Object lUnmarshalled = unmarshalImpl(aElement, new HashMap<Object, Object>());

        // Put primitive types in corresponding instance vars.
        if (RNDR_PRIM.equals(lElementKind))
        {
            if (lUnmarshalled instanceof Boolean)
                return new SerializerValueImpl(((Boolean) lUnmarshalled).booleanValue());
            else if (lUnmarshalled instanceof Byte)
                return new SerializerValueImpl(((Byte) lUnmarshalled).byteValue());
            else if (lUnmarshalled instanceof Short)
                return new SerializerValueImpl(((Short) lUnmarshalled).shortValue());
            else if (lUnmarshalled instanceof Character)
                return new SerializerValueImpl(((Character) lUnmarshalled).charValue());
            else if (lUnmarshalled instanceof Integer)
                return new SerializerValueImpl(((Integer) lUnmarshalled).intValue());
            else if (lUnmarshalled instanceof Long)
                return new SerializerValueImpl(((Long) lUnmarshalled).longValue());
            else if (lUnmarshalled instanceof Float)
                return new SerializerValueImpl(((Float) lUnmarshalled).floatValue());
            else if (lUnmarshalled instanceof Double)
                return new SerializerValueImpl(((Double) lUnmarshalled).doubleValue());
            else
            {
                throw new SerializerException(String.format(SER001, lUnmarshalled.getClass().getName(), aElement.getLine(), aElement.getCol()));
            }
        }
        else
        {
            return new SerializerValueImpl(lUnmarshalled);
        }
    }

   // Internal implementation. Always uses return objects, never primitives.
    public Object unmarshalImpl(JSONObject aElement, HashMap<Object, Object> aObjectPool)
    throws SerializerException
   {
       requireStringAttribute(aElement, RNDR_ATTR_KIND);
       final String lElementKind = ((JSONString) aElement.get(RNDR_ATTR_KIND)).getValue();
       if (RNDR_OBJREF.equals(lElementKind))
       {
           requireStringAttribute(aElement, RNDR_ATTR_REF);
           final String lRef = ((JSONString) aElement.get(RNDR_ATTR_REF)).getValue();
           //noinspection SuspiciousMethodCalls
           final Object lObjFromPool = aObjectPool.get(lRef);
           if (lObjFromPool == null)
           {
               throw new SerializerException(String.format(SER002, lRef));
           }
           return lObjFromPool;
       }
       else
       {
           if (RNDR_PRIM.equals(lElementKind))
           {
               return unmarshalImplPrimitive(aElement);
           }
           else if (RNDR_NULL.equals(lElementKind))
           {
               return null;
           }
           else
           {
               if (RNDR_ARR.equals(lElementKind))
               {
                   final ArraySerializer lAh = new ArraySerializer();
                   return lAh.toJava(aElement, this, aObjectPool);
               }
               else if (RNDR_OBJ.equals(lElementKind))
               {
                   requireStringAttribute(aElement, RNDR_ATTR_CLASS);
                   final String lBeanClassName = ((JSONString) aElement.get(RNDR_ATTR_CLASS)).getValue();

                   try
                   {
                       if (lBeanClassName == null)
                       {
                           throw new SerializerException(String.format(SER003, RNDR_ATTR_CLASS));
                       }

                       String lId = null;
                       try
                       {
                           JSONSerializer.requireStringAttribute(aElement, JSONSerializer.RNDR_ATTR_ID);
                           lId = ((JSONString) aElement.get(JSONSerializer.RNDR_ATTR_ID)).getValue();
                       }
                       catch (Exception eIgnore)
                       {
                       }

                       final Class<?> lBeanClass = Class.forName(lBeanClassName);
                       ClassSerializer lHelper = repo.findHelper(lBeanClass);
                       Object lResult =  lHelper.toJava(aElement, this, aObjectPool);
                       if(lId != null) aObjectPool.put(lId, lResult);
                       return lResult;
                   }
                   catch (ClassNotFoundException e)
                   {
                       throw new SerializerException(String.format(SER004, lBeanClassName), e);
                   }
               }
               else
               {
                   throw new SerializerException(String.format(SER005, lElementKind));
               }
           }
       }
   }

    private Object unmarshalImplPrimitive(JSONObject aElement)
    throws SerializerException
    {
        requireStringAttribute(aElement, RNDR_ATTR_TYPE);
        requireStringAttribute(aElement, RNDR_ATTR_VALUE);

        final String lType = ((JSONString) aElement.get(RNDR_ATTR_TYPE)).getValue();
        final String lValue = ((JSONString) aElement.get(RNDR_ATTR_VALUE)).getValue();

        try
        {
            if("boolean".equals(lType)) return new Boolean(lValue);
            else if("byte".equals(lType)) return new Byte(lValue);
            else if("short".equals(lType)) return new Short(lValue);
            else if("char".equals(lType)) return new Character(lValue.charAt(0));
            else if("int".equals(lType)) return new Integer(lValue);
            else if("long".equals(lType)) return new Long(lValue);
            else if("float".equals(lType)) return new Float(lValue);
            else if("double".equals(lType)) return new Double(lValue);
            else
            {
                throw new SerializerException(String.format(SER006, lType));
            }
        }
        catch(SerializerException passtrough)
        {
            throw passtrough;
        }
        catch(Exception e)
        {

            throw new SerializerException(String.format(SER007, lType, lValue), e);
        }
    }

    public static void requireStringAttribute(JSONObject aElement, String anAttribute)
    throws SerializerException
    {
        if(!aElement.containsKey(anAttribute))
        {
             throw new SerializerException(String.format(SER008, anAttribute, aElement.getLine(), aElement.getCol()));
        }

        if(!(aElement.get(anAttribute) instanceof JSONString))
        {
            throw new SerializerException(String.format(SER009, anAttribute, aElement.getLine(), aElement.getCol()));
        }
    }

    /**
     * Add custom helper class.
     *
     * @param aHelper the custom helper you want to add to the serializer.
     */
    public void addHelper(ClassSerializer aHelper)
    {
        repo.addHelper(aHelper);
    }

    /**
     * The objects that fall back on the general object helper will be serialized by
     * using their fields directly. Without further annotations, the default
     * constructor without arguments will be used in the POJO. If this is not sufficient,
     * the @JSONConstructor and @JSONConstructorArgs annotations can be used as well in the  POJO to
     * indicate which constructor has to be used.
     */
    public void usePojoAccess()
    {
        setSerializeOption(OPT_OBJSERIALIZE, OBJSERIALIZE_FIELD);
    }

    /**
     * The objects that fall back on the general object helper will be serialized by
     * using their JavaBean properties. The  JavaBean always needs a
     * default constructor without arguments.
     */
    public void useJavaBeanAccess()
    {
        setSerializeOption(OPT_OBJSERIALIZE, OBJSERIALIZE_PROPERTY);
    }

    public void setSerializeOption(String key, Object value) {
        options.put(key,  value);
    }

    public Object getSerializeOption(String key, Object defaultValue) {
        if(options.containsKey(key)) return options.get(key);
        else return defaultValue;
    }

    public boolean hasSerializeOption(String key) {
        return options.containsKey(key);
    }
}
