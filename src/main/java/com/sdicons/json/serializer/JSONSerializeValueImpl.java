/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.serializer;

public class JSONSerializeValueImpl
implements JSONSerializeValue
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

    private JSONSerializeValueImpl()
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

    public JSONSerializeValueImpl(boolean aBool)
    {
        this();
        resultType = BOOLEAN;
        resultBoolean = aBool;
    }

    public JSONSerializeValueImpl(byte aByte)
    {
        this();
        resultType = BYTE;
        resultByte = aByte;
    }

    public JSONSerializeValueImpl(char aChar)
    {
        this();
        resultType = CHAR;
        resultChar = aChar;
    }

    public JSONSerializeValueImpl(double aDouble)
    {
        this();
        resultType = DOUBLE;
        resultDouble = aDouble;
    }

    public JSONSerializeValueImpl(float aFloat)
    {
        this();
        resultType = FLOAT;
        resultFloat = aFloat;
    }

    public JSONSerializeValueImpl(int aInt)
    {
        this();
        resultType = INT;
        resultInt = aInt;
    }

    public JSONSerializeValueImpl(long aLong)
    {
        this();
        resultType = LONG;
        resultLong = aLong;
    }

    public JSONSerializeValueImpl(short aShort)
    {
        this();
        resultType = SHORT;
        resultShort = aShort;
    }

    public JSONSerializeValueImpl(Object aReference)
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
    throws JSONSerializeException
    {
        if(resultType == BOOLEAN)
        {
            return resultBoolean;
        }
        else
        {
            final String lMsg = "No boolean result available.";
            throw new JSONSerializeException(lMsg);
        }
    }

    public byte getByte()
    throws JSONSerializeException
    {
        if(resultType == BYTE)
        {
            return resultByte;
        }
        else
        {
            final String lMsg = "No byte result available.";
            throw new JSONSerializeException(lMsg);
        }
    }

    public short getShort()
    throws JSONSerializeException
    {
        if(resultType == SHORT)
        {
            return resultShort;
        }
        else
        {
            final String lMsg = "No short result available.";
            throw new JSONSerializeException(lMsg);
        }
    }

    public char getChar()
    throws JSONSerializeException
    {
        if(resultType == CHAR)
        {
            return resultChar;
        }
        else
        {
            final String lMsg = "No char result available.";
            throw new JSONSerializeException(lMsg);
        }
    }

    public int getInt()
    throws JSONSerializeException
    {
        if(resultType == INT)
        {
            return resultInt;
        }
        else
        {
            String lMsg = "No int result available.";
            throw new JSONSerializeException(lMsg);
        }
    }

    public long getLong()
    throws JSONSerializeException
    {
        if(resultType == LONG)
        {
            return resultLong;
        }
        else
        {
            final String lMsg = "No long result available.";
            throw new JSONSerializeException(lMsg);
        }
    }

    public float getFloat()
    throws JSONSerializeException
    {
        if(resultType == FLOAT)
        {
            return resultFloat;
        }
        else
        {
            final String lMsg = "No float result available.";
            throw new JSONSerializeException(lMsg);
        }
    }

    public double getDouble()
    throws JSONSerializeException
    {
        if(resultType == DOUBLE)
        {
            return resultDouble;
        }
        else
        {
            final String lMsg = "No double result available.";
            throw new JSONSerializeException(lMsg);
        }
    }

    public Object getReference()
    throws JSONSerializeException
    {
        if(resultType == REFERENCE)
        {
            return resultReference;
        }
        else
        {
            final String lMsg = "No reference result available.";
            throw new JSONSerializeException(lMsg);
        }
    }
}
