package com.example.administrator.cnmar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.List;

import component.material.model.MaterialSpaceStock;
import component.material.model.MaterialStock;

public class MaterialCheckStockManageActivity extends AppCompatActivity {
    private static final String URL_CHECK_STOCK="http://benxiao.cnmar.com:8092/material_stock_check_manage/detail/{ID}";
    private static final String URL_CHECK_COMMIT="http://benxiao.cnmar.com:8092/material_stock_check_manage/check_commit?stockId={stockId}&spaceStockIds={spaceStockIds}&spaceIds={spaceIds}&beforeStocks={beforeStocks}&afterStocks={afterStocks}";
    private TextView tvMaterialCode,tvMaterialName,tvSize,tvUnit,tvRemark,tvProviderCode,tvMixType,tvStockNum;
    private TextView name1,name2,name3,name4;
    private MyListView lvSpaceInfo;
    private static String strUrl;
    private LinearLayout llLeftArrow;
    private Button btnSubmit;
    private TextView tvTitle;
    private int id;
    private int index=-1;
    private String spaceStockIds="";
    private String spaceIds="";
    private String beforeStocks="";
    private String afterStocks="";
    private HashMap<Integer,String> map=new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_stock_manage);
        init();
        id=getIntent().getIntExtra("ID",0);
        strUrl=URL_CHECK_STOCK.replace("{ID}",String.valueOf(id));
        strUrl= UniversalHelper.getTokenUrl(strUrl);
        getCheckListFromNet();
    }

    public void init(){
        tvTitle= (TextView) findViewById(R.id.title);
        tvTitle.setText("原料仓库-盘点管理");
        llLeftArrow= (LinearLayout) findViewById(R.id.left_arrow);
        llLeftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MaterialCheckStockManageActivity.this,MaterialStockActivity.class);
                intent.putExtra("flag",3);
                startActivity(intent);
            }
        });


        name1= (TextView) findViewById(R.id.column1);
        name2= (TextView) findViewById(R.id.column2);
        name3= (TextView) findViewById(R.id.column3);
        name4= (TextView) findViewById(R.id.column4);
        btnSubmit= (Button) findViewById(R.id.btnSubmit);
        name1.setText("仓位编码");
        name2.setText("仓位名称");
        name3.setText("库存数量");
        name4.setText("盘点数量");

        tvMaterialCode= (TextView) findViewById(R.id.tv11);
        tvMaterialName= (TextView) findViewById(R.id.tv12);
        tvSize= (TextView) findViewById(R.id.tv21);
        tvUnit= (TextView) findViewById(R.id.tv22);
        tvRemark= (TextView) findViewById(R.id.tv31);
        tvProviderCode= (TextView) findViewById(R.id.tv32);
        tvMixType= (TextView) findViewById(R.id.tv41);
        tvStockNum= (TextView) findViewById(R.id.tv42);

        lvSpaceInfo= (MyListView) findViewById(R.id.lvTable);
//        lvSpaceInfo.addFooterView(new ViewStub(this));

        btnSubmit.setVisibility(View.VISIBLE);
        btnSubmit.setText("提交");

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String spaceStockIds1=spaceStockIds.substring(0,spaceStockIds.length()-1);
                String spaceIds1=spaceIds.substring(0,spaceIds.length()-1);
                String beforeStocks1=beforeStocks.substring(0,beforeStocks.length()-1);
                for(int i=0;i<map.size();i++){
                    afterStocks+=map.get(i)+",";
                }
                String afterStocks1=afterStocks.substring(0,afterStocks.length()-1);
                String url=URL_CHECK_COMMIT.replace("{stockId}",String.valueOf(id)).replace("{spaceStockIds}",spaceStockIds1).replace("{spaceIds}",spaceIds1).replace("{beforeStocks}",beforeStocks1).replace("{afterStocks}",afterStocks1);
//                Log.d("tAGTAG",url);
                url=UniversalHelper.getTokenUrl(url);
                sendRequest(url);
            }
        });

    }
    public void sendRequest(String url){
        RequestQueue queue= Volley.newRequestQueue(this);
        StringRequest stringRequest=new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                String json= VolleyHelper.getJson(s);
                component.common.model.Response response= JSON.parseObject(json,component.common.model.Response.class);
                if(!response.isStatus()){
                    Toast.makeText(MaterialCheckStockManageActivity.this,response.getMsg(),Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent=new Intent(MaterialCheckStockManageActivity.this,MaterialStockActivity.class);
                    intent.putExtra("flag",3);
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
    public void getCheckListFromNet(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue queue= Volley.newRequestQueue(MaterialCheckStockManageActivity.this);
                StringRequest stringRequest=new StringRequest(strUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        String json= VolleyHelper.getJson(s);
//                        Log.d("RRRRR",json);
                        component.common.model.Response response= JSON.parseObject(json,component.common.model.Response.class );
                        MaterialStock materialStock=JSON.parseObject(response.getData().toString(),MaterialStock.class);
//                        得到列表的数据源
                        List<MaterialSpaceStock> list=materialStock.getSpaceStocks();

                        SpaceInfoAdapter myAdapter=new SpaceInfoAdapter(MaterialCheckStockManageActivity.this,list);
                        lvSpaceInfo.setAdapter(myAdapter);

                        tvMaterialCode.setText(materialStock.getMaterial().getCode());
                        tvMaterialName.setText(materialStock.getMaterial().getName());
                        tvSize.setText(materialStock.getMaterial().getSpec());
                        tvUnit.setText(materialStock.getMaterial().getUnit().getName());
                        tvRemark.setText(materialStock.getMaterial().getRemark());
                        tvProviderCode.setText(materialStock.getMaterial().getSupply().getCode());
                        tvMixType.setText(materialStock.getMaterial().getMixTypeVo().getValue());
                        tvStockNum.setText(String.valueOf(materialStock.getStock()));
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

    public class SpaceInfoAdapter extends BaseAdapter {
        private Context context;
        private List<MaterialSpaceStock> list=null;


        public SpaceInfoAdapter(Context context, List<MaterialSpaceStock> list) {
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
                convertView= LayoutInflater.from(context).inflate(R.layout.table_list_edit_item,parent,false);
                holder=new ViewHolder();
                holder.tvSpaceCode= (TextView) convertView.findViewById(R.id.column1);
                holder.tvSpaceName= (TextView) convertView.findViewById(R.id.column2);
                holder.tvStockNum= (TextView) convertView.findViewById(R.id.column3);
                holder.tvCheckNum= (EditText) convertView.findViewById(R.id.column4);

                convertView.setTag(holder);
            }else
                holder= (ViewHolder) convertView.getTag();

            holder.tvSpaceCode.setText(list.get(position).getSpace().getCode());
            holder.tvSpaceName.setText(list.get(position).getSpace().getName());
            holder.tvStockNum.setText(String.valueOf(list.get(position).getStock()));
            holder.tvCheckNum.setText("");


            holder.tvCheckNum.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        index = position;
                    }
                    return false;
                }
            });

            spaceStockIds+=String.valueOf(list.get(position).getId())+",";
            spaceIds+=String.valueOf(list.get(position).getSpaceId())+",";
            beforeStocks+=String.valueOf(list.get(position).getStock())+",";

                if(holder.tvCheckNum.getText().toString().equals("0")){
                    map.put(position,holder.tvCheckNum.getText().toString());
                }else{
                    holder.tvCheckNum.addTextChangedListener(new TextWatcher() {
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
//                                Log.d("position",s.toString());
                            }
                        }
                    });
                }

            holder.tvCheckNum.clearFocus();
            if (index != -1 && index == position) {
                // 如果当前的行下标和点击事件中保存的index一致，手动为EditText设置焦点。
                holder.tvCheckNum.requestFocus();
            }
            holder.tvCheckNum.setSelection(holder.tvCheckNum .getText().length());
            return convertView;
        }

        public class ViewHolder{
            TextView tvSpaceCode;
            TextView tvSpaceName;
            TextView tvStockNum;
            EditText tvCheckNum;
        }
    }
}
