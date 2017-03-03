package com.example.administrator.cnmar.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.administrator.cnmar.R;
import com.example.administrator.cnmar.entity.MessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class CheckFlowFragment extends Fragment {
    private PlanCheckFlowFragment planFragment;
    private BomCheckFlowFragment bomFragment;
    private FragmentTransaction transaction;
    @BindView(R.id.rbValid)
    RadioButton rbValid;
    @BindView(R.id.rbInvalid)
    RadioButton rbInvalid;
    @BindView(R.id.rg)
    RadioGroup rg;

    public CheckFlowFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bin, container, false);
        ButterKnife.bind(this, view);
        //      注册订阅
        EventBus.getDefault().register(this);

        rbValid.setText("加工单检验流水");
        rbInvalid.setText("子加工单检验流水");
        setSelection(1);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbValid:
                        setSelection(1);
                        break;
                    case R.id.rbInvalid:
                        setSelection(2);
                        break;
                }
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
       //      取消订阅
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void handleEvent(MessageEvent event){
        Log.d("handleEventhandle","handleEvent");
        if (event.getId()==0){
            setSelection(1);
        }else if (event.getId()==1){
            setSelection(2);
        }
    }

    //    i等于1表示选择了合格品，i等于2表示选择了不合格品
    public void setSelection(int i) {
//      fragment嵌套里面不能再用getFragmentManager(),要用getChildFragmentManager()
        transaction = getChildFragmentManager().beginTransaction();
        switch (i) {
            case 1:
                rbValid.setChecked(true);
                if (bomFragment != null) {
                    transaction.hide(bomFragment);
                }
                if (planFragment == null) {
                    planFragment = new PlanCheckFlowFragment();
                    transaction.add(R.id.content, planFragment);
                } else
                    transaction.show(planFragment);
                break;
            case 2:
                rbInvalid.setChecked(true);
                if (planFragment != null) {
                    transaction.hide(planFragment);
                }
                if (bomFragment == null) {
                    bomFragment = new BomCheckFlowFragment();
                    transaction.add(R.id.content, bomFragment);
                } else
                    transaction.show(bomFragment);
                break;
            default:
                break;

        }
        transaction.commit();
    }

}


