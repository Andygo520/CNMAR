package com.example.administrator.cnmar.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.example.administrator.cnmar.LoginActivity;
import com.example.administrator.cnmar.R;
import com.example.administrator.cnmar.entity.UserInfor;
import com.example.administrator.cnmar.http.VolleyHelper;

import component.system.model.SystemUser;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    private TextView tvAccount;
    private TextView tvName;
    private TextView tvSex;
    private TextView tvBirthday;
    private NetworkImageView ivImage;
    private Button btnLogOff;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_profile, container, false);
        tvAccount= (TextView) view.findViewById(R.id.account);
        tvName= (TextView) view.findViewById(R.id.username);
        tvSex= (TextView) view.findViewById(R.id.sex);
        tvBirthday= (TextView) view.findViewById(R.id.birthday);
        ivImage= (NetworkImageView) view.findViewById(R.id.image);
        btnLogOff= (Button) view.findViewById(R.id.logoff);

        btnLogOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                LoginActivity.editor.putBoolean("isChecked",false);
//               注销账号的时候，只保留账号内容
                LoginActivity.editor.putString("password","");
                LoginActivity.editor.putBoolean("isChecked",true).commit();
                Intent intent=new Intent(getActivity(),LoginActivity.class);
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

    public void getProfileFromNet(){
        RequestQueue queue= Volley.newRequestQueue(getActivity());

        StringRequest stringRequest=new StringRequest(LoginActivity.strUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                String json=VolleyHelper.getJson(s);
                UserInfor userInfor= JSON.parseObject(json,UserInfor.class);
                component.common.model.Response response=JSON.parseObject(json,component.common.model.Response.class);
                SystemUser model = JSON.parseObject(response.getData().toString(), SystemUser.class);

                tvAccount.setText(userInfor.getData().getUsername());
                tvName.setText(userInfor.getData().getName());
                tvSex.setText(model.getGenderVo().getValue());
                if(userInfor.getData().getBirthday()!=null){
                    tvBirthday.setText(userInfor.getData().getBirthday().toString());
                }else
                    tvBirthday.setText("未设置");
                if(userInfor.getData().getImgId()>0){
                    String path="http://139.196.104.170:8090"+"/"+model.getImg().getPath();
                    VolleyHelper.showImageByUrl(getActivity(),path,ivImage);
//                    HttpURLConnection con= null;
//                    try {
//                        con = (HttpURLConnection)new URL(path).openConnection();
//                        InputStream in=con.getInputStream();
//                        Bitmap bitmap= BitmapFactory.decodeStream(in);
////                      ivImage.setImageBitmap(bitmap);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }

//                    Log.d("tag",path);
//                    Uri uri=Uri.parse(path);
//                    ivImage.setImageURI(uri);
//                    try {
//                        InputStream is=new URL(path).openStream();
//                        Bitmap bitmap=BitmapFactory.decodeStream(is);
//                        ivImage.setImageBitmap(bitmap);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });

        queue.add(stringRequest);
    }

}
