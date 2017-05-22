package com.example.administrator.cnmar.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.cnmar.R;
import com.example.administrator.cnmar.activity.CompanyManageActivity;
import com.example.administrator.cnmar.activity.HalfProductStockActivity;
import com.example.administrator.cnmar.activity.MaterialStockActivity;
import com.example.administrator.cnmar.activity.PlanManageActivity;
import com.example.administrator.cnmar.activity.ProduceManageActivity;
import com.example.administrator.cnmar.activity.ProductStockActivity;
import com.example.administrator.cnmar.activity.QualityControlActivity;
import com.example.administrator.cnmar.activity.ReportManageActivity;
import com.example.administrator.cnmar.activity.SupplyManageActivity;
import com.example.administrator.cnmar.activity.SystemManageActivity;
import com.example.administrator.cnmar.entity.Item;
import com.example.administrator.cnmar.entity.MyItemTouchCallback;
import com.example.administrator.cnmar.entity.RecyclerAdapter;
import com.example.administrator.cnmar.helper.ACache;
import com.example.administrator.cnmar.helper.OnRecyclerItemClickListener;
import com.example.administrator.cnmar.helper.SPHelper;
import com.example.administrator.cnmar.helper.VibratorUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements MyItemTouchCallback.OnDragListener {
    private RecyclerView recyclerView;
    //    RecyclerView结合ItemTouchHelper类实现拖拽排序的功能（还可以实现滑动删除）
    private ItemTouchHelper itemTouchHelper;
    private List<Item> results = new ArrayList<Item>(); // 适配器的数据源
    private Map<String, Integer> map = new HashMap<>(); // 该map用来存放菜单名跟图片的对应关系
    private String username;  //登录用户名

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        map.put(getResources().getString(R.string.HOME_YLCK), R.mipmap.ylck);
        map.put(getResources().getString(R.string.HOME_CPCK), R.mipmap.cpck);
        map.put(getResources().getString(R.string.HOME_JHGL), R.mipmap.jhgl);
        map.put(getResources().getString(R.string.HOME_QYGL), R.mipmap.qygl);
        map.put(getResources().getString(R.string.HOME_XGFGL), R.mipmap.xggl);
        map.put(getResources().getString(R.string.HOME_XTGL), R.mipmap.xtgl);
        map.put(getResources().getString(R.string.HOME_PKGL), R.mipmap.pkgl);
        map.put(getResources().getString(R.string.HOME_BBGL), R.mipmap.bbgl);
        map.put(getResources().getString(R.string.HOME_BCPCK), R.mipmap.bcpgl);
        map.put(getResources().getString(R.string.HOME_SCGL), R.mipmap.scgl);


//        得到用户拥有的菜单名、姓名
        String roleMenu = SPHelper.getString(getActivity(), "Menu", "");
        username = SPHelper.getString(getActivity(), "username");

//        存放菜单名的列表
        List<String> titleList = new ArrayList<>();
        if (roleMenu.contains(",")) {
            String[] menuText = roleMenu.split(",");
            for (String title : menuText) {
                titleList.add(title);
            }
        } else if (!roleMenu.contains(",") && roleMenu.length() > 0) {
            titleList.add(roleMenu);
        }

//        存放菜单图片的列表
        List<Integer> imageList = new ArrayList<>();

        for (String keyTitle : titleList) {
            imageList.add(map.get(keyTitle));
        }

//        从缓存中取出数据
        ArrayList<Item> items = (ArrayList<Item>) ACache.get(getActivity()).getAsObject("items");
        String name = ACache.get(getActivity()).getAsString("name");
//      如果有缓存数据，并且没有切换账号，就直接用缓存数据
        if (items != null && name != null && name.equalsIgnoreCase(username)) {
            results.addAll(items);
        } else {
            if (titleList.size() > 0) {
                for (int i = 0; i < titleList.size(); i++) {
                    results.add(new Item(titleList.get(i), imageList.get(i)));
                }
            }
        }

//        设置recyclerView的布局方式
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        RecyclerAdapter adapter = new RecyclerAdapter(R.layout.item_grid, results);
        recyclerView.setAdapter(adapter);
//        给recyclerView添加网格
//        recyclerView.addItemDecoration(new DividerGridItemDecoration(getActivity()));
        itemTouchHelper = new ItemTouchHelper(new MyItemTouchCallback(adapter).setOnDragListener(this));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        recyclerView.addOnItemTouchListener(new OnRecyclerItemClickListener(recyclerView) {
            @Override
            public void onLongClick(RecyclerView.ViewHolder vh) {
                itemTouchHelper.startDrag(vh);
                VibratorUtil.Vibrate(getActivity(), 70);   //震动70ms
            }

            @Override
            public void onItemClick(RecyclerView.ViewHolder vh) {
                Item item = results.get(vh.getLayoutPosition());
                switch (item.getImg()) {
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
                        Intent intent5 = new Intent(getActivity(), SystemManageActivity.class);
                        startActivity(intent5);
                        break;
                    case R.mipmap.pkgl:
                        Intent intent6 = new Intent(getActivity(), QualityControlActivity.class);
                        startActivity(intent6);
                        break;
                    case R.mipmap.bbgl:
                        Intent intent7 = new Intent(getActivity(), ReportManageActivity.class);
                        startActivity(intent7);
                        break;
                    case R.mipmap.bcpgl:
                        Intent intent8 = new Intent(getActivity(), HalfProductStockActivity.class);
                        startActivity(intent8);
                        break;
                    case R.mipmap.scgl:
                        Intent intent9 = new Intent(getActivity(), ProduceManageActivity.class);
                        startActivity(intent9);
                        break;
                }
            }
        });
        return view;
    }

    @Override
    public void onFinishDrag() {
        //进行拖拽操作后将数据源和用户名（判断切换用户用）存入缓存
        ACache.get(getActivity()).put("items", (ArrayList<Item>) results);
        ACache.get(getActivity()).put("name", (String) username);
    }
}
