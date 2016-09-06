package com.example.administrator.cnmar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.administrator.cnmar.helper.UniversalHelper;
import com.example.administrator.cnmar.http.VolleyHelper;
import com.example.administrator.cnmar.model.MaterialStock;

public class StockDetailActivity extends AppCompatActivity {
    private static final String URL_STOCK_DETAIL="http://139.196.104.170:8092/material_stock/detail/{id}";
    private TextView tvTitle;
    private ImageView ivLeftArrow,ivScann;
    private TextView tvMaterialCode,tvMaterialName,tvSize,tvUnit,tvRemark,
                     tvSupplierCode,tvIsMixed,tvStockSum,tvMinStock,tvMaxStock;
    private String strUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_detail);

        init();
//        取出传递到库存详情页面的id
        int id=getIntent().getIntExtra("ID",0);
        strUrl=URL_STOCK_DETAIL.replace("{id}",String.valueOf(id));
        getStockDetailFromNet();
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if(keyCode==KeyEvent.ACTION_DOWN){
//            Intent intent=new Intent(this,MaterialWarehouseActivity.class);
//            startActivity(intent);
//            finish();
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    public void init(){
        tvTitle= (TextView) findViewById(R.id.title);
        ivLeftArrow= (ImageView) findViewById(R.id.left_img);
        UniversalHelper.backToLastActivity(this,ivLeftArrow,new MaterialWarehouseActivity());
        ivScann= (ImageView) findViewById(R.id.scann);
        tvTitle.setText("原料仓库-库存");
        tvMaterialCode= (TextView) findViewById(R.id.tvMaterialCode);
        tvMaterialName= (TextView) findViewById(R.id.tvMaterialName);
        tvSize= (TextView) findViewById(R.id.tvSize);
        tvUnit= (TextView) findViewById(R.id.tvUnit);
        tvRemark= (TextView) findViewById(R.id.tvRemark);
        tvSupplierCode= (TextView) findViewById(R.id.tvSupplierCode);
        tvIsMixed= (TextView) findViewById(R.id.tvIsMixed);
        tvStockSum= (TextView) findViewById(R.id.tvStockSum);
        tvMinStock= (TextView) findViewById(R.id.tvMinStock);
        tvMaxStock= (TextView) findViewById(R.id.tvMaxStock);
    }
    public void getStockDetailFromNet(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue queue= Volley.newRequestQueue(StockDetailActivity.this);
                StringRequest stringRequest=new StringRequest(strUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        String json= VolleyHelper.getJson(s);
                        com.example.administrator.cnmar.model.Response response= JSON.parseObject(json, com.example.administrator.cnmar.model.Response.class);
                        MaterialStock materialStock=JSON.parseObject(response.getData().toString(),MaterialStock.class);
                        tvMaterialCode.setText(materialStock.getMaterial().getCode());
                        tvMaterialName.setText(materialStock.getMaterial().getName());
                        tvSize.setText(materialStock.getMaterial().getSpec());
                        tvUnit.setText(materialStock.getMaterial().getUnit().getName());
                        tvRemark.setText(materialStock.getMaterial().getRemark());
                        tvSupplierCode.setText(materialStock.getMaterial().getSupply().getCode());
                        tvIsMixed.setText(materialStock.getMaterial().getMixTypeVo().getValue());
                        tvStockSum.setText(String.valueOf(materialStock.getStock()));
                        tvMinStock.setText(String.valueOf(materialStock.getMaterial().getMinStock()));
                        tvMaxStock.setText(String.valueOf(materialStock.getMaterial().getMaxStock()));
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
