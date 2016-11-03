package com.example.administrator.cnmar.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.administrator.cnmar.R;
import com.example.administrator.cnmar.entity.MyListView;
import com.example.administrator.cnmar.helper.UniversalHelper;
import com.example.administrator.cnmar.helper.UrlHelper;
import com.example.administrator.cnmar.http.VolleyHelper;

import java.util.List;

import component.half.model.HalfSpaceStock;
import component.half.model.HalfStock;

public class HalfProductStockDetailActivity extends AppCompatActivity {
    private TextView tvTitle;
    private ImageView ivLeftArrow;
    private TextView tvCode,tvName,tvSize,tvUnit,
            tvIsMixed,tvStockSum,tvMinStock,tvMaxStock;
    private String strUrl;
    private MyListView lvSpace;
    private LinearLayout llLeftArrow;

    private SpaceAdapter myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_half_product_stock_detail);

        init();
//        取出传递到库存详情页面的id
        int id=getIntent().getIntExtra("ID",0);
        strUrl= UrlHelper.URL_HALF_PRODUCT_STOCK_DETAIL.replace("{id}",String.valueOf(id));
        strUrl= UniversalHelper.getTokenUrl(strUrl);
        getStockDetailFromNet();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            Intent intent=new Intent(this, HalfProductStockActivity.class);
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
        UniversalHelper.backToLastActivity(this,llLeftArrow,new HalfProductStockActivity());

        tvTitle.setText("半成品仓库-库存");
        tvCode= (TextView) findViewById(R.id.tvCode);
        tvName= (TextView) findViewById(R.id.tvName);
        tvSize= (TextView) findViewById(R.id.tvSize);
        tvUnit= (TextView) findViewById(R.id.tvUnit);
        tvIsMixed= (TextView) findViewById(R.id.tvIsMixed);
        tvStockSum= (TextView) findViewById(R.id.tvStockSum);
        tvMinStock= (TextView) findViewById(R.id.tvMinStock);
        tvMaxStock= (TextView) findViewById(R.id.tvMaxStock);
        lvSpace= (MyListView) findViewById(R.id.lvSpace);
//        lvSpace.addFooterView(new ViewStub(this));

    }
    public void getStockDetailFromNet(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue queue= Volley.newRequestQueue(HalfProductStockDetailActivity.this);
                StringRequest stringRequest=new StringRequest(strUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        String json= VolleyHelper.getJson(s);
                        component.common.model.Response response= JSON.parseObject(json,component.common.model.Response.class );
                        HalfStock halfStock=JSON.parseObject(response.getData().toString(),HalfStock.class);
//                        得到列表的数据源
                        List<HalfSpaceStock> list=halfStock.getSpaceStocks();
                        myAdapter=new SpaceAdapter(HalfProductStockDetailActivity.this,list);
                        lvSpace.setAdapter(myAdapter);

                        tvCode.setText(halfStock.getHalf().getCode());
                        tvName.setText(halfStock.getHalf().getName());
                        tvSize.setText(halfStock.getHalf().getSpec());
                        tvUnit.setText(halfStock.getHalf().getUnit().getName());
                        tvIsMixed.setText(halfStock.getHalf().getMixTypeVo().getValue());
                        tvStockSum.setText(String.valueOf(halfStock.getStock()));
                        tvMinStock.setText(String.valueOf(halfStock.getHalf().getMinStock()));
                        tvMaxStock.setText(String.valueOf(halfStock.getHalf().getMaxStock()));
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

    public class SpaceAdapter extends BaseAdapter {
        private Context context;
        private List<HalfSpaceStock> list=null;

        public SpaceAdapter(Context context, List<HalfSpaceStock> list) {
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
