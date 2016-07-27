package com.rongjie.pdf.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.rongjie.leke.MyApplication;

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
    /** 获取Context */
    public static Context getContext() {
        return MyApplication.getApplication();
    }





    /** 获取主线程的handler */
    public static Handler getMainThreadHandler() {
        return MyApplication.getMainThreadHandler();
    }

    /** 把任务post主线程中 */
    public static boolean post(Runnable runnable) {
        boolean result = false;
        Handler handler = getMainThreadHandler();
        if (handler != null) {
            result = handler.post(runnable);
        }
        return result;
    }

    /** 把任务延迟post到主线程中 */
    public static boolean postDelayed(Runnable runnable, long delay) {
        boolean result = false;
        Handler handler = getMainThreadHandler();
        if (handler != null) {
            result = handler.postDelayed(runnable, delay);
        }
        return result;
    }

    /** 把任务post到主线程的消息队列最前面 */
    public static boolean postAtFrontOfQueue(Runnable runnable) {
        boolean result = false;
        Handler handler = getMainThreadHandler();
        if (handler != null) {
            result = handler.postAtFrontOfQueue(runnable);
        }
        return result;
    }

    /** 把任务从主线程的消息队列中删除 */
    public static void removeCallbacks(Runnable runnable) {
        Handler handler = getMainThreadHandler();
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
    }



    /** 获取应用资源对象 */
    public static Resources getResources() {
        if (getContext() != null) {
            return getContext().getResources();
        } else {
            return null;
        }
    }

    /** dip转换px */
    public static int dip2px(int dip) {
        if (getResources() != null) {
            final float scale = getResources().getDisplayMetrics().density;
            return (int) (dip * scale + 0.5f);
        } else {
            return 0;
        }
    }

    /** px转换dip */
    public static int px2dip(int px) {
        if (getResources() != null) {
            final float scale = getResources().getDisplayMetrics().density;
            return (int) (px / scale + 0.5f);
        } else {
            return 0;
        }
    }

    /** sp转px*/
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /** 获取dimen值 */
    public static int getDimens(int resId) {
        if (getResources() != null) {
            return getResources().getDimensionPixelSize(resId);
        } else {
            return 0;
        }
    }

    /** 获取图片 */
    public static Drawable getDrawable(int resId) {
        if (getResources() != null) {
            return getResources().getDrawable(resId);
        } else {
            return null;
        }
    }

    /**获取图片*/
    public static Bitmap getBitmap(int resId){
        if(getResources() != null){
            return BitmapFactory.decodeResource(getResources(), resId);
        }else{
            return null;
        }
    }

    /** 获取颜色 */
    public static int getColor(int resId) {
        if (getResources() != null) {
            return getResources().getColor(resId);
        } else {
            return 0;
        }
    }

    /** 获取颜色选择器 */
    public static ColorStateList getColorStateList(int resId) {
        if (getResources() != null) {
            return getResources().getColorStateList(resId);
        } else {
            return null;
        }
    }

    /** 获取文字 */
    public static String getString(int resId) {
        if (getResources() != null) {
            return getResources().getString(resId);
        } else {
            return null;
        }
    }

    /** 获取文字，并按照后面的参数进行格式化 */
    public static String getString(int resId, Object... formatAgrs) {
        if (getResources() != null) {
            return getResources().getString(resId, formatAgrs);
        } else {
            return null;
        }
    }

    /**
     * 根据指定的layout索引，创建一个View
     * @param resId 指定的layout索引
     * @return 新的View
     */
    public static View inflate(int resId) {
        Context context = Uiutils.getContext();
        if (context != null) {
            return LayoutInflater.from(context).inflate(resId, null);
        }
        return null;
    }

}
