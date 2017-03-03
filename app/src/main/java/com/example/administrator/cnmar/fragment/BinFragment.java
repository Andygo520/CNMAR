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

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class BinFragment extends Fragment {
    private ValidBinFragment validBinFragment;
    private InvalidBinFragment invalidBinFragment;
    private FragmentTransaction transaction;
    @BindView(R.id.rbValid)
    RadioButton rbValid;
    @BindView(R.id.rbInvalid)
    RadioButton rbInvalid;
    @BindView(R.id.rg)
    RadioGroup rg;

    public BinFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bin, container, false);
        ButterKnife.bind(this, view);

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
    
//    i等于1表示选择了合格品，i等于2表示选择了不合格品
    public void setSelection(int i) {
//      fragment嵌套里面不能再用getFragmentManager(),要用getChildFragmentManager()
        transaction = getChildFragmentManager().beginTransaction();
        switch (i) {
            case 1:
                rbValid.setChecked(true);
                if (invalidBinFragment != null) {
                    transaction.hide(invalidBinFragment);
                }
                if (validBinFragment == null) {
                    validBinFragment = new ValidBinFragment();
                    transaction.add(R.id.content, validBinFragment);
                } else
                    transaction.show(validBinFragment);
                break;
            case 2:
                rbInvalid.setChecked(true);
                if (validBinFragment != null) {
                    transaction.hide(validBinFragment);
                }
                if (invalidBinFragment == null) {
                    invalidBinFragment = new InvalidBinFragment();
                    transaction.add(R.id.content, invalidBinFragment);
                } else
                    transaction.show(invalidBinFragment);
                break;
            default:
                break;

        }
        transaction.commit();
    }

}
