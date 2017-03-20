package com.example.administrator.cnmar.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.example.administrator.cnmar.helper.UniversalHelper;
import com.example.administrator.cnmar.helper.UrlHelper;
import com.example.administrator.cnmar.helper.VolleyHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import component.com.model.ComBox;
import component.com.model.ComTool;


public class PrevProcessOutBoxActivity extends AppCompatActivity {

    @BindView(R.id.tv)
    TextView tv;
    private Context context;
    private int boxId;
    private int currentNum;//现存数量
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
    @BindView(R.id.btn)
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_process_out_box);
        ButterKnife.bind(this);
        AppExit.getInstance().addActivity(this);

        init();
        String url = getIntent().getStringExtra("result");
        url = UniversalHelper.getTokenUrl(url);
        getTableInfoFromNet(url);
    }

    private void init() {
        context = PrevProcessOutBoxActivity.this;
        title.setText("上工序合格品出料框");
        name21.setText("工序");
        name22.setText("车间");
        name31.setText("机台工位");
        name32.setText("工装");
        name41.setText("上一道工序");
        name42.setText("机台工位");
        name51.setText("料框编码");
        name52.setText("类型");
        name61.setText("现存数量");
        name62.setText("出框数量");
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
                        ComBox box = JSON.parseObject(response.getData().toString(), ComBox.class);
                        boxId = box.getId();
//                        加工单详情
                        if (box.getReceive().getPlan() != null) {
                            tv.setText("加工单");
                            name11.setText("加工单编号");
                            name12.setText("成品编码");
                            tv11.setText(box.getReceive().getPlan().getCode());
                            tv12.setText(box.getReceive().getPlan().getProduct().getCode());
                            tv21.setText(box.getReceive().getProcessProduct().getName());
                            tv22.setText(box.getReceive().getProcessProduct().getStation().getWorkshop().getName());
                            tv31.setText(box.getReceive().getProcessProduct().getStation().getName());
//                            得到工装列表
                            List<ComTool> tools = box.getReceive().getProcessProduct().getTools();
                            if (tools.size() == 0) {
                                tv32.setText("");
                            } else {
                                String toolNames = "";
                                for (ComTool tool : tools) {
                                    toolNames += tool.getName() + "，";
                                }
                                tv32.setText(toolNames.substring(0, toolNames.length() - 1));
                            }
                            tv41.setText(box.getReceive().getProcessProduct().getPrev().getName());
                            tv42.setText(box.getReceive().getProcessProduct().getPrev().getStation().getName());
                        }
//                       子加工单详情
                        else {
                            tv.setText("子加工单");
                            name11.setText("子加工单编号");
                            name12.setText("半成品编码");
                            tv11.setText(box.getReceive().getBom().getCode());
                            tv12.setText(box.getReceive().getBom().getHalf().getCode());
                            tv21.setText(box.getReceive().getProcessHalf().getName());
                            tv22.setText(box.getReceive().getProcessHalf().getStation().getWorkshop().getName());
                            tv31.setText(box.getReceive().getProcessHalf().getStation().getName());
                            //                       得到工装列表
                            List<ComTool> tools = box.getReceive().getProcessHalf().getTools();
                            if (tools.size() == 0) {
                                tv32.setText("");
                            } else {
                                String toolNames = "";
                                for (ComTool tool : tools) {
                                    toolNames += tool.getName() + "，";
                                }
                                tv32.setText(toolNames.substring(0, toolNames.length() - 1));
                            }
                            tv41.setText(box.getReceive().getProcessHalf().getPrev().getName());
                            tv42.setText(box.getReceive().getProcessHalf().getPrev().getStation().getName());
                        }
                        tv51.setText(box.getCode());
                        tv52.setText(box.getBoxTypeVo().getValue());
                        currentNum = box.getNum();
                        tv61.setText(currentNum + "");
//                        默认让取出数量等于现存数
                        et62.setText(currentNum + "");
                        et62.setSelection(String.valueOf(currentNum).length());//将光标移到文本末尾
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
                new AlertDialog.Builder(context)
                        .setTitle("系统提示")
                        .setMessage("确认出料框吗？")
                        .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String num = et62.getText().toString().trim();
                                if (num==null || num.equals("")){
                                    Toast.makeText(context, "请输入出框数量", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (Integer.parseInt(num) > currentNum) {
                                    Toast.makeText(context, "出框数量应小于现存数量", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                String commit_url = UrlHelper.URL_PREV_OUT_COMMIT.replace("{boxId}", boxId + "").replace("{num}", num);
                                Log.d("commit_url", commit_url);
                                commit_url = UniversalHelper.getTokenUrl(commit_url);
                                sendRequest(commit_url);
                            }
                        })
                        .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).create().show();
                break;
        }
    }

    private void sendRequest(final String url) {
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
                            finish();
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
}
