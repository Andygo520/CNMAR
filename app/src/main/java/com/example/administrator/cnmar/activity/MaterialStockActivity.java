package com.example.administrator.cnmar.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import com.example.administrator.cnmar.fragment.InOrderFragment;
import com.example.administrator.cnmar.fragment.MaterialCheckStockFragment;
import com.example.administrator.cnmar.fragment.MaterialStockFragment;
import com.example.administrator.cnmar.fragment.OutOrderFragment;
import com.example.administrator.cnmar.helper.SPHelper;

public class MaterialStockActivity extends AppCompatActivity {
    private TextView tvTitle;
    private MaterialStockFragment materialStockFragment;
    private InOrderFragment inHouseBillFragment;
    private OutOrderFragment outHouseBillFragment;
    private MaterialCheckStockFragment materialCheckStockFragment;
    private RadioGroup rg;
    private RadioButton rbStock, rbInOrder, rbOutOrder, rbCheckStock;
    private LinearLayout llLeftArrow;
    //    定义一个标志位，判断是哪个activity启动的本页面
    private int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_stock);
        AppExit.getInstance().addActivity(this);
//        取出一级菜单name与其二级菜单url的对应关系
        String sublist = SPHelper.getString(this, getResources().getString(R.string.HOME_YLCK), "");
//        Log.d("subList",sublist);

        tvTitle = (TextView) findViewById(R.id.title);
        rg = (RadioGroup) findViewById(R.id.rg);
        rbStock = (RadioButton) findViewById(R.id.rb1);
        rbInOrder = (RadioButton) findViewById(R.id.rb2);
        rbOutOrder = (RadioButton) findViewById(R.id.rb3);
        rbCheckStock = (RadioButton) findViewById(R.id.rb4);
        llLeftArrow = (LinearLayout) findViewById(R.id.left_arrow);

        llLeftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MaterialStockActivity.this, MainActivity.class));
            }
        });

        tvTitle.setText("原料仓库");

//       根据sublist的内容来设置默认选中的单选按钮(默认不可见)
        if (sublist.contains("," + getResources().getString(R.string.material_stock_url) + ",")) {
            rbStock.setVisibility(View.VISIBLE);
            setSelection(0);
        }
        if (sublist.contains("," + getResources().getString(R.string.material_in_order_url) + ",")) {
            rbInOrder.setVisibility(View.VISIBLE);
            if (!sublist.contains("," + getResources().getString(R.string.material_stock_url) + ","))
                setSelection(1);
        }
        if (sublist.contains("," + getResources().getString(R.string.material_out_order_url) + ",")) {
            rbOutOrder.setVisibility(View.VISIBLE);
            if (!sublist.contains("," + getResources().getString(R.string.material_stock_url) + ",")
                    && !sublist.contains("," + getResources().getString(R.string.material_in_order_url) + ","))
                setSelection(2);

        }
        if (sublist.contains("," + getResources().getString(R.string.material_stock_check_manage_url) + ",")) {
            rbCheckStock.setVisibility(View.VISIBLE);
            if (!sublist.contains("," + getResources().getString(R.string.material_stock_url) + ",")
                    && !sublist.contains("," + getResources().getString(R.string.material_in_order_url) + ",")
                    && !sublist.contains("," + getResources().getString(R.string.material_out_order_url) + ","))
                setSelection(3);
        }


        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb1:
                        setSelection(0);
                        break;
                    case R.id.rb2:
                        setSelection(1);
                        break;
                    case R.id.rb3:
                        setSelection(2);
                        break;
                    case R.id.rb4:
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
        flag = getIntent().getIntExtra("flag", 999);
        if (flag == 0) {
            setSelection(0);
        } else if (flag == 1) {
            setSelection(1);
        } else if (flag == 2) {
            setSelection(2);
        } else if (flag == 3) {
            setSelection(3);
        }
    }

    @Override
    public void onAttachFragment(android.support.v4.app.Fragment fragment) {
        super.onAttachFragment(fragment);
        if (materialStockFragment == null && fragment instanceof MaterialStockFragment) {
            materialStockFragment = (MaterialStockFragment) fragment;
        } else if (inHouseBillFragment == null && fragment instanceof InOrderFragment) {
            inHouseBillFragment = (InOrderFragment) fragment;
        } else if (outHouseBillFragment == null && fragment instanceof OutOrderFragment) {
            outHouseBillFragment = (OutOrderFragment) fragment;
        } else if (materialCheckStockFragment == null && fragment instanceof MaterialCheckStockFragment) {
            materialCheckStockFragment = (MaterialCheckStockFragment) fragment;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(MaterialStockActivity.this, MainActivity.class));
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
            case 0: {
                rbStock.setChecked(true);
                rbStock.setBackgroundColor(getResources().getColor(R.color.colorBase));
                rbInOrder.setBackgroundColor(getResources().getColor(R.color.color_white));
                rbOutOrder.setBackgroundColor(getResources().getColor(R.color.color_white));
                rbCheckStock.setBackgroundColor(getResources().getColor(R.color.color_white));
                //                动态设置单选按钮文本上下左右的图片
                Drawable drawable1 = getResources().getDrawable(R.drawable.stock_selected);
                Drawable drawable2 = getResources().getDrawable(R.drawable.in_order);
                Drawable drawable3 = getResources().getDrawable(R.drawable.out_order);
                Drawable drawable4 = getResources().getDrawable(R.drawable.check_stock);

                drawable1.setBounds(0, 15, 70, 85);//第一0是距左边距离，第二15是距上边距离，长宽为70
                drawable2.setBounds(0, 15, 70, 85);//第一0是距左边距离，第二15是距上边距离，长宽为70
                drawable3.setBounds(0, 15, 70, 85);//第一0是距左边距离，第二15是距上边距离，长宽为70
                drawable4.setBounds(0, 15, 70, 85);//第一0是距左边距离，第二15是距上边距离，长宽为70
                rbStock.setCompoundDrawables(null, drawable1, null, null);//只放上边
                rbInOrder.setCompoundDrawables(null, drawable2, null, null);//只放上边
                rbOutOrder.setCompoundDrawables(null, drawable3, null, null);//只放上边
                rbCheckStock.setCompoundDrawables(null, drawable4, null, null);//只放上边

                if (materialStockFragment == null) {
                    materialStockFragment = new MaterialStockFragment();
                    transaction.add(R.id.content, materialStockFragment);
                } else
                    transaction.show(materialStockFragment);
                break;
            }
            case 1: {
                rbInOrder.setChecked(true);
                rbStock.setBackgroundColor(getResources().getColor(R.color.color_white));
                rbInOrder.setBackgroundColor(getResources().getColor(R.color.colorBase));
                rbOutOrder.setBackgroundColor(getResources().getColor(R.color.color_white));
                rbCheckStock.setBackgroundColor(getResources().getColor(R.color.color_white));
                //                动态设置单选按钮文本上下左右的图片
                Drawable drawable1 = getResources().getDrawable(R.drawable.stock);
                Drawable drawable2 = getResources().getDrawable(R.drawable.in_order_selected);
                Drawable drawable3 = getResources().getDrawable(R.drawable.out_order);
                Drawable drawable4 = getResources().getDrawable(R.drawable.check_stock);

                drawable1.setBounds(0, 15, 70, 85);//第一0是距左边距离，第二15是距上边距离，长宽为70
                drawable2.setBounds(0, 15, 70, 85);//第一0是距左边距离，第二15是距上边距离，长宽为70
                drawable3.setBounds(0, 15, 70, 85);//第一0是距左边距离，第二15是距上边距离，长宽为70
                drawable4.setBounds(0, 15, 70, 85);//第一0是距左边距离，第二15是距上边距离，长宽为70
                rbStock.setCompoundDrawables(null, drawable1, null, null);//只放上边
                rbInOrder.setCompoundDrawables(null, drawable2, null, null);//只放上边
                rbOutOrder.setCompoundDrawables(null, drawable3, null, null);//只放上边
                rbCheckStock.setCompoundDrawables(null, drawable4, null, null);//只放上边
                if (inHouseBillFragment == null) {
                    inHouseBillFragment = new InOrderFragment();
                    transaction.add(R.id.content, inHouseBillFragment);
                } else
                    transaction.show(inHouseBillFragment);
                break;
            }
            case 2: {
                rbOutOrder.setChecked(true);
                rbStock.setBackgroundColor(getResources().getColor(R.color.color_white));
                rbInOrder.setBackgroundColor(getResources().getColor(R.color.color_white));
                rbOutOrder.setBackgroundColor(getResources().getColor(R.color.colorBase));
                rbCheckStock.setBackgroundColor(getResources().getColor(R.color.color_white));
                //                动态设置单选按钮文本上下左右的图片
                Drawable drawable1 = getResources().getDrawable(R.drawable.stock);
                Drawable drawable2 = getResources().getDrawable(R.drawable.in_order);
                Drawable drawable3 = getResources().getDrawable(R.drawable.out_order_selected);
                Drawable drawable4 = getResources().getDrawable(R.drawable.check_stock);

                drawable1.setBounds(0, 15, 70, 85);//第一0是距左边距离，第二15是距上边距离，长宽为70
                drawable2.setBounds(0, 15, 70, 85);//第一0是距左边距离，第二15是距上边距离，长宽为70
                drawable3.setBounds(0, 15, 70, 85);//第一0是距左边距离，第二15是距上边距离，长宽为70
                drawable4.setBounds(0, 15, 70, 85);//第一0是距左边距离，第二15是距上边距离，长宽为70
                rbStock.setCompoundDrawables(null, drawable1, null, null);//只放上边
                rbInOrder.setCompoundDrawables(null, drawable2, null, null);//只放上边
                rbOutOrder.setCompoundDrawables(null, drawable3, null, null);//只放上边
                rbCheckStock.setCompoundDrawables(null, drawable4, null, null);//只放上边

                if (outHouseBillFragment == null) {
                    outHouseBillFragment = new OutOrderFragment();
                    transaction.add(R.id.content, outHouseBillFragment);
                } else
                    transaction.show(outHouseBillFragment);
                break;
            }
            case 3: {
                rbCheckStock.setChecked(true);
                rbStock.setBackgroundColor(getResources().getColor(R.color.color_white));
                rbInOrder.setBackgroundColor(getResources().getColor(R.color.color_white));
                rbOutOrder.setBackgroundColor(getResources().getColor(R.color.color_white));
                rbCheckStock.setBackgroundColor(getResources().getColor(R.color.colorBase));
                //                动态设置单选按钮文本上下左右的图片
                Drawable drawable1 = getResources().getDrawable(R.drawable.stock);
                Drawable drawable2 = getResources().getDrawable(R.drawable.in_order);
                Drawable drawable3 = getResources().getDrawable(R.drawable.out_order);
                Drawable drawable4 = getResources().getDrawable(R.drawable.check_stock_selected);

                drawable1.setBounds(0, 15, 70, 85);//第一0是距左边距离，第二15是距上边距离，长宽为70
                drawable2.setBounds(0, 15, 70, 85);//第一0是距左边距离，第二15是距上边距离，长宽为70
                drawable3.setBounds(0, 15, 70, 85);//第一0是距左边距离，第二15是距上边距离，长宽为70
                drawable4.setBounds(0, 15, 70, 85);//第一0是距左边距离，第二15是距上边距离，长宽为70
                rbStock.setCompoundDrawables(null, drawable1, null, null);//只放上边
                rbInOrder.setCompoundDrawables(null, drawable2, null, null);//只放上边
                rbOutOrder.setCompoundDrawables(null, drawable3, null, null);//只放上边
                rbCheckStock.setCompoundDrawables(null, drawable4, null, null);//只放上边

                if (materialCheckStockFragment == null) {
                    materialCheckStockFragment = new MaterialCheckStockFragment();
                    transaction.add(R.id.content, materialCheckStockFragment);
                } else
                    transaction.show(materialCheckStockFragment);
                break;
            }
            default:
                break;
        }

        transaction.commit();
    }

    public void hideFragment(FragmentTransaction transaction) {
        if (materialStockFragment != null)
            transaction.hide(materialStockFragment);
        if (inHouseBillFragment != null)
            transaction.hide(inHouseBillFragment);
        if (outHouseBillFragment != null)
            transaction.hide(outHouseBillFragment);
        if (materialCheckStockFragment != null)
            transaction.hide(materialCheckStockFragment);

    }
}
