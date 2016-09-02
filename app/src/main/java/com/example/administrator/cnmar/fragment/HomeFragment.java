package com.example.administrator.cnmar.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.cnmar.MaterialWarehouseActivity;
import com.example.administrator.cnmar.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private GridView gvMenu;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        gvMenu = (GridView) view.findViewById(R.id.gvMenu);
        gvMenu.setAdapter(new ImageAdapter(getActivity()));

        return view;
    }

    public class ImageAdapter extends BaseAdapter {
        private Context context;

        public ImageAdapter(Context context) {
            this.context = context;
        }

        private Integer[] images = {
                R.mipmap.ylck,
                R.mipmap.cpck,
                R.mipmap.jhgl,
                R.mipmap.qygl,
                R.mipmap.xggl,
                R.mipmap.xtgl,

        };


        private String[] texts = {
                "原料仓库",
                "成品仓库",
                "计划管理",
                "企业管理",
                "相关管理",
                "系统管理"
        };

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(R.layout.home_item, null);
                holder.tvMenuItem = (TextView) convertView.findViewById(R.id.tvMenuItem);
                holder.ivMenuItem = (ImageView) convertView.findViewById(R.id.ivMenuItem);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.ivMenuItem.setImageResource(images[position]);
            holder.tvMenuItem.setText(texts[position]);
            gvMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch (images[position]){
                        case R.mipmap.ylck:
                            Intent intent=new Intent(getActivity(), MaterialWarehouseActivity.class);
                            startActivity(intent);
                            break;
                        case R.mipmap.cpck:
                            break;
                        case R.mipmap.jhgl:
                            break;
                        case R.mipmap.qygl:
                            break;
                        case R.mipmap.xggl:
                            break;
                        case R.mipmap.xtgl:
                            break;
                    }
                }
            });
            return convertView;
        }

        private class ViewHolder {
            TextView tvMenuItem;
            ImageView ivMenuItem;
        }
    }
}
