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
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.administrator.cnmar.R;
import com.example.administrator.cnmar.activity.ScannStationDetailActivity;
import com.example.administrator.cnmar.entity.MyListView;
import com.example.administrator.cnmar.helper.VolleyHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import component.produce.model.ProduceReceive;
import zxing.activity.CaptureActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class StationFragment extends Fragment {

    private int stationId;//扫描机台的id
    @BindView(R.id.bomTable)
    LinearLayout bomTable;
    @BindView(R.id.planTable)
    LinearLayout planTable;
    @BindView(R.id.column1)
    TextView column1;
    @BindView(R.id.column2)
    TextView column2;
    @BindView(R.id.column3)
    TextView column3;
    @BindView(R.id.column4)
    TextView column4;
    @BindView(R.id.col1)
    TextView col1;
    @BindView(R.id.col2)
    TextView col2;
    @BindView(R.id.col3)
    TextView col3;
    @BindView(R.id.col4)
    TextView col4;
    @BindView(R.id.column5)
    TextView column5;
    @BindView(R.id.column6)
    TextView column6;
    @BindView(R.id.tableLayout)
    TableLayout tableLayout;
    @BindView(R.id.col5)
    TextView col5;
    @BindView(R.id.col6)
    TextView col6;
    @BindView(R.id.lvTable)
    MyListView lvTable;
    @BindView(R.id.lvProduceBom)
    MyListView lvProduceBom;
    @BindView(R.id.btnScann)
    Button btnScann;

    public StationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for getActivity() fragment
        View view = inflater.inflate(R.layout.fragment_station, container, false);
        ButterKnife.bind(this, view);

        tableLayout.setColumnCollapsed(9,false);
        tableLayout.setColumnCollapsed(10,false);
        tableLayout.setColumnCollapsed(11,false);
        tableLayout.setColumnCollapsed(12,false);
        tableLayout.setColumnStretchable(9,true);
        tableLayout.setColumnStretchable(11,true);

        column1.setText("加工单编号");
        column2.setText("成品编码");
        column3.setText("计划生产数量");
        column4.setText("领料单编号");
        column5.setText("工序");
        column6.setText("机台工位");

        col1.setText("子加工单编号");
        col2.setText("半成品编码");
        col3.setText("计划生产数量");
        col4.setText("领料单编号");
        col5.setText("工序");
        col6.setText("机台工位");
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //        扫码返回从后台取数据
        if (resultCode == 0) {
            RequestQueue queue = Volley.newRequestQueue(getActivity());
            final String codeUrl = data.getStringExtra("result");
//            从返回的结果中取出机台id
            String id = codeUrl.substring(codeUrl.indexOf("qrcode/"), codeUrl.indexOf("?")).substring(7);
            stationId = Integer.parseInt(id);
            StringRequest stringRequest = new StringRequest(codeUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    String json = VolleyHelper.getJson(s);
                    component.common.model.Response response = JSON.parseObject(json, component.common.model.Response.class);
                    if (!response.isStatus()) {
                        Toast.makeText(getActivity(), response.getMsg(), Toast.LENGTH_SHORT).show();
                    } else {
                        btnScann.setVisibility(View.GONE);//有返回数据的时候，不显示扫描按钮
//                      response.getData()对应着一个数组，数组包括两个列表，分别为加工单跟子加工单列表，判断各自的长度，如果长度为0就不显示
                        List list = JSON.parseArray(response.getData().toString(), List.class);
                        List<ProduceReceive> producePlan = JSON.parseArray(list.get(0).toString(), ProduceReceive.class);//数组第0个元素代表加工单
                        List<ProduceReceive> produceBom = JSON.parseArray(list.get(1).toString(), ProduceReceive.class);//数组第1个元素代表子加工单
                        if (producePlan.size() > 0) {
                            planTable.setVisibility(View.VISIBLE);
                            lvTable.setAdapter(new BillAdapter(producePlan, getActivity()));
                        } else {
                            planTable.setVisibility(View.GONE);
                        }

                        if (produceBom.size() > 0) {
                            bomTable.setVisibility(View.VISIBLE);
                            lvProduceBom.setAdapter(new BillAdapter1(produceBom, getActivity()));
                        } else {
                            bomTable.setVisibility(View.GONE);
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
        else if (resultCode == 1) {

        }
    }

    @OnClick(R.id.btnScann)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnScann:
                planTable.setVisibility(View.GONE);
                bomTable.setVisibility(View.GONE);
                Intent intent = new Intent(getActivity(), CaptureActivity.class);
                intent.putExtra("FLAG", -100);//作为跳转到扫描页面的标志位
                startActivityForResult(intent, 1);
                break;
        }
    }

    /*
    * 加工单的适配器
    * */
    class BillAdapter extends BaseAdapter {
        private Context context;
        private List<ProduceReceive> list = null;

        public BillAdapter(List<ProduceReceive> list, Context context) {
            this.list = list;
            this.context = context;
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
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.table_list_item, parent, false);
                TableLayout tableLayout= (TableLayout) convertView.findViewById(R.id.tableLayout);
                tableLayout.setColumnCollapsed(9,false);
                tableLayout.setColumnCollapsed(10,false);
                tableLayout.setColumnCollapsed(11,false);
                tableLayout.setColumnCollapsed(12,false);
                tableLayout.setColumnStretchable(9,true);
                tableLayout.setColumnStretchable(11,true);
                holder.tvPlanCode = (TextView) convertView.findViewById(R.id.column1);
                holder.tvProductCode = (TextView) convertView.findViewById(R.id.column2);
                holder.tvPlanProduceNum = (TextView) convertView.findViewById(R.id.column3);
                holder.tvReceive = (TextView) convertView.findViewById(R.id.column4);
                holder.tvProcess = (TextView) convertView.findViewById(R.id.column5);
                holder.tvStation = (TextView) convertView.findViewById(R.id.column6);
                convertView.setTag(holder);
            } else
                holder = (ViewHolder) convertView.getTag();

            holder.tvPlanCode.setText(list.get(position).getPlan().getCode());
            holder.tvPlanProduceNum.setText(list.get(position).getPlan().getProduceNum() + list.get(position).getPlan().getProduct().getUnit().getName());
            holder.tvProductCode.setText(list.get(position).getPlan().getProduct().getCode());
            holder.tvReceive.setText(list.get(position).getCode());
            holder.tvProcess.setText(list.get(position).getProcessProduct().getName());
            holder.tvStation.setText(list.get(position).getProcessProduct().getStation().getName());
            holder.tvPlanCode.setTextColor(getResources().getColor(R.color.colorBase));
            holder.tvPlanCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ScannStationDetailActivity.class);
                    intent.putExtra("ID", list.get(position).getId());// 列表项id
                    intent.putExtra("stationId", stationId);
                    startActivity(intent);
                }
            });
            return convertView;
        }

        class ViewHolder {
            public TextView tvPlanCode;//加工单编号
            public TextView tvProductCode;//成品编码
            public TextView tvPlanProduceNum;//计划生产数量
            public TextView tvReceive;//领料单编号
            public TextView tvProcess;//工序
            public TextView tvStation;//机台工位
        }
    }

    /*
    * 子加工单的适配器
    * */
    class BillAdapter1 extends BaseAdapter {
        private Context context;
        private List<ProduceReceive> list = null;

        public BillAdapter1(List<ProduceReceive> list, Context context) {
            this.list = list;
            this.context = context;
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
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.table_list_item, parent, false);
                TableLayout tableLayout= (TableLayout) convertView.findViewById(R.id.tableLayout);
                tableLayout.setColumnCollapsed(9,false);
                tableLayout.setColumnCollapsed(10,false);
                tableLayout.setColumnCollapsed(11,false);
                tableLayout.setColumnCollapsed(12,false);
                tableLayout.setColumnStretchable(9,true);
                tableLayout.setColumnStretchable(11,true);
                holder.tvProduceBomCode = (TextView) convertView.findViewById(R.id.column1);
                holder.tvHalfCode = (TextView) convertView.findViewById(R.id.column2);
                holder.tvPlanProduceNum = (TextView) convertView.findViewById(R.id.column3);
                holder.tvReceive = (TextView) convertView.findViewById(R.id.column4);
                holder.tvProcess = (TextView) convertView.findViewById(R.id.column5);
                holder.tvStation = (TextView) convertView.findViewById(R.id.column6);

                convertView.setTag(holder);
            } else
                holder = (ViewHolder) convertView.getTag();

            holder.tvProduceBomCode.setText(list.get(position).getBom().getCode());
            holder.tvProduceBomCode.setTextColor(getResources().getColor(R.color.colorBase));
            holder.tvProduceBomCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ScannStationDetailActivity.class);
                    intent.putExtra("ID", list.get(position).getId());// 列表项id
                    intent.putExtra("stationId", stationId);
                    startActivity(intent);
                }
            });
            holder.tvHalfCode.setText(list.get(position).getBom().getHalf().getCode());
            holder.tvPlanProduceNum.setText(list.get(position).getBom().getReceiveNum() + list.get(position).getBom().getHalf().getUnit().getName());
            holder.tvReceive.setText(list.get(position).getCode());
            holder.tvProcess.setText(list.get(position).getProcessHalf().getName());
            holder.tvStation.setText(list.get(position).getProcessHalf().getStation().getName());

            return convertView;
        }

        class ViewHolder {
            //           子加工单编号，半成品编码，计划生产数量，领料单编号，工序，机台工位
            public TextView tvProduceBomCode;
            public TextView tvHalfCode;
            public TextView tvPlanProduceNum;
            public TextView tvReceive;
            public TextView tvProcess;//工序
            public TextView tvStation;//机台工位
        }
    }

}
   

  

