package com.rongjie.pdf.utils;

import android.app.Activity;
import android.content.Context;
import android.view.WindowManager;

/**
 * Created by Administrator on 2016/7/25.
 */
public class Uiutils {


    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }

    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getHeight();
    }
}
