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
import android.widget.TableLayout;
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
import com.example.administrator.cnmar.activity.MaterialInOrderDetailActivity;
import com.example.administrator.cnmar.entity.MyListView;
import com.example.administrator.cnmar.helper.UniversalHelper;
import com.example.administrator.cnmar.helper.UrlHelper;
import com.example.administrator.cnmar.helper.VolleyHelper;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import component.material.model.MaterialInOrder;
import component.material.vo.InOrderStatusVo;

/**
 * A simple {@link Fragment} subclass.
 */
public class InOrderFragment extends Fragment {
    int page = 1;    //    page代表显示的是第几页内容，从1开始
    private int total; // 总页数
    private int num = 1; // 第几页
    private int count; // 数据总条数

    //    表头6个字段
    private TextView tv1, tv2, tv3, tv4;
    private MyListView lvInOrder;
    private BillAdapter myAdapter;
    private LinearLayout llSearch;
    private TableLayout tableLayout;
    private EditText etSearchInput;
    private ImageView ivDelete;   // 搜索框有输入之后删除内容的图片按钮
    private Spinner spinner;

    //    配合Spinner使用的分割线
    private TextView tvLine;
    //   数组用来存放所有入库单状态
    String[] status = {"所有状态", "待打印", "待入库", "已入库", "未全部入库", "待检验", "检验不合格"};

    //    map用来将入库单状态跟InOrderStatusVo里面的状态关联
    private Map<String, Object> map = new HashMap<>();

    private TwinklingRefreshLayout refreshLayout;
    //    用来存放从后台取出的数据列表，作为adapter的数据源
    private List<MaterialInOrder> data = new ArrayList<>();
    private String strUrl = UniversalHelper.getTokenUrl(UrlHelper.URL_IN_ORDER.replace("{page}", String.valueOf(page)));

    public InOrderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.refresh_frame, container, false);
        lvInOrder = (MyListView) view.findViewById(R.id.listView);
//      在map中存入状态与Vo的对应关系，“所有状态”存入空字符
        map.put(status[0], "");
        map.put(status[1], InOrderStatusVo.pre_print.getKey());
        map.put(status[2], InOrderStatusVo.pre_in_stock.getKey());
        map.put(status[3], InOrderStatusVo.in_stock.getKey());
        map.put(status[4], InOrderStatusVo.not_all.getKey());
        map.put(status[5], InOrderStatusVo.pre_test.getKey());
        map.put(status[6], InOrderStatusVo.test_fail.getKey());

        tv1 = (TextView) view.findViewById(R.id.tv1);
        tv2 = (TextView) view.findViewById(R.id.tv2);
        tv3 = (TextView) view.findViewById(R.id.tv3);
        tv4 = (TextView) view.findViewById(R.id.tv4);

        tv1.setText("入库单号");
        tv2.setText("供应商编码");
        tv3.setText("到货日期");
        tv4.setText("入库单状态");

        llSearch = (LinearLayout) view.findViewById(R.id.llSearch);
        ivDelete = (ImageView) view.findViewById(R.id.ivDelete);
        etSearchInput = (EditText) view.findViewById(R.id.etSearchInput);
        etSearchInput.setHint("入库单号查询");
        spinner = (Spinner) view.findViewById(R.id.spinner);
        spinner.setVisibility(View.VISIBLE);
        // 建立数据源
        String[] mItems = getResources().getStringArray(R.array.materialInOrderStatus);
        // 建立Adapter并且绑定数据源
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, mItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //绑定 Adapter到控件
        spinner.setAdapter(adapter);

//       只有在拥有垂直下拉列表的Fragment才显示这个分割线
        tvLine = (TextView) view.findViewById(R.id.seperator_layout);
        tvLine.setVisibility(View.VISIBLE);

//      默认情况下不激活setOnItemSelectedListener方法，只有选择的时候才调用该方法
        spinner.setSelection(0, false);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String urlString = UrlHelper.URL_SEARCH_IN_ORDER.replace("{query.code}", "").replace("{query.status}", String.valueOf(map.get(status[position])));
                urlString = UniversalHelper.getTokenUrl(urlString);
                Log.d("status", urlString);
                getInOrderListFromNet(urlString);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        refreshLayout = (TwinklingRefreshLayout) view.findViewById(R.id.refreshLayout);
        UniversalHelper.initRefresh(getActivity(), refreshLayout);
        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                      下拉刷新默认显示第一页（10条）内容
                        page = 1;
                        getInOrderListFromNet(strUrl);
                        refreshLayout.finishRefreshing();
                    }
                }, 400);
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page++;
//                            当page等于总页数的时候，提示“加载完成”
                        if (page == total) {
                            String url = UniversalHelper.getTokenUrl(UrlHelper.URL_IN_ORDER.replace("{page}", String.valueOf(page)));
                            getInOrderListFromNet(url);
                            Toast.makeText(getActivity(), "加载完成", Toast.LENGTH_SHORT).show();
                            // 结束上拉刷新...
                            refreshLayout.finishLoadmore();
                            return;
                        }
                        String url = UniversalHelper.getTokenUrl(UrlHelper.URL_IN_ORDER.replace("{page}", String.valueOf(page)));
                        getInOrderListFromNet(url);
                        Toast.makeText(getActivity(), "已加载更多", Toast.LENGTH_SHORT).show();
                        // 结束上拉刷新...
                        refreshLayout.finishLoadmore();
                    }
                }, 400);
            }
        });
        etSearchInput.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    String input = etSearchInput.getText().toString().trim();
                    if (input.equals("")) {
                        Toast.makeText(getActivity(), "请输入内容后再查询", Toast.LENGTH_SHORT).show();
                    } else {
                        String urlString = UrlHelper.URL_SEARCH_IN_ORDER.replace("{query.code}", input).replace("{query.status}", "");
                        urlString = UniversalHelper.getTokenUrl(urlString);
                        getInOrderListFromNet(urlString);
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
                    getInOrderListFromNet(strUrl);
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
                String urlString = UrlHelper.URL_SEARCH_IN_ORDER.replace("{query.code}", input).replace("{query.status}", "");
                urlString = UniversalHelper.getTokenUrl(urlString);

                getInOrderListFromNet(urlString);
            }
        });
        getInOrderListFromNet(strUrl);
        Log.d("countcount", count + "");

        return view;
    }

    /*
    * Fragment 从隐藏切换至显示，会调用onHiddenChanged(boolean hidden)方法
    * */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
//        Fragment重新显示到最前端中
        if (!hidden) {
            page = 1;
            getInOrderListFromNet(strUrl);
        }
    }

    public void getInOrderListFromNet(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue quene = Volley.newRequestQueue(getActivity());
                Log.d("Tag", "开始执行");
                StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        String json = VolleyHelper.getJson(s);
                        Log.d("GGGG", json);
                        component.common.model.Response response = JSON.parseObject(json, component.common.model.Response.class);
                        List<MaterialInOrder> list = JSON.parseArray(response.getData().toString(), MaterialInOrder.class);
                        int i = 0;
                        for (MaterialInOrder materialInOrder : list) {
//                         统计入库单状态为待打印、待检验、待入库的记录数
                            if (materialInOrder.getInOrderStatusVo().getKey() == 1 ||
                                    materialInOrder.getInOrderStatusVo().getKey() == 2 ||
                                    materialInOrder.getInOrderStatusVo().getKey() == 5)
                                i++;
                        }
                        count = response.getPage().getCount();
                        total = response.getPage().getTotal();
                        num = response.getPage().getNum();
                        //      数据小于10条或者当前页为最后一页就设置不能上拉加载更多
                        if (count <= 10 || num == total)
                            refreshLayout.setEnableLoadmore(false);
                        else
                            refreshLayout.setEnableLoadmore(true);
                        //  当前是第一页的时候，直接显示list内容；当显示更多页的时候，将后面页的list数据加到data中
                        if (num == 1) {
                            data = list;
                            myAdapter = new BillAdapter(data, getActivity());
                            lvInOrder.setAdapter(myAdapter);
                        } else {
                            data.addAll(list);
//                            myAdapter.notifyDataSetChanged();
                            myAdapter = new BillAdapter(data, getActivity());
                            lvInOrder.setAdapter(myAdapter);
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
        private List<MaterialInOrder> list = null;

        public BillAdapter(List<MaterialInOrder> list, Context context) {
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
                holder.tvInOrderNo = (TextView) convertView.findViewById(R.id.column1);
                holder.tvSupplyNo = (TextView) convertView.findViewById(R.id.column2);
                holder.tvArriveDate = (TextView) convertView.findViewById(R.id.column3);
                holder.tvInOrderStatus = (TextView) convertView.findViewById(R.id.column4);

                convertView.setTag(holder);
            } else
                holder = (ViewHolder) convertView.getTag();

            holder.tvInOrderNo.setText(list.get(position).getCode());
//            供应商编码非空判断
            if (list.get(position).getSupply() != null)
                holder.tvSupplyNo.setText(list.get(position).getSupply().getCode());
            else
                holder.tvSupplyNo.setText("");

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            holder.tvArriveDate.setText(sdf.format(list.get(position).getArrivalDate()));
            holder.tvInOrderStatus.setText(list.get(position).getInOrderStatusVo().getValue());
            holder.tvInOrderNo.setTextColor(getResources().getColor(R.color.colorBase));
            holder.tvInOrderNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MaterialInOrderDetailActivity.class);
                    intent.putExtra("ID", list.get(position).getId());
                    context.startActivity(intent);
                }
            });
            return convertView;
        }

        class ViewHolder {
            public TextView tvInOrderNo;
            public TextView tvSupplyNo;
            public TextView tvArriveDate;
            public TextView tvInOrderStatus;
        }

    }

}
