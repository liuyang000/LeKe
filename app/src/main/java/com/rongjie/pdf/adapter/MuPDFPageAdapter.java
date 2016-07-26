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

import com.artifex.mupdfdemo.MuPDFCore;
import com.rongjie.leke.OptionConlection;
import com.rongjie.leke.R;
import com.rongjie.leke.fragment.TextBookFragment1;
import com.rongjie.leke.view.MyFrameLayout;
import com.rongjie.pdf.View.MuPDFPageView;


public class MuPDFPageAdapter extends BaseAdapter {
    private final Context mContext;
    private final MuPDFCore mCore;
    private final SparseArray<PointF> mPageSizes = new SparseArray<PointF>();
    private String tag = "MuPDFPageAdapter.class";
    private TextBookFragment1 fragment;

    public void setTextBookFragment(TextBookFragment1 fragment) {
        this.fragment = fragment;
    }

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

    private OptionConlection option;

    public void setOptionConlection(OptionConlection option) {
        this.option = option;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewGroup viewGroup;
        final MuPDFPageView pageView;
        final MyFrameLayout frameLayout;
        if (convertView == null) {
            viewGroup = (ViewGroup) View.inflate(mContext, R.layout.holder_pdf_view, null);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT
                    , FrameLayout.LayoutParams.MATCH_PARENT);
            pageView = new MuPDFPageView(mContext, mCore, new Point(parent.getWidth(), parent.getHeight()));
            pageView.setLayoutParams(params);
            viewGroup.addView(pageView, 0);

            frameLayout = (MyFrameLayout) View.inflate(mContext, R.layout.content_layout, null);
            frameLayout.setLayoutParams(params);
            viewGroup.addView(frameLayout, 1);
        } else {
            viewGroup = (ViewGroup) convertView;
            pageView = (MuPDFPageView) viewGroup.getChildAt(0);
            frameLayout = (MyFrameLayout) viewGroup.getChildAt(1);
        }
//        if (null != option) {
//            option.setRootView(frameLayout);
//            option.initNoteView();
//        }
        if (null != fragment) {
            fragment.setParentOfNoteView(frameLayout);
            fragment.initNoteView();
        }

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
