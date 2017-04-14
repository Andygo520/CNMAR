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
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableRow;
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
import com.example.administrator.cnmar.helper.RoleHelper;
import com.example.administrator.cnmar.helper.SPHelper;
import com.example.administrator.cnmar.helper.UniversalHelper;
import com.example.administrator.cnmar.helper.UrlHelper;
import com.example.administrator.cnmar.helper.VolleyHelper;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.lankton.flowlayout.FlowLayout;
import component.basic.vo.PackTypeVo;
import component.com.model.ComBox;
import component.process.model.ProcessHalf;
import component.produce.model.ProduceBom;
import component.produce.model.ProduceBomBatch;
import component.produce.vo.ProduceStatusVo;
import component.produce.vo.ReceiveStatusVo;

import static com.example.administrator.cnmar.activity.ProductionPlanDetailActivity.dip2px;

public class ProduceBomDetailActivity extends AppCompatActivity {
    @BindView(R.id.tv92)
    TextView tvLastSuccessNum;
    @BindView(R.id.tv102)
    TextView tv102;
    @BindView(R.id.rb1)
    RadioButton rb1;
    @BindView(R.id.rb2)
    RadioButton rb2;
    @BindView(R.id.rg)
    RadioGroup rg;
    @BindView(R.id.column1)
    TextView column1;
    @BindView(R.id.column2)
    TextView column2;
    @BindView(R.id.column3)
    TextView column3;
    @BindView(R.id.column4)
    TextView column4;
    @BindView(R.id.lvTable)
    MyListView lvHalfIn;
    @BindView(R.id.row)
    TableRow row;
    @BindView(R.id.rowRg)
    TableRow rowRg;
    @BindView(R.id.checkAll)
    CheckBox checkAll;
    @BindView(R.id.flowlayout)
    FlowLayout flowlayout;
    @BindView(R.id.tvBox)
    TextView tvBox;
    @BindView(R.id.rowBox)
    TableRow rowBox;
    @BindView(R.id.rowBoxFlow)
    TableRow rowBoxFlow;
    @BindView(R.id.etRemark)
    EditText etRemark;
    @BindView(R.id.llRemark)
    LinearLayout llRemark;
    private Context context;
    private TextView tvPlanCode, tvPlanName, tvProductCode, tvProductName, tvSize, tvHalfCode, tvHalfName,
            tvUnit, tvProduceNum, tvSuccessNum, tvActualNum, tvBeginDate, tvEndDate, tvReceiveOrder, tvReceiveOrderStatus, tvProcess;
    private EditText etBatchProduceNum;
    private Button btn;
    private static String strUrl;
    private LinearLayout llLeftArrow;
    private MyListView lvMaterial, lvHalf;
    private TextView tvTitle;
    private int id, receiveId, flag, status;
    private String successNum; //记录合格品数量
    private String actualNum; //记录当前批次实际生产数量
    private int test;//最后一行的TestId，当TestId=0的时候，代表还未检验，就可以输入合格品数量，提交入库
    private int batchId;//最后一行id

    private List<CheckBox> checkBoxList = new ArrayList<CheckBox>();
    private Map<Integer, String> boxMap = new LinkedHashMap<>();
    private String lastProcessBoxIds1;
    private String box = "";//用来存放tvBox内容

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produce_bom_detail);
        ButterKnife.bind(this);
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
        checkAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    for (CheckBox checkBox : checkBoxList) {
                        checkBox.setChecked(true);
                    }
                } else {
                    for (CheckBox checkBox : checkBoxList) {
                        checkBox.setChecked(false);
                    }
                }
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn.getText().toString().equals("生成领料单")) {
                    new AlertDialog.Builder(context)
                            .setTitle("系统提示")
                            .setMessage("确认领料且生成出库单？")
                            .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    UniversalHelper.showProgressDialog(context);
                                    String url = UrlHelper.URL_BOM_RECEIVE_MATERIAL_COMMIT.replace("{ID}", String.valueOf(id)).replace("{receiveId}", String.valueOf(receiveId));
                                    Log.d("queding", url);
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
                    if (successNum == null || successNum.equals("")) {
                        Toast.makeText(context, "请输入合格品数量", Toast.LENGTH_SHORT).show();
                    } else {
                        if (Integer.parseInt(successNum) > Integer.parseInt(actualNum)) {
                            Toast.makeText(context, "合格品数量不能大于实际生产数量", Toast.LENGTH_LONG).show();
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
                                            String remark = etRemark.getText().toString().trim();
                                            try {
                                                remark = URLEncoder.encode(remark, "utf-8");
                                            } catch (UnsupportedEncodingException e) {
                                                e.printStackTrace();
                                            }
                                            String url = UrlHelper.URL_HALF_PRODUCT_PRE_IN_STOCK_COMMIT
                                                    .replace("{batchId}", String.valueOf(batchId))
                                                    .replace("{successNum}", successNum)
                                                    .replace("{testId}", receiveId + "")
                                                    .replace("{testRemark}", remark);
                                            url = UniversalHelper.getTokenUrl(url);
                                            Log.d("UniversalHelper", url);
                                            sendRequest(url);
                                        }
                                    }).create().show();
                        }
                    }
                } else if (btn.getText().toString().equals("提交待检验")) {
//              获得用户的输入
                    actualNum = etBatchProduceNum.getText().toString().trim();
                    String lastProcessBoxIds = "";

//                    用此方法来循环遍历map
                    for (Map.Entry<Integer, String> entry : boxMap.entrySet()) {
                        String value = entry.getValue();
                        lastProcessBoxIds += value + ",";
                    }

                    if (lastProcessBoxIds.length() > 0)
                        lastProcessBoxIds1 = lastProcessBoxIds.substring(0, lastProcessBoxIds.length() - 1);
                    else
                        lastProcessBoxIds1 = "";

                    Log.d("lastProcessBoxIds", boxMap.size() + "");
                    Log.d("lastProcessBoxIds", lastProcessBoxIds1);
//                  判断加工单状态
                    if (rb1.isChecked()) {
                        status = 1;
                    } else {
                        status = 2;
                    }
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
                                        String url = UrlHelper.URL_HALF_PRODUCT_ACTUAL_NUM_COMMIT
                                                .replace("{ID}", String.valueOf(id))
                                                .replace("{actualNum}", actualNum)
                                                .replace("{actualId}", receiveId + "")
                                                .replace("{lastProcessBoxIds}", lastProcessBoxIds1)
                                                .replace("{status}", "" + status);
                                        Log.d("lastProcessBoxIds", url);
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
                Intent intent = new Intent(context, PlanManageActivity.class);
                intent.putExtra("flag", 1);//跳转到计划管理后根据“flag”标志默认显示子加工单
                startActivity(intent);
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
        etBatchProduceNum = (EditText) findViewById(R.id.tv62);
        tvActualNum = (TextView) findViewById(R.id.tv71);
        tvSuccessNum = (TextView) findViewById(R.id.tv72);
        tvReceiveOrder = (TextView) findViewById(R.id.tv81);
        tvReceiveOrderStatus = (TextView) findViewById(R.id.tv82);
        tvProcess = (TextView) findViewById(R.id.tv91);
        lvMaterial = (MyListView) findViewById(R.id.lvTableMaterial);
        lvHalf = (MyListView) findViewById(R.id.lvTableHalf);
        etBatchProduceNum.setFocusable(false);
        etBatchProduceNum.setFocusableInTouchMode(false);
        column1.setText("实际生产数量");
        column2.setText("合格品数量");
        column3.setText("入库单号");
        column4.setText("入库单状态");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(context, PlanManageActivity.class);
            intent.putExtra("flag", 1);//跳转到计划管理后根据“flag”标志默认显示子加工单
            startActivity(intent);
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
                            intent.putExtra("flag", 1);//跳转到计划管理后根据“flag”标志默认显示子加工单
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
                        Log.d("bomjson", json);
                        component.common.model.Response response = JSON.parseObject(json, component.common.model.Response.class);
                        ProduceBom produceBom = JSON.parseObject(response.getData().toString(), ProduceBom.class);

                        List<ProduceBom> materialSubs = produceBom.getMaterialSubs();
                        List<ProduceBom> halfSubs = produceBom.getHalfSubs();
                        List<ProduceBomBatch> batchs = produceBom.getBatchs();

//                        用来动态显示当前批次生产数量
                        final int[] j = {0};
                        if (produceBom != null) {
                            final List<ComBox> boxList = produceBom.getLastProcessBoxs();
                            if (boxList != null) {
                                for (int i = 0; i < boxList.size(); i++) {
                                    ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                    lp.setMargins(dip2px(context, 3), 0, dip2px(context, 3), 0);
                                    final CheckBox checkBox = new CheckBox(context);
                                    checkBox.setPadding(dip2px(context, 5), 0, dip2px(context, 5), 0);
//                            checkBox.setTextColor(Color.parseColor("#FF3030"));
                                    checkBox.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                                    checkBox.setEnabled(false);
                                    //                               得到tvBox显示的内容
                                    box += boxList.get(i).getCode() + "（" + boxList.get(i).getNum() + "）" + "，";
                                    checkBox.setText(boxList.get(i).getCode() + "（" + boxList.get(i).getNum() + "）");
                                    checkBox.setGravity(Gravity.CENTER_VERTICAL);
                                    flowlayout.addView(checkBox, lp);
                                    checkBoxList.add(checkBox);
                                    final int finalI = i;
                                    checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                        @Override
                                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                            if (isChecked) {
                                                j[0] += boxList.get(finalI).getNum();
                                                boxMap.put(finalI, boxList.get(finalI).getId() + "");
                                            } else {
                                                j[0] -= boxList.get(finalI).getNum();
                                                boxMap.remove(finalI);
                                            }
                                            etBatchProduceNum.setText(j[0] + "");
                                        }
                                    });
                                }
                                //                         去掉最后的"，"
                                if (box.length() > 0)
                                    box = box.substring(0, box.length() - 1);
                            }
                        }

                        if (checkBoxList.size() > 0)
                            checkAll.setVisibility(View.VISIBLE);

//                        List<ProduceBom> produceBoms=produceBom.getProduceBoms();
                        lvMaterial.setAdapter(new MaterialAdapter(context, materialSubs));
                        lvHalf.setAdapter(new HalfAdapter(context, halfSubs));
                        lvHalfIn.setAdapter(new HalfInAdapter(context, batchs));

//                      子加工单没有领料的时候,并且只有超级用户、系统管理员或车间班组长(角色id=6)才能“生成领料单”
                        if (produceBom.getReceive() == null
                                && (RoleHelper.isSuper(context) || RoleHelper.isAdministrator(context) || RoleHelper.isWorkshopLeader(context))) {
                            btn.setVisibility(View.VISIBLE);
                            tv102.setText(produceBom.getProduceStatusVo().getValue());
                            tvBox.setText(box);
                        } else if (produceBom.getReceive() != null && produceBom.getReceive().getReceiveStatusVo() == ReceiveStatusVo.no) {
                            tv102.setText(produceBom.getProduceStatusVo().getValue());
                            tvBox.setText(box);
                        } else {
//                       已领料未完工的时候显示待检验按钮
                            //           只有超级用户、系统管理员或统计员(角色id=7)才能“提交待检验”
                            if (produceBom.getReceive() != null && produceBom.getProduceStatusVo() == ProduceStatusVo.no
                                    && batchs != null && (test > 0 || batchs.size() == 0)
                                    && (RoleHelper.isSuper(context) || RoleHelper.isAdministrator(context) || RoleHelper.isStatistician(context))) {
                                btn.setVisibility(View.VISIBLE);
                                btn.setText("提交待检验");
                                rowBox.setVisibility(View.GONE);
                                rowBoxFlow.setVisibility(View.VISIBLE);
                                rowRg.setVisibility(View.VISIBLE);
                                row.setVisibility(View.GONE);
                                checkAll.setEnabled(true);
                                for (CheckBox checkBox : checkBoxList) {
                                    checkBox.setEnabled(true);
                                }
//                        判断加工单状态
                                if (produceBom.getProduceStatusVo() == ProduceStatusVo.no)
                                    rb1.setChecked(true);
                                else
                                    rb2.setChecked(true);
                                //                       如果没有可选料框就显示“请输入”
                                if (checkBoxList.size() == 0)
                                    etBatchProduceNum.setHint("请输入");
                                etBatchProduceNum.setFocusable(true);
                                etBatchProduceNum.setFocusableInTouchMode(true);
                            } else {
                                etBatchProduceNum.setText("");
                                tv102.setText(produceBom.getProduceStatusVo().getValue());
                                tvBox.setText(box);
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

                        tvProduceNum.setText(produceBom.getReceiveNum() + produceBom.getHalf().getUnit().getName());
                        tvActualNum.setText(produceBom.getBatchGroup().getActualNum() + "");
                        tvSuccessNum.setText(produceBom.getBatchGroup().getSuccessNum() + "");

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
                        if (produceBom.getReceive() != null) {
                            tvReceiveOrder.setText(produceBom.getReceive().getCode());
                            tvReceiveOrderStatus.setText(produceBom.getReceive().getReceiveStatusVo().getValue());
                        } else {
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

//          末工序合格品数量的设置
                        if (produceBom.getReceive() != null)
                            tvLastSuccessNum.setText(produceBom.getProcessHalf() == null ? "" : produceBom.getProcessHalf().getSuccessNum() + "");
                        else
                            tvLastSuccessNum.setText("");
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

    public class HalfInAdapter extends BaseAdapter {
        private Context context;
        private List<ProduceBomBatch> list = null;

        public HalfInAdapter(Context context, List<ProduceBomBatch> list) {
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
                convertView = LayoutInflater.from(context).inflate(R.layout.table_list_edit, parent, false);
                holder = new ViewHolder();
                holder.tvActualNum = (TextView) convertView.findViewById(R.id.column1);
                holder.etSuccessNum = (EditText) convertView.findViewById(R.id.column2);
                holder.tvInOrder = (TextView) convertView.findViewById(R.id.column3);
                holder.tvInOrderStatus = (TextView) convertView.findViewById(R.id.column4);
                convertView.setTag(holder);
            } else
                holder = (ViewHolder) convertView.getTag();

            holder.tvActualNum.setText(list.get(position).getActualNum() + "");
//                获取最后一行的TestId字段
            test = list.get(list.size() - 1).getTestId();
            batchId = list.get(list.size() - 1).getId();//最后一行的id
//                    未检验的时候，合格品数显示为空
            if (test == 0) {
                holder.etSuccessNum.setText(list.get(position).getSuccessNum() + "");
//                    只有超级用户、系统管理员或检验员(角色id=5)才能“提交待入库”
                if (RoleHelper.isSuper(context) || RoleHelper.isAdministrator(context) || RoleHelper.isTestman(context)) {
                    btn.setVisibility(View.VISIBLE);
                    llRemark.setVisibility(View.VISIBLE);
                    btn.setText("提交待入库");
                    if (position == list.size() - 1) {
                        holder.etSuccessNum.setText("");
                        holder.etSuccessNum.setEnabled(true);
                        holder.etSuccessNum.setHint("请输入");
                        actualNum = list.get(position).getActualNum() + "";//获取最后一行的实际生产数量
                        holder.etSuccessNum.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                if (s.length() > 0)
                                    successNum = s.toString();
                                else
                                    successNum = "";
                            }
                        });
                    }
                }
            } else
                holder.etSuccessNum.setText(list.get(position).getSuccessNum() + "");
//         库存为空就直接设置为空字符
            if (list.get(position).getHalfInOrder() != null) {
                holder.tvInOrder.setText(list.get(position).getHalfInOrder().getCode());
                holder.tvInOrderStatus.setText(list.get(position).getHalfInOrder().getInOrderStatusVo().getValue());
            } else {
                holder.tvInOrder.setText("");
                holder.tvInOrderStatus.setText("");
            }

            return convertView;
        }

        public class ViewHolder {
            TextView tvActualNum;
            EditText etSuccessNum;
            TextView tvInOrder;
            TextView tvInOrderStatus;
        }
    }
}
