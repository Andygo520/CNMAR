package com.example.administrator.cnmar.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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

import java.text.SimpleDateFormat;
import java.util.List;

import component.basic.vo.PackTypeVo;
import component.half.model.HalfSpaceStockCheck;
import component.half.model.HalfStockCheck;

public class HalfProductCheckStockDetailActivity extends AppCompatActivity {
    private Context context=HalfProductCheckStockDetailActivity.this;
    private TextView tvCode,tvName,tvSize,tvUnit,tvMixType,tvCheckTime,tvPreCheckNum,tvAfterCheckNum,tvRemark,tvStockType;
    private TextView name1,name2,name3,name4;
    private MyListView lvSpaceInfo;
    private static String strUrl;
    private TableLayout tableLayout;
    private LinearLayout llLeftArrow;
    private TextView tvTitle;
    private int id;
    private int type; //盘点详情Fragment传递过来的原料入库类型,为0代表是扫码（有包装），为1代表是输数量
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_half_product_check_stock_detail);
        AppExit.getInstance().addActivity(this);
        init();
        id=getIntent().getIntExtra("ID",0);
        type = getIntent().getIntExtra("type", 999);
//        扫描类型
        if (type == 0) {
//           表格布局显示之前隐藏的第4列，并将第4列设置为可伸展
            tableLayout.setColumnCollapsed(7, false);
            tableLayout.setColumnCollapsed(8, false);
            tableLayout.setColumnStretchable(7, true);
        }
        strUrl= UrlHelper.URL_HALF_PRODUCT_CHECK_QUERY_DETAIL.replace("{ID}",String.valueOf(id));
        strUrl= UniversalHelper.getTokenUrl(strUrl);

        getCheckListFromNet();
    }
    public void init(){
        tvTitle= (TextView) findViewById(R.id.title);
        tvTitle.setText("半成品仓库-盘点详情");
        tableLayout = (TableLayout) findViewById(R.id.tableLayout);
        llLeftArrow= (LinearLayout) findViewById(R.id.left_arrow);
        llLeftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        name1= (TextView) findViewById(R.id.column1);
        name2= (TextView) findViewById(R.id.column2);
        name3= (TextView) findViewById(R.id.column3);
        name4= (TextView) findViewById(R.id.column4);

        name1.setText("仓位编码");
        name2.setText("盘点前数量");
        name3.setText("盘点后数量");
        name4.setText("二维码序列号");

        tvCode= (TextView) findViewById(R.id.tv11);
        tvName= (TextView) findViewById(R.id.tv12);
        tvSize= (TextView) findViewById(R.id.tv21);
        tvUnit= (TextView) findViewById(R.id.tv22);
        tvStockType= (TextView) findViewById(R.id.tv31);
        tvMixType= (TextView) findViewById(R.id.tv32);
        tvRemark= (TextView) findViewById(R.id.tv41);
        tvCheckTime= (TextView) findViewById(R.id.tv42);
        tvPreCheckNum= (TextView) findViewById(R.id.tv51);
        tvAfterCheckNum= (TextView) findViewById(R.id.tv52);


        lvSpaceInfo= (MyListView) findViewById(R.id.lvTable);
//        lvSpaceInfo.addFooterView(new ViewStub(this));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
           finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void getCheckListFromNet(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue queue= Volley.newRequestQueue(context);
                StringRequest stringRequest=new StringRequest(strUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        String json= VolleyHelper.getJson(s);
//                        Log.d("RRRRR",json);
                        component.common.model.Response response= JSON.parseObject(json,component.common.model.Response.class );
                        HalfStockCheck halfStockCheck=JSON.parseObject(response.getData().toString(),HalfStockCheck.class);
//                        得到列表的数据源
                        List<HalfSpaceStockCheck> list=halfStockCheck.getSpaceChecks();

 //     输入数量的显示3列
                        if (type == 1) {
                            SpaceInfoAdapter myAdapter = new SpaceInfoAdapter(context, list);
                            lvSpaceInfo.setAdapter(myAdapter);
                        }
//      扫码的显示4列
                        else if (type == 0) {
                            SpaceInfoAdapter1 myAdapter = new SpaceInfoAdapter1(context, list);
                            lvSpaceInfo.setAdapter(myAdapter);
                        }

                        tvCode.setText(halfStockCheck.getHalf().getCode());
                        tvName.setText(halfStockCheck.getHalf().getName());
                        tvSize.setText(halfStockCheck.getHalf().getSpec());
 //                        有包装的，单位格式为“9个/袋”
                        if (halfStockCheck.getHalf().getPackType() != PackTypeVo.empty.getKey())
                            tvUnit.setText(halfStockCheck.getHalf().getPackNum()
                                    + halfStockCheck.getHalf().getUnit().getName()
                                    + " / " + halfStockCheck.getHalf().getPackTypeVo().getValue().substring(1, 2));
                        else
                            tvUnit.setText(halfStockCheck.getHalf().getUnit().getName());

                        tvMixType.setText(halfStockCheck.getHalf().getMixTypeVo().getValue());
                        tvRemark.setText(halfStockCheck.getHalf().getRemark());
                        tvStockType.setText(halfStockCheck.getHalf().getStockTypeVo().getValue());

                        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        tvCheckTime.setText(sdf.format(halfStockCheck.getCtime()));

                        tvPreCheckNum.setText(halfStockCheck.getBeforeStock()+halfStockCheck.getHalf().getUnit().getName());
                        tvAfterCheckNum.setText(halfStockCheck.getAfterStock()+halfStockCheck.getHalf().getUnit().getName());
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
    
    //    该适配器用来显示3列（输数量类型）
    public class SpaceInfoAdapter extends BaseAdapter {
        private Context context;
        private List<HalfSpaceStockCheck> list=null;


        public SpaceInfoAdapter(Context context, List<HalfSpaceStockCheck> list) {
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
                convertView= LayoutInflater.from(context).inflate(R.layout.table_3_item,parent,false);
                holder=new ViewHolder();
                holder.tvSpaceCode= (TextView) convertView.findViewById(R.id.column1);
                holder.tvPreNum= (TextView) convertView.findViewById(R.id.column2);
                holder.tvAfterNum= (TextView) convertView.findViewById(R.id.column3);

                convertView.setTag(holder);
            }else
                holder= (ViewHolder) convertView.getTag();

            holder.tvSpaceCode.setText(list.get(position).getSpace().getCode());
            holder.tvPreNum.setText(String.valueOf(list.get(position).getBeforeStock()));
            holder.tvAfterNum.setText(String.valueOf(list.get(position).getAfterStock()));

            return convertView;
        }

        public class ViewHolder{
            TextView tvSpaceCode;
            TextView tvPreNum;
            TextView tvAfterNum;
        }
    }
    
    //    该适配器用来显示4列数据（在显示有包装的扫码产品盘点详情的时候使用该Adapter）
    public class SpaceInfoAdapter1 extends BaseAdapter {
        private Context context;
        private List<HalfSpaceStockCheck> list = null;


        public SpaceInfoAdapter1(Context context, List<HalfSpaceStockCheck> list) {
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
                holder.tvPreNum = (TextView) convertView.findViewById(R.id.column2);
                holder.tvAfterNum = (TextView) convertView.findViewById(R.id.column3);
                holder.tvInOrderSpaceId = (TextView) convertView.findViewById(R.id.column4);

                convertView.setTag(holder);
            } else
                holder = (ViewHolder) convertView.getTag();

            holder.tvSpaceCode.setText(list.get(position).getSpace().getCode());
            holder.tvPreNum.setText(String.valueOf(list.get(position).getBeforeStock()));
            holder.tvAfterNum.setText(String.valueOf(list.get(position).getAfterStock()));
            holder.tvInOrderSpaceId.setText(String.valueOf(list.get(position).getInOrderSpaceId()));

            return convertView;
        }

        public class ViewHolder {
            TextView tvSpaceCode;
            TextView tvPreNum;
            TextView tvAfterNum;
            TextView tvInOrderSpaceId;  //二维码序列号
        }
    }
}
