package com.example.administrator.cnmar.entity;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.administrator.cnmar.R;

/**
 * Created by Administrator on 2016/9/27.
 */
/**
 * 自定义的ListView ,解决ScrollView嵌套ListView的时候，自定义的Adapter的getView（）多次执行的问题
 */
public class MyListView extends LinearLayout {
    private BaseAdapter adapter;
    private MyOnItemClickListener onItemClickListener;

    public MyListView(Context context) {
        super(context);
        initAttr(null);
    }
    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttr(attrs);
    }

    /**
     * 通知更新listview
     */
    public void notifyChange() {
        int count = getChildCount();
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        for (int i = count; i < adapter.getCount(); i++) {
            final int index = i;
            final LinearLayout layout = new LinearLayout(getContext());
            layout.setLayoutParams(params);
            layout.setOrientation(VERTICAL);
            View v = adapter.getView(i, null, null);
            v.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(MyListView.this,
                                layout, index, adapter.getItem(index));
                    }
                }
            });
            // 每个条目下面的线
            ImageView imageView = new ImageView(getContext());
            imageView.setBackgroundColor(getResources().getColor(R.color.color_dark_grey));
            LayoutParams param = new LayoutParams(LayoutParams.MATCH_PARENT,
                    1);
            imageView.setLayoutParams(param);
            layout.addView(v);
            layout.addView(imageView);
            addView(layout, index);
        }
    }

    /**
     * 设置方向
     */
    public void initAttr(AttributeSet attrs) {
        setOrientation(VERTICAL);
    }
    public BaseAdapter getAdapter() {
        return adapter;
    }

    /**
     * 设置adapter并模拟listview添加数据
     */
    public void setAdapter(BaseAdapter adpater) {
        this.adapter = adpater;
        notifyChange();
    }

    /**
     * 设置条目监听事件
     */
    public void setOnItemClickListener(MyOnItemClickListener onClickListener) {
        this.onItemClickListener = onClickListener;
    }

    /**
     * 点击事件监听
     */
    public static interface MyOnItemClickListener {
        public void onItemClick(ViewGroup parent, View view, int position,
                                Object o);
    }
}