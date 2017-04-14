package com.example.administrator.cnmar.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.administrator.cnmar.AppExit;
import com.example.administrator.cnmar.R;
import com.example.administrator.cnmar.helper.UniversalHelper;
import com.example.administrator.cnmar.helper.UrlHelper;
import com.example.administrator.cnmar.helper.VolleyHelper;

import java.text.SimpleDateFormat;

import component.system.model.SystemUser;


public class SystemUserDetailActivity extends AppCompatActivity {
    private Context context = SystemUserDetailActivity.this;
    private TextView tvAccount, tvName, tvSex, tvBirthday, tvStatus;
    private static String strUrl;
    private LinearLayout llLeftArrow;
    private TextView tvTitle;
    private int id;
    private NetworkImageView ivImage;   //头像

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_user_detail);
        AppExit.getInstance().addActivity(this);
        init();
        id = getIntent().getIntExtra("ID", 0);
        strUrl = UrlHelper.URL_SYSTEM_USER_DETAIL.replace("{ID}", String.valueOf(id));
        strUrl = UniversalHelper.getTokenUrl(strUrl);
        getUserInfoFromNet();
    }

    public void init() {
        tvTitle = (TextView) findViewById(R.id.title);
        tvTitle.setText("用户详情");
        llLeftArrow = (LinearLayout) findViewById(R.id.left_arrow);
        llLeftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        tvAccount = (TextView) findViewById(R.id.tv11);
        tvName = (TextView) findViewById(R.id.tv12);
        tvSex = (TextView) findViewById(R.id.tv21);
        tvBirthday = (TextView) findViewById(R.id.tv22);
        tvStatus = (TextView) findViewById(R.id.tv31);
        ivImage = (NetworkImageView) findViewById(R.id.image);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void getUserInfoFromNet() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue queue = Volley.newRequestQueue(context);
                StringRequest stringRequest = new StringRequest(strUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        String json = VolleyHelper.getJson(s);
//                        Log.d("RRRRR",json);
                        component.common.model.Response response = JSON.parseObject(json, component.common.model.Response.class);
                        SystemUser user = JSON.parseObject(response.getData().toString(), SystemUser.class);

                        tvAccount.setText(user.getUsername());
                        tvName.setText(user.getName());
                        tvSex.setText(user.getGender()==null ? "" : user.getGender());
                        if (user.getBirthday() == null)
                            tvBirthday.setText("");
                        else
                            tvBirthday.setText(new SimpleDateFormat("yyyy-MM-dd").format(user.getBirthday()));
                        tvStatus.setText(user.getIsEnableVo().getValue());  //账号状态
//                    ImgId>0代表用户设置了图像
                        if (user.getImgId() > 0) {
//                    获取图片的路径，路径=绝对路径+相对路径
                            String path = UrlHelper.URL_IMAGE + user.getImg().getPath();
                            VolleyHelper.showImageByUrl(context, path, ivImage);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                });
                queue.add(stringRequest);
            }
        }).start();
    }
}
