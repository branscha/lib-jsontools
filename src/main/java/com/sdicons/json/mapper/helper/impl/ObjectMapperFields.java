/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.mapper.helper.impl;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.sdicons.json.annotations.JSONConstructor;
import com.sdicons.json.annotations.JSONConstructorArgs;
import com.sdicons.json.mapper.JSONMapper;
import com.sdicons.json.mapper.MapperException;
import com.sdicons.json.mapper.helper.ClassMapper;
import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONValue;

public class ObjectMapperFields
implements ClassMapper
{
    private static final String OBJ001 = "JSONMapper/ObjectMapperFields/001: JSON<->Java. Found inconsistency in class: '%1$s'. If annotated methods are used, it should contain both @JSONConstructor and @JSONConstructorArgs together.";
    private static final String OBJ002 = "JSONMapper/ObjectMapperFields/002: JSON->Java. Cannot map JSON class '%s' to a JavaBean.";
    private static final String OBJ003 = "JSONMapper/ObjectMapperFields/003: JSON->Java. Error while calling the @JSONConstructor constructor in class: '%s' on parameter nr: %d with a value of class '%s'.";
    private static final String OBJ004 = "JSONMapper/ObjectMapperFields/004: JSON->Java. Failed to instantiate an object (using annotated constructor) of class: '%s'.";
    private static final String OBJ005 = "JSONMapper/ObjectMapperFields/005: JSON->Java. Type error while setting the field: '%s' in class '%s' with a value of class: '%s'.";
    private static final String OBJ006 = "JSONMapper/ObjectMapperFields/006: JSON->Java. Error while creating java object. Failed to invoke 'readResolve' on instance of class: '%s'.";
    private static final String OBJ007 = "JSONMapper/ObjectMapperFields/007: JSON->Java. IllegalAccessException while trying to instantiate class '%s'.";
    private static final String OBJ008 = "JSONMapper/ObjectMapperFields/008: JSON->Java. InstantiationException while trying to instantiate class '%s'.";
    private static final String OBJ009 = "JSONMapper/ObjectMapperFields/009: Java->JSON. Error while trying to invoke 'writeReplace' on instance of class: '%s'.";
    private static final String OBJ010 = "JSONMapper/ObjectMapperFields/010: Java->JSON. Error while reading field: '%s' from instance of class '%s'.";
    private static final String OBJ011 = "JSONMapper/ObjectMapperFields/011: Java->JSON. Error while invoking the @JSONConstructorArgs method called '%s(...)' on an instance of class '%s'.";
    private static final String OBJ012 = "JSONMapper/ObjectMapperFields/012: Java->JSON. Error while serializing element nr %d from the @JSONConstructorArgs method: '%s(...)' on instance of class: '%s'.";

    public Class<?> getHelpedClass()
    {
        return Object.class;
    }

    private Map<Class<?>, AnnotatedMethods> annotatedPool = new HashMap<Class<?>, AnnotatedMethods>();

    private static class AnnotatedMethods
    {
        public Constructor<?> cons;
        public Method serialize;

        public AnnotatedMethods(Constructor<?> aCons, Method aSerialize)
        {
            cons = aCons;
            serialize = aSerialize;
        }
    }

    protected Method getAnnotatedSerializingMethod(Class<?> aClass)
    {
        // Check if we have an annotated class.
        for(Method lMethod : aClass.getDeclaredMethods())
        {
            if(lMethod.isAnnotationPresent(JSONConstructorArgs.class))
            {
                lMethod.setAccessible(true);
                return lMethod;
            }
        }
        return null;
    }

    protected Constructor<?> getAnnotatedConstructor(Class<?> aClass)
    {
        //Check if we have a class with an annotated constructor
        final Constructor<?>[] lConstructors = aClass.getDeclaredConstructors();
        for(Constructor<?> lCons : lConstructors)
            if(lCons.isAnnotationPresent(JSONConstructor.class))
            {
                // Found the constructor we are
                // looking for.
                lCons.setAccessible(true);
                return lCons;
            }
        return null;
    }

    // Accessing a shared object should be synced.
    protected synchronized AnnotatedMethods getAnnotatedMethods(Class<?> aClass)
    throws MapperException
    {
        AnnotatedMethods lResult = annotatedPool.get(aClass);
        if(lResult == null)
        {
            final Constructor<?> lCons = getAnnotatedConstructor(aClass);
            final Method lMeth = getAnnotatedSerializingMethod(aClass);

            if((lMeth == null && lCons != null) || (lMeth != null && lCons == null))
                throw new MapperException(String.format(OBJ001, aClass.getClass().getName()));

            lResult = new AnnotatedMethods(lCons, lMeth);
            annotatedPool.put(aClass, lResult);
        }
        return lResult;
    }

    protected List<Field> getFieldInfo(Class<?> aClass)
    {
        final List<Field> lJavaFields = new LinkedList<Field>();
        Class<?> lClassWalker = aClass;
        while (lClassWalker != null)
        {
            final Field[] lClassFields = lClassWalker.getDeclaredFields();
            for (Field lFld : lClassFields)
            {
                int lModif = lFld.getModifiers();
                if (!Modifier.isTransient(lModif) &&
                        !Modifier.isAbstract(lModif) &&
                        !Modifier.isStatic(lModif) &&
                        !Modifier.isFinal(lModif))
                {
                    lFld.setAccessible(true);
                    lJavaFields.add(lFld);
                }
            }
            lClassWalker = lClassWalker.getSuperclass();
        }
        return lJavaFields;
    }

    public Object toJava(JSONMapper mapper, JSONValue aValue, Class<?> aRequestedClass)
    throws MapperException
    {
        if(!aValue.isObject()) throw new MapperException(String.format(OBJ002 , aValue.getClass().getName()));
        JSONObject aObject = (JSONObject) aValue;

        try
        {
            // The result can be constructed in two ways, an annotated constructor
            // or the default constructor. At this point we don't know which of the two
            // methods will be used.
            Object lResult;

            // Find info about the annotated methods.
            final AnnotatedMethods lAnnotated = getAnnotatedMethods(aRequestedClass);
            if (lAnnotated.cons != null)
            {
                // Let's get the field values to pass to the constructor
                int lCnt = lAnnotated.cons.getParameterTypes().length;
                final Object[] lAttrs = new Object[lCnt];
                for (int i = 0; i < lCnt; i++)
                {
                    final String lFldName = "cons-" + i;
                    final JSONValue lSubEl = aObject.get(lFldName);

                    try
                    {
                        lAttrs[i] = mapper.toJava(lSubEl, lAnnotated.cons.getParameterTypes()[i]);
                    }
                    catch(MapperException e)
                    {
                        throw new MapperException(String.format(OBJ003, aRequestedClass.getName(), i, lAnnotated.cons.getParameterTypes()[i].getName()), e);
                    }
                }

                // Create a new instance using the annotated constructor.
                try
                {
                    lResult = lAnnotated.cons.newInstance(lAttrs);
                }
                catch(Exception e)
                {
                    throw new MapperException(String.format(OBJ004, aRequestedClass.getName()), e);
                }
            }
            else
            {
                // Just use the default constructor.
                lResult = aRequestedClass.newInstance();
            }

            final List<Field> lJavaFields = getFieldInfo(aRequestedClass);
            for (String lPropname : aObject.getValue().keySet())
            {
                // Fetch subelement information.
                final JSONValue lSubEl = aObject.get(lPropname);
                for (Field lFld : lJavaFields)
                {
                    // Write the property.
                    if (lFld.getName().equals(lPropname))
                    {
                        Object lFldValue;
                        final Type lGenType = lFld.getGenericType();
                        if(lGenType instanceof ParameterizedType)
                            lFldValue = mapper.toJava(lSubEl, (ParameterizedType) lGenType);
                        else
                            lFldValue = mapper.toJava(lSubEl, lFld.getType());

                        try
                        {
                            lFld.setAccessible(true);
                            lFld.set(lResult, lFldValue);
                            break;
                        }
                        catch (Exception e)
                        {
                            throw new MapperException(String.format(OBJ005, lFld.getName(), aRequestedClass.getName(), lFldValue.getClass().getName()), e);
                        }
                    }
                }
            }

            // First we have to cope with the special case of "readResolve" objects.
            // It is described in the official specs of the serializable interface.
            ////////////////////////////////////////////////////////////////////////////
            if (lResult instanceof Serializable)
            {
                try
                {
                    final Method lReadResolve = lResult.getClass().getDeclaredMethod("readResolve");
                    if (lReadResolve != null)
                    {
                        lReadResolve.setAccessible(true);
                        lResult = lReadResolve.invoke(lResult);
                    }
                }
                catch (NoSuchMethodException e)
                {
                    // Do nothing, just continue normal operation.
                }
                catch (Exception e)
                {
                    throw new MapperException(String.format(OBJ006, lResult.getClass().getName()), e);
                }
            }
            ////////////////////////////////////////////////////////////////////////////

            return lResult;
        }
        catch (IllegalAccessException e)
        {
            throw new MapperException(String.format(OBJ007, aRequestedClass.getName()), e);
        }
        catch (InstantiationException e)
        {
            throw new MapperException(String.format(OBJ008, aRequestedClass.getName()), e);
        }
    }

    public JSONValue toJSON(JSONMapper mapper, Object aPojo)
    throws MapperException
    {
        // We will render the bean properties as the elements of a JSON object.
        final JSONObject lElements = new JSONObject();

        // First we have to cope with the special case of "writeReplace" objects.
        // It is described in the official specs of the serializable interface.
        ////////////////////////////////////////////////////////////////////////////
        if(aPojo instanceof Serializable)
        {
            try
            {
                final Method lWriteReplace = aPojo.getClass().getDeclaredMethod("writeReplace");
                if(lWriteReplace != null)
                {
                    lWriteReplace.setAccessible(true);
                    return mapper.toJSON(lWriteReplace.invoke(aPojo));
                }
            }
            catch(NoSuchMethodException e)
            {
                // Do nothing, just continue normal operation.
            }
            catch(Exception e)
            {
                throw new MapperException(String.format(OBJ009, aPojo.getClass().getName()), e);
            }
        }
        ////////////////////////////////////////////////////////////////////////////

        Class<?> lJavaClass = aPojo.getClass();
        final List<Field> lJavaFields = getFieldInfo(lJavaClass);
        // Find info about the annotated methods.
        final AnnotatedMethods lAnnotated = getAnnotatedMethods(lJavaClass);

        for(Field lFld : lJavaFields)
        {
            try
            {
                lFld.setAccessible(true);
                final JSONValue lFieldVal = mapper.toJSON(lFld.get(aPojo));
                lElements.getValue().put(lFld.getName(), lFieldVal);
            }
            catch(Exception e)
            {
                throw new MapperException(String.format(OBJ010, lFld.getName(), lJavaClass.getName()), e);
            }
        }

        // Check if we have an annotated class.
        if (lAnnotated.serialize != null)
        {
            Object[] lVals;
            try
            {
                lVals = (Object[]) lAnnotated.serialize.invoke(aPojo);
            }
            catch(Exception e)
            {
                throw new MapperException(String.format(OBJ011, lAnnotated.serialize.getName(), lJavaClass.getName()), e);
            }

            int i = 0;
            try
            {
                for (Object lVal : lVals)
                {
                    final JSONValue lFieldVal = mapper.toJSON(lVal);
                    lElements.getValue().put("cons-" + i, lFieldVal);
                    i++;
                }
            }
            catch(MapperException e)
            {
                throw new MapperException(String.format(OBJ012, i, lAnnotated.serialize.getName(), lJavaClass.getName()), e);
            }
        }
        return lElements;
    }
}
