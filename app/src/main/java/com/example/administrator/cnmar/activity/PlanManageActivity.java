package com.example.administrator.cnmar.activity;

import android.content.Intent;
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

import com.example.administrator.cnmar.R;
import com.example.administrator.cnmar.fragment.DeliveryPlanFragment;
import com.example.administrator.cnmar.fragment.ProductionPlanFragment;
import com.example.administrator.cnmar.fragment.ReceiveMaterialOrderFragment;

public class PlanManageActivity extends AppCompatActivity {

    private TextView tvTitle;
    private RadioGroup radioGroup;
    private RadioButton rbProductionPlan, rbReceiveMaterialOrder, rbDeliveryPlan;
    private ProductionPlanFragment productionPlanFragment;
    private ReceiveMaterialOrderFragment receiveMaterialOrderFragment;
    private DeliveryPlanFragment deliveryPlanFragment;
    private LinearLayout llLeftArrow;
    //    判断从哪个页面跳转过来的标志
    private int flag = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_manage);

//       取出登录界面获得的一级菜单与其二级菜单的对应关系
        String sublist = LoginActivity.sp.getString(getResources().getString(R.string.HOME_JHGL), "加工单，领料单，交付计划");


        tvTitle = (TextView) findViewById(R.id.title);
        tvTitle.setText("计划管理");

        radioGroup = (RadioGroup) findViewById(R.id.rg);
        rbProductionPlan = (RadioButton) findViewById(R.id.productionPlan);
        rbReceiveMaterialOrder = (RadioButton) findViewById(R.id.receiveMaterialOrder);
        rbDeliveryPlan = (RadioButton) findViewById(R.id.deliveryPlan);
        llLeftArrow = (LinearLayout) findViewById(R.id.left_arrow);

        llLeftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlanManageActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


//       根据sublist的内容来设置默认选中的单选按钮(默认不可见)
        if (sublist.contains("加工单")) {
            rbProductionPlan.setVisibility(View.VISIBLE);
            setTabSelection(0);
        }
        if (sublist.contains("领料单")) {
            rbReceiveMaterialOrder.setVisibility(View.VISIBLE);
            if (!sublist.contains("加工单"))
                setTabSelection(1);
        }
        if (sublist.contains("交付计划")) {
            rbDeliveryPlan.setVisibility(View.VISIBLE);
            if (!sublist.contains("加工单") && !sublist.contains("领料单"))
                setTabSelection(2);
        }


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.productionPlan:
                        setTabSelection(0);

                        break;
                    case R.id.receiveMaterialOrder:
                        setTabSelection(1);

                        break;
                    case R.id.deliveryPlan:
                        setTabSelection(2);

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
        flag = getIntent().getIntExtra("flag", 999);
        if (flag == 0) {
            setTabSelection(0);
        } else if (flag == 1) {
            setTabSelection(2);
        } else if (flag == 2) {
            setTabSelection(1);
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
        if (productionPlanFragment == null && fragment instanceof ProductionPlanFragment) {
            productionPlanFragment = (ProductionPlanFragment) fragment;
        } else if (deliveryPlanFragment == null && fragment instanceof DeliveryPlanFragment) {
            deliveryPlanFragment = (DeliveryPlanFragment) fragment;
        }
    }


    /**
     * 根据传入的index参数来设置选中的tab页。
     *
     * @param index 每个tab页对应的下标。0表示生产计划（加工单），1表示领料单，2表示交付计划
     */
    private void setTabSelection(int index) {
        // 开启一个Fragment事务
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragment(transaction);
        switch (index) {
            case 0:
                rbProductionPlan.setChecked(true);
                rbProductionPlan.setBackgroundColor(getResources().getColor(R.color.colorBase));
                rbReceiveMaterialOrder.setBackgroundColor(getResources().getColor(R.color.color_white));
                rbDeliveryPlan.setBackgroundColor(getResources().getColor(R.color.color_white));

                productionPlanFragment = null;
                productionPlanFragment = new ProductionPlanFragment();
                transaction.add(R.id.content, productionPlanFragment);
//                if (productionPlanFragment == null) {
//                    productionPlanFragment = new ProductionPlanFragment();
//                    transaction.add(R.id.content, productionPlanFragment);
//                } else {
//                    transaction.show(productionPlanFragment);
//                }
                break;
            case 1:
                rbReceiveMaterialOrder.setChecked(true);
                rbProductionPlan.setBackgroundColor(getResources().getColor(R.color.color_white));
                rbReceiveMaterialOrder.setBackgroundColor(getResources().getColor(R.color.colorBase));
                rbDeliveryPlan.setBackgroundColor(getResources().getColor(R.color.color_white));

                receiveMaterialOrderFragment = null;
                receiveMaterialOrderFragment = new ReceiveMaterialOrderFragment();
                transaction.add(R.id.content, receiveMaterialOrderFragment);
//                if (receiveMaterialOrderFragment == null) {
//                    receiveMaterialOrderFragment = new ReceiveMaterialOrderFragment();
//                    transaction.add(R.id.content, receiveMaterialOrderFragment);
//                } else {
//                    transaction.show(receiveMaterialOrderFragment);
//                }
                break;
            case 2:
                rbDeliveryPlan.setChecked(true);
                rbProductionPlan.setBackgroundColor(getResources().getColor(R.color.color_white));
                rbReceiveMaterialOrder.setBackgroundColor(getResources().getColor(R.color.color_white));
                rbDeliveryPlan.setBackgroundColor(getResources().getColor(R.color.colorBase));


                deliveryPlanFragment = null;
                deliveryPlanFragment = new DeliveryPlanFragment();
                transaction.add(R.id.content, deliveryPlanFragment);
//                if (deliveryPlanFragment == null) {
//                    deliveryPlanFragment = new DeliveryPlanFragment();
//                    transaction.add(R.id.content, deliveryPlanFragment);
//                } else {
//                    transaction.show(deliveryPlanFragment);
//                }
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
        if (productionPlanFragment != null) {
            transaction.hide(productionPlanFragment);
        }
        if (receiveMaterialOrderFragment != null) {
            transaction.hide(receiveMaterialOrderFragment);
        }
        if (deliveryPlanFragment != null) {
            transaction.hide(deliveryPlanFragment);
        }

    }

}
