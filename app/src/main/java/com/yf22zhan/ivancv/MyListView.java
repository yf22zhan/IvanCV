package com.yf22zhan.ivancv;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by yzhang on 2/10/2016.
 */
public class MyListView extends ListView {

    public MyListView(Context mContext) {
        super(mContext);
    }

    public MyListView(Context mContext, AttributeSet attrs) {
        super(mContext, attrs);
    }

    public MyListView(Context mContext, AttributeSet attrs, int defStyle) {
        super(mContext, attrs, defStyle);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);

        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
