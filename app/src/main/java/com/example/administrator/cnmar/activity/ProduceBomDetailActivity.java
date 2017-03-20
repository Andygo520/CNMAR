package com.example.administrator.cnmar.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.DefaultRetryPolicy;
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
import java.util.List;

import component.basic.vo.PackTypeVo;
import component.process.model.ProcessHalf;
import component.produce.model.ProduceBom;
import component.produce.vo.ReceiveStatusVo;

public class ProduceBomDetailActivity extends AppCompatActivity {

    private Context context;
    private TextView tvPlanCode, tvPlanName, tvProductCode, tvProductName, tvSize,tvHalfCode,tvHalfName,
            tvUnit, tvProduceNum, tvCheckMan, tvBeginDate, tvEndDate,tvReceiveOrder,tvReceiveOrderStatus, tvProcess, tvHalfProductInOrder;
    private EditText etSuccessNum, etActualNum;
    private Button btn;
    private static String strUrl;
    private LinearLayout llLeftArrow;
    private MyListView lvMaterial,lvHalf;
    private TextView tvTitle;
    private int id, receiveId, flag;
    private String successNum; //记录合格品数量
    private String actualNum; //记录实际生产数量

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produce_bom_detail);
        AppExit.getInstance().addActivity(this);

        id = getIntent().getIntExtra("ID", 0);

        flag = getIntent().getIntExtra("FLAG", 0); //页面跳转的标志

        receiveId = SPHelper.getInt(this, "userId", 0);
        init();
        strUrl = UrlHelper.URL_PRODUCE_BOM_DETAIL.replace("{ID}", String.valueOf(id));
        strUrl = UniversalHelper.getTokenUrl(strUrl);
        getListFromNet();
    }

    public void init() {
        context = ProduceBomDetailActivity.this;
        tvTitle = (TextView) findViewById(R.id.title);
        tvTitle.setText("子加工单管理");
        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn.getText().toString().equals("领料")) {
                    new AlertDialog.Builder(context)
                            .setTitle("系统提示")
                            .setMessage("确认领料且生成出库单？")
                            .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    UniversalHelper.showProgressDialog(context);
                                    String url = UrlHelper.URL_BOM_RECEIVE_MATERIAL_COMMIT.replace("{ID}", String.valueOf(id)).replace("{receiveId}", String.valueOf(receiveId));
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
                } else if (btn.getText().toString().equals("提交待入库")) {
//              获得用户的输入
                    successNum = etSuccessNum.getText().toString().trim();
                    if (successNum.length() == 0) {
                        Toast.makeText(context, "请输入合格品数量", Toast.LENGTH_SHORT).show();
                    } else {
//                        合格品数量不做限制
//                        if (Integer.parseInt(successNum) > produceNum) {
//                            Toast.makeText(context, "合格品数量不能大于生产数量，请重新输入", Toast.LENGTH_LONG).show();
//                        } else {
                        new AlertDialog.Builder(context)
                                .setTitle("系统提示")
                                .setMessage("确定提交吗？")
                                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                })
                                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        UniversalHelper.showProgressDialog(context);
                                        String url = UrlHelper.URL_HALF_PRODUCT_PRE_IN_STOCK_COMMIT.replace("{ID}", String.valueOf(id)).replace("{successNum}", successNum).replace("{testId}", receiveId + "");
                                        url = UniversalHelper.getTokenUrl(url);
                                        Log.d("UniversalHelper", url);
                                        sendRequest(url);
                                    }
                                }).create().show();
//                        }
                    }
                } else if (btn.getText().toString().equals("提交待检验")) {
//              获得用户的输入
                    actualNum = etActualNum.getText().toString().trim();
                    if (actualNum.length() == 0) {
                        Toast.makeText(context, "请输入实际生产数量", Toast.LENGTH_SHORT).show();
                    } else {
                        new AlertDialog.Builder(context)
                                .setTitle("系统提示")
                                .setMessage("确定提交吗？")
                                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                })
                                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        UniversalHelper.showProgressDialog(context);
                                        String url = UrlHelper.URL_HALF_PRODUCT_ACTUAL_NUM_COMMIT.replace("{ID}", String.valueOf(id)).replace("{actualNum}", actualNum).replace("{actualId}",receiveId+"");
                                        url = UniversalHelper.getTokenUrl(url);
                                        sendRequest(url);
                                    }
                                }).create().show();
                    }
                }
            }
        });
        llLeftArrow = (LinearLayout) findViewById(R.id.left_arrow);
        llLeftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProduceBomDetailActivity.this.finish();//结束当前页面
            }
        });

        tvPlanCode = (TextView) findViewById(R.id.tv11);
        tvPlanName = (TextView) findViewById(R.id.tv12);
        tvProductCode = (TextView) findViewById(R.id.tv21);
        tvProductName = (TextView) findViewById(R.id.tv22);
        tvBeginDate = (TextView) findViewById(R.id.tv31);
        tvEndDate = (TextView) findViewById(R.id.tv32);
        tvHalfCode = (TextView) findViewById(R.id.tv41);
        tvHalfName = (TextView) findViewById(R.id.tv42);
        tvSize = (TextView) findViewById(R.id.tv51);
        tvUnit = (TextView) findViewById(R.id.tv52);
        tvProduceNum = (TextView) findViewById(R.id.tv61);
        etActualNum = (EditText) findViewById(R.id.tv62);
        tvCheckMan= (TextView) findViewById(R.id.tv71);
        etSuccessNum= (EditText) findViewById(R.id.tv72);
        tvReceiveOrder = (TextView) findViewById(R.id.tv81);
        tvReceiveOrderStatus = (TextView) findViewById(R.id.tv82);
        tvProcess = (TextView) findViewById(R.id.tv91);
        tvHalfProductInOrder = (TextView) findViewById(R.id.tv92);

        etActualNum.setFocusable(false);
        etSuccessNum.setFocusable(false);
        etActualNum.setFocusableInTouchMode(false);
        etSuccessNum.setFocusableInTouchMode(false);
        lvMaterial= (MyListView) findViewById(R.id.lvTableMaterial);
        lvHalf= (MyListView) findViewById(R.id.lvTableHalf);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();//结束当前页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void sendRequest(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue queue = Volley.newRequestQueue(context);
                StringRequest request = new StringRequest(url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        UniversalHelper.dismissProgressDialog();
                        String json = VolleyHelper.getJson(s);
                        Log.d("production", json);
                        component.common.model.Response response = JSON.parseObject(json, component.common.model.Response.class);
                        if (!response.isStatus()) {
                            Toast.makeText(context, response.getMsg(), Toast.LENGTH_LONG).show();
                        } else {
                            Intent intent = new Intent(context, PlanManageActivity.class);
                            intent.putExtra("flag",1);//跳转到计划管理后根据“flag”标志默认显示子加工单
                            startActivity(intent);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        UniversalHelper.dismissProgressDialog();
                        Toast.makeText(context, "连接超时", Toast.LENGTH_LONG).show();
                    }
                });
//                重设Volley请求超时时间
                request.setRetryPolicy(new DefaultRetryPolicy(1000 * 1000, 0, 0f));
                queue.add(request);
            }
        }).start();
    }

    public void getListFromNet() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue queue = Volley.newRequestQueue(context);
                StringRequest stringRequest = new StringRequest(strUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        String json = VolleyHelper.getJson(s);
                        Log.d("bomjson",json);
                        component.common.model.Response response = JSON.parseObject(json, component.common.model.Response.class);
                        ProduceBom produceBom = JSON.parseObject(response.getData().toString(), ProduceBom.class);

                        List<ProduceBom> materialSubs=produceBom.getMaterialSubs();
                        List<ProduceBom> halfSubs=produceBom.getHalfSubs();

//                        List<ProduceBom> produceBoms=produceBom.getProduceBoms();
                        lvMaterial.setAdapter(new MaterialAdapter(context,materialSubs));
                        lvHalf.setAdapter(new HalfAdapter(context,halfSubs));

//                      加工单、子加工单同时没有领料的时候显示领料按钮
                        if (produceBom.getReceive() == null && produceBom.getPlan().getReceive()==null) {
                            btn.setVisibility(View.VISIBLE);
                            etActualNum.setText("");
                            etSuccessNum.setText("");
                        } else {
//                        ActualId==0表示还没有输入实际生产数，领料单为已领料状态才显示“检验”按钮
                            if (produceBom.getActualId() == 0 ) {
                                if(produceBom.getReceive()!=null && produceBom.getReceive().getStatus()== ReceiveStatusVo.yes.getKey()){
                                        btn.setVisibility(View.VISIBLE);
                                        btn.setText("提交待检验");
                                        etSuccessNum.setText("");
                                        etActualNum.setHint("请输入");
                                        etActualNum.setFocusable(true);
                                        etActualNum.setFocusableInTouchMode(true);
                                }else{
                                    etSuccessNum.setText("");
                                    etActualNum.setText("");
                                }
                            } else if (produceBom.getActualId() != 0 ){
                                etActualNum.setText(produceBom.getActualNum() + "");
                                //                        根据TestId字段判断是否入库
                                if (produceBom.getTestId() == 0) {
                                    btn.setVisibility(View.VISIBLE);
                                    btn.setText("提交待入库");
                                    etSuccessNum.setHint("请输入");
                                    etSuccessNum.setFocusable(true);
                                    etSuccessNum.setFocusableInTouchMode(true);
                                } else {
                                    tvHalfProductInOrder.setText(produceBom.getHalfInOrder()==null?"":produceBom.getHalfInOrder().getCode());
                                    etSuccessNum.setText(String.valueOf(produceBom.getSuccessNum()));
                                }
                            }
                        }
                        tvPlanCode.setText(produceBom.getCode());
                        tvPlanName.setText(produceBom.getPlan().getName());
                        tvProductCode.setText(produceBom.getPlan().getProduct().getCode());
                        tvProductName.setText(produceBom.getPlan().getProduct().getName());
                        tvHalfCode.setText(produceBom.getHalf().getCode());
                        tvHalfName.setText(produceBom.getHalf().getName());
                        tvSize.setText(produceBom.getHalf().getSpec());
//                        有包装的，单位格式为“9个/袋”
                        if (produceBom.getHalf().getPackType() != PackTypeVo.empty.getKey())
                            tvUnit.setText(produceBom.getHalf().getPackNum()
                                    + produceBom.getHalf().getUnit().getName()
                                    + " / " + produceBom.getHalf().getPackTypeVo().getValue().substring(1, 2));
                        else
                            tvUnit.setText(produceBom.getHalf().getUnit().getName());

                        if (produceBom.getTest() != null)
                            tvCheckMan.setText(produceBom.getTest().getName());
                        else
                            tvCheckMan.setText("");

                        tvProduceNum.setText(produceBom.getReceiveNum() + produceBom.getHalf().getUnit().getName());

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//                      开始日期、结束日期的非空判断
                        if (produceBom.getPlan().getStartDate() == null)
                            tvBeginDate.setText("");
                        else
                            tvBeginDate.setText(sdf.format(produceBom.getPlan().getStartDate()));
                        if (produceBom.getPlan().getEndDate() == null)
                            tvEndDate.setText("");
                        else
                            tvEndDate.setText(sdf.format(produceBom.getPlan().getEndDate()));

//                        获取用户“品控管理”以及“计划管理”菜单及其子菜单的对应关系，后面按钮的显示与此有关
                        String subList = SPHelper.getString(context, getResources().getString(R.string.HOME_PKGL));
                        String subList1 = SPHelper.getString(context, getResources().getString(R.string.HOME_JHGL));

//                        领料单状态设置
                        if(produceBom.getReceive()!=null){
                            tvReceiveOrder.setText(produceBom.getReceive().getCode());
                            tvReceiveOrderStatus.setText(produceBom.getReceive().getReceiveStatusVo().getValue());
                        }
                        else{
                            tvReceiveOrder.setText("");
                            tvReceiveOrderStatus.setText("");
                        }


//                    工序字段的设置
                        List<ProcessHalf> processHalfList = produceBom.getHalf().getProcessList();
                        String processName = "";
                        if (processHalfList != null && processHalfList.size() > 0) {
                            for (ProcessHalf process : processHalfList) {
                                processName += process.getName() + "，";
                            }
                            tvProcess.setText(processName.substring(0, processName.length() - 1));
                        } else
                            tvProcess.setText("");

                        if (produceBom.getHalfInOrder() != null)
                            tvHalfProductInOrder.setText(produceBom.getHalfInOrder().getCode());
                        else
                            tvHalfProductInOrder.setText("");
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

    public class MaterialAdapter extends BaseAdapter {
        private Context context;
        private List<ProduceBom> list = null;


        public MaterialAdapter(Context context, List<ProduceBom> list) {
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
                holder.tvMaterialCode = (TextView) convertView.findViewById(R.id.column1);
                holder.tvMaterialName = (TextView) convertView.findViewById(R.id.column2);
                holder.tvStockNum = (TextView) convertView.findViewById(R.id.column3);
                holder.tvNum = (TextView) convertView.findViewById(R.id.column4);

                convertView.setTag(holder);
            } else
                holder = (ViewHolder) convertView.getTag();

            holder.tvMaterialCode.setText(list.get(position).getMaterial().getCode());
            holder.tvMaterialName.setText(list.get(position).getMaterial().getName());
//         库存为空就直接设置为空字符
            if (list.get(position).getMaterial().getStock() != null)
                holder.tvStockNum.setText(String.valueOf(list.get(position).getMaterial().getStock().getStock()) + list.get(position).getMaterial().getUnit().getName());
            else
                holder.tvStockNum.setText("");

            holder.tvNum.setText(String.valueOf(list.get(position).getReceiveNum()) + list.get(position).getMaterial().getUnit().getName());

            return convertView;
        }

        public class ViewHolder {
            TextView tvMaterialCode;
            TextView tvMaterialName;
            TextView tvStockNum;
            TextView tvNum;
        }
    }

    public class HalfAdapter extends BaseAdapter {
        private Context context;
        private List<ProduceBom> list = null;


        public HalfAdapter(Context context, List<ProduceBom> list) {
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
                holder.tvHalfCode = (TextView) convertView.findViewById(R.id.column1);
                holder.tvHalfName = (TextView) convertView.findViewById(R.id.column2);
                holder.tvStockNum = (TextView) convertView.findViewById(R.id.column3);
                holder.tvNum = (TextView) convertView.findViewById(R.id.column4);

                convertView.setTag(holder);
            } else
                holder = (ViewHolder) convertView.getTag();

            holder.tvHalfCode.setText(list.get(position).getHalf().getCode());
            holder.tvHalfName.setText(list.get(position).getHalf().getName());
//         库存为空就直接设置为空字符
            if (list.get(position).getHalf().getStock() != null)
                holder.tvStockNum.setText(String.valueOf(list.get(position).getHalf().getStock().getStock()) + list.get(position).getHalf().getUnit().getName());
            else
                holder.tvStockNum.setText("");

            holder.tvNum.setText(String.valueOf(list.get(position).getReceiveNum()) + list.get(position).getHalf().getUnit().getName());

            return convertView;
        }

        public class ViewHolder {
            TextView tvHalfCode;
            TextView tvHalfName;
            TextView tvStockNum;
            TextView tvNum;
        }
    }

}
