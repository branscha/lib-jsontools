/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.serializer;

import com.sdicons.json.model.JSONObject;
import com.sdicons.json.serializer.marshall.Marshall;
import com.sdicons.json.serializer.marshall.JSONMarshall;

import java.util.Date;

public class RenderingExample
{
    public static void main(String[] aArgs)
    {
        try
        {

            {
                Marshall lRenderer = new JSONMarshall();
                JSONObject lElement = lRenderer.marshall(1);
                System.out.println(lElement.render(true));
            }

            {
                Marshall lRenderer = new JSONMarshall();
                JSONObject lElement = lRenderer.marshall(new int[]{0,1,2,3,4,5,6,7,8,9});
                 System.out.println(lElement.render(true));
            }

            {
                Marshall lRenderer = new JSONMarshall();
                MyBean lMyBean = new MyBean();
                lMyBean.setId(1003);
                lMyBean.setName("This is a test...");
                lMyBean.setPtr(lMyBean);
                JSONObject lElement = lRenderer.marshall(lMyBean);
                 System.out.println(lElement.render(true));
            }

            {
                Marshall lRenderer = new JSONMarshall();
                JSONObject lElement = lRenderer.marshall(new Date());
                System.out.println(lElement.render(true));
            }
        }
        catch(Exception e)
        {
            e.printStackTrace(System.out);
        }
    }
}
