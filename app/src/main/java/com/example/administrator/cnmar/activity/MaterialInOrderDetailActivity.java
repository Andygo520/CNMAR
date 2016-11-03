package com.example.administrator.cnmar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
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
import com.example.administrator.cnmar.R;
import com.example.administrator.cnmar.fragment.InOrderDetailState1Fragment;
import com.example.administrator.cnmar.fragment.InOrderDetailState2Fragment;
import com.example.administrator.cnmar.helper.UniversalHelper;
import com.example.administrator.cnmar.helper.UrlHelper;
import com.example.administrator.cnmar.http.VolleyHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import component.material.model.MaterialInOrder;
import component.material.vo.InOrderStatusVo;
import zxing.activity.CaptureActivity;

public class MaterialInOrderDetailActivity extends AppCompatActivity {
    private TextView tvName11,tvName12,tvName21,tvName22,tvName31,tvName32;
    private TextView tvInOrder,tvInBatchNo,tvPurchaseOrderNo,tvArriveDate,tvRemark,tvInOrderStatus;
//    private ListView lvMaterialInfo;
    private static String strUrl;
    private TextView tvTitle;
    private ImageView ivScann;
    private LinearLayout llLeftArrow;
    private InOrderDetailState1Fragment fragment1;
    private InOrderDetailState2Fragment fragment2;
    private FragmentTransaction transaction;
    private int id;
//    final int ID=0;
    private int flag=-1;
    private int flag1=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_order_detail);

        init();

            id=getIntent().getExtras().getInt("ID");
            strUrl= UrlHelper.URL_IN_ORDER_DETAIL.replace("{id}",String.valueOf(id));
          strUrl= UniversalHelper.getTokenUrl(strUrl);
//        保存从列表页面传递过来的id对应的URL地址，在扫描页面返回的时候用到
            LoginActivity.editor.putString("URL",strUrl).commit();


        getInOrderDetailFromNet();

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            Intent intent=new Intent(MaterialInOrderDetailActivity.this,MaterialStockActivity.class);
            intent.putExtra("flag",1);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void init(){
        tvTitle= (TextView) findViewById(R.id.title);
        tvTitle.setText("原料仓库-入库单");
        tvName11=(TextView) findViewById(R.id.name11);
        tvName12=(TextView) findViewById(R.id.name12);
        tvName21=(TextView) findViewById(R.id.name21);
        tvName22=(TextView) findViewById(R.id.name22);
        tvName31=(TextView) findViewById(R.id.name31);
        tvName32=(TextView) findViewById(R.id.name32);
        llLeftArrow= (LinearLayout) findViewById(R.id.left_arrow);
        ivScann= (ImageView) findViewById(R.id.scann);

        llLeftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MaterialInOrderDetailActivity.this,MaterialStockActivity.class);
                intent.putExtra("flag",1);
                startActivity(intent);
            }
        });
        tvName11.setText("入库单号");
        tvName12.setText("入库批次号");
        tvName21.setText("采购订单号");
        tvName22.setText("到货日期");
        tvName31.setText("备注");
        tvName32.setText("入库单状态");

        tvInOrder= (TextView) findViewById(R.id.tv11);
        tvInBatchNo= (TextView) findViewById(R.id.tv12);
        tvPurchaseOrderNo= (TextView) findViewById(R.id.tv21);
        tvArriveDate= (TextView) findViewById(R.id.tv22);
        tvRemark= (TextView) findViewById(R.id.tv31);
        tvInOrderStatus= (TextView) findViewById(R.id.tv32);
//        lvMaterialInfo= (ListView) findViewById(R.id.lvTable);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            RequestQueue queue= Volley.newRequestQueue(MaterialInOrderDetailActivity.this);
            StringRequest stringRequest=new StringRequest(data.getStringExtra("result"), new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    String json=VolleyHelper.getJson(s);
                    component.common.model.Response response=JSON.parseObject(json,component.common.model.Response.class);
                    if(response.isStatus()==false){
                        Toast.makeText(MaterialInOrderDetailActivity.this,response.getMsg(),Toast.LENGTH_SHORT).show();
                    }else{
//                        transaction=getSupportFragmentManager().beginTransaction();
//                        transaction.remove(fragment2);
                        getInOrderDetailFromNet();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                }
            });
            queue.add(stringRequest);
        }else if (resultCode==3){
            strUrl=LoginActivity.sp.getString("URL","");
            getInOrderDetailFromNet();
        }
    }

    public void getInOrderDetailFromNet(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("strURl",strUrl);

                RequestQueue queue= Volley.newRequestQueue(MaterialInOrderDetailActivity.this);
                StringRequest stringRequest=new StringRequest(strUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        String json= VolleyHelper.getJson(s);
                        component.common.model.Response response= JSON.parseObject(json,component.common.model.Response.class );
                        MaterialInOrder materialInOrder=JSON.parseObject(response.getData().toString(),MaterialInOrder.class);
                        if(materialInOrder.getStatus()==InOrderStatusVo.pre_in_stock.getKey()){
                            ivScann.setVisibility(View.VISIBLE);
                            ivScann.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent=new Intent(MaterialInOrderDetailActivity.this, CaptureActivity.class);
                                    intent.putExtra("FLAG",1);
                                    startActivityForResult(intent,0);
//                                    finish();
                                }
                            });

                        }
                        tvInOrder.setText(materialInOrder.getCode());

//                      入库批次根据入库时间产生
                        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
                        if(materialInOrder.getInTime()!=null){
                            tvInBatchNo.setText(sdf.format(materialInOrder.getInTime()));
                        }else
                            tvInBatchNo.setText("");
                        tvPurchaseOrderNo.setText(materialInOrder.getPurchaseOrder());
                        tvArriveDate.setText(DateFormat.getDateInstance().format(materialInOrder.getArrivalDate()));
                        tvRemark.setText(materialInOrder.getRemark());
                        tvInOrderStatus.setText(materialInOrder.getInOrderStatusVo().getValue());

                        transaction=getSupportFragmentManager().beginTransaction();
                        if(materialInOrder.getStatus()== InOrderStatusVo.pre_test.getKey() ||materialInOrder.getStatus()==InOrderStatusVo.test_fail.getKey()){
                            if (fragment1!=null) transaction.remove(fragment1);
                            fragment1=new InOrderDetailState1Fragment();
                            transaction.add(R.id.content,fragment1).commit();

                        }else{
                            if (fragment2!=null) transaction.remove(fragment2);
                            fragment2=new InOrderDetailState2Fragment();
                            transaction.add(R.id.content,fragment2).commit();

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

}
