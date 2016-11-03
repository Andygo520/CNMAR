package com.example.administrator.cnmar.activity;

import android.content.Context;
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
import com.example.administrator.cnmar.fragment.CompanyInfoFragment;
import com.example.administrator.cnmar.fragment.ProductManageFragment;

public class CompanyManageActivity extends AppCompatActivity {
    private Context context = CompanyManageActivity.this;
    private TextView tvTitle;
    private RadioGroup radioGroup;
    private RadioButton rbCompanyInfo, rbProductManage;
    private CompanyInfoFragment companyInfoFragment;
    private ProductManageFragment productManageFragment;
    private LinearLayout llLeftArrow;
    //    判断从哪个页面跳转过来的标志
    private int flag = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_manage);

//       取出登录界面获得的一级菜单与其二级菜单的对应关系
        String sublist = LoginActivity.sp.getString(getResources().getString(R.string.HOME_QYGL), "企业信息，产品管理");


        tvTitle = (TextView) findViewById(R.id.title);
        tvTitle.setText("企业管理");

        radioGroup = (RadioGroup) findViewById(R.id.rg);
        rbCompanyInfo = (RadioButton) findViewById(R.id.companyInfo);
        rbProductManage = (RadioButton) findViewById(R.id.productManage);
        llLeftArrow = (LinearLayout) findViewById(R.id.left_arrow);

        llLeftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
            }
        });


//       根据sublist的内容来设置默认选中的单选按钮(默认不可见)
        if (sublist.contains("企业信息")) {
            rbCompanyInfo.setVisibility(View.VISIBLE);
            setTabSelection(0);
        }
        if (sublist.contains("产品管理")) {
            rbProductManage.setVisibility(View.VISIBLE);
            if (!sublist.contains("企业信息"))
                setTabSelection(1);
        }


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.companyInfo:
                        setTabSelection(0);

                        break;
                    case R.id.productManage:
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
        flag = getIntent().getIntExtra("flag", 999);
        if (flag == 0) {
            setTabSelection(0);
        } else if (flag == 1) {
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
        if (companyInfoFragment == null && fragment instanceof CompanyInfoFragment) {
            companyInfoFragment = (CompanyInfoFragment) fragment;
        } else if (productManageFragment == null && fragment instanceof ProductManageFragment) {
            productManageFragment = (ProductManageFragment) fragment;
        }
    }


    /**
     * 根据传入的index参数来设置选中的tab页。
     *
     * @param index 每个tab页对应的下标。0表示企业信息，1表示产品管理
     */
    private void setTabSelection(int index) {
        // 开启一个Fragment事务
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragment(transaction);
        switch (index) {
            case 0:
                rbCompanyInfo.setChecked(true);
                rbCompanyInfo.setBackgroundColor(getResources().getColor(R.color.colorBase));
                rbProductManage.setBackgroundColor(getResources().getColor(R.color.color_white));

                companyInfoFragment = null;
                companyInfoFragment = new CompanyInfoFragment();
                transaction.add(R.id.content, companyInfoFragment);
//                if (companyInfoFragment == null) {
//                    companyInfoFragment = new CompanyInfoFragment();
//                    transaction.add(R.id.content, companyInfoFragment);
//                } else {
//                    transaction.show(companyInfoFragment);
//                }
                break;
            case 1:
                rbProductManage.setChecked(true);
                rbCompanyInfo.setBackgroundColor(getResources().getColor(R.color.color_white));
                rbProductManage.setBackgroundColor(getResources().getColor(R.color.colorBase));

                productManageFragment = null;
                productManageFragment = new ProductManageFragment();
                transaction.add(R.id.content, productManageFragment);
//                if (productManageFragment == null) {
//                    productManageFragment = new ProductManageFragment();
//                    transaction.add(R.id.content, productManageFragment);
//                } else {
//                    transaction.show(productManageFragment);
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
        if (companyInfoFragment != null) {
            transaction.hide(companyInfoFragment);
        }
        if (productManageFragment != null) {
            transaction.hide(productManageFragment);
        }

    }

}
