package com.example.administrator.cnmar.entity;

/**
 * Created by Administrator on 2016/9/27.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.administrator.cnmar.R;

/**
 * 自定义的ListView ,解决ScrollView嵌套ListView的时候，自定义的Adapter的getView（）多次执行的问题
 */
public class MyListView extends LinearLayout {
    private BaseAdapter adapter;
    private MyOnItemClickListener onItemClickListener;
    boolean footerViewAttached = false;
    private View footerview;

    /**
     * 通知更新listview
     */
    public void notifyChange() {
        int count = getChildCount();
        if (footerViewAttached) {
            count--;
        }
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
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
                        onItemClickListener.onItemClick(MyListView.this, layout, index,
                                adapter.getItem(index));
                    }
                }
            });
            ImageView imageView = new ImageView(getContext());
            imageView.setBackgroundColor(getResources().getColor(R.color.color_dark_grey));
            LayoutParams param = new LayoutParams(LayoutParams.MATCH_PARENT,2);
            imageView.setLayoutParams(param);
            layout.addView(v);
            layout.addView(imageView);
            addView(layout, index);
        }
    }

    public MyListView(Context context) {
        super(context);
        initAttr(null);
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttr(attrs);
    }

    public void initAttr(AttributeSet attrs) {
        setOrientation(VERTICAL);
    }

    /**
     * 初始化footerview
     *
     * @param footerView
     */
    public void initFooterView(final View footerView) {
        this.footerview = footerView;
    }

    /**
     * 设置footerView监听事件
     *
     * @param onClickListener
     */
    public void setFooterViewListener(OnClickListener onClickListener) {
        this.footerview.setOnClickListener(onClickListener);
    }

    public BaseAdapter getAdapter() {
        return adapter;
    }

    /**
     * 设置adapter并模拟listview添加????数据
     *
     * @param adpater
     */
    public void setAdapter(BaseAdapter adpater) {
        this.adapter = adpater;
        removeAllViews();
        if (footerViewAttached)
            addView(footerview);
        notifyChange();
    }

    /**
     * 设置条目监听事件
     *
     * @param onClickListener
     */
    public void setOnItemClickListener(MyOnItemClickListener onClickListener) {
        this.onItemClickListener = onClickListener;
    }

    /**
     * 没有下一页了
     */
    public void noMorePages() {
        if (footerview != null && footerViewAttached) {
            removeView(footerview);
            footerViewAttached = false;
        }
    }

    /**
     * 可能还有下一??
     */
    public void mayHaveMorePages() {
        if (!footerViewAttached && footerview != null) {
            addView(footerview);
            footerViewAttached = true;
        }
    }

    public static interface MyOnItemClickListener {
        public void onItemClick(ViewGroup parent, View view, int position, Object o);
    }
}