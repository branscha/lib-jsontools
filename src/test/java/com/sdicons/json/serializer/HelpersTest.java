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

import com.sdicons.json.serializer.marshall.JSONMarshall;
import com.sdicons.json.serializer.marshall.Marshall;
import com.sdicons.json.serializer.marshall.MarshallException;
import com.sdicons.json.serializer.marshall.MarshallValue;

@SuppressWarnings({"unchecked"})
public class HelpersTest
{

    Marshall marshall;

    @Before
    public void setUp()
    throws Exception
    {
        marshall = new JSONMarshall();
    }

    @Test
    public void testByte() throws MarshallException
    {
        Byte lByte = (byte) 5;
        MarshallValue lResult = marshall.unmarshall(marshall.marshall(lByte));
        Assert.assertTrue(MarshallValue.REFERENCE == lResult.getType());
        Byte lByte2 = (Byte) lResult.getReference();
        Assert.assertTrue(lByte2.equals(lByte));
    }

    @Test
    public void testShort() throws MarshallException
    {
        Short lShort = (short) 3;
        MarshallValue lResult = marshall.unmarshall(marshall.marshall(lShort));
        Assert.assertTrue(MarshallValue.REFERENCE == lResult.getType());
        Short lShort2 = (Short) lResult.getReference();
        Assert.assertTrue(lShort2.equals(lShort));
    }

    @Test
    public void testChar() throws MarshallException
    {
        Character lchar = 'x';
        MarshallValue lResult = marshall.unmarshall(marshall.marshall(lchar));
        Assert.assertTrue(MarshallValue.REFERENCE == lResult.getType());
        Character lChar2 = (Character) lResult.getReference();
        Assert.assertTrue(lChar2.equals(lchar));
    }

    @Test
    public void testInteger() throws MarshallException
    {
        Integer lInt = 17;
        MarshallValue lResult = marshall.unmarshall(marshall.marshall(lInt));
        Assert.assertTrue(MarshallValue.REFERENCE == lResult.getType());
        Integer lInt2 = (Integer) lResult.getReference();
        Assert.assertTrue(lInt2.equals(lInt));
    }

    @Test
    public void testLong() throws MarshallException
    {
        Long lLong = (long) 21;
        MarshallValue lResult = marshall.unmarshall(marshall.marshall(lLong));
        Assert.assertTrue(MarshallValue.REFERENCE == lResult.getType());
        Long lLong2 = (Long) lResult.getReference();
        Assert.assertTrue(lLong2.equals(lLong));
    }

    @Test
    public void testFloat() throws MarshallException
    {
        Float lFloat = new Float(21.331);
        MarshallValue lResult = marshall.unmarshall(marshall.marshall(lFloat));
        Assert.assertTrue(MarshallValue.REFERENCE == lResult.getType());
        Float lFloat2 = (Float) lResult.getReference();
        Assert.assertTrue(lFloat2.equals(lFloat));
    }

    @Test
    public void testDouble() throws MarshallException
    {
        Double lDouble = 21.156331;
        MarshallValue lResult = marshall.unmarshall(marshall.marshall(lDouble));
        Assert.assertTrue(MarshallValue.REFERENCE == lResult.getType());
        Double lDouble2 = (Double) lResult.getReference();
        Assert.assertTrue(lDouble2.equals(lDouble));
    }

    @Test
    public void testBigDecimal() throws MarshallException
    {
        BigDecimal lDecimal = new BigDecimal(21.331);
        MarshallValue lResult = marshall.unmarshall(marshall.marshall(lDecimal));
        Assert.assertTrue(MarshallValue.REFERENCE == lResult.getType());
        BigDecimal lDecimal2 = (BigDecimal) lResult.getReference();
        Assert.assertTrue(lDecimal2.equals(lDecimal));
    }

    @Test
    public void testBigInteger() throws MarshallException
    {
        BigInteger lBigInt = new BigInteger("123456789123456789123456789123456789");
        MarshallValue lResult = marshall.unmarshall(marshall.marshall(lBigInt));
        Assert.assertTrue(MarshallValue.REFERENCE == lResult.getType());
        BigInteger lBigInt2 = (BigInteger) lResult.getReference();
        Assert.assertTrue(lBigInt2.equals(lBigInt));
    }

    @Test
    public void testDate() throws MarshallException
    {
        final Date lDate = new Date();
        MarshallValue lResult = marshall.unmarshall(marshall.marshall(lDate));
        Assert.assertTrue(MarshallValue.REFERENCE == lResult.getType());
        final Date lDate2 = (Date) lResult.getReference();
        Assert.assertTrue(lDate2.getTime() == lDate.getTime());
    }

    @Test
    public void testCollections() throws MarshallException
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
            
            MarshallValue lResult = marshall.unmarshall(marshall.marshall(lLinkedList));
            Assert.assertTrue(MarshallValue.REFERENCE == lResult.getType());
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
            
            MarshallValue lResult = marshall.unmarshall(marshall.marshall(lArrayList));
            Assert.assertTrue(MarshallValue.REFERENCE == lResult.getType());
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
            
            MarshallValue lResult = marshall.unmarshall(marshall.marshall(lTreeSet));
            Assert.assertTrue(MarshallValue.REFERENCE == lResult.getType());
            final Set lTreeSet2 = (TreeSet) lResult.getReference();
            Assert.assertTrue(lTreeSet.size() == lTreeSet2.size());
        }
    }

    @Test
    public void testMaps() throws MarshallException
    {
        {
            // HashMap.
            // //////////////
            final Map lHashMap = new HashMap();
            lHashMap.put("uno", "one");
            lHashMap.put("duo", "two");
            lHashMap.put("tres", "three");
            
            MarshallValue lResult = marshall.unmarshall(marshall.marshall(lHashMap));
            Assert.assertTrue(MarshallValue.REFERENCE == lResult.getType());
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
            
            MarshallValue lResult = marshall.unmarshall(marshall.marshall(lTreeMap));
            Assert.assertTrue(MarshallValue.REFERENCE == lResult.getType());
            final Map lTreeMap2 = (TreeMap) lResult.getReference();
            Assert.assertTrue(lTreeMap.size() == lTreeMap2.size());
        }
    }

    @Test
    public void testColors() throws MarshallException
    {
        {
            final Color lColor = Color.WHITE;
            MarshallValue lResult = marshall.unmarshall(marshall.marshall(lColor));
            Assert.assertTrue(lResult.getReference().equals(lColor));
        }
        
        {
            final Color lColor = Color.RED;
            MarshallValue lResult = marshall.unmarshall(marshall.marshall(lColor));
            Assert.assertTrue(lResult.getReference().equals(lColor));
        }
        
        {
            final Color lColor = Color.GREEN;
            MarshallValue lResult = marshall.unmarshall(marshall.marshall(lColor));
            Assert.assertTrue(lResult.getReference().equals(lColor));
        }
        
        {
            final Color lColor = Color.BLUE;
            MarshallValue lResult = marshall.unmarshall(marshall.marshall(lColor));
            Assert.assertTrue(lResult.getReference().equals(lColor));
        }
    }

    @Test
    public void testFonts() throws MarshallException
    {
        final Font lFont = new JPanel().getFont();
        MarshallValue lResult = marshall.unmarshall(marshall.marshall(lFont));
        Assert.assertTrue(lResult.getReference().equals(lFont));
    }

    static enum SimpsonEnum {HOMER, MARGE, LISA, BART, MAGGY}

    @Test
    public void testEnums() throws MarshallException
    {
        for (SimpsonEnum lSimpson : SimpsonEnum.values()) {
            MarshallValue lResult = marshall.unmarshall(marshall.marshall(lSimpson));
            Assert.assertTrue(lResult.getReference() == lSimpson);
        }
    }
}
