package com.example.administrator.cnmar.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.administrator.cnmar.R;
import com.example.administrator.cnmar.StockDetailActivity;
import com.example.administrator.cnmar.http.VolleyHelper;
import com.example.administrator.cnmar.model.MaterialInOrder;

import java.text.DateFormat;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class InHouseBillFragment extends Fragment {
    private static final String URL_IN_HOUSE_BILL="http://139.196.104.170:8092/material_in_order/list?query.code=&page.num=1";
    private static final String URL_SEARCH_IN_HOUSE_BILL="http://139.196.104.170:8092/material_in_order/list?query.code={query.code}&page.num=1";
    private ListView lvInOrder;
    private ImageView ivSearch;
    private EditText etSearchInput;

    public InHouseBillFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_in_house_bill, container, false);
        lvInOrder= (ListView) view.findViewById(R.id.lvInOrder);
        lvInOrder.addFooterView(new ViewStub(getActivity()));
//        lvInOrder.addHeaderView(new ViewStub(getActivity()));

        ivSearch= (ImageView) view.findViewById(R.id.ivSearch);
        etSearchInput= (EditText) view.findViewById(R.id.etSearchInput);
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input=etSearchInput.getText().toString().trim();
                String urlString=URL_SEARCH_IN_HOUSE_BILL.replace("{query.code}",input);
                getInOrderListFromNet(urlString);
            }
        });
        getInOrderListFromNet(URL_IN_HOUSE_BILL);
        return view;
    }

    public void getInOrderListFromNet(final String url){
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue quene= Volley.newRequestQueue(getActivity());
                Log.d("Tag","开始执行");
                StringRequest stringRequest=new StringRequest(url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        String json= VolleyHelper.getJson(s);
                        Log.d("GGGG",json);
                        com.example.administrator.cnmar.model.Response response= JSON.parseObject(json, com.example.administrator.cnmar.model.Response.class);
                        List<MaterialInOrder> list= JSON.parseArray(response.getData().toString(),MaterialInOrder.class );
                        BillAdapter myAdapter=new BillAdapter(list,getActivity());
                        lvInOrder.setAdapter(myAdapter);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d("Tag",volleyError.toString());

                    }
                });
                quene.add(stringRequest);
            }
        }).start();
    }

    class BillAdapter extends BaseAdapter {
        private Context context;
        private List<MaterialInOrder> list=null;

        public BillAdapter(List<MaterialInOrder> list, Context context) {
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
            ViewHolder holder=null;
            if(convertView==null){
                holder=new ViewHolder();
                convertView= LayoutInflater.from(context).inflate(R.layout.in_house_bill_item,parent,false);
                holder.tvInOrderNo= (TextView) convertView.findViewById(R.id.tvInOrderNo);
                holder.tvArriveDate= (TextView) convertView.findViewById(R.id.tvArriveDate);
                holder.tvInOrderStatus= (TextView) convertView.findViewById(R.id.tvInOrderStatus);
                holder.detail= (TextView) convertView.findViewById(R.id.detail);
                convertView.setTag(holder);
            }else
                holder= (ViewHolder) convertView.getTag();
//            Log.d("GGGG", DateFormat.getDateInstance().format(list.get(position).getArrivalDate()));
            holder.tvInOrderNo.setText(list.get(position).getCode());
            holder.tvArriveDate.setText(DateFormat.getDateInstance().format(list.get(position).getArrivalDate()));
            holder.tvInOrderStatus.setText(list.get(position).getInOrderStatusVo().getValue());
            holder.detail.setText("详情");
            holder.detail.setTextColor(getResources().getColor(R.color.colorBase));
            holder.detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, StockDetailActivity.class);
                    intent.putExtra("ID", list.get(position).getId());
                    context.startActivity(intent);
                }
            });
            return convertView;
        }

        class ViewHolder{
            public  TextView tvInOrderNo;
            public  TextView tvArriveDate;
            public  TextView tvInOrderStatus;
            public  TextView detail;
        }

    }
}
