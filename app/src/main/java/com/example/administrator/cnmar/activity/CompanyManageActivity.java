package com.example.administrator.cnmar.activity;

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
import com.example.administrator.cnmar.fragment.CompanyInfoFragment;
import com.example.administrator.cnmar.fragment.ProductManageFragment;
import com.example.administrator.cnmar.helper.SPHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CompanyManageActivity extends AppCompatActivity {
    @BindView(R.id.left_arrow)
    LinearLayout llLeftArrow;
    @BindView(R.id.title)
    TextView tvTitle;
    @BindView(R.id.rb1)
    RadioButton rbCompanyInfo;
    @BindView(R.id.rb2)
    RadioButton rbProductManage;
    @BindView(R.id.rg)
    RadioGroup rg;
    private CompanyInfoFragment companyInfoFragment;
    private ProductManageFragment productManageFragment;
    //    判断从哪个页面跳转过来的标志
//    private int flag = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_manage);
        ButterKnife.bind(this);
        AppExit.getInstance().addActivity(this);
//       取出登录界面获得的一级菜单与其二级菜单的对应关系
        String sublist = SPHelper.getString(this, getResources().getString(R.string.HOME_QYGL), "");

        tvTitle.setText("企业管理");
        rbCompanyInfo.setText(R.string.rbCompanyInfo);
        rbProductManage.setText(R.string.rbProductManage);

//       根据sublist的内容来设置默认选中的单选按钮(默认不可见)
        if (sublist.contains(","+getResources().getString(R.string.company_url)+",")) {
            rbCompanyInfo.setVisibility(View.VISIBLE);
            setTabSelection(0);
        }
        if (sublist.contains(","+getResources().getString(R.string.company_product_url)+",")) {
            rbProductManage.setVisibility(View.VISIBLE);
            if (!sublist.contains(","+getResources().getString(R.string.company_url)+","))
                setTabSelection(1);
        }

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
                    default:
                        break;
                }
            }
        });

    }


//    @Override
//    protected void onResume() {
//        super.onResume();
//        flag = getIntent().getIntExtra("flag", 999);
//        if (flag == 0) {
//            setTabSelection(0);
//        } else if (flag == 1) {
//            setTabSelection(1);
//        }
//    }


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
//                动态设置单选按钮文本上下左右的图片
                Drawable drawable1 = getResources().getDrawable(R.drawable.company_selected);
                Drawable drawable2 = getResources().getDrawable(R.drawable.product);
                drawable1.setBounds(0, 15, 70, 85);//第一0是距左边距离，第二15是距上边距离，长宽为70
                drawable2.setBounds(0, 15, 70, 85);//第一0是距左边距离，第二15是距上边距离，长宽为70
                rbCompanyInfo.setCompoundDrawables(null,drawable1,null,null);//只放上边
                rbProductManage.setCompoundDrawables(null,drawable2,null,null);//只放上边
                if (companyInfoFragment == null) {
                    companyInfoFragment = new CompanyInfoFragment();
                    transaction.add(R.id.content, companyInfoFragment);
                } else {
                    transaction.show(companyInfoFragment);
                }
                break;
            case 1:
                rbProductManage.setChecked(true);
                rbCompanyInfo.setBackgroundColor(getResources().getColor(R.color.color_white));
                rbProductManage.setBackgroundColor(getResources().getColor(R.color.colorBase));
//                动态设置单选按钮文本上下左右的图片
                Drawable drawable3 = getResources().getDrawable(R.drawable.company);
                Drawable drawable4 = getResources().getDrawable(R.drawable.product_selected);
                drawable3.setBounds(0, 15, 70, 85);//第一0是距左边距离，第二15是距上边距离，长宽为70
                drawable4.setBounds(0, 15, 70, 85);//第一0是距左边距离，第二15是距上边距离，长宽为70
                rbCompanyInfo.setCompoundDrawables(null,drawable3,null,null);//只放上边
                rbProductManage.setCompoundDrawables(null,drawable4,null,null);//只放上边
                if (productManageFragment == null) {
                    productManageFragment = new ProductManageFragment();
                    transaction.add(R.id.content, productManageFragment);
                } else {
                    transaction.show(productManageFragment);
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
        if (companyInfoFragment != null) {
            transaction.hide(companyInfoFragment);
        }
        if (productManageFragment != null) {
            transaction.hide(productManageFragment);
        }
    }

    @OnClick(R.id.left_arrow)
    public void onClick() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
