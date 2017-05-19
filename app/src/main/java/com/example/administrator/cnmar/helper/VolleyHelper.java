package com.example.administrator.cnmar.helper;

import android.content.Context;
import android.graphics.Bitmap;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.example.administrator.cnmar.R;

/**
 * Created by Administrator on 2016/9/1.
 */
public class VolleyHelper {

    public static String getJson(String response) {
//         String json=response.substring("callback(".length(), response.lastIndexOf(");"));
        String json = response;
        return json;
    }

    /*
    * 根据URL地址显示图片内容
    *
    * */
    public static void showImageByUrl(Context context, String url, NetworkImageView networkImageView) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        ImageLoader imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
            @Override
            public Bitmap getBitmap(String s) {
                return null;
            }

            @Override
            public void putBitmap(String s, Bitmap bitmap) {

            }
        });
//             networkImageView.setDefaultImageResId(R.mipmap.ic_launcher);
        networkImageView.setErrorImageResId(R.mipmap.ic_launcher);
        networkImageView.setImageUrl(url, imageLoader);

    }

}
