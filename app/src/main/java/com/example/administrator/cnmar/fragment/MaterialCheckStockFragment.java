package com.example.administrator.cnmar.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.administrator.cnmar.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * A simple {@link Fragment} subclass.
 */
public class MaterialCheckStockFragment extends Fragment {
    private RadioButton rbManage, rbQuery;
    private RadioGroup radioGroup;
    private MaterialCheckStockFragmentManage fragmentManage;
    private MaterialCheckStockFragmentQuery fragmentQuery;
    private FragmentTransaction transaction;

    public MaterialCheckStockFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_check_stock, container, false);
        radioGroup = (RadioGroup) view.findViewById(R.id.rg);
        rbManage = (RadioButton) view.findViewById(R.id.rbCheckManage);
        rbQuery = (RadioButton) view.findViewById(R.id.rbCheckQuery);
//        默认选中盘点管理
        setSelection(1);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbCheckManage:
                        setSelection(1);
                        break;
                    case R.id.rbCheckQuery:
                        setSelection(2);
                        break;

                }
            }
        });

        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            EventBus.getDefault().postSticky("HiddenChanged");
        }
    }

    //    i等于1表示选择了库存管理，i等于2表示选择了库存查询
    public void setSelection(int i) {
//      fragment嵌套里面不能再用getFragmentManager(),要用getChildFragmentManager()
        transaction = getChildFragmentManager().beginTransaction();
        switch (i) {
            case 1:
                rbManage.setChecked(true);
                if (fragmentQuery != null) {
                    transaction.hide(fragmentQuery);
                }
                if (fragmentManage == null) {
                    fragmentManage = new MaterialCheckStockFragmentManage();
                    transaction.add(R.id.content, fragmentManage);
                } else
                    transaction.show(fragmentManage);
                break;
            case 2:
                rbQuery.setChecked(true);
                if (fragmentManage != null) {
                    transaction.hide(fragmentManage);
                }
                if (fragmentQuery == null) {
                    fragmentQuery = new MaterialCheckStockFragmentQuery();
                    transaction.add(R.id.content, fragmentQuery);
                } else
                    transaction.show(fragmentQuery);
                break;
            default:
                break;

        }
        transaction.commit();
    }
}
