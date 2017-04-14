package com.example.administrator.cnmar.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import component.basic.vo.PackTypeVo;
import component.produce.model.ProduceBom;
import component.produce.model.ProduceReceive;

public class ReceiveMaterialOrderDetailActivity extends AppCompatActivity {
    private static String strUrl;
    private int id;
    private Context context;

    @BindView(R.id.name11)
    TextView name11;
    @BindView(R.id.name21)
    TextView name21;
    @BindView(R.id.name22)
    TextView name22;
    @BindView(R.id.name41)
    TextView name41;
    @BindView(R.id.name42)
    TextView name42;
    @BindView(R.id.name51)
    TextView name51;
    @BindView(R.id.name52)
    TextView name52;
    @BindView(R.id.name61)
    TextView name61;
    @BindView(R.id.name62)
    TextView name62;
    @BindView(R.id.left_arrow)
    LinearLayout leftArrow;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tv11)
    TextView tvReceiveMaterialOrder;
    @BindView(R.id.name12)
    TextView name12;
    @BindView(R.id.tv12)
    TextView tvPlanNo;
    @BindView(R.id.tv21)
    TextView tvMaterialOutOrder;
    @BindView(R.id.tv22)
    TextView tvHalfOutOrder;
    @BindView(R.id.name31)
    TextView name31;
    @BindView(R.id.tv31)
    TextView tvCode;
    @BindView(R.id.name32)
    TextView name32;
    @BindView(R.id.tv32)
    TextView tvName;
    @BindView(R.id.tv41)
    TextView tvSize;
    @BindView(R.id.tv42)
    TextView tvUnit;
    @BindView(R.id.tv51)
    TextView tvProduceNum;
    @BindView(R.id.tv52)
    TextView tvStatus;
    @BindView(R.id.tv61)
    TextView tvPerson;
    @BindView(R.id.tv62)
    TextView tvTime;
    @BindView(R.id.lvTableMaterial)
    MyListView lvTableMaterial;
    @BindView(R.id.lvTableHalf)
    MyListView lvTableHalf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_material_order_detail);
        ButterKnife.bind(this);
        AppExit.getInstance().addActivity(this);

        id = getIntent().getIntExtra("ID", 0);
        //   从登陆页面取出用户的角色信息
//        role = LoginActivity.sp.getString("Role", "123");
//        isSuper = LoginActivity.sp.getBoolean("isSuper", false);

        init();
        strUrl = UrlHelper.URL_RECEIVE_MATERIAL_ORDER_DETAIL.replace("{ID}", String.valueOf(id));
        strUrl = UniversalHelper.getTokenUrl(strUrl);
        getListFromNet();
    }

    public void init() {
        name11.setText("领料单编号");
        name21.setText("原料出库单号");
        name22.setText("半成品出库单号");
        name51.setText("计划生产数量");
        name52.setText("领料单状态");
        name61.setText("领料人");
        name62.setText("领料时间");

        context = ReceiveMaterialOrderDetailActivity.this;
        title.setText("领料单详情");
        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,PlanManageActivity.class);
                intent.putExtra("flag",2);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent=new Intent(context,PlanManageActivity.class);
            intent.putExtra("flag",2);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    public void getListFromNet() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue queue = Volley.newRequestQueue(ReceiveMaterialOrderDetailActivity.this);
                StringRequest stringRequest = new StringRequest(strUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        String json = VolleyHelper.getJson(s);
                        component.common.model.Response response = JSON.parseObject(json, component.common.model.Response.class);
                        ProduceReceive produceReceive = JSON.parseObject(response.getData().toString(), ProduceReceive.class);
//       根据PlanId字段来判断显示加工单还是子加工单，PlanId()>0显示加工单
                        if (produceReceive.getPlanId() > 0) {
                            name12.setText("加工单编号");
                            name31.setText("成品编码");
                            name32.setText("成品名称");
                            name41.setText("成品规格");
                            name42.setText("成品单位");

                            tvPlanNo.setText(produceReceive.getPlan().getCode());
                            tvCode.setText(produceReceive.getPlan().getProduct().getCode());
                            tvName.setText(produceReceive.getPlan().getProduct().getName());
                            tvSize.setText(produceReceive.getPlan().getProduct().getSpec());
                            //                        有包装的，单位格式为“9个/袋”
                            if (produceReceive.getPlan().getProduct().getPackType() != PackTypeVo.empty.getKey())
                                tvUnit.setText(produceReceive.getPlan().getProduct().getPackNum()
                                        + produceReceive.getPlan().getProduct().getUnit().getName()
                                        + " / " + produceReceive.getPlan().getProduct().getPackTypeVo().getValue().substring(1, 2));
                            else
                                tvUnit.setText(produceReceive.getPlan().getProduct().getUnit().getName());

                            tvProduceNum.setText(produceReceive.getPlan().getProduceNum() + produceReceive.getPlan().getProduct().getUnit().getName());
                            lvTableMaterial.setAdapter(new MaterialAdapter(context, produceReceive.getPlan().getMaterialSubs()));
                            lvTableHalf.setAdapter(new HalfAdapter(context, produceReceive.getPlan().getHalfSubs()));

                        } else {
                            name12.setText("子加工单编号");
                            name31.setText("半成品编码");
                            name32.setText("半成品名称");
                            name41.setText("半成品规格");
                            name42.setText("半成品单位");
                            tvPlanNo.setText(produceReceive.getBom().getCode());
                            tvCode.setText(produceReceive.getBom().getHalf().getCode());
                            tvName.setText(produceReceive.getBom().getHalf().getName());
                            tvSize.setText(produceReceive.getBom().getHalf().getSpec());
                            //                        有包装的，单位格式为“9个/袋”
                            if (produceReceive.getBom().getHalf().getPackType() != PackTypeVo.empty.getKey())
                                tvUnit.setText(produceReceive.getBom().getHalf().getPackNum()
                                        + produceReceive.getBom().getHalf().getUnit().getName()
                                        + " / " + produceReceive.getBom().getHalf().getPackTypeVo().getValue().substring(1, 2));
                            else
                                tvUnit.setText(produceReceive.getBom().getHalf().getUnit().getName());

                            tvProduceNum.setText(produceReceive.getBom().getReceiveNum() + produceReceive.getBom().getHalf().getUnit().getName());
                            lvTableMaterial.setAdapter(new MaterialAdapter(context, produceReceive.getBom().getMaterialSubs()));
                            lvTableHalf.setAdapter(new HalfAdapter(context, produceReceive.getBom().getHalfSubs()));
                        }

                        tvReceiveMaterialOrder.setText(produceReceive.getCode());
                        tvMaterialOutOrder.setText(produceReceive.getMaterialOutOrder() == null ? "" : produceReceive.getMaterialOutOrder().getCode());
                        tvHalfOutOrder.setText(produceReceive.getHalfOutOrder() == null ? "" : produceReceive.getHalfOutOrder().getCode());
                        tvStatus.setText(produceReceive.getReceiveStatusVo().getValue());
                        tvPerson.setText(produceReceive.getReceiveUser().getName());

                        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

//                        只有已领料才有领料时间，否则设置为空
                        if (produceReceive.getReceiveTime() == null)
                            tvTime.setText("");
                        else
                            tvTime.setText(sdf1.format(produceReceive.getReceiveTime()));

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
                holder.tvSpec = (TextView) convertView.findViewById(R.id.column3);
                holder.tvNum = (TextView) convertView.findViewById(R.id.column4);

                convertView.setTag(holder);
            } else
                holder = (ViewHolder) convertView.getTag();

            holder.tvMaterialCode.setText(list.get(position).getMaterial().getCode());
            holder.tvMaterialName.setText(list.get(position).getMaterial().getName());
            holder.tvSpec.setText(list.get(position).getMaterial().getSpec());

            holder.tvNum.setText(String.valueOf(list.get(position).getReceiveNum()) + list.get(position).getMaterial().getUnit().getName());

            return convertView;
        }

        public class ViewHolder {
            TextView tvMaterialCode;
            TextView tvMaterialName;
            TextView tvSpec;
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
                holder.tvSpec = (TextView) convertView.findViewById(R.id.column3);
                holder.tvNum = (TextView) convertView.findViewById(R.id.column4);

                convertView.setTag(holder);
            } else
                holder = (ViewHolder) convertView.getTag();

            holder.tvHalfCode.setText(list.get(position).getHalf().getCode());
            holder.tvHalfName.setText(list.get(position).getHalf().getName());
            holder.tvSpec.setText(list.get(position).getHalf().getSpec());

            holder.tvNum.setText(String.valueOf(list.get(position).getReceiveNum()) + list.get(position).getHalf().getUnit().getName());

            return convertView;
        }

        public class ViewHolder {
            TextView tvHalfCode;
            TextView tvHalfName;
            TextView tvSpec;
            TextView tvNum;
        }
    }
}
