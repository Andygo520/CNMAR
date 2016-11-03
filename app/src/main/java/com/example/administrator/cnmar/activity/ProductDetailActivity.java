package com.example.administrator.cnmar.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import component.company.model.CompanyProduct;

public class ProductDetailActivity extends AppCompatActivity {
    private int id;
    private static String strUrl;
    private Context context=ProductDetailActivity.this;
    private TextView tvTitle;
    private LinearLayout llLeftArrow;
    private TextView tvProductCode;
    private TextView tvProductName;
    private TextView tvSize;
    private NetworkImageView ivImage;   //产品资质
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        id = getIntent().getIntExtra("ID", 0);
        strUrl = UrlHelper.URL_PRODUCT_DETAIL.replace("{id}", String.valueOf(id));
        strUrl = UniversalHelper.getTokenUrl(strUrl);
        
        tvTitle = (TextView) findViewById(R.id.title);
        tvTitle.setText("产品详情");
        llLeftArrow = (LinearLayout) findViewById(R.id.left_arrow);
        llLeftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CompanyManageActivity.class);
                intent.putExtra("flag",1);
                startActivity(intent);
            }
        });
        tvProductCode= (TextView)findViewById(R.id.productCode);
        tvProductName= (TextView) findViewById(R.id.productName);
        tvSize= (TextView) findViewById(R.id.size);
        ivImage= (NetworkImageView) findViewById(R.id.image);

        webView = (WebView) findViewById(R.id.webView);



        new Thread(new Runnable() {
            @Override
            public void run() {
                getProductInfoFromNet();
            }
        }).start();
    }

    public void getProductInfoFromNet(){
        RequestQueue queue= Volley.newRequestQueue(context);

        StringRequest stringRequest=new StringRequest(strUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                component.common.model.Response response = JSON.parseObject(VolleyHelper.getJson(s), component.common.model.Response.class);
                CompanyProduct companyProduct = JSON.parseObject(response.getData().toString(), CompanyProduct.class);

                tvProductCode.setText(companyProduct.getCode());
                tvProductName.setText(companyProduct.getName());
                tvSize.setText(companyProduct.getSpec());
//               ImgId>0代表用户设置了产品资质图片
                if(companyProduct.getImgId()>0){
//                    获取图片的路径，路径=绝对路径+相对路径
                    String path= UrlHelper.URL_IMAGE+companyProduct.getImg().getPath();
                    VolleyHelper.showImageByUrl(context,path,ivImage);
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

}
