package com.rongjie.leke;

import android.app.Application;
import android.content.Context;

/**
 * Created by jiangliang on 2016/6/30.
 */
public class MyApplication extends Application {

    private static Context instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static Context getInstance(){
        return instance;
    }
}
