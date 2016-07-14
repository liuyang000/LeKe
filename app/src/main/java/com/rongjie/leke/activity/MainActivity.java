package com.rongjie.leke.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.rongjie.leke.R;
import com.rongjie.leke.fragment.BaseFragment;
import com.rongjie.leke.fragment.ExerciseBookFragment;
import com.rongjie.leke.fragment.NoteBookFragment;
import com.rongjie.leke.fragment.TextBookFragment;

/**
 * Created by jiangliang on 2016/6/30.
 */
public class MainActivity extends Activity implements BaseFragment.OnSwitcherListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        FragmentManager fm = getFragmentManager();
//        FragmentTransaction ft = fm.beginTransaction();
//        TextBookFragment fragment = new TextBookFragment();
//        fragment.setOnSwitcherListener(this);
//        ft.add(R.id.container, fragment);
//        ft.commit();
        toTextBookFragment();
    }

    /**
     * 跳转至课本界面
     */
    private BaseFragment fragment;
    private TextBookFragment textBookFragment;

    private void toTextBookFragment() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (null == textBookFragment) {
            textBookFragment = new TextBookFragment();
            textBookFragment.setOnSwitcherListener(this);
            ft.add(R.id.container, textBookFragment);
        } else {
            ft.show(textBookFragment);
        }
        if (fragment != null) {
            ft.hide(fragment);
        }
        fragment = textBookFragment;
        ft.commit();
    }

    /**
     * 跳转至笔记本界面
     */
    private NoteBookFragment noteBookFragment;
    private void toNoteBookFragment() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (null == noteBookFragment) {
            noteBookFragment = new NoteBookFragment();
            noteBookFragment.setOnSwitcherListener(this);
            ft.add(R.id.container, noteBookFragment);
        } else {
            ft.show(noteBookFragment);
        }
        if (fragment != null) {
            ft.hide(fragment);
        }
        fragment = noteBookFragment;
        ft.commit();
    }

    /**
     * 跳转至作业界面
     */
    private ExerciseBookFragment exerciseBookFragment;
    private void toExerciseBookFragment(){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (null == exerciseBookFragment){
            exerciseBookFragment = new ExerciseBookFragment();
            exerciseBookFragment.setOnSwitcherListener(this);
            ft.add(R.id.container,exerciseBookFragment);
        }else{
            ft.show(exerciseBookFragment);
        }
        if (fragment != null) {
            ft.hide(fragment);
        }
        fragment = exerciseBookFragment;
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
