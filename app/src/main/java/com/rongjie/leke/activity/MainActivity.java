package com.rongjie.leke.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.rongjie.leke.R;
import com.rongjie.leke.fragment.BaseFragment;
import com.rongjie.leke.fragment.ExerciseBookFragment;
import com.rongjie.leke.fragment.NoteBookFragment;
import com.rongjie.leke.fragment.TextBookFragment1;

/**
 * Created by jiangliang on 2016/6/30.
 */
public class MainActivity extends Activity implements BaseFragment.OnSwitcherListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toTextBookFragment();
    }

    /**
     * 跳转至课本界面
     */
    private BaseFragment mFragment;
    private TextBookFragment1 mTextBookFragment;

    private void toTextBookFragment() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (null == mTextBookFragment) {
            mTextBookFragment = new TextBookFragment1();
            mTextBookFragment.setOnSwitcherListener(this);
            ft.add(R.id.container, mTextBookFragment);
        } else {
            ft.show(mTextBookFragment);
        }
        if (mFragment != null) {
            ft.hide(mFragment);
        }
        mFragment = mTextBookFragment;
        ft.commit();
    }

    /**
     * 跳转至笔记本界面
     */
    private NoteBookFragment mNoteBookFragment;
    private void toNoteBookFragment() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (null == mNoteBookFragment) {
            mNoteBookFragment = new NoteBookFragment();
            mNoteBookFragment.setOnSwitcherListener(this);
            ft.add(R.id.container, mNoteBookFragment);
        } else {
            ft.show(mNoteBookFragment);
        }
        if (mFragment != null) {
            ft.hide(mFragment);
        }
        mFragment = mNoteBookFragment;
        ft.commit();
    }

    /**
     * 跳转至作业界面
     */
    private ExerciseBookFragment mExerciseBookFragment;
    private void toExerciseBookFragment(){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (null == mExerciseBookFragment){
            mExerciseBookFragment = new ExerciseBookFragment();
            mExerciseBookFragment.setOnSwitcherListener(this);
            ft.add(R.id.container, mExerciseBookFragment);
        }else{
            ft.show(mExerciseBookFragment);
        }
        if (mFragment != null) {
            ft.hide(mFragment);
        }
        mFragment = mExerciseBookFragment;
        ft.commit();
    }

    @Override
    public void switch2TextBookFragment() {
        toTextBookFragment();
    }

    @Override
    public void switch2NoteBookFragment() {
        toNoteBookFragment();
    }

    @Override
    public void switch2ExerciseFragment() {
        toExerciseBookFragment();
    }
}
