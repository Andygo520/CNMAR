package com.example.administrator.cnmar.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.administrator.cnmar.AppExit;
import com.example.administrator.cnmar.R;
import com.example.administrator.cnmar.helper.UniversalHelper;
import com.example.administrator.cnmar.helper.UrlHelper;
import com.example.administrator.cnmar.helper.VolleyHelper;

import component.supply.model.Supply;

public class SupplyManageDetailActivity extends AppCompatActivity {
    private Context context = SupplyManageDetailActivity.this;
    private TextView tvSupplyCode, tvSupplyName, tvPhone, tvFax, tvAddress, tvPerson, tvPosition, tvMobilePhone, tvMailBox;
    private static String strUrl;
    private LinearLayout llLeftArrow;
    private TextView tvTitle;
    private WebView webView;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supply_manage_detail);
        AppExit.getInstance().addActivity(this);
        init();
        id = getIntent().getIntExtra("ID", 0);
        strUrl = UrlHelper.URL_SUPPLY_DETAIL.replace("{id}", String.valueOf(id));
        strUrl = UniversalHelper.getTokenUrl(strUrl);
        getSupplyInfoFromNet();
    }

    public void init() {
        tvTitle = (TextView) findViewById(R.id.title);
        tvTitle.setText("供应商详情");
        llLeftArrow = (LinearLayout) findViewById(R.id.left_arrow);
        llLeftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        tvSupplyCode = (TextView) findViewById(R.id.tv11);
        tvSupplyName = (TextView) findViewById(R.id.tv12);
        tvPhone = (TextView) findViewById(R.id.tv21);
        tvFax = (TextView) findViewById(R.id.tv22);
        tvAddress = (TextView) findViewById(R.id.tv31);
        tvPerson = (TextView) findViewById(R.id.tv41);
        tvPosition = (TextView) findViewById(R.id.tv42);
        tvMobilePhone = (TextView) findViewById(R.id.tv51);
        tvMailBox = (TextView) findViewById(R.id.tv52);

        webView = (WebView) findViewById(R.id.webView);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void getSupplyInfoFromNet() {
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
                        Supply supply = JSON.parseObject(response.getData().toString(), Supply.class);


                        tvSupplyCode.setText(supply.getCode());
                        tvSupplyName.setText(supply.getName());
                        tvPhone.setText(supply.getTel());
                        tvFax.setText(supply.getFax());
                        tvAddress.setText(supply.getAddress());
                        tvPerson.setText(supply.getContact());
                        tvPosition.setText(supply.getJob());
                        tvMobilePhone.setText(supply.getPhone());
                        tvMailBox.setText(supply.getEmail());
                        String introduction = supply.getIntro();
//   loadData出现中文乱码，用loadDataWithBaseURL解决问题
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
