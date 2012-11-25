/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.serializer.helper.impl;

import com.sdicons.json.serializer.JSONSerializeException;
import com.sdicons.json.serializer.JSONSerializer;
import com.sdicons.json.serializer.helper.SerializeHelper;
import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONString;

import java.util.*;
import java.beans.*;
import java.lang.reflect.*;

public class ObjectHelper
implements SerializeHelper
{
    public void renderValue(Object aObj, JSONObject aObjectElement, JSONSerializer aMarshall, HashMap aPool)
    throws JSONSerializeException
    {
        // We will render the bean properties as the elements of a JSON object.
        final JSONObject lElements = new JSONObject();
        aObjectElement.getValue().put(JSONSerializer.RNDR_ATTR_VALUE, lElements);

        try
        {
            Class lClass = aObj.getClass();
            PropertyDescriptor[] lPropDesc = Introspector.getBeanInfo(lClass, Introspector.USE_ALL_BEANINFO).getPropertyDescriptors();
            for (PropertyDescriptor aLPropDesc : lPropDesc)
            {
                Method lReader = aLPropDesc.getReadMethod();
                Method lWriter = aLPropDesc.getWriteMethod();
                String lPropName = aLPropDesc.getName();

                // Only serialize if the property is READ-WRITE.
                if (lReader != null && lWriter != null)
                {
                    lElements.getValue().put(lPropName, aMarshall.marshalImpl(lReader.invoke(aObj), aPool));
                }
            }
        }
        catch(IntrospectionException e)
        {
            final String lMsg = "Error while introspecting JavaBean.";
            throw new JSONSerializeException(lMsg);
        }
        catch(IllegalAccessException e)
        {
            final String lMsg = "Illegal access while trying to fetch a bean property (1).";
            throw new JSONSerializeException(lMsg);
        }
        catch(InvocationTargetException e)
        {
            final String lMsg = "Illegal access while trying to fetch a bean property (2).";
            throw new JSONSerializeException(lMsg);
        }
    }

    public Object parseValue(JSONObject aObjectElement, JSONSerializer aMarshall, HashMap aPool)
    throws JSONSerializeException
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
            Class lBeanClass = Class.forName(lBeanClassName);
            Object lBean;

            lBean = lBeanClass.newInstance();
            if (lId != null) aPool.put(lId, lBean);

            JSONObject lProperties = (JSONObject) aObjectElement.get(JSONSerializer.RNDR_ATTR_VALUE);

            for(String lPropname : lProperties.getValue().keySet())
            {
                // Fetch subelement information.
                JSONObject lSubEl = (JSONObject) lProperties.get(lPropname);
                Object lProp = aMarshall.unmarshalImpl(lSubEl, aPool);

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
                            final String lMsg = "Could not find a setter for prop: " + lPropname + " in class: " + lBeanClassName;
                            throw new JSONSerializeException(lMsg);
                        }
                        lWriter.invoke(lBean, lProp);
                        break;
                    }
                }

                if(!lFoundWriter)
                {
                    final String lMsg = "Could not find a setter for prop: " + lPropname + " in class: " + lBeanClassName;
                    throw new JSONSerializeException(lMsg);
                }
            }

            return lBean;
        }
        catch (ClassNotFoundException e)
        {
            final String lMsg = "Could not find JavaBean class: " + lBeanClassName;
            throw new JSONSerializeException(lMsg);
        }
        catch (IllegalAccessException e)
        {
            final String lMsg = "IllegalAccessException while trying to instantiate bean: " + lBeanClassName;
            throw new JSONSerializeException(lMsg);
        }
        catch (InstantiationException e)
        {
            final String lMsg = "InstantiationException while trying to instantiate bean: " + lBeanClassName;
            throw new JSONSerializeException(lMsg);
        }
        catch (IntrospectionException e)
        {
            final String lMsg = "IntrospectionException while trying to fill bean: " + lBeanClassName;
            throw new JSONSerializeException(lMsg);
        }
        catch (InvocationTargetException e)
        {
            final String lMsg = "InvocationTargetException while trying to fill bean: " + lBeanClassName;
            throw new JSONSerializeException(lMsg);
        }
    }

    public Class getHelpedClass()
    {
        return Object.class;
    }
}
