package com.example.administrator.cnmar.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.administrator.cnmar.activity.LoginActivity;
import com.example.administrator.cnmar.activity.MaterialStockActivity;
import com.example.administrator.cnmar.R;
import com.example.administrator.cnmar.entity.MyListView;
import com.example.administrator.cnmar.helper.UniversalHelper;
import com.example.administrator.cnmar.helper.UrlHelper;
import com.example.administrator.cnmar.http.VolleyHelper;

import java.util.HashMap;
import java.util.List;

import component.material.model.MaterialInOrder;
import component.material.model.MaterialInOrderMaterial;
import component.material.vo.InOrderStatusVo;

/**
 * A simple {@link Fragment} subclass.
 */
public class InOrderDetailState1Fragment extends Fragment {
    private MyListView listView;
    private String strUrl;
    private Button btnSubmit;
    private HashMap<Integer, String> map = new HashMap<>();
    private int id, testId;
    MaterialInfoAdapter myAdapter;
    private String inOrderMaterialIds = "";
    private String res = "";
    private String failNums = "";
    private String role;
    private Boolean isSuper;


    public InOrderDetailState1Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_in_order_detail_state1, container, false);
        listView = (MyListView) view.findViewById(R.id.lvTable);
//       listView.addFooterView(new ViewStub(getActivity()));
        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);

//        从SharedPreference中取出用户的角色，若角色包含"检验员"就显示按钮;如果是超级用户显示所有按钮
        role = LoginActivity.sp.getString("Role", "123");
        isSuper = LoginActivity.sp.getBoolean("isSuper", false);

        //        取出传递到详情页面的id，以及从登陆页面传递过来的用户的id（提交检验结果的时候用到）
        id = getActivity().getIntent().getIntExtra("ID", 0);
        testId = LoginActivity.sp.getInt("userId", 0);

        strUrl = UrlHelper.URL_IN_ORDER_DETAIL.replace("{id}", String.valueOf(id));
        strUrl = UniversalHelper.getTokenUrl(strUrl);
        getMaterialListFromNet(strUrl);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                不合格数量的非空判断
                if (map.size() < myAdapter.getCount()) {
                    Toast.makeText(getActivity(), R.string.input_fail_num , Toast.LENGTH_SHORT).show();
                    return;
                }
                String inOrderMaterialIds1 = inOrderMaterialIds.substring(0, inOrderMaterialIds.length() - 1);
                String res1 = res.substring(0, res.length() - 1);
                for (int i = 0; i < map.size(); i++) {
                    failNums += map.get(i) + ",";
                }
                String failNums1 = failNums.substring(0, failNums.length() - 1);
                String url = UrlHelper.URL_TEST_COMMIT.replace("{inOrderId}", String.valueOf(id)).replace("{testId}", String.valueOf(testId)).replace("{inOrderMaterialIds}", inOrderMaterialIds1).replace("{res}", res1).replace("{failNums}", failNums1);
                Log.d("strUrlstrUrlstrUrl", url);
                url = UniversalHelper.getTokenUrl(url);
                sendRequest(url);
            }
        });

        return view;
    }

    public void getMaterialListFromNet(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue quene = Volley.newRequestQueue(getActivity());
                StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        String json = VolleyHelper.getJson(s);
                        component.common.model.Response response = JSON.parseObject(json, component.common.model.Response.class);
                        MaterialInOrder materialInOrder = JSON.parseObject(response.getData().toString(), MaterialInOrder.class);
                        List<MaterialInOrderMaterial> list = materialInOrder.getInOrderMaterials();

//                        Log.d("materialIdsmate",String.valueOf(materialIds.size()));
//                        只有单据状态为待检验并且用户拥有“检验员”角色或者是超级用户才能显示按钮
                        if (materialInOrder.getStatus() == InOrderStatusVo.pre_test.getKey() && (role.contains("检验员") || isSuper)) {
                            btnSubmit.setVisibility(View.VISIBLE);
                            myAdapter = new MaterialInfoAdapter(getActivity(), list);
                            listView.setAdapter(myAdapter);

                        } else {
                            MaterialInfoAdapter1 myAdapter = new MaterialInfoAdapter1(getActivity(), list);
                            listView.setAdapter(myAdapter);
//                            setListViewHeightBasedOnChildren(listView);
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

    //    /**
//     * 动态设置ListView的高度
//     * @param listView
//     */
//    public static void setListViewHeightBasedOnChildren(ListView listView) {
//        if(listView == null) return;
//        ListAdapter listAdapter = listView.getAdapter();
//        if (listAdapter == null) {
//            // pre-condition
//            return;
//        }
//        int totalHeight = 0;
//        for (int i = 0; i < listAdapter.getCount(); i++) {
//            View listItem = listAdapter.getView(i, null, listView);
//            listItem.measure(0, 0);
//            totalHeight += listItem.getMeasuredHeight();
//        }
//        ViewGroup.LayoutParams params = listView.getLayoutParams();
//        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
//        listView.setLayoutParams(params);
//    }
    public void sendRequest(String url) {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Intent intent = new Intent(getActivity(), MaterialStockActivity.class);
                intent.putExtra("flag", 1);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        queue.add(stringRequest);

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

    public class MaterialInfoAdapter1 extends BaseAdapter {
        private Context context;
        private List<MaterialInOrderMaterial> list = null;

        public MaterialInfoAdapter1(Context context, List<MaterialInOrderMaterial> list) {
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
}
