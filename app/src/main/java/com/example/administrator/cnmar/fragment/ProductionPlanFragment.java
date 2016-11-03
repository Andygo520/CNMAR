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
import com.example.administrator.cnmar.activity.ProductionPlanDetailActivity;
import com.example.administrator.cnmar.entity.MyListView;
import com.example.administrator.cnmar.helper.UniversalHelper;
import com.example.administrator.cnmar.helper.UrlHelper;
import com.example.administrator.cnmar.http.VolleyHelper;

import java.util.ArrayList;
import java.util.List;

import component.produce.model.ProducePlan;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductionPlanFragment extends Fragment {
    //    page代表显示的是第几页内容，从1开始
    int page = 1;
    private MyListView listView;
    private BillAdapter myAdapter;
    private LinearLayout llSearch;
    private EditText etSearchInput;
    private ImageView ivDelete;
    private MaterialRefreshLayout materialRefreshLayout;
    private Handler handler = new Handler();
    //    用来存放从后台取出的数据列表，作为adapter的数据源
    private List<ProducePlan> data = new ArrayList<>();
    private String strUrl = UniversalHelper.getTokenUrl(UrlHelper.URL_PRODUCE_PLAN.replace("{page}", String.valueOf(page)));

    public ProductionPlanFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_production_plan, container, false);
        listView = (MyListView) view.findViewById(R.id.listView);
//        listView.addFooterView(new ViewStub(getActivity()));
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

                        getPlanListFromNet(strUrl);
                        Log.d("llSearch", strUrl);

                        materialRefreshLayout.finishRefresh();
                    }
                }, 400);
            }

            @Override
            public void onRefreshLoadMore(final MaterialRefreshLayout materialRefreshLayout) {
                materialRefreshLayout.setLoadMore(false);

                if (page == 1 && myAdapter.getCount() < 10) {
                    materialRefreshLayout.finishRefreshLoadMore();

                } else {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            myAdapter = new BillAdapter();
                            page++;
                            String url = UniversalHelper.getTokenUrl(UrlHelper.URL_PRODUCE_PLAN.replace("{page}", String.valueOf(page)));
                            Log.d("url2", url);
                            getPlanListFromNet(url);
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
        etSearchInput.setHint("加工单编号查询");
        etSearchInput.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    String input = etSearchInput.getText().toString().trim();
                    if (input.equals("")) {
                        Toast.makeText(getActivity(), "请输入内容后再查询", Toast.LENGTH_SHORT).show();
                    } else {
                        String urlString = UrlHelper.URL_SEARCH_PRODUCE_PLAN.replace("{query.code}", input);
                        urlString = UniversalHelper.getTokenUrl(urlString);
                        Log.d("Search", urlString);
                        myAdapter = null;

                        getPlanListFromNet(urlString);
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
                    getPlanListFromNet(strUrl);

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
                String urlString = UrlHelper.URL_SEARCH_PRODUCE_PLAN.replace("{query.code}", input);
                urlString = UniversalHelper.getTokenUrl(urlString);
                myAdapter = null;

                getPlanListFromNet(urlString);
            }
        });
//        getPlanListFromNet(strUrl);
        return view;
    }

    public void getPlanListFromNet(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue quene = Volley.newRequestQueue(getActivity());
                StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        String json = VolleyHelper.getJson(s);
                        Log.d("GGGG", s);
                        component.common.model.Response response = JSON.parseObject(json, component.common.model.Response.class);
                        List<ProducePlan> list = JSON.parseArray(response.getData().toString(), ProducePlan.class);
                        if (myAdapter == null) {
                            data = list;
                            myAdapter = new BillAdapter(data, getActivity());
                            listView.setAdapter(myAdapter);
                        } else {
                            data.addAll(list);
//                            myAdapter.notifyDataSetChanged();
                            myAdapter = new BillAdapter(data, getActivity());
                            listView.setAdapter(myAdapter);
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
        private List<ProducePlan> list = null;

        public BillAdapter(List<ProducePlan> list, Context context) {
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
                convertView = LayoutInflater.from(context).inflate(R.layout.in_order_item, parent, false);
                holder.tvPlanNo = (TextView) convertView.findViewById(R.id.tvInOrderNo);
                holder.tvProductCode = (TextView) convertView.findViewById(R.id.tvArriveDate);
                holder.tvProduceNum = (TextView) convertView.findViewById(R.id.tvInOrderStatus);
                holder.detail = (TextView) convertView.findViewById(R.id.detail);
                convertView.setTag(holder);
            } else
                holder = (ViewHolder) convertView.getTag();

            holder.tvPlanNo.setText(list.get(position).getCode());
            holder.tvProductCode.setText(list.get(position).getProduct().getCode());
            holder.tvProduceNum.setText(String.valueOf(list.get(position).getProduceNum()));
            holder.detail.setText("详情");
            holder.detail.setTextColor(getResources().getColor(R.color.colorBase));
            holder.detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ProductionPlanDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("ID", list.get(position).getId());
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
            return convertView;
        }

        class ViewHolder {
            public TextView tvPlanNo;
            public TextView tvProductCode;
            public TextView tvProduceNum;
            public TextView detail;
        }

    }


}
