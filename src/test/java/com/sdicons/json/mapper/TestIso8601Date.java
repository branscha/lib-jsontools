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
    public void testIt() throws MapperException {

        // Test DateMapper from Java -> JSON.
        //
        Date date1 = new Date();
        String string = DateMapper.toRFC3339(date1);
        // 2012-11-23T20:37:30.217
        Assert.assertTrue(string.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}\\:\\d{2}\\:\\d{2}\\.\\d{3}"));
        //
        string = DateMapper.toRFC3339(date1, false);
        // 2012-11-23T20:39:16.584+01:00
        Assert.assertTrue(string.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}\\:\\d{2}\\:\\d{2}\\.\\d{3}\\+\\d{2}\\:\\d{2}"));

        // II. Test DateMapper from JSON -> Java.
        //
        Assert.assertEquals("2006-12-05T00:00:00.000+0100", df.format(DateMapper.fromISO8601("2006-12-05")));
        Assert.assertEquals("2006-12-05T12:23:00.000+0100", df.format(DateMapper.fromISO8601("2006-12-05 12:23")));
        Assert.assertEquals("2006-12-05T12:23:00.000+0100", df.format(DateMapper.fromISO8601("2006-12-05T12:23")));
        Assert.assertEquals("2006-12-05T12:23:54.667+0100", df.format(DateMapper.fromISO8601("2006-12-05T12:23:54.667")));
        Assert.assertEquals("2006-12-05T05:23:54.667+0100", df.format(DateMapper.fromISO8601("2006-12-05T12:23:54.667+08:00", false)));
        Assert.assertEquals("2006-01-09T00:00:00.000+0100", df.format(DateMapper.fromISO8601("2006W02")));
        Assert.assertEquals("2006-01-09T00:00:00.000+0100", df.format(DateMapper.fromISO8601("2006W021")));
        Assert.assertEquals("2006-01-22T00:00:00.000+0100", df.format(DateMapper.fromISO8601("2006W027")));
        Assert.assertEquals("2006-01-10T05:23:54.667+0100", df.format(DateMapper.fromISO8601("2006W022T12:23:54.667+08:00", false)));
        Assert.assertEquals("2006-01-07T00:00:00.000+0100", df.format(DateMapper.fromISO8601("2006007")));
        Assert.assertEquals("2006-01-14T05:23:54.667+0100", df.format(DateMapper.fromISO8601("2006014T12:23:54.667+08:00", false)));
    }
}
