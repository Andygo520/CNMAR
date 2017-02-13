package com.example.administrator.cnmar.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
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

import java.text.SimpleDateFormat;

import component.basic.vo.PackTypeVo;
import component.produce.model.ProducePlan;

public class ProductQualityControlDetailActivity extends AppCompatActivity {
    private Context context;
    private TextView tvPlanCode, tvPlanName, tvProductCode, tvProductName, tvSize, tvCheckTime,
            tvUnit, tvProduceNum, tvCheckMan, tvBeginDate, tvEndDate, tvProductInOrder;
    private TextView tvSuccessNum, tvActualNum;
    private String strUrl;
    private LinearLayout llLeftArrow;
    private TextView tvTitle;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_quality_control_detail);
        AppExit.getInstance().addActivity(this);
        id = getIntent().getIntExtra("ID", 0);

        init();
        strUrl = UrlHelper.URL_PRODUCT_QC_DETAIL.replace("{ID}", String.valueOf(id));
        strUrl = UniversalHelper.getTokenUrl(strUrl);
        getListFromNet();
    }

    public void init() {
        context = ProductQualityControlDetailActivity.this;
        tvTitle = (TextView) findViewById(R.id.title);
        tvTitle.setText("成品检验-详情");
        llLeftArrow = (LinearLayout) findViewById(R.id.left_arrow);
        llLeftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductQualityControlDetailActivity.this.finish();//结束当前页面
            }
        });

        tvPlanCode = (TextView) findViewById(R.id.tv11);
        tvPlanName = (TextView) findViewById(R.id.tv12);
        tvProductCode = (TextView) findViewById(R.id.tv21);
        tvProductName = (TextView) findViewById(R.id.tv22);
        tvSize = (TextView) findViewById(R.id.tv31);
        tvUnit = (TextView) findViewById(R.id.tv32);
        tvBeginDate = (TextView) findViewById(R.id.tv41);
        tvEndDate = (TextView) findViewById(R.id.tv42);
        tvProduceNum = (TextView) findViewById(R.id.tv51);
        tvActualNum = (TextView) findViewById(R.id.tv52);
        tvCheckMan = (TextView) findViewById(R.id.tv61);
        tvCheckTime = (TextView) findViewById(R.id.tv62);
        tvSuccessNum = (TextView) findViewById(R.id.tv71);
        tvProductInOrder = (TextView) findViewById(R.id.tv72);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();//结束当前页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    public void getListFromNet() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue queue = Volley.newRequestQueue(context);
                StringRequest stringRequest = new StringRequest(strUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        String json = VolleyHelper.getJson(s);
                        Log.d("production", json);
                        component.common.model.Response response = JSON.parseObject(json, component.common.model.Response.class);
                        ProducePlan producePlan = JSON.parseObject(response.getData().toString(), ProducePlan.class);

                        tvPlanCode.setText(producePlan.getCode());
                        tvPlanName.setText(producePlan.getName());
                        tvProductCode.setText(producePlan.getProduct().getCode());
                        tvProductName.setText(producePlan.getProduct().getName());
                        tvSize.setText(producePlan.getProduct().getSpec());
//                        有包装的，单位格式为“9个/袋”
                        if (producePlan.getProduct().getPackType() != PackTypeVo.empty.getKey())
                            tvUnit.setText(producePlan.getProduct().getPackNum()
                                    + producePlan.getProduct().getUnit().getName()
                                    + " / " + producePlan.getProduct().getPackTypeVo().getValue().substring(1, 2));
                        else
                            tvUnit.setText(producePlan.getProduct().getUnit().getName());


                        tvCheckMan.setText(producePlan.getTest().getName());

                        tvProduceNum.setText(producePlan.getProduceNum() + producePlan.getProduct().getUnit().getName());

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

//                      开始日期、结束日期的非空判断
                        if (producePlan.getStartDate() == null)
                            tvBeginDate.setText("");
                        else
                            tvBeginDate.setText(sdf.format(producePlan.getStartDate()));
                        if (producePlan.getEndDate() == null)
                            tvEndDate.setText("");
                        else
                            tvEndDate.setText(sdf.format(producePlan.getEndDate()));

                        tvActualNum.setText(producePlan.getActualNum() + "");
                        tvProductInOrder.setText(producePlan.getProductInOrder().getCode());
                        tvSuccessNum.setText(String.valueOf(producePlan.getSuccessNum()));

                        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        tvCheckTime.setText(sdf1.format(producePlan.getTestTime()));

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
