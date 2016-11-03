package com.example.administrator.cnmar.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
import com.example.administrator.cnmar.R;
import com.example.administrator.cnmar.entity.MyListView;
import com.example.administrator.cnmar.helper.UniversalHelper;
import com.example.administrator.cnmar.helper.UrlHelper;
import com.example.administrator.cnmar.http.VolleyHelper;

import java.text.SimpleDateFormat;
import java.util.List;

import component.material.vo.OutOrderStatusVo;
import component.produce.model.ProduceBom;
import component.produce.model.ProducePlan;

public class ProductionPlanDetailActivity extends AppCompatActivity {
    private TextView tvPlanCode, tvPlanName, tvProductCode, tvProductName, tvSize,
            tvUnit, tvProduceNum, tvBeginDate, tvEndDate, tvMaterialOutOrder, tvProductInOrder;
    private EditText etSuccessNum;
    private TextView name1, name2, name3, name4;
    private MyListView listView;
    private static String strUrl;
    private LinearLayout llLeftArrow;
    private TextView tvTitle;
    private int id, receiveId;
    private Button btn;
    private String successNum;

    private String role;
    private Boolean isSuper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_production_plan_detail);
        id = getIntent().getIntExtra("ID", 0);
        receiveId = LoginActivity.sp.getInt("userId", 0);

        //   从登陆页面取出用户的角色信息
        role = LoginActivity.sp.getString("Role", "123");
        isSuper = LoginActivity.sp.getBoolean("isSuper", false);

        init();
        strUrl = UrlHelper.URL_PRODUCE_PLAN_DETAIL.replace("{ID}", String.valueOf(id));
        strUrl = UniversalHelper.getTokenUrl(strUrl);
        getListFromNet();
    }

    public void init() {
        tvTitle = (TextView) findViewById(R.id.title);
        tvTitle.setText("加工单管理");
        llLeftArrow = (LinearLayout) findViewById(R.id.left_arrow);
        llLeftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductionPlanDetailActivity.this, PlanManageActivity.class);
                intent.putExtra("flag", 0);
                startActivity(intent);
            }
        });

        btn = (Button) findViewById(R.id.btnSubmit);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn.getText().toString().equals("领料")) {
                    new AlertDialog.Builder(ProductionPlanDetailActivity.this)
                            .setTitle("系统提示")
                            .setMessage("确认领料且生成出库单？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String url = UrlHelper.URL_RECEIVE_MATERIAL_COMMIT.replace("{ID}", String.valueOf(id)).replace("{receiveId}", String.valueOf(receiveId));
                                    url = UniversalHelper.getTokenUrl(url);
                                    sendRequest(url);
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            }).create().show();


                } else if (btn.getText().toString().equals("提交待入库")) {
                    successNum = etSuccessNum.getText().toString().trim();
                    if (successNum.equals("")) {
                        Toast.makeText(ProductionPlanDetailActivity.this, "请输入合格品数量", Toast.LENGTH_SHORT).show();
                    } else {
                        String url = UrlHelper.URL_PRODUCT_PRE_IN_STOCK_COMMIT.replace("{ID}", String.valueOf(id)).replace("{successNum}", successNum);
                        url = UniversalHelper.getTokenUrl(url);
                        Log.d("jiagong",url);
                        sendRequest(url);
                    }
                }
            }
        });

        name1 = (TextView) findViewById(R.id.column1);
        name2 = (TextView) findViewById(R.id.column2);
        name3 = (TextView) findViewById(R.id.column3);
        name4 = (TextView) findViewById(R.id.column4);
        name1.setText("原料编码");
        name2.setText("原料名称");
        name4.setText("领料数量");

        tvPlanCode = (TextView) findViewById(R.id.tv11);
        tvPlanName = (TextView) findViewById(R.id.tv12);
        tvProductCode = (TextView) findViewById(R.id.tv21);
        tvProductName = (TextView) findViewById(R.id.tv22);
        tvSize = (TextView) findViewById(R.id.tv31);
        tvUnit = (TextView) findViewById(R.id.tv32);
        tvProduceNum = (TextView) findViewById(R.id.tv41);
        tvBeginDate = (TextView) findViewById(R.id.tv51);
        tvEndDate = (TextView) findViewById(R.id.tv52);
        tvMaterialOutOrder = (TextView) findViewById(R.id.tv61);
        tvProductInOrder = (TextView) findViewById(R.id.tv62);

        etSuccessNum = (EditText) findViewById(R.id.tv42);
        etSuccessNum.setFocusable(false);
        etSuccessNum.setFocusableInTouchMode(false);

        listView = (MyListView) findViewById(R.id.lvTable);
//        listView.addFooterView(new ViewStub(this));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(this, PlanManageActivity.class);
            intent.putExtra("flag", 0);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void sendRequest(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue queue = Volley.newRequestQueue(ProductionPlanDetailActivity.this);
                StringRequest request = new StringRequest(url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        String json = VolleyHelper.getJson(s);
                        Log.d("production", json);
                        component.common.model.Response response = JSON.parseObject(json, component.common.model.Response.class);
                        if (!response.isStatus()) {
                            Toast.makeText(ProductionPlanDetailActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent = new Intent(ProductionPlanDetailActivity.this, PlanManageActivity.class);
                            startActivity(intent);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                });
                queue.add(request);
            }
        }).start();
    }

    public void getListFromNet() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue queue = Volley.newRequestQueue(ProductionPlanDetailActivity.this);
                StringRequest stringRequest = new StringRequest(strUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        String json = VolleyHelper.getJson(s);
                        Log.d("production", json);
                        component.common.model.Response response = JSON.parseObject(json, component.common.model.Response.class);
                        ProducePlan producePlan = JSON.parseObject(response.getData().toString(), ProducePlan.class);
//                        得到列表的数据源
                        List<ProduceBom> list = producePlan.getProduceBoms();

//                       领料前显示库存数量，领料后显示规格
                        if (producePlan.getMaterialOutOrder() == null) {
                            name3.setText("库存数量");
                            MyAdapter1 myAdapter = new MyAdapter1(ProductionPlanDetailActivity.this, list);
                            listView.setAdapter(myAdapter);
                        } else {
                            name3.setText("规格");
                            MyAdapter myAdapter = new MyAdapter(ProductionPlanDetailActivity.this, list);
                            listView.setAdapter(myAdapter);
                        }


                        tvPlanCode.setText(producePlan.getCode());
                        tvPlanName.setText(producePlan.getName());
                        tvProductCode.setText(producePlan.getProduct().getCode());
                        tvProductName.setText(producePlan.getProduct().getName());
                        tvSize.setText(producePlan.getProduct().getSpec());
                        tvUnit.setText(producePlan.getProduct().getUnit().getName());
                        tvProduceNum.setText(String.valueOf(producePlan.getProduceNum()));

                        etSuccessNum.setText("");

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                        tvBeginDate.setText(sdf.format(producePlan.getStartDate()));
                        tvEndDate.setText(sdf.format(producePlan.getEndDate()));

//     只有原料出库单状态为空并且用户为超级管理员或车间班组长，才显示“领料”按钮
                        if (producePlan.getMaterialOutOrder() == null && (role.contains("车间班组长") || isSuper)) {
                            tvMaterialOutOrder.setText("");
                            btn.setVisibility(View.VISIBLE);
                            btn.setText("领料");
                            etSuccessNum.setFocusable(false);
                            etSuccessNum.setFocusableInTouchMode(false);
                        } else
                            tvMaterialOutOrder.setText(producePlan.getMaterialOutOrder().getCode());
//     只有在原料出库单状态为已出库(或未全部出库)并且成品入库单为空的状态下，才显示“提交待入库”按钮
                        if (producePlan.getProductInOrder() == null) {
                            tvProductInOrder.setText("");
                            if (producePlan.getMaterialOutOrder() != null && (producePlan.getMaterialOutOrder().getStatus() == OutOrderStatusVo.not_all.getKey() || producePlan.getMaterialOutOrder().getStatus() == OutOrderStatusVo.out_stock.getKey()) && (role.contains("检验员") || isSuper)) {
                                btn.setVisibility(View.VISIBLE);
                                btn.setText("提交待入库");
                                etSuccessNum.setFocusableInTouchMode(true);
                            }

                        } else {
                            tvProductInOrder.setText(producePlan.getProductInOrder().getCode());
                            etSuccessNum.setText(String.valueOf(producePlan.getSuccessNum()));
                            etSuccessNum.setTextColor(getResources().getColor(R.color.color_red));
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

    public class MyAdapter extends BaseAdapter {
        private Context context;
        private List<ProduceBom> list = null;


        public MyAdapter(Context context, List<ProduceBom> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.table_list_item, parent, false);
                holder = new ViewHolder();
                holder.tvMaterialCode = (TextView) convertView.findViewById(R.id.column1);
                holder.tvMaterialName = (TextView) convertView.findViewById(R.id.column2);
                holder.tvSize = (TextView) convertView.findViewById(R.id.column3);
                holder.tvNum = (TextView) convertView.findViewById(R.id.column4);

                convertView.setTag(holder);
            } else
                holder = (ViewHolder) convertView.getTag();

            holder.tvMaterialCode.setText(list.get(position).getMaterial().getCode());
            holder.tvMaterialName.setText(list.get(position).getMaterial().getName());
            holder.tvSize.setText(list.get(position).getMaterial().getSpec());
            holder.tvNum.setText(String.valueOf(list.get(position).getReceiveNum()) + list.get(position).getMaterial().getUnit().getName());

            return convertView;
        }

        public class ViewHolder {
            TextView tvMaterialCode;
            TextView tvMaterialName;
            TextView tvSize;
            TextView tvNum;
        }
    }

    public class MyAdapter1 extends BaseAdapter {
        private Context context;
        private List<ProduceBom> list = null;


        public MyAdapter1(Context context, List<ProduceBom> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.table_list_item, parent, false);
                holder = new ViewHolder();
                holder.tvMaterialCode = (TextView) convertView.findViewById(R.id.column1);
                holder.tvMaterialName = (TextView) convertView.findViewById(R.id.column2);
                holder.tvStockNum = (TextView) convertView.findViewById(R.id.column3);
                holder.tvNum = (TextView) convertView.findViewById(R.id.column4);

                convertView.setTag(holder);
            } else
                holder = (ViewHolder) convertView.getTag();

            holder.tvMaterialCode.setText(list.get(position).getMaterial().getCode());
            holder.tvMaterialName.setText(list.get(position).getMaterial().getName());
//         库存为空就直接设置为空字符
            if (list.get(position).getMaterial().getStock() != null)
                holder.tvStockNum.setText(String.valueOf(list.get(position).getMaterial().getStock().getStock()) + list.get(position).getMaterial().getUnit().getName());
            else
                holder.tvStockNum.setText("");

            holder.tvNum.setText(String.valueOf(list.get(position).getReceiveNum()) + list.get(position).getMaterial().getUnit().getName());

            return convertView;
        }

        public class ViewHolder {
            TextView tvMaterialCode;
            TextView tvMaterialName;
            TextView tvStockNum;
            TextView tvNum;
        }
    }


}
