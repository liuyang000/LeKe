package com.rongjie.leke.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.rongjie.leke.R;
import com.rongjie.leke.view.NoteBookView;

import java.util.concurrent.Executors;

/**
 * Created by jiangliang on 2016/7/11.
 * 便签编辑页面
 */
public class ShortNoteFragment extends Fragment implements View.OnClickListener, NoteBookView.OnDoOptListener, NoteBookView.OnUndoOptListener {
    private Button cancelBtn;
    private Button saveBtn;
    private Button inputByGesture;
    private Button inputBySoft;
    private Button undo;
    private Button redo;
    private EditText editText;
    private View root;
    private NoteBookView noteBookView;
    private Context context;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.context = activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.note_layout,container,false);
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        cancelBtn = (Button) root.findViewById(R.id.cancel_label);
        saveBtn = (Button) root.findViewById(R.id.save_label);
        inputByGesture = (Button) root.findViewById(R.id.input_gesture);
        inputBySoft = (Button) root.findViewById(R.id.input_soft);
        undo = (Button) root.findViewById(R.id.undo);
        redo = (Button) root.findViewById(R.id.redo);
        editText = (EditText) root.findViewById(R.id.edit_text);
        noteBookView = (NoteBookView) root.findViewById(R.id.booknote);
        noteBookView.setReDoOptListener(this);
        noteBookView.setUndoOptListener(this);
        undo.setEnabled(false);
        redo.setEnabled(false);

        inputByGesture.setEnabled(false);
        inputBySoft.setEnabled(true);
        cancelBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
        inputByGesture.setOnClickListener(this);
        inputBySoft.setOnClickListener(this);
        undo.setOnClickListener(this);
        redo.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_label:
                showDialog();
                break;
            case R.id.save_label:
                save();
                break;
            case R.id.input_gesture:
                showGestureInput();
                break;
            case R.id.input_soft:
                showSoftInput();
                break;
            case R.id.undo:
                noteBookView.undo();
                break;
            case R.id.redo:
                noteBookView.redo();
                break;
        }
    }

    private void showGestureInput() {
        inputByGesture.setEnabled(false);
        inputBySoft.setEnabled(true);
        noteBookView.setVisibility(View.VISIBLE);
        editText.setVisibility(View.GONE);
        popupInputMethodWindow();
    }

    private void showSoftInput() {
        inputByGesture.setEnabled(true);
        inputBySoft.setEnabled(false);
        noteBookView.setVisibility(View.GONE);
        editText.setVisibility(View.VISIBLE);
        editText.requestFocus();
        popupInputMethodWindow();
    }

    /**
     * 弹出虚拟键盘
     */
    private Handler handler = new Handler();
    private InputMethodManager imm;

    private void popupInputMethodWindow() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                imm = (InputMethodManager) context.getSystemService(Service.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }, 0);
    }

    private void save() {
        if (null != addLabelListener) {
            addLabelListener.addLabel();
        }
        hideInputMethodWindow();
        hideView();
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                noteBookView.save();
            }
        });
    }

    /**
     * 隐藏虚拟键盘
     */
    private void hideInputMethodWindow() {
        if (editText.getVisibility() == View.VISIBLE && null != imm && imm.isActive(editText)) {
            //因为是在fragment下，所以用了getView()获取view，也可以用findViewById（）来获取父控件
            getView().requestFocus();//强制获取焦点，不然getActivity().getCurrentFocus().getWindowToken()会报错
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            imm.restartInput(editText);
        }
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.give_up_paper);
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                noteBookView.clear();
                if (null != deleteLabelListener) {
                    deleteLabelListener.deleteLabel();
                }
                hideInputMethodWindow();
                hideView();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void hideView() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.hide(this);
        ft.commit();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && editText.getVisibility() == View.VISIBLE) {
            editText.requestFocus();
            popupInputMethodWindow();
        }
    }

    @Override
    public void disenableReDoView() {
        redo.setEnabled(false);
    }

    @Override
    public void enableReDoView() {
        redo.setEnabled(true);
    }

    @Override
    public void disenableUndoView() {
        undo.setEnabled(false);
    }

    @Override
    public void enableUndoView() {
        undo.setEnabled(true);
    }

    public interface DeleteLabelListener {
        void deleteLabel();
    }

    public interface AddLabelListener {
        void addLabel();
    }

    private DeleteLabelListener deleteLabelListener;
    private AddLabelListener addLabelListener;

    public void setAddLabelListener(AddLabelListener addLabelListener) {
        this.addLabelListener = addLabelListener;
    }

    public void setDeleteLabelListener(DeleteLabelListener deleteLabelListener) {
        this.deleteLabelListener = deleteLabelListener;
    }
}
