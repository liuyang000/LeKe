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
//        btn.setWidth(50);
//        btn.setHeight(30);
        return new ColorViewHolder(btn);
    }

    @Override
    public void onBindViewHolder(ColorViewHolder holder, final int position) {
//        holder.btn.setBackgroundColor(colors[position]);
//        GradientDrawable drawable = new GradientDrawable();
//        drawable.setColor(colors[position]);
//        drawable.setStroke(5,0xe0e0e0);
//        drawable.setCornerRadii(new float[]{2,2,2,2,2,2,2,2});
//        holder.btn.setBackgroundDrawable(drawable);
//        holder.btn.setBackgroundResource(R.drawable.color_item_shape);
        holder.btn.setFillColor(colors[position]);
        if (null != listener) {
            holder.btn.setOnClickListener(new View.OnClickListener() {
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
//        Button btn;

        ColorView btn;
        public ColorViewHolder(View itemView) {
            super(itemView);
            btn = (ColorView) itemView;
        }
    }
}
