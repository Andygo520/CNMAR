package com.example.administrator.cnmar.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.administrator.cnmar.AppExit;
import com.example.administrator.cnmar.R;
import com.example.administrator.cnmar.helper.SPHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReportManageActivity extends AppCompatActivity {
    @BindView(R.id.left_arrow)
    LinearLayout leftArrow;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.webMaterial)
    WebView webMaterial;
    @BindView(R.id.webHalf)
    WebView webHalf;
    @BindView(R.id.webProduct)
    WebView webProduct;
    @BindView(R.id.rb1)
    RadioButton rbMaterialReport;
    @BindView(R.id.rb2)
    RadioButton rbHalfReport;
    @BindView(R.id.rb3)
    RadioButton rbProductReport;
    @BindView(R.id.rg)
    RadioGroup rg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_manage);
        ButterKnife.bind(this);
        AppExit.getInstance().addActivity(this);

//       取出登录界面获得的一级菜单与其二级菜单的对应关系
        String sublist = SPHelper.getString(this, getResources().getString(R.string.HOME_BBGL), "");
//       根据sublist的内容来设置默认选中的单选按钮(默认不可见)
        if (sublist.contains(","+getResources().getString(R.string.material_report_url)+",")) {
            rbMaterialReport.setVisibility(View.VISIBLE);
            setTabSelection(0);
        }
        if (sublist.contains(","+getResources().getString(R.string.half_report_url)+",")) {
            rbHalfReport.setVisibility(View.VISIBLE);
            if (!sublist.contains(","+getResources().getString(R.string.material_report_url)+","))
                setTabSelection(1);
        }
        if (sublist.contains(","+getResources().getString(R.string.product_report_url)+",")) {
            rbProductReport.setVisibility(View.VISIBLE);
            if (!sublist.contains(","+getResources().getString(R.string.material_report_url)+",")
                    && !sublist.contains(","+getResources().getString(R.string.half_report_url)+","))
                setTabSelection(2);
        }
        title.setText("报表管理");
        rbMaterialReport.setText(R.string.rbMaterialReport);
        rbHalfReport.setText(R.string.rbHalfReport);
        rbProductReport.setText(R.string.rbProductReport);

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
                }

            }
        });

    }

    @OnClick(R.id.left_arrow)
    public void onClick() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * 根据传入的index参数来设置选中的tab页。
     *
     * @param index 每个tab页对应的下标。0表示原料报表，1表示半成品报表，2表示成品报表
     */
    private void setTabSelection(int index) {
        switch (index) {
            case 0: {
                rbMaterialReport.setChecked(true);
                rbMaterialReport.setBackgroundColor(getResources().getColor(R.color.colorBase));
                rbHalfReport.setBackgroundColor(getResources().getColor(R.color.color_white));
                rbProductReport.setBackgroundColor(getResources().getColor(R.color.color_white));
                //                动态设置单选按钮文本上下左右的图片
                Drawable drawable1 = getResources().getDrawable(R.drawable.material_table_selected);
                Drawable drawable2 = getResources().getDrawable(R.drawable.half_table);
                Drawable drawable3 = getResources().getDrawable(R.drawable.product_table);
                drawable1.setBounds(0, 15, 70, 85);//第一0是距左边距离，第二15是距上边距离，长宽为70
                drawable2.setBounds(0, 15, 70, 85);//第一0是距左边距离，第二15是距上边距离，长宽为70
                drawable3.setBounds(0, 15, 70, 85);//第一0是距左边距离，第二15是距上边距离，长宽为70
                rbMaterialReport.setCompoundDrawables(null,drawable1,null,null);//只放上边
                rbHalfReport.setCompoundDrawables(null,drawable2,null,null);//只放上边
                rbProductReport.setCompoundDrawables(null,drawable3,null,null);//只放上边
                webMaterial.setVisibility(View.VISIBLE);
                webHalf.setVisibility(View.GONE);
                webProduct.setVisibility(View.GONE);
                WebSettings mWebSettings = webMaterial.getSettings();
//              调用JS方法.安卓版本大于17,加上注解 @JavascriptInterface
                mWebSettings.setJavaScriptEnabled(true);
                webMaterial.loadUrl("http://benxiao.cnmar.com:8091/material_report/show");
                break;
            }
            case 1: {
                rbHalfReport.setChecked(true);
                rbMaterialReport.setBackgroundColor(getResources().getColor(R.color.color_white));
                rbHalfReport.setBackgroundColor(getResources().getColor(R.color.colorBase));
                rbProductReport.setBackgroundColor(getResources().getColor(R.color.color_white));
                //                动态设置单选按钮文本上下左右的图片
                Drawable drawable1 = getResources().getDrawable(R.drawable.material_table);
                Drawable drawable2 = getResources().getDrawable(R.drawable.half_table_selected);
                Drawable drawable3 = getResources().getDrawable(R.drawable.product_table);
                drawable1.setBounds(0, 15, 70, 85);//第一0是距左边距离，第二15是距上边距离，长宽为70
                drawable2.setBounds(0, 15, 70, 85);//第一0是距左边距离，第二15是距上边距离，长宽为70
                drawable3.setBounds(0, 15, 70, 85);//第一0是距左边距离，第二15是距上边距离，长宽为70
                rbMaterialReport.setCompoundDrawables(null,drawable1,null,null);//只放上边
                rbHalfReport.setCompoundDrawables(null,drawable2,null,null);//只放上边
                rbProductReport.setCompoundDrawables(null,drawable3,null,null);//只放上边
                webHalf.setVisibility(View.VISIBLE);
                webMaterial.setVisibility(View.GONE);
                webProduct.setVisibility(View.GONE);
                WebSettings mWebSettings = webHalf.getSettings();
//              调用JS方法.安卓版本大于17,加上注解 @JavascriptInterface
                mWebSettings.setJavaScriptEnabled(true);
                webHalf.loadUrl("http://benxiao.cnmar.com:8091/half_report/show");
                break;
            }

            case 2: {
                rbProductReport.setChecked(true);
                rbMaterialReport.setBackgroundColor(getResources().getColor(R.color.color_white));
                rbHalfReport.setBackgroundColor(getResources().getColor(R.color.color_white));
                rbProductReport.setBackgroundColor(getResources().getColor(R.color.colorBase));
                //                动态设置单选按钮文本上下左右的图片
                Drawable drawable1 = getResources().getDrawable(R.drawable.material_table);
                Drawable drawable2 = getResources().getDrawable(R.drawable.half_table);
                Drawable drawable3 = getResources().getDrawable(R.drawable.product_table_selected);
                drawable1.setBounds(0, 15, 70, 85);//第一0是距左边距离，第二15是距上边距离，长宽为70
                drawable2.setBounds(0, 15, 70, 85);//第一0是距左边距离，第二15是距上边距离，长宽为70
                drawable3.setBounds(0, 15, 70, 85);//第一0是距左边距离，第二15是距上边距离，长宽为70
                rbMaterialReport.setCompoundDrawables(null,drawable1,null,null);//只放上边
                rbHalfReport.setCompoundDrawables(null,drawable2,null,null);//只放上边
                rbProductReport.setCompoundDrawables(null,drawable3,null,null);//只放上边
                webProduct.setVisibility(View.VISIBLE);
                webHalf.setVisibility(View.GONE);
                webMaterial.setVisibility(View.GONE);
                WebSettings mWebSettings = webProduct.getSettings();
//              调用JS方法.安卓版本大于17,加上注解 @JavascriptInterface
                mWebSettings.setJavaScriptEnabled(true);
                webProduct.loadUrl("http://benxiao.cnmar.com:8091/product_report/show");
                break;
            }
        }
    }

}
