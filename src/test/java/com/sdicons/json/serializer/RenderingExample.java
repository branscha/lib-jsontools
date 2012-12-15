/*******************************************************************************
 * Copyright (c) 2006-2013 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.serializer;

import java.util.Date;

import com.sdicons.json.model.JSONObject;

public class RenderingExample
{
    public static void main(String[] aArgs)
    {
        try
        {

            {
                JSONSerializer lRenderer = new JSONSerializer();
                JSONObject lElement = lRenderer.marshal(1);
                System.out.println(lElement.render(true));
            }

            {
                JSONSerializer lRenderer = new JSONSerializer();
                JSONObject lElement = lRenderer.marshal(new int[]{0,1,2,3,4,5,6,7,8,9});
                 System.out.println(lElement.render(true));
            }

            {
                JSONSerializer lRenderer = new JSONSerializer();
                MyBean lMyBean = new MyBean();
                lMyBean.setId(1003);
                lMyBean.setName("This is a test...");
                lMyBean.setPtr(lMyBean);
                JSONObject lElement = lRenderer.marshal(lMyBean);
                System.out.println(lElement.render(true));
            }

            {
                JSONSerializer lRenderer = new JSONSerializer();
                JSONObject lElement = lRenderer.marshal(new Date());
                System.out.println(lElement.render(true));
            }
        }
        catch(Exception e)
        {
            e.printStackTrace(System.out);
        }
    }
}
