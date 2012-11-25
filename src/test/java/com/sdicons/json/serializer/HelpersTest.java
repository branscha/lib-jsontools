/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.serializer;

import java.awt.Color;
import java.awt.Font;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.JPanel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


@SuppressWarnings({"unchecked"})
public class HelpersTest
{

    JSONSerializer marshall;

    @Before
    public void setUp()
    throws Exception
    {
        marshall = new JSONSerializer();
    }

    @Test
    public void testByte() throws JSONSerializeException
    {
        Byte lByte = (byte) 5;
        JSONSerializeValue lResult = marshall.unmarshal(marshall.marshal(lByte));
        Assert.assertTrue(JSONSerializeValue.REFERENCE == lResult.getType());
        Byte lByte2 = (Byte) lResult.getReference();
        Assert.assertTrue(lByte2.equals(lByte));
    }

    @Test
    public void testShort() throws JSONSerializeException
    {
        Short lShort = (short) 3;
        JSONSerializeValue lResult = marshall.unmarshal(marshall.marshal(lShort));
        Assert.assertTrue(JSONSerializeValue.REFERENCE == lResult.getType());
        Short lShort2 = (Short) lResult.getReference();
        Assert.assertTrue(lShort2.equals(lShort));
    }

    @Test
    public void testChar() throws JSONSerializeException
    {
        Character lchar = 'x';
        JSONSerializeValue lResult = marshall.unmarshal(marshall.marshal(lchar));
        Assert.assertTrue(JSONSerializeValue.REFERENCE == lResult.getType());
        Character lChar2 = (Character) lResult.getReference();
        Assert.assertTrue(lChar2.equals(lchar));
    }

    @Test
    public void testInteger() throws JSONSerializeException
    {
        Integer lInt = 17;
        JSONSerializeValue lResult = marshall.unmarshal(marshall.marshal(lInt));
        Assert.assertTrue(JSONSerializeValue.REFERENCE == lResult.getType());
        Integer lInt2 = (Integer) lResult.getReference();
        Assert.assertTrue(lInt2.equals(lInt));
    }

    @Test
    public void testLong() throws JSONSerializeException
    {
        Long lLong = (long) 21;
        JSONSerializeValue lResult = marshall.unmarshal(marshall.marshal(lLong));
        Assert.assertTrue(JSONSerializeValue.REFERENCE == lResult.getType());
        Long lLong2 = (Long) lResult.getReference();
        Assert.assertTrue(lLong2.equals(lLong));
    }

    @Test
    public void testFloat() throws JSONSerializeException
    {
        Float lFloat = new Float(21.331);
        JSONSerializeValue lResult = marshall.unmarshal(marshall.marshal(lFloat));
        Assert.assertTrue(JSONSerializeValue.REFERENCE == lResult.getType());
        Float lFloat2 = (Float) lResult.getReference();
        Assert.assertTrue(lFloat2.equals(lFloat));
    }

    @Test
    public void testDouble() throws JSONSerializeException
    {
        Double lDouble = 21.156331;
        JSONSerializeValue lResult = marshall.unmarshal(marshall.marshal(lDouble));
        Assert.assertTrue(JSONSerializeValue.REFERENCE == lResult.getType());
        Double lDouble2 = (Double) lResult.getReference();
        Assert.assertTrue(lDouble2.equals(lDouble));
    }

    @Test
    public void testBigDecimal() throws JSONSerializeException
    {
        BigDecimal lDecimal = new BigDecimal(21.331);
        JSONSerializeValue lResult = marshall.unmarshal(marshall.marshal(lDecimal));
        Assert.assertTrue(JSONSerializeValue.REFERENCE == lResult.getType());
        BigDecimal lDecimal2 = (BigDecimal) lResult.getReference();
        Assert.assertTrue(lDecimal2.equals(lDecimal));
    }

    @Test
    public void testBigInteger() throws JSONSerializeException
    {
        BigInteger lBigInt = new BigInteger("123456789123456789123456789123456789");
        JSONSerializeValue lResult = marshall.unmarshal(marshall.marshal(lBigInt));
        Assert.assertTrue(JSONSerializeValue.REFERENCE == lResult.getType());
        BigInteger lBigInt2 = (BigInteger) lResult.getReference();
        Assert.assertTrue(lBigInt2.equals(lBigInt));
    }

    @Test
    public void testDate() throws JSONSerializeException
    {
        final Date lDate = new Date();
        JSONSerializeValue lResult = marshall.unmarshal(marshall.marshal(lDate));
        Assert.assertTrue(JSONSerializeValue.REFERENCE == lResult.getType());
        final Date lDate2 = (Date) lResult.getReference();
        Assert.assertTrue(lDate2.getTime() == lDate.getTime());
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void testCollections() throws JSONSerializeException
    {
        {
            // Linked lists.
            // //////////////
            final List lLinkedList = new LinkedList();
            lLinkedList.add("uno");
            lLinkedList.add("duo");
            lLinkedList.add("tres");

            final List lNestedLinkedList = new LinkedList();
            lNestedLinkedList.add("one");
            lNestedLinkedList.add("two");
            lNestedLinkedList.add("three");
            lLinkedList.add(lNestedLinkedList);

            // This will not work! See java bug 4756334.
            // A container cannot contain itself when the hashcode is
            // calculated, this generates a
            // StackOverflow. According to Sun this behaviour is wanted.
            // lLinkedList.add(lLinkedList);

            JSONSerializeValue lResult = marshall.unmarshal(marshall.marshal(lLinkedList));
            Assert.assertTrue(JSONSerializeValue.REFERENCE == lResult.getType());
            final List lLinkedList2 = (LinkedList) lResult.getReference();
            Assert.assertTrue(lLinkedList.size() == lLinkedList2.size());
        }

        // Array lists.
        // /////////////
        {
            final List lArrayList = new ArrayList();
            lArrayList.add("uno");
            lArrayList.add("duo");
            lArrayList.add("tres");

            final List lNestedArrayList = new ArrayList();
            lNestedArrayList.add("one");
            lNestedArrayList.add("two");
            lNestedArrayList.add("three");
            lArrayList.add(lNestedArrayList);

            JSONSerializeValue lResult = marshall.unmarshal(marshall.marshal(lArrayList));
            Assert.assertTrue(JSONSerializeValue.REFERENCE == lResult.getType());
            final List lArrayList2 = (ArrayList) lResult.getReference();
            Assert.assertTrue(lArrayList.size() == lArrayList2.size());
        }

        // TreeSet.
        // /////////
        {
            final Set lTreeSet = new TreeSet();
            lTreeSet.add("uno");
            lTreeSet.add("duo");
            lTreeSet.add("tres");

            JSONSerializeValue lResult = marshall.unmarshal(marshall.marshal(lTreeSet));
            Assert.assertTrue(JSONSerializeValue.REFERENCE == lResult.getType());
            final Set lTreeSet2 = (TreeSet) lResult.getReference();
            Assert.assertTrue(lTreeSet.size() == lTreeSet2.size());
        }
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void testMaps() throws JSONSerializeException
    {
        {
            // HashMap.
            // //////////////
            final Map lHashMap = new HashMap();
            lHashMap.put("uno", "one");
            lHashMap.put("duo", "two");
            lHashMap.put("tres", "three");

            JSONSerializeValue lResult = marshall.unmarshal(marshall.marshal(lHashMap));
            Assert.assertTrue(JSONSerializeValue.REFERENCE == lResult.getType());
            final Map lHashMap2 = (HashMap) lResult.getReference();
            Assert.assertTrue(lHashMap.size() == lHashMap2.size());
        }

        {
            // TreeMap.
            // //////////////
            final Map lTreeMap = new TreeMap();
            lTreeMap.put("uno", "one");
            lTreeMap.put("duo", "two");
            lTreeMap.put("tres", "three");

            JSONSerializeValue lResult = marshall.unmarshal(marshall.marshal(lTreeMap));
            Assert.assertTrue(JSONSerializeValue.REFERENCE == lResult.getType());
            final Map lTreeMap2 = (TreeMap) lResult.getReference();
            Assert.assertTrue(lTreeMap.size() == lTreeMap2.size());
        }
    }

    @Test
    public void testColors() throws JSONSerializeException
    {
        {
            final Color lColor = Color.WHITE;
            JSONSerializeValue lResult = marshall.unmarshal(marshall.marshal(lColor));
            Assert.assertTrue(lResult.getReference().equals(lColor));
        }

        {
            final Color lColor = Color.RED;
            JSONSerializeValue lResult = marshall.unmarshal(marshall.marshal(lColor));
            Assert.assertTrue(lResult.getReference().equals(lColor));
        }

        {
            final Color lColor = Color.GREEN;
            JSONSerializeValue lResult = marshall.unmarshal(marshall.marshal(lColor));
            Assert.assertTrue(lResult.getReference().equals(lColor));
        }

        {
            final Color lColor = Color.BLUE;
            JSONSerializeValue lResult = marshall.unmarshal(marshall.marshal(lColor));
            Assert.assertTrue(lResult.getReference().equals(lColor));
        }
    }

    @Test
    public void testFonts() throws JSONSerializeException
    {
        final Font lFont = new JPanel().getFont();
        JSONSerializeValue lResult = marshall.unmarshal(marshall.marshal(lFont));
        Assert.assertTrue(lResult.getReference().equals(lFont));
    }

    static enum SimpsonEnum {HOMER, MARGE, LISA, BART, MAGGY}

    @Test
    public void testEnums() throws JSONSerializeException
    {
        for (SimpsonEnum lSimpson : SimpsonEnum.values()) {
            JSONSerializeValue lResult = marshall.unmarshal(marshall.marshal(lSimpson));
            Assert.assertTrue(lResult.getReference() == lSimpson);
        }
    }
}
