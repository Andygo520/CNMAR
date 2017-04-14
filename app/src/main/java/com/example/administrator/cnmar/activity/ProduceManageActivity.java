package com.example.administrator.cnmar.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.administrator.cnmar.AppExit;
import com.example.administrator.cnmar.R;
import com.example.administrator.cnmar.fragment.BinFragment;
import com.example.administrator.cnmar.fragment.CheckFlowFragment;
import com.example.administrator.cnmar.fragment.ProduceCheckFragment;
import com.example.administrator.cnmar.fragment.StationFragment;
import com.example.administrator.cnmar.helper.SPHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ProduceManageActivity extends AppCompatActivity {

    private StationFragment stationFragment;
    private BinFragment binFragment;
    private ProduceCheckFragment checkFragment;
    private CheckFlowFragment flowFragment;
    private Context context;

    @BindView(R.id.rb1)
    RadioButton rb1;
    @BindView(R.id.rb2)
    RadioButton rb2;
    @BindView(R.id.rb3)
    RadioButton rb3;
    @BindView(R.id.rb4)
    RadioButton rb4;
    @BindView(R.id.rg)
    RadioGroup rg;
    @BindView(R.id.left_arrow)
    LinearLayout leftArrow;
    @BindView(R.id.title)
    TextView title;

    //    判断从哪个页面跳转过来的标志
    private int flag = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produce_manage);
        ButterKnife.bind(this);
        AppExit.getInstance().addActivity(this);

        context = ProduceManageActivity.this;
        title.setText("生产管理");

        Boolean isSuper = SPHelper.getBoolean(context, "isSuper");
        Boolean isOperator = SPHelper.getBoolean(context, "isOperator");
        Boolean isTest = SPHelper.getBoolean(context, "isTest");
//操作工显示机台工位和线边仓，检验员显示线边仓，检验，检验流水
//isSuper为true都能看到
        if (isSuper || (isOperator && isTest)) {
            rb1.setVisibility(View.VISIBLE);
            rb2.setVisibility(View.VISIBLE);
            rb3.setVisibility(View.VISIBLE);
            rb4.setVisibility(View.VISIBLE);
            setTabSelection(0);
        } else {
            if (isOperator) {
                rb1.setVisibility(View.VISIBLE);
                rb2.setVisibility(View.VISIBLE);
                setTabSelection(0);
            } else if (isTest) {
                rb2.setVisibility(View.VISIBLE);
                rb3.setVisibility(View.VISIBLE);
                rb4.setVisibility(View.VISIBLE);
                setTabSelection(2);
            }
        }

        rb1.setText("机台工位");
        rb2.setText("线边仓");
        rb3.setText("检验");
        rb4.setText("检验流水");
//    得到从ProduceCheckFragment跳转来的Intent对象
        flag = getIntent().getIntExtra("SIGN", 999);
        Log.d("flagAcResult", flag + "");
        if (flag == 0)
            setTabSelection(3);//显示检验流水界面


        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb1:
                        setTabSelection(0);
                        break;
                    case R.id.rb2:
                        setTabSelection(1);
                        break;
                    case R.id.rb3:
                        setTabSelection(2);
                        break;
                    case R.id.rb4:
                        setTabSelection(3);
                        break;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(ProduceManageActivity.this, MainActivity.class));
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (stationFragment == null && fragment instanceof StationFragment) {
            stationFragment = (StationFragment) fragment;
        } else if (binFragment == null && fragment instanceof BinFragment) {
            binFragment = (BinFragment) fragment;
        } else if (checkFragment == null && fragment instanceof ProduceCheckFragment) {
            checkFragment = (ProduceCheckFragment) fragment;
        } else if (flowFragment == null && fragment instanceof CheckFlowFragment) {
            flowFragment = (CheckFlowFragment) fragment;
        }
    }


    /**
     * 根据传入的index参数来设置选中的tab页。
     *
     * @param index 每个tab页对应的下标。0表示生产计划（加工单），1表示子加工单，2表示领料单，3表示交付计划
     */
    private void setTabSelection(int index) {
        // 开启一个Fragment事务
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragment(transaction);
        switch (index) {
            case 0:
                rb1.setChecked(true);
                rb1.setBackgroundColor(getResources().getColor(R.color.colorBase));
                rb2.setBackgroundColor(getResources().getColor(R.color.color_white));
                rb3.setBackgroundColor(getResources().getColor(R.color.color_white));
                rb4.setBackgroundColor(getResources().getColor(R.color.color_white));
                //                动态设置单选按钮文本上下左右的图片
                Drawable drawable1 = getResources().getDrawable(R.drawable.production_plan_selected);
                Drawable drawable2 = getResources().getDrawable(R.drawable.produce_bom);
                Drawable drawable3 = getResources().getDrawable(R.drawable.receive_order);
                Drawable drawable4 = getResources().getDrawable(R.drawable.delivery_plan);
                drawable1.setBounds(0, 15, 70, 85);//第一0是距左边距离，第二15是距上边距离，长宽为70
                drawable2.setBounds(0, 15, 70, 85);//第一0是距左边距离，第二15是距上边距离，长宽为70
                drawable3.setBounds(0, 15, 70, 85);//第一0是距左边距离，第二15是距上边距离，长宽为70
                drawable4.setBounds(0, 15, 70, 85);//第一0是距左边距离，第二15是距上边距离，长宽为70
                rb1.setCompoundDrawables(null, drawable1, null, null);//只放上边
                rb2.setCompoundDrawables(null, drawable2, null, null);//只放上边
                rb3.setCompoundDrawables(null, drawable3, null, null);//只放上边
                rb4.setCompoundDrawables(null, drawable4, null, null);//只放上边

                if (stationFragment == null) {
                    stationFragment = new StationFragment();
                    transaction.add(R.id.content, stationFragment);
                } else {
                    transaction.show(stationFragment);
                }
                break;
            case 1:
                rb2.setChecked(true);
                rb2.setBackgroundColor(getResources().getColor(R.color.colorBase));
                rb1.setBackgroundColor(getResources().getColor(R.color.color_white));
                rb3.setBackgroundColor(getResources().getColor(R.color.color_white));
                rb4.setBackgroundColor(getResources().getColor(R.color.color_white));
                //                动态设置单选按钮文本上下左右的图片
                Drawable drawable5 = getResources().getDrawable(R.drawable.production_plan);
                Drawable drawable6 = getResources().getDrawable(R.drawable.produce_bom_selected);
                Drawable drawable7 = getResources().getDrawable(R.drawable.receive_order);
                Drawable drawable8 = getResources().getDrawable(R.drawable.delivery_plan);
                drawable5.setBounds(0, 15, 70, 85);//第一0是距左边距离，第二15是距上边距离，长宽为70
                drawable6.setBounds(0, 15, 70, 85);//第一0是距左边距离，第二15是距上边距离，长宽为70
                drawable7.setBounds(0, 15, 70, 85);//第一0是距左边距离，第二15是距上边距离，长宽为70
                drawable8.setBounds(0, 15, 70, 85);//第一0是距左边距离，第二15是距上边距离，长宽为70
                rb1.setCompoundDrawables(null, drawable5, null, null);//只放上边
                rb2.setCompoundDrawables(null, drawable6, null, null);//只放上边
                rb3.setCompoundDrawables(null, drawable7, null, null);//只放上边
                rb4.setCompoundDrawables(null, drawable8, null, null);//只放上边

                if (binFragment == null) {
                    binFragment = new BinFragment();
                    transaction.add(R.id.content, binFragment);
                } else {
                    transaction.show(binFragment);
                }
                break;
            case 2:
                rb3.setChecked(true);
                rb1.setBackgroundColor(getResources().getColor(R.color.color_white));
                rb2.setBackgroundColor(getResources().getColor(R.color.color_white));
                rb3.setBackgroundColor(getResources().getColor(R.color.colorBase));
                rb4.setBackgroundColor(getResources().getColor(R.color.color_white));
                //                动态设置单选按钮文本上下左右的图片
                Drawable drawable9 = getResources().getDrawable(R.drawable.production_plan);
                Drawable drawable10 = getResources().getDrawable(R.drawable.produce_bom);
                Drawable drawable11 = getResources().getDrawable(R.drawable.receive_order_selected);
                Drawable drawable12 = getResources().getDrawable(R.drawable.delivery_plan);
                drawable9.setBounds(0, 15, 70, 85);//第一0是距左边距离，第二15是距上边距离，长宽为70
                drawable10.setBounds(0, 15, 70, 85);//第一0是距左边距离，第二15是距上边距离，长宽为70
                drawable11.setBounds(0, 15, 70, 85);//第一0是距左边距离，第二15是距上边距离，长宽为70
                drawable12.setBounds(0, 15, 70, 85);//第一0是距左边距离，第二15是距上边距离，长宽为70
                rb1.setCompoundDrawables(null, drawable9, null, null);//只放上边
                rb2.setCompoundDrawables(null, drawable10, null, null);//只放上边
                rb3.setCompoundDrawables(null, drawable11, null, null);//只放上边
                rb4.setCompoundDrawables(null, drawable12, null, null);//只放上边
                if (checkFragment == null) {
                    checkFragment = new ProduceCheckFragment();
                    transaction.add(R.id.content, checkFragment);
                } else {
                    transaction.show(checkFragment);
                }
                break;
            case 3:
                rb4.setChecked(true);
                rb1.setBackgroundColor(getResources().getColor(R.color.color_white));
                rb2.setBackgroundColor(getResources().getColor(R.color.color_white));
                rb3.setBackgroundColor(getResources().getColor(R.color.color_white));
                rb4.setBackgroundColor(getResources().getColor(R.color.colorBase));
                //                动态设置单选按钮文本上下左右的图片
                Drawable drawable13 = getResources().getDrawable(R.drawable.production_plan);
                Drawable drawable14 = getResources().getDrawable(R.drawable.produce_bom);
                Drawable drawable15 = getResources().getDrawable(R.drawable.receive_order);
                Drawable drawable16 = getResources().getDrawable(R.drawable.delivery_plan_selected);
                drawable13.setBounds(0, 15, 70, 85);//第一0是距左边距离，第二15是距上边距离，长宽为70
                drawable14.setBounds(0, 15, 70, 85);//第一0是距左边距离，第二15是距上边距离，长宽为70
                drawable15.setBounds(0, 15, 70, 85);//第一0是距左边距离，第二15是距上边距离，长宽为70
                drawable16.setBounds(0, 15, 70, 85);//第一0是距左边距离，第二15是距上边距离，长宽为70
                rb1.setCompoundDrawables(null, drawable13, null, null);//只放上边
                rb2.setCompoundDrawables(null, drawable14, null, null);//只放上边
                rb3.setCompoundDrawables(null, drawable15, null, null);//只放上边
                rb4.setCompoundDrawables(null, drawable16, null, null);//只放上边
                if (flowFragment == null) {
                    flowFragment = new CheckFlowFragment();
                    transaction.add(R.id.content, flowFragment);
                } else {
                    transaction.show(flowFragment);
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
        if (stationFragment != null) {
            transaction.hide(stationFragment);
        }
        if (binFragment != null) {
            transaction.hide(binFragment);
        }
        if (checkFragment != null) {
            transaction.hide(checkFragment);
        }
        if (flowFragment != null) {
            transaction.hide(flowFragment);
        }

    }

    @OnClick(R.id.left_arrow)
    public void onClick() {
        startActivity(new Intent(ProduceManageActivity.this, MainActivity.class));
    }

}

