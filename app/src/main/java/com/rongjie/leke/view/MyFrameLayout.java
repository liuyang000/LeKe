package com.rongjie.leke.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * Created by jiangliang on 2016/7/6.
 */
public class MyFrameLayout extends FrameLayout {
    public MyFrameLayout(Context context) {
        super(context);
    }

    public MyFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return isInterceptable;
    }

    private boolean isInterceptable;

    public void setInterceptable(boolean isInterceptable){
        this.isInterceptable = isInterceptable;
    }

}
