package com.example.administrator.cnmar.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.cnmar.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class OutHouseBillFragment extends Fragment {


    public OutHouseBillFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_out_house_bill, container, false);
    }

}
