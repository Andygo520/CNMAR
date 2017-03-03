package com.example.administrator.cnmar.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
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

public class InBoxActivity extends AppCompatActivity {

    private Context context;
    private int receiveId, stationId, boxId;
    @BindView(R.id.left_arrow)
    LinearLayout leftArrow;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tvTableTitle)
    TextView tvTableTitle;
    @BindView(R.id.tvTableTitle1)
    TextView tvTableTitle1;
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
    @BindView(R.id.et52)
    EditText et52;
    @BindView(R.id.btn)
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scann_box);
        ButterKnife.bind(this);
        AppExit.getInstance().addActivity(this);

        init();
        String url = getIntent().getStringExtra("result");
        receiveId = getIntent().getIntExtra("receiveId",0);
        stationId = getIntent().getIntExtra("stationId",0);
        url = UniversalHelper.getTokenUrl(url);
        getTableInfoFromNet(url);
    }

    public void init() {
        context = InBoxActivity.this;
        tvTableTitle1.setText("料框");
        title.setText("生产管理");

        name21.setText("机台工位");
        name22.setText("车间");
        name31.setText("工序");
        name32.setText("工装");
        name41.setText("料框编码");
        name42.setText("类型");
        name51.setText("现存数量");
        name52.setText("入框数量");
        et52.setFocusable(false);
        et52.setFocusableInTouchMode(true);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void sendRequest(final String url){
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue queue = Volley.newRequestQueue(context);
                StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        String json = VolleyHelper.getJson(s);
                        component.common.model.Response response = JSON.parseObject(json, component.common.model.Response.class);
                        if (response.isStatus()){
                            finish();
                        }else
                            Toast.makeText(context,response.getMsg(),Toast.LENGTH_SHORT).show();

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

                        boxId=comBox.getId();
//                        加工单详情
                        if (comBox.getReceive().getPlan() != null) {
                            name11.setText("加工单编号");
                            name12.setText("成品编码");
                            tvTableTitle.setText("加工单");

                            tv11.setText(comBox.getReceive().getPlan().getCode());
                            tv12.setText(comBox.getReceive().getPlan().getProduct().getCode());
                            tv21.setText(comBox.getReceive().getProcessProduct().getStation().getName());
                            tv22.setText(comBox.getReceive().getProcessProduct().getStation().getWorkshop().getName());
                            tv31.setText(comBox.getReceive().getProcessProduct().getName());
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
                            tv41.setText(comBox.getCode());
                            tv42.setText(comBox.getBoxTypeVo().getValue());
                            tv51.setText(comBox.getNum() + "");//现存数量
                        }
//                       子加工单详情
                        else {
                            name11.setText("子加工单编号");
                            name12.setText("半成品编码");
                            tvTableTitle.setText("子加工单");

                            tv11.setText(comBox.getReceive().getBom().getCode());
                            tv12.setText(comBox.getReceive().getBom().getHalf().getCode());
                            tv21.setText(comBox.getReceive().getProcessHalf().getStation().getName());
                            tv22.setText(comBox.getReceive().getProcessHalf().getStation().getWorkshop().getName());
                            tv31.setText(comBox.getReceive().getProcessHalf().getName());
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
                            tv41.setText(comBox.getCode());
                            tv42.setText(comBox.getBoxTypeVo().getValue());
                            tv51.setText(comBox.getNum() + "");//现存数量
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

    @OnClick({R.id.left_arrow, R.id.btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_arrow:
                finish();
                break;
            case R.id.btn:
                new AlertDialog.Builder(context)
                        .setTitle("系统提示")
                        .setMessage("确认入料框吗？")
                        .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String inCommit=et52.getText().toString().trim();
                                String commit_url= UrlHelper.URL_IN_BOX.replace("{boxId}",boxId+"").replace("{receiveId}",receiveId+"")
                                        .replace("{stationId}",stationId+"").replace("{num}",inCommit)
                                        .replace("{userId}", SPHelper.getInt(context,"userId")+"");
                                Log.d("commit_url",commit_url);
                                commit_url=UniversalHelper.getTokenUrl(commit_url);
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

}
