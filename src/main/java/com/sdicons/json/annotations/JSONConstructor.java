/*******************************************************************************
 * Copyright (c) 2006-2013 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate the constructor of the JavaBean that has to be used by the mapper
 * or the serializer to instantiate the bean. The argument list for the constructor
 * has to be generated by the {@link JSONConstructorArgs} annotated method. The two annotations
 * {@link JSONConstructor} and {@link JSONConstructorArgs} work together.
 *
 * @see JSONConstructorArgs
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.CONSTRUCTOR)
public @interface JSONConstructor
{
}