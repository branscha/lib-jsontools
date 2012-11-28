/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.mapper.helper.impl;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.sdicons.json.mapper.JSONMapper;
import com.sdicons.json.mapper.MapperException;
import com.sdicons.json.mapper.helper.MapperHelper;
import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONValue;

public class ObjectMapperProps
implements MapperHelper
{
    private static final String OBJ001 = "JSONMapper/ObjectMapperProps/001: JSON->Java. ObjectMapper cannot map JSON class '%s' to a JavaBean.";
    private static final String OBJ002 = "JSONMapper/ObjectMapperProps/002: JSON->Java. Could not find a setter for property '%s' in class '%s'.";
    private static final String OBJ003 = "JSONMapper/ObjectMapperProps/003: JSON->Java. IllegalAccessException while trying to instantiate class '%s'.";
    private static final String OBJ004 = "JSONMapper/ObjectMapperProps/004: JSON->Java. InstantiationException while trying to instantiate class '%s'.";
    private static final String OBJ005 = "JSONMapper/ObjectMapperProps/005: JSON->Java. IntrospectionException while trying to fill class '%s'.";
    private static final String OBJ006 = "JSONMapper/ObjectMapperProps/006: JSON->Java. InvocationTargetException while trying to fill class '%s'.";
    private static final String OBJ007 = "JSONMapper/ObjectMapperProps/007: Java->JSON. Error while introspecting JavaBean with class '%s'.";
    private static final String OBJ008 = "JSONMapper/ObjectMapperProps/008: Java->JSON. Illegal access while trying to fetch a bean property named '%s' on bean '%s'.";
    private static final String OBJ009 = "JSONMapper/ObjectMapperProps/009: Java->JSON. Illegal access while trying to fetch a bean property '%s' on bean '%s'.";

    public Class<?> getHelpedClass()
    {
        return Object.class;
    }

    public Object toJava(JSONMapper mapper, JSONValue aValue, Class<?> aRequestedClass)
    throws MapperException
    {
        if(!aValue.isObject()) throw new MapperException(String.format(OBJ001, aValue.getClass().getName()));
        JSONObject aObject = (JSONObject) aValue;

        try
        {
            final Object lBean = aRequestedClass.newInstance();

            for (String lPropname : aObject.getValue().keySet())
            {
                // Fetch subelement information.
                final JSONValue lSubEl = aObject.get(lPropname);

                // Put the property in the bean.
                boolean lFoundWriter = false;
                PropertyDescriptor[] lPropDesc = Introspector.getBeanInfo(aRequestedClass, Introspector.USE_ALL_BEANINFO).getPropertyDescriptors();
                for (PropertyDescriptor aLPropDesc : lPropDesc)
                {
                    if (aLPropDesc.getName().equals(lPropname))
                    {
                        lFoundWriter = true;
                        final Method lWriter = aLPropDesc.getWriteMethod();
                        if (lWriter == null)
                        {
                        	//Ignore the case of no setter
                            // final String lMsg = "Could not find a setter for prop: " + lPropname + " in class: " + aRequestedClass;
                            // System.out.println("WARNING:"+lMsg);
                            break;
                            //throw new MapperException(lMsg);
                        }
                        else
                        {
                            Object lProp;
                            Type[] lTypes = lWriter.getGenericParameterTypes();
                            if (lTypes.length == 1 && (lTypes[0] instanceof ParameterizedType))
                            {
                                // We can make use of the extra type information of the parameter of the
                                // seter. This extra type information can be exploited by the mapper
                                // to produce a more fine grained mapping.
                                lProp = mapper.toJava(lSubEl, (ParameterizedType) lTypes[0]);
                            }
                            else
                            {
                                // We cannot use extra type information, we fall back on the
                                // raw class information.
                                lProp = mapper.toJava(lSubEl, aLPropDesc.getPropertyType());
                            }

                            lWriter.invoke(lBean, lProp);
                        }
                        break;
                    }
                }

                if (!lFoundWriter)
                {
                    throw new MapperException(String.format(OBJ002, lPropname, aRequestedClass.getName()));
                }
            }
            return lBean;
        }
        catch (IllegalAccessException e)
        {
            throw new MapperException(String.format(OBJ003, aRequestedClass.getName()), e);
        }
        catch (InstantiationException e)
        {
            throw new MapperException(String.format(OBJ004, aRequestedClass.getName()), e);
        }
        catch (IntrospectionException e)
        {
            throw new MapperException(String.format(OBJ005, aRequestedClass.getName()), e);
        }
        catch (InvocationTargetException e)
        {
            throw new MapperException(String.format(OBJ006, aRequestedClass.getName()), e);
        }
    }

    public JSONValue toJSON(JSONMapper mapper, Object aPojo)
    throws MapperException
    {
         // We will render the bean properties as the elements of a JSON object.
        final JSONObject lElements = new JSONObject();
        String lPropName="";
        try
        {
            Class<?> lClass = aPojo.getClass();
            PropertyDescriptor[] lPropDesc = Introspector.getBeanInfo(lClass, Introspector.USE_ALL_BEANINFO).getPropertyDescriptors();
            for (PropertyDescriptor aLPropDesc : lPropDesc)
            {
                final Method lReader = aLPropDesc.getReadMethod();
                lPropName = aLPropDesc.getName();
                // Only serialize if the property is READABLE
                // ignore the properties created by a proxy from Hibernate
                if(lReader!=null&&
                		(lReader.getReturnType().toString().contains("net.sf.cglib.proxy.Callback")||
                		 lReader.getReturnType().toString().contains("org.hibernate.proxy.LazyInitializer"))){
                	continue;
                }
                // Ignore the getClass() for any objects
                if(lReader!=null&&lPropName.equals("class")){
                	continue;
                }

                if (lReader != null)
                {
                    lElements.getValue().put(lPropName, mapper.toJSON(lReader.invoke(aPojo)));
                }
            }

            return lElements;
        }
        catch(IntrospectionException e)
        {
            throw new MapperException(String.format(OBJ007, aPojo.getClass().getName()), e);
        }
        catch(IllegalAccessException e)
        {
            throw new MapperException(String.format(OBJ008, lPropName, aPojo.toString()), e);
        }
        catch(InvocationTargetException e)
        {
            throw new MapperException(String.format(OBJ009, lPropName, aPojo.toString()), e);
        }
    }
}
