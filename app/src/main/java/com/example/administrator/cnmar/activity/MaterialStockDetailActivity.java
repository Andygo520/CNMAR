package com.example.administrator.cnmar.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.administrator.cnmar.AppExit;
import com.example.administrator.cnmar.R;
import com.example.administrator.cnmar.entity.MyListView;
import com.example.administrator.cnmar.helper.UniversalHelper;
import com.example.administrator.cnmar.helper.UrlHelper;
import com.example.administrator.cnmar.helper.VolleyHelper;

import java.util.List;

import component.basic.vo.PackTypeVo;
import component.basic.vo.StockTypeVo;
import component.material.model.MaterialSpaceStock;
import component.material.model.MaterialStock;

public class MaterialStockDetailActivity extends AppCompatActivity {
    private TextView tvTitle;
    private ImageView ivLeftArrow, ivScann;
    private TextView tvMaterialCode, tvMaterialName, tvSize, tvUnit, tvStockType,
            tvSupplierCode, tvIsMixed, tvStockSum, tvMinStock, tvMaxStock;
    private TextView name1,name2,name3,name4; //仓位信息的4个字段
    private String strUrl;
    private MyListView lvSpace;
    private LinearLayout llLeftArrow;
    private TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_detail);
        AppExit.getInstance().addActivity(this);
        init();
//        取出传递到库存详情页面的id
        int id = getIntent().getIntExtra("ID", 0);
        strUrl = UrlHelper.URL_STOCK_DETAIL.replace("{id}", String.valueOf(id));
        strUrl = UniversalHelper.getTokenUrl(strUrl);
        getStockDetailFromNet();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void init() {
        tvTitle = (TextView) findViewById(R.id.title);
        ivLeftArrow = (ImageView) findViewById(R.id.left_img);
        llLeftArrow = (LinearLayout) findViewById(R.id.left_arrow);
        llLeftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });

        ivScann = (ImageView) findViewById(R.id.scann);
        tvTitle.setText("原料仓库-库存");
        tvMaterialCode = (TextView) findViewById(R.id.tvMaterialCode);
        tvMaterialName = (TextView) findViewById(R.id.tvMaterialName);
        tvSize = (TextView) findViewById(R.id.tvSize);
        tvUnit = (TextView) findViewById(R.id.tvUnit);
        tvStockType = (TextView) findViewById(R.id.tvStockType);
        tvSupplierCode = (TextView) findViewById(R.id.tvSupplierCode);
        tvIsMixed = (TextView) findViewById(R.id.tvIsMixed);
        tvStockSum = (TextView) findViewById(R.id.tvStockSum);
        tvMinStock = (TextView) findViewById(R.id.tvMinStock);
        tvMaxStock = (TextView) findViewById(R.id.tvMaxStock);
        lvSpace = (MyListView) findViewById(R.id.lvTable);

        tableLayout = (TableLayout) findViewById(R.id.tableLayout);
        name1= (TextView) findViewById(R.id.column1);
        name2= (TextView) findViewById(R.id.column2);
        name3= (TextView) findViewById(R.id.column3);
        name4 = (TextView) findViewById(R.id.column4);

        name1.setText("仓位编码");
        name2.setText("仓位容量");
        name3.setText("库存数量");
        name4.setText("二维码序列号");
    }

    public void getStockDetailFromNet() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue queue = Volley.newRequestQueue(MaterialStockDetailActivity.this);
                StringRequest stringRequest = new StringRequest(strUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        String json = VolleyHelper.getJson(s);
                        component.common.model.Response response = JSON.parseObject(json, component.common.model.Response.class);
                        MaterialStock materialStock = JSON.parseObject(response.getData().toString(), MaterialStock.class);
//                        得到列表的数据源
                        List<MaterialSpaceStock> list = materialStock.getSpaceStocks();
//             扫码有包装的显示4列（多一列“二维码编号”）,否则显示3列
                        if (materialStock.getMaterial().getStockType()== StockTypeVo.scan.getKey()
                                && materialStock.getMaterial().getPackType()!=PackTypeVo.empty.getKey()){
                            tableLayout.setColumnCollapsed(7,false);
                            tableLayout.setColumnCollapsed(8,false);
                            tableLayout.setColumnStretchable(7,true);
                            SpaceAdapter1 adapter=new SpaceAdapter1(MaterialStockDetailActivity.this,list);
                            lvSpace.setAdapter(adapter);
                        }else {
                            SpaceAdapter adapter=new SpaceAdapter(MaterialStockDetailActivity.this,list);
                            lvSpace.setAdapter(adapter);
                        }

                        tvMaterialCode.setText(materialStock.getMaterial().getCode());
                        tvMaterialName.setText(materialStock.getMaterial().getName());
                        tvSize.setText(materialStock.getMaterial().getSpec());
//                        有包装的原料，单位格式为“9个/袋”
                        if (materialStock.getMaterial().getPackType() != PackTypeVo.empty.getKey())
                            tvUnit.setText(materialStock.getMaterial().getPackNum()
                                    + materialStock.getMaterial().getUnit().getName()
                                    + " / " + materialStock.getMaterial().getPackTypeVo().getValue().substring(1, 2));
                        else
                            tvUnit.setText(materialStock.getMaterial().getUnit().getName());
//                        显示出入库类型（扫码还是输入数量）
                        tvStockType.setText(materialStock.getMaterial().getStockTypeVo().getValue());

                        tvSupplierCode.setText(materialStock.getMaterial().getSupply().getCode());
                        tvIsMixed.setText(materialStock.getMaterial().getMixTypeVo().getValue());

                        tvStockSum.setText(materialStock.getStock() + materialStock.getMaterial().getUnit().getName());

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

//     该适配器用来显示四列数据
    public class SpaceAdapter extends BaseAdapter {
        private Context context;
        private List<MaterialSpaceStock> list = null;

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
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.table_3_item, parent, false);
                holder = new ViewHolder();
                holder.tvSpaceCode = (TextView) convertView.findViewById(R.id.column1);
                holder.tvSpaceCapacity = (TextView) convertView.findViewById(R.id.column2);
                holder.tvStockNum = (TextView) convertView.findViewById(R.id.column3);
                convertView.setTag(holder);
            } else
                holder = (ViewHolder) convertView.getTag();
            holder.tvSpaceCode.setText(list.get(position).getSpace().getCode());
            holder.tvSpaceCapacity.setText(String.valueOf(list.get(position).getSpace().getCapacity()));
            holder.tvStockNum.setText(String.valueOf(list.get(position).getStock()));
            return convertView;
        }

        public class ViewHolder {
            TextView tvSpaceCode;
            TextView tvSpaceCapacity;
            TextView tvStockNum;
        }
    }

//    该适配器用来显示五列数据（有包装的扫码产品多显示一列“二维码序列号”）
    public class SpaceAdapter1 extends BaseAdapter {
        private Context context;
        private List<MaterialSpaceStock> list = null;


        public SpaceAdapter1(Context context, List<MaterialSpaceStock> list) {
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
                convertView = LayoutInflater.from(context).inflate(R.layout.table_list_item, parent, false);

                holder = new ViewHolder();
                holder.tvSpaceCode = (TextView) convertView.findViewById(R.id.column1);
                holder.tvSpaceCapacity = (TextView) convertView.findViewById(R.id.column2);
                holder.tvStockNum = (TextView) convertView.findViewById(R.id.column3);
                holder.tvInOrderSpaceId = (TextView) convertView.findViewById(R.id.column4);

                convertView.setTag(holder);
            } else
                holder = (ViewHolder) convertView.getTag();

            holder.tvSpaceCode.setText(list.get(position).getSpace().getCode());
            holder.tvStockNum.setText(String.valueOf(list.get(position).getStock()));
            holder.tvSpaceCapacity.setText(String.valueOf(list.get(position).getSpace().getCapacity()));
            holder.tvInOrderSpaceId.setText(String.valueOf(list.get(position).getInOrderSpaceId()));
            return convertView;
        }

        public class ViewHolder {
            TextView tvSpaceCode;
            TextView tvSpaceCapacity;
            TextView tvStockNum;
            TextView tvInOrderSpaceId;  //二维码序列号
        }
    }
}
