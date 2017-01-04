package com.example.administrator.cnmar.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
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
import com.example.administrator.cnmar.activity.DeliveryPlanDetailActivity;
import com.example.administrator.cnmar.activity.MaterialInOrderDetailActivity;
import com.example.administrator.cnmar.activity.MaterialOutOrderDetailActivity;
import com.example.administrator.cnmar.activity.ProductInOrderDetailActivity;
import com.example.administrator.cnmar.activity.ProductOutOrderDetailActivity;
import com.example.administrator.cnmar.activity.ProductionPlanDetailActivity;
import com.example.administrator.cnmar.entity.MyListView;
import com.example.administrator.cnmar.helper.VolleyHelper;

import java.util.List;

import component.material.model.MaterialInOrderSpace;
import component.material.model.MaterialOutOrder;
import component.product.model.ProductInOrderSpace;
import component.product.model.ProductOutOrder;
import zxing.activity.CaptureActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class TraceBackFragment extends Fragment {
    private Button btnScann;
    private LinearLayout  llMaterial, llProduct;
    private TextView tvMaterialCode, tvMaterialName, tvSpaceCode, tvSpaceName, tvMaterialInOrder,
            tvProductCode, tvProductName, tvSpaceCode1, tvSpaceName1, tvProductInOrder, tvPlan;
    private MyListView lvProduct, lvMaterial;
    private TextView tvTitle;

    public TraceBackFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trace_back, container, false);
        btnScann = (Button) view.findViewById(R.id.btnScann);
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        llMaterial = (LinearLayout) view.findViewById(R.id.llMaterial);
        llProduct = (LinearLayout) view.findViewById(R.id.llProduct);
        tvMaterialCode = (TextView) view.findViewById(R.id.tvMaterialCode);
        tvMaterialName = (TextView) view.findViewById(R.id.tvMaterialName);
        tvSpaceCode = (TextView) view.findViewById(R.id.tvSpaceCode);
        tvSpaceName = (TextView) view.findViewById(R.id.tvSpaceName);
        tvMaterialInOrder = (TextView) view.findViewById(R.id.tvMaterialInOrder);
        tvProductCode = (TextView) view.findViewById(R.id.tvProductCode);
        tvProductName = (TextView) view.findViewById(R.id.tvProductName);
        tvSpaceCode1 = (TextView) view.findViewById(R.id.tvSpaceCode1);
        tvSpaceName1 = (TextView) view.findViewById(R.id.tvSpaceName1);
        tvProductInOrder = (TextView) view.findViewById(R.id.tvProductInOrder);
        tvPlan = (TextView) view.findViewById(R.id.tvPlan);

        lvProduct = (MyListView) view.findViewById(R.id.lvProduct);
        lvMaterial = (MyListView) view.findViewById(R.id.lvMaterial);

        btnScann.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CaptureActivity.class);
                intent.putExtra("FLAG",100);//作为跳转到扫描页面的标志位
                startActivityForResult(intent, 0);
            }
        });
        return view;
    }
/*
* Fragment的onActivityResult方法可能不执行，需要重写其所属的Activity的onActivityResult方法即可
* */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        扫码返回从后台取数据
        if (resultCode == 0) {
            RequestQueue queue = Volley.newRequestQueue(getActivity());
            final String codeUrl = data.getStringExtra("result");
            StringRequest stringRequest = new StringRequest(codeUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    String json = VolleyHelper.getJson(s);
                    component.common.model.Response response = JSON.parseObject(json, component.common.model.Response.class);
                    if (!response.isStatus()) {
                        Toast.makeText(getActivity(), response.getMsg(), Toast.LENGTH_SHORT).show();
                    } else {
                        tvTitle.setVisibility(View.VISIBLE);
//                        追溯原料二维码
                        if (codeUrl.contains("material")) {
                            llMaterial.setVisibility(View.VISIBLE);
                            llProduct.setVisibility(View.GONE);
                             final MaterialInOrderSpace inOrderSpace = JSON.parseObject(response.getData().toString(), MaterialInOrderSpace.class);
                            tvMaterialCode.setText(inOrderSpace.getMaterial().getCode());
                            tvMaterialName.setText(inOrderSpace.getMaterial().getName());
                            tvSpaceCode.setText(inOrderSpace.getSpace().getCode());
                            tvSpaceName.setText(inOrderSpace.getSpace().getName());
                            tvMaterialInOrder.setText(inOrderSpace.getInOrder().getCode());
                            tvMaterialInOrder.setTextColor(getResources().getColor(R.color.colorBase));

                            tvMaterialInOrder.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent=new Intent(getActivity(), MaterialInOrderDetailActivity.class);
                                    intent.putExtra("ID",inOrderSpace.getInOrderId());
                                    startActivity(intent);
                                }
                            });
                            List<MaterialOutOrder> outOrderList = inOrderSpace.getOutOrders();
                            lvMaterial.setAdapter(new BillAdapter(outOrderList,getActivity()));
                        }
//                        追溯成品二维码
                        else {
                            llProduct.setVisibility(View.VISIBLE);
                            llMaterial.setVisibility(View.GONE);
                            final ProductInOrderSpace inOrderSpace = JSON.parseObject(response.getData().toString(), ProductInOrderSpace.class);
                            tvProductCode.setText(inOrderSpace.getProduct().getCode());
                            tvProductName.setText(inOrderSpace.getProduct().getName());
                            tvSpaceCode1.setText(inOrderSpace.getSpace().getCode());
                            tvSpaceName1.setText(inOrderSpace.getSpace().getName());
                            tvProductInOrder.setText(inOrderSpace.getInOrder().getCode());
                            tvProductInOrder.setTextColor(getResources().getColor(R.color.colorBase));
                            tvProductInOrder.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent=new Intent(getActivity(), ProductInOrderDetailActivity.class);
                                    intent.putExtra("ID",inOrderSpace.getInOrderId());
                                    startActivity(intent);
                                }
                            });
                            if (inOrderSpace.getInOrder().getProducePlan() != null){
                                tvPlan.setText(inOrderSpace.getInOrder().getProducePlan().getCode());
                                tvPlan.setTextColor(getResources().getColor(R.color.colorBase));
                                tvPlan.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent=new Intent(getActivity(), ProductionPlanDetailActivity.class);
                                        intent.putExtra("ID",inOrderSpace.getInOrder().getProducePlan().getId());
                                        intent.putExtra("FLAG",999);// 作为跳转到加工单详情页面的标志，不让用户操作，区别于“计划管理—加工单”
                                        startActivity(intent);
                                    }
                                });
                            }
                            else
                                tvPlan.setText("");
                            List<ProductOutOrder> outOrderList = inOrderSpace.getOutOrders();
                            lvProduct.setAdapter(new BillAdapter1(outOrderList,getActivity()));
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
//        若没有扫码直接返回就不做任何处理
        else if (resultCode == 1){

        }
    }

    /*
    * 原料追溯的适配器
    * */
    class BillAdapter extends BaseAdapter {
        private Context context;
        private List<MaterialOutOrder> list = null;

        public BillAdapter(List<MaterialOutOrder> list, Context context) {
            this.list = list;
            this.context = context;
        }

        public BillAdapter() {

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
                holder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.table_trace_list_item, parent, false);
                holder.tvMaterialOutOrder = (TextView) convertView.findViewById(R.id.column1);
                holder.materialOutOrder = (TextView) convertView.findViewById(R.id.column2);
                holder.tvPlan = (TextView) convertView.findViewById(R.id.column3);
                holder.plan = (TextView) convertView.findViewById(R.id.column4);
                convertView.setTag(holder);
            } else
                holder = (ViewHolder) convertView.getTag();

            holder.tvMaterialOutOrder.setText("原料出库单");
            holder.materialOutOrder.setText(list.get(position).getCode());
            holder.materialOutOrder.setTextColor(getResources().getColor(R.color.colorBase));
            holder.materialOutOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getActivity(), MaterialOutOrderDetailActivity.class);
                    intent.putExtra("ID", list.get(position).getId());
                    startActivity(intent);
                }
            });
            holder.tvPlan.setText("加工单");
            if (list.get(position).getProducePlan()!=null){
                holder.plan.setText(list.get(position).getProducePlan().getCode());
                holder.plan.setTextColor(getResources().getColor(R.color.colorBase));
                holder.plan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(getActivity(), ProductionPlanDetailActivity.class);
                        intent.putExtra("ID", list.get(position).getProducePlan().getId());
                        intent.putExtra("FLAG",999);// 作为跳转到加工单详情页面的标志，不让用户操作，区别于“计划管理—加工单”
                        startActivity(intent);
                    }
                });
            }else
                holder.plan.setText("");

            return convertView;
        }

        class ViewHolder {
            public TextView tvMaterialOutOrder;
            public TextView materialOutOrder;
            public TextView tvPlan;
            public TextView plan;
        }
    }

    /*
    * 成品追溯的适配器
    * */
    class BillAdapter1 extends BaseAdapter {
        private Context context;
        private List<ProductOutOrder> list = null;

        public BillAdapter1(List<ProductOutOrder> list, Context context) {
            this.list = list;
            this.context = context;
        }

        public BillAdapter1() {

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
                holder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.table_trace_list_item, parent, false);
                holder.tvProductOutOrder = (TextView) convertView.findViewById(R.id.column1);
                holder.productOutOrder = (TextView) convertView.findViewById(R.id.column2);
                holder.tvPlan = (TextView) convertView.findViewById(R.id.column3);
                holder.plan = (TextView) convertView.findViewById(R.id.column4);
                convertView.setTag(holder);
            } else
                holder = (ViewHolder) convertView.getTag();

            holder.tvProductOutOrder.setText("成品出库单");
            holder.productOutOrder.setText(list.get(position).getCode());
            holder.productOutOrder.setTextColor(getResources().getColor(R.color.colorBase));
            holder.productOutOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getActivity(), ProductOutOrderDetailActivity.class);
                    intent.putExtra("ID", list.get(position).getId());
                    startActivity(intent);
                }
            });
            holder.tvPlan.setText("交付计划");
            if (list.get(position).getDeliverPlan() != null) {
                holder.plan.setText(list.get(position).getDeliverPlan().getCode());
                holder.plan.setTextColor(getResources().getColor(R.color.colorBase));
                holder.plan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(getActivity(), DeliveryPlanDetailActivity.class);
                        intent.putExtra("ID", list.get(position).getDeliverPlan().getId());
                        startActivity(intent);
                    }
                });
            } else
                holder.plan.setText("");

            return convertView;
        }

        class ViewHolder {
            public TextView tvProductOutOrder;
            public TextView productOutOrder;
            public TextView tvPlan;
            public TextView plan;
        }
    }
}
