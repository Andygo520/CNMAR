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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import component.basic.vo.PackTypeVo;
import component.basic.vo.StockTypeVo;
import component.material.model.MaterialInOrder;
import component.material.model.MaterialInOrderMaterial;
import component.material.model.MaterialInOrderSpace;
import component.material.vo.InOrderStatusVo;
import zxing.activity.CaptureActivity;

public class MaterialInOrderDetailActivity extends AppCompatActivity {
    @BindView(R.id.left_arrow)
    LinearLayout leftArrow;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.scann)
    ImageView scann;
    @BindView(R.id.name11)
    TextView name11;
    @BindView(R.id.tv11)
    TextView tvInOrder;
    @BindView(R.id.name12)
    TextView name12;
    @BindView(R.id.tv12)
    TextView tvInBatchNo;
    @BindView(R.id.name21)
    TextView name21;
    @BindView(R.id.tv21)
    TextView tvSupplyNo;
    @BindView(R.id.name22)
    TextView name22;
    @BindView(R.id.tv22)
    TextView tvSupplyName;
    @BindView(R.id.name31)
    TextView name31;
    @BindView(R.id.tv31)
    TextView tvPurchaseOrderNo;
    @BindView(R.id.name32)
    TextView name32;
    @BindView(R.id.tv32)
    TextView tvArriveDate;
    @BindView(R.id.name41)
    TextView name41;
    @BindView(R.id.tv41)
    TextView tvRemark;
    @BindView(R.id.name42)
    TextView name42;
    @BindView(R.id.tv42)
    TextView tvInOrderStatus;
    @BindView(R.id.row)
    TableRow row;
    @BindView(R.id.column1)
    TextView column1;
    @BindView(R.id.column2)
    TextView column2;
    @BindView(R.id.column3)
    TextView column3;
    @BindView(R.id.column4)
    TextView column4;
    @BindView(R.id.lvTable)
    MyListView lvTable;
    @BindView(R.id.llTest)
    LinearLayout llTest;
    @BindView(R.id.lvTestTable)
    MyListView lvTestTable;
    @BindView(R.id.btnTestSubmit)
    Button btnTestSubmit;
    @BindView(R.id.llTestSubmit)
    LinearLayout llTestSubmit;
    @BindView(R.id.lvInOrderTable)
    MyListView lvInOrderTable;
    @BindView(R.id.btnInOrderSubmit)
    Button btnInOrderSubmit;
    @BindView(R.id.llInOrderSubmit)
    LinearLayout llInOrderSubmit;
    @BindView(R.id.etRemark)
    EditText etRemark;


    private Context context = MaterialInOrderDetailActivity.this;
    private int id, testId;//id是传递到详情页面的入库单id,testId为用户ID
    private String strUrl;
    private String inOrderMaterialIds = "";
    private String res = "";
    private String failNums = "";
    private HashMap<Integer, String> map = new HashMap<>();//map存放检验结果
    private String inOrderSpaceIds = "";
    private String inOrderSpaceIds1 = "";
    private String preInStocks = "";
    private String preInStocks1 = "";
    private String inStocks = "";
    private String inStocks1 = "";
    private HashMap<Integer, String> map1 = new HashMap<>();//map1存放入库信息
    private MaterialTestAdapter myAdapter;//提交检验用Adapter
    private MaterialInOrderAdapter myAdapter1;//提交入库用Adapter
    private List<MaterialInOrderSpace> list1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_in_order_detail);
        ButterKnife.bind(this);
        AppExit.getInstance().addActivity(this);
        //        取出传递到详情页面的id，以及从登陆页面传递过来的用户的id（提交检验结果的时候用到）
        id = getIntent().getIntExtra("ID", 0);
        testId = SPHelper.getInt(context, "userId", 0);
        init();

        strUrl = UrlHelper.URL_IN_ORDER_DETAIL.replace("{id}", String.valueOf(id));
        strUrl = UniversalHelper.getTokenUrl(strUrl);
//        保存从列表页面传递过来的id对应的URL地址，在扫描页面返回的时候用到
        SPHelper.putString(this, "URL", strUrl);

        getInOrderDetailFromNet();
    }

    public void init() {
        title.setText("原料仓库-入库单");
//        将详情table不可见的第四行设置为可见
        row.setVisibility(View.VISIBLE);

        name11.setText("入库单号");
        name12.setText("入库批次号");
        name21.setText("供应商编码");
        name22.setText("供应商名称");
        name31.setText("采购订单号");
        name32.setText("到货日期");
        name41.setText("备注");
        name42.setText("入库单状态");

        column1.setText("原料编码");
        column2.setText("检验数量");
        column3.setText("不合格数量");
        column4.setText("检验结果");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(context, MaterialStockActivity.class);
            intent.putExtra("flag", 1);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    public void getInOrderDetailFromNet() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("strURl", strUrl);
                RequestQueue queue = Volley.newRequestQueue(MaterialInOrderDetailActivity.this);
                StringRequest stringRequest = new StringRequest(strUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        String json = VolleyHelper.getJson(s);
                        component.common.model.Response response = JSON.parseObject(json, component.common.model.Response.class);
                        MaterialInOrder materialInOrder = JSON.parseObject(response.getData().toString(), MaterialInOrder.class);
                        List<MaterialInOrderMaterial> inOrderMaterials = materialInOrder.getInOrderMaterials();
//                        不是待打印或者待检验状态的入库单，显示检验信息
                        if (materialInOrder.getStatus() != InOrderStatusVo.pre_print.getKey()
                                && materialInOrder.getStatus() != InOrderStatusVo.pre_test.getKey()) {
                            llTest.setVisibility(View.VISIBLE);
                            MyAdapter adapter = new MyAdapter(MaterialInOrderDetailActivity.this, inOrderMaterials);
                            lvTable.setAdapter(adapter);
                        }
//                       入库单状态为待入库的时候才显示扫描按钮
                        if (materialInOrder.getStatus() == InOrderStatusVo.pre_in_stock.getKey())
                            scann.setVisibility(View.VISIBLE);

                        tvInOrder.setText(materialInOrder.getCode());

//                      入库批次根据入库时间产生
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                        if (materialInOrder.getInTime() != null) {
                            tvInBatchNo.setText(sdf.format(materialInOrder.getInTime()));
                        } else
                            tvInBatchNo.setText("");
//                      供应商非空判断
                        if (materialInOrder.getSupply() != null) {
                            tvSupplyNo.setText(materialInOrder.getSupply().getCode());
                            tvSupplyName.setText(materialInOrder.getSupply().getName());
                        } else {
                            tvSupplyNo.setText("");
                            tvSupplyName.setText("");
                        }


                        tvPurchaseOrderNo.setText(materialInOrder.getPurchaseOrder());

                        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                        tvArriveDate.setText(sdf1.format(materialInOrder.getArrivalDate()));
                        tvRemark.setText(materialInOrder.getRemark());
                        tvInOrderStatus.setText(materialInOrder.getInOrderStatusVo().getValue());

//                      入库单状态为待检验和检验不合格状态时，显示原料及仓位信息—提交检验列表，否则显示原料及仓位信息—提交入库列表
                        if (materialInOrder.getStatus() == InOrderStatusVo.pre_test.getKey()
                                || materialInOrder.getStatus() == InOrderStatusVo.test_fail.getKey()) {
                            llTestSubmit.setVisibility(View.VISIBLE);

                        } else {
                            llInOrderSubmit.setVisibility(View.VISIBLE);
                        }

//                        获取用户“品控管理”菜单与其二级子菜单的对应关系，后面按钮的显示与此有关
                        String subList = SPHelper.getString(context, getResources().getString(R.string.HOME_PKGL), "");

//                        显示“提交检验”按钮两条件：
//                        1.单据状态为待检验 2.原料入库单只有超级用户、系统管理员或者检验员才能“提交检验结果”
                        if (materialInOrder.getStatus() == InOrderStatusVo.pre_test.getKey()
                                && (RoleHelper.isSuper(context) || RoleHelper.isAdministrator(context) || RoleHelper.isTestman(context))) {
                            btnTestSubmit.setVisibility(View.VISIBLE);
                            myAdapter = new MaterialTestAdapter(MaterialInOrderDetailActivity.this, inOrderMaterials);
                            lvTestTable.setAdapter(myAdapter);

                        } else {
                            MaterialTestAdapter1 myAdapter = new MaterialTestAdapter1(MaterialInOrderDetailActivity.this, inOrderMaterials);
                            lvTestTable.setAdapter(myAdapter);
                        }
//                      每次从后台取数据的时候，重新初始化list1，防止数据列表重复
                        list1 = new ArrayList<MaterialInOrderSpace>();
                        List<MaterialInOrderSpace> list2 = new ArrayList<MaterialInOrderSpace>();

                        for (int i = 0; i < inOrderMaterials.size(); i++) {
                            list2 = inOrderMaterials.get(i).getInOrderSpaces();
                            for (int j = 0; j < list2.size(); j++) {
                                list2.get(j).getSpace().setMaterial(inOrderMaterials.get(i).getMaterial());
                            }
                            list1.addAll(list2);
                        }
//                        显示“提交入库”按钮两条件：
//                        1.单据状态为待入库 2.只有超级用户、系统管理员或原料库管理（角色id=2）的角色才能“提交入库”
                        if (materialInOrder.getStatus() == InOrderStatusVo.pre_in_stock.getKey()
                                && (RoleHelper.isSuper(context) || RoleHelper.isAdministrator(context) || RoleHelper.isMaterialStockman(context))) {
                            btnInOrderSubmit.setVisibility(View.VISIBLE);
                            btnInOrderSubmit.setText("提交入库");
                            myAdapter1 = new MaterialInOrderAdapter(context, list1);
                            lvInOrderTable.setAdapter(myAdapter1);
                        } else {
                            MaterialInOrderAdapter1 myAdapter = new MaterialInOrderAdapter1(context, list1);
                            lvInOrderTable.setAdapter(myAdapter);
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(context, "连接超时", Toast.LENGTH_SHORT).show();
                    }
                });
                queue.add(stringRequest);
            }
        }).start();
    }

    public void sendRequest(String url) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                UniversalHelper.dismissProgressDialog();
                String json = VolleyHelper.getJson(s);
                component.common.model.Response response = JSON.parseObject(json, component.common.model.Response.class);
                if (response.isStatus()) {
                    Intent intent = new Intent(context, MaterialStockActivity.class);
                    intent.putExtra("flag", 1);
                    startActivity(intent);
                } else {
                    Toast.makeText(context, response.getMsg(), Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 3) {
            strUrl = SPHelper.getString(this, "URL", "");
            getInOrderDetailFromNet();
        }
    }

    @OnClick({R.id.left_arrow, R.id.scann, R.id.btnTestSubmit, R.id.btnInOrderSubmit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_arrow: {
                Intent intent = new Intent(context, MaterialStockActivity.class);
                intent.putExtra("flag", 1);
                startActivity(intent);
                break;
            }
            case R.id.scann: {
                Intent intent = new Intent(context, CaptureActivity.class);
                intent.putExtra("FLAG", 1);
                startActivityForResult(intent, 0);
                break;
            }
            case R.id.btnTestSubmit: {
                //                不合格数量的非空判断
                if (map.size() < myAdapter.getCount()) {
                    Toast.makeText(context, R.string.input_fail_num, Toast.LENGTH_SHORT).show();
                    return;
                }
                final String inOrderMaterialIds1 = inOrderMaterialIds.substring(0, inOrderMaterialIds.length() - 1);
                final String res1 = res.substring(0, res.length() - 1);
                for (int i = 0; i < map.size(); i++) {
                    failNums += map.get(i) + ",";
                }
                final String failNums1 = failNums.substring(0, failNums.length() - 1);
                new AlertDialog.Builder(context)
                        .setTitle("系统提示")
                        .setMessage("确认提交检验结果？")
                        .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                UniversalHelper.showProgressDialog(context);
                                String testRemark=etRemark.getText().toString().trim();
                                try {
                                    testRemark= URLEncoder.encode(testRemark,"utf-8");
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                                String url = UrlHelper.URL_TEST_COMMIT.replace("{inOrderId}", String.valueOf(id))
                                        .replace("{testId}", String.valueOf(testId))
                                        .replace("{testRemark}", testRemark)
                                        .replace("{inOrderMaterialIds}", inOrderMaterialIds1)
                                        .replace("{res}", res1)
                                        .replace("{failNums}", failNums1);
                                url = UniversalHelper.getTokenUrl(url);
                                sendRequest(url);
                            }
                        })
                        .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).create().show();
                break;
            }
            case R.id.btnInOrderSubmit: {
                //   入库数量的非空判断
                if (map.size() < myAdapter1.getCount()) {
                    Toast.makeText(context, "请输入入库数量再提交", Toast.LENGTH_SHORT).show();
                    return;
                }
                for (int position = 0; position < map.size(); position++) {
                    if (map.get(position).equals("?")) {
                        Toast.makeText(context, R.string.more_than_pre_in, Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if (inOrderSpaceIds.length() > 0)
                    inOrderSpaceIds1 = inOrderSpaceIds.substring(0, inOrderSpaceIds.length() - 1);
                if (preInStocks.length() > 0)
                    preInStocks1 = preInStocks.substring(0, preInStocks.length() - 1);

//      每次点击提交入库按钮，将inStocks设置为空，防止之前的数值对之后的数值产生影响
                inStocks = "";
                for (int i = 0; i < map.size(); i++) {
                    inStocks += map.get(i) + ",";
                }
                if (inStocks.length() > 0)
                    inStocks1 = inStocks.substring(0, inStocks.length() - 1);

                String url = UrlHelper.URL_IN_ORDER_COMMIT.replace("{inOrderId}", String.valueOf(id)).replace("{inOrderSpaceIds}", inOrderSpaceIds1).replace("{preInStocks}", preInStocks1).replace("{inStocks}", inStocks1);
                url = UniversalHelper.getTokenUrl(url);
                Log.d("urlurl", url);

                for (int position = 0; position < map.size(); position++) {
                    if (Integer.parseInt(map.get(position)) < list1.get(position).getPreInStock().intValue()) {
                        final String finalUrl = url;

                        new AlertDialog.Builder(context)
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
                break;
            }

        }
    }

    /*
    * 检验信息列表使用的Adapter
    * */
    public class MyAdapter extends BaseAdapter {
        private Context context;
        private List<MaterialInOrderMaterial> list = null;

        public MyAdapter(Context context, List<MaterialInOrderMaterial> list) {
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
                convertView = LayoutInflater.from(context).inflate(R.layout.table_list_item, parent, false);
                holder = new ViewHolder();
                holder.tvMaterialCode = (TextView) convertView.findViewById(R.id.column1);
                holder.tvTestNum = (TextView) convertView.findViewById(R.id.column2);
                holder.tvFailNum = (TextView) convertView.findViewById(R.id.column3);
                holder.tvTestStatus = (TextView) convertView.findViewById(R.id.column4);
                convertView.setTag(holder);
            } else
                holder = (ViewHolder) convertView.getTag();
            holder.tvMaterialCode.setText(list.get(position).getMaterial().getCode());
            holder.tvTestNum.setText(list.get(position).getStandard().getSampleNum()
                    + list.get(position).getMaterial().getUnit().getName());
            holder.tvFailNum.setText(String.valueOf(list.get(position).getFailNum()));
            if (list.get(position).getFailNum() >= list.get(position).getStandard().getReNum())
                holder.tvTestStatus.setText("不合格");
            else
                holder.tvTestStatus.setText("合格");

            return convertView;
        }

        public class ViewHolder {
            TextView tvMaterialCode;
            TextView tvTestNum;
            TextView tvFailNum;
            TextView tvTestStatus;
        }
    }

    /*
    * 原料及仓位信息列表—检验提交使用的Adapter
    * */
    public class MaterialTestAdapter extends BaseAdapter {
        private Context context;
        private List<MaterialInOrderMaterial> list = null;


        public MaterialTestAdapter(Context context, List<MaterialInOrderMaterial> list) {
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
                convertView = LayoutInflater.from(context).inflate(R.layout.material_info_item1, parent, false);
                holder = new ViewHolder();
                holder.tvMaterialCode = (TextView) convertView.findViewById(R.id.materialCode);
                holder.tvToBeInOrderNum = (TextView) convertView.findViewById(R.id.toBeInOrderNum);
                holder.tvTestNum = (TextView) convertView.findViewById(R.id.testNum);
                holder.tvAcNum = (TextView) convertView.findViewById(R.id.acNum);
                holder.tvReNum = (TextView) convertView.findViewById(R.id.reNum);
                holder.tvInvalidateNum = (EditText) convertView.findViewById(R.id.invalidateNum);

                convertView.setTag(holder);
            } else
                holder = (ViewHolder) convertView.getTag();
            holder.tvMaterialCode.setText(list.get(position).getMaterial().getCode());
            holder.tvToBeInOrderNum.setText(String.valueOf(list.get(position).getPreInStock()));
            holder.tvTestNum.setText(String.valueOf(list.get(position).getStandard().getSampleNum()));
            holder.tvAcNum.setText(String.valueOf(list.get(position).getStandard().getAcNum()));
            holder.tvReNum.setText(String.valueOf(list.get(position).getStandard().getReNum()));
            holder.tvInvalidateNum.setText("");

            inOrderMaterialIds += list.get(position).getId() + ",";
            Log.d("Tag", String.valueOf(inOrderMaterialIds));
            res += list.get(position).getStandard().getReNum() + ",";


            holder.tvInvalidateNum.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() > 0) {
                        map.put(position, s.toString());
                    } else {
                        map.remove(position);
                    }

                }
            });

            return convertView;
        }

        public class ViewHolder {
            TextView tvMaterialCode;
            TextView tvToBeInOrderNum;
            TextView tvTestNum;
            TextView tvAcNum;
            TextView tvReNum;
            EditText tvInvalidateNum;
        }
    }

    public class MaterialTestAdapter1 extends BaseAdapter {
        private Context context;
        private List<MaterialInOrderMaterial> list = null;

        public MaterialTestAdapter1(Context context, List<MaterialInOrderMaterial> list) {
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
                convertView = LayoutInflater.from(context).inflate(R.layout.material_info_item1, parent, false);
                holder = new ViewHolder();
                holder.tvMaterialCode = (TextView) convertView.findViewById(R.id.materialCode);
                holder.tvToBeInOrderNum = (TextView) convertView.findViewById(R.id.toBeInOrderNum);
                holder.tvTestNum = (TextView) convertView.findViewById(R.id.testNum);
                holder.tvAcNum = (TextView) convertView.findViewById(R.id.acNum);
                holder.tvReNum = (TextView) convertView.findViewById(R.id.reNum);
                holder.tvInvalidateNum = (EditText) convertView.findViewById(R.id.invalidateNum);

                convertView.setTag(holder);
            } else
                holder = (ViewHolder) convertView.getTag();
            holder.tvMaterialCode.setText(list.get(position).getMaterial().getCode());
            holder.tvToBeInOrderNum.setText(String.valueOf(list.get(position).getPreInStock()));
            holder.tvTestNum.setText(String.valueOf(list.get(position).getStandard().getSampleNum()));
            holder.tvAcNum.setText(String.valueOf(list.get(position).getStandard().getAcNum()));
            holder.tvReNum.setText(String.valueOf(list.get(position).getStandard().getReNum()));
            holder.tvInvalidateNum.setText(String.valueOf(list.get(position).getFailNum()));
            holder.tvInvalidateNum.setFocusable(false);
            holder.tvInvalidateNum.setFocusableInTouchMode(false);


            return convertView;
        }

        public class ViewHolder {
            TextView tvMaterialCode;
            TextView tvToBeInOrderNum;
            TextView tvTestNum;
            TextView tvAcNum;
            TextView tvReNum;
            EditText tvInvalidateNum;
        }
    }

    /*
    * 原料及仓位列表—入库使用的Adapter
    * */
    public class MaterialInOrderAdapter extends BaseAdapter {
        private Context context;
        private List<MaterialInOrderSpace> list = null;


        public MaterialInOrderAdapter(Context context, List<MaterialInOrderSpace> list) {
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
                holder.tvMaterialCode = (TextView) convertView.findViewById(R.id.materialCode);
                holder.tvSpaceCode = (TextView) convertView.findViewById(R.id.spaceCode);
                holder.tvToBeInOrderNum = (TextView) convertView.findViewById(R.id.toBeNum);
                holder.tvInNum = (EditText) convertView.findViewById(R.id.num);

                convertView.setTag(holder);
            } else
                holder = (ViewHolder) convertView.getTag();

            holder.tvMaterialCode.setText(list.get(position).getSpace().getMaterial().getCode());
            holder.tvSpaceCode.setText(list.get(position).getSpace().getCode());

            holder.tvToBeInOrderNum.setText(list.get(position).getPreInStock() + list.get(position).getSpace().getMaterial().getUnit().getName());


            inOrderSpaceIds += list.get(position).getId() + ",";
            preInStocks += list.get(position).getPreInStock() + ",";

//            判断原料是扫描入库还是输入数量入库
//            处理扫描入库的情况
            if (list.get(position).getSpace().getMaterial().getStockType() == StockTypeVo.scan.getKey()) {
                holder.tvInNum.setText(String.valueOf(list.get(position).getInStock()));
//                如果原料有包装类型（在扫码的前提下），并且成功扫描到原料（入库数不为0），允许用户手动输入入库数量，否则不允许输入数量
                if (list.get(position).getSpace().getMaterial().getPackType() != PackTypeVo.empty.getKey()) {
//              设置数字颜色为蓝色，并将用户输入的数字保存到map中，与不能输入数字的(红色)区分开
                    holder.tvInNum.setTextColor(context.getResources().getColor(R.color.colorBase));
//                    入库数不为0才能点击
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
                                    toast = new Toast(MaterialInOrderDetailActivity.this);
                                    toast.makeText(MaterialInOrderDetailActivity.this, "请扫描二维码", Toast.LENGTH_SHORT).show();
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
                                toast = new Toast(MaterialInOrderDetailActivity.this);
                                toast.makeText(MaterialInOrderDetailActivity.this, "请扫描二维码", Toast.LENGTH_SHORT).show();
                            } else
                                toast.show();

                        }
                    });
                }
            }
//            处理输入数量入库的情况
            else {
                holder.tvInNum.setText("");
                final ViewHolder finalHolder = holder;
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


            return convertView;
        }

        public class ViewHolder {
            TextView tvMaterialCode;
            TextView tvSpaceCode;
            TextView tvToBeInOrderNum;
            EditText tvInNum;
        }
    }

    public class MaterialInOrderAdapter1 extends BaseAdapter {
        private Context context;
        private List<MaterialInOrderSpace> list = null;


        public MaterialInOrderAdapter1(Context context, List<MaterialInOrderSpace> list) {
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
                holder.tvMaterialCode = (TextView) convertView.findViewById(R.id.materialCode);
                holder.tvSpaceCode = (TextView) convertView.findViewById(R.id.spaceCode);
                holder.tvToBeInOrderNum = (TextView) convertView.findViewById(R.id.toBeNum);
                holder.tvInNum = (EditText) convertView.findViewById(R.id.num);

                convertView.setTag(holder);
            } else
                holder = (ViewHolder) convertView.getTag();

            holder.tvMaterialCode.setText(list.get(position).getSpace().getMaterial().getCode());
            holder.tvSpaceCode.setText(list.get(position).getSpace().getCode());

            holder.tvToBeInOrderNum.setText(list.get(position).getPreInStock() + list.get(position).getSpace().getMaterial().getUnit().getName());

            holder.tvInNum.setText(String.valueOf(list.get(position).getInStock()));
            holder.tvInNum.setFocusable(false);
            holder.tvInNum.setFocusableInTouchMode(false);
            return convertView;
        }

        public class ViewHolder {
            TextView tvMaterialCode;
            TextView tvSpaceCode;
            TextView tvToBeInOrderNum;
            EditText tvInNum;
        }
    }
}
