package com.rongjie.pdf.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rongjie.leke.MyApplication;
import com.rongjie.pdf.bean.TextBookInfo;
import com.rongjie.pdf.utils.FileUtils;
import com.rongjie.pdf.utils.StringUtils;
import com.rongjie.leke.R;
import java.io.File;
import java.util.List;

/**
 * Created by Administrator on 2016/7/14.
 * 书的适配器
 */
public class AdaptetFragmentTextBook extends RecyclerView.Adapter<AdaptetFragmentTextBook.HolerFragmentTextBook> {

    private Context mContext;
    private List<TextBookInfo> mInfos;

    /**
     * 构造函数 初始化数据
     */
    public AdaptetFragmentTextBook(Context mContext, List<TextBookInfo> mInfos) {
        this.mContext = mContext;
        this.mInfos = mInfos;
    }

    /**
     * 创建HOLDER  和创建View
     */
    @Override
    public HolerFragmentTextBook onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = View.inflate(mContext, R.layout.adapter_fragment_text_book, null);

        return new HolerFragmentTextBook(view);
    }

    /**
     * 绑定数据
     */
    @Override
    public void onBindViewHolder(final HolerFragmentTextBook holder, int position) {
        holder.mRlItemBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null)
                    mListener.onClickItemTextBookListener(mInfos.get(holder.getLayoutPosition()).getFileAbs());
            }
        });

        holder.setViewData(position);
    }

    /**
     * 返回个数
     */
    @Override
    public int getItemCount() {
        if (mInfos != null) {
            return mInfos.size();
        }
        return 0;
    }

    /**
     * holder
     */
    public class HolerFragmentTextBook extends RecyclerView.ViewHolder {

        private final TextView mTvBookName;
        private final RelativeLayout mRlItemBook;
        private final ImageView mImgBookIcon;

        public HolerFragmentTextBook(View itemView) {
            super(itemView);
            mTvBookName = (TextView) itemView.findViewById(R.id.tv_book_name);
            mRlItemBook = (RelativeLayout) itemView.findViewById(R.id.rl_item_book);
            mImgBookIcon = (ImageView) itemView.findViewById(R.id.img_book_icon);

        }

        public void setViewData(int position) {
            if (mInfos != null && mInfos.size() > 0) {
                //设置书的名字
                String name = mInfos.get(position).getFileName().substring(0, mInfos.get(position).getFileName().length() - 4);
                mTvBookName.setText(name);
                loadImage(name, mImgBookIcon, 300, 300);
            }
        }
    }


    private ClickItemTextBookListener mListener;

    /**
     * 书籍  点击事件的监听
     */
    public interface ClickItemTextBookListener {

        public void onClickItemTextBookListener(String fileAbs);
    }

    /**
     * 点击事件
     */
    public void setListener(ClickItemTextBookListener listener) {
        this.mListener = listener;
    }

    /**
     * 加载图片
     */
    private void loadImage(String fileNmae, ImageView img, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        img.setImageResource(R.mipmap.yuanye);
        img.setTag(fileNmae);
        Bitmap bitmap = null;

        File file = new File(FileUtils.getTextBookIconFilesDir(MyApplication.getApplication()) + fileNmae);
        if (!file.exists()) {
            return;
        }
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getAbsolutePath(), options);

        int width = options.outWidth;
        int height = options.outHeight;

        int inSampleSize = 1;
        if (width > reqWidth || height > reqHeight) {
            int widthRadio = Math.round(width * 1.0f / reqWidth);
            int heightRadio = Math.round(height * 1.0f / reqHeight);

            inSampleSize = Math.max(widthRadio, heightRadio);
        }


        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);


        if (bitmap != null && img.getTag() != null && !bitmap.isRecycled()) {
            Object tag = img.getTag();
            if (tag instanceof String) {
                String str = (String) tag;
                if (StringUtils.isEquals(str, fileNmae)) {
                    img.setImageBitmap(bitmap);
                }
            }
        }
    }
}
