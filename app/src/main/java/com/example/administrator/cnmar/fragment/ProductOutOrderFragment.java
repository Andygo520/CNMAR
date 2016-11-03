package com.example.administrator.cnmar.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.example.administrator.cnmar.R;
import com.example.administrator.cnmar.activity.ProductOutOrderDetailActivity;
import com.example.administrator.cnmar.entity.MyListView;
import com.example.administrator.cnmar.helper.UniversalHelper;
import com.example.administrator.cnmar.helper.UrlHelper;
import com.example.administrator.cnmar.http.VolleyHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import component.product.model.ProductOutOrder;
import component.product.vo.OutOrderStatusVo;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductOutOrderFragment extends Fragment {
    private MyListView lvOutOrder;
    private LinearLayout llSearch;
    private EditText etSearchInput;
    private ImageView ivDelete;

    private MaterialRefreshLayout materialRefreshLayout;
    private Handler handler = new Handler();
    private BillAdapter myAdapter;
    int page = 1;    //    page代表显示的是第几页内容，从1开始
    private int total; // 总页数
    private int num = 1; // 第几页
    private int count; // 数据总条数

    private Spinner spinner;
    //    配合Spinner使用的分割线
    private TextView tvLine;
    //   数组用来存放所有出库单状态
    String[] status = {"所有状态", "待出库", "未全部出库", "已出库"};
    //    map用来将出库单状态跟OutOrderStatusVo里面的状态关联
    private Map<String, Object> map = new HashMap<>();

    //    用来存放从后台取出的数据列表，作为adapter的数据源
    private List<ProductOutOrder> data = new ArrayList<>();
    private String url = UniversalHelper.getTokenUrl(UrlHelper.URL_PRODUCT_OUT_ORDER.replace("{page}", String.valueOf(page)));

    public ProductOutOrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_out_order, container, false);
        lvOutOrder = (MyListView) view.findViewById(R.id.lvOutOrder);
//        lvOutOrder.addFooterView(new ViewStub(getActivity()));
        ivDelete = (ImageView) view.findViewById(R.id.ivDelete);

        //      在map中存入状态与Vo的对应关系，“所有状态”存入空字符
        map.put(status[0], "");
        map.put(status[1], OutOrderStatusVo.pre_out_stock.getKey());
        map.put(status[2], OutOrderStatusVo.not_all.getKey());
        map.put(status[3], OutOrderStatusVo.out_stock.getKey());

        spinner = (Spinner) view.findViewById(R.id.spinner);
        spinner.setVisibility(View.VISIBLE);
//       只有在拥有垂直下拉列表的Fragment才显示这个分割线
        tvLine = (TextView) view.findViewById(R.id.seperator_layout);
        tvLine.setVisibility(View.VISIBLE);
// 建立数据源
        String[] mItems = getResources().getStringArray(R.array.outOrderStatus);
        // 建立Adapter并且绑定数据源
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, mItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //绑定 Adapter到控件
        spinner.setAdapter(adapter);
//      默认情况下不激活setOnItemSelectedListener方法，只有选择的时候才调用该方法
        spinner.setSelection(0, false);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String urlString = UrlHelper.URL_SEARCH_PRODUCT_OUT_ORDER.replace("{query.code}", "").replace("{query.status}", String.valueOf(map.get(status[position])));
                urlString = UniversalHelper.getTokenUrl(urlString);
                Log.d("status", urlString);
                myAdapter = null;
                getOutOrderListFromNet(urlString);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        materialRefreshLayout = (MaterialRefreshLayout) view.findViewById(R.id.refresh);
        materialRefreshLayout.autoRefresh();//drop-down refresh automatically
        materialRefreshLayout.setLoadMore(true);

//        materialRefreshLayout.autoRefreshLoadMore();
        materialRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {
                //一般加载数据都是在子线程中，这里我用到了handler
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        myAdapter = null;
//                      下拉刷新默认显示第一页（10条）内容
                        page = 1;
                        getOutOrderListFromNet(url);
                        materialRefreshLayout.finishRefresh();
                    }
                }, 400);
            }

            @Override
            public void onRefreshLoadMore(final MaterialRefreshLayout materialRefreshLayout) {

                if (count <= 10) {
                    materialRefreshLayout.setLoadMore(false);
                    materialRefreshLayout.finishRefreshLoadMore();

                } else {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            myAdapter = new BillAdapter();
                            page++;
//                            当page等于总页数的时候，提示“加载完成”，不能继续上拉加载更多
                            if (page == total) {
                                String url = UniversalHelper.getTokenUrl(UrlHelper.URL_PRODUCT_OUT_ORDER.replace("{page}", String.valueOf(page)));
                                Log.d("urlfinish", url);
                                getOutOrderListFromNet(url);
                                Toast.makeText(getActivity(), "加载完成", Toast.LENGTH_SHORT).show();
                                // 结束上拉刷新...
                                materialRefreshLayout.finishRefreshLoadMore();
                                materialRefreshLayout.setLoadMore(false);
                                return;
                            }
                            String url = UniversalHelper.getTokenUrl(UrlHelper.URL_PRODUCT_OUT_ORDER.replace("{page}", String.valueOf(page)));
                            Log.d("urlmore", url);
                            getOutOrderListFromNet(url);
                            Toast.makeText(getActivity(), "已加载更多", Toast.LENGTH_SHORT).show();
                            // 结束上拉刷新...
                            materialRefreshLayout.finishRefreshLoadMore();
                        }
                    }, 400);
                }


            }
        });
        llSearch = (LinearLayout) view.findViewById(R.id.llSearch);
        etSearchInput = (EditText) view.findViewById(R.id.etSearchInput);
        etSearchInput.setHint("出库单号查询");
        etSearchInput.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    String input = etSearchInput.getText().toString().trim();
                    if (input.equals("")) {
                        Toast.makeText(getActivity(), "请输入内容后再查询", Toast.LENGTH_SHORT).show();
                    } else {
                        String urlString = UniversalHelper.getTokenUrl(UrlHelper.URL_SEARCH_PRODUCT_OUT_ORDER.replace("{query.code}", input));
                        myAdapter = null;
                        getOutOrderListFromNet(urlString);
                    }
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                    }
                    return true;
                }
                return false;
            }

        });
        etSearchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("")) {
                    ivDelete.setVisibility(View.GONE);
                    myAdapter = null;
                    getOutOrderListFromNet(url);

                } else {
                    ivDelete.setVisibility(View.VISIBLE);
                    ivDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            etSearchInput.setText("");
                        }
                    });
                }
            }
        });
        llSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm.isActive()) {
                    imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                }
                String input = etSearchInput.getText().toString().trim();
                if (input.equals("")) {
                    Toast.makeText(getActivity(), "请输入内容后再查询", Toast.LENGTH_SHORT).show();
                    return;
                }

                String urlString = UniversalHelper.getTokenUrl(UrlHelper.URL_SEARCH_PRODUCT_OUT_ORDER.replace("{query.code}", input));
                myAdapter = null;
                getOutOrderListFromNet(urlString);
            }
        });
//        getOutOrderListFromNet(url);
        return view;
    }

    public void getOutOrderListFromNet(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue quene = Volley.newRequestQueue(getActivity());
//                Log.d("Tag","开始执行");
                StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        String json = VolleyHelper.getJson(s);
//                        Log.d("GGGG",json);
                        component.common.model.Response response = JSON.parseObject(json, component.common.model.Response.class);
                        List<ProductOutOrder> list = JSON.parseArray(response.getData().toString(), ProductOutOrder.class);

                        count = response.getPage().getCount();
                        total = response.getPage().getTotal();
                        num = response.getPage().getNum();
                        if (myAdapter == null) {
                            data = list;
                            myAdapter = new BillAdapter(data, getActivity());
                            lvOutOrder.setAdapter(myAdapter);
                        } else {
                            data.addAll(list);
//                            myAdapter.notifyDataSetChanged();
                            myAdapter = new BillAdapter(data, getActivity());
                            lvOutOrder.setAdapter(myAdapter);
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

    class BillAdapter extends BaseAdapter {
        private Context context;
        private List<ProductOutOrder> list = null;

        public BillAdapter(List<ProductOutOrder> list, Context context) {
            this.list = list;
            this.context = context;
        }

        public BillAdapter() {

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
                holder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.out_order_item, parent, false);
                holder.tvOutOrderNo = (TextView) convertView.findViewById(R.id.tvOutOrderNo);
                holder.tvPlanNo = (TextView) convertView.findViewById(R.id.tvPlanNo);
                holder.tvOutOrderStatus = (TextView) convertView.findViewById(R.id.tvOutOrderStatus);
                holder.detail = (TextView) convertView.findViewById(R.id.detail);
                convertView.setTag(holder);
            } else
                holder = (ViewHolder) convertView.getTag();
//            Log.d("GGGG", DateFormat.getDateInstance().format(list.get(position).getArrivalDate()));
            holder.tvOutOrderNo.setText(list.get(position).getCode());

//            设置交付计划编号
            if (list.get(position).getDeliverPlan() != null)
                holder.tvPlanNo.setText(list.get(position).getDeliverPlan().getCode());
            else
                holder.tvPlanNo.setText("");

            holder.tvOutOrderStatus.setText(list.get(position).getOutOrderStatusVo().getValue());
            holder.detail.setText("详情");
            holder.detail.setTextColor(getResources().getColor(R.color.colorBase));
            holder.detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ProductOutOrderDetailActivity.class);
                    intent.putExtra("ID", list.get(position).getId());
//                    intent.putExtra("flag", 0);
                    context.startActivity(intent);
                }
            });
            return convertView;
        }

        class ViewHolder {
            public TextView tvOutOrderNo;
            public TextView tvPlanNo;
            public TextView tvOutOrderStatus;
            public TextView detail;
        }

    }

}
