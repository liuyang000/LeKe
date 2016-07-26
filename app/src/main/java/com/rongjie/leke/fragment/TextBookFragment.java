package com.rongjie.leke.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.artifex.mupdfdemo.MuPDFCore;
import com.artifex.mupdfdemo.OutlineItem;
import com.rongjie.leke.R;
import com.rongjie.pdf.View.MuPDFReaderView;
import com.rongjie.pdf.adapter.BookMarkAdapter;
import com.rongjie.pdf.adapter.OutlineAdapter;
import com.rongjie.pdf.bean.BookMarkInfo;
import com.rongjie.pdf.bean.OutlineActivityData;
import com.rongjie.pdf.global.PdfParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jiangliang on 2016/7/11.
 */
public class TextBookFragment extends com.rongjie.leke.fragment.BaseFragment implements AdapterView.OnItemClickListener {

    private ImageView bookMark;
    private ImageView directory;
    private LinearLayout bookNeedLayout;
    private ViewGroup mRlDirectory;
    private ListView mLvBookDirectory;
    private MuPDFReaderView mDocView;
    private Button bookMarkBtn;

    private OutlineItem mOutlineItems[];
    private String mFileName;
    private String mStrUrl;
    private MuPDFCore mCore;

    private int mPageSliderRes;
    //书签页码
    private int mbookMarksPage;
    private List<BookMarkInfo> mInfos = new ArrayList<>();
    private boolean mRlPageVisible = true;
    private Map<Integer, BookMarkInfo> mBookMarks = new LinkedHashMap<>();

    /***
     * seekbar 前进后退
     */
    private Map<String, Integer> mProgresInfos = new HashMap<>();
    private String START = "START";
    private String END = "END";
    private boolean isBackPage = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.activity_mupdf, container, false);
        return root;
    }

    @Override
    protected void initOtherView() {
        super.initOtherView();
        bookMark = (ImageView) root.findViewById(R.id.bookmark);
        directory = (ImageView) root.findViewById(R.id.directory);
        bookNeedLayout = (LinearLayout) root.findViewById(R.id.book_need_layout);
        bookNeedLayout.setVisibility(View.VISIBLE);
        mRlDirectory = (ViewGroup) root.findViewById(R.id.rl_directory);

        mLvBookDirectory = (ListView) root.findViewById(R.id.lv_item_book_directory);
        mLvBookDirectory.setDividerHeight(0);
        mLvBookDirectory.setOnItemClickListener(this);

        bookMarkBtn = (Button) root.findViewById(R.id.btn_item_bookmarks);
        initPageNumber();
        initDocView();
    }

    @Override
    protected void addListener() {
        super.addListener();
        bookMark.setOnClickListener(this);
        directory.setOnClickListener(this);
        mRlDirectory.setOnClickListener(this);
        mSeekbarPage.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) {
                //设置进度
                mProgresInfos.put(END, seekBar.getProgress());
                //设置按钮可以点击
                backBtn.setEnabled(true);
                isBackPage = true;
                mDocView.setDisplayedViewIndex((seekBar.getProgress() + mPageSliderRes / 2) / mPageSliderRes);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                mProgresInfos.put(START, seekBar.getProgress());
            }

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updatePageNumView((progress + mPageSliderRes / 2) / mPageSliderRes);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back_page:
                back();
                break;
            case R.id.bookmark:
                view = bookMarker;
                break;
            case R.id.directory:
                view = directory;
                break;
        }
        super.onClick(v);
    }

    private void initDocView() {
        mDocView = new MuPDFReaderView((Activity) context) {
            @Override
            protected void onMoveToChild(int i) {
                if (mCore != null && mTvPageNumber != null) {
                    //更新seek
                    System.out.println("onMoveToChild i+===" + i);
                    mTvPageNumber.setText(String.format("%d / %d", i + 1, mCore.countPages()));
                    mSeekbarPage.setMax((mCore.countPages() - 1) * mPageSliderRes);
                    mSeekbarPage.setProgress(i * mPageSliderRes);
                    //更新书签 ,设置按钮颜色
                    mbookMarksPage = i + 1;
                    if (mBookMarks.containsKey(mbookMarksPage)) {
                        bookMarkBtn.setBackgroundColor(context.getResources().getColor(R.color.red));
                    } else {
                        bookMarkBtn.setBackgroundColor(context.getResources().getColor(R.color.seek_thumb));
                    }
                }
            }

            //TODO:增加 单单击按钮 隐藏和显示seek
            @Override
            protected void onTapMainDocArea() {
                super.onTapMainDocArea();

                if (mRlPageVisible) {
                    hideSeekbarLayout();
                } else {
                    showSeekbarLayout();
                }
                mRlPageVisible = !mRlPageVisible;
            }
        };
    }

    private void initPageNumber() {
        if (mCore == null)
            return;
        // 获取 当前显示的 PDF 角标
        final int index = mDocView.getDisplayedViewIndex();
        // 更新显示
        updatePageNumView(index);
        System.out.println("(mCore.countPages() - 1) * mPageSliderRes ======" + (mCore.countPages() - 1) * mPageSliderRes);
        mSeekbarPage.setMax((mCore.countPages() - 1) * mPageSliderRes);
        mSeekbarPage.setProgress(index * mPageSliderRes);
    }

    private void updatePageNumView(int index) {
        if (mCore == null)
            return;
        mTvPageNumber.setText(String.format("%d / %d", index + 1, mCore.countPages()));
    }

    @Override
    protected void initDatas() {
        super.initDatas();

        int smax = Math.max(mCore.countPages() - 1, 1);
        mPageSliderRes = ((10 + smax - 1) / smax) * 2;

        SharedPreferences prefs = ((Activity) context).getPreferences(Context.MODE_PRIVATE);
        mbookMarksPage = prefs.getInt("page" + mFileName, 0) + 1;

        Intent intent = ((Activity) context).getIntent();
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            Uri uri = intent.getData();
            mStrUrl = Uri.decode(uri.getEncodedPath());
            mCore = openFile(mStrUrl);

            //PDF 没有内容
            if (mCore == null && mCore.countPages() == 0) {
                mCore = null;
            }

            if (null == mCore) {
                Toast.makeText(context, "当前PDF 没有内容", Toast.LENGTH_SHORT);
                return;
            }

            if (!mCore.fileFormat().startsWith("PDF")) { // 文件的格式
                Toast.makeText(context, "文件格式不是PDF", Toast.LENGTH_SHORT);
                return;
            }
        }
    }

    private MuPDFCore openFile(String path) {
        MuPDFCore core = null;

        int lastSlashPos = path.lastIndexOf('/');
        mFileName = lastSlashPos == -1 ? path : path.substring(lastSlashPos + 1);
        System.out.println("Trying to open " + path);
        PdfParams.CURRENT_PDF_FILE_NAME = mFileName.substring(0, mFileName.length() - 4);
        try {
            // 解析PDF 核心类
            core = new MuPDFCore(context, path);
            // New file: drop the old outline data
            //删除 PDF 目录 ，需要回复数据
            OutlineActivityData.set(null);
        } catch (Exception e) {
            System.out.println(e);
        }
        return core;
    }

    //TODO:增加 seeker 动画
    private void hideSeekbarLayout() {
        mRl_page.clearAnimation();
        TranslateAnimation anim = new TranslateAnimation(0, 0, 0, mRl_page.getHeight());
        anim.setDuration(200);
        anim.setFillAfter(true);
        anim.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                mSeekbarPage.setEnabled(false);
                backBtn.setEnabled(false);
            }
        });
        mRl_page.startAnimation(anim);
    }

    //TODO:增加 seeker 动画
    private void showSeekbarLayout() {
        mRl_page.clearAnimation();
        TranslateAnimation anim = new TranslateAnimation(0, 0, mRl_page.getHeight(), 0);
        anim.setDuration(200);
        anim.setFillAfter(true);
        anim.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                mSeekbarPage.setEnabled(true);
                if (isBackPage) {
                    backBtn.setEnabled(true);
                }
            }
        });
        mRl_page.startAnimation(anim);
    }

    private void back() {
        int progress;
        if (!isBackPage) {
            isBackPage = true;
            progress = mProgresInfos.get(END);
        } else {
            isBackPage = false;
            progress = mProgresInfos.get(START);
        }
        mDocView.setDisplayedViewIndex((progress + mPageSliderRes / 2) / mPageSliderRes);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        bookNeedLayout.setVisibility(View.VISIBLE);
        textbook.setEnabled(false);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mRlDirectory.setVisibility(View.GONE);
        if (parent.getAdapter() instanceof OutlineAdapter) {
            System.out.println("onItemClick == OutlineAdapter");
            // 保持用户可以看见的 位置 ，因为一个目录会有可能出现很长
            OutlineActivityData.get().position = mLvBookDirectory.getFirstVisiblePosition();
            if (position >= 0) {
                mDocView.setDisplayedViewIndex(mOutlineItems[position].page);
            }

        } else if (parent.getAdapter() instanceof BookMarkAdapter) {
            System.out.println("onItemClick == BookMarkAdapter");
            System.out.println("markNumber == mInfos.get(position).getNumber()");
            mDocView.setDisplayedViewIndex(mInfos.get(position).getNumber() - 1);
        }
    }
}
