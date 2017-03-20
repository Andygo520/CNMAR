package com.example.administrator.cnmar.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableRow;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import component.produce.model.ProduceTest;

public class CheckFlowDetailActivity extends AppCompatActivity {
    private int flag;//区分加工单子加工单的标志位
    private Context context;
    @BindView(R.id.row)
    TableRow row;
    @BindView(R.id.left_arrow)
    LinearLayout leftArrow;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.name11)
    TextView name11;
    @BindView(R.id.tv11)
    TextView tv11;
    @BindView(R.id.name12)
    TextView name12;
    @BindView(R.id.tv12)
    TextView tv12;
    @BindView(R.id.name21)
    TextView name21;
    @BindView(R.id.tv21)
    TextView tv21;
    @BindView(R.id.name22)
    TextView name22;
    @BindView(R.id.tv22)
    TextView tv22;
    @BindView(R.id.name31)
    TextView name31;
    @BindView(R.id.tv31)
    TextView tv31;
    @BindView(R.id.name32)
    TextView name32;
    @BindView(R.id.tv32)
    TextView tv32;
    @BindView(R.id.name41)
    TextView name41;
    @BindView(R.id.tv41)
    TextView tv41;
    @BindView(R.id.name42)
    TextView name42;
    @BindView(R.id.tv42)
    TextView tv42;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_flow_detail);
        ButterKnife.bind(this);
        AppExit.getInstance().addActivity(this);

        int id = getIntent().getIntExtra("Id", 0);
        flag = getIntent().getIntExtra("Flag", 0);
        init();
        String url = UrlHelper.URL_CHECK_FLOW_DETAIL.replace("{Id}", id + "");
        url = UniversalHelper.getTokenUrl(url);
        Log.d("urlFlow", url);
        getDetailFromNet(url);
    }

    public void init() {
        context = CheckFlowDetailActivity.this;
        title.setText("生产管理");
        if (flag == 1)
            name11.setText("加工单编号");
        else if (flag == 2)
            name11.setText("子加工单编号");

        name12.setText("工序");
        name21.setText("机台工位");
        name22.setText("检验数量");
        name31.setText("不合格品数量");
        name32.setText("不合格原因");
        row.setVisibility(View.VISIBLE);
        name41.setText("检验员");
        name42.setText("检验时间");
    }

    public void getDetailFromNet(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue q = Volley.newRequestQueue(context);
                StringRequest request = new StringRequest(url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        String json = VolleyHelper.getJson(s);
                        component.common.model.Response response = JSON.parseObject(json, component.common.model.Response.class);
                        ProduceTest test = JSON.parseObject(response.getData().toString(), ProduceTest.class);
//      显示加工单检验流水
                        if (flag == 1) {
                            tv11.setText(test.getReceive().getPlan().getCode());
                            tv12.setText(test.getReceive().getProcessProduct() == null ? "" : test.getReceive().getProcessProduct().getName());
                        }
//      显示子加工单检验流水
                        else if (flag == 2) {
                            tv11.setText(test.getReceive().getBom().getCode());
                            tv12.setText(test.getReceive().getProcessHalf() == null ? "" : test.getReceive().getProcessHalf().getName());
                        }

                        tv21.setText(test.getStation().getName());
                        tv22.setText(test.getTestNum() + "");
                        tv31.setText(test.getFailNum() + "");
                        tv32.setText(test.getReason());
                        tv41.setText(test.getTest().getName());
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        tv42.setText(sdf.format(test.getTestTime()));

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                });
                q.add(request);
            }
        }).start();
    }

    @OnClick(R.id.left_arrow)
    public void onClick() {
        finish();
    }
}
