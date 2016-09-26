package com.example.administrator.cnmar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.administrator.cnmar.fragment.InOrderFragment;
import com.example.administrator.cnmar.fragment.OutOrderFragment;
import com.example.administrator.cnmar.fragment.ProductCheckStockFragment;
import com.example.administrator.cnmar.fragment.ProductStockFragment;
import com.example.administrator.cnmar.helper.UniversalHelper;


public class ProductStockActivity extends AppCompatActivity {
        private TextView tvTitle;
        private ImageView ivLeftArrow;
        private ProductStockFragment stockFragment;
        private InOrderFragment inHouseBillFragment;
        private OutOrderFragment outHouseBillFragment;
        private ProductCheckStockFragment checkStockFragment;
        private RadioGroup rg;
        private RadioButton rbStock,rbInOrder,rbOutOrder,rbCheckStock;
        private LinearLayout llLeftArrow;
        //    定义一个标志位，判断是哪个activity启动的本页面
        private int flag=0;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_material_warehouse);

            tvTitle= (TextView) findViewById(R.id.title);
            rg= (RadioGroup) findViewById(R.id.rg);
            rbStock= (RadioButton) findViewById(R.id.stock);
            rbInOrder= (RadioButton) findViewById(R.id.inHouseBill);
            rbOutOrder= (RadioButton) findViewById(R.id.outHouseBill);
            rbCheckStock= (RadioButton) findViewById(R.id.checkStock);
            llLeftArrow= (LinearLayout) findViewById(R.id.left_arrow);

            ivLeftArrow= (ImageView) findViewById(R.id.left_img);
            UniversalHelper.backToLastActivity(this,llLeftArrow,new MainActivity());
            tvTitle.setText("成品仓库");

//      默认选中库存
            setSelection(0);

            rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId){
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
            flag=getIntent().getIntExtra("flag",0);
            if(flag==1){
                setSelection(1);
            }else if (flag==2){
                setSelection(2);
            }else if (flag==3){
                setSelection(3);
            }
//        else if (flag==4){
//            setSelection(3);
//        }
        }


        @Override
        public boolean onKeyDown(int keyCode, KeyEvent event) {
            if(keyCode==KeyEvent.KEYCODE_BACK){
                Intent intent=new Intent(this,MainActivity.class);
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
        public  void setSelection(int id){
            FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
            hideFragment(transaction);

            switch (id){
                case 0:
                    rbStock.setChecked(true);
                    rbStock.setBackgroundColor(getResources().getColor(R.color.colorBase));
                    rbInOrder.setBackgroundColor(getResources().getColor(R.color.color_white));
                    rbOutOrder.setBackgroundColor(getResources().getColor(R.color.color_white));
                    rbCheckStock.setBackgroundColor(getResources().getColor(R.color.color_white));
                    if(stockFragment==null){
                        stockFragment=new ProductStockFragment();
                        transaction.add(R.id.content,stockFragment);
                    }else
                        transaction.show(stockFragment);
                    break;
                case 1:
                    rbInOrder.setChecked(true);
                    rbStock.setBackgroundColor(getResources().getColor(R.color.color_white));
                    rbInOrder.setBackgroundColor(getResources().getColor(R.color.colorBase));
                    rbOutOrder.setBackgroundColor(getResources().getColor(R.color.color_white));
                    rbCheckStock.setBackgroundColor(getResources().getColor(R.color.color_white));
                    if(inHouseBillFragment==null){
                        inHouseBillFragment=new InOrderFragment();
                        transaction.add(R.id.content,inHouseBillFragment);
                    }else
                        transaction.show(inHouseBillFragment);
                    break;
                case 2:
                    rbOutOrder.setChecked(true);
                    rbStock.setBackgroundColor(getResources().getColor(R.color.color_white));
                    rbInOrder.setBackgroundColor(getResources().getColor(R.color.color_white));
                    rbOutOrder.setBackgroundColor(getResources().getColor(R.color.colorBase));
                    rbCheckStock.setBackgroundColor(getResources().getColor(R.color.color_white));
                    if(outHouseBillFragment==null){
                        outHouseBillFragment=new OutOrderFragment();
                        transaction.add(R.id.content,outHouseBillFragment);
                    }else
                        transaction.show(outHouseBillFragment);
                    break;
                case 3:
                    rbCheckStock.setChecked(true);
                    rbStock.setBackgroundColor(getResources().getColor(R.color.color_white));
                    rbInOrder.setBackgroundColor(getResources().getColor(R.color.color_white));
                    rbOutOrder.setBackgroundColor(getResources().getColor(R.color.color_white));
                    rbCheckStock.setBackgroundColor(getResources().getColor(R.color.colorBase));
                    if(checkStockFragment==null){
                        checkStockFragment=new ProductCheckStockFragment();
//                    if(flag==4){
//                        Bundle data=new Bundle();
//                        data.putInt("SIGN",1);
//                        checkStockFragment.setArguments(data);
//                    }
                        transaction.add(R.id.content,checkStockFragment);
                    }else
                        transaction.show(checkStockFragment);
                    break;
                default:
                    break;
            }

            transaction.commit();
        }
        public void hideFragment(FragmentTransaction transaction){
            if(stockFragment!=null)
                transaction.hide(stockFragment);
            if(inHouseBillFragment!=null)
                transaction.hide(inHouseBillFragment);
            if(outHouseBillFragment!=null)
                transaction.hide(outHouseBillFragment);
            if(checkStockFragment!=null)
                transaction.hide(checkStockFragment);

        }
    }

