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
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.administrator.cnmar.R;
import com.example.administrator.cnmar.activity.MaterialCheckStockDetailActivity;
import com.example.administrator.cnmar.entity.MyListView;
import com.example.administrator.cnmar.helper.UniversalHelper;
import com.example.administrator.cnmar.helper.UrlHelper;
import com.example.administrator.cnmar.helper.VolleyHelper;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import component.basic.vo.StockTypeVo;
import component.material.model.MaterialStockCheck;

/**
 * A simple {@link Fragment} subclass.
 */
public class MaterialCheckStockFragmentQuery extends Fragment {
    //    表头4个字段
    private TextView tv1, tv2, tv3, tv4;
    private MyListView lvCheckQuery;
    private LinearLayout llSearch;
    private EditText etSearchInput;
    private ImageView ivDelete;
    private TextView tvField1, tvField2, tvField3, tvField4;
    private TwinklingRefreshLayout refreshLayout;
    private Handler handler = new Handler();
    private BillAdapter myAdapter;
    int page = 1;    //    page代表显示的是第几页内容，从1开始
    private int total; // 总页数
    private int num = 1; // 第几页
    private int count; // 数据总条数

    //    用来存放从后台取出的数据列表，作为adapter的数据源
    private List<MaterialStockCheck> data = new ArrayList<>();
    private String url = UniversalHelper.getTokenUrl(UrlHelper.URL_CHECK_QUERY.replace("{page}", String.valueOf(page)));

    public MaterialCheckStockFragmentQuery() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.refresh_frame, container, false);

        tv1 = (TextView) view.findViewById(R.id.tv1);
        tv2 = (TextView) view.findViewById(R.id.tv2);
        tv3 = (TextView) view.findViewById(R.id.tv3);
        tv4 = (TextView) view.findViewById(R.id.tv4);

        tv1.setText("原料编码");
        tv2.setText("原料名称");
        tv3.setText("盘点前总量");
        tv4.setText("盘点后总量");

        lvCheckQuery = (MyListView) view.findViewById(R.id.listView);
//        lvCheckQuery.addFooterView(new ViewStub(context));
        ivDelete = (ImageView) view.findViewById(R.id.ivDelete);

        refreshLayout = (TwinklingRefreshLayout) view.findViewById(R.id.refreshLayout);
        UniversalHelper.initRefresh(getActivity(),refreshLayout);
        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter(){
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                      下拉刷新默认显示第一页（10条）内容
                        page = 1;
                        getCheckStockQueryListFromNet(url);
                        refreshLayout.setEnableLoadmore(true);
                        refreshLayout.finishRefreshing();
                    }
                },400);
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            page++;
//                            当page等于总页数的时候，提示“加载完成”，不能继续上拉加载更多
                            if (page == total) {
                                String url = UniversalHelper.getTokenUrl(UrlHelper.URL_CHECK_QUERY.replace("{page}", String.valueOf(page)));
                                Log.d("urlfinish", url);
                                getCheckStockQueryListFromNet(url);
                                Toast.makeText(getActivity(), "加载完成", Toast.LENGTH_SHORT).show();
                                // 结束上拉刷新...
                                refreshLayout.finishLoadmore();
                                return;
                            }
                            String url = UniversalHelper.getTokenUrl(UrlHelper.URL_CHECK_QUERY.replace("{page}", String.valueOf(page)));
                            Log.d("urlmore", url);
                            getCheckStockQueryListFromNet(url);
                            Toast.makeText(getActivity(), "已加载更多", Toast.LENGTH_SHORT).show();
                            // 结束上拉刷新...
                            refreshLayout.finishLoadmore();
                        }
                    },400);
            }
        });

        llSearch = (LinearLayout) view.findViewById(R.id.llSearch);
        etSearchInput = (EditText) view.findViewById(R.id.etSearchInput);
        etSearchInput.setHint("原料编码查询");
        etSearchInput.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    String input = etSearchInput.getText().toString().trim();
                    if (input.equals("")) {
                        Toast.makeText(getActivity(), "请输入内容后再查询", Toast.LENGTH_SHORT).show();
                    } else {
                        String urlString = UrlHelper.URL_SEARCH_CHECK_QUERY.replace("{query.code}", input);
                        urlString = UniversalHelper.getTokenUrl(urlString);
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
                    getCheckStockQueryListFromNet(url);

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
                String urlString = UrlHelper.URL_SEARCH_CHECK_QUERY.replace("{query.code}", input);
                urlString = UniversalHelper.getTokenUrl(urlString);
                getCheckStockQueryListFromNet(urlString);
            }
        });
        getCheckStockQueryListFromNet(url);
        return view;
    }

    /*
* Fragment 从隐藏切换至显示，会调用onHiddenChanged(boolean hidden)方法
* */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
//        Fragment重新显示到最前端中
        if (!hidden){
            page=1;
            getCheckStockQueryListFromNet(url);
        }
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
                        List<MaterialStockCheck> list = JSON.parseArray(response.getData().toString(), MaterialStockCheck.class);
                        count = response.getPage().getCount();
                        total = response.getPage().getTotal();
                        num = response.getPage().getNum();
                        //      数据小于10条或者当前页为最后一页就设置不能上拉加载更多
                        if (count <= 10 || num==total)
                            refreshLayout.setEnableLoadmore(false);
                        else
                            refreshLayout.setEnableLoadmore(true);
                        //  当前是第一页的时候，直接显示list内容；当显示更多页的时候，将后面页的list数据加到data中
                        if (num == 1) {
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
        private List<MaterialStockCheck> list = null;

        public BillAdapter(List<MaterialStockCheck> list, Context context) {
            this.list = list;
            this.context = context;
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
                TableRow tableRow = (TableRow) convertView.findViewById(R.id.table_row);
//                偶数行背景设为灰色
                if (position % 2 == 0)
                    tableRow.setBackgroundColor(getResources().getColor(R.color.color_light_grey));
                holder.tvMaterialCode = (TextView) convertView.findViewById(R.id.column1);
                holder.tvMaterialName = (TextView) convertView.findViewById(R.id.column2);
                holder.tvPreNum = (TextView) convertView.findViewById(R.id.column3);
                holder.tvAfterNum = (TextView) convertView.findViewById(R.id.column4);
                convertView.setTag(holder);
            } else
                holder = (ViewHolder) convertView.getTag();
//            Log.d("GGGG", DateFormat.getDateInstance().format(list.get(position).getArrivalDate()));
            holder.tvMaterialCode.setText(list.get(position).getMaterial().getCode());
            holder.tvMaterialName.setText(list.get(position).getMaterial().getName());
            holder.tvPreNum.setText(list.get(position).getBeforeStock() + list.get(position).getMaterial().getUnit().getName());
            holder.tvAfterNum.setText(list.get(position).getAfterStock() + list.get(position).getMaterial().getUnit().getName());

            holder.tvMaterialCode.setTextColor(getResources().getColor(R.color.colorBase));
            holder.tvMaterialCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MaterialCheckStockDetailActivity.class);
                    intent.putExtra("ID", list.get(position).getId());
//                    将原料是扫码还是输入数量入库的类型传递给盘点详情页面，扫码传递0，反之传递1
                    if (list.get(position).getMaterial().getStockType() == StockTypeVo.scan.getKey())
                        intent.putExtra("type", 0);
                    else
                        intent.putExtra("type", 1);
                    context.startActivity(intent);
                }
            });
            return convertView;
        }

        class ViewHolder {
            public TextView tvMaterialCode;
            public TextView tvMaterialName;
            public TextView tvPreNum;
            public TextView tvAfterNum;
        }

    }
}
