package com.example.administrator.cnmar.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.administrator.cnmar.MaterialStockActivity;
import com.example.administrator.cnmar.R;
import com.example.administrator.cnmar.http.VolleyHelper;

import java.util.HashMap;
import java.util.List;

import component.material.model.MaterialInOrder;
import component.material.model.MaterialInOrderMaterial;
import component.material.vo.InOrderStatusVo;

/**
 * A simple {@link Fragment} subclass.
 */
public class InOrderDetailState1Fragment extends Fragment {
    private static final String URL_IN_ORDER_DETAIL="http://benxiao.cnmar.com:8092/material_in_order/detail/{id}";
    private static final String URL_TEST_COMMIT="http://benxiao.cnmar.com:8092/material_in_order/test_commit?inOrderId={inOrderId}&inOrderMaterialIds={inOrderMaterialIds}&res={res}&failNums={failNums}";
    private ListView listView;
    private String strUrl;
    private Button btnSubmit;
    private HashMap<Integer,String> map=new HashMap<>();
    private int id;
    private String inOrderMaterialIds="";
    private String res="";
    private String failNums="";





    public InOrderDetailState1Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view=inflater.inflate(R.layout.fragment_in_order_detail_state1, container, false);
           listView= (ListView) view.findViewById(R.id.lvTable);
            listView.addFooterView(new ViewStub(getActivity()));
            btnSubmit= (Button) view.findViewById(R.id.btnSubmit);
        //        取出传递到详情页面的id
        id=getActivity().getIntent().getIntExtra("ID",0);
        strUrl=URL_IN_ORDER_DETAIL.replace("{id}",String.valueOf(id));
        getMaterialListFromNet(strUrl);


        return view ;
    }

    public void getMaterialListFromNet(final String url){
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue quene= Volley.newRequestQueue(getActivity());
                StringRequest stringRequest=new StringRequest(url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        String json= VolleyHelper.getJson(s);
                        component.common.model.Response response= JSON.parseObject(json, component.common.model.Response.class);
                        MaterialInOrder materialInOrder= JSON.parseObject(response.getData().toString(),MaterialInOrder.class );
                        List<MaterialInOrderMaterial> list=materialInOrder.getInOrderMaterials();

//                        Log.d("materialIdsmate",String.valueOf(materialIds.size()));
                        if(materialInOrder.getStatus()== InOrderStatusVo.PRE_TEST.getKey()){
                            btnSubmit.setVisibility(View.VISIBLE);
                            MaterialInfoAdapter myAdapter=new MaterialInfoAdapter(getActivity(),list);
                            listView.setAdapter(myAdapter);
                        }else if (materialInOrder.getStatus()== InOrderStatusVo.TEST_FAIL.getKey()){
                            MaterialInfoAdapter1 myAdapter=new MaterialInfoAdapter1(getActivity(),list);
                            listView.setAdapter(myAdapter);
                        }


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

    public void sendRequest(String url){
        RequestQueue queue=Volley.newRequestQueue(getActivity());
        StringRequest stringRequest=new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Intent intent=new Intent(getActivity(), MaterialStockActivity.class);
                intent.putExtra("flag",1);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        queue.add(stringRequest);

    }
    public class MaterialInfoAdapter extends BaseAdapter {
        private Context context;
        private List<MaterialInOrderMaterial> list=null;

        public MaterialInfoAdapter(Context context, List<MaterialInOrderMaterial> list) {
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
            ViewHolder holder=null;
            if(convertView==null){
                convertView= LayoutInflater.from(context).inflate(R.layout.material_info_item1,parent,false);
                holder=new ViewHolder();
                holder.tvMaterialCode= (TextView) convertView.findViewById(R.id.materialCode);
                holder.tvToBeInOrderNum= (TextView) convertView.findViewById(R.id.toBeInOrderNum);
                holder.tvTestNum= (TextView) convertView.findViewById(R.id.testNum);
                holder.tvAcNum= (TextView) convertView.findViewById(R.id.acNum);
                holder.tvReNum= (TextView) convertView.findViewById(R.id.reNum);
                holder.tvInvalidateNum= (EditText) convertView.findViewById(R.id.invalidateNum);

                convertView.setTag(holder);
            }else
                holder= (ViewHolder) convertView.getTag();
            holder.tvMaterialCode.setText(list.get(position).getMaterial().getCode());
            holder.tvToBeInOrderNum.setText(String.valueOf(list.get(position).getPreInStock()));
            holder.tvTestNum.setText(String.valueOf(list.get(position).getStandard().getSampleNum()));
            holder.tvAcNum.setText(String.valueOf(list.get(position).getStandard().getAcNum()));
            holder.tvReNum.setText(String.valueOf(list.get(position).getStandard().getReNum()));
            holder.tvInvalidateNum.setText("");

            inOrderMaterialIds+=list.get(position).getId()+",";
            res+=list.get(position).getStandard().getReNum()+",";

//            if(holder.tvInvalidateNum.getText().toString().equals("0")){
//                map.put(position,holder.tvInvalidateNum.getText().toString());
//            }else{
            holder.tvInvalidateNum.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    if(s.toString().equals("0")){
                        map.put(position,s.toString());

                    }
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(s.length()>0)
                    {
                        map.put(position,s.toString());
                        Log.d("position",s.toString());
                    }
                }
            });
//            }
            final ViewHolder finalHolder = holder;
            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(finalHolder.tvInvalidateNum.getText().toString().equals("")){
                        Toast.makeText(getActivity(),"请输入不合格数量后再提交",Toast.LENGTH_SHORT).show();
                    }else{
                        String inOrderMaterialIds1=inOrderMaterialIds.substring(0,inOrderMaterialIds.length()-1);
                        String res1=res.substring(0,res.length()-1);
                        for(int i=0;i<map.size();i++){
                            failNums+=map.get(i)+",";
                        }
                        String failNums1=failNums.substring(0,failNums.length()-1);
                        String url=URL_TEST_COMMIT.replace("{inOrderId}",String.valueOf(id)).replace("{inOrderMaterialIds}",inOrderMaterialIds1).replace("{res}",res1).replace("{failNums}",failNums1);
                        Log.d("strUrlstrUrlstrUrl",url);
                        sendRequest(url);
                    }
                }
            });
            return convertView;
        }

        public class ViewHolder{
            TextView tvMaterialCode;
            TextView tvToBeInOrderNum;
            TextView tvTestNum;
            TextView tvAcNum;
            TextView tvReNum;
            EditText tvInvalidateNum;
        }
    }
    public class MaterialInfoAdapter1 extends BaseAdapter {
        private Context context;
        private List<MaterialInOrderMaterial> list=null;

        public MaterialInfoAdapter1(Context context, List<MaterialInOrderMaterial> list) {
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
            ViewHolder holder=null;
            if(convertView==null){
                convertView= LayoutInflater.from(context).inflate(R.layout.material_info_item1,parent,false);
                holder=new ViewHolder();
                holder.tvMaterialCode= (TextView) convertView.findViewById(R.id.materialCode);
                holder.tvToBeInOrderNum= (TextView) convertView.findViewById(R.id.toBeInOrderNum);
                holder.tvTestNum= (TextView) convertView.findViewById(R.id.testNum);
                holder.tvAcNum= (TextView) convertView.findViewById(R.id.acNum);
                holder.tvReNum= (TextView) convertView.findViewById(R.id.reNum);
                holder.tvInvalidateNum= (EditText) convertView.findViewById(R.id.invalidateNum);

                convertView.setTag(holder);
            }else
                holder= (ViewHolder) convertView.getTag();
            holder.tvMaterialCode.setText(list.get(position).getMaterial().getCode());
            holder.tvToBeInOrderNum.setText(String.valueOf(list.get(position).getPreInStock()));
            holder.tvTestNum.setText(String.valueOf(list.get(position).getStandard().getSampleNum()));
            holder.tvAcNum.setText(String.valueOf(list.get(position).getStandard().getAcNum()));
            holder.tvReNum.setText(String.valueOf(list.get(position).getStandard().getReNum()));
            holder.tvInvalidateNum.setText(String.valueOf(list.get(position).getFailNum()));

            inOrderMaterialIds+=list.get(position).getId()+",";
            res+=list.get(position).getStandard().getReNum()+",";

//            if(holder.tvInvalidateNum.getText().toString().equals("0")){
//                map.put(position,holder.tvInvalidateNum.getText().toString());
//            }else{
            holder.tvInvalidateNum.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    if(s.toString().equals("0")){
                        map.put(position,s.toString());

                    }
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(s.length()>0)
                    {
                        map.put(position,s.toString());
                        Log.d("position",s.toString());
                    }
                }
            });
//            }
            return convertView;
        }

        public class ViewHolder{
            TextView tvMaterialCode;
            TextView tvToBeInOrderNum;
            TextView tvTestNum;
            TextView tvAcNum;
            TextView tvReNum;
            EditText tvInvalidateNum;
        }
    }
}
