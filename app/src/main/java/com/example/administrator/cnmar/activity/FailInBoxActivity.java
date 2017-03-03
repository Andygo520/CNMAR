package com.example.administrator.cnmar.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.administrator.cnmar.AppExit;
import com.example.administrator.cnmar.R;
import com.example.administrator.cnmar.helper.SPHelper;
import com.example.administrator.cnmar.helper.UniversalHelper;
import com.example.administrator.cnmar.helper.UrlHelper;
import com.example.administrator.cnmar.helper.VolleyHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import component.com.model.ComBox;
import component.com.model.ComTool;

public class FailInBoxActivity extends AppCompatActivity {

    private Context context;
    private int boxId, testBoxId, receiveId, stationId, testId;
    private int num;//记录待检验料框现存数量
    @BindView(R.id.left_arrow)
    LinearLayout leftArrow;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tvTableTitle)
    TextView tvTableTitle;
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
    @BindView(R.id.tvTableTitle1)
    TextView tvTableTitle1;
    @BindView(R.id.name41)
    TextView name41;
    @BindView(R.id.tv41)
    TextView tv41;
    @BindView(R.id.name42)
    TextView name42;
    @BindView(R.id.tv42)
    TextView tv42;
    @BindView(R.id.tvTableTitle2)
    TextView tvTableTitle2;
    @BindView(R.id.name51)
    TextView name51;
    @BindView(R.id.tv51)
    TextView tv51;
    @BindView(R.id.name52)
    TextView name52;
    @BindView(R.id.tv52)
    TextView tv52;
    @BindView(R.id.name61)
    TextView name61;
    @BindView(R.id.tv61)
    TextView tv61;
    @BindView(R.id.name62)
    TextView name62;
    @BindView(R.id.et62)
    EditText et62;
    @BindView(R.id.name71)
    TextView name71;
    @BindView(R.id.et71)
    EditText et71;
    @BindView(R.id.btn)
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fail_in_box);
        ButterKnife.bind(this);
        AppExit.getInstance().addActivity(this);

        init();
        String url = getIntent().getStringExtra("result");
        Log.d("FailInBoxActivity", url);
        url = UniversalHelper.getTokenUrl(url);
        getTableInfoFromNet(url);
    }

    public void init() {
        context = FailInBoxActivity.this;
        title.setText("生产管理");
        name21.setText("工序");
        name22.setText("车间");
        name31.setText("机台工位");
        name32.setText("工装");
        name41.setText("料框编码");
        name42.setText("现存数量");
        name51.setText("料框编码");
        name52.setText("类型");
        name61.setText("现存数量");
        name62.setText("入框数量");
        name71.setText("不合格原因");
        tvTableTitle1.setText("待检验料框");
        tvTableTitle2.setText("不合格品料框");
//       因为et71默认获得焦点，而我们希望et62默认获得焦点，所以将et71设置为触摸的时候获得焦点
        et71.setFocusableInTouchMode(false);
        et71.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                et71.setFocusableInTouchMode(true);
                return false;
            }
        });
    }

    public void getTableInfoFromNet(final String strUrl) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue queue = Volley.newRequestQueue(context);
                StringRequest stringRequest = new StringRequest(strUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        String json = VolleyHelper.getJson(s);
                        component.common.model.Response response = JSON.parseObject(json, component.common.model.Response.class);
                        ComBox comBox = JSON.parseObject(response.getData().toString(), ComBox.class);

                        boxId = comBox.getId();
                        testBoxId = comBox.getTestBox().getId();
                        receiveId = comBox.getReceiveId();
                        stationId = comBox.getStationId();

//                        加工单详情
                        if (comBox.getReceive().getPlan() != null) {
                            name11.setText("加工单编号");
                            name12.setText("成品编码");
                            tvTableTitle.setText("加工单");

                            tv11.setText(comBox.getReceive().getPlan().getCode());
                            tv12.setText(comBox.getReceive().getPlan().getProduct().getCode());
                            tv21.setText(comBox.getReceive().getProcessProduct().getName());
                            tv22.setText(comBox.getReceive().getProcessProduct().getStation().getWorkshop().getName());
                            tv31.setText(comBox.getReceive().getProcessProduct().getStation().getName());
//                          得到工装列表
                            List<ComTool> tools = comBox.getReceive().getProcessProduct().getTools();
                            if (tools.size() == 0) {
                                tv32.setText("");
                            } else {
                                String toolNames = "";
                                for (ComTool tool : tools) {
                                    toolNames += tool.getName() + "，";
                                }
                                tv32.setText(toolNames.substring(0, toolNames.length() - 1));
                            }

                        }
//                       子加工单详情
                        else {
                            name11.setText("子加工单编号");
                            name12.setText("半成品编码");
                            tvTableTitle.setText("子加工单");

                            tv11.setText(comBox.getReceive().getBom().getCode());
                            tv12.setText(comBox.getReceive().getBom().getHalf().getCode());
                            tv21.setText(comBox.getReceive().getProcessHalf().getName());
                            tv22.setText(comBox.getReceive().getProcessHalf().getStation().getWorkshop().getName());
                            tv31.setText(comBox.getReceive().getProcessHalf().getStation().getName());
//                           得到工装列表
                            List<ComTool> tools = comBox.getReceive().getProcessHalf().getTools();
                            if (tools.size() == 0) {
                                tv32.setText("");
                            } else {
                                String toolNames = "";
                                for (ComTool tool : tools) {
                                    toolNames += tool.getName() + "，";
                                }
                                tv32.setText(toolNames.substring(0, toolNames.length() - 1));
                            }
                        }
                        tv41.setText(comBox.getTestBox().getCode());
                        num=comBox.getTestBox().getNum();//现存数量
                        tv42.setText(num+ "");
                        tv51.setText(comBox.getCode());
                        tv52.setText(comBox.getBoxTypeVo().getValue());
                        tv61.setText(comBox.getNum() + "");
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

    public void sendRequest(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue queue = Volley.newRequestQueue(context);
                StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        String json = VolleyHelper.getJson(s);
                        component.common.model.Response response = JSON.parseObject(json, component.common.model.Response.class);
                        if (response.isStatus()) {
                            Intent intent = new Intent(context, ProduceManageActivity.class);
                            intent.putExtra("flag", 0);
                            startActivity(intent);
                        } else
                            Toast.makeText(context, response.getMsg(), Toast.LENGTH_SHORT).show();

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


    @OnClick({R.id.left_arrow, R.id.btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_arrow:
                finish();
                break;
            case R.id.btn:
                String failNum = et62.getText().toString().trim();
//                输入数量的判断
                if (Integer.parseInt(failNum)>num){
                    Toast.makeText(context,"入框数量不能大于待检验数量",Toast.LENGTH_SHORT).show();
                    return;
                }
                String failReason = et71.getText().toString().trim();
                String url = UrlHelper.URL_FAIL_IN_BOX_COMMIT.replace("{boxId}", "" + boxId)
                        .replace("{testBoxId}", "" + testBoxId)
                        .replace("{receiveId}", "" + receiveId)
                        .replace("{stationId}", "" + stationId)
                        .replace("{testId}", "" + SPHelper.getInt(context, "userId"))
                        .replace("{failNum}", failNum)
                        .replace("{reason}", failReason);
                Log.d("URL_FAIL_IN_BOX_COMMIT", url);
                url = UniversalHelper.getTokenUrl(url);
                sendRequest(url);
                break;
        }
    }
}
