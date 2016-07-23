package com.rongjie.pdf;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rongjie.leke.MyApplication;
import com.rongjie.leke.R;
import com.rongjie.pdf.fragment.CoachBookFragment;
import com.rongjie.pdf.fragment.FolderFragment;
import com.rongjie.pdf.fragment.HomeworkFragment;
import com.rongjie.pdf.fragment.NotesFragment;
import com.rongjie.pdf.fragment.ReferenceBooksFragment;
import com.rongjie.pdf.fragment.TextBookFragment;

import org.w3c.dom.Text;

/**
 * Created by Administrator on 2016/7/12.
 */
public class ActivityMainUI extends Activity implements View.OnClickListener {
    /**
     * 文件夹
     */
    private ViewGroup mRlFolder;
    /**
     * 作业
     */
    private ViewGroup mRHomework;
    /**
     * 笔记
     */
    private ViewGroup mRlNotes;
    /**
     * 课外书
     */
    private ViewGroup mRlReferenceBooks;
    /**
     * 辅导书
     */
    private ViewGroup mRlCoachBook;
    /**
     * 课本
     */
    private ViewGroup mRlTextBook;
    /**
     * 切换fragment
     */
    private FrameLayout mFlContentLayout;
    private CoachBookFragment mCoachBookFragment;
    private HomeworkFragment mHomeworkFragment;
    private FolderFragment mFolderFragment;
    private NotesFragment mNotesFragment;
    private ReferenceBooksFragment mReferenceBooksFragment;
    private TextBookFragment mTextBookFragment;
    private TextView mTvFolder;
    private View mViewFolder;
    private TextView mTvHomework;
    private View mViewHomework;
    private TextView mTvNotes;
    private View mViewNotes;
    private TextView mTvReferenceBooks;
    private View mViewReferenceBooks;
    private TextView mTvCoachBook;
    private View mViewCoachBook;
    private TextView mTvTextBook;
    private View mViewTextBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_ui);
        mFlContentLayout = (FrameLayout) findViewById(R.id.fl_content_layout);

        mRlFolder = (ViewGroup) findViewById(R.id.rl_folder);
        mRlFolder.setOnClickListener(this);
        mTvFolder = (TextView) findViewById(R.id.tv_folder);
        mViewFolder = (View) findViewById(R.id.view_folder);


        mRHomework = (ViewGroup) findViewById(R.id.rl_homework);
        mRHomework.setOnClickListener(this);
        mTvHomework = (TextView) findViewById(R.id.tv_homework);
        mViewHomework = (View) findViewById(R.id.view_homework);


        mRlNotes = (ViewGroup) findViewById(R.id.rl_notes);
        mRlNotes.setOnClickListener(this);
        mTvNotes = (TextView) findViewById(R.id.tv_notes);
        mViewNotes = (View) findViewById(R.id.view_notes);


        mRlReferenceBooks = (ViewGroup) findViewById(R.id.rl_reference_books);
        mRlReferenceBooks.setOnClickListener(this);
        mTvReferenceBooks = (TextView) findViewById(R.id.tv_reference_books);
        mViewReferenceBooks = (View) findViewById(R.id.view_reference_books);


        mRlCoachBook = (ViewGroup) findViewById(R.id.rl_coach_book);
        mRlCoachBook.setOnClickListener(this);
        mTvCoachBook = (TextView) findViewById(R.id.tv_coach_book);
        mViewCoachBook = (View) findViewById(R.id.view_coach_book);


        mRlTextBook = (ViewGroup) findViewById(R.id.rl_text_book);
        mRlTextBook.setOnClickListener(this);
        mTvTextBook = (TextView) findViewById(R.id.tv_text_book);
        mViewTextBook = (View) findViewById(R.id.view_text_book);


        initFragment();
        //默认书为首页
        mRlTextBook.callOnClick();
    }

    @Override
    public void onClick(View v) {
        int clickedViewId = v.getId();
        //更新 按钮状态
        refreshTabBtnState(clickedViewId);
        switch (clickedViewId) {
            case R.id.rl_folder:
                bringFragmentToFrontInner(FragmentDisplayOption.FOLDER_FRAGMENT);
                break;

            case R.id.rl_homework:
                bringFragmentToFrontInner(FragmentDisplayOption.HOMEWORK_FRAGMENT);
                break;

            case R.id.rl_notes:
                bringFragmentToFrontInner(FragmentDisplayOption.NOTES_FRAGMENT);
                break;

            case R.id.rl_reference_books:
                bringFragmentToFrontInner(FragmentDisplayOption.REFERENCE_BOOKS_FRAGMENT);
                break;

            case R.id.rl_coach_book:
                bringFragmentToFrontInner(FragmentDisplayOption.COACH_BOOK_FRAGMENT);
                break;

            case R.id.rl_text_book:
                bringFragmentToFrontInner(FragmentDisplayOption.TEXT_BOOK_FRAGMENT);
                break;
        }
    }

    /***
     * 切换fragment
     */
    private void bringFragmentToFrontInner(FragmentDisplayOption fragmentDisplayOption) {

        Fragment whichToFront = null;
        Fragment whichToBack1 = null;
        Fragment whichToBack2 = null;
        Fragment whichToBack3 = null;
        Fragment whichToBack4 = null;
        Fragment whichToBack5 = null;


        switch (fragmentDisplayOption) {
            case TEXT_BOOK_FRAGMENT:
                whichToFront = mTextBookFragment;
                whichToBack1 = mCoachBookFragment;
                whichToBack2 = mReferenceBooksFragment;
                whichToBack3 = mNotesFragment;
                whichToBack4 = mHomeworkFragment;
                whichToBack5 = mFolderFragment;
                break;

            case COACH_BOOK_FRAGMENT:
                whichToFront = mCoachBookFragment;
                whichToBack1 = mTextBookFragment;
                whichToBack2 = mReferenceBooksFragment;
                whichToBack3 = mNotesFragment;
                whichToBack4 = mHomeworkFragment;
                whichToBack5 = mFolderFragment;
                break;

            case REFERENCE_BOOKS_FRAGMENT:
                whichToFront = mReferenceBooksFragment;
                whichToBack1 = mTextBookFragment;
                whichToBack2 = mCoachBookFragment;
                whichToBack3 = mNotesFragment;
                whichToBack4 = mHomeworkFragment;
                whichToBack5 = mFolderFragment;
                break;

            case NOTES_FRAGMENT:
                whichToFront = mNotesFragment;
                whichToBack1 = mTextBookFragment;
                whichToBack2 = mCoachBookFragment;
                whichToBack3 = mReferenceBooksFragment;
                whichToBack4 = mHomeworkFragment;
                whichToBack5 = mFolderFragment;
                break;

            case HOMEWORK_FRAGMENT:
                whichToFront = mHomeworkFragment;
                whichToBack1 = mTextBookFragment;
                whichToBack2 = mCoachBookFragment;
                whichToBack3 = mReferenceBooksFragment;
                whichToBack4 = mNotesFragment;
                whichToBack5 = mFolderFragment;
                break;

            case FOLDER_FRAGMENT:
                whichToFront = mFolderFragment;
                whichToBack1 = mTextBookFragment;
                whichToBack2 = mCoachBookFragment;
                whichToBack3 = mReferenceBooksFragment;
                whichToBack4 = mNotesFragment;
                whichToBack5 = mHomeworkFragment;
                break;

            default:
                break;
        }

        FragmentManager fragmentManager = this.getFragmentManager();
        fragmentManager.beginTransaction()
                .show(whichToFront)
                .hide(whichToBack1)
                .hide(whichToBack2)
                .hide(whichToBack3)
                .hide(whichToBack4)
                .hide(whichToBack5)
                .commitAllowingStateLoss();

    }

    /**
     * 初始化 6个fragment
     */
    private void initFragment() {

        mCoachBookFragment = new CoachBookFragment();
        mHomeworkFragment = new HomeworkFragment();
        mFolderFragment = new FolderFragment();
        mNotesFragment = new NotesFragment();
        mReferenceBooksFragment = new ReferenceBooksFragment();
        mTextBookFragment = new TextBookFragment();

        FragmentManager childFragmentManager = this.getFragmentManager();
        childFragmentManager.beginTransaction()
                .add(R.id.fl_content_layout, mCoachBookFragment)
                .add(R.id.fl_content_layout, mHomeworkFragment)
                .add(R.id.fl_content_layout, mFolderFragment)
                .add(R.id.fl_content_layout, mNotesFragment)
                .add(R.id.fl_content_layout, mReferenceBooksFragment)
                .add(R.id.fl_content_layout, mTextBookFragment)
                .hide(mCoachBookFragment)
                .hide(mHomeworkFragment)
                .hide(mFolderFragment)
                .hide(mNotesFragment)
                .hide(mReferenceBooksFragment)
                .hide(mTextBookFragment)
                .commitAllowingStateLoss();

    }

    /**
     * 设置按钮状态
     */
    private void refreshTabBtnState(int clickedViewId) {
        mRlTextBook = (ViewGroup) findViewById(R.id.rl_text_book);
        mRlTextBook.setOnClickListener(this);

        boolean isCoachBookFragment = clickedViewId == R.id.rl_coach_book;
        boolean isHomeworkFragment = clickedViewId == R.id.rl_homework;
        boolean isFolderFragment = clickedViewId == R.id.rl_folder;
        boolean isNotesFragment = clickedViewId == R.id.rl_notes;
        boolean isReferenceBooksFragment = clickedViewId == R.id.rl_reference_books;
        boolean isTextBookFragment = clickedViewId == R.id.rl_text_book;

        int blackColor = MyApplication.getInstance().getResources().getColor(R.color.black);
        int blueColor = MyApplication.getInstance().getResources().getColor(R.color.blue);
        int hideView =  View.INVISIBLE;
        int showView =  View.VISIBLE;
        //isCoachBookFragment
        if (isCoachBookFragment){
            mTvCoachBook.setTextColor(blueColor);
            mViewCoachBook.setVisibility(showView);
        }else {
            mTvCoachBook.setTextColor(blackColor);
            mViewCoachBook.setVisibility(hideView);
        }

        //isFolderFragment
        if (isFolderFragment){
            mTvFolder.setTextColor(blueColor);
            mViewFolder.setVisibility(showView);
        }else {
            mTvFolder.setTextColor(blackColor);
            mViewFolder.setVisibility(hideView);
        }

        //isHomeworkFragment
        if (isHomeworkFragment){
            mTvHomework.setTextColor(blueColor);
            mViewHomework.setVisibility(showView);
        }else {
            mTvHomework.setTextColor(blackColor);
            mViewHomework.setVisibility(hideView);
        }

        //isNotesFragment
        if (isNotesFragment){
            mTvNotes.setTextColor(blueColor);
            mViewNotes.setVisibility(showView);
        }else {
            mTvNotes.setTextColor(blackColor);
            mViewNotes.setVisibility(hideView);
        }

        //isReferenceBooksFragment
        if (isReferenceBooksFragment){
            mTvReferenceBooks.setTextColor(blueColor);
            mViewReferenceBooks.setVisibility(showView);
        }else {
            mTvReferenceBooks.setTextColor(blackColor);
            mViewReferenceBooks.setVisibility(hideView);
        }
        //isTextBookFragment
        if (isTextBookFragment){
            mTvTextBook.setTextColor(blueColor);
            mViewTextBook.setVisibility(showView);
        }else {
            mTvTextBook.setTextColor(blackColor);
            mViewTextBook.setVisibility(hideView);
        }

    }


    private enum FragmentDisplayOption {

        COACH_BOOK_FRAGMENT,

        HOMEWORK_FRAGMENT,

        FOLDER_FRAGMENT,

        NOTES_FRAGMENT,

        REFERENCE_BOOKS_FRAGMENT,

        TEXT_BOOK_FRAGMENT
    }
}
