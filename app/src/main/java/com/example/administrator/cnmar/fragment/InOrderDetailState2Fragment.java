package com.example.administrator.cnmar.fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.administrator.cnmar.activity.LoginActivity;
import com.example.administrator.cnmar.activity.MaterialStockActivity;
import com.example.administrator.cnmar.R;
import com.example.administrator.cnmar.entity.MyListView;
import com.example.administrator.cnmar.helper.UniversalHelper;
import com.example.administrator.cnmar.helper.UrlHelper;
import com.example.administrator.cnmar.http.VolleyHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import component.basic.vo.StockTypeVo;
import component.material.model.MaterialInOrder;
import component.material.model.MaterialInOrderMaterial;
import component.material.model.MaterialInOrderSpace;
import component.material.vo.InOrderStatusVo;

/**
 * A simple {@link Fragment} subclass.
 */
public class InOrderDetailState2Fragment extends Fragment implements View.OnClickListener {
    private String strUrl;
    private TextView name1, name2, name3, name4;
    private MyListView listView;
    private Button btnSubmit;
    private HashMap<Integer, String> map = new HashMap<>();
    private int id;
    MaterialInfoAdapter myAdapter;
    private String inOrderSpaceIds = "";
    private String inOrderSpaceIds1 = "";
    private String preInStocks = "";
    private String preInStocks1 = "";
    private String inStocks = "";
    private String inStocks1 = "";
    private String role;
    private Boolean isSuper;


    private List<MaterialInOrderSpace> list1;


    public InOrderDetailState2Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_in_order_detail_state2, container, false);
        name1 = (TextView) view.findViewById(R.id.column1);
        name2 = (TextView) view.findViewById(R.id.column2);
        name3 = (TextView) view.findViewById(R.id.column3);
        name4 = (TextView) view.findViewById(R.id.column4);
        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);

        //        从SharedPreference中取出用户信息
        role = LoginActivity.sp.getString("Role", "123");
        isSuper=LoginActivity.sp.getBoolean("isSuper",false);


        listView = (MyListView) view.findViewById(R.id.lvTable);
//        listView.addFooterView(new ViewStub(getActivity()));
        name1.setText("原料编码");
        name2.setText("仓位编码");
        name3.setText("待入库数量");
        name4.setText("已入库数量");

        //        取出传递到详情页面的id
        id = getActivity().getIntent().getIntExtra("ID", 0);
        strUrl = UrlHelper.URL_IN_ORDER_DETAIL.replace("{id}", String.valueOf(id));
        strUrl = UniversalHelper.getTokenUrl(strUrl);

        getMaterialListFromNet(strUrl);

        btnSubmit.setOnClickListener(this);

        return view;
    }

    public void sendRequest(String url) {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("ssssss", s);
                String json = VolleyHelper.getJson(s);
                component.common.model.Response response = JSON.parseObject(json, component.common.model.Response.class);
                if (response.isStatus()) {
                    Intent intent = new Intent(getActivity(), MaterialStockActivity.class);
                    intent.putExtra("flag", 1);
                    startActivity(intent);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        queue.add(stringRequest);

    }

    public void getMaterialListFromNet(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue quene = Volley.newRequestQueue(getActivity());
                StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        String json = VolleyHelper.getJson(s);
                        component.common.model.Response response = JSON.parseObject(json, component.common.model.Response.class);
                        MaterialInOrder materialInOrder = JSON.parseObject(response.getData().toString(), MaterialInOrder.class);
                        List<MaterialInOrderMaterial> list = materialInOrder.getInOrderMaterials();
                        list1 = new ArrayList<MaterialInOrderSpace>();
                        List<MaterialInOrderSpace> list2 = new ArrayList<MaterialInOrderSpace>();

                        for (int i = 0; i < list.size(); i++) {
                            list2 = list.get(i).getInOrderSpaces();
                            for (int j = 0; j < list2.size(); j++) {
                                list2.get(j).getSpace().setMaterial(list.get(i).getMaterial());
                            }
                            list1.addAll(list2);
                        }


//                        MaterialInfoAdapter myAdapter=new MaterialInfoAdapter(getActivity(),list1);
//                        listView.setAdapter(myAdapter);

                        if (materialInOrder.getStatus() == InOrderStatusVo.pre_in_stock.getKey() && (role.contains("原料库管员") || isSuper)) {
                            btnSubmit.setVisibility(View.VISIBLE);
                            btnSubmit.setText("提交入库");
                            myAdapter = new MaterialInfoAdapter(getActivity(), list1);
                            listView.setAdapter(myAdapter);

                        } else {
                            MaterialInfoAdapter1 myAdapter = new MaterialInfoAdapter1(getActivity(), list1);
                            listView.setAdapter(myAdapter);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d("Tag", volleyError.toString());

                    }
                });
                quene.add(stringRequest);
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        //   入库数量的非空判断
        if (map.size() < myAdapter.getCount()) {
            Toast.makeText(getActivity(), "请输入入库数量再提交", Toast.LENGTH_SHORT).show();
            return;
        }
        for (int position = 0; position < map.size(); position++) {
            if (map.get(position).equals("?")) {
                Toast.makeText(getActivity(),R.string.more_than_pre_in, Toast.LENGTH_SHORT).show();
                return;
            }
        }

        inOrderSpaceIds1 = inOrderSpaceIds.substring(0, inOrderSpaceIds.length() - 1);
        preInStocks1 = preInStocks.substring(0, preInStocks.length() - 1);
        for (int i = 0; i < map.size(); i++) {
            inStocks += map.get(i) + ",";
        }

        inStocks1 = inStocks.substring(0, inStocks.length() - 1);

        String url = UrlHelper.URL_IN_ORDER_COMMIT.replace("{inOrderId}", String.valueOf(id)).replace("{inOrderSpaceIds}", inOrderSpaceIds1).replace("{preInStocks}", preInStocks1).replace("{inStocks}", inStocks1);
        url = UniversalHelper.getTokenUrl(url);
        for (int position = 0; position < map.size(); position++) {
            if (Integer.parseInt(map.get(position)) < list1.get(position).getPreInStock().intValue()) {
                final String finalUrl = url;
                new AlertDialog.Builder(getActivity())
                        .setTitle("系统提示")
                        .setMessage("未全部入库，提交之后不能修改，确认入库吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sendRequest(finalUrl);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                inStocks = "";
                            }
                        }).create().show();
                return;
            }
        }

        sendRequest(url);
    }


    public class MaterialInfoAdapter extends BaseAdapter {
        private Context context;
        private List<MaterialInOrderSpace> list = null;


        public MaterialInfoAdapter(Context context, List<MaterialInOrderSpace> list) {
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
                convertView = LayoutInflater.from(context).inflate(R.layout.material_info_item2, parent, false);
                holder = new ViewHolder();
                holder.tvMaterialCode = (TextView) convertView.findViewById(R.id.materialCode);
                holder.tvSpaceCode = (TextView) convertView.findViewById(R.id.spaceCode);
                holder.tvToBeInOrderNum = (TextView) convertView.findViewById(R.id.toBeNum);
                holder.tvInNum = (EditText) convertView.findViewById(R.id.num);

                convertView.setTag(holder);
            } else
                holder = (ViewHolder) convertView.getTag();

            holder.tvMaterialCode.setText(list.get(position).getSpace().getMaterial().getCode());
            holder.tvSpaceCode.setText(list.get(position).getSpace().getCode());
            holder.tvToBeInOrderNum.setText(String.valueOf(list.get(position).getPreInStock()));
//                holder.tvInNum.setText(String.valueOf(list.get(position).getInStock()));

            inOrderSpaceIds += list.get(position).getId() + ",";
            preInStocks += list.get(position).getPreInStock() + ",";

//            判断原料是扫描入库还是输入数量入库
            if (list.get(position).getSpace().getMaterial().getStockType() == StockTypeVo.scan.getKey()) {
                holder.tvInNum.setText(String.valueOf(list.get(position).getInStock()));
                holder.tvInNum.setFocusable(false);
                holder.tvInNum.setFocusableInTouchMode(false);
//                map.put(position, "");
                map.put(position, String.valueOf(list.get(position).getInStock()));
                holder.tvInNum.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast toast = null;
                        if (toast == null) {
                            toast = new Toast(getActivity());
                            toast.makeText(getActivity(), "该原料为扫描出入库", Toast.LENGTH_SHORT).show();
                        } else
                            toast.show();

                    }
                });
            } else {
                holder.tvInNum.setText("");
                final ViewHolder finalHolder = holder;
                holder.tvInNum.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (s.length() > 0) {
                            if (Integer.parseInt(s.toString()) > list.get(position).getPreInStock().intValue()) {
                                map.put(position, "?");
                            } else
                                map.put(position, s.toString());

                        } else {
                            map.remove(position);
                        }
                    }
                });
            }


            return convertView;
        }

        public class ViewHolder {
            TextView tvMaterialCode;
            TextView tvSpaceCode;
            TextView tvToBeInOrderNum;
            EditText tvInNum;
        }
    }

    public class MaterialInfoAdapter1 extends BaseAdapter {
        private Context context;
        private List<MaterialInOrderSpace> list = null;


        public MaterialInfoAdapter1(Context context, List<MaterialInOrderSpace> list) {
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
                convertView = LayoutInflater.from(context).inflate(R.layout.material_info_item2, parent, false);
                holder = new ViewHolder();
                holder.tvMaterialCode = (TextView) convertView.findViewById(R.id.materialCode);
                holder.tvSpaceCode = (TextView) convertView.findViewById(R.id.spaceCode);
                holder.tvToBeInOrderNum = (TextView) convertView.findViewById(R.id.toBeNum);
                holder.tvInNum = (EditText) convertView.findViewById(R.id.num);

                convertView.setTag(holder);
            } else
                holder = (ViewHolder) convertView.getTag();

            holder.tvMaterialCode.setText(list.get(position).getSpace().getMaterial().getCode());
            holder.tvSpaceCode.setText(list.get(position).getSpace().getCode());
            holder.tvToBeInOrderNum.setText(String.valueOf(list.get(position).getPreInStock()));
            holder.tvInNum.setText(String.valueOf(list.get(position).getInStock()));
            holder.tvInNum.setFocusable(false);
            holder.tvInNum.setFocusableInTouchMode(false);
            return convertView;
        }

        public class ViewHolder {
            TextView tvMaterialCode;
            TextView tvSpaceCode;
            TextView tvToBeInOrderNum;
            EditText tvInNum;
        }
    }
}
