package com.rongjie.pdf.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.rongjie.leke.MyApplication;
import com.rongjie.leke.R;
import com.rongjie.pdf.View.dialog.BookMarksDialog;
import com.rongjie.pdf.View.dialog.NoteFragmentDialog;
import com.rongjie.pdf.adapter.AdaptetFragmentNotes;
import com.rongjie.pdf.bean.NoteInfo;
import com.rongjie.pdf.utils.FileUtils;
import com.rongjie.pdf.utils.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2016/7/12.
 * 笔记
 */
public class NotesFragment extends Fragment implements AdaptetFragmentNotes.ClickItemNotesListener {//, BookMarksDialog.DialogClickFinsihListener {

    private List<NoteInfo> mNotes = new ArrayList<NoteInfo>();
    private ViewGroup mRootView;
    private RecyclerView mRecyclerView;
    private AdaptetFragmentNotes mAdaptetFragmentNotes;
    private ProgressBar mProgressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = (ViewGroup) inflater.inflate(R.layout.fragment_notes, null);
        mProgressBar = (ProgressBar) mRootView.findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.VISIBLE);

        //做了一个延迟100 毫秒的操作 ，主要是为了解决，系统在遍历文件是需要时间的，进来会黑屏一段才显示
        //先加载布局然后在做遍历，用户体检会解决很好

        MyApplication.getMainThreadHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initBookDatas();
                findBooks();
                mAdaptetFragmentNotes.notifyDataSetChanged();
                mProgressBar.setVisibility(View.GONE);
            }
        }, 100);

        return mRootView;
    }

    /**
     * 初始化 书列表
     */
    private void initBookDatas() {
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.recycler_View);
        GridLayoutManager layout = new GridLayoutManager(getActivity(), 4);
        mRecyclerView.setLayoutManager(layout);
        mAdaptetFragmentNotes = new AdaptetFragmentNotes(getActivity(),mNotes);
        mAdaptetFragmentNotes.setListener(this);
        mRecyclerView.setAdapter(mAdaptetFragmentNotes);
        mRecyclerView.setHasFixedSize(true);
    }

    /**
     * 搜索本地的书
     */
    private void findBooks() {
        mNotes.clear();
        String bookFile = FileUtils.getTextBookFilesDir(getActivity());
        System.out.println("appFile ==" + bookFile);
        File dir = new File(bookFile);
        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            return;
        }
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (!file.isDirectory() && file.getName().contains("pdf")) {
                System.out.println("  file.getName()" + file.getName());
                NoteInfo info = new NoteInfo();
                info.setFileName( file.getName().substring(0, file.getName().length() - 4));
                mNotes.add(info);
            }

        }
        NoteInfo info = new NoteInfo();
        info.setAddView(true);
        mNotes.add(info);
        dir = null;
        files = null;
        System.gc();
        System.out.println("books.size" + mNotes.size());
    }



    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        // 不需要做什么处理
        System.out.println("onHiddenChanged == " + hidden);
    }

    @Override
    public void onClickItemNotesListener(NoteInfo info) {
        if (info.isAddView()){
            //TODO:弹出框
            Toast.makeText(getActivity(),"ADD"+"：笔记", Toast.LENGTH_SHORT).show();
//            showBookMarksDialog("add",BookMarksDialog.DialogMode.ADD,"添加笔记本");
            mNoteDialog = new NoteFragmentDialog(getActivity()) ;
            mNoteDialog.show();
        }else{
            //TODO:进入笔记
            Toast.makeText(getActivity(), info.getFileName()+"：笔记", Toast.LENGTH_SHORT).show();
        }
    }



   private NoteFragmentDialog mNoteDialog ;

//    public void showBookMarksDialog(String tag, BookMarksDialog.DialogMode dialogMode, String msg) {
//
//        if (null == mBookMarkDialog) {
//            mBookMarkDialog = new BookMarksDialog(getActivity());
//            mBookMarkDialog.setListener(NotesFragment.this);
//        }
//
//        if (!mBookMarkDialog.isShowing()) {
//            mBookMarkDialog.show();
//            mBookMarkDialog.setTag(tag);
//            mBookMarkDialog.setDialogMode(dialogMode);
//            mBookMarkDialog.setEditBookMarksContent(msg);
//        }
//    }

//    //dialog点击按钮
//    @Override
//    public void onDialogConfirmCancleClick(BookMarksDialog dialog, BookMarksDialog.ClickState clickState) {
//
//        if (clickState == BookMarksDialog.ClickState.IMG_BTN_CLOSE_CLICKED) {
//            System.out.println("IMG_BTN_CLOSE_CLICKED");
//
//        } else if (clickState == BookMarksDialog.ClickState.BTN_CHANGE_CLICKED) {
//        } else if (clickState == BookMarksDialog.ClickState.BTN_DELETE_CLICKED) {
//        } else if (clickState == BookMarksDialog.ClickState.BTN_ADD_CLICKED) {
//            //TODO:添加笔记
//            String fileName = dialog.getEditBookMarksContent();
//            if (StringUtils.isEmpty(fileName)){
//                return;
//            }else {
//                NoteInfo addView = mNotes.remove(mNotes.size() - 1);
//                NoteInfo info = new NoteInfo();
//                info.setFileName(fileName);
//                info.setAddInfo(true);
//                mNotes.add(info) ;
//                mNotes.add(addView);
//            }
//            mAdaptetFragmentNotes.notifyDataSetChanged();
//        }
//
//        dialog.dismiss();
//    }
}
