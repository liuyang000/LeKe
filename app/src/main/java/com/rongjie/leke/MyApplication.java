package com.rongjie.leke;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.rongjie.pdf.utils.FileUtils;

/**
 * Created by jiangliang on 2016/6/30.
 */
public class MyApplication extends Application {

    private static Context instance;
    /** 主线程Handler */
    public static Handler mMainThreadHandler;
    public static MyApplication mInstance;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mInstance = this;
        mMainThreadHandler = new Handler();
        //创建 课本 文件夹
        FileUtils.createDirs(FileUtils.getTextBookFilesDir(this));
        //创建 课本 图片 文件夹
        FileUtils.createDirs(FileUtils.getTextBookIconFilesDir(this));

    }

    public static Context getInstance(){
        return instance;
    }
    public static MyApplication getApplication() {
        return mInstance;
    }

    public static Handler getMainThreadHandler() {
        return mMainThreadHandler;
    }
}
