package com.example.administrator.cnmar.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

import java.security.MessageDigest;

/**
 * Created by Administrator on 2016/9/6.
 */
public class UniversalHelper {
    public static void backToLastActivity(final Context context, LinearLayout leftArrow, final Activity lastActivity){
        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,lastActivity.getClass());
                context.startActivity(intent);
            }
        });
    }
    public static String getTokenUrl(String url) {
        String token = url.replaceAll(UrlHelper.URL_BASE, "");
        String strUrl="";
        if (token.indexOf("/") == 0) {
            token = token.substring(1, token.length());
        }
        if (token.indexOf("/") != -1) {
            token = token.substring(0, token.indexOf("/"));
        }
        if (token.indexOf("?") != -1) {
            token = token.substring(0, token.indexOf("?"));
        }
        String param = url.indexOf("?") == -1 ? "?token=" : "&token=";
        try {
             strUrl=url + param + md5Encode(token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strUrl;
    }
    public static String md5Encode(String inStr) throws Exception {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }

        byte[] byteArray = inStr.getBytes("UTF-8");
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }


}
