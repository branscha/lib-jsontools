/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.mapper;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsMapContaining.hasKey;

import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.sdicons.json.helper.JSONConstruct;
import com.sdicons.json.helper.JSONMap;
import com.sdicons.json.mapper.helper.impl.DateMapper;
import com.sdicons.json.model.JSONArray;
import com.sdicons.json.model.JSONValue;
import com.sdicons.json.parser.JSONParser;
import com.sdicons.json.parser.JSONParserException;

public class MapperTest {
    public static enum TheSimpsons {
        HOMER, BART, LISA, MARGE, MAGGY
    };

    public static class TestBean {
        private Boolean booleanMbr;
        private String stringMbr;
        private Integer integerMbr;
        private int intMbr;
        private Short shortMbr;
        private Byte byteMbr;
        private Long longMbr;
        private Float floatMbr;
        private Double doubleMbr;
        private BigInteger bigIntMbr;
        private BigDecimal bigDecimalMbr;
        private Character charMbr;
        private Date date;
        private Boolean true1;
        private Boolean true2;
        private Boolean true3;
        private Boolean false1;
        private Boolean false2;
        private Boolean false3;
        private String onlyReadableProperty = "read me";
        @SuppressWarnings("unused")
        private String onlyWritableProperty;
        private LinkedList<String> linkedList;
        private ArrayList<Date> arrayList;
        private TheSimpsons simpson;

        private LinkedHashMap<String, Date> linkedMap;

        public Integer getIntegerMbr() {
            return integerMbr;
        }

        public void setIntegerMbr(Integer integerMbr) {
            this.integerMbr = integerMbr;
        }

        public void setOnlyWritableProperty(String onlyWritableProperty) {
            this.onlyWritableProperty = onlyWritableProperty;
        }

        public String getOnlyReadableProperty() {
            return onlyReadableProperty;
        }

        public Short getShortMbr() {
            return shortMbr;
        }

        public void setShortMbr(Short shortMbr) {
            this.shortMbr = shortMbr;
        }

        public String getStringMbr() {
            return stringMbr;
        }

        public void setStringMbr(String stringMbr) {
            this.stringMbr = stringMbr;
        }

        public Boolean getBooleanMbr() {
            return booleanMbr;
        }

        public void setBooleanMbr(Boolean booleanMbr) {
            this.booleanMbr = booleanMbr;
        }

        public Byte getByteMbr() {
            return byteMbr;
        }

        public void setByteMbr(Byte byteMbr) {
            this.byteMbr = byteMbr;
        }

        public Long getLongMbr() {
            return longMbr;
        }

        public void setLongMbr(Long longMbr) {
            this.longMbr = longMbr;
        }

        public Double getDoubleMbr() {
            return doubleMbr;
        }

        public void setDoubleMbr(Double doubleMbr) {
            this.doubleMbr = doubleMbr;
        }

        public Float getFloatMbr() {
            return floatMbr;
        }

        public void setFloatMbr(Float floatMbr) {
            this.floatMbr = floatMbr;
        }

        public BigDecimal getBigDecimalMbr() {
            return bigDecimalMbr;
        }

        public void setBigDecimalMbr(BigDecimal bigDecimalMbr) {
            this.bigDecimalMbr = bigDecimalMbr;
        }

        public BigInteger getBigIntMbr() {
            return bigIntMbr;
        }

        public void setBigIntMbr(BigInteger bigIntMbr) {
            this.bigIntMbr = bigIntMbr;
        }

        public Character getCharMbr() {
            return charMbr;
        }

        public void setCharMbr(Character charMbr) {
            this.charMbr = charMbr;
        }

        public int getIntMbr() {
            return intMbr;
        }

        public void setIntMbr(int intMbr) {
            this.intMbr = intMbr;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public ArrayList<Date> getArrayList() {
            return arrayList;
        }

        public void setArrayList(ArrayList<Date> lArrayList) {
            this.arrayList = lArrayList;
        }

        public LinkedList<String> getLinkedList() {
            return linkedList;
        }

        public void setLinkedList(LinkedList<String> lLinkedList) {
            this.linkedList = lLinkedList;
        }

        public LinkedHashMap<String, Date> getLinkedMap() {
            return linkedMap;
        }

        public void setLinkedMap(LinkedHashMap<String, Date> linkedMap) {
            this.linkedMap = linkedMap;
        }

        public Boolean getFalse1() {
            return false1;
        }

        public void setFalse1(Boolean false1) {
            this.false1 = false1;
        }

        public Boolean getFalse2() {
            return false2;
        }

        public void setFalse2(Boolean false2) {
            this.false2 = false2;
        }

        public Boolean getFalse3() {
            return false3;
        }

        public void setFalse3(Boolean false3) {
            this.false3 = false3;
        }

        public Boolean getTrue1() {
            return true1;
        }

        public void setTrue1(Boolean true1) {
            this.true1 = true1;
        }

        public Boolean getTrue2() {
            return true2;
        }

        public void setTrue2(Boolean true2) {
            this.true2 = true2;
        }

        public Boolean getTrue3() {
            return true3;
        }

        public void setTrue3(Boolean true3) {
            this.true3 = true3;
        }

        public TheSimpsons getSimpson() {
            return simpson;
        }

        public void setSimpson(TheSimpsons simpson) {
            this.simpson = simpson;
        }
    }

    public static class MyDate {
        private Date theDate;
        private String theTimeZone;

        @JSONConstruct
        public MyDate(long aTime, String aTimeZone) {
            theDate = new Date(aTime);
            theTimeZone = aTimeZone;
        }

        @JSONMap
        public Object[] getTime() {
            return new Object[] { theDate.getTime(), theTimeZone };
        }
    }

    public static class MyPojo {
        private String firstName;
        private String lastName;

        public void setNames(String aFirst, String aLast) {
            firstName = aFirst;
            lastName = aLast;
        }

        public String toString() {
            return "MyPojo{" + "firstName='" + firstName + '\'' + ", lastName='" + lastName + '\'' + '}';
        }
    }
    
    private JSONMapper mapper;
    
    @Before
    public void setup() {
        mapper = new JSONMapper();
    }
    

    @Test
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void testIt() throws JSONParserException, MapperException {
        MapperTest.TestBean lDuupje = new MapperTest.TestBean();
        //
        lDuupje.setIntegerMbr(new Integer(13));
        lDuupje.setIntMbr(17);
        lDuupje.setShortMbr(new Short((short) 17));
        lDuupje.setStringMbr("It is not fair!");
        lDuupje.setBooleanMbr(true);
        lDuupje.setByteMbr((byte) 32);
        lDuupje.setLongMbr(12345l);
        lDuupje.setFloatMbr(123.12f);
        lDuupje.setDoubleMbr(987.89);
        lDuupje.setBigIntMbr(new BigInteger("123654789555"));
        lDuupje.setBigDecimalMbr(new BigDecimal("1111111465465.676476545"));
        lDuupje.setCharMbr('A');
        lDuupje.setDate(new Date());
        Boolean trueBoolean = new Boolean(true);
        Boolean falseBoolean = new Boolean(false);
        lDuupje.setTrue1(trueBoolean);
        lDuupje.setTrue2(trueBoolean);
        lDuupje.setTrue3(trueBoolean);
        lDuupje.setFalse1(falseBoolean);
        lDuupje.setFalse2(falseBoolean);
        lDuupje.setFalse3(falseBoolean);
        lDuupje.setSimpson(TheSimpsons.HOMER);
        //
        LinkedList lLinkedList = new LinkedList();
        lLinkedList.add("uno");
        lLinkedList.add("duo");
        lDuupje.setLinkedList(lLinkedList);
        //
        ArrayList<Date> lArrayList = new ArrayList<Date>();
        lArrayList.add(new Date());
        lArrayList.add(new Date());
        lDuupje.setArrayList(lArrayList);
        //
        LinkedHashMap<String, Date> lMap = new LinkedHashMap<String, Date>();
        lMap.put("uno", new Date());
        lMap.put("duo", new Date());
        lDuupje.setLinkedMap(lMap);
        //
        JSONValue lObj = mapper.toJSON(lDuupje);
        String toJS = lObj.render(true);
        Assert.assertNotNull(toJS);
        //
        String fromJS = toJS.replaceAll("onlyReadableProperty", "onlyWritableProperty");
        fromJS = fromJS.replaceAll("read me", "changed me");
        Reader stringReader = new StringReader(fromJS);
        JSONParser jsonParser = new JSONParser(stringReader, fromJS);
        lObj = jsonParser.nextValue();
        
        // JSON -> Java.
        MapperTest.TestBean lLitmus = (MapperTest.TestBean) mapper.toJava(lObj, TestBean.class);
        Assert.assertNotNull(lLitmus);
//        Assert.assertEquals("changed me",lLitmus.onlyWritableProperty);
    }

    public static class Graph {
        private HashMap<String, ArrayList<Integer>> nodes;
        @SuppressWarnings("rawtypes")
        private ArrayList edges;
        private Collection<HashMap<String, String>> col;

        @SuppressWarnings("rawtypes")
        public ArrayList getEdges() {
            return edges;
        }

        public void setEdges(@SuppressWarnings("rawtypes") ArrayList edges) {
            this.edges = edges;
        }

        public HashMap<String, ArrayList<Integer>> getNodes() {
            return nodes;
        }

        public void setNodes(HashMap<String, ArrayList<Integer>> nodes) {
            this.nodes = nodes;
        }

        public Collection<HashMap<String, String>> getCol() {
            return col;
        }

        public void setCol(Collection<HashMap<String, String>> col) {
            this.col = col;
        }
    }

    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void mapGraph() throws MapperException {
        HashMap<String, ArrayList<Integer>> nodes = new HashMap<String, ArrayList<Integer>>();
        //
        ArrayList<Integer> nodeInfo = new ArrayList<Integer>();
        nodeInfo.add(new Integer(270));
        nodeInfo.add(new Integer(360));
        nodes.put("uniqueNodeId1", nodeInfo);
        //
        Collection<HashMap<String, String>> collection = new Vector<HashMap<String, String>>();
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("index1", "value1");
        collection.add(hashMap);
        //
        Graph graph = new Graph();
        graph.setNodes(nodes);
        graph.setEdges(new ArrayList());
        graph.setCol(collection);
        
        
        // Map Java -> JSON
        JSONValue lObj = mapper.toJSON(graph);
        Assert.assertNotNull(lObj);
        Assert.assertNotNull(lObj.render(true));

        // JSON -> Java.
        Object javaObj  = mapper.toJava(lObj, graph.getClass());
        Assert.assertThat(javaObj, is(instanceOf(Graph.class)));
        Graph graph2 = (Graph) javaObj;
        //
        HashMap<String, ArrayList<Integer>> nodes2 = graph2.getNodes();
        Assert.assertThat(nodes2, hasKey("uniqueNodeId1"));
        
        //
        ArrayList<Integer> nodeInfo2 = nodes2.get("uniqueNodeId1");
        Iterator<Integer> iterator = nodeInfo2.iterator();
        while (iterator.hasNext()) {
            Assert.assertNotNull(iterator.next());
        }
        //
        Assert.assertNotNull(graph2.getCol());
        Collection<?> col2 = graph2.getCol();
        Assert.assertThat(col2, hasSize(1));
        Map<String, ?> map2 = (Map) ((List) col2).get(0);
        Assert.assertThat(map2, hasKey("index1"));
    }

    @Test
    public void mapArray() throws MapperException {
        // Map an array to JSON.
        //
        String[] orig = { "abc", "bcd", "def" };
        JSONValue json = mapper.toJSON(orig);
        Assert.assertNotNull(json);
        // Map JSON to an object.
        //
        Object unmapped = mapper.toJava(json, orig.getClass());
        Assert.assertThat(unmapped, is(instanceOf(String[].class)));
        Assert.assertArrayEquals(orig, (String[]) unmapped);
    }

    public static class TestBean4 {
        private String myString;

        public TestBean4() {
            this.myString = "";
        }

        public TestBean4(String myString) {
            super();
            this.myString = myString;
        }

        public String getMyString() {
            return myString;
        }

        public void setMyString(String myString) {
            this.myString = myString;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((myString == null) ? 0 : myString.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (getClass() != obj.getClass()) return false;
            TestBean4 other = (TestBean4) obj;
            if (myString == null) {
                if (other.myString != null) return false;
            } else if (!myString.equals(other.myString))
                return false;
            return true;
        }
    }

    @Test
    public void mapArrayOfPojos() throws MapperException {
        // Map a POJO array  to JSON.
        //
        TestBean4[] bean4s = { new TestBean4("abc"), new TestBean4("bcd"), new TestBean4("def") };
        JSONValue lObj = mapper.toJSON(bean4s);
        // Unmap from JSON to Java.
        //
        Object javaObj = mapper.toJava(lObj, bean4s.getClass());
        TestBean4[] beans = (TestBean4[]) javaObj;
        Assert.assertArrayEquals(bean4s, beans);
    }

    @Test
    public void mapArrayOfArrays() throws MapperException {
        // Map an array of arrays Java -> JSON.
        //
        String[][] strings = { { "abc", "bcd", "def" }, { "abc", "bcd", "def" } };
        //
        JSONValue lObj = mapper.toJSON(strings);
        Assert.assertThat(lObj, is(instanceOf(JSONArray.class)));
        //
        // Map JSON -> Java.
        Object javaObj = mapper.toJava(lObj, strings.getClass());
        Assert.assertThat(javaObj, is(instanceOf(String[][].class)));
        String[][] strings2 = (String[][]) javaObj;
        Assert.assertArrayEquals(strings, strings2);
    }

    public static class SetAndListBean {
        private Set<String> stringSet;
        private List<String> stringList;

        public List<String> getStringList() {
            return stringList;
        }

        public void setStringList(List<String> stringList) {
            this.stringList = stringList;
        }

        public Set<String> getStringSet() {
            return stringSet;
        }

        public void setStringSet(Set<String> stringSet) {
            this.stringSet = stringSet;
        }
    }

    @Test
    public void mapSetAndList() throws MapperException {
        SetAndListBean setAndListBean = new SetAndListBean();
        Set<String> stringSet = new HashSet<String>();
        stringSet.add("abc");
        stringSet.add("bcd");
        List<String> stringList = new Vector<String>();
        stringList.add("abc");
        stringList.add("bcd");
        setAndListBean.setStringSet(stringSet);
        setAndListBean.setStringList(stringList);

        JSONValue jsonValue = mapper.toJSON(setAndListBean);
        Assert.assertNotNull(jsonValue.render(true));
        
        Object object = mapper.toJava(jsonValue, setAndListBean.getClass());
        SetAndListBean setAndListBean2 = (SetAndListBean) object;
        
        Iterator<String> iterator = setAndListBean2.getStringList().iterator();

        Assert.assertNotNull(iterator.next());
        Assert.assertNotNull(iterator.next());

        iterator = setAndListBean2.getStringSet().iterator();
        
        Assert.assertNotNull(iterator.next());
        Assert.assertNotNull(iterator.next());
    }

    @Test
    public void testDateMapper() throws MapperException {
        // By Default,DateMapper will ignore the timezone.
        // it's convenient for me,and maybe others.
        Date date1 = new Date();
        JSONValue lObj = mapper.toJSON(date1);
        Assert.assertNotNull(lObj.render(true));
        //
        Object javaObj = mapper.toJava(lObj, date1.getClass());
        Date date2 = (Date) javaObj;
        Assert.assertEquals(date1, date2);
        //
        DateMapper.setTimeZoneIgnored(false);
        date1 = new Date();
        lObj = mapper.toJSON(date1);
        Assert.assertNotNull(lObj.render(true));
        //
        javaObj = mapper.toJava(lObj, date1.getClass());
        date2 = (Date) javaObj;
        Assert.assertEquals(date1, date2);
    }

    @Test
    public void testAnnotatedMapper() throws MapperException {
        // Map fields, not properties.
        mapper.usePojoAccess();
        MyDate lMyDate = new MyDate(new Date().getTime(), "CEST");
        JSONValue lObj = mapper.toJSON(lMyDate);
        Assert.assertNotNull(lObj.render(true));
    }

    @Test
    public void mapPojoFields() throws MapperException {
        // Map fields, not properties.
        mapper.usePojoAccess();
        MyPojo lPojo = new MyPojo();
        lPojo.setNames("Homer", "Simpson");
        
        // Java -> JSON.
        JSONValue lObj = mapper.toJSON(lPojo);
        Assert.assertEquals("{\n" +
                "   \"firstName\" : \"Homer\",\n" +
                "   \"lastName\" : \"Simpson\"\n" +
                "}", lObj.render(true));
        
        // JSON -> Java
        Object javaObj = mapper.toJava(lObj, MyPojo.class);
        Assert.assertNotNull(javaObj);
        Assert.assertThat(javaObj, is(instanceOf(MyPojo.class)));
        Assert.assertEquals("MyPojo{firstName='Homer', lastName='Simpson'}", javaObj.toString());
    }
}
