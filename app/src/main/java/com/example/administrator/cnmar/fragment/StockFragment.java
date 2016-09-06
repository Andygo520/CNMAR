package com.example.administrator.cnmar.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.administrator.cnmar.model.MaterialStock;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class StockFragment extends Fragment {
    private static final String URL_STOCK="http://139.196.104.170:8092/material_stock/list?query.code=&page.num=1";
    private static final String URL_SEARCH_STOCK="http://139.196.104.170:8092/material_stock/list?query.code={query.code}&page.num=1";
    private ListView lvStock;
    private ImageView ivSearch;
    private EditText etSearchInput;
    public StockFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_stock, container, false);
        lvStock= (ListView) view.findViewById(R.id.test_list_view);
        ivSearch= (ImageView) view.findViewById(R.id.ivSearch);
        etSearchInput= (EditText) view.findViewById(R.id.etSearchInput);
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input=etSearchInput.getText().toString().trim();
                String urlString=URL_SEARCH_STOCK.replace("{query.code}",input);
                getStockListFromNet(urlString);
            }
        });
        getStockListFromNet(URL_STOCK);
        return view;
    }


    public void getStockListFromNet(final String url){
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue quene= Volley.newRequestQueue(getActivity());
                Log.d("Tag","开始执行");
                StringRequest stringRequest=new StringRequest(url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        String json= VolleyHelper.getJson(s);
                        Log.d("TAG",json);
                        com.example.administrator.cnmar.model.Response response= JSON.parseObject(json, com.example.administrator.cnmar.model.Response.class);
                        List<MaterialStock> list= JSON.parseArray(response.getData().toString(),MaterialStock.class );
                        StockAdapter myAdapter=new StockAdapter(list,getActivity());
                        lvStock.setAdapter(myAdapter);
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

    class StockAdapter extends BaseAdapter {
        private Context context;
        private List<MaterialStock> list=null;

        public StockAdapter(List<MaterialStock> list, Context context) {
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
                convertView= LayoutInflater.from(context).inflate(R.layout.stock_item,parent,false);
                holder.materialCode= (TextView) convertView.findViewById(R.id.materialCode);
                holder.materialName= (TextView) convertView.findViewById(R.id.materialName);
                holder.stockSum= (TextView) convertView.findViewById(R.id.stockSum);
                holder.minStock= (TextView) convertView.findViewById(R.id.minStock);
                holder.maxStock= (TextView) convertView.findViewById(R.id.maxStock);
                holder.detail= (TextView) convertView.findViewById(R.id.detail);
                convertView.setTag(holder);
            }else
                holder= (ViewHolder) convertView.getTag();

                holder.materialCode.setText(list.get(position).getMaterial().getCode());
                holder.materialName.setText(list.get(position).getMaterial().getName());
                holder.stockSum.setText(list.get(position).getStock()+list.get(position).getMaterial().getUnit().getName());
                holder.minStock.setText(String.valueOf(list.get(position).getMaterial().getMinStock()));
                holder.maxStock.setText(String.valueOf(list.get(position).getMaterial().getMaxStock()));
                holder.detail.setText("详情");
                holder.detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context,StockDetailActivity.class);
                    intent.putExtra("ID",list.get(position).getId());
                    context.startActivity(intent);
                }
            });

            return convertView;
        }

        class ViewHolder{
            public  TextView materialCode;
            public  TextView materialName;
            public  TextView stockSum;
            public  TextView minStock;
            public  TextView maxStock;
            public  TextView detail;
        }

    }


}
