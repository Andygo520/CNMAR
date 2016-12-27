package com.example.administrator.cnmar.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
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
import com.example.administrator.cnmar.AppExit;
import com.example.administrator.cnmar.R;
import com.example.administrator.cnmar.entity.MyListView;
import com.example.administrator.cnmar.helper.UniversalHelper;
import com.example.administrator.cnmar.helper.UrlHelper;
import com.example.administrator.cnmar.helper.VolleyHelper;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import component.supply.model.Supply;

public class SupplyManageActivity extends AppCompatActivity {
    //    表头4个字段
    private TextView tv1, tv2, tv3, tv4;
    private Context context=SupplyManageActivity.this;
    private TextView tvTitle;
    private MyListView listView;
    private LinearLayout llSearch,llReturn;
    private EditText etSearchInput;

    private ImageView ivDelete;
    private TwinklingRefreshLayout refreshLayout;
    private Handler handler = new Handler();
    private BillAdapter myAdapter;
    int page = 1;    //    page代表显示的是第几页内容，从1开始
    private int total; // 总页数
    private int num = 1; // 第几页
    private int count; // 数据总条数
    //    用来存放从后台取出的数据列表，作为adapter的数据源
    private List<Supply> data = new ArrayList<>();
    private String url = UniversalHelper.getTokenUrl(UrlHelper.URL_SUPPLY_LIST.replace("{page}", String.valueOf(page)));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supply_manage);
        AppExit.getInstance().addActivity(this);
        tv1= (TextView) findViewById(R.id.tv1);
        tv2= (TextView) findViewById(R.id.tv2);
        tv3= (TextView) findViewById(R.id.tv3);
        tv4= (TextView) findViewById(R.id.tv4);

        tv1.setText("供应商编码");
        tv2.setText("供应商名称");
        tv3.setText("电话");
        tv4.setText("联系人");

        tvTitle= (TextView) findViewById(R.id.title);
        tvTitle.setText("供应商管理");

        llReturn= (LinearLayout) findViewById(R.id.left_arrow);
        llReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,MainActivity.class);
                startActivity(intent);
            }
        });

        listView = (MyListView) findViewById(R.id.listView);
//        listView.addFooterView(new ViewStub(getActivity()));
        ivDelete = (ImageView) findViewById(R.id.ivDelete);

        refreshLayout = (TwinklingRefreshLayout) findViewById(R.id.refreshLayout);

        UniversalHelper.initRefresh(this,refreshLayout);
        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter(){
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                      下拉刷新默认显示第一页（10条）内容
                        page = 1;
                        getSupplyListFromNet(url);
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
//                          当page等于总页数的时候，提示“加载完成”，不能继续上拉加载更多
                            if (page==total){
                                String url = UniversalHelper.getTokenUrl(UrlHelper.URL_SUPPLY_LIST.replace("{page}", String.valueOf(page)));
                                getSupplyListFromNet(url);
                                Toast.makeText(context, "加载完成", Toast.LENGTH_SHORT).show();
                                // 结束上拉刷新...
                                refreshLayout.finishLoadmore();
                                return;
                            }
                            String url = UniversalHelper.getTokenUrl(UrlHelper.URL_SUPPLY_LIST.replace("{page}", String.valueOf(page)));
                            getSupplyListFromNet(url);
                            Toast.makeText(context, "已加载更多", Toast.LENGTH_SHORT).show();
                            // 结束上拉刷新...
                            refreshLayout.finishLoadmore();
                        }
                    },400);
            }
        });
        llSearch = (LinearLayout) findViewById(R.id.llSearch);
        etSearchInput = (EditText) findViewById(R.id.etSearchInput);
        etSearchInput.setHint("供应商名称查询");
        etSearchInput.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    String input = etSearchInput.getText().toString().trim();
                    try {
                        input=URLEncoder.encode(input, "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    if (input.equals("")) {
                        Toast.makeText(context, "请输入内容后再查询", Toast.LENGTH_SHORT).show();
                    } else {
                        String urlString = UniversalHelper.getTokenUrl(UrlHelper.URL_SEARCH_SUPPLY_LIST.replace("{query.code}", input));
                        getSupplyListFromNet(urlString);
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
                    getSupplyListFromNet(url);

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
                    Toast.makeText(context, "请输入内容后再查询", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    input=URLEncoder.encode(input, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                String urlString = UniversalHelper.getTokenUrl(UrlHelper.URL_SEARCH_SUPPLY_LIST.replace("{query.code}", input));
                getSupplyListFromNet(urlString);
            }
        });
        getSupplyListFromNet(url);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            Intent intent=new Intent(context,MainActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void getSupplyListFromNet(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue quene = Volley.newRequestQueue(context);
                StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        String json = VolleyHelper.getJson(s);
                        component.common.model.Response response = JSON.parseObject(json, component.common.model.Response.class);
                        List<Supply> list = JSON.parseArray(response.getData().toString(), Supply.class);
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
                            myAdapter = new BillAdapter(data, context);
                            listView.setAdapter(myAdapter);
                        } else {
                            data.addAll(list);
//                            myAdapter.notifyDataSetChanged();
                            myAdapter = new BillAdapter(data, context);
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
        private List<Supply> list = null;

        public BillAdapter(List<Supply> list, Context context) {
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
                TableRow tableRow = (TableRow) convertView.findViewById(R.id.table_row);
//                偶数行背景设为灰色
                if (position % 2 == 0)
                    tableRow.setBackgroundColor(getResources().getColor(R.color.color_light_grey));
                holder.column1 = (TextView) convertView.findViewById(R.id.column1);
                holder.column2 = (TextView) convertView.findViewById(R.id.column2);
                holder.column3 = (TextView) convertView.findViewById(R.id.column3);
                holder.column4 = (TextView) convertView.findViewById(R.id.column4);
                convertView.setTag(holder);
            } else
                holder = (ViewHolder) convertView.getTag();
            
            holder.column1.setText(list.get(position).getCode());
            holder.column2.setText(list.get(position).getName());
            holder.column3.setText(list.get(position).getTel());
            holder.column4.setText(list.get(position).getContact());
            holder.column1.setTextColor(getResources().getColor(R.color.colorBase));
            holder.column1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, SupplyManageDetailActivity.class);
                    intent.putExtra("ID", list.get(position).getId());
                    context.startActivity(intent);
                }
            });
            return convertView;
        }

        class ViewHolder {
            public TextView column1;
            public TextView column2;
            public TextView column3;
            public TextView column4;
        }

    }
}
