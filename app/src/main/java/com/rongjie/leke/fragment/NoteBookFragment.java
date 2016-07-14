package com.rongjie.leke.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rongjie.leke.R;

/**
 * Created by jiangliang on 2016/7/14.
 */
public class NoteBookFragment extends BaseFragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e("NoteBookFragment","onCreateView。。。。。。。。。。。。。。。");
        root = inflater.inflate(R.layout.notebook_layout,container,false);
        return root;
    }

    @Override
    protected void initOtherView() {
        super.initOtherView();
        notebook.setEnabled(false);
        screenShotImg.setVisibility(View.GONE);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.e("NoteBookFragment","onHiddenChanged。。。。。。。。。。。。。。。" + hidden);
    }
}
