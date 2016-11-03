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
import android.widget.BaseAdapter;
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
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.example.administrator.cnmar.R;
import com.example.administrator.cnmar.activity.HalfProductCheckStockDetailActivity;
import com.example.administrator.cnmar.entity.MyListView;
import com.example.administrator.cnmar.helper.UniversalHelper;
import com.example.administrator.cnmar.helper.UrlHelper;
import com.example.administrator.cnmar.http.VolleyHelper;

import java.util.ArrayList;
import java.util.List;

import component.half.model.HalfStockCheck;

/**
 * A simple {@link Fragment} subclass.
 */
public class HalfProductCheckStockFragmentQuery extends Fragment {
    private MyListView lvCheckQuery;
    private LinearLayout llSearch;
    private EditText etSearchInput;
    private ImageView ivDelete;
    private TextView tvField1, tvField2, tvField3, tvField4;
    private MaterialRefreshLayout materialRefreshLayout;
    private Handler handler = new Handler();
    private BillAdapter myAdapter;
    int page = 1;    //    page代表显示的是第几页内容，从1开始
    private int total; // 总页数
    private int num = 1; // 第几页
    private int count; // 数据总条数

    //    用来存放从后台取出的数据列表，作为adapter的数据源
    private List<HalfStockCheck> data = new ArrayList<>();
    private String url = UniversalHelper.getTokenUrl(UrlHelper.URL_HALF_PRODUCT_CHECK_QUERY.replace("{page}", String.valueOf(page)));

    public HalfProductCheckStockFragmentQuery() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_half_product_check_stock_fragment_query, container, false);
        lvCheckQuery = (MyListView) view.findViewById(R.id.lvTable);
//        lvCheckQuery.addFooterView(new ViewStub(context));
        ivDelete = (ImageView) view.findViewById(R.id.ivDelete);

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
                        getCheckStockQueryListFromNet(url);
                        materialRefreshLayout.finishRefresh();
                    }
                }, 400);
            }

            @Override
            public void onRefreshLoadMore(final MaterialRefreshLayout materialRefreshLayout) {

                if (count <= 10) {
                    materialRefreshLayout.setLoadMore(false);
                } else {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            myAdapter = new BillAdapter();
                            page++;
//                            当page等于总页数的时候，提示“加载完成”，不能继续上拉加载更多
                            if (page==total){
                                String url = UniversalHelper.getTokenUrl(UrlHelper.URL_HALF_PRODUCT_CHECK_QUERY.replace("{page}", String.valueOf(page)));
                                Log.d("urlfinish", url);
                                getCheckStockQueryListFromNet(url);
                                Toast.makeText(getActivity(), "加载完成", Toast.LENGTH_SHORT).show();
                                // 结束上拉刷新...
                                materialRefreshLayout.finishRefreshLoadMore();
                                materialRefreshLayout.setLoadMore(false);
                                return;
                            }
                            String url = UniversalHelper.getTokenUrl(UrlHelper.URL_HALF_PRODUCT_CHECK_QUERY.replace("{page}", String.valueOf(page)));
                            Log.d("urlmore", url);
                            getCheckStockQueryListFromNet(url);
                            Toast.makeText(getActivity(), "已加载更多", Toast.LENGTH_SHORT).show();
                            // 结束上拉刷新...
                            materialRefreshLayout.finishRefreshLoadMore();
                        }
                    }, 400);
                }




            }
        });

        llSearch = (LinearLayout) view.findViewById(R.id.llSearch);
        tvField1 = (TextView) view.findViewById(R.id.column1);
        tvField2 = (TextView) view.findViewById(R.id.column2);
        tvField3 = (TextView) view.findViewById(R.id.column3);
        tvField4 = (TextView) view.findViewById(R.id.column4);
        tvField1.setText("半成品编码");
        tvField2.setText("盘点前总量");
        tvField3.setText("盘点后总量");
        tvField4.setText("操作");


        etSearchInput = (EditText) view.findViewById(R.id.etSearchInput);
        etSearchInput.setHint("半成品编码查询");
        etSearchInput.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    String input = etSearchInput.getText().toString().trim();
                    if (input.equals("")) {
                        Toast.makeText(getActivity(), "请输入内容后再查询", Toast.LENGTH_SHORT).show();
                    } else {
                        String urlString = UrlHelper.URL_SEARCH_HALF_PRODUCT_CHECK_QUERY.replace("{query.code}", input);
                        urlString = UniversalHelper.getTokenUrl(urlString);
                        myAdapter = null;
                        getCheckStockQueryListFromNet(urlString);
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
                    getCheckStockQueryListFromNet(url);
                }else{
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
                String urlString = UrlHelper.URL_SEARCH_HALF_PRODUCT_CHECK_QUERY.replace("{query.code}", input);
                urlString = UniversalHelper.getTokenUrl(urlString);
                myAdapter = null;
                getCheckStockQueryListFromNet(urlString);
            }
        });
        return view;
    }

    public void getCheckStockQueryListFromNet(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue quene = Volley.newRequestQueue(getParentFragment().getActivity());
                Log.d("Tag", "开始执行");
                StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        String json = VolleyHelper.getJson(s);
                        component.common.model.Response response = JSON.parseObject(json, component.common.model.Response.class);
                        List<HalfStockCheck> list = JSON.parseArray(response.getData().toString(), HalfStockCheck.class);
                        count = response.getPage().getCount();
                        total = response.getPage().getTotal();
                        num = response.getPage().getNum();
                        if (myAdapter == null) {
                            data = list;
                            myAdapter = new BillAdapter(data, getActivity());
                            lvCheckQuery.setAdapter(myAdapter);
                        } else {
                            data.addAll(list);
//                            myAdapter.notifyDataSetChanged();
                            myAdapter = new BillAdapter(data, getActivity());
                            lvCheckQuery.setAdapter(myAdapter);
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
        private List<HalfStockCheck> list = null;

        public BillAdapter(List<HalfStockCheck> list, Context context) {
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
                convertView = LayoutInflater.from(context).inflate(R.layout.table_list_item, parent, false);
                holder.tvCode = (TextView) convertView.findViewById(R.id.column1);
                holder.tvPreNum = (TextView) convertView.findViewById(R.id.column2);
                holder.tvAfterNum = (TextView) convertView.findViewById(R.id.column3);
                holder.tvDetail = (TextView) convertView.findViewById(R.id.column4);
                convertView.setTag(holder);
            } else
                holder = (ViewHolder) convertView.getTag();
//            Log.d("GGGG", DateFormat.getDateInstance().format(list.get(position).getArrivalDate()));
            holder.tvCode.setText(list.get(position).getHalf().getCode());
            holder.tvPreNum.setText(String.valueOf(list.get(position).getBeforeStock()));
            holder.tvAfterNum.setText(String.valueOf(list.get(position).getAfterStock()));
            holder.tvDetail.setText("详情");
            holder.tvDetail.setTextColor(getResources().getColor(R.color.colorBase));
            holder.tvDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, HalfProductCheckStockDetailActivity.class);
                    intent.putExtra("ID", list.get(position).getId());
//                    intent.putExtra("flag", 0);
                    context.startActivity(intent);
                }
            });
            return convertView;
        }

        class ViewHolder {
            public TextView tvCode;
            public TextView tvPreNum;
            public TextView tvAfterNum;
            public TextView tvDetail;
        }

    }
}
