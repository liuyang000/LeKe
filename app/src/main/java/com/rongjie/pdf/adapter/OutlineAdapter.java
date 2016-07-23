package com.rongjie.pdf.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.artifex.mupdfdemo.OutlineItem;
import com.rongjie.leke.R;

/**
 * Created by Administrator on 2016/7/1.
 */
public class OutlineAdapter extends BaseAdapter {
    private final OutlineItem mItems[];
    private final LayoutInflater mInflater;

    public OutlineAdapter(LayoutInflater inflater, OutlineItem items[]) {
        mInflater = inflater;
        mItems = items;
    }

    public int getCount() {
        // 当获取目录 不存在
        if (mItems==null || mItems.length == 0){
            return  0;
        }
        return mItems.length;
    }

    public Object getItem(int arg0) {
        return null;
    }

    public long getItemId(int arg0) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v;
        if (convertView == null) {
            v = mInflater.inflate(R.layout.adapter_outline_entry, null);
        } else {
            v = convertView;
        }
        int level = mItems[position].level;
        if (level > 8) level = 8;
        String space = "";
        for (int i = 0; i < level; i++)
            space += "   ";
        ((TextView) v.findViewById(R.id.title)).setText(space + mItems[position].title);
        ((TextView) v.findViewById(R.id.page)).setText(String.valueOf(mItems[position].page + 1));
        return v;
    }

}

