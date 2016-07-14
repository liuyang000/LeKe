package com.rongjie.leke.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rongjie.leke.R;

/**
 * Created by jiangliang on 2016/7/14.
 */
public class ExerciseBookFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return root = inflater.inflate(R.layout.notebook_layout,container,false);
    }

    @Override
    protected void initOtherView() {
        exerciseBook.setEnabled(false);
    }



}
