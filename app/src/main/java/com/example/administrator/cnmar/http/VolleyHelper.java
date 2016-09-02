package com.example.administrator.cnmar.http;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * Created by Administrator on 2016/9/1.
 */
public class VolleyHelper {
    public static final String LOGIN_URL = "http://139.196.104.170:8092/login_commit?username=admin&password=1";

    public static void  getString(Context context){
        RequestQueue requestQueue= Volley.newRequestQueue(context);

        StringRequest stringRequest=new StringRequest(LOGIN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                getJson(s);
               Log.d("TAG",getJson(s));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("TAG", volleyError.getMessage(), volleyError);
            }
        });
        requestQueue.add(stringRequest);

    }

     public static String getJson(String response){
         String json=response.substring("callback(".length(), response.lastIndexOf(");"));
         return json;
     }

//    public static JSONObject parseStringToJson(String json){
//          JSONObject jsonObject=JSON.parseObject(json);
//    }
}
