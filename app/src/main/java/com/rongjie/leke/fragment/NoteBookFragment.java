package com.rongjie.leke.fragment;

import android.util.Log;

import com.rongjie.leke.R;

/**
 * Created by jiangliang on 2016/7/14.
 */
public class NoteBookFragment extends BaseFragment {

    private static final String TAG = "NoteBookFragment";

    @Override
    protected void initOtherView() {
        super.initOtherView();
        Log.e(TAG,"initOtherView.................");
        mStub.setLayoutResource(R.layout.notebook_item);
        parent = mStub.inflate();
        mNotebookIv.setEnabled(false);
        initNoteView();
        mNoteBookView.setCanvasColor(0xFFAAAAAA);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG,"onStart...................");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.e(TAG,"hidden changed : " + hidden);
    }
}
