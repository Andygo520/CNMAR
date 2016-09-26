package com.example.administrator.cnmar.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/9/7.
 */
public class NetworkHelper {
    public static boolean isNetworkConnected(Context context){
        ConnectivityManager connectivityManager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        return networkInfo!=null&&networkInfo.isConnected();
    }
    public static void noNetworkToast(Context context){
        Toast.makeText(context,"网络异常，请检查网络连接",Toast.LENGTH_SHORT).show();
    }
}
