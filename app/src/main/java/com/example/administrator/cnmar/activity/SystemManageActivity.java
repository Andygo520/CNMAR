package com.example.administrator.cnmar.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.administrator.cnmar.AppExit;
import com.example.administrator.cnmar.R;
import com.example.administrator.cnmar.fragment.SystemLogFragment;
import com.example.administrator.cnmar.fragment.SystemUserFragment;
import com.example.administrator.cnmar.helper.SPHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SystemManageActivity extends AppCompatActivity {
    private Context context = SystemManageActivity.this;
    @BindView(R.id.left_arrow)
    LinearLayout leftArrow;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.rb1)
    RadioButton rbUser;
    @BindView(R.id.rb2)
    RadioButton rbLog;
    @BindView(R.id.rg)
    RadioGroup rg;
    private SystemLogFragment systemLogFragment;
    private SystemUserFragment systemUserFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_manage);
        ButterKnife.bind(this);
        AppExit.getInstance().addActivity(this);
//       取出登录界面获得的一级菜单与其二级菜单的对应关系
        String sublist = SPHelper.getString(this, getResources().getString(R.string.HOME_XTGL), "");
//      根据sublist的内容来设置默认选中的单选按钮(默认不可见)
        if (sublist.contains(","+getResources().getString(R.string.system_user_url)+",")) {
            rbUser.setVisibility(View.VISIBLE);
            setTabSelection(0);
        }
        if (sublist.contains(","+getResources().getString(R.string.system_log_url)+",")) {
            rbLog.setVisibility(View.VISIBLE);
            if (!sublist.contains(","+getResources().getString(R.string.system_user_url)+","))
                setTabSelection(1);
        }

        rbUser.setText(R.string.rbUser);
        rbLog.setText(R.string.rbLog);

        title.setText("系统管理");
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb1:
                        setTabSelection(0);
                        break;
                    case R.id.rb2:
                        setTabSelection(1);
                        break;
                }
            }
        });

    }

    @OnClick({R.id.left_arrow})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_arrow:
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (systemLogFragment == null && fragment instanceof SystemLogFragment) {
            systemLogFragment = (SystemLogFragment) fragment;
        } else if (systemUserFragment == null && fragment instanceof SystemUserFragment) {
            systemUserFragment = (SystemUserFragment) fragment;
        }
    }

    /**
     * 根据传入的index参数来设置选中的tab页。
     *
     * @param index 每个tab页对应的下标。0表示用户管理，1表示操作日志
     */
    private void setTabSelection(int index) {
        // 开启一个Fragment事务
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragment(transaction);
        switch (index) {
            case 0:
                rbUser.setChecked(true);
                rbUser.setBackgroundColor(getResources().getColor(R.color.colorBase));
                rbLog.setBackgroundColor(getResources().getColor(R.color.color_white));
                //                动态设置单选按钮文本上下左右的图片
                Drawable drawable1 = getResources().getDrawable(R.drawable.user_manage_selected);
                Drawable drawable2 = getResources().getDrawable(R.drawable.log);
                drawable1.setBounds(0, 15, 70, 85);//第一0是距左边距离，第二15是距上边距离，长宽为70
                drawable2.setBounds(0, 15, 70, 85);//第一0是距左边距离，第二15是距上边距离，长宽为70
                rbUser.setCompoundDrawables(null,drawable1,null,null);//只放上边
                rbLog.setCompoundDrawables(null,drawable2,null,null);//只放上边
                if (systemUserFragment == null) {
                    systemUserFragment = new SystemUserFragment();
                    transaction.add(R.id.content, systemUserFragment);
                } else {
                    transaction.show(systemUserFragment);
                }
                break;
            case 1:
                rbLog.setChecked(true);
                rbUser.setBackgroundColor(getResources().getColor(R.color.color_white));
                rbLog.setBackgroundColor(getResources().getColor(R.color.colorBase));
                //                动态设置单选按钮文本上下左右的图片
                Drawable drawable3 = getResources().getDrawable(R.drawable.user_manage);
                Drawable drawable4 = getResources().getDrawable(R.drawable.log_selected);
                drawable3.setBounds(0, 15, 70, 85);//第一0是距左边距离，第二15是距上边距离，长宽为70
                drawable4.setBounds(0, 15, 70, 85);//第一0是距左边距离，第二15是距上边距离，长宽为70
                rbUser.setCompoundDrawables(null,drawable3,null,null);//只放上边
                rbLog.setCompoundDrawables(null,drawable4,null,null);//只放上边
                if (systemLogFragment == null) {
                    systemLogFragment = new SystemLogFragment();
                    transaction.add(R.id.content, systemLogFragment);
                } else {
                    transaction.show(systemLogFragment);
                }
                break;
        }
        transaction.commit();
    }

    /**
     * 将所有的Fragment都置为隐藏状态。
     *
     * @param transaction 用于对Fragment执行操作的事务
     */
    private void hideFragment(FragmentTransaction transaction) {
        if (systemLogFragment != null) {
            transaction.hide(systemLogFragment);
        }
        if (systemUserFragment != null) {
            transaction.hide(systemUserFragment);
        }
    }
}
