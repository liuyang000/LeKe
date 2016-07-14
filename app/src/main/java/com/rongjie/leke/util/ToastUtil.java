package com.rongjie.leke.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by jiangliang on 2016/7/14.
 */
public class ToastUtil {

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
    }


}
