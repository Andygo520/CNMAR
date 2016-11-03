package com.example.administrator.cnmar.activity;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.administrator.cnmar.R;
import com.example.administrator.cnmar.fragment.HalfProductCheckStockFragment;
import com.example.administrator.cnmar.fragment.HalfProductInOrderFragment;
import com.example.administrator.cnmar.fragment.HalfProductOutOrderFragment;
import com.example.administrator.cnmar.fragment.HalfProductStockFragment;

import com.example.administrator.cnmar.helper.UniversalHelper;

public class HalfProductStockActivity extends AppCompatActivity {
    private TextView tvTitle;
    private ImageView ivLeftArrow;
    private HalfProductStockFragment stockFragment;
    private HalfProductInOrderFragment inHouseBillFragment;
    private HalfProductOutOrderFragment outHouseBillFragment;
    private HalfProductCheckStockFragment checkStockFragment;
    private RadioGroup rg;
    private RadioButton rbStock, rbInOrder, rbOutOrder, rbCheckStock;
    private LinearLayout llLeftArrow;
    //    定义一个标志位，判断是哪个activity启动的本页面
    private int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_half_product_stock);

//       取出登录界面获得的一级菜单与其二级菜单的对应关系
        String sublist = LoginActivity.sp.getString(getResources().getString(R.string.HOME_BCPCK), "库存，入库单，出库单，盘库");

        tvTitle = (TextView) findViewById(R.id.title);
        rg = (RadioGroup) findViewById(R.id.rg);
        rbStock = (RadioButton) findViewById(R.id.stock);
        rbInOrder = (RadioButton) findViewById(R.id.inHouseBill);
        rbOutOrder = (RadioButton) findViewById(R.id.outHouseBill);
        rbCheckStock = (RadioButton) findViewById(R.id.checkStock);
        llLeftArrow = (LinearLayout) findViewById(R.id.left_arrow);

        ivLeftArrow = (ImageView) findViewById(R.id.left_img);
        UniversalHelper.backToLastActivity(this, llLeftArrow, new MainActivity());
        tvTitle.setText("半成品仓库");

//       根据sublist的内容来设置默认选中的单选按钮(默认不可见)
        if (sublist.contains("库存管理")) {
            rbStock.setVisibility(View.VISIBLE);
            setSelection(0);
        }
        if (sublist.contains("入库单管理")) {
            rbInOrder.setVisibility(View.VISIBLE);
            if (!sublist.contains("库存管理"))
                setSelection(1);
        }
        if (sublist.contains("出库单管理")) {
            rbOutOrder.setVisibility(View.VISIBLE);
            if (!sublist.contains("库存管理") && !sublist.contains("入库单管理"))
                setSelection(2);

        }
        if (sublist.contains("盘点")) {
            rbCheckStock.setVisibility(View.VISIBLE);
            if (!sublist.contains("库存管理") && !sublist.contains("入库单管理") && !sublist.contains("出库单管理"))
                setSelection(3);
        }

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.stock:
                        setSelection(0);
                        break;
                    case R.id.inHouseBill:
                        setSelection(1);
                        break;
                    case R.id.outHouseBill:
                        setSelection(2);
                        break;
                    case R.id.checkStock:
                        setSelection(3);
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
        flag = getIntent().getIntExtra("flag", 0);
        if (flag == 1) {
            setSelection(1);
        } else if (flag == 2) {
            setSelection(2);
        } else if (flag == 3) {
            setSelection(3);
        }
//        else if (flag==4){
//            setSelection(3);
//        }
    }

    @Override
    public void onAttachFragment(android.support.v4.app.Fragment fragment) {
        super.onAttachFragment(fragment);
        if (stockFragment == null && fragment instanceof HalfProductStockFragment) {
            stockFragment = (HalfProductStockFragment) fragment;
        } else if (inHouseBillFragment == null && fragment instanceof HalfProductInOrderFragment) {
            inHouseBillFragment = (HalfProductInOrderFragment) fragment;
        } else if (outHouseBillFragment == null && fragment instanceof HalfProductOutOrderFragment) {
            outHouseBillFragment = (HalfProductOutOrderFragment) fragment;
        } else if (checkStockFragment == null && fragment instanceof HalfProductCheckStockFragment) {
            checkStockFragment = (HalfProductCheckStockFragment) fragment;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /*
    *
    * 设置选中的Fragment,0代表库存,1代表入库单据,2代表出库单据,3代表盘库
    *
    * */
    public void setSelection(int id) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideFragment(transaction);

        switch (id) {
            case 0:
                rbStock.setChecked(true);
                rbStock.setBackgroundColor(getResources().getColor(R.color.colorBase));
                rbInOrder.setBackgroundColor(getResources().getColor(R.color.color_white));
                rbOutOrder.setBackgroundColor(getResources().getColor(R.color.color_white));
                rbCheckStock.setBackgroundColor(getResources().getColor(R.color.color_white));

                stockFragment = null;
                stockFragment = new HalfProductStockFragment();
                transaction.add(R.id.content, stockFragment);
//                if(stockFragment==null){
//                    stockFragment=new HalfProductStockFragment();
//                    transaction.add(R.id.content,stockFragment);
//                }else
//                    transaction.show(stockFragment);
                break;
            case 1:
                rbInOrder.setChecked(true);
                rbStock.setBackgroundColor(getResources().getColor(R.color.color_white));
                rbInOrder.setBackgroundColor(getResources().getColor(R.color.colorBase));
                rbOutOrder.setBackgroundColor(getResources().getColor(R.color.color_white));
                rbCheckStock.setBackgroundColor(getResources().getColor(R.color.color_white));

                inHouseBillFragment = null;
                inHouseBillFragment = new HalfProductInOrderFragment();
                transaction.add(R.id.content, inHouseBillFragment);
//                if(inHouseBillFragment==null){
//                    inHouseBillFragment=new HalfProductInOrderFragment();
//                    transaction.add(R.id.content,inHouseBillFragment);
//                }else
//                    transaction.show(inHouseBillFragment);
                break;
            case 2:
                rbOutOrder.setChecked(true);
                rbStock.setBackgroundColor(getResources().getColor(R.color.color_white));
                rbInOrder.setBackgroundColor(getResources().getColor(R.color.color_white));
                rbOutOrder.setBackgroundColor(getResources().getColor(R.color.colorBase));
                rbCheckStock.setBackgroundColor(getResources().getColor(R.color.color_white));

                outHouseBillFragment = null;
                outHouseBillFragment = new HalfProductOutOrderFragment();
                transaction.add(R.id.content, outHouseBillFragment);
//                if(outHouseBillFragment==null){
//                    outHouseBillFragment=new HalfProductOutOrderFragment();
//                    transaction.add(R.id.content,outHouseBillFragment);
//                }else
//                    transaction.show(outHouseBillFragment);
                break;
            case 3:
                rbCheckStock.setChecked(true);
                rbStock.setBackgroundColor(getResources().getColor(R.color.color_white));
                rbInOrder.setBackgroundColor(getResources().getColor(R.color.color_white));
                rbOutOrder.setBackgroundColor(getResources().getColor(R.color.color_white));
                rbCheckStock.setBackgroundColor(getResources().getColor(R.color.colorBase));

                checkStockFragment = null;
                checkStockFragment = new HalfProductCheckStockFragment();
                transaction.add(R.id.content, checkStockFragment);
//                if(checkStockFragment==null){
//                    checkStockFragment=new HalfProductCheckStockFragment();
//                    if(flag==4){
//                        Bundle data=new Bundle();
//                        data.putInt("SIGN",1);
//                        checkStockFragment.setArguments(data);
//                    }
//                    transaction.add(R.id.content,checkStockFragment);
//                }else
//                    transaction.show(checkStockFragment);
                break;
            default:
                break;
        }

        transaction.commit();
    }

    public void hideFragment(FragmentTransaction transaction) {
        if (stockFragment != null)
            transaction.hide(stockFragment);
        if (inHouseBillFragment != null)
            transaction.hide(inHouseBillFragment);
        if (outHouseBillFragment != null)
            transaction.hide(outHouseBillFragment);
        if (checkStockFragment != null)
            transaction.hide(checkStockFragment);

    }
}
