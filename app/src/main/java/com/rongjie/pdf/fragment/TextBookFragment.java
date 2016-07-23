package com.rongjie.pdf.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
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
import com.rongjie.pdf.MuPDFActivity;
import com.rongjie.pdf.adapter.AdaptetFragmentTextBook;
import com.rongjie.pdf.bean.TextBookInfo;
import com.rongjie.pdf.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/12.
 * 课本
 */
public class TextBookFragment extends Fragment implements AdaptetFragmentTextBook.ClickItemTextBookListener, Runnable {
    private List<TextBookInfo> mBooks = new ArrayList<TextBookInfo>();
    //    private Map<String,File> mBooksIcon = new HashMap<String,File>() ;
    private ViewGroup mRootView;
    private RecyclerView mRecyclerView;
    private AdaptetFragmentTextBook mAdaptetFragmentTextBook;
    private ProgressBar mProgressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mRootView = (ViewGroup) inflater.inflate(R.layout.fragment_text_book, null);
        //进度刷新条
        mProgressBar = (ProgressBar) mRootView.findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.VISIBLE);
        //做了一个延迟100 毫秒的操作 ，主要是为了解决，系统在遍历文件是需要时间的，进来会黑屏一段才显示
        //先加载布局然后在做遍历，用户体检会解决很好
        MyApplication.getMainThreadHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initBookDatas();
                findBooks();
                mAdaptetFragmentTextBook.notifyDataSetChanged();
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
        mAdaptetFragmentTextBook = new AdaptetFragmentTextBook(getActivity(), mBooks);
        mAdaptetFragmentTextBook.setListener(this);
        mRecyclerView.setAdapter(mAdaptetFragmentTextBook);
        mRecyclerView.setHasFixedSize(true);
    }

    /**
     * 搜索本地的书
     */
    private void findBooks() {
        mBooks.clear();
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
                TextBookInfo info = new TextBookInfo();
                info.setFileName(file.getName());
                info.setFileAbs(file.getAbsolutePath());
                mBooks.add(info);
            }
        }
        dir = null;
        files = null;
        System.gc();
        System.out.println("books.size" + mBooks.size());
    }

    @Override
    public void onClickItemTextBookListener(String fileAbs) {
        MyApplication.getMainThreadHandler().removeCallbacks(this);
        Toast.makeText(getActivity(), fileAbs, Toast.LENGTH_SHORT).show();
        System.out.println("file path ===" + fileAbs);
        Uri uri = Uri.parse(fileAbs);
        Intent intent = new Intent(getActivity(), MuPDFActivity.class);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(uri);
        startActivity(intent);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        // 不需要做什么处理
        System.out.println("onHiddenChanged == " + hidden);
    }

    @Override
    public void onStart() {
        super.onStart();
        // 这里主要解决的问题，进入书后 会截取封面，并且保存到sdk上
        // 由于截取是在后台子线程中的 ，当用户进PDF 快速退出，此时 图片还未保存、
        // 这里做了 延迟100毫秒后在更新adapter的数据，可以解决该问题
        MyApplication.getMainThreadHandler().removeCallbacks(this);
        MyApplication.getMainThreadHandler().postDelayed(this, 100);
    }

    @Override
    public void run() {
        mAdaptetFragmentTextBook.notifyDataSetChanged();
    }
}
