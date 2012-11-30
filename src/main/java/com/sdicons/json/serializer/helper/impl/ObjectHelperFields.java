/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.serializer.helper.impl;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.sdicons.json.helper.JSONConstructor;
import com.sdicons.json.helper.JSONConstructorArgs;
import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.serializer.JSONSerializer;
import com.sdicons.json.serializer.SerializerException;
import com.sdicons.json.serializer.helper.SerializerHelper;

public class ObjectHelperFields
implements SerializerHelper
{
    // Error messages.
    //
    private static final String OBJ001 = "JSONSerializer/ObjectHelperFields/001: JSON<->Java. Found inconsistency in class: '%s'. If annotated methods are used, it should contain both @JSONConstructor and @JSONConstructorArgs together.";
    private static final String OBJ002 = "JSONSerializer/ObjectHelperFields/002: Java->JSON. Error while trying to invoke 'writeReplace' on instance of class: '%1$s'.";
    private static final String OBJ003 = "JSONSerializer/ObjectHelperFields/003: Java->JSON. Error while invoking the @JSONConstructorArgs method called '%1$s(...)' on an instance of class: '%2$s'.";
    private static final String OBJ004 = "JSONSerializer/ObjectHelperFields/004: Java->JSON. Error while serializing element nr %1$d from the @JSONConstructorArgs method: '%2$s(...)' on instance of class '%3$s'.";
    private static final String OBJ005 = "JSONSerializer/ObjectHelperFields/005: Java->JSON. Error while serializing field: '%1$s' from instance of class: '%2$s'.";
    private static final String OBJ006 = "JSONSerializer/ObjectHelperFields/006: Java->JSON. Error while reading field: '%1$s' from instance of class: '%2$s'.";
    private static final String OBJ007 = "JSONSerializer/ObjectHelperFields/007: JSON->Java. Error while calling the @JSONConstructor constructor in class: '%1$s' on parameter nr: %2$d with a value of class '%3$s'.";
    private static final String OBJ008 = "JSONSerializer/ObjectHelperFields/008: JSON->Java. Tried to instantiate an object (using annotated constructor) of class '%1$s'.";
    private static final String OBJ009 = "JSONSerializer/ObjectHelperFields/009: JSON->Java. Type error while trying to set the field: '%1$s' in class: '%2$s' with a value of class '%3$s'.";
    private static final String OBJ010 = "JSONSerializer/ObjectHelperFields/010: JSON->Java. Tried to invoke 'readResolve' on instance of class: '%1$s'.";
    private static final String OBJ011 = "JSONSerializer/ObjectHelperFields/011: JSON->Java. Could not find JavaBean class '%s'.";
    private static final String OBJ012 = "JSONSerializer/ObjectHelperFields/012: JSON->Java. IllegalAccessException while trying to instantiate bean of class '%s'.";
    private static final String OBJ013 = "JSONSerializer/ObjectHelperFields/013: JSON->Java. InstantiationException while trying to instantiate bean of class '%s'. ";

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

    // Accessing a shared object should be synced.
    protected synchronized AnnotatedMethods getAnnotatedMethods(Class<?> aClass)
    throws SerializerException
    {
        AnnotatedMethods lResult = annotatedPool.get(aClass);
        if(lResult == null)
        {
            final Constructor<?> lCons = getAnnotatedConstructor(aClass);
            final Method lMeth = getAnnotatedSerializingMethod(aClass);

            if((lMeth == null && lCons != null) || (lMeth != null && lCons == null))
                throw new SerializerException(String.format(OBJ001, aClass.getClass().getName()));

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

    public void toJSON(Object aObj, JSONObject aObjectElement, JSONSerializer serializer, HashMap<Object, Object> aPool)
    throws SerializerException
    {
        // First we have to cope with the special case of "writeReplace" objects.
        // It is described in the official specifications of the serializable interface.
        ////////////////////////////////////////////////////////////////////////////
        if(aObj instanceof Serializable)
        {
            try
            {
                final Method lWriteReplace = aObj.getClass().getDeclaredMethod("writeReplace");
                if(lWriteReplace != null)
                {
                    lWriteReplace.setAccessible(true);
                    final JSONObject lOele = serializer.marshalImpl(lWriteReplace.invoke(aObj), aPool);
                    aObjectElement.getValue().put(JSONSerializer.RNDR_ATTR_VALUE, lOele);
                    return;
                }
            }
            catch(NoSuchMethodException e)
            {
                // Do nothing, just continue normal operation.
            }
            catch(Exception e)
            {
                throw new SerializerException(String.format(OBJ002, aObj.getClass().getName()), e);
            }
        }
        ////////////////////////////////////////////////////////////////////////////

        // We will render the bean properties as the elements of a JSON object.
        final JSONObject lElements = new JSONObject();
        aObjectElement.getValue().put(JSONSerializer.RNDR_ATTR_VALUE, lElements);

        Class<?> lClass = aObj.getClass();
        // Fetch the field  information, we need this in several places.
        final List<Field> lJavaFields = getFieldInfo(lClass);
        // Find info about the annotated methods.
        final AnnotatedMethods lAnnotated = getAnnotatedMethods(lClass);

        // Check if we have an annotated class.
        if(lAnnotated.serialize != null)
        {
            Object[] lVals;
            try
            {
                lVals = (Object[]) lAnnotated.serialize.invoke(aObj);
            }
            catch(Exception e)
            {
                throw new SerializerException(String.format(OBJ003, lAnnotated.serialize.getName(), lClass.getName()), e);
            }

            int i = 0;
            try
            {
                for(Object lVal : lVals)
                {
                    lElements.getValue().put("cons-" + i, serializer.marshalImpl(lVal, aPool));
                    i++;
                }
            }
            catch(SerializerException e)
            {
                throw new SerializerException(String.format(OBJ004, i, lAnnotated.serialize.getName(), lClass.getName()), e);
            }
        }

        for(Field lFld : lJavaFields)
        {
            try
            {
                lFld.setAccessible(true);
                lElements.getValue().put(lFld.getName(), serializer.marshalImpl(lFld.get(aObj), aPool));
            }
            catch(SerializerException e)
            {
                throw new SerializerException(String.format(OBJ005, lFld.getName(), lClass.getName()), e);
            }
            catch(Exception e)
            {
                throw new SerializerException(String.format(OBJ006, lFld.getName(), lClass.getName()), e);
            }
        }
    }

    public Object toJava(JSONObject aObjectElement, JSONSerializer serializer, HashMap<Object, Object> aPool)
    throws SerializerException
    {
        JSONSerializer.requireStringAttribute(aObjectElement, JSONSerializer.RNDR_ATTR_CLASS);
        final String lBeanClassName = ((JSONString) aObjectElement.get(JSONSerializer.RNDR_ATTR_CLASS)).getValue();

        String lId = null;
        try
        {
            JSONSerializer.requireStringAttribute(aObjectElement, JSONSerializer.RNDR_ATTR_ID);
            lId = ((JSONString) aObjectElement.get(JSONSerializer.RNDR_ATTR_ID)).getValue();
        }
        catch(Exception eIgnore){}

        try
        {
            final Class<?> lBeanClass = Class.forName(lBeanClassName);
            final List<Field> lJavaFields = getFieldInfo(lBeanClass);
            final AnnotatedMethods lAnnotated = getAnnotatedMethods(lBeanClass);
            final JSONObject lProperties = (JSONObject) aObjectElement.get(JSONSerializer.RNDR_ATTR_VALUE);

            Object lBean;

            if (lAnnotated.cons != null)
            {
                // Let's get the field values to pass to the constructor
                int lCnt = lAnnotated.cons.getParameterTypes().length;
                final Object[] lAttrs = new Object[lCnt];
                for (int i = 0; i < lCnt; i++)
                {
                    final String lFldName = "cons-" + i;
                    // Fetch subelement information.
                    JSONObject lSubEl = (JSONObject) lProperties.get(lFldName);

                    try
                    {
                        lAttrs[i] = serializer.unmarshalImpl(lSubEl, aPool);
                    }
                    catch(SerializerException e)
                    {
                        throw new SerializerException(String.format(OBJ007, lBeanClass.getName(), i, lSubEl.getClass().getName()), e);
                    }
                }

                // Create a new instance using the annotated constructor.
                try
                {
                    lBean = lAnnotated.cons.newInstance(lAttrs);
                }
                catch(Exception e)
                {
                    throw new SerializerException(String.format(OBJ008, lBeanClass.getName()), e);
                }
            }
            else
            {
                // Create a new instance using the default constructor
                lBean = lBeanClass.newInstance();
            }

            if (lId != null) aPool.put(lId, lBean);

            for(String lPropname : lProperties.getValue().keySet())
            {
                for (Field lFld : lJavaFields)
                {
                    // Write the property.
                    if (lFld.getName().equals(lPropname))
                    {
                        JSONObject lSubEl = (JSONObject) lProperties.get(lPropname);
                        final Object lFldValue = serializer.unmarshalImpl(lSubEl, aPool);

                        try
                        {
                            lFld.setAccessible(true);
                            lFld.set(lBean, lFldValue);
                            break;
                        }
                        catch (Exception e)
                        {
                            throw new SerializerException(String.format(OBJ009, lFld.getName(), lBeanClass.getName(), lFldValue.getClass().getName()), e);
                        }
                    }
                }
            }

            // First we have to cope with the special case of "readResolve" objects.
            // It is described in the official specs of the serializable interface.
            ////////////////////////////////////////////////////////////////////////////
            if (lBean instanceof Serializable)
            {
                try
                {
                    final Method lReadResolve = lBean.getClass().getDeclaredMethod("readResolve");
                    if (lReadResolve != null)
                    {
                        lReadResolve.setAccessible(true);
                        lBean = lReadResolve.invoke(lBean);
                        // Replace previous instance of the stored reference.
                        aPool.put(aObjectElement, lBean);
                    }
                }
                catch (NoSuchMethodException e)
                {
                    // Do nothing, just continue normal operation.
                }
                catch (Exception e)
                {
                    throw new SerializerException(String.format(OBJ010, lBean.getClass().getName()), e);
                }
            }
            ////////////////////////////////////////////////////////////////////////////

            return lBean;
        }
        catch (ClassNotFoundException e)
        {
            throw new SerializerException(String.format(OBJ011, lBeanClassName), e);
        }
        catch (IllegalAccessException e)
        {
            throw new SerializerException(String.format(OBJ012, lBeanClassName), e);
        }
        catch (InstantiationException e)
        {
            throw new SerializerException(String.format(OBJ013, lBeanClassName), e);
        }
    }

    public Class<?> getHelpedClass()
    {
        return Object.class;
    }
}
