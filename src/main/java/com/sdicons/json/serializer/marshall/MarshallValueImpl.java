/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.serializer.marshall;

public class MarshallValueImpl
implements MarshallValue
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

    private MarshallValueImpl()
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

    public MarshallValueImpl(boolean aBool)
    {
        this();
        resultType = BOOLEAN;
        resultBoolean = aBool;
    }

    public MarshallValueImpl(byte aByte)
    {
        this();
        resultType = BYTE;
        resultByte = aByte;
    }

    public MarshallValueImpl(char aChar)
    {
        this();
        resultType = CHAR;
        resultChar = aChar;
    }

    public MarshallValueImpl(double aDouble)
    {
        this();
        resultType = DOUBLE;
        resultDouble = aDouble;
    }

    public MarshallValueImpl(float aFloat)
    {
        this();
        resultType = FLOAT;
        resultFloat = aFloat;
    }

    public MarshallValueImpl(int aInt)
    {
        this();
        resultType = INT;
        resultInt = aInt;
    }

    public MarshallValueImpl(long aLong)
    {
        this();
        resultType = LONG;
        resultLong = aLong;
    }

    public MarshallValueImpl(short aShort)
    {
        this();
        resultType = SHORT;
        resultShort = aShort;
    }

    public MarshallValueImpl(Object aReference)
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
    throws MarshallException
    {
        if(resultType == BOOLEAN)
        {
            return resultBoolean;
        }
        else
        {
            final String lMsg = "No boolean result available.";
            throw new MarshallException(lMsg);
        }
    }

    public byte getByte()
    throws MarshallException
    {
        if(resultType == BYTE)
        {
            return resultByte;
        }
        else
        {
            final String lMsg = "No byte result available.";
            throw new MarshallException(lMsg);
        }
    }

    public short getShort()
    throws MarshallException
    {
        if(resultType == SHORT)
        {
            return resultShort;
        }
        else
        {
            final String lMsg = "No short result available.";
            throw new MarshallException(lMsg);
        }
    }

    public char getChar()
    throws MarshallException
    {
        if(resultType == CHAR)
        {
            return resultChar;
        }
        else
        {
            final String lMsg = "No char result available.";
            throw new MarshallException(lMsg);
        }
    }

    public int getInt()
    throws MarshallException
    {
        if(resultType == INT)
        {
            return resultInt;
        }
        else
        {
            String lMsg = "No int result available.";
            throw new MarshallException(lMsg);
        }
    }

    public long getLong()
    throws MarshallException
    {
        if(resultType == LONG)
        {
            return resultLong;
        }
        else
        {
            final String lMsg = "No long result available.";
            throw new MarshallException(lMsg);
        }
    }

    public float getFloat()
    throws MarshallException
    {
        if(resultType == FLOAT)
        {
            return resultFloat;
        }
        else
        {
            final String lMsg = "No float result available.";
            throw new MarshallException(lMsg);
        }
    }

    public double getDouble()
    throws MarshallException
    {
        if(resultType == DOUBLE)
        {
            return resultDouble;
        }
        else
        {
            final String lMsg = "No double result available.";
            throw new MarshallException(lMsg);
        }
    }

    public Object getReference()
    throws MarshallException
    {
        if(resultType == REFERENCE)
        {
            return resultReference;
        }
        else
        {
            final String lMsg = "No reference result available.";
            throw new MarshallException(lMsg);
        }
    }
}
