package com.rongjie.leke.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.rongjie.leke.Position;

public class MoveImageView extends ImageView {

    private float mRawX;
    private float mRawY;
    private float mStartX;
    private float mStartY;
    private static final String TAG = "MoveImageView";

    public MoveImageView(Context context) {
        super(context);
    }

    public MoveImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MoveImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    //用于判断是执行点击事件，还是更新View的位置。因为点击时也会出发几次move事件
    private int count = 0;
    private static final int MAX_COUNT = 6;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        final int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mStartX = event.getX();
                mStartY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                mRawX = event.getRawX();
                mRawY = event.getRawY();
                count++;
                if (count >= MAX_COUNT) {
                    updateWindowPosition();
                }
                break;

            case MotionEvent.ACTION_UP:
                if (count < MAX_COUNT) {
                    performClick();
                } else {
//                    imageViewMap.put(new Position(params.leftMargin,params.topMargin),this);
                    if (null != updateImageViewMapListener) {
                        updateImageViewMapListener.updateImageViewMap(new Position(params.leftMargin, params.topMargin), this);
                    }
                }
                count = 0;
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

    public void saveLocation() {
//        SpUtil.saveLableLocation(params.leftMargin,params.topMargin);
    }

    private UpdateImageViewMapListener updateImageViewMapListener;

    public void setUpdateImageViewMapListener(UpdateImageViewMapListener updateImageViewMapListener) {
        this.updateImageViewMapListener = updateImageViewMapListener;
    }

    public interface UpdateImageViewMapListener {
        void updateImageViewMap(Position position, MoveImageView imageView);
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
