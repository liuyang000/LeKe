package com.rongjie.pdf.View.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Display;
import android.view.Window;

/**
 * Created by Administrator on 2016/7/11.
 *
 * 对话框
 */
public abstract class RJBaseDialog extends Dialog {

    public static float DEFAULT_SCALE = 0.9f;
    private OnDismissListener mOutListener;
    private OnDismissListener mInnerListener = new InnerDismissListener();




    public RJBaseDialog(Context context) {
        super(context);
        init();
        Window window = getWindow();
      window.setBackgroundDrawableResource(android.R.color.transparent);
     super.setOnDismissListener(mInnerListener);
    }

    public RJBaseDialog(Context context, int themeResId) {
        super(context, themeResId);
        init();
        Window window = getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
       super.setOnDismissListener(mInnerListener);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        initLayout();
    }

    protected float getWidthScale() {
        return DEFAULT_SCALE;
    }

    protected float getHeightScale() {
        return DEFAULT_SCALE;
    }

    protected abstract void init();

    protected abstract void initLayout();

    @Override
    public void show() {
        super.show();
        Window window = getWindow();
        Display display = window.getWindowManager().getDefaultDisplay();
        int width = (int) (display.getWidth() * getWidthScale());
        int height = (int) (display.getHeight() * getHeightScale());
        window.setLayout(width, display.getHeight());
        //window.setGravity(Gravity.CENTER_VERTICAL);
    }

    @Override
    public void setOnDismissListener(OnDismissListener listener) {
        mOutListener = listener;
    }

    class InnerDismissListener implements OnDismissListener{
        @Override
        public void onDismiss(DialogInterface dialog) {
            if (mOutListener != null) {
                mOutListener.onDismiss(dialog);
            }
        }
    }
}
