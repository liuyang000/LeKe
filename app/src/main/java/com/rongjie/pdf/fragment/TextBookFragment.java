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
import android.widget.Toast;

import com.rongjie.leke.R;
import com.rongjie.pdf.MuPDFActivity;
import com.rongjie.pdf.adapter.AdaptetFragmentTextBook;
import com.rongjie.pdf.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/12.
 * 课本
 */
public class TextBookFragment extends Fragment implements AdaptetFragmentTextBook.ClickItemTextBookListener {
    private List<File> mBooks = new ArrayList<File>();
    private ViewGroup mRootView;
    private RecyclerView mRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = (ViewGroup) inflater.inflate(R.layout.fragment_text_book, null);
        findBooks();
        initBookDatas();
        return mRootView;
    }

    /**初始化 书列表*/
    private  void initBookDatas(){
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.recycler_View);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),4));
        AdaptetFragmentTextBook adaptetFragmentTextBook = new AdaptetFragmentTextBook(getActivity(),mBooks) ;
        adaptetFragmentTextBook.setListener(this);
        mRecyclerView.setAdapter(adaptetFragmentTextBook);
    }

    /**
     * 搜索本地的书
     */
    private void findBooks() {
        String appFile = FileUtils.getAppFilesDir(getActivity()) + FileUtils.TEXT_BOOK;
        FileUtils.createDirs(appFile);
        File dir = new File(appFile);
        File[] files = dir.listFiles();
        if (files == null || files.length == 0){
            return;
        }
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (!file.isDirectory() && file.getName().contains("pdf")) {
                System.out.println("  file.getName()" + file.getName());
                mBooks.add(file);
            }
        }
        System.out.println("books.size" + mBooks.size());
    }

    @Override
    public void onClickItemTextBookListener(File file) {
        Toast.makeText(getActivity(),file.getName(),Toast.LENGTH_SHORT).show();
        System.out.println("file path ==="+file.getAbsolutePath());
        Uri uri = Uri.parse(file.getAbsolutePath());
        Intent intent = new Intent(getActivity(), MuPDFActivity.class);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(uri);
        startActivity(intent);
    }
}
