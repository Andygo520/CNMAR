package com.example.administrator.cnmar.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
import android.widget.ImageView;
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
import com.example.administrator.cnmar.helper.SPHelper;
import com.example.administrator.cnmar.helper.UniversalHelper;
import com.example.administrator.cnmar.helper.UrlHelper;
import com.example.administrator.cnmar.helper.VolleyHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import component.basic.vo.PackTypeVo;
import component.basic.vo.StockTypeVo;
import component.product.model.ProductOutOrder;
import component.product.model.ProductOutOrderProduct;
import component.product.model.ProductOutOrderSpace;
import component.product.vo.OutOrderStatusVo;
import zxing.activity.CaptureActivity;

public class ProductOutOrderDetailActivity extends AppCompatActivity {
    private Context context;
    private TextView tvName11, tvName12, tvName21, tvName22, tvName31;
    private TextView tvOutOrder, tvOutBatchNo, tvPlanNo, tvRemark, tvOutOrderStatus;
    private TextView name1, name2, name3, name4, name5, name6;
    private TextView tvTitle;
    private MyListView listView;
    private static String strUrl;
    private ImageView ivScann;
    private LinearLayout llLeftArrow;
    private TableLayout tableLayout;
    private Button btnSubmit;
    private HashMap<Integer, String> map = new HashMap<>();

    private int id;
    private String outOrderSpaceIds = "";
    private String outOrderSpaceIds1 = "";
    private String preOutStocks = "";
    private String preOutStocks1 = "";
    private String outNums = "";
    private String outNums1 = "";
    private MyAdapter myAdapter;
    private List<ProductOutOrderSpace> list1;

    private String role;
    private Boolean isSuper;

    //    private int flag=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_out_order_detail);
        AppExit.getInstance().addActivity(this);
        //   从登陆页面取出用户的角色信息
        role = SPHelper.getString(this, "Role", "");
        isSuper = SPHelper.getBoolean(this, "isSuper", false);
        id = getIntent().getIntExtra("ID", 0);
        init();
        strUrl = UrlHelper.URL_PRODUCT_OUT_ORDER_DETAIL.replace("{id}", String.valueOf(id));
        strUrl = UniversalHelper.getTokenUrl(strUrl);

//        保存从列表页面传递过来的id对应的URL地址，在扫描页面返回的时候用到
        SPHelper.putString(this, "productOutURL", strUrl);
        getOutOrderDetailFromNet();


    }

    public void init() {
        context=ProductOutOrderDetailActivity.this;
        tvName11 = (TextView) findViewById(R.id.name11);
        tvName12 = (TextView) findViewById(R.id.name12);
        tvName21 = (TextView) findViewById(R.id.name21);
        tvName22 = (TextView) findViewById(R.id.name22);
        tvName31 = (TextView) findViewById(R.id.name31);
        llLeftArrow = (LinearLayout) findViewById(R.id.left_arrow);
        ivScann = (ImageView) findViewById(R.id.scann);
        tvTitle = (TextView) findViewById(R.id.title);
        tvTitle.setText("成品仓库-出库单");
        llLeftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductOutOrderDetailActivity.this.finish();
            }
        });
        tvName11.setText("出库单号");
        tvName12.setText("出库批次号");
        tvName21.setText("交付计划编号");
        tvName22.setText("备注");
        tvName31.setText("出库单状态");

//      在仓位信息默认显示6列
        tableLayout = (TableLayout) findViewById(R.id.tableLayout);
        tableLayout.setColumnCollapsed(9, false);
        tableLayout.setColumnCollapsed(10, false);
        tableLayout.setColumnCollapsed(11, false);
        tableLayout.setColumnCollapsed(12, false);
        tableLayout.setColumnStretchable(9, true);
        tableLayout.setColumnStretchable(11, true);
        name1 = (TextView) findViewById(R.id.column1);
        name2 = (TextView) findViewById(R.id.column2);
        name3 = (TextView) findViewById(R.id.column3);
        name4 = (TextView) findViewById(R.id.column4);
        name5 = (TextView) findViewById(R.id.column5);
        name6 = (TextView) findViewById(R.id.column6);

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        name1.setText("成品编码");
        name2.setText("仓位编码");
        name3.setText("待出库数量");
        name4.setText("入库批次号");
        name5.setText("已出库数量");
        name6.setText("二维码序列号");


        tvOutOrder = (TextView) findViewById(R.id.tv11);
        tvOutBatchNo = (TextView) findViewById(R.id.tv12);
        tvPlanNo = (TextView) findViewById(R.id.tv21);
        tvRemark = (TextView) findViewById(R.id.tv22);
        tvOutOrderStatus = (TextView) findViewById(R.id.tv31);

        listView = (MyListView) findViewById(R.id.lvTable);
//        listView.addFooterView(new ViewStub(this));

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (map.size() < myAdapter.getCount()) {
                    Toast.makeText(ProductOutOrderDetailActivity.this, "请先输入已出库数量", Toast.LENGTH_SHORT).show();
                    return;
                }
                for (int position = 0; position < map.size(); position++) {
                    if (map.get(position).equals("?")) {
                        Toast.makeText(ProductOutOrderDetailActivity.this, R.string.more_than_pre_out, Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                outOrderSpaceIds1 = outOrderSpaceIds.substring(0, outOrderSpaceIds.length() - 1);
                preOutStocks1 = preOutStocks.substring(0, preOutStocks.length() - 1);
//      每次点击提交按钮，将outNums设置为空，防止之前的数值对之后的数值产生影响
                outNums = "";
                for (int i = 0; i < map.size(); i++) {
                    outNums += map.get(i) + ",";
                }
                outNums1 = outNums.substring(0, outNums.length() - 1);

                String url = UrlHelper.URL_PRODUCT_OUT_COMMIT.replace("{outOrderId}", String.valueOf(id)).replace("{outOrderSpaceIds}", outOrderSpaceIds1).replace("{preOutStocks}", preOutStocks1).replace("{outStocks}", outNums1);
                url = UniversalHelper.getTokenUrl(url);

                for (int position = 0; position < map.size(); position++) {
                    if (Integer.parseInt(map.get(position)) < list1.get(position).getPreOutStock().intValue()) {
                        final String finalUrl = url;
                        new AlertDialog.Builder(ProductOutOrderDetailActivity.this)
                                .setTitle("系统提示")
                                .setMessage("部分出库，提交之后不能修改，确认出库吗？")
                                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        UniversalHelper.showProgressDialog(context);
                                        sendRequest(finalUrl);
                                    }
                                }).create().show();
                        return;
                    }
                }
                UniversalHelper.showProgressDialog(context);
                sendRequest(url);

            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 6) {
            strUrl = SPHelper.getString(this, "productOutURL", "");
            getOutOrderDetailFromNet();
        }
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
                    Toast.makeText(ProductOutOrderDetailActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(ProductOutOrderDetailActivity.this, ProductStockActivity.class);
                    intent.putExtra("flag", 2);
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

    public void getOutOrderDetailFromNet() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue queue = Volley.newRequestQueue(ProductOutOrderDetailActivity.this);
                StringRequest stringRequest = new StringRequest(strUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        String json = VolleyHelper.getJson(s);
//                        Log.d("RRRRR",json);
                        component.common.model.Response response = JSON.parseObject(json, component.common.model.Response.class);
                        ProductOutOrder productOutOrder = JSON.parseObject(response.getData().toString(), ProductOutOrder.class);
//                        得到列表的数据源
                        List<ProductOutOrderProduct> list = productOutOrder.getOutOrderProducts();
                        list1 = new ArrayList<>();
                        List<ProductOutOrderSpace> list2 = new ArrayList<>();

                        for (int i = 0; i < list.size(); i++) {
                            list2 = list.get(i).getOutOrderSpaces();
                            for (int j = 0; j < list2.size(); j++) {
                                list2.get(j).getSpace().setProduct(list.get(i).getProduct());
                            }
                            list1.addAll(list2);
                        }


                        if (productOutOrder.getStatus() == OutOrderStatusVo.pre_out_stock.getKey()) {
                            btnSubmit.setVisibility(View.VISIBLE);
                            myAdapter = new MyAdapter(ProductOutOrderDetailActivity.this, list1);
                            listView.setAdapter(myAdapter);
                            ivScann.setVisibility(View.VISIBLE);
                            ivScann.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(ProductOutOrderDetailActivity.this, CaptureActivity.class);
                                    intent.putExtra("FLAG", 4);
                                    intent.putExtra("id", id);
                                    startActivityForResult(intent, 3);
                                }
                            });
                        } else {
                            MyAdapter1 myAdapter = new MyAdapter1(ProductOutOrderDetailActivity.this, list1);
                            listView.setAdapter(myAdapter);
                        }

                        tvOutOrder.setText(productOutOrder.getCode());

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                        if (productOutOrder.getOutTime() != null) {
                            tvOutBatchNo.setText(sdf.format(productOutOrder.getOutTime()));
                        } else
                            tvOutBatchNo.setText("");


                        //            设置交付计划编号
                        if (productOutOrder.getDeliverPlan() != null)
                            tvPlanNo.setText(productOutOrder.getDeliverPlan().getCode());
                        else
                            tvPlanNo.setText("");

                        tvRemark.setText(productOutOrder.getRemark());
                        tvOutOrderStatus.setText(productOutOrder.getOutOrderStatusVo().getValue());


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


    public class MyAdapter extends BaseAdapter {
        private Context context;
        private List<ProductOutOrderSpace> list = null;


        public MyAdapter(Context context, List<ProductOutOrderSpace> list) {
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
                convertView = LayoutInflater.from(context).inflate(R.layout.list_item_6_edit, parent, false);
                holder = new ViewHolder();
                holder.tvCode = (TextView) convertView.findViewById(R.id.column1);
                holder.tvSpaceCode = (TextView) convertView.findViewById(R.id.column2);
                holder.tvToBeOutOrderNum = (TextView) convertView.findViewById(R.id.column3);
                holder.tvBatchNo = (TextView) convertView.findViewById(R.id.column4);
                holder.tvOutNum = (EditText) convertView.findViewById(R.id.column5);
                holder.tvInOrderSpaceId = (TextView) convertView.findViewById(R.id.column6);

                convertView.setTag(holder);
            } else
                holder = (ViewHolder) convertView.getTag();

            holder.tvCode.setText(list.get(position).getSpace().getProduct().getCode());
            holder.tvSpaceCode.setText(list.get(position).getSpace().getCode());
//            二维码序列号只对扫码有包装的产品有用，根据这个编号去扫描，对于其他类型的产品设置为空字符
            if (list.get(position).getSpace().getProduct().getStockType() == StockTypeVo.scan.getKey()
                    && list.get(position).getSpace().getProduct().getPackType() != PackTypeVo.empty.getKey()
                    )
                holder.tvInOrderSpaceId.setText(String.valueOf(list.get(position).getInOrderSpaceId()));
            else
                holder.tvInOrderSpaceId.setText("");
            holder.tvToBeOutOrderNum.setText(list.get(position).getPreOutStock() + list.get(position).getSpace().getProduct().getUnit().getName());

            //            入库批次的非空判断
            if (list.get(position).getSpace().getSpaceStock() != null)
                holder.tvBatchNo.setText(list.get(position).getSpace().getSpaceStock().getInTime());
            else
                holder.tvBatchNo.setText("");
            outOrderSpaceIds += String.valueOf(list.get(position).getId()) + ",";
            preOutStocks += String.valueOf(list.get(position).getPreOutStock()) + ",";
//            处理扫描出库的情况
            if (list.get(position).getSpace().getProduct().getStockType() == StockTypeVo.scan.getKey()) {
                holder.tvOutNum.setText(String.valueOf(list.get(position).getOutStock()));

//                如果原料有包装类型（在扫码的前提下），并且成功扫描到原料之后（出库数不为0），允许用户手动输入数量，否则不允许输入数量
                if (list.get(position).getSpace().getProduct().getPackType() != PackTypeVo.empty.getKey()) {
//              设置数字颜色为蓝色，并将用户输入的数字保存到map中，与不能输入数字的(红色)区分开
                    holder.tvOutNum.setTextColor(context.getResources().getColor(R.color.colorBase));
//                    出库数不为0才能点击
                    if (list.get(position).getOutStock() != 0) {
                        map.put(position, String.valueOf(list.get(position).getOutStock()));
                        holder.tvOutNum.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                if (s.length() > 0) {
                                    if (Integer.parseInt(s.toString()) > list.get(position).getPreOutStock().intValue()) {
                                        map.put(position, "?");
                                    } else
                                        map.put(position, s.toString());

                                } else {
                                    map.remove(position);
                                }
                            }
                        });

                    } else {
                        holder.tvOutNum.setFocusable(false);
                        holder.tvOutNum.setFocusableInTouchMode(false);
                        map.put(position, String.valueOf(0));
                        //            提示用户不能输入
                        holder.tvOutNum.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast toast = null;
                                if (toast == null) {
                                    toast = new Toast(ProductOutOrderDetailActivity.this);
                                    toast.makeText(ProductOutOrderDetailActivity.this, "请扫描二维码", Toast.LENGTH_SHORT).show();
                                } else
                                    toast.show();

                            }
                        });
                    }

                } else {
                    holder.tvOutNum.setFocusable(false);
                    holder.tvOutNum.setFocusableInTouchMode(false);

                    map.put(position, String.valueOf(list.get(position).getOutStock()));

//            提示用户不能输入
                    holder.tvOutNum.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast toast = null;
                            if (toast == null) {
                                toast = new Toast(ProductOutOrderDetailActivity.this);
                                toast.makeText(ProductOutOrderDetailActivity.this, "请扫描二维码", Toast.LENGTH_SHORT).show();
                            } else
                                toast.show();

                        }
                    });
                }

            }
//            若产品为手动输入数量的出入库产品，那么将输入的数量存入map中
            else {
                holder.tvOutNum.setText("");

                holder.tvOutNum.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (s.length() > 0) {
                            if (Integer.parseInt(s.toString()) > list.get(position).getPreOutStock().intValue()) {
                                map.put(position, "?");
                            } else
                                map.put(position, s.toString());
                        } else {
                            map.remove(position);
                        }

                    }
                });
            }

//            }
            return convertView;
        }

        public class ViewHolder {
            TextView tvCode;
            TextView tvSpaceCode;
            TextView tvToBeOutOrderNum;
            TextView tvBatchNo;
            EditText tvOutNum;
            TextView tvInOrderSpaceId;  //二维码序列号
        }
    }

    public class MyAdapter1 extends BaseAdapter {
        private Context context;
        private List<ProductOutOrderSpace> list = null;


        public MyAdapter1(Context context, List<ProductOutOrderSpace> list) {
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
                convertView = LayoutInflater.from(context).inflate(R.layout.list_item_6_edit, parent, false);
                holder = new ViewHolder();
                holder.tvCode = (TextView) convertView.findViewById(R.id.column1);
                holder.tvSpaceCode = (TextView) convertView.findViewById(R.id.column2);
                holder.tvToBeOutOrderNum = (TextView) convertView.findViewById(R.id.column3);
                holder.tvBatchNo = (TextView) convertView.findViewById(R.id.column4);
                holder.tvOutNum = (EditText) convertView.findViewById(R.id.column5);
                holder.tvInOrderSpaceId = (TextView) convertView.findViewById(R.id.column6);

                convertView.setTag(holder);
            } else
                holder = (ViewHolder) convertView.getTag();

            holder.tvCode.setText(list.get(position).getSpace().getProduct().getCode());
            holder.tvSpaceCode.setText(list.get(position).getSpace().getCode());
//            二维码序列号只对扫码有包装的产品有用，根据这个编号去扫描，对于其他类型的产品设置为空字符
            if (list.get(position).getSpace().getProduct().getStockType() == StockTypeVo.scan.getKey()
                    && list.get(position).getSpace().getProduct().getPackType() != PackTypeVo.empty.getKey()
                    )
                holder.tvInOrderSpaceId.setText(String.valueOf(list.get(position).getInOrderSpaceId()));
            else
                holder.tvInOrderSpaceId.setText("");

            holder.tvToBeOutOrderNum.setText(list.get(position).getPreOutStock() + list.get(position).getSpace().getProduct().getUnit().getName());


//            入库批次的非空判断
            if (list.get(position).getSpace().getSpaceStock() != null)
                holder.tvBatchNo.setText(list.get(position).getSpace().getSpaceStock().getInTime());
            else
                holder.tvBatchNo.setText("");
            holder.tvOutNum.setText(String.valueOf(list.get(position).getOutStock()));
            holder.tvOutNum.setFocusable(false);
            holder.tvOutNum.setFocusableInTouchMode(false);

            return convertView;
        }

        public class ViewHolder {
            TextView tvCode;
            TextView tvSpaceCode;
            TextView tvToBeOutOrderNum;
            TextView tvBatchNo;   // 入库批次
            EditText tvOutNum;
            TextView tvInOrderSpaceId;  //二维码序列号
        }
    }
}
