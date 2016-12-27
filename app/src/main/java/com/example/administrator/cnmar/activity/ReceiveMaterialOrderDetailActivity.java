package com.example.administrator.cnmar.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
import com.example.administrator.cnmar.entity.MyListView;
import com.example.administrator.cnmar.helper.UniversalHelper;
import com.example.administrator.cnmar.helper.UrlHelper;
import com.example.administrator.cnmar.helper.VolleyHelper;

import java.text.SimpleDateFormat;
import java.util.List;

import component.basic.vo.PackTypeVo;
import component.material.model.MaterialOutOrder;
import component.material.model.MaterialOutOrderMaterial;
import component.material.vo.OutOrderStatusVo;

public class ReceiveMaterialOrderDetailActivity extends AppCompatActivity {
    private TextView tvReceiveMaterialOrder, tvTime, tvProduceNo, tvProduceName, tvProductCode,
            tvProductName, tvUnit, tvProduceNum, tvBeginDate, tvEndDate, tvPerson, tvStatus;
    private TextView name1, name2, name3, name4;
    private MyListView listView;
    private static String strUrl;
    private LinearLayout llLeftArrow;
    private TextView tvTitle;
    private int id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_material_order_detail);
        id = getIntent().getIntExtra("ID", 0);
        AppExit.getInstance().addActivity(this);
        //   从登陆页面取出用户的角色信息
//        role = LoginActivity.sp.getString("Role", "123");
//        isSuper = LoginActivity.sp.getBoolean("isSuper", false);

        init();
        strUrl = UrlHelper.URL_RECEIVE_MATERIAL_ORDER_DETAIL.replace("{ID}", String.valueOf(id));
        strUrl = UniversalHelper.getTokenUrl(strUrl);
        getListFromNet();
    }

    public void init() {
        tvTitle = (TextView) findViewById(R.id.title);
        tvTitle.setText("领料单详情");
        llLeftArrow = (LinearLayout) findViewById(R.id.left_arrow);
        llLeftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        name1 = (TextView) findViewById(R.id.column1);
        name2 = (TextView) findViewById(R.id.column2);
        name3 = (TextView) findViewById(R.id.column3);
        name4 = (TextView) findViewById(R.id.column4);
        name1.setText("原料编码");
        name2.setText("原料名称");
        name3.setText("规格");

        tvReceiveMaterialOrder = (TextView) findViewById(R.id.tv11);
        tvTime = (TextView) findViewById(R.id.tv12);
        tvProduceNo = (TextView) findViewById(R.id.tv21);
        tvProduceName = (TextView) findViewById(R.id.tv22);
        tvProductCode = (TextView) findViewById(R.id.tv31);
        tvProductName = (TextView) findViewById(R.id.tv32);
        tvProduceNum = (TextView) findViewById(R.id.tv41);
        tvUnit = (TextView) findViewById(R.id.tv42);
        tvBeginDate = (TextView) findViewById(R.id.tv51);
        tvEndDate = (TextView) findViewById(R.id.tv52);
        tvPerson = (TextView) findViewById(R.id.tv61);
        tvStatus = (TextView) findViewById(R.id.tv62);


        listView = (MyListView) findViewById(R.id.lvTable);
//        listView.addFooterView(new ViewStub(this));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    public void getListFromNet() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue queue = Volley.newRequestQueue(ReceiveMaterialOrderDetailActivity.this);
                StringRequest stringRequest = new StringRequest(strUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        String json = VolleyHelper.getJson(s);
                        component.common.model.Response response = JSON.parseObject(json, component.common.model.Response.class);
                        MaterialOutOrder materialOutOrder = JSON.parseObject(response.getData().toString(), MaterialOutOrder.class);
//                        得到列表的数据源
                        List<MaterialOutOrderMaterial> list = materialOutOrder.getOutOrderMaterials();

//                       未领料显示待出库数量，领料后显示出库数量
                        if (materialOutOrder.getStatus() == OutOrderStatusVo.pre_out_stock.getKey()) {
                            name4.setText("待领料数量");
                            tvStatus.setText("未领料");
                            MyAdapter myAdapter = new MyAdapter(ReceiveMaterialOrderDetailActivity.this, list);
                            listView.setAdapter(myAdapter);
                        } else {
                            name4.setText("已领料数量");
                            tvStatus.setText("已领料");
                            MyAdapter1 myAdapter = new MyAdapter1(ReceiveMaterialOrderDetailActivity.this, list);
                            listView.setAdapter(myAdapter);
                        }


                        tvReceiveMaterialOrder.setText(materialOutOrder.getCode());

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

//                        只有已领料才有领料时间，否则设置为空
                        if (materialOutOrder.getOutTime() == null)
                            tvTime.setText("");
                        else
                            tvTime.setText(sdf1.format(materialOutOrder.getOutTime()));


                        tvProduceNo.setText(materialOutOrder.getProducePlan().getCode());
                        tvProduceName.setText(materialOutOrder.getProducePlan().getName());
                        tvProductCode.setText(materialOutOrder.getProducePlan().getProduct().getCode());
                        tvProductName.setText(materialOutOrder.getProducePlan().getProduct().getName());
//                        有包装的，单位格式为“9个/袋”
                        if (materialOutOrder.getProducePlan().getProduct().getPackType() != PackTypeVo.empty.getKey())
                            tvUnit.setText(materialOutOrder.getProducePlan().getProduct().getPackNum()
                                    + materialOutOrder.getProducePlan().getProduct().getUnit().getName()
                                    + " / " + materialOutOrder.getProducePlan().getProduct().getPackTypeVo().getValue().substring(1, 2));
                        else
                            tvUnit.setText(materialOutOrder.getProducePlan().getProduct().getUnit().getName());

                        tvProduceNum.setText(String.valueOf(materialOutOrder.getProducePlan().getProduceNum()) + materialOutOrder.getProducePlan().getProduct().getUnit().getName());
//   起始日期、结束日期的非空判断
                        if (materialOutOrder.getProducePlan().getStartDate() != null)
                            tvBeginDate.setText(sdf.format(materialOutOrder.getProducePlan().getStartDate()));
                        else
                            tvBeginDate.setText("");

                        if (materialOutOrder.getProducePlan().getEndDate() != null)
                            tvEndDate.setText(sdf.format(materialOutOrder.getProducePlan().getEndDate()));
                        else
                            tvEndDate.setText("");

                        tvPerson.setText(materialOutOrder.getReceive().getName());

////     只有原料出库单状态为空并且用户为超级管理员或车间班组长，才显示“领料”按钮
//                        if (materialOutOrder.getMaterialOutOrder() == null && (role.contains("车间班组长") || isSuper)) {
//                            tvPerson.setText("");
//                            btn.setVisibility(View.VISIBLE);
//                            btn.setText("领料");
//                            etSuccessNum.setFocusable(false);
//                            etSuccessNum.setFocusableInTouchMode(false);
//                        } else
//                            tvPerson.setText(materialOutOrder.getMaterialOutOrder().getCode());
////     只有在原料出库单状态为已出库(或未全部出库)并且成品入库单为空的状态下，才显示“提交待入库”按钮
//                        if (materialOutOrder.getProductInOrder() == null) {
//                            tvStatus.setText("");
//                            if (materialOutOrder.getMaterialOutOrder() != null && (materialOutOrder.getMaterialOutOrder().getStatus() == OutOrderStatusVo.NOT_ALL.getKey() || materialOutOrder.getMaterialOutOrder().getStatus() == OutOrderStatusVo.OUT_STOCK.getKey()) && (role.contains("检验员") || isSuper)) {
//                                btn.setVisibility(View.VISIBLE);
//                                btn.setText("提交待入库");
//                                etSuccessNum.setFocusableInTouchMode(true);
//                            }
//
//                        } else {
//                            tvStatus.setText(materialOutOrder.getProductInOrder().getCode());
//                            etSuccessNum.setText(String.valueOf(materialOutOrder.getSuccessNum()));
//                            etSuccessNum.setTextColor(getResources().getColor(R.color.color_red));
//                        }
//

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
        private List<MaterialOutOrderMaterial> list = null;


        public MyAdapter(Context context, List<MaterialOutOrderMaterial> list) {
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
                holder.tvPreNum = (TextView) convertView.findViewById(R.id.column4);

                convertView.setTag(holder);
            } else
                holder = (ViewHolder) convertView.getTag();

            holder.tvMaterialCode.setText(list.get(position).getMaterial().getCode());
            holder.tvMaterialName.setText(list.get(position).getMaterial().getName());
            holder.tvSize.setText(list.get(position).getMaterial().getSpec());
            holder.tvPreNum.setText(String.valueOf(list.get(position).getPreOutStock()) + list.get(position).getMaterial().getUnit().getName());

            return convertView;
        }

        public class ViewHolder {
            TextView tvMaterialCode;
            TextView tvMaterialName;
            TextView tvSize;
            TextView tvPreNum;
        }
    }

    public class MyAdapter1 extends BaseAdapter {
        private Context context;
        private List<MaterialOutOrderMaterial> list = null;


        public MyAdapter1(Context context, List<MaterialOutOrderMaterial> list) {
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
            holder.tvNum.setText(String.valueOf(list.get(position).getOutStock()) + list.get(position).getMaterial().getUnit().getName());

            return convertView;
        }

        public class ViewHolder {
            TextView tvMaterialCode;
            TextView tvMaterialName;
            TextView tvSize;
            TextView tvNum;
        }
    }
}
