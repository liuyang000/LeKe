package com.rongjie.leke.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.rongjie.leke.view.ColorView;

/**
 * Created by jiangliang on 2016/7/12.
 */
public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ColorViewHolder> {
    private int[] colors;
    private Context context;

    public ColorAdapter(int[] colors, Context context) {
        this.colors = colors;
        this.context = context;
    }

    @Override
    public ColorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ColorView btn = new ColorView(context);
        return new ColorViewHolder(btn);
    }

    @Override
    public void onBindViewHolder(ColorViewHolder holder, final int position) {
        holder.mColorView.setFillColor(colors[position]);
        if (null != listener) {
            holder.mColorView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return colors.length;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public class ColorViewHolder extends RecyclerView.ViewHolder {
        ColorView mColorView;
        public ColorViewHolder(View itemView) {
            super(itemView);
            mColorView = (ColorView) itemView;
        }
    }
}
