package com.rongjie.pdf.adapter;

import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.artifex.mupdfdemo.MuPDFCore;
import com.rongjie.leke.R;
import com.rongjie.pdf.View.MuPDFPageView;


public class MuPDFPageAdapter extends BaseAdapter {
    private final Context mContext;
    private final MuPDFCore mCore;
    private final SparseArray<PointF> mPageSizes = new SparseArray<PointF>();
    private String tag = "MuPDFPageAdapter.class";

    public MuPDFPageAdapter(Context c, MuPDFCore core) {
        mContext = c;
        mCore = core;
    }

    @Override
    public int getCount() {
        return mCore.countPages();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ImageButton imageView;
        final ViewGroup viewGroup;
        final MuPDFPageView pageView;
        final RelativeLayout relativeLayout;
        if (convertView == null) {
            viewGroup = (ViewGroup) View.inflate(mContext, R.layout.holder_pdf_view, null);
            pageView = new MuPDFPageView(mContext, mCore, new Point(parent.getWidth(), parent.getHeight()));
            viewGroup.addView(pageView, 0);
            relativeLayout = new RelativeLayout(mContext);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT
                    , FrameLayout.LayoutParams.MATCH_PARENT);

            relativeLayout.setLayoutParams(params);
            relativeLayout.setBackgroundColor(mContext.getResources().getColor(R.color.busy_indicator));
            imageView = new ImageButton(mContext);

            RelativeLayout.LayoutParams paramsTv = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

            imageView.setLayoutParams(paramsTv);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            relativeLayout.addView(imageView);
            viewGroup.addView(relativeLayout, 1);
        } else {
            viewGroup = (ViewGroup) convertView;
            pageView = (MuPDFPageView) viewGroup.getChildAt(0);
            relativeLayout = (RelativeLayout) viewGroup.getChildAt(1);
            imageView = (ImageButton) relativeLayout.getChildAt(0);
        }

        imageView.setImageResource(R.mipmap.yuanye);
        imageView.setVisibility(View.INVISIBLE);
        relativeLayout.setVisibility(View.INVISIBLE);

        PointF pageSize = mPageSizes.get(position);
        if (pageSize != null) {
            // We already know the page size. Set it up
            // immediately
            pageView.setPage(position, pageSize);
        } else { // 启动一个 后台线程  获取大小
            // Page size as yet unknown. Blank it for now, and
            // start a background task to find the size
            pageView.blank(position);
            AsyncTask<Void, Void, PointF> sizingTask = new AsyncTask<Void, Void, PointF>() {

                //在子线程 执行
                @Override
                protected PointF doInBackground(Void... voids) {
                    Log.i(tag, "doInBackground");
                    return mCore.getPageSize(position);
                }

                // 执行返回结果
                @Override
                protected void onPostExecute(PointF result) {
                    super.onPostExecute(result);
                    mPageSizes.put(position, result);
                    // Check that this view hasn't been reused for
                    // another page since we started
                    if (pageView.getPage() == position)
                        pageView.setPage(position, result);
                }

                // 执行之前 U线程 执行
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                }
            };
            sizingTask.execute((Void) null);
        }
        return viewGroup;
    }
}
