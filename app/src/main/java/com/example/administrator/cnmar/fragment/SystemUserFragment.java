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
import com.example.administrator.cnmar.activity.SystemUserDetailActivity;
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

import component.system.model.SystemRole;
import component.system.model.SystemUser;

/**
 * A simple {@link Fragment} subclass.
 */
public class SystemUserFragment extends Fragment {
    //    表头4个字段
    private TextView tv1, tv2, tv3, tv4;
    int page = 1;    //    page代表显示的是第几页内容，从1开始
    private int total; // 总页数
    private int num = 1; // 第几页
    private int count; // 数据总条数
    private MyListView listView;
    private BillAdapter myAdapter;
    private LinearLayout llSearch;
    private EditText etSearchInput;
    private ImageView ivDelete;
    private TwinklingRefreshLayout refreshLayout;
    private Handler handler = new Handler();
    //    用来存放从后台取出的数据列表，作为adapter的数据源
    private List<SystemUser> data = new ArrayList<>();
    private String strUrl = UniversalHelper.getTokenUrl(UrlHelper.URL_SYSTEM_USER.replace("{page}", String.valueOf(page)));

    public SystemUserFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.refresh_frame, container, false);
        tv1= (TextView) view.findViewById(R.id.tv1);
        tv2= (TextView) view.findViewById(R.id.tv2);
        tv3= (TextView) view.findViewById(R.id.tv3);
        tv4= (TextView) view.findViewById(R.id.tv4);
        tv1.setText("账号");
        tv2.setText("姓名");
        tv3.setText("账号状态");
        tv4.setText("角色");

        listView = (MyListView) view.findViewById(R.id.listView);
//        listView.addFooterView(new ViewStub(getActivity()));
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
                        getUserListFromNet(strUrl);
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
                                String url = UniversalHelper.getTokenUrl(UrlHelper.URL_SYSTEM_USER.replace("{page}", String.valueOf(page)));
                                getUserListFromNet(url);
                                Toast.makeText(getActivity(), "加载完成", Toast.LENGTH_SHORT).show();
                                // 结束上拉刷新...
                                refreshLayout.finishLoadmore();
                                return;
                            }
                            String url = UniversalHelper.getTokenUrl(UrlHelper.URL_SYSTEM_USER.replace("{page}", String.valueOf(page)));
                            getUserListFromNet(url);
                            Toast.makeText(getActivity(), "已加载更多", Toast.LENGTH_SHORT).show();
                            // 结束上拉刷新...
                            refreshLayout.finishLoadmore();
                        }
                    },400);
            }
        });
        ivDelete = (ImageView) view.findViewById(R.id.ivDelete);
        llSearch = (LinearLayout) view.findViewById(R.id.llSearch);
        etSearchInput = (EditText) view.findViewById(R.id.etSearchInput);
        etSearchInput.setHint("姓名查询");
        etSearchInput.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    String input = etSearchInput.getText().toString().trim();
                    try {
                        input= URLEncoder.encode(input, "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    if (input.equals("")) {
                        Toast.makeText(getActivity(), "请输入内容后再查询", Toast.LENGTH_SHORT).show();
                    } else {
                        String urlString = UrlHelper.URL_SEARCH_SYSTEM_USER.replace("{query.code}", input);
                        urlString = UniversalHelper.getTokenUrl(urlString);
                        Log.d("Search", urlString);
                        getUserListFromNet(urlString);
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
                    getUserListFromNet(strUrl);
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
                try {
                    input=URLEncoder.encode(input, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                String urlString = UrlHelper.URL_SEARCH_SYSTEM_USER.replace("{query.code}", input);
                urlString = UniversalHelper.getTokenUrl(urlString);
                getUserListFromNet(urlString);
            }
        });
        getUserListFromNet(strUrl);
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
            getUserListFromNet(strUrl);
        }
    }

    public void getUserListFromNet(final String url) {
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
                        List<SystemUser> list = JSON.parseArray(response.getData().toString(), SystemUser.class);
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
        private List<SystemUser> list = null;

        public BillAdapter(List<SystemUser> list, Context context) {
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

            holder.column1.setText(list.get(position).getUsername());
            holder.column2.setText(list.get(position).getName());
            holder.column3.setText(list.get(position).getIsEnableVo().getValue());
//            获取用户的角色列表，要进行非空判断
            List<SystemRole> roleList=list.get(position).getRoles();
            if (roleList.size()==0){
                holder.column4.setText("");
            }else {
                String roles="";
                for (int i=0;i<roleList.size();i++){
                    roles+=roleList.get(i).getName()+",";
                }
                holder.column4.setText(roles.substring(0,roles.length()-1));
            }
            holder.column1.setTextColor(getResources().getColor(R.color.colorBase));
            holder.column1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, SystemUserDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("ID", list.get(position).getId());
                    intent.putExtras(bundle);
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
