package com.rongjie.leke.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.rongjie.leke.R;
import com.rongjie.leke.adapter.NoteBookAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiangliang on 2016/7/14.
 */
public class NoteBookFragment extends BaseFragment {

    private static final String TAG = "NoteBookFragment";
    private RecyclerView mRecyclerView;
    private NoteBookAdapter mNoteBookAdapter;
    private List<Integer> mInts;
    @Override
    protected void initOtherView() {
        super.initOtherView();
        Log.e(TAG, "initOtherView.................");
//        mStub.setLayoutResource(R.layout.notebook_item);
//        parent = mStub.inflate();
//        mNotebookIv.setEnabled(false);
//        initNoteView();
//        mNoteBookView.setBackgroundResource(R.drawable.biji_canvas_bg);
        mStub.setLayoutResource(R.layout.notebook_layout);
        mStub.inflate();
        mInts = new ArrayList<>();
        mNotebookIv.setEnabled(false);
        mRecyclerView = (RecyclerView) mRoot.findViewById(R.id.notebook_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        mNoteBookAdapter = new NoteBookAdapter(mInts,mContext);
        mRecyclerView.setAdapter(mNoteBookAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG, "onStart...................");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.e(TAG, "hidden changed : " + hidden);
    }
}
