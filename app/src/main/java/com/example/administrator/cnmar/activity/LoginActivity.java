package com.example.administrator.cnmar.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.administrator.cnmar.R;
import com.example.administrator.cnmar.helper.NetworkHelper;
import com.example.administrator.cnmar.helper.UniversalHelper;
import com.example.administrator.cnmar.helper.UrlHelper;
import com.example.administrator.cnmar.http.VolleyHelper;

import java.util.List;

import component.system.model.SystemMenu;
import component.system.model.SystemRole;
import component.system.model.SystemUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    public static String strUrl;

    private Button mLoginButton;
    private EditText etUserName, etPassword;
    private CheckBox auto_login;


    public static SharedPreferences sp;
    public static SharedPreferences.Editor editor;
    //    private ProgressDialog dialog;
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            if (msg.what == 0x101) {
                iskill = false;
            }
            super.handleMessage(msg);
        }
    };
    private boolean iskill = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUserName = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        auto_login = (CheckBox) findViewById(R.id.chkPassword);

        sp = getSharedPreferences("UserInfo", MODE_PRIVATE);
        editor = sp.edit();
        mLoginButton = (Button) findViewById(R.id.btnLogin);
//
        etUserName.setText(sp.getString("username", ""));

//      实现自动登录
        if (sp.getBoolean("isChecked", false) && !sp.getString("password", "").equals("")) {
            etUserName.setText(sp.getString("username", ""));
            etPassword.setText(sp.getString("password", ""));
            auto_login.setChecked(true);
            onClick(mLoginButton);
        }
        mLoginButton.setOnClickListener(this);
//        dialog.cancel();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (iskill) {
                //退出
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                System.exit(0);
            } else {
                iskill = true;
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                handler.sendEmptyMessageDelayed(0x101, 2000); //延迟2秒发送消息
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
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


    @Override
    public void onClick(View v) {
//        dialog=ProgressDialog.show(this,"","加载中");
        final String strUserName = etUserName.getText().toString().trim();
        String strPassword = etPassword.getText().toString().trim();
        //       检查网络连接
        if (!NetworkHelper.isNetworkConnected(this)) {
            NetworkHelper.noNetworkToast(this);
            return;
        }
        if (strUserName.equals("") && strPassword.equals("")) {
            Toast.makeText(this, "请输入账号和密码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (strUserName.equals("")) {
            Toast.makeText(this, "请输入账号", Toast.LENGTH_SHORT).show();
            return;
        }
        if (strPassword.equals("")) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }

        strUrl = UniversalHelper.getTokenUrl(UrlHelper.URL_LOGIN.replace("{username}", strUserName).replace("{password}", strPassword));
//                 Log.d("TAG1", strUrl);

        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);

        StringRequest stringRequest = new StringRequest(strUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                component.common.model.Response response = JSON.parseObject(VolleyHelper.getJson(s), component.common.model.Response.class);

//                只有状态为true才能登陆系统
                if (response.isStatus()) {
                    SystemUser userInfor = JSON.parseObject(response.getData().toString(), SystemUser.class);
                    if (auto_login.isChecked()) {
                        editor.putString("username", etUserName.getText().toString().trim()).commit();
                        editor.putString("password", etPassword.getText().toString().trim()).commit();
                        editor.putBoolean("isChecked", true).commit();
                    } else
                        editor.putBoolean("isChecked", false).commit();

//                  判断是否是超级管理员，如果是就显示所有按钮
                    Boolean isSuper = userInfor.getIsSuper();
                    editor.putBoolean("isSuper", isSuper).commit();

//                  得到用户id,原料检验的时候需要提交该id
                    int id = userInfor.getId();
                    editor.putInt("userId", id).commit();

                    List<SystemMenu> menuList = userInfor.getMenus();
                    List<SystemRole> roles = userInfor.getRoles();

//                    用来存放用户的角色以及可以查看的菜单信息
                    String strMenus = "";
                    String strRoles = "";

//                  通过一层循环取出角色名
                    for (int i = 0; i < roles.size(); i++) {
                        strRoles += roles.get(i).getName();
                    }
                    editor.putString("Role", strRoles).commit();

//                  通过一层循环取出菜单名
                    for (int i = 0; i < menuList.size(); i++) {
                        strMenus += menuList.get(i).getName() + ",";
                    }
//                  通过二层循环取出菜单名与其子列表
                    for (int i = 0; i < menuList.size(); i++) {
                        List<SystemMenu> subList = menuList.get(i).getSubList();
                        String strSublist = "";
                        for (int b = 0; b < subList.size(); b++) {
                            strSublist += subList.get(b).getName();
                        }
//                       将一级菜单名与它对应的二级子列表存入sp中
                        editor.putString(menuList.get(i).getName(), strSublist).commit();
                    }

                    String[] menus = strMenus.split(",");
                    String roleMenu = "";
//                    得到所有菜单名字符串
                    String allMenus=getResources().getString(R.string.MENUS);

                    for (int j = 0; j < menus.length; j++) {
                        if (allMenus.contains(menus[j])) {
                            roleMenu += menus[j] + ",";
                        }
                    }
                    roleMenu = roleMenu.substring(0, roleMenu.length() - 1);
                    editor.putString("Menu", roleMenu).commit();
                    Log.d("LOG",roleMenu);

//                   登陆成功后，设置按钮不能再点击,避免多次点击按钮导致HomeFragment数据多次调用
                    mLoginButton.setEnabled(false);
                    mLoginButton.setClickable(false);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);

                } else {
                    Toast.makeText(LoginActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
                    etUserName.setText(strUserName);
                    etPassword.setText("");
                    auto_login.setChecked(true);
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
