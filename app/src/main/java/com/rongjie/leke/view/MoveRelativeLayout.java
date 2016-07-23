package com.rongjie.leke.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

/**
 * Created by jiangliang on 2016/7/13.
 */
public class MoveRelativeLayout extends RelativeLayout {

    private float mRawX;
    private float mRawY;
    private float mStartX;
    private float mStartY;
    private float tempX;
    private float tempY;

    public MoveRelativeLayout(Context context) {
        super(context);
    }

    public MoveRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mStartX = event.getX();
                mStartY = event.getY();
                tempX = event.getRawX();
                tempY = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                mRawX = event.getRawX();
                mRawY = event.getRawY();
                if ((int) tempX != (int) mRawX && (int) tempY != (int) mRawY) {
                    updateWindowPosition();
                }
                break;
        }
        return true;
    }

    private FrameLayout.LayoutParams params;
    private FrameLayout viewGroup;

    private void updateWindowPosition() {
        viewGroup = (FrameLayout) getParent();
        params = (FrameLayout.LayoutParams) getLayoutParams();
        params.leftMargin = (int) (mRawX - mStartX);
//        params.topMargin = (int) (mRawY - mStartY - 2 * getStatusBarHeight());
        params.topMargin = (int) (mRawY - mStartY);
        viewGroup.updateViewLayout(this, params);
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
