/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate the method in the JavaBean that generates the Object array that
 * will be used in the {@link JSONConstructor} annotated method. The two annotations
 * {@link JSONConstructor} and {@link JSONConstructorArgs} work together.
 *
 *  * @see JSONConstructor
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface JSONConstructorArgs
{
}
