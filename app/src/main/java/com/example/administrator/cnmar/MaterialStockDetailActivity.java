package com.example.administrator.cnmar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.administrator.cnmar.helper.UniversalHelper;
import com.example.administrator.cnmar.http.VolleyHelper;

import java.util.List;

import component.material.model.MaterialSpaceStock;
import component.material.model.MaterialStock;

public class MaterialStockDetailActivity extends AppCompatActivity {
    private static final String URL_STOCK_DETAIL="http://139.196.104.170:8092/material_stock/detail/{id}";
    private TextView tvTitle;
    private ImageView ivLeftArrow,ivScann;
    private TextView tvMaterialCode,tvMaterialName,tvSize,tvUnit,tvRemark,
                     tvSupplierCode,tvIsMixed,tvStockSum,tvMinStock,tvMaxStock;
    private String strUrl;
    private ListView lvSpace;
    private LinearLayout llLeftArrow;

    private SpaceAdapter myAdapter;
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            Intent intent=new Intent(this,MaterialStockActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void init(){
        tvTitle= (TextView) findViewById(R.id.title);
        ivLeftArrow= (ImageView) findViewById(R.id.left_img);
        llLeftArrow= (LinearLayout) findViewById(R.id.left_arrow);
        UniversalHelper.backToLastActivity(this,llLeftArrow,new MaterialStockActivity());

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
        lvSpace= (ListView) findViewById(R.id.lvSpace);
        lvSpace.addFooterView(new ViewStub(this));

    }
    public void getStockDetailFromNet(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue queue= Volley.newRequestQueue(MaterialStockDetailActivity.this);
                StringRequest stringRequest=new StringRequest(strUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        String json= VolleyHelper.getJson(s);
                        component.common.model.Response response= JSON.parseObject(json,component.common.model.Response.class );
                        MaterialStock materialStock=JSON.parseObject(response.getData().toString(),MaterialStock.class);
//                        得到列表的数据源
                        List<MaterialSpaceStock> list=materialStock.getSpaceStocks();
                        myAdapter=new SpaceAdapter(MaterialStockDetailActivity.this,list);
                        lvSpace.setAdapter(myAdapter);

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

    public class SpaceAdapter extends BaseAdapter{
       private Context context;
        private List<MaterialSpaceStock> list=null;

        public SpaceAdapter(Context context, List<MaterialSpaceStock> list) {
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
        public View getView(int position, View convertView, ViewGroup parent) {
              ViewHolder holder=null;
            if(convertView==null){
                convertView= LayoutInflater.from(context).inflate(R.layout.space_item,parent,false);
                holder=new ViewHolder();
                holder.tvSpaceCode= (TextView) convertView.findViewById(R.id.spaceCode);
                holder.tvSpaceName= (TextView) convertView.findViewById(R.id.spaceName);
                holder.tvSpaceCapacity= (TextView) convertView.findViewById(R.id.spaceCapacity);
                holder.tvStockNum= (TextView) convertView.findViewById(R.id.stockNum);
                convertView.setTag(holder);
            }else
                holder= (ViewHolder) convertView.getTag();
            holder.tvSpaceCode.setText(list.get(position).getSpace().getCode());
            holder.tvSpaceName.setText(list.get(position).getSpace().getName());
            holder.tvSpaceCapacity.setText(String.valueOf(list.get(position).getSpace().getCapacity()));
            holder.tvStockNum.setText(String.valueOf(list.get(position).getStock()));
            return convertView;
        }

        public class ViewHolder{
            TextView tvSpaceCode;
            TextView tvSpaceName;
            TextView tvSpaceCapacity;
            TextView tvStockNum;
        }
    }
}
