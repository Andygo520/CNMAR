package com.example.administrator.cnmar.activity;

import android.content.Context;
import android.content.Intent;
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
import com.example.administrator.cnmar.R;
import com.example.administrator.cnmar.entity.MyListView;
import com.example.administrator.cnmar.helper.UniversalHelper;
import com.example.administrator.cnmar.helper.UrlHelper;
import com.example.administrator.cnmar.http.VolleyHelper;

import java.text.SimpleDateFormat;
import java.util.List;

import component.custom.model.CustomDeliverPlan;
import component.custom.model.CustomDeliverProduct;

public class DeliveryPlanDetailActivity extends AppCompatActivity {

    private TextView tvPlanCode, tvDeliveryOrder, tvDeliveryDate, tvProductOutOrder;
    private TextView name1, name2, name3, name4;
    private MyListView listView;
    private static String strUrl;
    private LinearLayout llLeftArrow;
    private TextView tvTitle;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_plan_detail);

        init();
        id = getIntent().getIntExtra("ID", 0);
        strUrl = UrlHelper.URL_DELIVERY_PLAN_DETAIL.replace("{ID}", String.valueOf(id));
        strUrl = UniversalHelper.getTokenUrl(strUrl);
        getListFromNet();
    }

    public void init() {
        tvTitle = (TextView) findViewById(R.id.title);
        tvTitle.setText("销售管理");
        llLeftArrow = (LinearLayout) findViewById(R.id.left_arrow);
        llLeftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeliveryPlanDetailActivity.this, PlanManageActivity.class);
                intent.putExtra("flag", 2);
                startActivity(intent);
            }
        });


        name1 = (TextView) findViewById(R.id.column1);
        name2 = (TextView) findViewById(R.id.column2);
        name3 = (TextView) findViewById(R.id.column3);
        name4 = (TextView) findViewById(R.id.column4);
        name1.setText("成品编码");
        name2.setText("成品名称");
        name4.setText("交付数量");

        tvPlanCode = (TextView) findViewById(R.id.tv11);
        tvDeliveryOrder = (TextView) findViewById(R.id.tv12);
        tvDeliveryDate = (TextView) findViewById(R.id.tv21);
        tvProductOutOrder = (TextView) findViewById(R.id.tv22);



        listView = (MyListView) findViewById(R.id.lvTable);
//        listView.addFooterView(new ViewStub(this));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(this, PlanManageActivity.class);
            intent.putExtra("flag", 1);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void getListFromNet() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue queue = Volley.newRequestQueue(DeliveryPlanDetailActivity.this);
                StringRequest stringRequest = new StringRequest(strUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        String json = VolleyHelper.getJson(s);
                        component.common.model.Response response = JSON.parseObject(json, component.common.model.Response.class);
                        CustomDeliverPlan deliverPlan = JSON.parseObject(response.getData().toString(), CustomDeliverPlan.class);
//                        得到列表的数据源
                        List<CustomDeliverProduct> list = deliverPlan.getDeliverProducts();

//                       如果有成品出库单就显示规格，没有就显示库存数量
                        if (deliverPlan.getProductOutOrder()==null){
                            name3.setText("库存数量");
                            MyAdapter1 myAdapter = new MyAdapter1(DeliveryPlanDetailActivity.this, list);
                            listView.setAdapter(myAdapter);
                        }else{
                            name3.setText("规格");
                            MyAdapter myAdapter = new MyAdapter(DeliveryPlanDetailActivity.this, list);
                            listView.setAdapter(myAdapter);
                        }


                        tvPlanCode.setText(deliverPlan.getCode());
                        tvDeliveryOrder.setText(deliverPlan.getDeliverOrder());

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                        tvDeliveryDate.setText(sdf.format(deliverPlan.getDeliverDate()));

                        if (deliverPlan.getProductOutOrder()==null){
                            tvProductOutOrder.setText("");
                        }else
                            tvProductOutOrder.setText(deliverPlan.getProductOutOrder().getCode());


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
        private List<CustomDeliverProduct> list = null;


        public MyAdapter(Context context, List<CustomDeliverProduct> list) {
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
                holder.tvProductCode = (TextView) convertView.findViewById(R.id.column1);
                holder.tvProductName = (TextView) convertView.findViewById(R.id.column2);
                holder.tvSize = (TextView) convertView.findViewById(R.id.column3);
                holder.tvNum = (TextView) convertView.findViewById(R.id.column4);

                convertView.setTag(holder);
            } else
                holder = (ViewHolder) convertView.getTag();

            holder.tvProductCode.setText(list.get(position).getProduct().getCode());
            holder.tvProductName.setText(list.get(position).getProduct().getName());
            holder.tvSize.setText(list.get(position).getProduct().getSpec());
            holder.tvNum.setText(String.valueOf(list.get(position).getDeliverNum())+list.get(position).getProduct().getUnit().getName());

            return convertView;
        }

        public class ViewHolder {
            TextView tvProductCode;
            TextView tvProductName;
            TextView tvSize;
            TextView tvNum;
        }
    }

    public class MyAdapter1 extends BaseAdapter {
        private Context context;
        private List<CustomDeliverProduct> list = null;


        public MyAdapter1(Context context, List<CustomDeliverProduct> list) {
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
                holder.tvProductCode = (TextView) convertView.findViewById(R.id.column1);
                holder.tvProductName = (TextView) convertView.findViewById(R.id.column2);
                holder.tvStockNum = (TextView) convertView.findViewById(R.id.column3);
                holder.tvNum = (TextView) convertView.findViewById(R.id.column4);

                convertView.setTag(holder);
            } else
                holder = (ViewHolder) convertView.getTag();

            holder.tvProductCode.setText(list.get(position).getProduct().getCode());
            holder.tvProductName.setText(list.get(position).getProduct().getName());
            holder.tvStockNum.setText(String.valueOf(list.get(position).getProduct().getStock().getStock())+list.get(position).getProduct().getUnit().getName());
            holder.tvNum.setText(String.valueOf(list.get(position).getDeliverNum())+list.get(position).getProduct().getUnit().getName());

            return convertView;
        }

        public class ViewHolder {
            TextView tvProductCode;
            TextView tvProductName;
            TextView tvStockNum;
            TextView tvNum;
        }
    }
}
