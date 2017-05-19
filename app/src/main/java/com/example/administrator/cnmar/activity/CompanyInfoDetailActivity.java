package com.example.administrator.cnmar.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
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
import com.example.administrator.cnmar.AppExit;
import com.example.administrator.cnmar.R;
import com.example.administrator.cnmar.helper.UniversalHelper;
import com.example.administrator.cnmar.helper.UrlHelper;
import com.example.administrator.cnmar.helper.VolleyHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import component.company.model.Company;

public class CompanyInfoDetailActivity extends AppCompatActivity {
    private Context context;
    private int id;//详情页面的id
    private String strUrl;
    @BindView(R.id.left_arrow)
    LinearLayout llLeftArrow;
    @BindView(R.id.title)
    TextView tvTitle;
    @BindView(R.id.tvCompanyName)
    TextView tvCompanyName;
    @BindView(R.id.tvPhone)
    TextView tvPhone;
    @BindView(R.id.tvFax)
    TextView tvFax;
    @BindView(R.id.tvAddress)
    TextView tvAddress;
    @BindView(R.id.tvPerson)
    TextView tvPerson;
    @BindView(R.id.tvPosition)
    TextView tvPosition;
    @BindView(R.id.tvMobilePhone)
    TextView tvMobilePhone;
    @BindView(R.id.tvMailBox)
    TextView tvMailBox;
    @BindView(R.id.ivImage)
    NetworkImageView ivImage; //企业资质
    @BindView(R.id.webView)
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_info_detail);
        ButterKnife.bind(this);
        AppExit.getInstance().addActivity(this);

        context = CompanyInfoDetailActivity.this;
        tvTitle.setText("企业详情");
        id = getIntent().getIntExtra("ID", 0);
        strUrl = UrlHelper.URL_COMPANY_DETAIL.replace("{id}", String.valueOf(id));
        strUrl = UniversalHelper.getTokenUrl(strUrl);
        getCompanyInfoFromNet();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
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
//                        loadData出现中文乱码，用loadDataWithBaseURL解决问题
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

    @OnClick(R.id.left_arrow)
    public void onClick() {
        finish();
    }
}
