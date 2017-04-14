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
import component.com.model.ComStation;
import component.com.model.ComTool;
import zxing.activity.CaptureActivity;

import static com.example.administrator.cnmar.R.id.tableRow;

/*
* 机台的(子)加工单列表的详情页
* */
public class ScannStationDetailActivity extends AppCompatActivity {

    private Context context;
    private String url;
    private int processId, stationId, planId, bomId, id;//工序id、（子）加工单id、条目id
    @BindView(R.id.name101)
    TextView name101;
    @BindView(R.id.tv101)
    TextView tv101;
    @BindView(R.id.name102)
    TextView name102;
    @BindView(R.id.tv102)
    TextView tv102;
    @BindView(R.id.name91)
    TextView name91;
    @BindView(R.id.tv91)
    TextView tv91;
    @BindView(R.id.name92)
    TextView name92;
    @BindView(R.id.tv92)
    TextView tv92;
    @BindView(tableRow)
    TableRow row10;
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
        planId = getIntent().getIntExtra("planId", 0);
        bomId = getIntent().getIntExtra("bomId", 0);
        id = getIntent().getIntExtra("ID", -99);

        url = UrlHelper.URL_SCANN_STATION_DETAIL.replace("{ID}", id + "")
                .replace("{planId}", planId + "")
                .replace("{bomId}", bomId + "")
                .replace("{processId}", processId + "")
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
        name32.setText("实际生产数量");
        name41.setText("已入库合格品数量");
        name42.setText("末工序合格品数量");
        name51.setText("领料单编号");
        name52.setText("领料人");
        name61.setText("工序");
        name62.setText("车间");
        name71.setText("机台工位");
        name72.setText("工装");
        name81.setText("班组");
        name82.setText("操作工");
        name91.setText("工序合格品数量");
        name92.setText("工序不合格品数量");
        name101.setText("上一道工序");
        name102.setText("上工序合格品数量");
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
                        ComStation station = JSON.parseObject(response.getData().toString(), ComStation.class);
//                        加工单详情
                        if (station.getPlan() != null) {
                            tvTableTitle.setText("加工单详情");
                            name11.setText("加工单编号");
                            name12.setText("加工单名称");
                            name21.setText("成品编码");
                            name22.setText("成品名称");
                            tv11.setText(station.getPlan().getCode());
                            tv12.setText(station.getPlan().getName());
                            tv21.setText(station.getPlan().getProduct().getCode());
                            tv22.setText(station.getPlan().getProduct().getName());
                            tv31.setText(station.getPlan().getProduceNum() + station.getPlan().getProduct().getUnit().getName());
                            tv32.setText(station.getPlan().getBatchGroup() == null ? "" : station.getPlan().getBatchGroup().getActualNum() + "");
                            tv41.setText(station.getPlan().getBatchGroup() == null ? "" : station.getPlan().getBatchGroup().getSuccessNum() + "");
                            tv42.setText(station.getPlan().getProcessProduct().getLast().getSuccessNum() + "");// 末工序合格品数量
                            tv51.setText(station.getCode());
                            tv52.setText(station.getPlan().getReceive().getReceiveUser().getName());
                            tv61.setText(station.getPlan().getProcessProduct().getName());
                            tv62.setText(station.getWorkshop().getName());
                            tv71.setText(station.getName());
//                            得到工装列表
                            List<ComTool> tools = station.getPlan().getProcessProduct().getTools();
                            if (tools.size() == 0) {
                                tv72.setText("");
                            } else {
                                String toolNames = "";
                                for (ComTool tool : tools) {
                                    toolNames += tool.getName() + "，";
                                }
                                tv72.setText(toolNames.substring(0, toolNames.length() - 1));
                            }
                            tv81.setText(station.getPlan().getProcessProduct().getTeam().getName());
                            tv82.setText(SPHelper.getString(context, "name"));
                            tv91.setText(station.getPlan().getProcessProduct().getSuccessNum() + "");
                            tv92.setText(station.getPlan().getProcessProduct().getFailureNum() + "");
//                            上工序不为空显示最后一行以及扫描上工序料框按钮
                            if (station.getPlan().getProcessProduct().getPrev() != null) {
                                row10.setVisibility(View.VISIBLE);
                                btnScannLast.setVisibility(View.VISIBLE);
                                tv101.setText(station.getPlan().getProcessProduct().getPrev().getName());
                                tv102.setText(station.getPlan().getProcessProduct().getPrev().getSuccessNum() + "");
                            }
                        }
//                       子加工单详情
                        else {
                            tvTableTitle.setText("子加工单详情");
                            name11.setText("子加工单编号");
                            name12.setText("加工单名称");
                            name21.setText("半成品编码");
                            name22.setText("半成品名称");
                            tv11.setText(station.getBom().getCode());
                            tv12.setText(station.getBom().getPlan().getName());
                            tv21.setText(station.getBom().getHalf().getCode());
                            tv22.setText(station.getBom().getHalf().getName());
                            tv31.setText(station.getBom().getReceiveNum() + station.getBom().getHalf().getUnit().getName());
                            tv32.setText(station.getBom().getBatchGroup() == null ? "" : station.getBom().getBatchGroup().getActualNum() + "");
                            tv41.setText(station.getBom().getBatchGroup() == null ? "" : station.getBom().getBatchGroup().getSuccessNum() + "");
                            tv42.setText(station.getBom().getProcessHalf().getLast().getSuccessNum() + "");// 末工序合格品数量
                            tv51.setText(station.getCode());
                            tv52.setText(station.getBom().getReceive().getReceiveUser().getName());
                            tv61.setText(station.getBom().getProcessHalf().getName());
                            tv62.setText(station.getWorkshop().getName());
                            tv71.setText(station.getName());
                            //                       得到工装列表
                            List<ComTool> tools = station.getBom().getProcessHalf().getTools();
                            if (tools.size() == 0) {
                                tv72.setText("");
                            } else {
                                String toolNames = "";
                                for (ComTool tool : tools) {
                                    toolNames += tool.getName() + "，";
                                }
                                tv72.setText(toolNames.substring(0, toolNames.length() - 1));
                            }
                            tv81.setText(station.getBom().getProcessHalf().getTeam().getName());
                            tv82.setText(SPHelper.getString(context, "name"));
                            tv91.setText(station.getBom().getProcessHalf().getSuccessNum() + "");
                            tv92.setText(station.getBom().getProcessHalf().getFailureNum() + "");
                            //                            上工序不为空显示最后一行以及扫描上工序料框按钮
                            if (station.getBom().getProcessHalf().getPrev() != null) {
                                row10.setVisibility(View.VISIBLE);
                                btnScannLast.setVisibility(View.VISIBLE);
                                tv101.setText(station.getBom().getProcessHalf().getPrev().getName());
                                tv102.setText(station.getBom().getProcessHalf().getPrev().getSuccessNum() + "");
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

    @OnClick({R.id.left_arrow, R.id.btn, R.id.btnScannLast})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_arrow:
                finish();
                break;
            case R.id.btnScannLast:
                Intent intent1 = new Intent(context, CaptureActivity.class);
                intent1.putExtra("FLAG", 666);//跳转标志位
                intent1.putExtra("planId", planId);
                intent1.putExtra("bomId", bomId);
                intent1.putExtra("processId", processId);
                intent1.putExtra("stationId", stationId);
                startActivity(intent1);
                break;
            case R.id.btn:
                Intent intent = new Intent(context, CaptureActivity.class);
                intent.putExtra("FLAG", 50);//跳转标志位
                intent.putExtra("planId", planId);
                intent.putExtra("bomId", bomId);
                intent.putExtra("processId", processId);
                intent.putExtra("stationId", stationId);
                startActivity(intent);
                break;
        }
    }
}
