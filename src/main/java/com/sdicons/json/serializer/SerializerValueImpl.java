/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.serializer;

public class SerializerValueImpl
implements SerializerValue
{

    private boolean resultBoolean;
    private byte resultByte;
    private short resultShort;
    private char resultChar;
    private int resultInt;
    private long resultLong;
    private float resultFloat;
    private double resultDouble;
    private Object resultReference;

    private int resultType;

    private SerializerValueImpl()
    {
        resultBoolean = false;
        resultByte = 0;
        resultShort = 0;
        resultChar = ' ';
        resultInt = 0;
        resultLong = 0;
        resultFloat = 0;
        resultDouble = 0.0;
        resultReference = null;
    }

    public SerializerValueImpl(boolean aBool)
    {
        this();
        resultType = BOOLEAN;
        resultBoolean = aBool;
    }

    public SerializerValueImpl(byte aByte)
    {
        this();
        resultType = BYTE;
        resultByte = aByte;
    }

    public SerializerValueImpl(char aChar)
    {
        this();
        resultType = CHAR;
        resultChar = aChar;
    }

    public SerializerValueImpl(double aDouble)
    {
        this();
        resultType = DOUBLE;
        resultDouble = aDouble;
    }

    public SerializerValueImpl(float aFloat)
    {
        this();
        resultType = FLOAT;
        resultFloat = aFloat;
    }

    public SerializerValueImpl(int aInt)
    {
        this();
        resultType = INT;
        resultInt = aInt;
    }

    public SerializerValueImpl(long aLong)
    {
        this();
        resultType = LONG;
        resultLong = aLong;
    }

    public SerializerValueImpl(short aShort)
    {
        this();
        resultType = SHORT;
        resultShort = aShort;
    }

    public SerializerValueImpl(Object aReference)
    {
        this();
        resultType = REFERENCE;
        resultReference = aReference;
    }

    public int getType()
    {
        return resultType;
    }

    public boolean getBoolean()
    throws SerializerException
    {
        if(resultType == BOOLEAN)
        {
            return resultBoolean;
        }
        else
        {
            final String lMsg = "No boolean result available.";
            throw new SerializerException(lMsg);
        }
    }

    public byte getByte()
    throws SerializerException
    {
        if(resultType == BYTE)
        {
            return resultByte;
        }
        else
        {
            final String lMsg = "No byte result available.";
            throw new SerializerException(lMsg);
        }
    }

    public short getShort()
    throws SerializerException
    {
        if(resultType == SHORT)
        {
            return resultShort;
        }
        else
        {
            final String lMsg = "No short result available.";
            throw new SerializerException(lMsg);
        }
    }

    public char getChar()
    throws SerializerException
    {
        if(resultType == CHAR)
        {
            return resultChar;
        }
        else
        {
            final String lMsg = "No char result available.";
            throw new SerializerException(lMsg);
        }
    }

    public int getInt()
    throws SerializerException
    {
        if(resultType == INT)
        {
            return resultInt;
        }
        else
        {
            String lMsg = "No int result available.";
            throw new SerializerException(lMsg);
        }
    }

    public long getLong()
    throws SerializerException
    {
        if(resultType == LONG)
        {
            return resultLong;
        }
        else
        {
            final String lMsg = "No long result available.";
            throw new SerializerException(lMsg);
        }
    }

    public float getFloat()
    throws SerializerException
    {
        if(resultType == FLOAT)
        {
            return resultFloat;
        }
        else
        {
            final String lMsg = "No float result available.";
            throw new SerializerException(lMsg);
        }
    }

    public double getDouble()
    throws SerializerException
    {
        if(resultType == DOUBLE)
        {
            return resultDouble;
        }
        else
        {
            final String lMsg = "No double result available.";
            throw new SerializerException(lMsg);
        }
    }

    public Object getReference()
    throws SerializerException
    {
        if(resultType == REFERENCE)
        {
            return resultReference;
        }
        else
        {
            final String lMsg = "No reference result available.";
            throw new SerializerException(lMsg);
        }
    }
}
