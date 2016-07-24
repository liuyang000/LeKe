package com.rongjie.leke.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.rongjie.leke.R;

/**
 * Created by jiangliang on 2016/7/11.
 */
public class TextBookFragment extends com.rongjie.leke.fragment.BaseFragment {

    private ImageView bookMark;
    private ImageView directory;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e("TextBookFragment","onCreateView。。。。。。。。。。。。。。");
        root = inflater.inflate(R.layout.notebook_layout,container,false);
        return root;
    }

    @Override
    protected void initOtherView() {
        super.initOtherView();
        bookMark = (ImageView) root.findViewById(R.id.bookmark);
        directory = (ImageView) root.findViewById(R.id.directory);
    }

    @Override
    protected void addListener() {
        super.addListener();
        bookMark.setOnClickListener(this);
        directory.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.bookmark:
                break;
            case R.id.directory:
                break;
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        bookNeedLayout.setVisibility(View.VISIBLE);
        textbook.setEnabled(false);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        Log.e("TextBookFragment","onHiddenChanged。。。。。。。。。。。。。。。" + hidden);
    }
}
