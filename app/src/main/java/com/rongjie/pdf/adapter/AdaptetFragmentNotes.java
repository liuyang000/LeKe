package com.rongjie.pdf.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rongjie.leke.R;
import com.rongjie.pdf.bean.NoteInfo;
import com.rongjie.pdf.utils.Uiutils;

import java.util.List;

/**
 * Created by Administrator on 2016/7/28.
 * 笔记适配器
 */
public class AdaptetFragmentNotes extends RecyclerView.Adapter<AdaptetFragmentNotes.HolerFragmentNotes> {

    private Context mContext;
    private List<NoteInfo> mInfos;

    public AdaptetFragmentNotes(Context context, List<NoteInfo> noteInfos) {
        this.mContext = context;
        this.mInfos = noteInfos;
    }

    @Override
    public HolerFragmentNotes onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.adapter_fragment_notes, null);
        return new HolerFragmentNotes(view);
    }

    @Override
    public void onBindViewHolder(final HolerFragmentNotes holder, int position) {
        holder.mRlItemNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null)
                    mListener.onClickItemNotesListener(mInfos.get(holder.getLayoutPosition()));
            }
        });

        holder.setViewData(position);
    }

    @Override
    public int getItemCount() {
        if (mInfos != null) {
            return mInfos.size();
        }
        return 0;
    }

    //holder band
    public class HolerFragmentNotes extends RecyclerView.ViewHolder {

        private final TextView mTvBookName;
        private final RelativeLayout mRlItemNotes;
        private final ImageView mImgIcon;

        public HolerFragmentNotes(View itemView) {
            super(itemView);
            mTvBookName = (TextView) itemView.findViewById(R.id.tv_book_name);
            mRlItemNotes = (RelativeLayout) itemView.findViewById(R.id.rl_item_book);
            mImgIcon = (ImageView) itemView.findViewById(R.id.img_book_icon);

        }

        public void setViewData(int position) {
            if (mInfos != null && mInfos.size() > 0) {
                //设置书的名字
                if (mInfos.get(position).isAddView()){
                    mImgIcon.setImageDrawable(Uiutils.getDrawable(R.drawable.img_add));

                }else{
                    mTvBookName.setText(mInfos.get(position).getFileName());
                    mImgIcon.setImageDrawable(Uiutils.getDrawable(R.mipmap.yuanye));
                }
            }
        }
    }


    private ClickItemNotesListener mListener;

    /**
     * 书籍  点击事件的监听
     */
    public interface ClickItemNotesListener {

        void onClickItemNotesListener(NoteInfo info);
    }

    /**
     * 点击事件
     */
    public void setListener(ClickItemNotesListener listener) {
        this.mListener = listener;
    }
}
