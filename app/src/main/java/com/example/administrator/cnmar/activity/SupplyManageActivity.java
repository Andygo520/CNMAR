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
import com.example.administrator.cnmar.entity.MyListView;
import com.example.administrator.cnmar.helper.UniversalHelper;
import com.example.administrator.cnmar.helper.UrlHelper;
import com.example.administrator.cnmar.http.VolleyHelper;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import component.supply.model.Supply;

public class SupplyManageActivity extends AppCompatActivity {
    private Context context=SupplyManageActivity.this;
    private TextView tvTitle;
    private MyListView lvOutOrder;
    private LinearLayout llSearch,llReturn;
    private EditText etSearchInput;

    private ImageView ivDelete;
    private MaterialRefreshLayout materialRefreshLayout;
    private Handler handler = new Handler();
    private BillAdapter myAdapter;
    //    page代表显示的是第几页内容，从1开始
    int page = 1;
    //    用来存放从后台取出的数据列表，作为adapter的数据源
    private List<Supply> data = new ArrayList<>();
    private String url = UniversalHelper.getTokenUrl(UrlHelper.URL_SUPPLY_LIST.replace("{page}", String.valueOf(page)));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supply_manage);

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

        lvOutOrder = (MyListView) findViewById(R.id.lvOutOrder);
//        lvOutOrder.addFooterView(new ViewStub(getActivity()));
        ivDelete = (ImageView) findViewById(R.id.ivDelete);

        materialRefreshLayout = (MaterialRefreshLayout) findViewById(R.id.refresh);
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
                        getSupplyListFromNet(url);
                        materialRefreshLayout.finishRefresh();
                    }
                }, 400);
            }

            @Override
            public void onRefreshLoadMore(final MaterialRefreshLayout materialRefreshLayout) {
                if (page == 1 && myAdapter.getCount() < 10) {
                    materialRefreshLayout.setLoadMore(false);
                    materialRefreshLayout.finishRefreshLoadMore();

                } else {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            myAdapter = new BillAdapter();
                            page++;
                            String strUrl = UniversalHelper.getTokenUrl(UrlHelper.URL_SUPPLY_LIST.replace("{page}", String.valueOf(page)));
//                        Log.d("url2", strUrl);
                            getSupplyListFromNet(strUrl);
                            Toast.makeText(context, "已加载更多", Toast.LENGTH_SHORT).show();
                            // 结束上拉刷新...
                            materialRefreshLayout.finishRefreshLoadMore();
                        }
                    }, 400);
                }
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
                        myAdapter = null;
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
                    myAdapter = null;
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
                myAdapter = null;
                getSupplyListFromNet(urlString);
            }
        });
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
                        if (myAdapter == null) {
                            data = list;
                            myAdapter = new BillAdapter(data, context);
                            lvOutOrder.setAdapter(myAdapter);
                        } else {
                            data.addAll(list);
//                            myAdapter.notifyDataSetChanged();
                            myAdapter = new BillAdapter(data, context);
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
                holder.tvSupplyNo = (TextView) convertView.findViewById(R.id.column1);
                holder.tvSupplyName = (TextView) convertView.findViewById(R.id.column2);
                holder.tvPhone = (TextView) convertView.findViewById(R.id.column3);
                holder.detail = (TextView) convertView.findViewById(R.id.column4);
                convertView.setTag(holder);
            } else
                holder = (ViewHolder) convertView.getTag();
            
            holder.tvSupplyNo.setText(list.get(position).getCode());
            holder.tvSupplyName.setText(list.get(position).getName());
            holder.tvPhone.setText(list.get(position).getTel());

            holder.detail.setText("详情");
            holder.detail.setTextColor(getResources().getColor(R.color.colorBase));
            holder.detail.setOnClickListener(new View.OnClickListener() {
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
            public TextView tvSupplyNo;
            public TextView tvSupplyName;
            public TextView tvPhone;
            public TextView detail;
        }

    }
}
