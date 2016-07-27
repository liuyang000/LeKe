package com.rongjie.pdf.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.artifex.mupdfdemo.OutlineItem;
import com.rongjie.leke.MyApplication;
import com.rongjie.leke.R;
import com.rongjie.pdf.utils.Uiutils;

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
        if (mItems == null || mItems.length == 0) {
            return 0;
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
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.adapter_outline_entry, null);
            holder = new ViewHolder();
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_page = (TextView) convertView.findViewById(R.id.tv_page);
            holder.rl_manger = (RelativeLayout) convertView.findViewById(R.id.rl_manger);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        int level = mItems[position].level;

        int titleColor = MyApplication.getInstance().getResources().getColor(R.color.directory_title);
        int textColor = MyApplication.getInstance().getResources().getColor(R.color.directory_text);

        System.out.println("level====" + level);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.rl_manger.getLayoutParams();
        if (level == 1) { //level==0是标题
            holder.tv_title.setTextColor(textColor);
            params.height = Uiutils.dip2px(50);
            holder.rl_manger.setLayoutParams(params);
        } else { // 是标题
            holder.tv_title.setTextColor(titleColor);
            params.height =Uiutils.dip2px(60);
            holder.rl_manger.setLayoutParams(params);
        }
        holder.tv_title.setText(mItems[position].title);
        holder.tv_page.setText(String.valueOf(mItems[position].page + 1) + "页");
        return convertView;
    }


    public static class ViewHolder {
        public TextView tv_title;
        public TextView tv_page;
        public RelativeLayout rl_manger;
    }
}

