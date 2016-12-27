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
import com.example.administrator.cnmar.fragment.MaterialQualityControlFragment;
import com.example.administrator.cnmar.fragment.ProductQualityControlFragment;
import com.example.administrator.cnmar.fragment.TraceBackFragment;
import com.example.administrator.cnmar.helper.SPHelper;

public class QualityControlActivity extends AppCompatActivity {
    private TextView tvTitle;
    private RadioGroup radioGroup;
    private RadioButton rbQC, rbProductQC,rbTraceBack;
    private MaterialQualityControlFragment qcFragment;
    private ProductQualityControlFragment productQCFragment;
    private TraceBackFragment traceFragment;
    private LinearLayout llLeftArrow;
    //    判断从哪个页面跳转过来的标志
    private int flag = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quality_control);
        AppExit.getInstance().addActivity(this);
//       取出登录界面获得的一级菜单与其二级菜单的对应关系
        String sublist = SPHelper.getString(this,getResources().getString(R.string.HOME_PKGL), "");

        tvTitle = (TextView) findViewById(R.id.title);
        tvTitle.setText("品控管理");

        radioGroup = (RadioGroup) findViewById(R.id.rg);
        rbQC = (RadioButton) findViewById(R.id.rb1);
        rbProductQC = (RadioButton) findViewById(R.id.rb2);
        rbTraceBack = (RadioButton) findViewById(R.id.rb3);

        rbQC.setText(R.string.rbQC);
        rbProductQC.setText(R.string.rbProductQC);
        rbTraceBack.setText(R.string.rbTraceBack);


        llLeftArrow = (LinearLayout) findViewById(R.id.left_arrow);

        llLeftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QualityControlActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

//       根据sublist的内容来设置默认选中的单选按钮(默认不可见)
        if (sublist.contains(","+getResources().getString(R.string.material_in_order_test_url)+",")) {
            rbQC.setVisibility(View.VISIBLE);
            setTabSelection(0);
        }
        if (sublist.contains(","+getResources().getString(R.string.produce_product_test_url)+",")) {
            rbProductQC.setVisibility(View.VISIBLE);
            if (!sublist.contains(","+getResources().getString(R.string.material_in_order_test_url)+","))
            setTabSelection(1);
        }
        if (sublist.contains(","+getResources().getString(R.string.material_back_url)+",")) {
            rbTraceBack.setVisibility(View.VISIBLE);
            if (!sublist.contains(","+getResources().getString(R.string.material_in_order_test_url)+",")
                    && !sublist.contains(","+getResources().getString(R.string.produce_product_test_url)+","))
                setTabSelection(2);
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
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
        }else if (flag == 2) {
            setTabSelection(2);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
        if (qcFragment == null && fragment instanceof MaterialQualityControlFragment) {
            qcFragment = (MaterialQualityControlFragment) fragment;
        } else if (traceFragment == null && fragment instanceof TraceBackFragment) {
            traceFragment = (TraceBackFragment) fragment;
        }else if (productQCFragment == null && fragment instanceof ProductQualityControlFragment) {
            productQCFragment = (ProductQualityControlFragment) fragment;
        }
    }


    /**
     * 根据传入的index参数来设置选中的tab页。
     *
     * @param index 每个tab页对应的下标。0表示原料检验流水，1表示成品检验流水，2表示质量追溯
     */
    private void setTabSelection(int index) {
        // 开启一个Fragment事务
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragment(transaction);
        switch (index) {
            case 0:{
                rbQC.setChecked(true);
                rbQC.setBackgroundColor(getResources().getColor(R.color.colorBase));
                rbProductQC.setBackgroundColor(getResources().getColor(R.color.color_white));
                rbTraceBack.setBackgroundColor(getResources().getColor(R.color.color_white));
                //                动态设置单选按钮文本上下左右的图片
                Drawable drawable1 = getResources().getDrawable(R.drawable.material_qc_selected);
                Drawable drawable2 = getResources().getDrawable(R.drawable.product_qc);
                Drawable drawable3 = getResources().getDrawable(R.drawable.trace_back);
                drawable1.setBounds(0, 15, 70, 85);//第一0是距左边距离，第二15是距上边距离，长宽为70
                drawable2.setBounds(0, 15, 70, 85);//第一0是距左边距离，第二15是距上边距离，长宽为70
                drawable3.setBounds(0, 15, 70, 85);//第一0是距左边距离，第二15是距上边距离，长宽为70
                rbQC.setCompoundDrawables(null,drawable1,null,null);//只放上边
                rbProductQC.setCompoundDrawables(null,drawable2,null,null);//只放上边
                rbTraceBack.setCompoundDrawables(null,drawable3,null,null);//只放上边
                if (qcFragment == null) {
                    qcFragment = new MaterialQualityControlFragment();
                    transaction.add(R.id.content, qcFragment);
                } else {
                    transaction.show(qcFragment);
                }
                break;
            }
            case 1:{
                rbProductQC.setChecked(true);
                rbQC.setBackgroundColor(getResources().getColor(R.color.color_white));
                rbProductQC.setBackgroundColor(getResources().getColor(R.color.colorBase));
                rbTraceBack.setBackgroundColor(getResources().getColor(R.color.color_white));
                //                动态设置单选按钮文本上下左右的图片
                Drawable drawable1 = getResources().getDrawable(R.drawable.material_qc);
                Drawable drawable2 = getResources().getDrawable(R.drawable.product_qc_selected);
                Drawable drawable3 = getResources().getDrawable(R.drawable.trace_back);
                drawable1.setBounds(0, 15, 70, 85);//第一0是距左边距离，第二15是距上边距离，长宽为70
                drawable2.setBounds(0, 15, 70, 85);//第一0是距左边距离，第二15是距上边距离，长宽为70
                drawable3.setBounds(0, 15, 70, 85);//第一0是距左边距离，第二15是距上边距离，长宽为70
                rbQC.setCompoundDrawables(null,drawable1,null,null);//只放上边
                rbProductQC.setCompoundDrawables(null,drawable2,null,null);//只放上边
                rbTraceBack.setCompoundDrawables(null,drawable3,null,null);//只放上边
                if (productQCFragment == null) {
                    productQCFragment = new ProductQualityControlFragment();
                    transaction.add(R.id.content, productQCFragment);
                } else {
                    transaction.show(productQCFragment);
                }
                break;
            }
            case 2:{
                rbTraceBack.setChecked(true);
                rbQC.setBackgroundColor(getResources().getColor(R.color.color_white));
                rbProductQC.setBackgroundColor(getResources().getColor(R.color.color_white));
                rbTraceBack.setBackgroundColor(getResources().getColor(R.color.colorBase));
                //                动态设置单选按钮文本上下左右的图片
                Drawable drawable1 = getResources().getDrawable(R.drawable.material_qc);
                Drawable drawable2 = getResources().getDrawable(R.drawable.product_qc);
                Drawable drawable3 = getResources().getDrawable(R.drawable.trace_back_selected);
                drawable1.setBounds(0, 15, 70, 85);//第一0是距左边距离，第二15是距上边距离，长宽为70
                drawable2.setBounds(0, 15, 70, 85);//第一0是距左边距离，第二15是距上边距离，长宽为70
                drawable3.setBounds(0, 15, 70, 85);//第一0是距左边距离，第二15是距上边距离，长宽为70
                rbQC.setCompoundDrawables(null,drawable1,null,null);//只放上边
                rbProductQC.setCompoundDrawables(null,drawable2,null,null);//只放上边
                rbTraceBack.setCompoundDrawables(null,drawable3,null,null);//只放上边
                if (traceFragment == null) {
                    traceFragment = new TraceBackFragment();
                    transaction.add(R.id.content, traceFragment);
                } else {
                    transaction.show(traceFragment);
                }
                break;
            }

        }
        transaction.commit();
    }

    /**
     * 将所有的Fragment都置为隐藏状态。
     *
     * @param transaction 用于对Fragment执行操作的事务
     */
    private void hideFragment(FragmentTransaction transaction) {
        if (qcFragment != null) {
            transaction.hide(qcFragment);
        }
        if (productQCFragment != null) {
            transaction.hide(productQCFragment);
        }
        if (traceFragment != null) {
            transaction.hide(traceFragment);
        }
    }
}
