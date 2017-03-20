package com.example.administrator.cnmar.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
import zxing.activity.CaptureActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProduceCheckFragment extends Fragment {
    private int flag;//区分加工单、子加工单的标志位
    private int testBoxId, stationId, processId, receiveId;
    private String codeUrl="";
    @BindView(R.id.btnScann)
    Button btnScann;
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
    @BindView(R.id.tvTableTitle1)
    TextView tvTableTitle1;
    @BindView(R.id.name51)
    TextView name51;
    @BindView(R.id.tv51)
    TextView tv51;
    @BindView(R.id.name52)
    TextView name52;
    @BindView(R.id.tv52)
    TextView tv52;
    @BindView(R.id.btn1)
    Button btn1;
    @BindView(R.id.btn2)
    Button btn2;
    @BindView(R.id.scrollView)
    ScrollView scrollView;

    public ProduceCheckFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_produce_check, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (codeUrl != null && !codeUrl.equals("")){
            getData(codeUrl);
        }
    }

    public void init() {
        name21.setText("工序");
        name22.setText("车间");
        name31.setText("机台工位");
        name32.setText("工装");
        name41.setText("合格品数量");
        name42.setText("不合格品数量");
        name51.setText("料框编码");
        name52.setText("现存数量");
        tvTableTitle1.setText("待检验料框");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //        扫码返回从后台取数据
        if (resultCode == 0) {
            codeUrl = data.getStringExtra("result");
            Log.d("codeUrlcodeUrl", codeUrl);
            getData(codeUrl);
        } else {

        }
    }

    private void getData(String codeUrl) {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(codeUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                String json = VolleyHelper.getJson(s);
                component.common.model.Response response = JSON.parseObject(json, component.common.model.Response.class);
                if (!response.isStatus()) {
                    Toast.makeText(getActivity(), response.getMsg(), Toast.LENGTH_SHORT).show();
                } else {
//                        btnScann.setVisibility(View.GONE);//有返回数据的时候，不显示扫描按钮
                    scrollView.setVisibility(View.VISIBLE);
                    ComBox comBox = JSON.parseObject(response.getData().toString(), ComBox.class);
                    testBoxId = comBox.getId();
                    receiveId = comBox.getReceiveId();
                    stationId = comBox.getStationId();
                    processId = comBox.getProcessId();
//                        加工单详情
                    if (comBox.getReceive().getPlan() != null) {
                        flag = 0;
                        name11.setText("加工单编号");
                        name12.setText("成品编码");
                        tvTableTitle.setText("加工单");

                        tv11.setText(comBox.getReceive().getPlan().getCode());
                        tv12.setText(comBox.getReceive().getPlan().getProduct().getCode());
                        tv21.setText(comBox.getReceive().getProcessProduct().getName());
                        tv22.setText(comBox.getReceive().getProcessProduct().getStation().getWorkshop().getName());
                        tv31.setText(comBox.getReceive().getProcessProduct().getStation().getName());
//                               得到工装列表
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
                        tv41.setText(comBox.getReceive().getProcessProduct().getSuccessNum() + "");//合格品数量
                        tv42.setText(comBox.getReceive().getProcessProduct().getFailureNum() + "");//不合格品数量

                    }
//                       子加工单详情
                    else {
                        flag = 1;
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
                        tv41.setText(comBox.getReceive().getProcessHalf().getSuccessNum() + "");//合格品数量
                        tv42.setText(comBox.getReceive().getProcessHalf().getFailureNum() + "");//不合格品数量
                    }
                    tv51.setText(comBox.getCode());
                    tv52.setText(comBox.getNum() + "");//现存数量
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        queue.add(stringRequest);
    }

    public void sendRequest() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = UrlHelper.URL_FAIL_IN_BOX_COMMIT.replace("{boxId}", "0")
                        .replace("{testBoxId}", "" + testBoxId)
                        .replace("{receiveId}", "" + receiveId)
                        .replace("{processId}", "" + processId)
                        .replace("{stationId}", "" + stationId)
                        .replace("{testId}", "" + SPHelper.getInt(getActivity(), "userId"))
                        .replace("{failNum}", "0")
                        .replace("{reason}", "");
                url = UniversalHelper.getTokenUrl(url);
                RequestQueue queue = Volley.newRequestQueue(getActivity());
                StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        String json = VolleyHelper.getJson(s);
                        component.common.model.Response response = JSON.parseObject(json, component.common.model.Response.class);
                        if (!response.isStatus()) {
                            Toast.makeText(getActivity(), response.getMsg(), Toast.LENGTH_SHORT).show();
                        }else
                            Toast.makeText(getActivity(), "操作成功", Toast.LENGTH_SHORT).show();

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

    @OnClick({R.id.btnScann, R.id.btn1, R.id.btn2})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnScann:
                Intent intent = new Intent(getActivity(), CaptureActivity.class);
                intent.putExtra("FLAG", -50);//作为跳转到扫描页面的标志位
                startActivityForResult(intent, 1);
                break;
            case R.id.btn1:
                new AlertDialog.Builder(getActivity())
                        .setTitle("系统提示")
                        .setMessage("确定都合格吗？")
                        .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sendRequest();
                            }
                        })
                        .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).create().show();
                break;
            case R.id.btn2:
                Intent intent1 = new Intent(getActivity(), CaptureActivity.class);
                intent1.putExtra("FLAG", -60);//作为跳转到扫描页面的标志位
                intent1.putExtra("testBoxId", testBoxId);
                startActivity(intent1);
                break;
        }
    }
}
