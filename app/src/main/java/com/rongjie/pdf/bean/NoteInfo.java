package com.rongjie.pdf.bean;

/**
 * Created by Administrator on 2016/7/28.
 * 笔记info
 */
public class NoteInfo {
    /**笔记名称*/
    private  String fileName ;
    /**判断是否是 用户自己添加的*/
    private boolean  isAddInfo ;

    /*** 这个是添加到尾部的 View*/
    private  boolean isAddView;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


    public boolean isAddInfo() {
        return isAddInfo;
    }

    public void setAddInfo(boolean addInfo) {
        isAddInfo = addInfo;
    }

    public boolean isAddView() {
        return isAddView;
    }

    public void setAddView(boolean addView) {
        isAddView = addView;
    }
}
