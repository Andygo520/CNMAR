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
import android.widget.TextView;
import android.widget.Toast;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import component.basic.vo.StockTypeVo;
import component.material.model.MaterialOutOrder;
import component.material.model.MaterialOutOrderMaterial;
import component.material.model.MaterialOutOrderSpace;
import component.product.vo.OutOrderStatusVo;
import zxing.activity.CaptureActivity;

public class MaterialOutOrderDetailActivity extends AppCompatActivity {
    private TextView tvName11, tvName12, tvName21, tvName22, tvName31;
    private TextView tvOutOrder, tvOutBatchNo, tvPlanNo, tvRemark, tvOutOrderStatus;
    private TextView name1, name2, name3, name4, name5;
    private TextView tvTitle;
    private MyListView lvMaterialInfo;
    private static String strUrl;
    private ImageView ivScann;
    private LinearLayout llLeftArrow;
    private Button btnSubmit;
    private HashMap<Integer, String> map = new HashMap<>();

    private String role;
    private Boolean isSuper;
    private int id;
    private String outOrderSpaceIds = "";
    private String outOrderSpaceIds1 = "";
    private String preOutStocks = "";
    private String preOutStocks1 = "";
    private String outNums = "";
    private String outNums1 = "";
    private MaterialInfoAdapter myAdapter;
    private List<MaterialOutOrderSpace> list1;

    //    private int flag=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_out_order_detail);

//   从登陆页面取出用户的角色信息
        role = LoginActivity.sp.getString("Role", "123");
        isSuper = LoginActivity.sp.getBoolean("isSuper", false);

        init();

//       将列表页传递过来的出库单ID传递给id
        id = getIntent().getIntExtra("ID", 0);
        strUrl = UrlHelper.URL_OUT_ORDER_DETAIL.replace("{id}", String.valueOf(id));
        strUrl = UniversalHelper.getTokenUrl(strUrl);

//        保存从列表页面传递过来的id对应的URL地址，在扫描页面返回的时候用到
        LoginActivity.editor.putString("outURL", strUrl).commit();
        getOutOrderDetailFromNet();


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(MaterialOutOrderDetailActivity.this, MaterialStockActivity.class);
            intent.putExtra("flag", 2);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void init() {
        tvName11 = (TextView) findViewById(R.id.name11);
        tvName12 = (TextView) findViewById(R.id.name12);
        tvName21 = (TextView) findViewById(R.id.name21);
        tvName22 = (TextView) findViewById(R.id.name22);
        tvName31 = (TextView) findViewById(R.id.name31);
        llLeftArrow = (LinearLayout) findViewById(R.id.left_arrow);
        ivScann = (ImageView) findViewById(R.id.scann);
        tvTitle = (TextView) findViewById(R.id.title);
        tvTitle.setText("原料仓库-出库单");
        llLeftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MaterialOutOrderDetailActivity.this, MaterialStockActivity.class);
                intent.putExtra("flag", 2);
                startActivity(intent);
                finish();
            }
        });
        tvName11.setText("出库单号");
        tvName12.setText("出库批次号");
        tvName21.setText("加工单编号");
        tvName22.setText("备注");
        tvName31.setText("出库单状态");

        name1 = (TextView) findViewById(R.id.column1);
        name2 = (TextView) findViewById(R.id.column2);
        name3 = (TextView) findViewById(R.id.column3);
        name4 = (TextView) findViewById(R.id.column4);
        name5 = (TextView) findViewById(R.id.column5);

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        name1.setText("原料编码");
        name2.setText("仓位编码");
        name3.setText("待出库数量");
        name4.setText("入库批次号");
        name5.setText("已出库数量");

        tvOutOrder = (TextView) findViewById(R.id.tv11);
        tvOutBatchNo = (TextView) findViewById(R.id.tv12);
        tvPlanNo = (TextView) findViewById(R.id.tv21);
        tvRemark = (TextView) findViewById(R.id.tv22);
        tvOutOrderStatus = (TextView) findViewById(R.id.tv31);

        lvMaterialInfo = (MyListView) findViewById(R.id.lvTable);
//        lvMaterialInfo.addFooterView(new ViewStub(this));

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (map.size() < myAdapter.getCount()) {
                    Toast.makeText(MaterialOutOrderDetailActivity.this, "请先输入已出库数量", Toast.LENGTH_SHORT).show();
                    return;
                }
                for (int position = 0; position < map.size(); position++) {
                    if (map.get(position).equals("?")) {
                        Toast.makeText(MaterialOutOrderDetailActivity.this, R.string.more_than_pre_out, Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                outOrderSpaceIds1 = outOrderSpaceIds.substring(0, outOrderSpaceIds.length() - 1);
                preOutStocks1 = preOutStocks.substring(0, preOutStocks.length() - 1);
                for (int i = 0; i < map.size(); i++) {
                    outNums += map.get(i) + ",";
                }
                outNums1 = outNums.substring(0, outNums.length() - 1);

                String url = UrlHelper.URL_OUT_COMMIT.replace("{outOrderId}", String.valueOf(id)).replace("{outOrderSpaceIds}", outOrderSpaceIds1).replace("{preOutStocks}", preOutStocks1).replace("{outStocks}", outNums1);
                Log.d("outUrl", url);
                url = UniversalHelper.getTokenUrl(url);

                for (int position = 0; position < map.size(); position++) {
                    if (Integer.parseInt(map.get(position)) < list1.get(position).getPreOutStock().intValue()) {
                        final String finalUrl = url;
                        new AlertDialog.Builder(MaterialOutOrderDetailActivity.this)
                                .setTitle("系统提示")
                                .setMessage("未全部出库，提交之后不能修改，确认出库吗？")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        sendRequest(finalUrl);
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        outNums = "";
                                    }
                                }).create().show();
                        return;
                    }
                }
                sendRequest(url);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            RequestQueue queue = Volley.newRequestQueue(MaterialOutOrderDetailActivity.this);
//            扫描条码后得到返回的URL，在后面加上单据的id从而知道扫描的是哪一单
            String codeUrl = data.getStringExtra("result") + "?outOrderId=" + String.valueOf(id);
            Log.d("codeUrl", codeUrl);

            StringRequest stringRequest = new StringRequest(codeUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    String json = VolleyHelper.getJson(s);
                    component.common.model.Response response = JSON.parseObject(json, component.common.model.Response.class);
                    if (!response.isStatus()) {
                        Toast.makeText(MaterialOutOrderDetailActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
                    } else {
                        getOutOrderDetailFromNet();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                }
            });
            queue.add(stringRequest);
        } else if (resultCode == 4) {
            strUrl = LoginActivity.sp.getString("outURL", "");
            getOutOrderDetailFromNet();
        }
    }

    public void sendRequest(String url) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                String json = VolleyHelper.getJson(s);
                component.common.model.Response response = JSON.parseObject(json, component.common.model.Response.class);
                if (!response.isStatus()) {
                    Toast.makeText(MaterialOutOrderDetailActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(MaterialOutOrderDetailActivity.this, MaterialStockActivity.class);
                    intent.putExtra("flag", 2);
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

    public void getOutOrderDetailFromNet() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue queue = Volley.newRequestQueue(MaterialOutOrderDetailActivity.this);
                StringRequest stringRequest = new StringRequest(strUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        String json = VolleyHelper.getJson(s);
//                        Log.d("RRRRR",json);
                        component.common.model.Response response = JSON.parseObject(json, component.common.model.Response.class);
                        MaterialOutOrder materialOutOrder = JSON.parseObject(response.getData().toString(), MaterialOutOrder.class);
//                        得到列表的数据源
                        List<MaterialOutOrderMaterial> list = materialOutOrder.getOutOrderMaterials();
                        list1 = new ArrayList<MaterialOutOrderSpace>();
                        List<MaterialOutOrderSpace> list2 = new ArrayList<MaterialOutOrderSpace>();

                        for (int i = 0; i < list.size(); i++) {
                            list2 = list.get(i).getOutOrderSpaces();
                            for (int j = 0; j < list2.size(); j++) {
                                list2.get(j).getSpace().setMaterial(list.get(i).getMaterial());
                            }
                            list1.addAll(list2);
                        }


                        if (materialOutOrder.getStatus() == OutOrderStatusVo.pre_out_stock.getKey() && (role.contains("原料库管员") || isSuper)) {
                            btnSubmit.setVisibility(View.VISIBLE);
                            myAdapter = new MaterialInfoAdapter(MaterialOutOrderDetailActivity.this, list1);
                            lvMaterialInfo.setAdapter(myAdapter);
                            ivScann.setVisibility(View.VISIBLE);
                            ivScann.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(MaterialOutOrderDetailActivity.this, CaptureActivity.class);
                                    intent.putExtra("FLAG", 2);
                                    intent.putExtra("id", id);
                                    startActivityForResult(intent, 3);
                                }
                            });
                        } else {
                            MaterialInfoAdapter1 myAdapter = new MaterialInfoAdapter1(MaterialOutOrderDetailActivity.this, list1);
                            lvMaterialInfo.setAdapter(myAdapter);
                        }

                        tvOutOrder.setText(materialOutOrder.getCode());

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                        if (materialOutOrder.getOutTime() != null) {
                            tvOutBatchNo.setText(sdf.format(materialOutOrder.getOutTime()));
                        } else
                            tvOutBatchNo.setText("");

                        if (materialOutOrder.getProducePlan() != null)
                            tvPlanNo.setText(materialOutOrder.getProducePlan().getCode());
                        else
                            tvPlanNo.setText("");

                        tvRemark.setText(materialOutOrder.getRemark());
                        tvOutOrderStatus.setText(materialOutOrder.getOutOrderStatusVo().getValue());


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


    public class MaterialInfoAdapter extends BaseAdapter {
        private Context context;
        private List<MaterialOutOrderSpace> list = null;


        public MaterialInfoAdapter(Context context, List<MaterialOutOrderSpace> list) {
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
                convertView = LayoutInflater.from(context).inflate(R.layout.list_item_5_edit, parent, false);
                holder = new ViewHolder();
                holder.tvMaterialCode = (TextView) convertView.findViewById(R.id.column1);
                holder.tvSpaceCode = (TextView) convertView.findViewById(R.id.column2);
                holder.tvToBeOutOrderNum = (TextView) convertView.findViewById(R.id.column3);
                holder.tvBatchNo = (TextView) convertView.findViewById(R.id.column4);
                holder.tvOutNum = (EditText) convertView.findViewById(R.id.column5);

                convertView.setTag(holder);
            } else
                holder = (ViewHolder) convertView.getTag();

            holder.tvMaterialCode.setText(list.get(position).getSpace().getMaterial().getCode());
            holder.tvSpaceCode.setText(list.get(position).getSpace().getCode());
            holder.tvToBeOutOrderNum.setText(String.valueOf(list.get(position).getPreOutStock()));
            //            入库批次的非空判断
            if (list.get(position).getSpace().getSpaceStock() != null)
                holder.tvBatchNo.setText(list.get(position).getSpace().getSpaceStock().getInTime());
            else
                holder.tvBatchNo.setText("");
            outOrderSpaceIds += String.valueOf(list.get(position).getId()) + ",";
            preOutStocks += String.valueOf(list.get(position).getPreOutStock()) + ",";

            if (list.get(position).getSpace().getMaterial().getStockType() == StockTypeVo.scan.getKey()) {
                holder.tvOutNum.setText(String.valueOf(list.get(position).getOutStock()));
                holder.tvOutNum.setFocusable(false);
                holder.tvOutNum.setFocusableInTouchMode(false);
//                将扫码的每一行的出库数量保存到map中
                map.put(position, String.valueOf(list.get(position).getOutStock()));
                holder.tvOutNum.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MaterialOutOrderDetailActivity.this, "该原料为扫描二维码出入库", Toast.LENGTH_SHORT).show();
                    }
                });

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
            TextView tvMaterialCode;
            TextView tvSpaceCode;
            TextView tvToBeOutOrderNum;
            TextView tvBatchNo;
            EditText tvOutNum;
        }
    }

    public class MaterialInfoAdapter1 extends BaseAdapter {
        private Context context;
        private List<MaterialOutOrderSpace> list = null;


        public MaterialInfoAdapter1(Context context, List<MaterialOutOrderSpace> list) {
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
                convertView = LayoutInflater.from(context).inflate(R.layout.list_item_5_edit, parent, false);
                holder = new ViewHolder();
                holder.tvMaterialCode = (TextView) convertView.findViewById(R.id.column1);
                holder.tvSpaceCode = (TextView) convertView.findViewById(R.id.column2);
                holder.tvToBeOutOrderNum = (TextView) convertView.findViewById(R.id.column3);
                holder.tvBatchNo = (TextView) convertView.findViewById(R.id.column4);
                holder.tvOutNum = (EditText) convertView.findViewById(R.id.column5);

                convertView.setTag(holder);
            } else
                holder = (ViewHolder) convertView.getTag();

            holder.tvMaterialCode.setText(list.get(position).getSpace().getMaterial().getCode());
            holder.tvSpaceCode.setText(list.get(position).getSpace().getCode());
            holder.tvToBeOutOrderNum.setText(String.valueOf(list.get(position).getPreOutStock()));
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
            TextView tvMaterialCode;
            TextView tvSpaceCode;
            TextView tvToBeOutOrderNum;
            TextView tvBatchNo;    // 入库批次
            EditText tvOutNum;
        }
    }
}
