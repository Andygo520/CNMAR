package com.example.administrator.cnmar.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.cnmar.R;
import com.example.administrator.cnmar.StockDetailActivity;
import com.example.administrator.cnmar.model.MaterialStock;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class StockFragment extends Fragment {
    private ListView lvStock;
    private List<MaterialStock> list;
    private StockFragment.StockAdapter.ViewHolder holder;
    private StockAdapter myAdapter;


    public StockFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_stock, container, false);
        lvStock= (ListView) view.findViewById(R.id.test_list_view);
        myAdapter=new StockAdapter(getActivity());
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                getStockListFromNet();
//            }
//        }).start();
        lvStock.setAdapter(myAdapter);
        return view;
    }

    class StockAdapter extends BaseAdapter {
        private Context context;


        public StockAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return 8;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if(convertView==null){
                holder=new ViewHolder();
                convertView=LayoutInflater.from(context).inflate(R.layout.stock_item,parent,false);
                holder.materialCode= (TextView) convertView.findViewById(R.id.materialCode);
                holder.materialName= (TextView) convertView.findViewById(R.id.materialName);
                holder.stockSum= (TextView) convertView.findViewById(R.id.stockSum);
                holder.minStock= (TextView) convertView.findViewById(R.id.minStock);
                holder.maxStock= (TextView) convertView.findViewById(R.id.maxStock);
                holder.detail= (TextView) convertView.findViewById(R.id.detail);
                convertView.setTag(holder);
            }else
                holder= (ViewHolder) convertView.getTag();

//                getStockListFromNet(position);

            holder.materialCode.setText("详情");
//            Log.d("Tag",list.get(position).getMaterial().getCode());
            holder.materialName.setText("详情");
            holder.stockSum.setText("详情");
            holder.minStock.setText("详情");
            holder.maxStock.setText("详情");
            holder.detail.setText("详情");
            holder.detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context,StockDetailActivity.class);
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
//    public void getStockListFromNet(final int position){
//        RequestQueue quene= Volley.newRequestQueue(getActivity());
//
//        StringRequest stringRequest=new StringRequest("http://139.196.104.170:8092/material_stock/list?query.code=&page.num=1", new Response.Listener<String>() {
//            @Override
//            public void onResponse(String s) {
//                String json= VolleyHelper.getJson(s);
//                Log.d("Tag",json);
//                com.example.administrator.cnmar.model.Response response= JSON.parseObject(json, com.example.administrator.cnmar.model.Response.class);
//                 list= JSON.parseArray(response.getData().toString(),MaterialStock.class );
////                holder.materialCode.setText(list.get(position).getMaterial().getCode());
////                Log.d("Tag",list.get(position).getMaterial().getCode());
////                holder.materialName.setText(list.get(position).getMaterial().getName());
////                holder.stockSum.setText(list.get(position).getStock());
////                holder.minStock.setText(list.get(position).getMaterial().getMinStock());
////                holder.maxStock.setText(list.get(position).getMaterial().getMaxStock());
////                holder.detail.setText("详情");
////                Log.d("Tag","取数据");
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//
//            }
//        });
//
//        quene.add(stringRequest);
//    }
//

}
