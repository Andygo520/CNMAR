package com.example.administrator.cnmar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.administrator.cnmar.entity.UserInfor;
import com.example.administrator.cnmar.http.VolleyHelper;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    public static final String LOGIN_URL = "http://139.196.104.170:8092/login_commit?username={username}&password={password}";
    public static String strUrl;

    private TextView tvTitle;
    private ImageView ivLeftImage;
    private Button mLoginButton;
    private EditText etUserName, etPassword;
    private CheckBox auto_login;
    public static SharedPreferences sp;
    public static SharedPreferences.Editor editor;
//    private ProgressDialog dialog;
//    private Handler handler=new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//          if(msg.what==0)
//              dialog.dismiss();
//        }
//    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        tvTitle= (TextView) findViewById(R.id.title);
        ivLeftImage= (ImageView) findViewById(R.id.left_img);
        etUserName = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        auto_login= (CheckBox) findViewById(R.id.chkPassword);

        tvTitle.setText("欢迎登陆");
        ivLeftImage.setImageResource(R.mipmap.text_logo);
        sp=getSharedPreferences("UserInfo",MODE_PRIVATE);
        editor=sp.edit();
        mLoginButton = (Button) findViewById(R.id.btnLogin);

        if(sp.getBoolean("isChecked",false)){
            etUserName.setText(sp.getString("username",""));
            etPassword.setText(sp.getString("password",""));
            auto_login.setChecked(true);
            onClick(mLoginButton);
        }
        mLoginButton.setOnClickListener(this);
//        dialog.cancel();
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        etUserName.setText(sharedHelper.getValueByKey("username"));
//        if(sharedHelper.getValueByKey("savepwd").equalsIgnoreCase("true")){
//            etPassword.setText(sharedHelper.getValueByKey("password"));
//            chkPassword.setChecked(true);
//        }else {
//            etPassword.setText("");
//            chkPassword.setChecked(false);
//        }
//    }

    public void login() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
//        dialog=ProgressDialog.show(this,"","加载中");
                String strUserName = etUserName.getText().toString().trim();
                String strPassword = etPassword.getText().toString().trim();
                strUrl = LOGIN_URL.replace("{username}", strUserName).replace("{password}", strPassword);
                RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);

                StringRequest stringRequest = new StringRequest(strUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
//                        Log.d("TAG", s);
                        UserInfor userInfor = JSON.parseObject(VolleyHelper.getJson(s), UserInfor.class);
                        if(userInfor.isStatus()){
                            if (auto_login.isChecked()){
                                editor.putString("username", etUserName.getText().toString().trim()).commit();
                                editor.putString("password", etPassword.getText().toString().trim()).commit();
                                editor.putBoolean("isChecked",true).commit();
                            }
                            else
                                editor.putBoolean("isChecked",false).commit();
                            login();
                        }else
                        {
                            Toast.makeText(LoginActivity.this, userInfor.getMsg(), Toast.LENGTH_SHORT).show();
                            etUserName.setText("");
                            etPassword.setText("");
                            auto_login.setChecked(false);
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.e("TAG", volleyError.getMessage(), volleyError);
                    }
                });
                requestQueue.add(stringRequest);
//              登录操作执行完后，发送取消进度对话框的消息
//                handler.sendEmptyMessage(0);
            }

}
