package com.example.administrator.cnmar.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.cnmar.R;
import com.example.administrator.cnmar.activity.CompanyManageActivity;
import com.example.administrator.cnmar.activity.HalfProductStockActivity;
import com.example.administrator.cnmar.activity.LoginActivity;
import com.example.administrator.cnmar.activity.MaterialStockActivity;
import com.example.administrator.cnmar.activity.PlanManageActivity;
import com.example.administrator.cnmar.activity.ProductStockActivity;
import com.example.administrator.cnmar.activity.QualityControlActivity;
import com.example.administrator.cnmar.activity.SupplyManageActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private GridView gvMenu;
    private Map<String, Integer> map = new HashMap<>();

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        gvMenu = (GridView) view.findViewById(R.id.gvMenu);
        map.put(getResources().getString(R.string.HOME_YLCK), R.mipmap.ylck);
        map.put(getResources().getString(R.string.HOME_CPCK), R.mipmap.cpck);
        map.put(getResources().getString(R.string.HOME_JHGL), R.mipmap.jhgl);
        map.put(getResources().getString(R.string.HOME_QYGL), R.mipmap.qygl);
        map.put(getResources().getString(R.string.HOME_XGFGL), R.mipmap.xggl);
        map.put(getResources().getString(R.string.HOME_XTGL), R.mipmap.xtgl);
        map.put(getResources().getString(R.string.HOME_PKGL), R.mipmap.pkgl);
        map.put(getResources().getString(R.string.HOME_BBGL), R.mipmap.bbgl);
        map.put(getResources().getString(R.string.HOME_BCPCK), R.mipmap.bcpgl);

        Log.d("LOG","123455");
        String roleMenu = LoginActivity.sp.getString("Menu", getResources().getString(R.string.MENUS));
        Log.d("Log",roleMenu);

        String[] menuText = roleMenu.split(",");
        List<Integer> images = new ArrayList<>();
        for (int i = 0; i < menuText.length; i++) {
            images.add(map.get(menuText[i]));
        }
        Integer[] menuImage = images.toArray(new Integer[images.size()]);
        ImageAdapter adapter = new ImageAdapter(getActivity(), menuImage, menuText);
        gvMenu.setAdapter(adapter);

        return view;
    }

    public class ImageAdapter extends BaseAdapter {
        private Context context;
        private Integer[] images;
        private String[] texts;

        public ImageAdapter(Context context, Integer[] images, String[] texts) {
            this.context = context;
            this.images = images;
            this.texts = texts;
        }
        //        private String[] texts = {
//                StringHelper.HOME_YLCK,
//                StringHelper.HOME_CPCK,
//                StringHelper.HOME_JHGL,
//                StringHelper.HOME_QYGL,
//                StringHelper.HOME_XGFGL,
//                StringHelper.HOME_XTGL
//        };

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
                    switch (images[position]) {
                        case R.mipmap.ylck:
                            Intent intent = new Intent(getActivity(), MaterialStockActivity.class);
                            startActivity(intent);
                            break;
                        case R.mipmap.cpck:
                            Intent intent1 = new Intent(getActivity(), ProductStockActivity.class);
                            startActivity(intent1);
                            break;
                        case R.mipmap.jhgl:
                            Intent intent2 = new Intent(getActivity(), PlanManageActivity.class);
                            startActivity(intent2);
                            break;
                        case R.mipmap.qygl:
                            Intent intent3 = new Intent(getActivity(), CompanyManageActivity.class);
                            startActivity(intent3);
                            break;
                        case R.mipmap.xggl:
                            Intent intent4 = new Intent(getActivity(), SupplyManageActivity.class);
                            startActivity(intent4);
                            break;
                        case R.mipmap.xtgl:
                            break;
                        case R.mipmap.pkgl:
                            Intent intent6 = new Intent(getActivity(), QualityControlActivity.class);
                            startActivity(intent6);
                            break;
                        case R.mipmap.bbgl:
                            break;
                        case R.mipmap.bcpgl:
                            Intent intent8 = new Intent(getActivity(), HalfProductStockActivity.class);
                            startActivity(intent8);
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
