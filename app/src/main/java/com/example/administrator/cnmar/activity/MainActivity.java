package com.example.administrator.cnmar.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.administrator.cnmar.AppExit;
import com.example.administrator.cnmar.R;
import com.example.administrator.cnmar.fragment.HomeFragment;
import com.example.administrator.cnmar.fragment.ProfileFragment;

import cn.hugeterry.updatefun.UpdateFunGO;
import cn.hugeterry.updatefun.config.UpdateKey;

public class MainActivity extends AppCompatActivity {
    private RadioGroup radioGroup;
    private RadioButton rbHome,rbMyProfile;
    private HomeFragment homeFragment;
    private ProfileFragment profileFragment;
    private LinearLayout llLeftArrow;
    private long exitTime = 0;
    private Handler handler=new Handler(){

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            if(msg.what==0x101)
            {
                iskill=false;
            }
            super.handleMessage(msg);
        }
    };
    private boolean iskill=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppExit.getInstance().addActivity(this);

//     利用fir.im的第三方包实现软件更新
        UpdateKey.API_TOKEN = "de8532237f19e2b10cf1f85c8df09fff";
        UpdateKey.APP_ID = "580722daca87a82746000907";
        //下载方式:
        UpdateKey.DialogOrNotification=UpdateKey.WITH_DIALOG;//通过Dialog来进行下载
        //UpdateKey.DialogOrNotification=UpdateKey.WITH_NOTIFITION;通过通知栏来进行下载(默认)
        UpdateFunGO.init(this);


        radioGroup= (RadioGroup) findViewById(R.id.rg);
        rbHome= (RadioButton) findViewById(R.id.rb1);
        rbMyProfile= (RadioButton) findViewById(R.id.rb2);
        llLeftArrow= (LinearLayout) findViewById(R.id.left_arrow);

        rbHome.setVisibility(View.VISIBLE);
        rbMyProfile.setVisibility(View.VISIBLE);
        rbHome.setText(R.string.rbHome);
        rbMyProfile.setText(R.string.rbProfile);

//      默认选中首页
        setTabSelection(0);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb1:
                        setTabSelection(0);
                        break;
                    case  R.id.rb2:
                        setTabSelection(1);
                        break;
                    default:
                        break;
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        UpdateFunGO.onResume(this);
    }
    @Override
    protected void onStop() {
        super.onStop();
        UpdateFunGO.onStop(this);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if(homeFragment==null && fragment instanceof HomeFragment){
            homeFragment= (HomeFragment) fragment;
        }else if(profileFragment==null && fragment instanceof ProfileFragment){
            profileFragment= (ProfileFragment) fragment;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
            if(iskill)
            {
                //退出
                AppExit.getInstance().exit();
            }
            else
            {
                iskill=true;
                Toast.makeText(this,"再按一次退出程序",Toast.LENGTH_SHORT).show();
                handler.sendEmptyMessageDelayed(0x101,2000); //延迟2秒发送消息
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 根据传入的index参数来设置选中的tab页。
     *
     * @param index
     *            每个tab页对应的下标。0表示主页，1表示我的资料
     */
    private void setTabSelection(int index) {
        // 开启一个Fragment事务
       FragmentTransaction transaction =getSupportFragmentManager().beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragment(transaction);
        switch (index) {
            // 点击主页tab
            case 0:
                rbHome.setChecked(true);
                rbHome.setBackgroundColor(getResources().getColor(R.color.colorBase));
                rbMyProfile.setBackgroundColor(getResources().getColor(R.color.color_white));
//                动态设置单选按钮文本上下左右的图片（不需要的地方设置为0）
                Drawable drawable1 = getResources().getDrawable(R.drawable.home_selected);
                Drawable drawable2 = getResources().getDrawable(R.drawable.profile);
                drawable1.setBounds(0, 15, 70, 85);//第一0是距左边距离，第二0是距上边距离，40分别是长宽
                drawable2.setBounds(0, 15, 70, 85);//第一0是距左边距离，第二0是距上边距离，40分别是长宽
                rbHome.setCompoundDrawables(null,drawable1,null,null);//只放上边
                rbMyProfile.setCompoundDrawables(null,drawable2,null,null);//只放上边
//                rbMyProfile.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.profile,0,0);
                if (homeFragment == null) {
                    // 如果HomeFragment为空，则创建一个并添加到界面上
                    homeFragment = new HomeFragment();
                    transaction.add(R.id.content, homeFragment);
                } else {
                    // 如果HomeFragment不为空，则直接将它显示出来
                    transaction.show(homeFragment);
                }
                break;
            // 点击我的资料tab
            case 1:
                rbMyProfile.setChecked(true);
                rbHome.setBackgroundColor(getResources().getColor(R.color.color_white));
                rbMyProfile.setBackgroundColor(getResources().getColor(R.color.colorBase));
//                动态设置单选按钮文本上下左右的图片（不需要的地方设置为0）
                Drawable drawable3 = getResources().getDrawable(R.drawable.home);
                Drawable drawable4 = getResources().getDrawable(R.drawable.profile_selected);
                drawable3.setBounds(0, 15, 70, 85);//第一0是距左边距离，第二0是距上边距离，40分别是长宽
                drawable4.setBounds(0, 15, 70, 85);//第一0是距左边距离，第二0是距上边距离，40分别是长宽
                rbHome.setCompoundDrawables(null,drawable3,null,null);//只放上边
                rbMyProfile.setCompoundDrawables(null,drawable4,null,null);//只放上边
                if (profileFragment == null) {
                    // 如果profileFragment为空，则创建一个并添加到界面上
                    profileFragment = new ProfileFragment();
                    transaction.add(R.id.content, profileFragment);
                } else {
                    // 如果friendFragment不为空，则直接将它显示出来
                    transaction.show(profileFragment);
                }
                break;
        }
        transaction.commit();
    }
    /**
     * 将所有的Fragment都置为隐藏状态。
     *
     * @param transaction
     *            用于对Fragment执行操作的事务
     */
    private void hideFragment(FragmentTransaction transaction) {
        if (homeFragment != null) {
            transaction.hide(homeFragment);
        }
        if (profileFragment != null) {
            transaction.hide(profileFragment);
        }
    }

}
