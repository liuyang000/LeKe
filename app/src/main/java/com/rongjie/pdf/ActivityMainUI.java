package com.rongjie.pdf;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.rongjie.leke.R;
import com.rongjie.pdf.fragment.CoachBookFragment;
import com.rongjie.pdf.fragment.FolderFragment;
import com.rongjie.pdf.fragment.HomeworkFragment;
import com.rongjie.pdf.fragment.NotesFragment;
import com.rongjie.pdf.fragment.ReferenceBooksFragment;
import com.rongjie.pdf.fragment.TextBookFragment;

/**
 * Created by Administrator on 2016/7/12.
 */
public class ActivityMainUI extends Activity implements View.OnClickListener {
    /**
     * 文件夹
     */
    private RelativeLayout mRlFolder;
    /**
     * 作业
     */
    private RelativeLayout mRHomework;
    /**
     * 笔记
     */
    private RelativeLayout mRlNotes;
    /**
     * 课外书
     */
    private RelativeLayout mRlReferenceBooks;
    /**
     * 辅导书
     */
    private RelativeLayout mRlCoachBook;
    /**
     * 课本
     */
    private RelativeLayout mRlTextBook;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_ui);
        mFlContentLayout = (FrameLayout) findViewById(R.id.fl_content_layout);

        mRlFolder = (RelativeLayout) findViewById(R.id.rl_folder);
        mRlFolder.setOnClickListener(this);

        mRHomework = (RelativeLayout) findViewById(R.id.rl_homework);
        mRHomework.setOnClickListener(this);

        mRlNotes = (RelativeLayout) findViewById(R.id.rl_notes);
        mRlNotes.setOnClickListener(this);

        mRlReferenceBooks = (RelativeLayout) findViewById(R.id.rl_reference_books);
        mRlReferenceBooks.setOnClickListener(this);

        mRlCoachBook = (RelativeLayout) findViewById(R.id.rl_coach_book);
        mRlCoachBook.setOnClickListener(this);

        mRlTextBook = (RelativeLayout) findViewById(R.id.rl_text_book);
        mRlTextBook.setOnClickListener(this);

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
                whichToBack1 =mCoachBookFragment;
                whichToBack2 =mReferenceBooksFragment;
                whichToBack3 =mNotesFragment;
                whichToBack4 = mHomeworkFragment;
                whichToBack5 =mFolderFragment;
                break;

            case COACH_BOOK_FRAGMENT:
                whichToFront =mCoachBookFragment;
                whichToBack1 =mTextBookFragment;
                whichToBack2 =mReferenceBooksFragment;
                whichToBack3 =mNotesFragment;
                whichToBack4 = mHomeworkFragment;
                whichToBack5 =mFolderFragment;
                break;

            case REFERENCE_BOOKS_FRAGMENT:
                whichToFront =mReferenceBooksFragment;
                whichToBack1 =mTextBookFragment;
                whichToBack2 =mCoachBookFragment;
                whichToBack3 =mNotesFragment;
                whichToBack4 = mHomeworkFragment;
                whichToBack5 =mFolderFragment;
                break;

            case NOTES_FRAGMENT:
                whichToFront =mNotesFragment;
                whichToBack1 =mTextBookFragment;
                whichToBack2 =mCoachBookFragment;
                whichToBack3 =mReferenceBooksFragment;
                whichToBack4 = mHomeworkFragment;
                whichToBack5 =mFolderFragment;
                break;

            case HOMEWORK_FRAGMENT:
                whichToFront =mHomeworkFragment;
                whichToBack1 =mTextBookFragment;
                whichToBack2 =mCoachBookFragment;
                whichToBack3 =mReferenceBooksFragment;
                whichToBack4 = mNotesFragment;
                whichToBack5 =mFolderFragment;
                break;

            case FOLDER_FRAGMENT:
                whichToFront =mFolderFragment;
                whichToBack1 =mTextBookFragment;
                whichToBack2 =mCoachBookFragment;
                whichToBack3 =mReferenceBooksFragment;
                whichToBack4 = mNotesFragment;
                whichToBack5 =mHomeworkFragment;
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
        mRlTextBook = (RelativeLayout) findViewById(R.id.rl_text_book);
        mRlTextBook.setOnClickListener(this);

        boolean isCoachBookFragment = clickedViewId == R.id.rl_coach_book;
        boolean isHomeworkFragment = clickedViewId == R.id.rl_homework;
        boolean isFolderFragment = clickedViewId == R.id.rl_folder;
        boolean isNotesFragment = clickedViewId == R.id.rl_notes;
        boolean isReferenceBooksFragment = clickedViewId == R.id.rl_reference_books;
        boolean isTextBookFragment = clickedViewId == R.id.rl_text_book;


        mRlFolder.setSelected(isFolderFragment);
        mRHomework.setSelected(isHomeworkFragment);
        mRlNotes.setSelected(isNotesFragment);
        mRlReferenceBooks.setSelected(isReferenceBooksFragment);
        mRlCoachBook.setSelected(isCoachBookFragment);
        mRlTextBook.setSelected(isTextBookFragment);

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
