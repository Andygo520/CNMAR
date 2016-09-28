package com.example.administrator.cnmar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.administrator.cnmar.entity.MyListView;
import com.example.administrator.cnmar.helper.UniversalHelper;
import com.example.administrator.cnmar.http.VolleyHelper;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import component.basic.vo.StockTypeVo;
import component.material.model.MaterialOutOrder;
import component.material.model.MaterialOutOrderMaterial;
import component.material.model.MaterialOutOrderSpace;
import component.product.vo.OutOrderStatusVo;
import zxing.activity.CaptureActivity;

public class MaterialOutOrderDetailActivity extends AppCompatActivity {
    private static final String URL_OUT_ORDER_DETAIL="http://benxiao.cnmar.com:8092/material_out_order/detail/{id}";
    private static final String URL_OUT_COMMIT="http://benxiao.cnmar.com:8092/material_out_order/out_stock_commit?outOrderId={outOrderId}&outOrderSpaceIds={outOrderSpaceIds}&preOutStocks={preOutStocks}&outStocks={outStocks}";
    private TextView tvName11,tvName12,tvName21,tvName22,tvName31;
    private TextView tvOutOrder,tvOutBatchNo,tvPlanNo,tvRemark,tvOutOrderStatus;
    private TextView name1,name2,name3,name4;
    private MyListView lvMaterialInfo;
    private static String strUrl;
    private ImageView ivScann;
    private LinearLayout llLeftArrow;
    private Button btnSubmit;
    private HashMap<Integer,String> map=new HashMap<>();
    private int id;
    private String outOrderSpaceIds="";
    private String outOrderSpaceIds1="";
    private String preOutStocks="";
    private String preOutStocks1="";
    private String outNums="";
    private String outNums1="";

    //    private int flag=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_out_order_detail);

        init();
        id=getIntent().getIntExtra("ID",0);
        strUrl=URL_OUT_ORDER_DETAIL.replace("{id}",String.valueOf(id));
        strUrl= UniversalHelper.getTokenUrl(strUrl);

        getOutOrderDetailFromNet();


    }
    public void init(){
        tvName11=(TextView) findViewById(R.id.name11);
        tvName12=(TextView) findViewById(R.id.name12);
        tvName21=(TextView) findViewById(R.id.name21);
        tvName22=(TextView) findViewById(R.id.name22);
        tvName31=(TextView) findViewById(R.id.name31);
        llLeftArrow= (LinearLayout) findViewById(R.id.left_arrow);
        ivScann= (ImageView) findViewById(R.id.scann);


        llLeftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MaterialOutOrderDetailActivity.this,MaterialStockActivity.class);
                intent.putExtra("flag",2);
                startActivity(intent);
            }
        });
        tvName11.setText("出库单号");
        tvName12.setText("出库批次号");
        tvName21.setText("交付计划编号");
        tvName22.setText("备注");
        tvName31.setText("出库单状态");

        name1= (TextView) findViewById(R.id.column1);
        name2= (TextView) findViewById(R.id.column2);
        name3= (TextView) findViewById(R.id.column3);
        name4= (TextView) findViewById(R.id.column4);
        btnSubmit= (Button) findViewById(R.id.btnSubmit);
        name1.setText("原料编码");
        name2.setText("仓位编码");
        name3.setText("待出库数量");
        name4.setText("已出库数量");

        tvOutOrder= (TextView) findViewById(R.id.tv11);
        tvOutBatchNo= (TextView) findViewById(R.id.tv12);
        tvPlanNo= (TextView) findViewById(R.id.tv21);
        tvRemark= (TextView) findViewById(R.id.tv22);
        tvOutOrderStatus= (TextView) findViewById(R.id.tv31);

        lvMaterialInfo= (MyListView) findViewById(R.id.lvTable);
//        lvMaterialInfo.addFooterView(new ViewStub(this));

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(outOrderSpaceIds.length()>0){
                    outOrderSpaceIds1=outOrderSpaceIds.substring(0,outOrderSpaceIds.length()-1);
                }
                if (preOutStocks.length()>0){
                    preOutStocks1=preOutStocks.substring(0,preOutStocks.length()-1);
                }
                for (int i=0;i<map.size();i++){
                    outNums+=map.get(i)+",";
                }
                if (outNums.length()>0){
                    outNums1=outNums.substring(0,outNums.length()-1);
                }
                if(outNums1.startsWith(",")){
                    outNums1=outNums1.substring(1);
                }else if (outNums1.endsWith(",")){
                    outNums1=outNums1.substring(0,outNums1.length()-1);
                }
//                outOrderSpaceIds1=outOrderSpaceIds.substring(0,outOrderSpaceIds.length()-1);
//                preOutStocks1=preOutStocks.substring(0,preOutStocks.length()-1);
//                for(int i=0;i<map.size();i++){
//                    outNums+=map.get(i)+",";
//                }
//                String outNums1=outNums.substring(0,outNums.length()-1);
                String url=URL_OUT_COMMIT.replace("{outOrderId}",String.valueOf(id)).replace("{outOrderSpaceIds}",outOrderSpaceIds1).replace("{preOutStocks}",preOutStocks1).replace("{outStocks}",outNums1);
                url= UniversalHelper.getTokenUrl(url);
                sendRequest(url);
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("FFFF",data.getStringExtra("result"));
        if (resultCode==RESULT_OK){
            RequestQueue queue= Volley.newRequestQueue(MaterialOutOrderDetailActivity.this);
            StringRequest stringRequest=new StringRequest(data.getStringExtra("result"), new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    String json= VolleyHelper.getJson(s);
                    component.common.model.Response response= JSON.parseObject(json,component.common.model.Response.class);
                    if(!response.isStatus()){
                        Toast.makeText(MaterialOutOrderDetailActivity.this,response.getMsg(),Toast.LENGTH_SHORT).show();
                    }else{
                        if(response.getData().toString().equals("out")){
                            getOutOrderDetailFromNet();
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
    }
    public void sendRequest(String url){
        RequestQueue queue=Volley.newRequestQueue(this);
        StringRequest stringRequest=new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                String json= VolleyHelper.getJson(s);
                component.common.model.Response response= JSON.parseObject(json,component.common.model.Response.class);
                 if(!response.isStatus()){
                     Toast.makeText(MaterialOutOrderDetailActivity.this,response.getMsg(),Toast.LENGTH_SHORT).show();
                 }else {
                     Intent intent=new Intent(MaterialOutOrderDetailActivity.this,MaterialStockActivity.class);
                     intent.putExtra("flag",2);
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
    public void getOutOrderDetailFromNet(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue queue= Volley.newRequestQueue(MaterialOutOrderDetailActivity.this);
                StringRequest stringRequest=new StringRequest(strUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        String json= VolleyHelper.getJson(s);
//                        Log.d("RRRRR",json);
                        component.common.model.Response response= JSON.parseObject(json,component.common.model.Response.class );
                        MaterialOutOrder materialOutOrder=JSON.parseObject(response.getData().toString(),MaterialOutOrder.class);
//                        得到列表的数据源
                        List<MaterialOutOrderMaterial> list=materialOutOrder.getOutOrderMaterials();
                        List<MaterialOutOrderSpace> list1=new ArrayList<MaterialOutOrderSpace>();
                        List<MaterialOutOrderSpace> list2=new ArrayList<MaterialOutOrderSpace>();

                        for(int i=0;i<list.size();i++){
                            list2=list.get(i).getOutOrderSpaces();
                            for(int j=0;j<list2.size();j++){
                                list2.get(j).getSpace().setMaterial(list.get(i).getMaterial());
                            }
                            list1.addAll(list2);
                        }


                        if(materialOutOrder.getStatus()==OutOrderStatusVo.PRE_OUT_STOCK.getKey()){
                            MaterialInfoAdapter myAdapter=new MaterialInfoAdapter(MaterialOutOrderDetailActivity.this,list1);
                            lvMaterialInfo.setAdapter(myAdapter);
                            ivScann.setVisibility(View.VISIBLE);
                            ivScann.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent =new Intent(MaterialOutOrderDetailActivity.this, CaptureActivity.class);
                                    startActivityForResult(intent,3);
                                }
                            });
                        }else if(materialOutOrder.getStatus()==OutOrderStatusVo.OUT_STOCK.getKey()){
                            MaterialInfoAdapter1 myAdapter=new MaterialInfoAdapter1(MaterialOutOrderDetailActivity.this,list1);
                            lvMaterialInfo.setAdapter(myAdapter);
                        }

                        tvOutOrder.setText(materialOutOrder.getCode());
                        if(materialOutOrder.getOutTime()!=null){
                            tvOutBatchNo.setText(DateFormat.getDateInstance().format(materialOutOrder.getOutTime()));
                        }else
                            tvOutBatchNo.setText("");
                        tvPlanNo.setText("");
                        tvRemark.setText(materialOutOrder.getRemark());
                        tvOutOrderStatus.setText(materialOutOrder.getOutOrderStatusVo().getValue());
                        if(materialOutOrder.getStatus()== OutOrderStatusVo.PRE_OUT_STOCK.getKey()){
                            btnSubmit.setVisibility(View.VISIBLE);
                        }

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

    public class MaterialInfoAdapter1 extends BaseAdapter {
        private Context context;
        private List<MaterialOutOrderSpace> list=null;


        public MaterialInfoAdapter1(Context context, List<MaterialOutOrderSpace> list) {
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
                convertView= LayoutInflater.from(context).inflate(R.layout.material_info_item2,parent,false);
                holder=new ViewHolder();
                holder.tvMaterialCode= (TextView) convertView.findViewById(R.id.materialCode);
                holder.tvSpaceCode= (TextView) convertView.findViewById(R.id.spaceCode);
                holder.tvToBeOutOrderNum= (TextView) convertView.findViewById(R.id.toBeNum);
                holder.tvOutNum= (EditText) convertView.findViewById(R.id.num);

                convertView.setTag(holder);
            }else
                holder= (ViewHolder) convertView.getTag();

            holder.tvMaterialCode.setText(list.get(position).getSpace().getMaterial().getCode());
            holder.tvSpaceCode.setText(list.get(position).getSpace().getCode());
            holder.tvToBeOutOrderNum.setText(String.valueOf(list.get(position).getPreOutStock()));
            holder.tvOutNum.setText(String.valueOf(list.get(position).getOutStock()));



            return convertView;
        }

        public class ViewHolder{
            TextView tvMaterialCode;
            TextView tvSpaceCode;
            TextView tvToBeOutOrderNum;
            EditText tvOutNum;
        }
    }


    public class MaterialInfoAdapter extends BaseAdapter {
        private Context context;
        private List<MaterialOutOrderSpace> list=null;


        public MaterialInfoAdapter(Context context, List<MaterialOutOrderSpace> list) {
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
                convertView= LayoutInflater.from(context).inflate(R.layout.material_info_item2,parent,false);
                holder=new ViewHolder();
                holder.tvMaterialCode= (TextView) convertView.findViewById(R.id.materialCode);
                holder.tvSpaceCode= (TextView) convertView.findViewById(R.id.spaceCode);
                holder.tvToBeOutOrderNum= (TextView) convertView.findViewById(R.id.toBeNum);
                holder.tvOutNum= (EditText) convertView.findViewById(R.id.num);

                convertView.setTag(holder);
            }else
                holder= (ViewHolder) convertView.getTag();

            holder.tvMaterialCode.setText(list.get(position).getSpace().getMaterial().getCode());
            holder.tvSpaceCode.setText(list.get(position).getSpace().getCode());
            holder.tvToBeOutOrderNum.setText(String.valueOf(list.get(position).getPreOutStock()));
//            holder.tvOutNum.setText(String.valueOf(list.get(position).getOutStock()));


//          如果该产品为扫描二维码出入库产品，那么直接将扫描数量存入map
            if(list.get(position).getSpace().getMaterial().getStockType()== StockTypeVo.SCAN.getKey()){
                holder.tvOutNum.setText(String.valueOf(list.get(position).getOutStock()));
                holder.tvOutNum.setFocusable(false);
                holder.tvOutNum.setFocusableInTouchMode(false);
                map.put(position,"");
                holder.tvOutNum.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MaterialOutOrderDetailActivity.this,"该原料为扫描二维码出入库",Toast.LENGTH_SHORT).show();
                    }
                });

            }
//            若产品为手动输入数量的出入库产品，那么将输入的数量存入map中
            else{
                holder.tvOutNum.setText("");
                outOrderSpaceIds+=String.valueOf(list.get(position).getId())+",";
                preOutStocks+=String.valueOf(list.get(position).getPreOutStock())+",";
                holder.tvOutNum.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if(s.length()>0){
                            map.put(position,s.toString());
                        }

                    }
                });
            }

//            }
            return convertView;
        }

        public class ViewHolder{
            TextView tvMaterialCode;
            TextView tvSpaceCode;
            TextView tvToBeOutOrderNum;
            EditText tvOutNum;
        }
    }
}
