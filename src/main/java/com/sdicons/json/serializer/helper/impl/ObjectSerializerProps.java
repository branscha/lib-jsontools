/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.serializer.helper.impl;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.serializer.JSONSerializer;
import com.sdicons.json.serializer.SerializerException;
import com.sdicons.json.serializer.helper.ClassSerializer;

public class ObjectSerializerProps
implements ClassSerializer
{
    // Error messages.
    //
    private static final String OBJ001 = "JSONSerializer/ObjectSerializerProps/001: Java->JSON. Error while introspecting JavaBean of class '%s'.";
    private static final String OBJ002 = "JSONSerializer/ObjectSerializerProps/002: Java->JSON. Illegal access while trying to fetch a bean property '%s' of class '%s'.";
    private static final String OBJ003 = "JSONSerializer/ObjectSerializerProps/003: JSON->Java. Could not find a setter for property '%s' in class '%s'.";
    private static final String OBJ004 = "JSONSerializer/ObjectSerializerProps/004: JSON->Java. Could not find JavaBean class '%s'.";
    private static final String OBJ005 = "JSONSerializer/ObjectSerializerProps/005: JSON->Java. IllegalAccessException while trying to instantiate bean '%s'.";
    private static final String OBJ006 = "JSONSerializer/ObjectSerializerProps/006: JSON->Java. InstantiationException while trying to instantiate bean '%s'.";
    private static final String OBJ007 = "JSONSerializer/ObjectSerializerProps/007: JSON->Java. IntrospectionException while trying to fill bean '%s'.";
    private static final String OBJ008 = "JSONSerializer/ObjectSerializerProps/008: JSON->Java. InvocationTargetException while trying to fill bean '%s'.";

    public void toJSON(Object aObj, JSONObject aObjectElement, JSONSerializer serializer, HashMap<Object, Object> aPool)
    throws SerializerException
    {
        // We will render the bean properties as the elements of a JSON object.
        final JSONObject lElements = new JSONObject();
        aObjectElement.getValue().put(JSONSerializer.RNDR_ATTR_VALUE, lElements);
        Class<?> lClass = aObj.getClass();

        try
        {
            PropertyDescriptor[] lPropDesc = Introspector.getBeanInfo(lClass, Introspector.USE_ALL_BEANINFO).getPropertyDescriptors();
            for (PropertyDescriptor aLPropDesc : lPropDesc)
            {
                Method lReader = aLPropDesc.getReadMethod();
                Method lWriter = aLPropDesc.getWriteMethod();
                String lPropName = aLPropDesc.getName();

                // Only serialize if the property is READ-WRITE.
                if (lReader != null && lWriter != null)
                {
                    try {
                        lElements.getValue().put(lPropName, serializer.marshalImpl(lReader.invoke(aObj), aPool));
                    }
                    catch(IllegalAccessException e)
                    {
                        throw new SerializerException(String.format(OBJ002, lPropName, lClass.getName()), e);
                    }
                    catch(InvocationTargetException e)
                    {
                        throw new SerializerException(String.format(OBJ002, lPropName, lClass.getName()), e);
                    }
                }
            }
        }
        catch(IntrospectionException e)
        {
            throw new SerializerException(String.format(OBJ001, lClass.getName()), e);
        }
    }

    public Object toJava(JSONObject aObjectElement, JSONSerializer serializer, HashMap<Object, Object> aPool)
    throws SerializerException
    {
        JSONSerializer.requireStringAttribute(aObjectElement, JSONSerializer.RNDR_ATTR_CLASS);
        String lBeanClassName = ((JSONString) aObjectElement.get(JSONSerializer.RNDR_ATTR_CLASS)).getValue();

       String lId = null;
        try
        {
            JSONSerializer.requireStringAttribute(aObjectElement, JSONSerializer.RNDR_ATTR_ID);
            lId = ((JSONString) aObjectElement.get(JSONSerializer.RNDR_ATTR_ID)).getValue();
        }
        catch(Exception eIgnore){}

        try
        {
            Class<?> lBeanClass = Class.forName(lBeanClassName);
            Object lBean;

            lBean = lBeanClass.newInstance();
            if (lId != null) aPool.put(lId, lBean);

            JSONObject lProperties = (JSONObject) aObjectElement.get(JSONSerializer.RNDR_ATTR_VALUE);

            for(String lPropname : lProperties.getValue().keySet())
            {
                // Fetch subelement information.
                JSONObject lSubEl = (JSONObject) lProperties.get(lPropname);
                Object lProp = serializer.unmarshalImpl(lSubEl, aPool);

                // Put the property in the bean.
                boolean lFoundWriter = false;
                PropertyDescriptor[] lPropDesc = Introspector.getBeanInfo(lBeanClass, Introspector.USE_ALL_BEANINFO).getPropertyDescriptors();
                for(PropertyDescriptor aLPropDesc : lPropDesc)
                {
                    if(aLPropDesc.getName().equals(lPropname))
                    {
                        lFoundWriter = true;
                        Method lWriter = aLPropDesc.getWriteMethod();
                        if(lWriter == null)
                        {
                            throw new SerializerException(String.format(OBJ003, lPropname, lBeanClassName));
                        }
                        lWriter.invoke(lBean, lProp);
                        break;
                    }
                }

                if(!lFoundWriter)
                {
                    throw new SerializerException(String.format(OBJ003, lPropname, lBeanClassName));
                }
            }

            return lBean;
        }
        catch (ClassNotFoundException e)
        {
            throw new SerializerException(String.format(OBJ004, lBeanClassName), e);
        }
        catch (IllegalAccessException e)
        {
            throw new SerializerException(String.format(OBJ005, lBeanClassName), e);
        }
        catch (InstantiationException e)
        {
            throw new SerializerException(String.format(OBJ006, lBeanClassName), e);
        }
        catch (IntrospectionException e)
        {
            throw new SerializerException(String.format(OBJ007, lBeanClassName), e);
        }
        catch (InvocationTargetException e)
        {
            throw new SerializerException(String.format(OBJ008, lBeanClassName), e);
        }
    }

    public Class<?> getHelpedClass()
    {
        return Object.class;
    }
}
