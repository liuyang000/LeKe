package com.rongjie.pdf;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.artifex.mupdfdemo.MuPDFCore;
import com.artifex.mupdfdemo.OutlineItem;
import com.rongjie.leke.OptionConlection;
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
 * 用来 显示PDF 的UI 主界面
 */
public class MuPDFActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener, BookMarksDialog.DialogClickFinsihListener {
    /**
     * 核心操作
     */
    private MuPDFCore mCore;
    private String mFileName;
    private Context mContext;
    private MuPDFReaderView mDocView;
    private ViewGroup mRlPdf;
//    private Button mBtnHand;
//    private Button mBtnPen;
//    private Button mBtnDirectory;
//    private Button mBtnItemDirectory;
//    private Button mBtnItemBookmarks;
//    private Button mBtnItemNotes;
    private ViewGroup mRlDirectory;
    private ListView mLvBookDirectory;
    private OutlineItem mOutlineItems[];
    private OutlineAdapter mBookAdapter;

    private BookMarkAdapter mBookMarkAdapter;
    private RelativeLayout mRl_page;
    private SeekBar mSeekbarPage;
    private TextView mTvPageNumber;
    private int mPageSliderRes;

    private String mStrUrl;
//    private Button mBtn_bookmarks;

    private Map<Integer, BookMarkInfo> mBookMarks = new LinkedHashMap<Integer, BookMarkInfo>();

    //书签页码
    private int mbookMarksPage;
    private List<BookMarkInfo> mInfos = new ArrayList<BookMarkInfo>();
    private Button bookPackage;
    private Button mbtn_more_view;
    private Button btn_back_page;
//    private int mStartProgress;

    private OptionConlection optionInstance;

    private LinearLayout paintChoose;
    private int chooserHeight;
    private static final String PROPERTY_NAME = "translationY";
//    private Button mBtn_bookmarks;

    /***
     * seekbar 前进后退
     */
    private Map<String, Integer> mProgresInfos = new HashMap<String, Integer>();
    private String START = "START";
    private String END = "END";
    private boolean isBackPage = false;

    /**
     * 是否隐藏seekbar整体
     */
    private boolean mRlPageVisible = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        initData();
    }


    private void initData() {
        Intent intent = getIntent();
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            Uri uri = intent.getData();
            mStrUrl = Uri.decode(uri.getEncodedPath());
            mCore = openFile(mStrUrl);

            //PDF 没有内容
            if (mCore == null && mCore.countPages() == 0) {
                System.out.println("aaaaaaaaaaaaaa");
                mCore = null;
            }

            if (null == mCore) {
                System.out.println("bbbbbbbbbb");
                Toast.makeText(getApplicationContext(), "当前PDF 没有内容", Toast.LENGTH_SHORT);
                return;
            }

            if (!mCore.fileFormat().startsWith("PDF")) { // 文件的格式
                Toast.makeText(getApplicationContext(), "文件格式不是PDF", Toast.LENGTH_SHORT);
                return;
            }
            //初始化UI
            initLayout();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mFileName != null && mDocView != null) {
            SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = prefs.edit();
            edit.putInt("page" + mFileName, mDocView.getDisplayedViewIndex());
            edit.commit();
        }
    }

    @Override
    protected void onDestroy() {
        if (mCore != null) {
            mCore.onDestroy();
        }
        mCore = null;
        super.onDestroy();
    }

    /**
     * 初始化UI
     */
    private void initLayout() {
        setContentView(R.layout.activity_mupdf);
        optionInstance = new OptionConlection(this);
        optionInstance.init();
        initDocView();
        optionInstance.setMuPDFReaderView(mDocView);

        System.out.println("bbbbbbbbbbbbb");
        MuPDFPageAdapter adapter = new MuPDFPageAdapter(this, mCore);
        mDocView.setAdapter(adapter);
        SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
        mbookMarksPage = prefs.getInt("page" + mFileName, 0) + 1;

        mDocView.setDisplayedViewIndex(prefs.getInt("page" + mFileName, 0));

//        mDocView.setDisplayedViewIndex(10);

        /**
         * 需要设置内容
         */
        mRlPdf = (ViewGroup) this.findViewById(R.id.rl_pdf);
        mRlPdf.addView(mDocView, 0);


        adapter.setOptionConlection(optionInstance);
        mRlDirectory = (ViewGroup) this.findViewById(R.id.rl_directory);
        mRlDirectory.setOnClickListener(this);
        mLvBookDirectory = (ListView) this.findViewById(R.id.lv_item_book_directory);
        mLvBookDirectory.setDividerHeight(0);
        mLvBookDirectory.setOnItemClickListener(this);

        //seek
        mRl_page = (RelativeLayout) this.findViewById(R.id.rl_page);
        mSeekbarPage = (SeekBar) this.findViewById(R.id.seekbar_page);
        mTvPageNumber = (TextView) this.findViewById(R.id.tv_page_number);
        mRl_page = (RelativeLayout) this.findViewById(R.id.rl_page);
        mSeekbarPage = (SeekBar) this.findViewById(R.id.seekbar_page);
        mTvPageNumber = (TextView) this.findViewById(R.id.tv_page_number);
        paintChoose = (LinearLayout) this.findViewById(R.id.paint_choose);
        int smax = Math.max(mCore.countPages() - 1, 1);
        mPageSliderRes = ((10 + smax - 1) / smax) * 2;
        mSeekbarPage.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) {
                //设置进度
                mProgresInfos.put(END, seekBar.getProgress());
                //设置按钮可以点击
                btn_back_page.setEnabled(true);
                isBackPage = true;
                mDocView.setDisplayedViewIndex((seekBar.getProgress() + mPageSliderRes / 2) / mPageSliderRes);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
//                mStartProgress = seekBar.getProgress();
                mProgresInfos.put(START, seekBar.getProgress());
            }

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updatePageNumView((progress + mPageSliderRes / 2) / mPageSliderRes);
            }
        });

        initPageNumber();

      /*  mBtn_bookmarks = (Button) this.findViewById(R.id.btn_bookmarks);
        mBtn_bookmarks.setOnClickListener(this);*/

        if (mBookMarks.containsKey(mbookMarksPage)) {
            optionInstance.getViewBookMarker().setBackgroundColor(MuPDFActivity.this.getResources().getColor(R.color.red));
        } else {
            optionInstance.getViewBookMarker().setBackgroundColor(MuPDFActivity.this.getResources().getColor(R.color.transparent));
        }

//        if (mBookMarks.containsKey(mbookMarksPage)) {
//            mBtn_bookmarks.setBackgroundColor(MuPDFActivity.this.getResources().getColor(R.color.red));
//        } else {
//            mBtn_bookmarks.setBackgroundColor(MuPDFActivity.this.getResources().getColor(R.color.seek_thumb));
//        }

        btn_back_page = (Button) this.findViewById(R.id.btn_back_page);
        btn_back_page.setOnClickListener(this);
        btn_back_page.setEnabled(false);

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            optionInstance.insertPic(data);
        }
    }


    /***
     * 初始化 用来显示PDF的View
     *
     * @return
     */
    private void initDocView() {
        mDocView = new MuPDFReaderView(this) {
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
//                    if (mBookMarks.containsKey(mbookMarksPage)) {
//                        mBtn_bookmarks.setBackgroundColor(MuPDFActivity.this.getResources().getColor(R.color.red));
//                    } else {
//                        mBtn_bookmarks.setBackgroundColor(MuPDFActivity.this.getResources().getColor(R.color.seek_thumb));
//                    }
                    if (mBookMarks.containsKey(mbookMarksPage)) {
                        optionInstance.getViewBookMarker().setBackgroundColor(MuPDFActivity.this.getResources().getColor(R.color.red));
                    } else {
                        optionInstance.getViewBookMarker().setBackgroundColor(MuPDFActivity.this.getResources().getColor(R.color.transparent));
                    }
                }
            }

            //TODO:增加 单单击按钮 隐藏和显示seek
            @Override
            protected void onTapMainDocArea() {
                super.onTapMainDocArea();

                if (mRlPageVisible) {
                    mRlPageVisible = false;
                    //隐藏
                    hideSeekbarLayout();

                } else {
                    mRlPageVisible = true;
                    //显示
                    showSeekbarLayout();
                }
            }
        };
    }


    private MuPDFCore openFile(String path) {
        MuPDFCore core = null;

        int lastSlashPos = path.lastIndexOf('/');
        mFileName = lastSlashPos == -1 ? path : path.substring(lastSlashPos + 1);
        System.out.println("Trying to open " + path);
        PdfParams.CURRENT_PDF_FILE_NAME = mFileName.substring(0, mFileName.length() - 4);


        try {
            // 解析PDF 核心类
            core = new MuPDFCore(this, path);
            // New file: drop the old outline data
            //删除 PDF 目录 ，需要回复数据
            OutlineActivityData.set(null);
        } catch (Exception e) {
            System.out.println(e);
        }
        return core;
    }

    @Override
    public void onClick(View view) {
//        switch (view.getId()) {

//        switch (view.getId()) {
//            case R.id.gesture:
//                mDocView.setInterceptTouch(false);
//                mBtnDirectory.setEnabled(true);
//                break;
//
//            case R.id.paint_draw:
//                mBtnDirectory.setEnabled(false);
////                mDocView.refresh(false);
//                mDocView.setInterceptTouch(true);
////                chooserHeight = paintChoose.getHeight();
////                if (paintChoose.getVisibility() == View.VISIBLE) {
////                    outAnimator();
////                } else {
////                    inAnimatior();
////                }
//                break;
//
//            case R.id.btn_directory:
//                // 第一次进来 先获取 目录 ，以后就不需执行
//                if (mOutlineItems == null || mOutlineItems.length == 0) {
//                    // 获取书的目录
//                    OutlineItem outline[] = mCore.getOutline();
//                    if (outline != null) {
//                        // 设置 书的目录数据
//                        OutlineActivityData.get().items = outline;
//                        mOutlineItems = OutlineActivityData.get().items;
//                        if (mBookAdapter == null) {
//                            mBookAdapter = new OutlineAdapter(getLayoutInflater(), mOutlineItems);
//                            ;
//                        }
//                    }
//                }
//
//                mLvBookDirectory.setAdapter(mBookAdapter);
//                mBookAdapter.notifyDataSetChanged();
//                mRlDirectory.setVisibility(View.VISIBLE);
//                break;
//
//            case R.id.btn_item_directory:
//                mLvBookDirectory.setAdapter(mBookAdapter);
//                mBookAdapter.notifyDataSetChanged();
//                mLvBookDirectory.setSelection(OutlineActivityData.get().position);
//                break;
//            case R.id.btn_item_bookmarks:
//                System.out.println("btn_item_bookmarks");
//                //刷新数据
//                mInfos.clear();
//                if (mBookMarks.size() > 0) {
//                    mInfos.addAll(new ArrayList<>(mBookMarks.values()));
//                }
//                if (mBookMarkAdapter == null) {
//                    mBookMarkAdapter = new BookMarkAdapter(mInfos, MuPDFActivity.this);
//                }
//                mLvBookDirectory.setAdapter(mBookMarkAdapter);
//                mBookMarkAdapter.notifyDataSetChanged();
//                break;
//            case R.id.rl_directory:
//                mRlDirectory.setVisibility(View.GONE);
//                break;
//
//            case R.id.btn_bookmarks:
//                if (mBookMarks.containsKey(mbookMarksPage)) {
//                    showBookMarksDialog("btn_bookmarks", BookMarksDialog.DialogMode.CHANGE_OR_DELETE, mBookMarks.get(mbookMarksPage).getMarkName());
//                } else {
//                    showBookMarksDialog("btn_bookmarks", BookMarksDialog.DialogMode.ADD, "添加标签" + mbookMarksPage);
//                }
//                break;
//            case R.id.btn_item_directory:
//                // TODO: 增加判断 当获取目录为空
//                if (mOutlineItems != null && mOutlineItems.length > 0 && mBookAdapter != null) {
//                    mLvBookDirectory.setAdapter(mBookAdapter);
//                    mBookAdapter.notifyDataSetChanged();
//                    mLvBookDirectory.setSelection(OutlineActivityData.get().position);
//                } else {
//                    mBookAdapter = new OutlineAdapter(getLayoutInflater(), mOutlineItems);
//                    mLvBookDirectory.setAdapter(mBookAdapter);
//                    mBookAdapter.notifyDataSetChanged();
//                }
//                break;
//
//            case R.id.btn_item_bookmarks:
//                System.out.println("btn_item_bookmarks");
//                //刷新数据
//                mInfos.clear();
//                if (mBookMarks.size() > 0) {
//                    mInfos.addAll(new ArrayList<BookMarkInfo>(mBookMarks.values()));
//                }
//                if (mBookMarkAdapter == null) {
//                    mBookMarkAdapter = new BookMarkAdapter(mInfos, MuPDFActivity.this);
//                }
//                mLvBookDirectory.setAdapter(mBookMarkAdapter);
//                mBookMarkAdapter.notifyDataSetChanged();
//                break;
//
//            case R.id.btn_item_notes:
//                break;
//
//            case R.id.rl_directory:
//                mRlDirectory.setVisibility(View.GONE);
//                break;
//
            //  case R.id.btn_bookmarks:

//            case R.id.book_package:
//                /*Intent intent = new Intent(this, ActivityMainUI.class);
//                startActivity(intent);*/
//                this.finish();
//                break;
//
//            case R.id.btn_more_view:
//               //获取到 所有的文件 包括目录
//                Uri uri = Uri.parse(mStrUrl);
//                Intent intent1 = new Intent(this, TestTwoPDF.class);
//                intent1.setAction(Intent.ACTION_VIEW);
//                intent1.setData(uri);
//                startActivity(intent1);
//                break;
//
//            case R.id.btn_back_page:
//                //TODO:增加前进后退的 按钮事件
//                int progress;
//                if (!isBackPage) {
//                    isBackPage = true;
//                    progress = mProgresInfos.get(END);
//                } else {
//                    isBackPage = false;
//                    progress = mProgresInfos.get(START);
//                }
//
//                mDocView.setDisplayedViewIndex((progress + mPageSliderRes / 2) / mPageSliderRes);
//                break;
//        }
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
            mBookMarkDialog = new BookMarksDialog(this);
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

//        if (mBookMarks.containsKey(mbookMarksPage)) {
//            mBtn_bookmarks.setBackgroundColor(MuPDFActivity.this.getResources().getColor(R.color.red));
//        } else {
//            mBtn_bookmarks.setBackgroundColor(MuPDFActivity.this.getResources().getColor(R.color.seek_thumb));
//        }
        if (mBookMarks.containsKey(mbookMarksPage)) {
            optionInstance.getViewBookMarker().setBackgroundColor(MuPDFActivity.this.getResources().getColor(R.color.red));
        } else {
            optionInstance.getViewBookMarker().setBackgroundColor(MuPDFActivity.this.getResources().getColor(R.color.transparent));
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
    public void onClickDirectory() {
        // 第一次进来 先获取 目录 ，以后就不需执行
        if (mOutlineItems == null || mOutlineItems.length == 0) {
            // 获取书的目录
            OutlineItem outline[] = mCore.getOutline();
            if (outline != null) {
                // 设置 书的目录数据
                OutlineActivityData.get().items = outline;
                mOutlineItems = OutlineActivityData.get().items;
                if (mBookAdapter == null) {
                    mBookAdapter = new OutlineAdapter(getLayoutInflater(), mOutlineItems);
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
            //隐藏
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
                btn_back_page.setEnabled(false);
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
                    btn_back_page.setEnabled(true);
                }
            }
        });
        mRl_page.startAnimation(anim);
    }

}
