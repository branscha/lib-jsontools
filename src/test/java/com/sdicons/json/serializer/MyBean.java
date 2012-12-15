/*******************************************************************************
 * Copyright (c) 2006-2013 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.serializer;

public class MyBean
{
    private int id;
    private String name;
    private MyBean ptr;

    private Integer int1, int2;

    public int getId()
    {
        return id;
    }

    public void setId(int aId)
    {
        id = aId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String aName)
    {
        name = aName;
    }

    public MyBean getPtr()
    {
        return ptr;
    }

    public void setPtr(MyBean aPtr)
    {
        ptr = aPtr;
    }

    public Integer getInt1()
    {
        return int1;
    }

    public void setInt1(Integer aInt1)
    {
        int1 = aInt1;
    }

    public Integer getInt2()
    {
        return int2;
    }

    public void setInt2(Integer aInt2)
    {
        int2 = aInt2;
    }
}
