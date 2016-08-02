package com.rongjie.leke.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rongjie.leke.R;
import com.rongjie.leke.view.MyFrameLayout;
import com.rongjie.leke.view.NoteBookView;

import java.util.List;

/**
 * Created by jiangliang on 2016/7/29.
 */
public class NoteBookAdapter extends RecyclerView.Adapter<NoteBookAdapter.NoteBookViewHolder> {


    private List<Integer> mInts;
    private Context mContext;

    public NoteBookAdapter(List<Integer> ints, Context context) {
        mInts = ints;
        mContext = context;
    }

    @Override
    public NoteBookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.notebook_item,null);
        return new NoteBookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NoteBookViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mInts.size();
    }

    public class NoteBookViewHolder extends RecyclerView.ViewHolder {
        MyFrameLayout mFrameLayout;
        NoteBookView noteBookView;

        public NoteBookViewHolder(View itemView) {
            super(itemView);
            mFrameLayout = (MyFrameLayout) itemView;
            noteBookView = (NoteBookView) itemView.findViewById(R.id.booknote);
        }
    }

}