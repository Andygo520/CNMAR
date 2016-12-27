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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import component.custom.model.CustomDeliverPlan;
import component.custom.model.CustomDeliverProduct;

public class DeliveryPlanDetailActivity extends AppCompatActivity {
    @BindView(R.id.left_arrow)
    LinearLayout llLeftArrow;
    @BindView(R.id.title)
    TextView tvTitle;
    @BindView(R.id.tvPlanCode)
    TextView tvPlanCode;
    @BindView(R.id.tvDeliveryOrder)
    TextView tvDeliveryOrder;
    @BindView(R.id.tvDeliveryDate)
    TextView tvDeliveryDate;
    @BindView(R.id.tvProductOutOrder)
    TextView tvProductOutOrder;
    @BindView(R.id.column1)
    TextView column1;
    @BindView(R.id.column2)
    TextView column2;
    @BindView(R.id.column3)
    TextView column3;
    @BindView(R.id.column4)
    TextView column4;
    @BindView(R.id.lvTable)
    MyListView lvTable;
    private String strUrl;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_plan_detail);
        ButterKnife.bind(this);
        AppExit.getInstance().addActivity(this);

        id = getIntent().getIntExtra("ID", 0);

        tvTitle.setText("销售管理");
        column1.setText("成品编码");
        column2.setText("成品名称");
        column4.setText("交付数量");
        strUrl = UrlHelper.URL_DELIVERY_PLAN_DETAIL.replace("{ID}", String.valueOf(id));
        strUrl = UniversalHelper.getTokenUrl(strUrl);
        getListFromNet();
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
                        if (deliverPlan.getProductOutOrder() == null) {
                            column3.setText("库存数量");
                            MyAdapter1 myAdapter = new MyAdapter1(DeliveryPlanDetailActivity.this, list);
                            lvTable.setAdapter(myAdapter);
                        } else {
                            column3.setText("规格");
                            MyAdapter myAdapter = new MyAdapter(DeliveryPlanDetailActivity.this, list);
                            lvTable.setAdapter(myAdapter);
                        }


                        tvPlanCode.setText(deliverPlan.getCode());
                        tvDeliveryOrder.setText(deliverPlan.getDeliverOrder());

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                        tvDeliveryDate.setText(sdf.format(deliverPlan.getDeliverDate()));

                        if (deliverPlan.getProductOutOrder() == null) {
                            tvProductOutOrder.setText("");
                        } else
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

    @OnClick(R.id.left_arrow)
    public void onClick() {
        finish();
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
            holder.tvNum.setText(String.valueOf(list.get(position).getDeliverNum()) + list.get(position).getProduct().getUnit().getName());

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

//          库存非空判断
            if (list.get(position).getProduct().getStock() != null)
                holder.tvStockNum.setText(String.valueOf(list.get(position).getProduct().getStock().getStock()) + list.get(position).getProduct().getUnit().getName());
            else
                holder.tvStockNum.setText("");

            holder.tvNum.setText(String.valueOf(list.get(position).getDeliverNum()) + list.get(position).getProduct().getUnit().getName());

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
