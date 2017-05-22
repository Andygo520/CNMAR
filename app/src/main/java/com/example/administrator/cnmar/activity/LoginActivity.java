package com.example.administrator.cnmar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
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
import com.example.administrator.cnmar.AppExit;
import com.example.administrator.cnmar.R;
import com.example.administrator.cnmar.helper.NetworkHelper;
import com.example.administrator.cnmar.helper.SPHelper;
import com.example.administrator.cnmar.helper.UniversalHelper;
import com.example.administrator.cnmar.helper.UrlHelper;
import com.example.administrator.cnmar.helper.VolleyHelper;

import java.util.List;

import component.system.model.SystemMenu;
import component.system.model.SystemRole;
import component.system.model.SystemUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private String strUrl;
    private Button mLoginButton;
    private EditText etUserName, etPassword;
    private CheckBox auto_login;
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
    private boolean flag = false;//引导页跳转的标志位

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        AppExit.getInstance().addActivity(this);

        etUserName = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        auto_login = (CheckBox) findViewById(R.id.chkPassword);
        mLoginButton = (Button) findViewById(R.id.btnLogin);

        etUserName.setText(SPHelper.getString(this, "username", ""));

//      实现自动登录
        if (SPHelper.getBoolean(this, "isChecked", false) && !SPHelper.getString(this, "password", "").equals("")) {
            etUserName.setText(SPHelper.getString(this, "username", ""));
            etPassword.setText(SPHelper.getString(this, "password", ""));
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
                AppExit.getInstance().exit();
            } else {
                iskill = true;
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                handler.sendEmptyMessageDelayed(0x101, 2000); //延迟2秒发送消息
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
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

        UniversalHelper.showProgressDialog(this);
        strUrl = UniversalHelper.getTokenUrl(UrlHelper.URL_LOGIN.replace("{username}", strUserName).replace("{password}", strPassword));

        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);

        StringRequest stringRequest = new StringRequest(strUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                UniversalHelper.dismissProgressDialog();
                component.common.model.Response response = JSON.parseObject(VolleyHelper.getJson(s), component.common.model.Response.class);
//                只有状态为true才能登陆系统
                if (response.isStatus()) {
                    SystemUser userInfor = JSON.parseObject(response.getData().toString(), SystemUser.class);
                    if (auto_login.isChecked()) {
                        SPHelper.putString(LoginActivity.this, "username", etUserName.getText().toString().trim());
                        SPHelper.putString(LoginActivity.this, "password", etPassword.getText().toString().trim());
                        SPHelper.putBoolean(LoginActivity.this, "isChecked", true);
//                 登陆成功并且勾选了自动登录就给标志位赋值为true,下次登陆的时候直接从引导页跳转到主页面
                        flag = true;
                    } else{
                        SPHelper.putBoolean(LoginActivity.this, "isChecked", false);
                    }
//                  保存flag的值
                    SPHelper.putBoolean(LoginActivity.this, "flag", flag);

//                  判断是否是超级管理员，如果是就显示所有按钮
                    Boolean isSuper = userInfor.getIsSuper();
                    SPHelper.putBoolean(LoginActivity.this, "isSuper", isSuper);

//                 操作工跟测试员的判断，这与“生产管理”显示的模块有关
                    Boolean isOperator = userInfor.getIsOperator();
                    Boolean isTest = userInfor.getIsTest();
                    SPHelper.putBoolean(LoginActivity.this, "isOperator", isOperator);
                    SPHelper.putBoolean(LoginActivity.this, "isTest", isTest);

//                  得到用户id,原料检验的时候需要提交该id,并且在我的资料Fragment也会用到
                    int id = userInfor.getId();
                    SPHelper.putInt(LoginActivity.this, "userId", id);
                    SPHelper.putString(LoginActivity.this, "name", userInfor.getName());//将登录用户名存入sp


                    List<SystemMenu> menuList = userInfor.getMenus();
                    List<SystemRole> roles = userInfor.getRoles();

//                    用来存放用户的角色以及可以查看的菜单信息
                    String strMenus = "";
                    String strRoles = ",";
                    String roleName = "";

//                  通过一层循环取出角色名
                    if (roles != null && roles.size() > 0) {
                        for (SystemRole role : roles) {
                            strRoles += role.getId() + ",";
                            roleName += role.getName() + ",";
                        }
                        SPHelper.putString(LoginActivity.this, "RoleId", strRoles);
                        SPHelper.putString(LoginActivity.this, "Role", roleName.substring(0, roleName.length() - 1));
                    }

                    String roleMenu = "";
//                  通过二层循环取出菜单与其子列表
                    if (menuList != null && menuList.size() > 0) {
                        for (SystemMenu firstMenu : menuList) {
                            strMenus += firstMenu.getName() + ",";// 取出一级菜单名
                            List<SystemMenu> subList = firstMenu.getSubList();
                            String strSublist = ","; //存放二级菜单的所有url字段
                            String thirdSublist = ",";  //存放三级菜单的所有url字段
                            for (SystemMenu secondMenu : subList) {
                                //  如果存在三级菜单，就取出二级菜单名赋值给strMenus，将三级菜单的url赋值给thirdSublist
                                //   否则，将二级菜单的url赋值给strSublist
                                if (secondMenu.getSubList() != null) {
                                    List<SystemMenu> threeList = secondMenu.getSubList();
                                    strMenus += secondMenu.getName() + ",";
                                    for (SystemMenu thirdMenu : threeList) {
                                        thirdSublist += thirdMenu.getUrl() + ",";
                                    }
//                       将二级菜单与它对应的三级子列表存入sp中（二级菜单的name作为key，其包含的三级列表的url作为data）
                                    SPHelper.putString(LoginActivity.this, secondMenu.getName(), thirdSublist);
                                } else {
                                    strSublist += secondMenu.getUrl() + ",";
                                }
                            }
//                       将一级菜单与它对应的二级子列表存入sp中（一级菜单的name作为key，其包含的二级列表的url作为data）
//                       在二级列表strSublist前后加上“，”，防止之后使用contain方法出现类似"a,ab".contain("b")的错误
//                       这样在出现类似问题的时候可以用",a,ab,".contain(",b,")方法规避该问题
                            SPHelper.putString(LoginActivity.this, firstMenu.getName(), strSublist);
                        }
                        String[] menus = strMenus.split(",");
//                    得到app所有菜单对应的名称字段
                        String allMenus = LoginActivity.this.getResources().getString(R.string.MENUS);

                        for (int j = 0; j < menus.length; j++) {
                            if (allMenus.contains(menus[j])) {
                                roleMenu += menus[j] + ",";
                            }
                        }
                        if (roleMenu.length() > 0)
                            roleMenu = roleMenu.substring(0, roleMenu.length() - 1);
                    }
                    //  让操作工跟测试员、超级用户可以显示生产管理模块
                    if (isOperator || isTest || isSuper)
                        roleMenu = roleMenu + "," + getResources().getString(R.string.HOME_SCGL);
                    if (roleMenu.startsWith(","))
                        roleMenu = roleMenu.substring(1);
//                   将用户拥有的app模块菜单名存入sp中
                    SPHelper.putString(LoginActivity.this, "Menu", roleMenu);


//                   登陆成功后，设置按钮不能再点击
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
                UniversalHelper.dismissProgressDialog();
                Toast.makeText(LoginActivity.this, "连接超时", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);
    }

}
