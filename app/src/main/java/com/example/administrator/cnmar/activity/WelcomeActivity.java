package com.example.administrator.cnmar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import com.example.administrator.cnmar.AppExit;
import com.example.administrator.cnmar.helper.SPHelper;

public class WelcomeActivity extends AppCompatActivity {
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
//            如果登录用户存在并且勾选了自动登录，那么下次进入app直接跳转到主页面
                boolean flag = SPHelper.getBoolean(WelcomeActivity.this, "flag", false);
                if (flag == false) {
                    Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        启动页显示的背景图跟其主题的 windowBackground一样的时候，不用调用setContentView
//        setContentView(R.layout.activity_welcome);
        AppExit.getInstance().addActivity(this);
        handler.sendEmptyMessageDelayed(0, 2000);
    }
}
