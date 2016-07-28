package com.rongjie.leke.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.artifex.mupdfdemo.MuPDFCore;
import com.artifex.mupdfdemo.OutlineItem;
import com.rongjie.leke.MyApplication;
import com.rongjie.leke.R;
import com.rongjie.pdf.View.MuPDFReaderView;
import com.rongjie.pdf.View.dialog.BookMarksDialog;
import com.rongjie.pdf.adapter.BookMarkAdapter;
import com.rongjie.pdf.adapter.MuPDFPageAdapter;
import com.rongjie.pdf.adapter.OutlineAdapter;
import com.rongjie.pdf.bean.BookMarkInfo;
import com.rongjie.pdf.bean.OutlineActivityData;
import com.rongjie.pdf.global.PdfParams;
import com.rongjie.pdf.utils.DateUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jiangliang on 2016/7/26.
 */
public class TextBookFragment1 extends BaseFragment implements AdapterView.OnItemClickListener, BookMarksDialog.DialogClickFinsihListener {

    private static final String TAG = "TextBookFragment";
    private MuPDFCore mCore;
    private String mFileName;
    private MuPDFReaderView mDocView;
    private ViewGroup mRlPdf;
    private ViewGroup mRlDirectory;
    private ListView mLvBookDirectory;
    private OutlineItem mOutlineItems[];
    private OutlineAdapter mBookAdapter;

    private LinearLayout bookNeedLayout;
    private RelativeLayout mRl_page;
    private SeekBar mSeekbarPage;
    private TextView mTvPageNumber;
    private Button mBackPageBtn;
    private Button mBookMarkBtn;
    private Button mItemDirectory;
    private ImageView mBookMarkImg;
    private ImageView mDirectoryImg;


    private int mPageSliderRes;
    private BookMarkAdapter mBookMarkAdapter;
    private String mStrUrl;

    private Map<Integer, BookMarkInfo> mBookMarks = new LinkedHashMap<>();
    //书签页码
    private int mbookMarksPage;
    private List<BookMarkInfo> mInfos = new ArrayList<>();

    /***
     * seekbar 前进后退
     */
    private Map<String, Integer> mProgresInfos = new HashMap<>();
    private String START = "START";
    private String END = "END";
    private boolean isBackPage = false;

    /**
     * 是否隐藏seekbar整体
     */
    private boolean mRlPageVisible = true;
    private View viewItemDirectory;
    private View viewItemBookmarks;

    @Override
    protected void initDatas() {
        super.initDatas();
        Intent intent = ((Activity) mContext).getIntent();
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            Uri uri = intent.getData();
            mStrUrl = Uri.decode(uri.getEncodedPath());
            mCore = openFile(mStrUrl);

            //PDF 没有内容
            if (mCore == null || mCore.countPages() == 0) {
                mCore = null;
            }

            if (null == mCore) {
                Toast.makeText(mContext, "当前PDF 没有内容", Toast.LENGTH_SHORT);
                return;
            }

            if (!mCore.fileFormat().startsWith("PDF")) { // 文件的格式
                Toast.makeText(mContext, "文件格式不是PDF", Toast.LENGTH_SHORT);
                return;
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG,"onStart..............");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.e(TAG,"hidden changed : " + hidden);
        if (hidden) {
            if (mFileName != null && mDocView != null) {
                SharedPreferences prefs = ((Activity) mContext).getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = prefs.edit();
                edit.putInt("page" + mFileName, mDocView.getDisplayedViewIndex());
                edit.apply();
            }
        }
    }

    @Override
    public void onDestroy() {
        if (mCore != null) {
            mCore.onDestroy();
        }
        mCore = null;
        super.onDestroy();
    }

    /**
     * 初始化UI
     */
    @Override
    protected void initOtherView() {
        super.initOtherView();
        Log.e(TAG,"initOtherView............");
        mStub.setLayoutResource(R.layout.pdf_layout);
        mStub.inflate();
        mTextbookIv.setEnabled(false);
        initDocView();
        MuPDFPageAdapter adapter = new MuPDFPageAdapter(mContext, mCore);
        mDocView.setAdapter(adapter);
        adapter.setTextBookFragment(this);
        SharedPreferences prefs = ((Activity) mContext).getPreferences(Context.MODE_PRIVATE);
        mbookMarksPage = prefs.getInt("page" + mFileName, 0) + 1;
        mDocView.setDisplayedViewIndex(prefs.getInt("page" + mFileName, 0));
        /**
         * 需要设置内容
         */
        mRlPdf = (ViewGroup) mRoot.findViewById(R.id.rl_pdf);
        mRlPdf.addView(mDocView, 0);

        bookNeedLayout = (LinearLayout) mRoot.findViewById(R.id.book_need_layout);
        bookNeedLayout.setVisibility(View.VISIBLE);

        viewItemDirectory = mRoot.findViewById(R.id.view_item_directory);
        viewItemBookmarks = mRoot.findViewById(R.id.view_item_bookmarks);
        mRlDirectory = (ViewGroup) mRoot.findViewById(R.id.rl_directory);
        mLvBookDirectory = (ListView) mRoot.findViewById(R.id.lv_item_book_directory);
        mLvBookDirectory.setDividerHeight(0);
        mBookMarkBtn = (Button) mRoot.findViewById(R.id.btn_item_bookmarks);
        mItemDirectory = (Button) mRoot.findViewById(R.id.btn_item_directory);
        //seek
        mRl_page = (RelativeLayout) mRoot.findViewById(R.id.rl_page);
        mSeekbarPage = (SeekBar) mRoot.findViewById(R.id.seekbar_page);
        mTvPageNumber = (TextView) mRoot.findViewById(R.id.tv_page_number);

        mBookMarkImg = (ImageView) mRoot.findViewById(R.id.bookmark);
        mDirectoryImg = (ImageView) mRoot.findViewById(R.id.directory);

        initPageNumber();
        if (mBookMarks.containsKey(mbookMarksPage)) {
            mBookMarkBtn.setBackgroundColor(mContext.getResources().getColor(R.color.red));
        } else {
            mBookMarkBtn.setBackgroundColor(mContext.getResources().getColor(R.color.seek_thumb));
        }
        mBackPageBtn = (Button) mRoot.findViewById(R.id.btn_back_page);
        mBackPageBtn.setEnabled(false);

    }

    @Override
    protected void addListener() {
        super.addListener();
        mBookMarkBtn.setOnClickListener(this);
        mItemDirectory.setOnClickListener(this);
        mBackPageBtn.setOnClickListener(this);
        mRlDirectory.setOnClickListener(this);
        mBookMarkImg.setOnClickListener(this);
        mDirectoryImg.setOnClickListener(this);
        mLvBookDirectory.setOnItemClickListener(this);
        int smax = Math.max(mCore.countPages() - 1, 1);
        mPageSliderRes = ((10 + smax - 1) / smax) * 2;
        mSeekbarPage.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) {
                //设置进度
                mProgresInfos.put(END, seekBar.getProgress());
                //设置按钮可以点击
                mBackPageBtn.setEnabled(true);
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

    /***
     * 初始化 用来显示PDF的View
     *
     * @return
     */
    private void initDocView() {
        mDocView = new MuPDFReaderView((Activity) mContext) {
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
                        mBookMarkBtn.setBackgroundColor(mContext.getResources().getColor(R.color.red));
                    } else {
                        mBookMarkBtn.setBackgroundColor(mContext.getResources().getColor(R.color.seek_thumb));
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


    private MuPDFCore openFile(String path) {
        MuPDFCore core = null;
        int lastSlashPos = path.lastIndexOf('/');
        mFileName = lastSlashPos == -1 ? path : path.substring(lastSlashPos + 1);
        PdfParams.CURRENT_PDF_FILE_NAME = mFileName.substring(0, mFileName.length() - 4);
        try {
            // 解析PDF 核心类
            core = new MuPDFCore(mContext, path);
            //删除 PDF 目录 ，需要回复数据
            OutlineActivityData.set(null);
        } catch (Exception e) {
            System.out.println(e);
        }
        return core;
    }

    @Override
    public void onClick(View view) {
        parent = mDocView.getCurrentItem();
        initNoteView();
        mDocView.setInterceptTouch(false);
        switch (view.getId()) {
            case R.id.gesture:
                mDocView.setInterceptTouch(true);
                break;
            case R.id.btn_item_directory:
                clickDirectoryBtn();
                break;
            case R.id.btn_item_bookmarks:
                clickBookMarkBtn();
                break;
            case R.id.rl_directory:
                mRlDirectory.setVisibility(View.GONE);
                break;
            case R.id.btn_back_page:
                clickBackBtn();
                break;
            case R.id.bookmark:
                view = mBookMarkerIv;
                showMarkDialog();
                break;
            case R.id.directory:
                view = mDirectoryIv;
                showDirectory();
                break;
        }
        super.onClick(view);
    }

    private void clickBackBtn() {
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

    private void clickBookMarkBtn() {
        setMarkAndDirectoryClickTextChange(false);
        System.out.println("btn_item_bookmarks");
        //刷新数据
        mInfos.clear();
        if (mBookMarks.size() > 0) {
            mInfos.addAll(new ArrayList<>(mBookMarks.values()));
        }
        if (mBookMarkAdapter == null) {
            mBookMarkAdapter = new BookMarkAdapter(mInfos, mContext);
        }
        mLvBookDirectory.setAdapter(mBookMarkAdapter);
        mBookMarkAdapter.notifyDataSetChanged();
    }

    private void clickDirectoryBtn() {
        setMarkAndDirectoryClickTextChange(true);
        // 增加判断 当获取目录为空
        if (mOutlineItems != null && mOutlineItems.length > 0 && mBookAdapter != null) {
            mLvBookDirectory.setAdapter(mBookAdapter);
            mBookAdapter.notifyDataSetChanged();
            mLvBookDirectory.setSelection(OutlineActivityData.get().position);
        } else {
            mBookAdapter = new OutlineAdapter(((Activity) mContext).getLayoutInflater(), mOutlineItems);
            mLvBookDirectory.setAdapter(mBookAdapter);
            mBookAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 设置目录和书签 文本变化
     */
    private void setMarkAndDirectoryClickTextChange(boolean isDirectory) {
        int blackColor = MyApplication.getInstance().getResources().getColor(R.color.black);
        int blueColor = MyApplication.getInstance().getResources().getColor(R.color.blue);

        viewItemDirectory.setVisibility(isDirectory ? View.VISIBLE : View.INVISIBLE);
        viewItemBookmarks.setVisibility(!isDirectory ? View.VISIBLE : View.INVISIBLE);
        mBookMarkBtn.setTextColor(isDirectory ? blueColor : blackColor);
        mItemDirectory.setTextColor(!isDirectory ? blueColor : blackColor);

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


    private BookMarksDialog mBookMarkDialog;

    public void showBookMarksDialog(String tag, BookMarksDialog.DialogMode dialogMode, String msg) {

        if (null == mBookMarkDialog) {
            mBookMarkDialog = new BookMarksDialog(mContext);
            mBookMarkDialog.setListener(this);
        }


        if (!mBookMarkDialog.isShowing()) {
            mBookMarkDialog.show();
            mBookMarkDialog.setTag(tag);
            mBookMarkDialog.setDialogMode(dialogMode);
            mBookMarkDialog.setEditBookMarksContent(msg);
        }
    }

    //dialog点击按钮
    @Override
    public void onDialogConfirmCancleClick(BookMarksDialog dialog, BookMarksDialog.ClickState clickState) {

        if (clickState == BookMarksDialog.ClickState.IMG_BTN_CLOSE_CLICKED) {
            System.out.println("IMG_BTN_CLOSE_CLICKED");

        } else if (clickState == BookMarksDialog.ClickState.BTN_CHANGE_CLICKED) {

            System.out.println("BTN_CHANGE_CLICKED");
            System.out.println("getEditBookMarksContent == " + dialog.getEditBookMarksContent());
            String text = "";
            if (TextUtils.isEmpty(dialog.getEditBookMarksContent())) {
                text = "书签" + mbookMarksPage;
            } else {
                text = dialog.getEditBookMarksContent();
            }

            mBookMarks.get(mbookMarksPage).setMarkName(text);
            mBookMarks.get(mbookMarksPage).setNumber(mbookMarksPage);
            mBookMarks.get(mbookMarksPage).setCreatTime(DateUtils.getCalendarAndTimeString());

        } else if (clickState == BookMarksDialog.ClickState.BTN_DELETE_CLICKED) {

            System.out.println("BTN_DELETE_CLICKED");

            mBookMarks.remove(mbookMarksPage);
        } else if (clickState == BookMarksDialog.ClickState.BTN_ADD_CLICKED) {

            System.out.println("BTN_ADD_CLICKED");
            System.out.println("getEditBookMarksContent == " + dialog.getEditBookMarksContent());
            String text = "";
            if (TextUtils.isEmpty(dialog.getEditBookMarksContent())) {
                text = "书签" + mbookMarksPage;
            } else {
                text = dialog.getEditBookMarksContent();
            }

            BookMarkInfo bookMarkInfo = new BookMarkInfo();
            bookMarkInfo.setMarkName(text);
            bookMarkInfo.setNumber(mbookMarksPage);
            bookMarkInfo.setCreatTime(DateUtils.getCalendarAndTimeString());
            mBookMarks.put(mbookMarksPage, bookMarkInfo);
        }

        if (mBookMarks.containsKey(mbookMarksPage)) {
            mBookMarkBtn.setBackgroundColor(mContext.getResources().getColor(R.color.red));
        } else {
            mBookMarkBtn.setBackgroundColor(mContext.getResources().getColor(R.color.seek_thumb));
        }
        dialog.dismiss();
    }


    /**
     * 设置对话框 添加和修改标签
     */
    public void showMarkDialog() {
        if (mBookMarks.containsKey(mbookMarksPage)) {
            showBookMarksDialog("btn_bookmarks", BookMarksDialog.DialogMode.CHANGE_OR_DELETE, mBookMarks.get(mbookMarksPage).getMarkName());
        } else {
            showBookMarksDialog("btn_bookmarks", BookMarksDialog.DialogMode.ADD, "添加标签" + mbookMarksPage);
        }
    }


    /***
     * 点击目录
     */
    public void showDirectory() {
        // 第一次进来 先获取 目录 ，以后就不需执行
        if (mOutlineItems == null || mOutlineItems.length == 0) {
            // 获取书的目录
            OutlineItem outline[] = mCore.getOutline();
            if (outline != null) {
                // 设置 书的目录数据
                OutlineActivityData.get().items = outline;
                mOutlineItems = OutlineActivityData.get().items;
                if (mBookAdapter == null) {
                    mBookAdapter = new OutlineAdapter(((Activity) mContext).getLayoutInflater(), mOutlineItems);
                }
            }
        }
        // TODO: 增加判断 当获取目录为空
        if (mOutlineItems != null && mOutlineItems.length > 0 && mBookAdapter != null) {
            mLvBookDirectory.setAdapter(mBookAdapter);
            mBookAdapter.notifyDataSetChanged();
        }
        mRlDirectory.setVisibility(View.VISIBLE);
        if (mRlPageVisible) {
            mRlPageVisible = false;
            hideSeekbarLayout();
        }
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
                mBackPageBtn.setEnabled(false);
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
                    mBackPageBtn.setEnabled(true);
                }
            }
        });
        mRl_page.startAnimation(anim);
    }


}
