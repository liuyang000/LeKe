package com.rongjie.pdf.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rongjie.leke.R;


/**
 * Created by Administrator on 2016/7/12.
 * 笔记
 */
public class NotesFragment extends Fragment {
    private ViewGroup mRootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return mRootView = (ViewGroup) inflater.inflate(R.layout.fragment_notes, null);
    }
}
