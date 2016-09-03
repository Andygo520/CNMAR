package com.example.administrator.cnmar.http;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.administrator.cnmar.R;

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
//  显示我的资料内容
//    public static void showTextByUrl(Context context,String urlLogin, final TextView tvGender,final TextView tvName, final TextView tvUsername,final TextView tvBirthday){
//        RequestQueue requestQueue= Volley.newRequestQueue(context);
//        StringRequest stringRequest=new StringRequest(urlLogin, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String s) {
//                String json=VolleyHelper.getJson(s);
//                UserInfor userInfor= JSON.parseObject(json,UserInfor.class);
//                tvGender.setText(userInfor.getData().getGender());
//                tvBirthday.setText(userInfor.getData().getBirthday().toString());
//                tvUsername.setText(userInfor.getData().getUsername());
//                tvName.setText(userInfor.getData().getName());
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                Log.e("TAG", volleyError.getMessage(), volleyError);
//            }
//        });
//        requestQueue.add(stringRequest);
//
//    }

    public static void showImageByUrl(Context context, String url, NetworkImageView networkImageView){
             RequestQueue requestQueue= Volley.newRequestQueue(context);

             ImageLoader imageLoader=new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
                 @Override
                 public Bitmap getBitmap(String s) {
                     return null;
                 }

                 @Override
                 public void putBitmap(String s, Bitmap bitmap) {

                 }
             });
             networkImageView.setDefaultImageResId(R.mipmap.default_image);
             networkImageView.setErrorImageResId(R.mipmap.ic_launcher);
             networkImageView.setImageUrl(url,imageLoader);

         }

//    public static JSONObject parseStringToJson(String json){
//          JSONObject jsonObject=JSON.parseObject(json);
//    }
}
