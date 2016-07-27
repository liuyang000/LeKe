package com.rongjie.leke.fragment;

import android.util.Log;

import com.rongjie.leke.R;

/**
 * Created by jiangliang on 2016/7/14.
 */
public class ExerciseBookFragment extends BaseFragment {

    private static final String TAG = "ExerciseBookFragment";

    @Override
    protected void initOtherView() {
        Log.e(TAG,"initOtherView................");
        mExerciseBookIv.setEnabled(false);
        mStub.setLayoutResource(R.layout.notebook_item);
        parent = mStub.inflate();
        initNoteView();
        mNoteBookView.setCanvasColor(0xFFAAAAAA);
    }



}
