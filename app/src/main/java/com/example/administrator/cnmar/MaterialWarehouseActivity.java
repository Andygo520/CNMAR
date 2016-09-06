package com.example.administrator.cnmar;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.administrator.cnmar.fragment.CheckStockFragment;
import com.example.administrator.cnmar.fragment.InHouseBillFragment;
import com.example.administrator.cnmar.fragment.OutHouseBillFragment;
import com.example.administrator.cnmar.fragment.StockFragment;
import com.example.administrator.cnmar.helper.UniversalHelper;

public class MaterialWarehouseActivity extends AppCompatActivity {
    private TextView tvTitle;
    private ImageView ivLeftArrow,ivScann;
    private StockFragment stockFragment;
    private InHouseBillFragment inHouseBillFragment;
    private OutHouseBillFragment outHouseBillFragment;
    private CheckStockFragment checkStockFragment;
    private RadioGroup rg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_warehouse);

        tvTitle= (TextView) findViewById(R.id.title);
        rg= (RadioGroup) findViewById(R.id.rg);
        ivLeftArrow= (ImageView) findViewById(R.id.left_img);
        UniversalHelper.backToLastActivity(this,ivLeftArrow,new MainActivity());
        ivScann= (ImageView) findViewById(R.id.scann);
        tvTitle.setText("原料仓库");

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

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if(keyCode==KeyEvent.ACTION_DOWN){
//            Intent intent=new Intent(this,MainActivity.class);
//            startActivity(intent);
//            finish();
//        }
//        return super.onKeyDown(keyCode, event);
//    }
  /*
  *
  * 设置选中的Fragment,0代表库存,1代表入库单据,2代表出库单据,3代表盘库
  *
  * */
    public void setSelection(int id){
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        hideFragment(transaction);

        switch (id){
            case 0:
                if(stockFragment==null){
                    stockFragment=new StockFragment();
                    transaction.add(R.id.content,stockFragment);
                }else
                    transaction.show(stockFragment);
                break;
            case 1:
                if(inHouseBillFragment==null){
                    inHouseBillFragment=new InHouseBillFragment();
                    transaction.add(R.id.content,inHouseBillFragment);
                }else
                    transaction.show(inHouseBillFragment);
                break;
            case 2:
                if(outHouseBillFragment==null){
                    outHouseBillFragment=new OutHouseBillFragment();
                    transaction.add(R.id.content,outHouseBillFragment);
                }else
                    transaction.show(outHouseBillFragment);
                break;
            case 3:
                if(checkStockFragment==null){
                    checkStockFragment=new CheckStockFragment();
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
