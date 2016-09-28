package com.example.administrator.cnmar.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.administrator.cnmar.R;
import com.example.administrator.cnmar.MaterialStockDetailActivity;
import com.example.administrator.cnmar.helper.UniversalHelper;
import com.example.administrator.cnmar.http.VolleyHelper;

import java.util.List;

import component.material.model.MaterialStock;

/**
 * A simple {@link Fragment} subclass.
 */
public class MaterialStockFragment extends Fragment {
    private static final String URL_STOCK="http://benxiao.cnmar.com:8092/material_stock/list?query.code=&page.num=1";
    private static final String URL_SEARCH_STOCK="http://benxiao.cnmar.com:8092/material_stock/list?query.code={query.code}&page.num=1";
    private ListView lvStock;
    private LinearLayout llSearch;
    private EditText etSearchInput;
    private String url= UniversalHelper.getTokenUrl(URL_STOCK);
    public MaterialStockFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_stock, container, false);
        lvStock= (ListView) view.findViewById(R.id.stock_list_view);
//        lvStock.addHeaderView(new ViewStub(getActivity()));
        lvStock.addFooterView(new ViewStub(getActivity()));
        llSearch= (LinearLayout) view.findViewById(R.id.llSearch);
        etSearchInput= (EditText) view.findViewById(R.id.etSearchInput);
        etSearchInput.setHint("原料编码查询");
        etSearchInput.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode==KeyEvent.KEYCODE_ENTER){
                    String input=etSearchInput.getText().toString().trim();
                    if(input.equals("")){
                        Toast.makeText(getActivity(),"请输入内容后再查询",Toast.LENGTH_SHORT).show();
                    }else{
                        String urlString=URL_SEARCH_STOCK.replace("{query.code}",input);
                        urlString=UniversalHelper.getTokenUrl(urlString);
                        getStockListFromNet(urlString);
                    }
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                    }
                    return true;
                }
                return false;
            }

        });
        etSearchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals(""))
                    getStockListFromNet(url);
            }
        });
        llSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input=etSearchInput.getText().toString().trim();
                String urlString=URL_SEARCH_STOCK.replace("{query.code}",input);
                urlString=UniversalHelper.getTokenUrl(urlString);
                getStockListFromNet(urlString);
            }
        });
        getStockListFromNet(url);
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
                        component.common.model.Response response= JSON.parseObject(json, component.common.model.Response.class);
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
                holder.materialCode= (TextView) convertView.findViewById(R.id.code);
                holder.materialName= (TextView) convertView.findViewById(R.id.name);
                holder.stockSum= (TextView) convertView.findViewById(R.id.stockSum);
//                holder.minStock= (TextView) convertView.findViewById(R.id.minStock);
//                holder.maxStock= (TextView) convertView.findViewById(R.id.maxStock);
                holder.detail= (TextView) convertView.findViewById(R.id.detail);
                convertView.setTag(holder);
            }else
                holder= (ViewHolder) convertView.getTag();

                holder.materialCode.setText(list.get(position).getMaterial().getCode());
                holder.materialName.setText(list.get(position).getMaterial().getName());
                holder.stockSum.setText(list.get(position).getStock() + list.get(position).getMaterial().getUnit().getName());
//                holder.minStock.setText(String.valueOf(list.get(position).getMaterial().getMinStock()));
//                holder.maxStock.setText(String.valueOf(list.get(position).getMaterial().getMaxStock()));
                holder.detail.setText("详情");
                holder.detail.setTextColor(getResources().getColor(R.color.colorBase));
                holder.detail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, MaterialStockDetailActivity.class);
                        intent.putExtra("ID", list.get(position).getId());
                        context.startActivity(intent);
                    }
                });
//            }
            return convertView;
        }

        class ViewHolder{
            public  TextView materialCode;
            public  TextView materialName;
            public  TextView stockSum;
//            public  TextView minStock;
//            public  TextView maxStock;
            public  TextView detail;
        }

    }


}
