package com.example.administrator.cnmar.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.administrator.cnmar.R;
import com.example.administrator.cnmar.activity.LoginActivity;
import com.example.administrator.cnmar.helper.SPHelper;
import com.example.administrator.cnmar.helper.UniversalHelper;
import com.example.administrator.cnmar.helper.UrlHelper;
import com.example.administrator.cnmar.helper.VolleyHelper;

import java.text.SimpleDateFormat;

import component.system.model.SystemUser;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    private TextView tvAccount;
    private TextView tvName;
    private TextView tvSex;
    private TextView tvBirthday;
    private TextView tvMobile;
    private TextView tvDuty;
    private TextView tvPosition;
    private TextView tvDepartment;

    private NetworkImageView ivImage;
    private Button btnLogOff;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        tvAccount = (TextView) view.findViewById(R.id.account);
        tvName = (TextView) view.findViewById(R.id.username);
        tvSex = (TextView) view.findViewById(R.id.sex);
        tvBirthday = (TextView) view.findViewById(R.id.birthday);
        tvMobile = (TextView) view.findViewById(R.id.mobile);
        tvPosition = (TextView) view.findViewById(R.id.position);
        tvDuty = (TextView) view.findViewById(R.id.duty);
        tvDepartment = (TextView) view.findViewById(R.id.department);

        ivImage = (NetworkImageView) view.findViewById(R.id.image);
        btnLogOff = (Button) view.findViewById(R.id.logoff);

        btnLogOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                LoginActivity.editor.putBoolean("isChecked",false);
//               注销账号的时候，只保留账号内容
                SPHelper.putString(getActivity(), "password", "");
                SPHelper.putBoolean(getActivity(), "isChecked", true);
                SPHelper.putBoolean(getActivity(), "flag", false);

//                注销账号的时候，清除用户数据
                SPHelper.putString(getActivity(),getResources().getString(R.string.HOME_XTGL),"");
                SPHelper.putString(getActivity(),getResources().getString(R.string.HOME_QYGL),"");
                SPHelper.putString(getActivity(),getResources().getString(R.string.HOME_XGFGL),"");
                SPHelper.putString(getActivity(),getResources().getString(R.string.HOME_YLCK),"");
                SPHelper.putString(getActivity(),getResources().getString(R.string.HOME_BCPCK),"");
                SPHelper.putString(getActivity(),getResources().getString(R.string.HOME_CPCK),"");
                SPHelper.putString(getActivity(),getResources().getString(R.string.HOME_JHGL),"");
                SPHelper.putString(getActivity(),getResources().getString(R.string.HOME_PKGL),"");
                SPHelper.putString(getActivity(),getResources().getString(R.string.HOME_BBGL),"");

                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                getProfileFromNet();
            }
        }).start();

        return view;
    }

    public void getProfileFromNet() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = UrlHelper.URL_PROFILE.replace("{ID}", SPHelper.getInt(getActivity(), "userId") + "");
        url = UniversalHelper.getTokenUrl(url);
        Log.d("profile", url);
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                component.common.model.Response response = JSON.parseObject(VolleyHelper.getJson(s), component.common.model.Response.class);
                SystemUser userInfor = JSON.parseObject(response.getData().toString(), SystemUser.class);
                tvAccount.setText(userInfor.getUsername());
//                tvName这个文本框用来显示用户姓名、角色
                StringBuilder sb = new StringBuilder();
                sb.append(userInfor.getName() + "\n")
                        .append(SPHelper.getString(getActivity(), "Role"));
                tvName.setText(sb.toString());

                tvSex.setText(userInfor.getGenderVo().getValue());
                if (userInfor.getBirthday() != null) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String birthday = sdf.format(userInfor.getBirthday());
                    tvBirthday.setText(birthday);
                } else
                    tvBirthday.setText("");

                if (userInfor.getDept() != null)
                    tvDepartment.setText(userInfor.getDept().getName());
                else
                    tvDepartment.setText("");

                if (userInfor.getJob() != null)
                    tvPosition.setText(userInfor.getJob().getName());
                else
                    tvPosition.setText("");

                if (userInfor.getDuty() != null)
                    tvDuty.setText(userInfor.getDuty().getName());
                else
                    tvDuty.setText("");

                if (userInfor.getPhone() != null)
                    tvMobile.setText(userInfor.getPhone());
                else
                    tvMobile.setText("");

                if (userInfor.getImgId() > 0) {
//                    获取图片的路径，路径=绝对路径+相对路径
                    String path = UrlHelper.URL_IMAGE + userInfor.getImg().getPath();
                    Log.d("juedui",path);
                    VolleyHelper.showImageByUrl(getActivity(), path, ivImage);
                } else
                    ivImage.setDefaultImageResId(R.mipmap.user_icon);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });

        queue.add(stringRequest);
    }

}
