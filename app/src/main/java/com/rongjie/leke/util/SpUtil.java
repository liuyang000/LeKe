package com.rongjie.leke.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.rongjie.leke.MyApplication;

/**
 * Created by jiangliang on 2016/6/30.
 */
public class SpUtil {
    private static final String LABEL_LOCATION = "label_location";
    private static final String LOCATION_X = "locationX";
    private static final String LOCATION_Y = "locationY";
    private static final String LABEL_CONTENT = "label_content";
    private static final SharedPreferences sp = MyApplication.getInstance().getSharedPreferences(LABEL_LOCATION, Context.MODE_PRIVATE);
    private static final SharedPreferences.Editor editor = sp.edit();
    public static void saveLableLocation(int x,int y){
        editor.putInt(LOCATION_X,x);
        editor.putInt(LOCATION_Y,y);
        editor.apply();
    }

    public static int getLocationX(){
        return sp.getInt(LOCATION_X,-1);
    }

    public static int getLocationY(){
        return sp.getInt(LOCATION_Y,-1);
    }

    public static void saveLabelContent(String content){
        editor.putString(LABEL_CONTENT,content);
        editor.apply();
    }
    public static void clearLabel(){
        editor.clear();
        editor.apply();
    }
    public static String getLabelContent(){
        return sp.getString(LABEL_CONTENT,"");
    }


}
