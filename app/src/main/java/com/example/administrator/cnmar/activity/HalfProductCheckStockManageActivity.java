package com.example.administrator.cnmar.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.HashMap;
import java.util.List;

import component.basic.vo.PackTypeVo;
import component.half.model.HalfSpaceStock;
import component.half.model.HalfStock;

public class HalfProductCheckStockManageActivity extends AppCompatActivity {
    private Context context = HalfProductCheckStockManageActivity.this;
    private TextView tvCode, tvName, tvSize, tvUnit, tvMixType, tvStockNum, tvStockType, tvRemark;
    private TextView name1, name2, name3, name4;
    private MyListView lvSpaceInfo;
    private static String strUrl;
    private TableLayout tableLayout;
    private LinearLayout llLeftArrow;
    private Button btnSubmit;
    private TextView tvTitle;
    private int id;
    private int type; //盘点管理Fragment传递过来的原料入库类型,为0代表是扫码（有包装），为1代表是输数量
    private String spaceStockIds = "";
    private String spaceIds = "";
    private String beforeStocks = "";
    private String inOrderSpaceIds = "";
    private HashMap<Integer, String> map = new HashMap<>();
    private SpaceInfoAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_half_product_check_stock_manage);
        AppExit.getInstance().addActivity(this);
        init();
        id = getIntent().getIntExtra("ID", 0);
        type = getIntent().getIntExtra("type", 999);
        //        扫描类型
        if (type == 0) {
//           表格布局显示之前隐藏的第4列，并将第4列设置为可伸展
            tableLayout.setColumnCollapsed(7, false);
            tableLayout.setColumnCollapsed(8, false);
            tableLayout.setColumnStretchable(7, true);
        }
        strUrl = UrlHelper.URL_HALF_PRODUCT_CHECK_MANAGE_DETAIL.replace("{ID}", String.valueOf(id));
        strUrl = UniversalHelper.getTokenUrl(strUrl);

        getCheckListFromNet();
    }

    public void init() {
        tvTitle = (TextView) findViewById(R.id.title);
        tvTitle.setText("半成品仓库-盘点管理");
        tableLayout = (TableLayout) findViewById(R.id.tableLayout);
        llLeftArrow = (LinearLayout) findViewById(R.id.left_arrow);
        llLeftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HalfProductStockActivity.class);
                intent.putExtra("flag", 3);
                startActivity(intent);
            }
        });


        name1 = (TextView) findViewById(R.id.column1);
        name2 = (TextView) findViewById(R.id.column2);
        name3 = (TextView) findViewById(R.id.column3);
        name4 = (TextView) findViewById(R.id.column4);

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        name1.setText("仓位编码");
        name2.setText("库存数量");
        name3.setText("盘点数量");
        name4.setText("二维码序列号");

        tvCode = (TextView) findViewById(R.id.tv11);
        tvName = (TextView) findViewById(R.id.tv12);
        tvSize = (TextView) findViewById(R.id.tv21);
        tvUnit = (TextView) findViewById(R.id.tv22);
        tvStockType = (TextView) findViewById(R.id.tv31);
        tvMixType = (TextView) findViewById(R.id.tv32);
        tvRemark = (TextView) findViewById(R.id.tv41);
        tvStockNum = (TextView) findViewById(R.id.tv42);

        lvSpaceInfo = (MyListView) findViewById(R.id.lvTable);
//        lvSpaceInfo.addFooterView(new ViewStub(this));

        btnSubmit.setVisibility(View.VISIBLE);
        btnSubmit.setText("提交");

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String spaceStockIds1 = spaceStockIds.substring(0, spaceStockIds.length() - 1);
                String spaceIds1 = spaceIds.substring(0, spaceIds.length() - 1);
                String inOrderSpaceIds1 = inOrderSpaceIds.substring(0, inOrderSpaceIds.length() - 1);
                String beforeStocks1 = beforeStocks.substring(0, beforeStocks.length() - 1);
                String afterStocks = "";//记录盘点后数量
                for (int i = 0; i < map.size(); i++) {
                    afterStocks += map.get(i) + ",";
                }
                String afterStocks1 = afterStocks.substring(0, afterStocks.length() - 1);
                String url = UrlHelper.URL_HALF_PRODUCT_CHECK_COMMIT.replace("{stockId}", String.valueOf(id)).replace("{spaceStockIds}", spaceStockIds1).replace("{spaceIds}", spaceIds1).replace("{inOrderSpaceIds}", inOrderSpaceIds1).replace("{beforeStocks}", beforeStocks1).replace("{afterStocks}", afterStocks1);
                url = UniversalHelper.getTokenUrl(url);
                UniversalHelper.showProgressDialog(context);
                sendRequest(url);
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(context, HalfProductStockActivity.class);
            intent.putExtra("flag", 3);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void sendRequest(String url) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                UniversalHelper.dismissProgressDialog();
                String json = VolleyHelper.getJson(s);
                component.common.model.Response response = JSON.parseObject(json, component.common.model.Response.class);
                if (!response.isStatus()) {
                    Toast.makeText(context, response.getMsg(), Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(context, HalfProductStockActivity.class);
                    intent.putExtra("flag", 3);
                    startActivity(intent);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                UniversalHelper.dismissProgressDialog();
                Toast.makeText(context, "连接超时", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);

    }

    public void getCheckListFromNet() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue queue = Volley.newRequestQueue(context);
                StringRequest stringRequest = new StringRequest(strUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        String json = VolleyHelper.getJson(s);
//                        Log.d("RRRRR",json);
                        component.common.model.Response response = JSON.parseObject(json, component.common.model.Response.class);
                        HalfStock halfStock = JSON.parseObject(response.getData().toString(), HalfStock.class);
//                        得到列表的数据源
                        List<HalfSpaceStock> list = halfStock.getSpaceStocks();

//     输入数量的原料盘点时显示4列
                        if (type == 1) {
                            SpaceInfoAdapter myAdapter = new SpaceInfoAdapter(context, list);
                            lvSpaceInfo.setAdapter(myAdapter);
                        }
//     扫码的原料盘点时显示5列
                        else if (type == 0) {
                            SpaceInfoAdapter1 myAdapter = new SpaceInfoAdapter1(context, list);
                            lvSpaceInfo.setAdapter(myAdapter);
                        }
                        tvCode.setText(halfStock.getHalf().getCode());
                        tvName.setText(halfStock.getHalf().getName());
                        tvSize.setText(halfStock.getHalf().getSpec());
//                        有包装的，单位格式为“9个/袋”
                        if (halfStock.getHalf().getPackType() != PackTypeVo.empty.getKey())
                            tvUnit.setText(halfStock.getHalf().getPackNum()
                                    + halfStock.getHalf().getUnit().getName()
                                    + " / " + halfStock.getHalf().getPackTypeVo().getValue().substring(1, 2));
                        else
                            tvUnit.setText(halfStock.getHalf().getUnit().getName());

                        tvRemark.setText(halfStock.getHalf().getRemark());
                        tvStockType.setText(halfStock.getHalf().getStockTypeVo().getValue());
                        tvMixType.setText(halfStock.getHalf().getMixTypeVo().getValue());
                        tvStockNum.setText(halfStock.getStock() + halfStock.getHalf().getUnit().getName());
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

    //    该适配器用来显示3列
    public class SpaceInfoAdapter extends BaseAdapter {
        private Context context;
        private List<HalfSpaceStock> list = null;


        public SpaceInfoAdapter(Context context, List<HalfSpaceStock> list) {
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
                convertView = LayoutInflater.from(context).inflate(R.layout.table_list_edit_item, parent, false);
                holder = new ViewHolder();
                holder.tvSpaceCode = (TextView) convertView.findViewById(R.id.column1);
                holder.tvStockNum = (TextView) convertView.findViewById(R.id.column2);
                holder.tvCheckNum = (EditText) convertView.findViewById(R.id.column3);
                convertView.setTag(holder);
            } else
                holder = (ViewHolder) convertView.getTag();

            holder.tvSpaceCode.setText(list.get(position).getSpace().getCode());
            holder.tvStockNum.setText(String.valueOf(list.get(position).getStock()));
            holder.tvCheckNum.setText("");

            spaceStockIds += String.valueOf(list.get(position).getId()) + ",";
            spaceIds += String.valueOf(list.get(position).getSpaceId()) + ",";
            inOrderSpaceIds += String.valueOf(list.get(position).getInOrderSpaceId()) + ",";
            beforeStocks += String.valueOf(list.get(position).getStock()) + ",";

//            默认每个位置的盘点后数量为空字符（即不做盘点，数量不变），只有数量变化的时候才保存用户的输入
            map.put(position, "");
            holder.tvCheckNum.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    //                    有输入就保存输入的数值，输入后又清除的位置保存空字符
                    if (s.length() > 0) {
                        map.put(position, s.toString());
                    } else {
                        map.put(position, "");
                    }
                }
            });

            return convertView;
        }

        public class ViewHolder {
            TextView tvSpaceCode;
            TextView tvStockNum;
            EditText tvCheckNum;
        }
    }

    //    该适配器用来显示4列数据（在有包装的扫码产品盘点的时候使用该Adapter）
    public class SpaceInfoAdapter1 extends BaseAdapter {
        private Context context;
        private List<HalfSpaceStock> list = null;


        public SpaceInfoAdapter1(Context context, List<HalfSpaceStock> list) {
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
                convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
                holder = new ViewHolder();
                holder.tvSpaceCode = (TextView) convertView.findViewById(R.id.column1);
                holder.tvStockNum = (TextView) convertView.findViewById(R.id.column2);
                holder.tvCheckNum = (EditText) convertView.findViewById(R.id.column3);
                holder.tvInOrderSpaceId = (TextView) convertView.findViewById(R.id.column4);

                convertView.setTag(holder);
            } else
                holder = (ViewHolder) convertView.getTag();

            holder.tvSpaceCode.setText(list.get(position).getSpace().getCode());
            holder.tvStockNum.setText(String.valueOf(list.get(position).getStock()));
            holder.tvCheckNum.setText("");
            holder.tvInOrderSpaceId.setText(String.valueOf(list.get(position).getInOrderSpaceId()));

            spaceStockIds += String.valueOf(list.get(position).getId()) + ",";
            spaceIds += String.valueOf(list.get(position).getSpaceId()) + ",";
            inOrderSpaceIds += String.valueOf(list.get(position).getInOrderSpaceId()) + ",";
            beforeStocks += String.valueOf(list.get(position).getStock()) + ",";

//            默认每个位置的盘点后数量为空字符（即不做盘点，数量不变），只有数量变化的时候才保存用户的输入
            map.put(position, "");
            holder.tvCheckNum.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    //                    有输入就保存输入的数值，输入后又清除的该位置保存空字符
                    if (s.length() > 0) {
                        map.put(position, s.toString());
                    } else {
                        map.put(position, "");
                    }
                }
            });
            return convertView;
        }

        public class ViewHolder {
            TextView tvSpaceCode;
            TextView tvStockNum;
            EditText tvCheckNum;
            TextView tvInOrderSpaceId;  //二维码序列号
        }
    }
}
