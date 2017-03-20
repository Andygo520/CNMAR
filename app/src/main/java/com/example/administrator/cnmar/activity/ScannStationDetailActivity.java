package com.example.administrator.cnmar.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
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
import com.example.administrator.cnmar.helper.SPHelper;
import com.example.administrator.cnmar.helper.UniversalHelper;
import com.example.administrator.cnmar.helper.UrlHelper;
import com.example.administrator.cnmar.helper.VolleyHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import component.com.model.ComTool;
import component.produce.model.ProduceReceive;
import zxing.activity.CaptureActivity;

/*
* 机台的(子)加工单列表的详情页
* */
public class ScannStationDetailActivity extends AppCompatActivity {
    private Context context;
    private String url;
    private int processId, stationId, id;//工序id、机台id、条目id(领料单id)
    @BindView(R.id.name91)
    TextView name91;
    @BindView(R.id.tv91)
    TextView tv91;
    @BindView(R.id.name92)
    TextView name92;
    @BindView(R.id.tv92)
    TextView tv92;
    @BindView(R.id.tableRow)
    TableRow tableRow;
    @BindView(R.id.btnScannLast)
    Button btnScannLast;
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
    @BindView(R.id.name41)
    TextView name41;
    @BindView(R.id.tv41)
    TextView tv41;
    @BindView(R.id.name42)
    TextView name42;
    @BindView(R.id.tv42)
    TextView tv42;
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
    @BindView(R.id.tv62)
    TextView tv62;
    @BindView(R.id.name71)
    TextView name71;
    @BindView(R.id.tv71)
    TextView tv71;
    @BindView(R.id.name72)
    TextView name72;
    @BindView(R.id.tv72)
    TextView tv72;
    @BindView(R.id.name81)
    TextView name81;
    @BindView(R.id.tv81)
    TextView tv81;
    @BindView(R.id.name82)
    TextView name82;
    @BindView(R.id.tv82)
    TextView tv82;
    @BindView(R.id.btn)
    Button btn;//扫描料框

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scann_station_detail);
        ButterKnife.bind(this);
        AppExit.getInstance().addActivity(this);

        context = ScannStationDetailActivity.this;
        init();
        processId = getIntent().getIntExtra("processId", -1);
        stationId = getIntent().getIntExtra("stationId", -1);
        id = getIntent().getIntExtra("ID", -99);

        url = UrlHelper.URL_SCANN_STATION_DETAIL.replace("{ID}", id + "")
                .replace("{processId}", processId + "")
                .replace("{stationId}", stationId + "")
                .replace("{userId}", SPHelper.getInt(context, "userId") + "");
        url = UniversalHelper.getTokenUrl(url);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getTableInfoFromNet(url);
    }

    public void init() {
        title.setText("生产管理");
        name31.setText("计划生产数量");
        name32.setText("末工序合格品数量");
        name41.setText("领料单编号");
        name42.setText("领料人");
        name51.setText("工序");
        name52.setText("车间");
        name61.setText("机台工位");
        name62.setText("工装");
        name71.setText("班组");
        name72.setText("操作工");
        name81.setText("合格品数量");
        name82.setText("不合格品数量");
        name91.setText("上一道工序");
        name92.setText("上工序合格品数量");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
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
                        ProduceReceive produceReceive = JSON.parseObject(response.getData().toString(), ProduceReceive.class);

//                        加工单详情
                        if (produceReceive.getPlan() != null) {
                            tvTableTitle.setText("加工单详情");
                            name11.setText("加工单编号");
                            name12.setText("加工单名称");
                            name21.setText("成品编码");
                            name22.setText("成品名称");
                            tv11.setText(produceReceive.getPlan().getCode());
                            tv12.setText(produceReceive.getPlan().getName());
                            tv21.setText(produceReceive.getPlan().getProduct().getCode());
                            tv22.setText(produceReceive.getPlan().getProduct().getName());
                            tv31.setText(produceReceive.getPlan().getProduceNum() + produceReceive.getPlan().getProduct().getUnit().getName());
                            tv32.setText(produceReceive.getProcessProduct().getLast().getSuccessNum()+"");// 末工序合格品数量
                            tv41.setText(produceReceive.getCode());
                            tv42.setText(produceReceive.getReceiveUser().getName());
                            tv51.setText(produceReceive.getProcessProduct().getName());
                            tv52.setText(produceReceive.getProcessProduct().getStation().getWorkshop().getName());
                            tv61.setText(produceReceive.getProcessProduct().getStation().getName());
//                            得到工装列表
                            List<ComTool> tools = produceReceive.getProcessProduct().getTools();
                            if (tools.size() == 0) {
                                tv62.setText("");
                            } else {
                                String toolNames = "";
                                for (ComTool tool : tools) {
                                    toolNames += tool.getName() + "，";
                                }
                                tv62.setText(toolNames.substring(0, toolNames.length() - 1));
                            }
                            tv71.setText(produceReceive.getTeam().getName());
                            tv72.setText(SPHelper.getString(context, "name"));
                            tv81.setText(produceReceive.getProcessProduct().getSuccessNum() + "");
                            tv82.setText(produceReceive.getProcessProduct().getFailureNum() + "");
//                            上工序不为空显示最后一行以及扫描上工序料框按钮
                            if (produceReceive.getProcessProduct().getPrev() != null) {
                                tableRow.setVisibility(View.VISIBLE);
                                btnScannLast.setVisibility(View.VISIBLE);
                                tv91.setText(produceReceive.getProcessProduct().getPrev().getName());
                                tv92.setText(produceReceive.getProcessProduct().getPrev().getSuccessNum()+"");
                            }
                        }
//                       子加工单详情
                        else {
                            tvTableTitle.setText("子加工单详情");
                            name11.setText("子加工单编号");
                            name12.setText("加工单名称");
                            name21.setText("半成品编码");
                            name22.setText("半成品名称");
                            tv11.setText(produceReceive.getBom().getCode());
                            tv12.setText(produceReceive.getBom().getPlan().getName());
                            tv21.setText(produceReceive.getBom().getHalf().getCode());
                            tv22.setText(produceReceive.getBom().getHalf().getName());
                            tv31.setText(produceReceive.getBom().getReceiveNum() + produceReceive.getBom().getHalf().getUnit().getName());
                            tv32.setText(produceReceive.getProcessHalf().getLast().getSuccessNum()+"");// 末工序合格品数量
                            tv41.setText(produceReceive.getCode());
                            tv42.setText(produceReceive.getReceiveUser().getName());
                            tv51.setText(produceReceive.getProcessHalf().getName());
                            tv52.setText(produceReceive.getProcessHalf().getStation().getWorkshop().getName());
                            tv61.setText(produceReceive.getProcessHalf().getStation().getName());
                            //                       得到工装列表
                            List<ComTool> tools = produceReceive.getProcessHalf().getTools();
                            if (tools.size() == 0) {
                                tv62.setText("");
                            } else {
                                String toolNames = "";
                                for (ComTool tool : tools) {
                                    toolNames += tool.getName() + "，";
                                }
                                tv62.setText(toolNames.substring(0, toolNames.length() - 1));
                            }
                            tv71.setText(produceReceive.getTeam().getName());
                            tv72.setText(SPHelper.getString(context, "name"));
                            tv81.setText(produceReceive.getProcessHalf().getSuccessNum() + "");
                            tv82.setText(produceReceive.getProcessHalf().getFailureNum() + "");
 //                            上工序不为空显示最后一行以及扫描上工序料框按钮
                            if (produceReceive.getProcessHalf().getPrev() != null) {
                                tableRow.setVisibility(View.VISIBLE);
                                btnScannLast.setVisibility(View.VISIBLE);
                                tv91.setText(produceReceive.getProcessHalf().getPrev().getName());
                                tv92.setText(produceReceive.getProcessHalf().getPrev().getSuccessNum()+"");
                            }
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

    @OnClick({R.id.left_arrow, R.id.btn,R.id.btnScannLast})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_arrow:
                finish();
                break;
            case R.id.btnScannLast:
                Intent intent1 = new Intent(context, CaptureActivity.class);
                intent1.putExtra("FLAG", 666);//跳转标志位
                intent1.putExtra("receiveId", id);
                intent1.putExtra("processId", processId);
                intent1.putExtra("stationId", stationId);
                startActivity(intent1);
                break;
            case R.id.btn:
                Intent intent = new Intent(context, CaptureActivity.class);
                intent.putExtra("FLAG", 50);//跳转标志位
                intent.putExtra("receiveId", id);
                intent.putExtra("processId", processId);
                intent.putExtra("stationId", stationId);
                startActivity(intent);
                break;
        }
    }
}
