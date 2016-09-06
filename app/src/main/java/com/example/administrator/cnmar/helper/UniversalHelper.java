package com.example.administrator.cnmar.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Administrator on 2016/9/6.
 */
public class UniversalHelper {
    public static void backToLastActivity(final Context context, ImageView imageView, final Activity lastActivity){
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,lastActivity.getClass());
                context.startActivity(intent);
            }
        });
    }
}
