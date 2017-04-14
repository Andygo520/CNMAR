package com.example.administrator.cnmar.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
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
import com.example.administrator.cnmar.helper.RoleHelper;
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
import component.product.model.ProductInOrder;
import component.product.model.ProductInOrderProduct;
import component.product.model.ProductInOrderSpace;
import component.product.vo.InOrderStatusVo;
import zxing.activity.CaptureActivity;

public class ProductInOrderDetailActivity extends AppCompatActivity {
    private Context context;
    private TextView tvName11, tvName12, tvName21, tvName22;
    private TextView tvInOrder, tvInBatchNo, tvPlanNo, tvInOrderStatus;
    private TextView name1, name2, name3, name4;
    private TextView tvTitle;
    private MyListView listView;
    private static String strUrl;
    private ImageView ivScann;
    private LinearLayout llLeftArrow;
    private Button btnSubmit;
    private TableRow row3;
    private HashMap<Integer, String> map = new HashMap<>();

    private int id;
    private String inOrderSpaceIds = "";
    private String inOrderSpaceIds1 = "";
    private String preInStocks = "";
    private String preInStocks1 = "";
    private String inNums = "";
    private String inNums1 = "";
    private MyAdapter myAdapter;
    private List<ProductInOrderSpace> list1;

    private String role;
    private Boolean isSuper;

    //    private int flag=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_in_order_detail);
        AppExit.getInstance().addActivity(this);
        //   从登陆页面取出用户的角色信息
        role = SPHelper.getString(this, "Role", "");
        isSuper = SPHelper.getBoolean(this, "isSuper", false);

        init();
        id = getIntent().getIntExtra("ID", 0);

        strUrl = UrlHelper.URL_PRODUCT_IN_ORDER_DETAIL.replace("{id}", String.valueOf(id));
        strUrl = UniversalHelper.getTokenUrl(strUrl);

//        保存从列表页面传递过来的id对应的URL地址，在扫描页面返回的时候用到
        SPHelper.putString(this, "productInURL", strUrl);
        getInOrderDetailFromNet();
    }

    public void init() {
        context = ProductInOrderDetailActivity.this;
        tvName11 = (TextView) findViewById(R.id.name11);
        tvName12 = (TextView) findViewById(R.id.name12);
        tvName21 = (TextView) findViewById(R.id.name21);
        tvName22 = (TextView) findViewById(R.id.name22);
        row3 = (TableRow) findViewById(R.id.row3);
        row3.setVisibility(View.GONE);
        llLeftArrow = (LinearLayout) findViewById(R.id.left_arrow);
        ivScann = (ImageView) findViewById(R.id.scann);
        tvTitle = (TextView) findViewById(R.id.title);
        tvTitle.setText("成品仓库-入库单");
        llLeftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductStockActivity.class);
                intent.putExtra("flag", 1);
                startActivity(intent);
            }
        });
        tvName11.setText("入库单号");
        tvName12.setText("入库批次号");
        tvName21.setText("加工单编号");
        tvName22.setText("入库单状态");

        name1 = (TextView) findViewById(R.id.column1);
        name2 = (TextView) findViewById(R.id.column2);
        name3 = (TextView) findViewById(R.id.column3);
        name4 = (TextView) findViewById(R.id.column4);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        name1.setText("成品编码");
        name2.setText("仓位编码");
        name3.setText("待入库数量");
        name4.setText("已入库数量");

        tvInOrder = (TextView) findViewById(R.id.tv11);
        tvInBatchNo = (TextView) findViewById(R.id.tv12);
        tvPlanNo = (TextView) findViewById(R.id.tv21);
        tvInOrderStatus = (TextView) findViewById(R.id.tv22);

        listView = (MyListView) findViewById(R.id.lvTable);
//        listView.addFooterView(new ViewStub(this));

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (map.size() < myAdapter.getCount()) {
                    Toast.makeText(ProductInOrderDetailActivity.this, "请先输入已入库数量", Toast.LENGTH_SHORT).show();
                    return;
                }
                for (int position = 0; position < map.size(); position++) {
                    if (map.get(position).equals("?")) {
                        Toast.makeText(ProductInOrderDetailActivity.this, R.string.more_than_pre_in, Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if (inOrderSpaceIds.length() > 0)
                    inOrderSpaceIds1 = inOrderSpaceIds.substring(0, inOrderSpaceIds.length() - 1);
                if (preInStocks.length() > 0)
                    preInStocks1 = preInStocks.substring(0, preInStocks.length() - 1);
                //        每次点击提交入库按钮，将inNums设置为空，防止之前的数值对之后的数值产生影响
                inNums = "";
                for (int i = 0; i < map.size(); i++) {
                    inNums += map.get(i) + ",";
                }
                if (inNums.length() > 0)
                    inNums1 = inNums.substring(0, inNums.length() - 1);

                String url = UrlHelper.URL_PRODUCT_IN_ORDER_COMMIT.replace("{inOrderId}", String.valueOf(id)).replace("{inOrderSpaceIds}", inOrderSpaceIds1).replace("{preInStocks}", preInStocks1).replace("{inStocks}", inNums1);
                Log.d("URL_PRODUCT_IN_ORDER",url);
                url = UniversalHelper.getTokenUrl(url);

                for (int position = 0; position < map.size(); position++) {
                    if (Integer.parseInt(map.get(position)) < list1.get(position).getPreInStock().intValue()) {
                        final String finalUrl = url;
                        new AlertDialog.Builder(ProductInOrderDetailActivity.this)
                                .setTitle("系统提示")
                                .setMessage("部分入库，提交之后不能修改，确认入库吗？")
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
            Intent intent = new Intent(context, ProductStockActivity.class);
            intent.putExtra("flag", 1);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 5) {
            strUrl = SPHelper.getString(this, "productInURL", "");
            getInOrderDetailFromNet();
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
                    Toast.makeText(ProductInOrderDetailActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(context, ProductStockActivity.class);
                    intent.putExtra("flag", 1);
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

    public void getInOrderDetailFromNet() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue queue = Volley.newRequestQueue(ProductInOrderDetailActivity.this);
                StringRequest stringRequest = new StringRequest(strUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        String json = VolleyHelper.getJson(s);
                        Log.d("RRRRR", json);
                        component.common.model.Response response = JSON.parseObject(json, component.common.model.Response.class);
                        ProductInOrder productInOrder = JSON.parseObject(response.getData().toString(), ProductInOrder.class);
//                        得到列表的数据源
                        List<ProductInOrderProduct> list = productInOrder.getInOrderProducts();
                        list1 = new ArrayList<>();
                        List<ProductInOrderSpace> list2 = new ArrayList<>();

                        for (int i = 0; i < list.size(); i++) {
                            list2 = list.get(i).getInOrderSpaces();
                            for (int j = 0; j < list2.size(); j++) {
                                list2.get(j).getSpace().setProduct(list.get(i).getProduct());
                            }
                            list1.addAll(list2);
                        }

//             待入库的产品才能扫码提交入库
//             只有超级用户、系统管理员、成品库管员才能输入“已入库数量”，点“确认入库”按钮
                        if (productInOrder.getStatus() == InOrderStatusVo.pre_in_stock.getKey()
                                && (RoleHelper.isSuper(context) || RoleHelper.isAdministrator(context) || RoleHelper.isProductStockman(context))) {
                            btnSubmit.setVisibility(View.VISIBLE);
                            btnSubmit.setText("提交入库");
                            myAdapter = new MyAdapter(ProductInOrderDetailActivity.this, list1);
                            listView.setAdapter(myAdapter);
                            ivScann.setVisibility(View.VISIBLE);
                            ivScann.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(ProductInOrderDetailActivity.this, CaptureActivity.class);
                                    intent.putExtra("FLAG", 3);
                                    startActivityForResult(intent, 3);
                                }
                            });
                        } else {
                            MyAdapter1 myAdapter = new MyAdapter1(ProductInOrderDetailActivity.this, list1);
                            listView.setAdapter(myAdapter);
                        }

                        tvInOrder.setText(productInOrder.getCode());

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                        if (productInOrder.getInTime() != null) {
                            tvInBatchNo.setText(sdf.format(productInOrder.getInTime()));
                        } else
                            tvInBatchNo.setText("");

//       设置加工单内容
                        if (productInOrder.getBatch() != null)
                            tvPlanNo.setText(productInOrder.getBatch().getPlan().getCode());
                        else
                            tvPlanNo.setText("");

                        tvInOrderStatus.setText(productInOrder.getInOrderStatusVo().getValue());

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
        private List<ProductInOrderSpace> list = null;


        public MyAdapter(Context context, List<ProductInOrderSpace> list) {
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
                convertView = LayoutInflater.from(context).inflate(R.layout.material_info_item2, parent, false);
                holder = new ViewHolder();
                holder.tvCode = (TextView) convertView.findViewById(R.id.materialCode);
                holder.tvSpaceCode = (TextView) convertView.findViewById(R.id.spaceCode);
                holder.tvToBeInOrderNum = (TextView) convertView.findViewById(R.id.toBeNum);
                holder.tvInNum = (EditText) convertView.findViewById(R.id.num);

                convertView.setTag(holder);
            } else
                holder = (ViewHolder) convertView.getTag();

            holder.tvCode.setText(list.get(position).getSpace().getProduct().getCode());
            holder.tvSpaceCode.setText(list.get(position).getSpace().getCode());

            holder.tvToBeInOrderNum.setText(list.get(position).getPreInStock()
                    + list.get(position).getSpace().getProduct().getUnit().getName());

            inOrderSpaceIds += String.valueOf(list.get(position).getId()) + ",";
            preInStocks += String.valueOf(list.get(position).getPreInStock()) + ",";

            if (list.get(position).getSpace().getProduct().getStockType() == StockTypeVo.scan.getKey()) {
                holder.tvInNum.setText(String.valueOf(list.get(position).getInStock()));

//                如果原料有包装类型（在扫码的前提下），并且成功扫描到原料之后（出库数不为0），允许用户手动输入数量，否则不允许输入数量
                if (list.get(position).getSpace().getProduct().getPackType() != PackTypeVo.empty.getKey()) {
//              设置数字颜色为蓝色，并将用户输入的数字保存到map中，与不能输入数字的(红色)区分开
                    holder.tvInNum.setTextColor(context.getResources().getColor(R.color.colorBase));
//                    出库数不为0才能点击
                    if (list.get(position).getInStock() != 0) {
                        map.put(position, String.valueOf(list.get(position).getInStock()));
                        holder.tvInNum.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                if (s.length() > 0) {
                                    if (Integer.parseInt(s.toString()) > list.get(position).getPreInStock().intValue()) {
                                        map.put(position, "?");
                                    } else
                                        map.put(position, s.toString());

                                } else {
                                    map.remove(position);
                                }
                            }
                        });

                    } else {
                        holder.tvInNum.setFocusable(false);
                        holder.tvInNum.setFocusableInTouchMode(false);
                        map.put(position, String.valueOf(0));
                        //            提示用户不能输入
                        holder.tvInNum.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast toast = null;
                                if (toast == null) {
                                    toast = new Toast(ProductInOrderDetailActivity.this);
                                    toast.makeText(ProductInOrderDetailActivity.this, "请扫描二维码", Toast.LENGTH_SHORT).show();
                                } else
                                    toast.show();

                            }
                        });
                    }

                } else {
                    holder.tvInNum.setFocusable(false);
                    holder.tvInNum.setFocusableInTouchMode(false);

                    map.put(position, String.valueOf(list.get(position).getInStock()));

//            提示用户不能输入
                    holder.tvInNum.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast toast = null;
                            if (toast == null) {
                                toast = new Toast(ProductInOrderDetailActivity.this);
                                toast.makeText(ProductInOrderDetailActivity.this, "请扫描二维码", Toast.LENGTH_SHORT).show();
                            } else
                                toast.show();

                        }
                    });
                }

            }
//            若产品为手动输入数量的出入库产品，那么将输入的数量存入map中
            else {
                holder.tvInNum.setText("");

                holder.tvInNum.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (s.length() > 0) {
                            if (Integer.parseInt(s.toString()) > list.get(position).getPreInStock().intValue()) {
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
            TextView tvToBeInOrderNum;
            EditText tvInNum;
        }
    }

    public class MyAdapter1 extends BaseAdapter {
        private Context context;
        private List<ProductInOrderSpace> list = null;


        public MyAdapter1(Context context, List<ProductInOrderSpace> list) {
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
                convertView = LayoutInflater.from(context).inflate(R.layout.material_info_item2, parent, false);
                holder = new ViewHolder();
                holder.tvCode = (TextView) convertView.findViewById(R.id.materialCode);
                holder.tvSpaceCode = (TextView) convertView.findViewById(R.id.spaceCode);
                holder.tvToBeInOrderNum = (TextView) convertView.findViewById(R.id.toBeNum);
                holder.tvInNum = (EditText) convertView.findViewById(R.id.num);

                convertView.setTag(holder);
            } else
                holder = (ViewHolder) convertView.getTag();

            holder.tvCode.setText(list.get(position).getSpace().getProduct().getCode());
            holder.tvSpaceCode.setText(list.get(position).getSpace().getCode());


            holder.tvToBeInOrderNum.setText(list.get(position).getPreInStock()
                    + list.get(position).getSpace().getProduct().getUnit().getName());

            holder.tvInNum.setText(String.valueOf(list.get(position).getInStock()));
            holder.tvInNum.setFocusable(false);
            holder.tvInNum.setFocusableInTouchMode(false);

            return convertView;
        }

        public class ViewHolder {
            TextView tvCode;
            TextView tvSpaceCode;
            TextView tvToBeInOrderNum;
            EditText tvInNum;
        }
    }
}
