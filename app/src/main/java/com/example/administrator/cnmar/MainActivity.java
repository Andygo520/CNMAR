package com.example.administrator.cnmar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.administrator.cnmar.fragment.HomeFragment;
import com.example.administrator.cnmar.fragment.ProfileFragment;

public class MainActivity extends AppCompatActivity {

    private TextView tvTitle;
    private ImageView ivLeftImage;
    private RadioGroup radioGroup;
    private RadioButton rbHome,rbMyProfile;
    private HomeFragment homeFragment;
    private ProfileFragment profileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvTitle= (TextView) findViewById(R.id.title);
        ivLeftImage= (ImageView) findViewById(R.id.left_img);
        radioGroup= (RadioGroup) findViewById(R.id.rg);
        rbHome= (RadioButton) findViewById(R.id.home);
        rbMyProfile= (RadioButton) findViewById(R.id.mProfile);


//      默认选中首页
        setTabSelection(0);



        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.home:
                        setTabSelection(0);
                        rbHome.setBackgroundColor(getResources().getColor(R.color.colorBase));
                        rbMyProfile.setBackgroundColor(getResources().getColor(R.color.color_white));
                        break;
                    case  R.id.mProfile:
                        setTabSelection(1);
                        rbHome.setBackgroundColor(getResources().getColor(R.color.color_white));
                        rbMyProfile.setBackgroundColor(getResources().getColor(R.color.colorBase));
                        break;
                    default:
                        break;
                }
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            Intent intent=new Intent(this,LoginActivity.class);
            startActivity(intent);
            finish();
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
                tvTitle.setText("洲马物联");
                ivLeftImage.setImageResource(R.mipmap.text_logo);
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
                tvTitle.setText("我的资料");
                ivLeftImage.setImageResource(R.mipmap.arrow_left_white);
                ivLeftImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setTabSelection(0);
                        rbHome.setChecked(true);
                    }
                });
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
