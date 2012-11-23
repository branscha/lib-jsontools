/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
/*Dec 4, 2006 Danny
*/
package com.sdicons.json.mapper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import com.sdicons.json.mapper.helper.impl.DateMapper;

public class TestIso8601Date {
    
	private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
	
    @Test
	public void testIt()
    throws MapperException {
		
		//test date from java to json
		Date date1=new Date();
		String  string=DateMapper.toRFC3339(date1);
		System.out.println(string);
		string=DateMapper.toRFC3339(date1,false);
		System.out.println(string);
		
		//test date from json to java		
        Date date2;
        date2 = DateMapper.fromISO8601(string, false);
        Assert.assertEquals(date1, date2);
        
        string = "2006-12-05";
        date2 = DateMapper.fromISO8601(string);
        Assert.assertEquals("2006-12-05T00:00:00.000+0100", df.format(date2));
        
        string = "2006-12-05 12:23";
        date2 = DateMapper.fromISO8601(string);
        Assert.assertEquals("2006-12-05T12:23:00.000+0100", df.format(date2));
        
        string = "2006-12-05T12:23";
        date2 = DateMapper.fromISO8601(string);
        System.out.println(df.format(date2) + "<--" + string);
        
        string = "2006-12-05T12:23:54.667";
        date2 = DateMapper.fromISO8601(string);
        System.out.println(df.format(date2) + "<--" + string);
        
        string = "2006-12-05T12:23:54.667+08:00";
        date2 = DateMapper.fromISO8601(string, false);
        System.out.println(df.format(date2) + "<--" + string);
        
        string = "2006W02";
        date2 = DateMapper.fromISO8601(string);
        System.out.println(df.format(date2) + "<--" + string);
        
        string = "2006W021";
        date2 = DateMapper.fromISO8601(string);
        System.out.println(df.format(date2) + "<--" + string);
        
        string = "2006W027";
        date2 = DateMapper.fromISO8601(string);
        System.out.println(df.format(date2) + "<--" + string);
        
        string = "2006W022T12:23:54.667+08:00";
        date2 = DateMapper.fromISO8601(string, false);
        System.out.println(df.format(date2) + "<--" + string);
        
        string = "2006007";
        date2 = DateMapper.fromISO8601(string);
        System.out.println(df.format(date2) + "<--" + string);
        
        string = "2006014T12:23:54.667+08:00";
        date2 = DateMapper.fromISO8601(string, false);
        System.out.println(df.format(date2) + "<--" + string);
    }
}
