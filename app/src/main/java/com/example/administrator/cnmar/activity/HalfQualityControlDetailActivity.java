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
import component.produce.model.ProduceBomBatch;


public class HalfQualityControlDetailActivity extends AppCompatActivity {

    private Context context;
    private TextView tvPlanCode, tvPlanName, tvHalfCode, tvHalfName, tvSize, tvCheckTime,
            tvUnit, tvProduceNum, tvRemark, tvCheckMan, tvHalfInOrder;
    private TextView tvSuccessNum, tvActualNum;
    private String strUrl;
    private LinearLayout llLeftArrow;
    private TextView tvTitle;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_half_quality_control_detail);
        id = getIntent().getIntExtra("ID", 0);

        AppExit.getInstance().addActivity(this);
        init();
        strUrl = UrlHelper.URL_HALF_QC_DETAIL.replace("{ID}", String.valueOf(id));
        strUrl = UniversalHelper.getTokenUrl(strUrl);
        getListFromNet();
    }

    public void init() {
        context = HalfQualityControlDetailActivity.this;
        tvTitle = (TextView) findViewById(R.id.title);
        tvTitle.setText("半成品检验-详情");
        llLeftArrow = (LinearLayout) findViewById(R.id.left_arrow);
        llLeftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HalfQualityControlDetailActivity.this.finish();//结束当前页面
            }
        });

        tvPlanCode = (TextView) findViewById(R.id.tv11);
        tvPlanName = (TextView) findViewById(R.id.tv12);
        tvHalfCode = (TextView) findViewById(R.id.tv21);
        tvHalfName = (TextView) findViewById(R.id.tv22);
        tvSize = (TextView) findViewById(R.id.tv31);
        tvUnit = (TextView) findViewById(R.id.tv32);
        tvProduceNum = (TextView) findViewById(R.id.tv41);
        tvActualNum = (TextView) findViewById(R.id.tv42);
        tvSuccessNum = (TextView) findViewById(R.id.tv51);
        tvHalfInOrder = (TextView) findViewById(R.id.tv52);
        tvRemark = (TextView) findViewById(R.id.tv61);
        tvCheckMan = (TextView) findViewById(R.id.tv71);
        tvCheckTime = (TextView) findViewById(R.id.tv72);
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
                        ProduceBomBatch ProduceBomBatch = JSON.parseObject(response.getData().toString(), ProduceBomBatch.class);

                        tvPlanCode.setText(ProduceBomBatch.getBom().getPlan().getCode());
                        tvPlanName.setText(ProduceBomBatch.getBom().getPlan().getName());
                        tvHalfCode.setText(ProduceBomBatch.getBom().getHalf().getCode());
                        tvHalfName.setText(ProduceBomBatch.getBom().getHalf().getName());
                        tvSize.setText(ProduceBomBatch.getBom().getHalf().getSpec());
//                        有包装的，单位格式为“9个/袋”
                        if (ProduceBomBatch.getBom().getHalf().getPackType() != PackTypeVo.empty.getKey())
                            tvUnit.setText(ProduceBomBatch.getBom().getHalf().getPackNum()
                                    + ProduceBomBatch.getBom().getHalf().getUnit().getName()
                                    + " / " + ProduceBomBatch.getBom().getHalf().getPackTypeVo().getValue().substring(1, 2));
                        else
                            tvUnit.setText(ProduceBomBatch.getBom().getHalf().getUnit().getName());


                        tvCheckMan.setText(ProduceBomBatch.getTest().getName());
                        tvRemark.setText(ProduceBomBatch.getTestRemark());//检验备注
//                        计划生产的半成品数
                        tvProduceNum.setText(ProduceBomBatch.getBom().getReceiveNum() + ProduceBomBatch.getBom().getHalf().getUnit().getName());

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                        tvActualNum.setText(ProduceBomBatch.getActualNum() + "");
                        tvHalfInOrder.setText(ProduceBomBatch.getHalfInOrder().getCode());
                        tvSuccessNum.setText(String.valueOf(ProduceBomBatch.getSuccessNum()));

                        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        tvCheckTime.setText(sdf1.format(ProduceBomBatch.getTestTime()));

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
