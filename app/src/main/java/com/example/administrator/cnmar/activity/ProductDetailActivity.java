package com.example.administrator.cnmar.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import component.company.model.CompanyProduct;

public class ProductDetailActivity extends AppCompatActivity {
    @BindView(R.id.left_arrow)
    LinearLayout llLeftArrow;
    @BindView(R.id.title)
    TextView tvTitle;
    @BindView(R.id.tvProductCode)
    TextView tvProductCode;
    @BindView(R.id.tvProductName)
    TextView tvProductName;
    @BindView(R.id.tvSize)
    TextView tvSize;
    @BindView(R.id.ivImage)
    NetworkImageView ivImage;
    @BindView(R.id.webView)
    WebView webView;
    private int id;
    private  String strUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        ButterKnife.bind(this);
        AppExit.getInstance().addActivity(this);

        id = getIntent().getIntExtra("ID", 0);
        strUrl = UrlHelper.URL_PRODUCT_DETAIL.replace("{id}", String.valueOf(id));
        strUrl = UniversalHelper.getTokenUrl(strUrl);

        tvTitle.setText("产品详情");
        new Thread(new Runnable() {
            @Override
            public void run() {
                getProductInfoFromNet();
            }
        }).start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void getProductInfoFromNet() {
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(strUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                component.common.model.Response response = JSON.parseObject(VolleyHelper.getJson(s), component.common.model.Response.class);
                CompanyProduct companyProduct = JSON.parseObject(response.getData().toString(), CompanyProduct.class);

                tvProductCode.setText(companyProduct.getCode());
                tvProductName.setText(companyProduct.getName());
                tvSize.setText(companyProduct.getSpec());
//               ImgId>0代表用户设置了产品资质图片
                if (companyProduct.getImgId() > 0) {
//                    获取图片的路径，路径=绝对路径+相对路径
                    String path = UrlHelper.URL_IMAGE + companyProduct.getImg().getPath();
                    VolleyHelper.showImageByUrl(ProductDetailActivity.this, path, ivImage);
                }
//              得到简介内容
                String introduction = companyProduct.getIntro();
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

    @OnClick(R.id.left_arrow)
    public void onClick() {
        finish();
    }
}
