package com.example.administrator.cnmar.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.example.administrator.cnmar.activity.MaterialStockDetailActivity;
import com.example.administrator.cnmar.entity.MyListView;
import com.example.administrator.cnmar.helper.UniversalHelper;
import com.example.administrator.cnmar.helper.UrlHelper;
import com.example.administrator.cnmar.helper.VolleyHelper;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import component.basic.vo.PackTypeVo;
import component.material.model.MaterialStock;

/**
 * A simple {@link Fragment} subclass.
 */
public class MaterialStockFragment extends Fragment {
    //    表头4个字段
    private TextView tv1, tv2, tv3, tv4;
    private MyListView lvStock;
    private LinearLayout llSearch;
    private EditText etSearchInput;
    private ImageView ivDelete;
    private TwinklingRefreshLayout refreshLayout;
    private StockAdapter myAdapter;
    int page = 1;    //    page代表显示的是第几页内容，从1开始
    private int total; // 总页数
    private int num = 1; // 第几页
    private int count; // 数据总条数
    //    用来存放从后台取出的数据列表，作为adapter的数据源
    private List<MaterialStock> data = new ArrayList<>();
    private String url = UniversalHelper.getTokenUrl(UrlHelper.URL_STOCK.replace("{page}", String.valueOf(page)));

    public MaterialStockFragment() {
        // Required empty public constructor
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
        tv3.setText("单位");
        tv4.setText("库存总量");

        lvStock = (MyListView) view.findViewById(R.id.listView);
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
                        getStockListFromNet(url);
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
                            if (page == total) {
                                String strUrl = UniversalHelper.getTokenUrl(UrlHelper.URL_STOCK.replace("{page}", String.valueOf(page)));
                                getStockListFromNet(strUrl);
                                Toast.makeText(getActivity(), "加载完成", Toast.LENGTH_SHORT).show();
                                // 结束上拉刷新...
                                refreshLayout.finishLoadmore();
                                return;
                            }
                            String strUrl = UniversalHelper.getTokenUrl(UrlHelper.URL_STOCK.replace("{page}", String.valueOf(page)));
                            getStockListFromNet(strUrl);
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
                        String urlString = UrlHelper.URL_SEARCH_STOCK.replace("{query.code}", input);
                        urlString = UniversalHelper.getTokenUrl(urlString);
                        getStockListFromNet(urlString);
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
                    getStockListFromNet(url);
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
                String urlString = UrlHelper.URL_SEARCH_STOCK.replace("{query.code}", input);
                urlString = UniversalHelper.getTokenUrl(urlString);
                getStockListFromNet(urlString);
            }
        });
        getStockListFromNet(url);
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
            getStockListFromNet(url);
        }
    }

    public void getStockListFromNet(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue quene = Volley.newRequestQueue(getActivity());
                StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        String json = VolleyHelper.getJson(s);
                        component.common.model.Response response = JSON.parseObject(json, component.common.model.Response.class);
                        List<MaterialStock> list = JSON.parseArray(response.getData().toString(), MaterialStock.class);

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
                            myAdapter = new StockAdapter(data, getActivity());
                            lvStock.setAdapter(myAdapter);

                        } else {
                            data.addAll(list);
//                            myAdapter.notifyDataSetChanged();
                            myAdapter = new StockAdapter(data, getActivity());
                            lvStock.setAdapter(myAdapter);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getActivity(), "连接超时", Toast.LENGTH_LONG).show();
                    }
                });
                quene.add(stringRequest);
            }
        }).start();
    }

    class StockAdapter extends BaseAdapter {
        private Context context;
        private List<MaterialStock> list = null;

        public StockAdapter(List<MaterialStock> list, Context context) {
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
                holder.materialCode = (TextView) convertView.findViewById(R.id.column1);
                holder.materialName = (TextView) convertView.findViewById(R.id.column2);
                holder.unit = (TextView) convertView.findViewById(R.id.column3);
                holder.stockSum = (TextView) convertView.findViewById(R.id.column4);
                convertView.setTag(holder);
            } else
                holder = (ViewHolder) convertView.getTag();

            holder.materialCode.setText(list.get(position).getMaterial().getCode());
            holder.materialName.setText(list.get(position).getMaterial().getName());
//            有包装的单位格式为“10个/捆”，没包装的直接显示单位
            if (list.get(position).getMaterial().getPackTypeVo().getKey() != PackTypeVo.empty.getKey())
                holder.unit.setText(list.get(position).getMaterial().getPackNum()
                        + list.get(position).getMaterial().getUnit().getName()
                        + "/"
                        + list.get(position).getMaterial().getPackTypeVo().getValue().substring(1));
            else
                holder.unit.setText(list.get(position).getMaterial().getUnit().getName());

            holder.stockSum.setText(list.get(position).getStock() + list.get(position).getMaterial().getUnit().getName());

            holder.materialCode.setTextColor(getResources().getColor(R.color.colorBase));
            holder.materialCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MaterialStockDetailActivity.class);
//                        intent.putExtra("ID", list.get(position).getId());
                    Bundle bundle = new Bundle();
                    bundle.putInt("ID", list.get(position).getId());
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
//            }
            return convertView;
        }

        class ViewHolder {
            public TextView materialCode;
            public TextView materialName;
            public TextView unit;
            public TextView stockSum;
        }

    }


}
