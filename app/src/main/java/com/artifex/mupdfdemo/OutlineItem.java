package com.artifex.mupdfdemo;

/**
 * Created by Administrator on 2016/6/29.
 */
public class OutlineItem {

    public final int    level;
    public final String title;
    public final int    page;

    OutlineItem(int _level, String _title, int _page) {
        level = _level;
        title = _title;
        page  = _page;
    }


}
