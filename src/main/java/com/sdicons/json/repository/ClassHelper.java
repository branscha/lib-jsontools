/*******************************************************************************
 * Copyright (c) 2006-2013 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.repository;

/**
 * It represents some kind of functionality that is associated with a class. 
 * An example could be a converter or something similar. The idea is that when
 * you have an instance you can apply the correct functionality on that instance
 * using the class.
 */
public interface ClassHelper
{
    public Class<?> getHelpedClass();
}
