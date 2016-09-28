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
import com.example.administrator.cnmar.MaterialOutOrderDetailActivity;
import com.example.administrator.cnmar.R;
import com.example.administrator.cnmar.helper.UniversalHelper;
import com.example.administrator.cnmar.http.VolleyHelper;

import java.util.List;

import component.material.model.MaterialOutOrder;

/**
 * A simple {@link Fragment} subclass.
 */
public class OutOrderFragment extends Fragment {
    private static final String URL_OUT_ORDER="http://benxiao.cnmar.com:8092/material_out_order/list?query.code=&page.num=1";
    private static final String URL_SEARCH_OUT_ORDER="http://benxiao.cnmar.com:8092/material_out_order/list?query.code={query.code}&page.num=1";
    private ListView lvOutOrder;
    private LinearLayout llSearch;
    private EditText etSearchInput;
    private String url= UniversalHelper.getTokenUrl(URL_OUT_ORDER);

    public OutOrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_out_house_bill, container, false);
        lvOutOrder= (ListView) view.findViewById(R.id.lvOutOrder);
        lvOutOrder.addFooterView(new ViewStub(getActivity()));

        llSearch= (LinearLayout) view.findViewById(R.id.llSearch);
        etSearchInput= (EditText) view.findViewById(R.id.etSearchInput);
        etSearchInput.setHint("出库单号查询");
        etSearchInput.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode==KeyEvent.KEYCODE_ENTER){
                    String input=etSearchInput.getText().toString().trim();
                    if(input.equals("")){
                        Toast.makeText(getActivity(),"请输入内容后再查询",Toast.LENGTH_SHORT).show();
                    }else{
                        String urlString=URL_SEARCH_OUT_ORDER.replace("{query.code}",input);
                        urlString=UniversalHelper.getTokenUrl(urlString);
                        getOutOrderListFromNet(urlString);
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
                        getOutOrderListFromNet(url);
            }
        });
        llSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input=etSearchInput.getText().toString().trim();
                String urlString=URL_SEARCH_OUT_ORDER.replace("{query.code}",input);
                urlString=UniversalHelper.getTokenUrl(urlString);
                getOutOrderListFromNet(urlString);
            }
        });
        getOutOrderListFromNet(url);
        return view;
    }
    public void getOutOrderListFromNet(final String url){
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue quene= Volley.newRequestQueue(getActivity());
//                Log.d("Tag","开始执行");
                StringRequest stringRequest=new StringRequest(url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        String json= VolleyHelper.getJson(s);
//                        Log.d("GGGG",json);
                        component.common.model.Response response= JSON.parseObject(json, component.common.model.Response.class);
                        List<MaterialOutOrder> list= JSON.parseArray(response.getData().toString(),MaterialOutOrder.class );
                        BillAdapter myAdapter=new BillAdapter(list,getActivity());
                        lvOutOrder.setAdapter(myAdapter);

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
        private List<MaterialOutOrder> list=null;

        public BillAdapter(List<MaterialOutOrder> list, Context context) {
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
                convertView= LayoutInflater.from(context).inflate(R.layout.out_order_item,parent,false);
                holder.tvOutOrderNo= (TextView) convertView.findViewById(R.id.tvOutOrderNo);
                holder.tvPlanNo= (TextView) convertView.findViewById(R.id.tvPlanNo);
                holder.tvOutOrderStatus= (TextView) convertView.findViewById(R.id.tvOutOrderStatus);
                holder.detail= (TextView) convertView.findViewById(R.id.detail);
                convertView.setTag(holder);
            }else
                holder= (ViewHolder) convertView.getTag();
//            Log.d("GGGG", DateFormat.getDateInstance().format(list.get(position).getArrivalDate()));
            holder.tvOutOrderNo.setText(list.get(position).getCode());
            holder.tvPlanNo.setText("");
            holder.tvOutOrderStatus.setText(list.get(position).getOutOrderStatusVo().getValue());
            holder.detail.setText("详情");
            holder.detail.setTextColor(getResources().getColor(R.color.colorBase));
            holder.detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MaterialOutOrderDetailActivity.class);
                    intent.putExtra("ID", list.get(position).getId());
//                    intent.putExtra("flag", 0);
                    context.startActivity(intent);
                }
            });
            return convertView;
        }

        class ViewHolder{
            public  TextView tvOutOrderNo;
            public  TextView tvPlanNo;
            public  TextView tvOutOrderStatus;
            public  TextView detail;
        }

    }
}
