package com.example.administrator.cnmar.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.DefaultRetryPolicy;
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
import component.produce.model.ProduceBom;
import component.produce.model.ProducePlan;

public class ProductQualityControlDetailActivity extends AppCompatActivity {
    private Context context;
    private TextView tvPlanCode, tvPlanName, tvProductCode, tvProductName, tvSize, tvCheckTime,
            tvUnit, tvProduceNum, tvCheckMan, tvBeginDate, tvEndDate, tvMaterialOutOrder, tvProductInOrder;
    private TextView tvSuccessNum, tvActualNum;
    private TextView name1, name2, name3, name4;
    private MyListView listView;
    private String strUrl;
    private LinearLayout llLeftArrow;
    private TextView tvTitle;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_quality_control_detail);
        id = getIntent().getIntExtra("ID", 0);

        AppExit.getInstance().addActivity(this);
        init();
        strUrl = UrlHelper.URL_PRODUCT_QC_DETAIL.replace("{ID}", String.valueOf(id));
        strUrl = UniversalHelper.getTokenUrl(strUrl);
        getListFromNet();
    }

    public void init() {
        context = ProductQualityControlDetailActivity.this;
        tvTitle = (TextView) findViewById(R.id.title);
        tvTitle.setText("成品检验-详情");
        llLeftArrow = (LinearLayout) findViewById(R.id.left_arrow);
        llLeftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductQualityControlDetailActivity.this.finish();//结束当前页面
            }
        });


        name1 = (TextView) findViewById(R.id.column1);
        name2 = (TextView) findViewById(R.id.column2);
        name3 = (TextView) findViewById(R.id.column3);
        name4 = (TextView) findViewById(R.id.column4);
        name1.setText("原料编码");
        name2.setText("原料名称");
        name3.setText("规格");
        name4.setText("领料数量");

        tvPlanCode = (TextView) findViewById(R.id.tv11);
        tvPlanName = (TextView) findViewById(R.id.tv12);
        tvProductCode = (TextView) findViewById(R.id.tv21);
        tvProductName = (TextView) findViewById(R.id.tv22);
        tvSize = (TextView) findViewById(R.id.tv31);
        tvUnit = (TextView) findViewById(R.id.tv32);
        tvProduceNum = (TextView) findViewById(R.id.tv41);
        tvCheckMan = (TextView) findViewById(R.id.tv51);
        tvBeginDate = (TextView) findViewById(R.id.tv61);
        tvEndDate = (TextView) findViewById(R.id.tv62);
        tvMaterialOutOrder = (TextView) findViewById(R.id.tv71);
        tvProductInOrder = (TextView) findViewById(R.id.tv72);
        tvCheckTime = (TextView) findViewById(R.id.tvCheckTime);
        tvActualNum = (TextView) findViewById(R.id.tv42);
        tvSuccessNum = (TextView) findViewById(R.id.tv52);


        listView = (MyListView) findViewById(R.id.lvTable);
//        listView.addFooterView(new ViewStub(this));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();//结束当前页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void sendRequest(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue queue = Volley.newRequestQueue(context);
                StringRequest request = new StringRequest(url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        UniversalHelper.dismissProgressDialog();
                        String json = VolleyHelper.getJson(s);
                        Log.d("production", json);
                        component.common.model.Response response = JSON.parseObject(json, component.common.model.Response.class);
                        if (!response.isStatus()) {
                            Toast.makeText(context, response.getMsg(), Toast.LENGTH_LONG).show();
                        } else {
                            Intent intent = new Intent(context, PlanManageActivity.class);
                            startActivity(intent);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        UniversalHelper.dismissProgressDialog();
                        Toast.makeText(context, "连接超时", Toast.LENGTH_LONG).show();
                    }
                });
//                重设Volley请求超时时间
                request.setRetryPolicy(new DefaultRetryPolicy(1000 * 1000, 0, 0f));
                queue.add(request);
            }
        }).start();
    }

    public void getListFromNet() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue queue = Volley.newRequestQueue(context);
                StringRequest stringRequest = new StringRequest(strUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        String json = VolleyHelper.getJson(s);
                        Log.d("production", json);
                        component.common.model.Response response = JSON.parseObject(json, component.common.model.Response.class);
                        ProducePlan producePlan = JSON.parseObject(response.getData().toString(), ProducePlan.class);
//                        得到列表的数据源
                        List<ProduceBom> list = producePlan.getProduceBoms();

                        MyAdapter myAdapter = new MyAdapter(context, list);
                        listView.setAdapter(myAdapter);

                        tvPlanCode.setText(producePlan.getCode());
                        tvPlanName.setText(producePlan.getName());
                        tvProductCode.setText(producePlan.getProduct().getCode());
                        tvProductName.setText(producePlan.getProduct().getName());
                        tvSize.setText(producePlan.getProduct().getSpec());
//                        有包装的，单位格式为“9个/袋”
                        if (producePlan.getProduct().getPackType() != PackTypeVo.empty.getKey())
                            tvUnit.setText(producePlan.getProduct().getPackNum()
                                    + producePlan.getProduct().getUnit().getName()
                                    + " / " + producePlan.getProduct().getPackTypeVo().getValue().substring(1, 2));
                        else
                            tvUnit.setText(producePlan.getProduct().getUnit().getName());


                        tvCheckMan.setText(producePlan.getTest().getName());

                        tvProduceNum.setText(producePlan.getProduceNum() + producePlan.getProduct().getUnit().getName());

                        tvSuccessNum.setText("");

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

//                      开始日期、结束日期的非空判断
                        if (producePlan.getStartDate() == null)
                            tvBeginDate.setText("");
                        else
                            tvBeginDate.setText(sdf.format(producePlan.getStartDate()));
                        if (producePlan.getEndDate() == null)
                            tvEndDate.setText("");
                        else
                            tvEndDate.setText(sdf.format(producePlan.getEndDate()));

                        tvMaterialOutOrder.setText(producePlan.getReceive().getCode());
                        tvActualNum.setText(producePlan.getActualNum() + "");
                        tvProductInOrder.setText(producePlan.getProductInOrder().getCode());
                        tvSuccessNum.setText(String.valueOf(producePlan.getSuccessNum()));

                        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        tvCheckTime.setText(sdf1.format(producePlan.getTestTime()));

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
}
