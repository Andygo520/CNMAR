package com.example.administrator.cnmar.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.administrator.cnmar.R;
import com.example.administrator.cnmar.helper.UniversalHelper;
import com.example.administrator.cnmar.helper.UrlHelper;
import com.example.administrator.cnmar.http.VolleyHelper;

import component.company.model.Company;

public class CompanyInfoDetailActivity extends AppCompatActivity {
    private Context context = CompanyInfoDetailActivity.this;
    private TextView tvCompanyName, tvPhone, tvFax, tvAddress, tvPerson, tvPosition, tvMobilePhone, tvMailBox;
    private static String strUrl;
    private LinearLayout llLeftArrow;
    private TextView tvTitle;
    private WebView webView;
    private int id;
    private NetworkImageView ivImage;   //产品资质

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_info_detail);

        init();
        id = getIntent().getIntExtra("ID", 0);
        strUrl = UrlHelper.URL_COMPANY_DETAIL.replace("{id}", String.valueOf(id));
        strUrl = UniversalHelper.getTokenUrl(strUrl);
        getCompanyInfoFromNet();
    }

    public void init() {
        tvTitle = (TextView) findViewById(R.id.title);
        tvTitle.setText("企业详情");
        llLeftArrow = (LinearLayout) findViewById(R.id.left_arrow);
        llLeftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CompanyManageActivity.class);
                intent.putExtra("flag",0);
                startActivity(intent);
            }
        });


        tvCompanyName = (TextView) findViewById(R.id.tv11);
        tvPhone = (TextView) findViewById(R.id.tv12);
        tvFax = (TextView) findViewById(R.id.tv21);
        tvAddress = (TextView) findViewById(R.id.tv22);
        tvPerson = (TextView) findViewById(R.id.tv31);
        tvPosition = (TextView) findViewById(R.id.tv32);
        tvMobilePhone = (TextView) findViewById(R.id.tv41);
        tvMailBox = (TextView) findViewById(R.id.tv42);

        webView = (WebView) findViewById(R.id.webView);
        ivImage = (NetworkImageView) findViewById(R.id.image);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(this, CompanyManageActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void getCompanyInfoFromNet() {
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
                        Company company = JSON.parseObject(response.getData().toString(), Company.class);


                        tvCompanyName.setText(company.getName());
                        tvPhone.setText(company.getTel());
                        tvFax.setText(company.getFax());
                        tvAddress.setText(company.getAddress());
                        tvPerson.setText(company.getContact());
                        tvPosition.setText(company.getJob());
                        tvMobilePhone.setText(company.getPhone());
                        tvMailBox.setText(company.getEmail());
//                    ImgId>0代表用户设置了企业资质图片
                        if (company.getImgId() > 0) {
//                    获取图片的路径，路径=绝对路径+相对路径
                            String path = UrlHelper.URL_IMAGE + company.getImg().getPath();
                            VolleyHelper.showImageByUrl(context, path, ivImage);
                        }
//                        得到简介内容
                        String introduction = company.getIntro();
//                         loadData出现中文乱码，用loadDataWithBaseURL解决问题
//                        webView.loadData(introduction,"text/html","UTF-8");
                        webView.loadDataWithBaseURL("file://", introduction, "text/html", "UTF-8", "about:blank");
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
