package com.rongjie.pdf.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rongjie.leke.R;

import java.io.File;
import java.util.List;

/**
 * Created by Administrator on 2016/7/14.
 * 书的适配器
 */
public class AdaptetFragmentTextBook extends RecyclerView.Adapter<AdaptetFragmentTextBook.HolerFragmentTextBook> {

    private Context mContext ;
    private List<File> mInfos ;

    /**构造函数 初始化数据*/
    public AdaptetFragmentTextBook(Context mContext, List<File> mInfos) {
        this.mContext = mContext;
        this.mInfos = mInfos;
    }

    /**创建HOLDER  和创建View*/
    @Override
    public HolerFragmentTextBook onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = View.inflate(mContext , R.layout.adapter_fragment_text_book,null) ;

        return  new HolerFragmentTextBook(view) ;
    }

    /**绑定数据*/
    @Override
    public void onBindViewHolder(final HolerFragmentTextBook holder, int position) {
        holder.mRlItemBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener!=null)
                    mListener.onClickItemTextBookListener(mInfos.get(holder.getLayoutPosition()));
            }
        });

        holder.setViewData(position);
    }

    /**返回个数*/
    @Override
    public int getItemCount() {
        if (mInfos!=null){
            return mInfos.size();
        }
        return 0;
    }

    /**holder*/
    public class HolerFragmentTextBook extends RecyclerView.ViewHolder {

        private final TextView mTvBookName;
        private final RelativeLayout mRlItemBook;

        public HolerFragmentTextBook(View itemView) {
            super(itemView);
             mTvBookName=(TextView)itemView.findViewById(R.id.tv_book_name) ;
             mRlItemBook=(RelativeLayout)itemView.findViewById(R.id.rl_item_book) ;

        }

        public  void setViewData(int position){

            if (mInfos!=null && mInfos.size()>0){
                //设置书的名字
               String name = mInfos.get(position).getName().substring(0, mInfos.get(position).getName().length()-4);
                mTvBookName.setText(name);
            }
        }
    }


    private  ClickItemTextBookListener mListener ;
    /**书籍  点击事件的监听*/
    public  interface  ClickItemTextBookListener {

        public void onClickItemTextBookListener(File file);
    }

    /**点击事件*/
    public void setListener(ClickItemTextBookListener listener) {
        this.mListener = listener;
    }
}
