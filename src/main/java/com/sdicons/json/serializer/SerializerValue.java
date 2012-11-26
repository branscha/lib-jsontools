/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.serializer;

/**
 * This interface represents an object that is the result of converting a JSON structure
 * into a java structure. A dedicated interface is supplied because you cannot know
 * in advance whether the result will be a primitive type or a reference type.
 * This interface lets you investigate which kind of value is returned before you use it.
 */
public interface SerializerValue
{
    final int BOOLEAN=0;
    final int BYTE=1;
    final int SHORT=2;
    final int CHAR=3;
    final int INT=4;
    final int LONG=5;
    final int FLOAT=6;
    final int DOUBLE=7;
    final int REFERENCE=8;

    /**
     * Get the primitive boolean value.
     * @return The unmarshalled boolean value.
     * @throws SerializerException If it is not a boolean representation.
     */
    boolean getBoolean()
    throws SerializerException;

    /**
     * Get the primitive byte value.
     * @return The unmarshalled byte value.
     * @throws SerializerException  If it is not a byte representation.
     */
    byte getByte()
    throws SerializerException;

    /**
     * Get the primitive short value.
     * @return The unmarshalled short value.
     * @throws SerializerException If it is not a short representation.
     */
    short getShort()
    throws SerializerException;

    /**
     * Get the primitive char value.
     * @return The unmarshalled char value.
     * @throws SerializerException If it is not a char representation.
     */
    char getChar()
    throws SerializerException;

    /**
     * Get the primitive int value.
     * @return The unmarshalled int value.
     * @throws SerializerException If it is not an int representation.
     */
    int getInt()
    throws SerializerException;

    /**
     * Get the primitive long value.
     * @return The unmarshalled long value.
     * @throws SerializerException If it is not a long representation.
     */
    long getLong()
    throws SerializerException;

    /**
     * Get the primitive float value.
     * @return The unmarshalled float value.
     * @throws SerializerException If it is not a float representation.
     */
    float getFloat()
    throws SerializerException;

    /**
     * Get the primitive double value.
     * @return The unmarshalled primitive value.
     * @throws SerializerException If it is not a double representation.
     */
    double getDouble()
    throws SerializerException;

    /**
     * Get the reference to a Java object.
     * @return The unmarshalled reference to the Java object.
     * @throws SerializerException If it is not a reference representation.
     */
    Object getReference()
    throws SerializerException;

    /**
     * Get the type of the value so that you can access its value safely.
     * @return One of the getValues BOOLEAN, BYTE, SHORT CHAR, INT, LONG, FLOAT, DOUBLE, REFERENCE.
     */
    int getType();
}
