package com.example.administrator.cnmar.activity;

import android.content.Context;
import android.os.Bundle;
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
import java.util.HashMap;
import java.util.List;

import component.material.model.MaterialInOrder;
import component.material.model.MaterialInOrderMaterial;
import component.material.vo.InOrderStatusVo;

public class MaterialQualityControlDetailActivity extends AppCompatActivity {
    private TextView tvTitle;
    private LinearLayout llReturn;
    private TextView tvName11, tvName12, tvName21, tvName22, tvName31, tvName32;
    private TextView tvInOrder, tvArriveDate, tvInOrderStatus, tvTestStatus, tvTestPerson, tvTestTime;
    private MyListView listView;
    private String strUrl;
    private Button btnSubmit;
    private HashMap<Integer, String> map = new HashMap<>();
    private int id;
    MaterialInfoAdapter myAdapter;
    private String inOrderMaterialIds = "";
    private String res = "";
    private String failNums = "";
    private String role;
    private Boolean isSuper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quality_control_detail);
        AppExit.getInstance().addActivity(this);
        tvTitle = (TextView) findViewById(R.id.title);
        tvTitle.setText("原料检验-详情");

        llReturn = (LinearLayout) findViewById(R.id.left_arrow);
        llReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvName11 = (TextView) findViewById(R.id.name11);
        tvName12 = (TextView) findViewById(R.id.name12);
        tvName21 = (TextView) findViewById(R.id.name21);
        tvName22 = (TextView) findViewById(R.id.name22);
        tvName31 = (TextView) findViewById(R.id.name31);
        tvName32 = (TextView) findViewById(R.id.name32);

        tvName11.setText("入库单号");
        tvName12.setText("到货日期");
        tvName21.setText("入库单状态");
        tvName22.setText("检验状态");
        tvName31.setText("检验员");
        tvName32.setText("检验时间");

        tvInOrder = (TextView) findViewById(R.id.tv11);
        tvArriveDate = (TextView) findViewById(R.id.tv12);
        tvInOrderStatus = (TextView) findViewById(R.id.tv21);
        tvTestStatus = (TextView) findViewById(R.id.tv22);
        tvTestPerson = (TextView) findViewById(R.id.tv31);
        tvTestTime = (TextView) findViewById(R.id.tv32);


        listView = (MyListView) findViewById(R.id.lvTable);
//       listView.addFooterView(new ViewStub(getActivity()));
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

//        从SharedPreference中取出用户的角色，若角色包含"检验员"就显示按钮;如果是超级用户显示所有按钮
        role = SPHelper.getString(this, "Role", "");
        isSuper = SPHelper.getBoolean(this, "isSuper", false);

        //        取出传递到详情页面的id
        id = getIntent().getIntExtra("ID", 0);
        strUrl = UrlHelper.URL_QC_DETAIL.replace("{id}", String.valueOf(id));
        strUrl = UniversalHelper.getTokenUrl(strUrl);
        Log.d("QQCQC", strUrl);
        getMaterialListFromNet(strUrl);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                不合格数量的非空判断
                if (map.size() < myAdapter.getCount()) {
                    Toast.makeText(MaterialQualityControlDetailActivity.this, "请输入不合格数量再提交", Toast.LENGTH_SHORT).show();
                    return;
                }
                String inOrderMaterialIds1 = inOrderMaterialIds.substring(0, inOrderMaterialIds.length() - 1);
                String res1 = res.substring(0, res.length() - 1);
                for (int i = 0; i < map.size(); i++) {
                    failNums += map.get(i) + ",";
                }
                String failNums1 = failNums.substring(0, failNums.length() - 1);
                String url = UrlHelper.URL_TEST_COMMIT.replace("{inOrderId}", String.valueOf(id)).replace("{inOrderMaterialIds}", inOrderMaterialIds1).replace("{res}", res1).replace("{failNums}", failNums1);
                Log.d("strUrlstrUrlstrUrl", url);
                url = UniversalHelper.getTokenUrl(url);
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

    public void getMaterialListFromNet(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue quene = Volley.newRequestQueue(MaterialQualityControlDetailActivity.this);
                StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        String json = VolleyHelper.getJson(s);
                        Log.d("QC", json);
                        component.common.model.Response response = JSON.parseObject(json, component.common.model.Response.class);
                        MaterialInOrder materialInOrder = JSON.parseObject(response.getData().toString(), MaterialInOrder.class);
                        List<MaterialInOrderMaterial> list = materialInOrder.getInOrderMaterials();


                        tvInOrder.setText(materialInOrder.getCode());
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                        tvArriveDate.setText(sdf.format(materialInOrder.getArrivalDate()));
                        tvTestStatus.setText(materialInOrder.getRemark());
                        tvInOrderStatus.setText(materialInOrder.getInOrderStatusVo().getValue());


                        if (materialInOrder.getStatus() == InOrderStatusVo.test_fail.getKey()) {
                            tvTestStatus.setText("检验不合格");
                        } else {
                            tvTestStatus.setText("检验合格");
                        }

                        tvTestPerson.setText(materialInOrder.getTest().getName());
                        tvTestTime.setText(sdf1.format(materialInOrder.getTestTime()));

                        myAdapter = new MaterialInfoAdapter(MaterialQualityControlDetailActivity.this, list);
                        listView.setAdapter(myAdapter);
//                        只有单据状态为待检验并且用户拥有“检验员”角色或者是超级用户才能显示按钮
                        if (materialInOrder.getStatus() == InOrderStatusVo.pre_test.getKey() && (role.contains("检验员") || isSuper)) {
                            btnSubmit.setVisibility(View.VISIBLE);
                            btnSubmit.setText("跳转到检验页面");
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d("Tag", volleyError.toString());

                    }
                });
                quene.add(stringRequest);
            }
        }).start();
    }

    public class MaterialInfoAdapter extends BaseAdapter {
        private Context context;
        private List<MaterialInOrderMaterial> list = null;

        public MaterialInfoAdapter(Context context, List<MaterialInOrderMaterial> list) {
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
            holder.tvInvalidateNum.setText(String.valueOf(list.get(position).getFailNum()) + list.get(position).getMaterial().getUnit().getName());
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

}
