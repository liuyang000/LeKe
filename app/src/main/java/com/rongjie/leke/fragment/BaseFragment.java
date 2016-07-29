package com.rongjie.leke.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.rongjie.leke.Position;
import com.rongjie.leke.R;
import com.rongjie.leke.adapter.ColorAdapter;
import com.rongjie.leke.decoration.DividerGridItemDecoration;
import com.rongjie.leke.util.ImageLoader;
import com.rongjie.leke.util.ToastUtil;
import com.rongjie.leke.view.MoveImageView;
import com.rongjie.leke.view.MyFrameLayout;
import com.rongjie.leke.view.NoteBookView;
import com.rongjie.leke.view.ScreenShotView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jiangliang on 2016/7/8.
 */
public class BaseFragment extends Fragment implements View.OnClickListener, NoteBookView.OnUndoOptListener, NoteBookView.OnDoOptListener, ShortNoteFragment.DeleteLabelListener, ShortNoteFragment.AddLabelListener, View.OnTouchListener, MoveImageView.UpdateImageViewMapListener, SeekBar.OnSeekBarChangeListener {
    protected ImageButton mPen;
    protected ImageButton mPencil;
    protected ImageButton mOliBlackPen;
    protected ImageButton mMakerPen;
    protected ImageButton mEraser;
    protected ImageButton color;
    protected SeekBar mPenSizePg;
    protected ImageView mPenSizeIv;
    protected SeekBar mPenAlphaPg;
    protected ImageView mPenAlphaIv;
    protected ImageView mBookPackageIv;
    protected ImageView mGestureIv;
    protected ImageView mPaintDrawIv;
    protected ImageView mDrawPicIv;
    protected ImageView mLabelIv;
    protected ImageView mImageIv;
    protected ImageView mScreenshotIv;
    protected ImageView mSendIv;
    protected ImageView mUndoIv;
    protected ImageView mRedoIv;
    protected ImageView mTextbookIv;
    protected ImageView mNotebookIv;
    protected ImageView mExerciseBookIv;
    protected ImageView mBookMarkerIv;
    protected ImageView mDirectoryIv;
    protected NoteBookView mNoteBookView;
    protected LinearLayout mPaintChoose;
    protected LinearLayout mBookNeedLayout;
    protected LinearLayout mOptionLayout;
    protected ScreenShotView mScreenShotView;
    protected MyFrameLayout mFrameLayout;
    protected View mRoot;
    protected Context mContext;
    protected ViewStub mStub;

    protected RelativeLayout mRl_page;
    protected SeekBar mSeekbarPage;
    protected TextView mTvPageNumber;
    protected Button mBackBtn;

    protected FrameLayout mParentLayout;
    protected int x;//绘画开始的横坐标
    protected int y;//绘画开始的纵坐标
    protected int right;//绘画结束的横坐标
    protected int bottom;//绘画结束的纵坐标
    protected int width;//绘画的宽度
    protected int height;//绘画的高度
    private static final String PROPERTY_NAME = "translationY";
    private static final String TAG = "BaseFragment";
    private Map<Position, MoveImageView> imageViews;//用来存放位置和图标的对应关系

    private boolean isNeedCutScreen;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mContext = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDatas();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.fragment_base_layout, container, false);
        return mRoot;
    }

    protected void initDatas() {
        imageViews = new HashMap<>();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initBaseView();
        initOtherView();
        addListener();
    }

    private void initBaseView() {
        mPen = (ImageButton) mRoot.findViewById(R.id.pen);
        mPencil = (ImageButton) mRoot.findViewById(R.id.pencil);
        mOliBlackPen = (ImageButton) mRoot.findViewById(R.id.oli_black_pen);
        mMakerPen = (ImageButton) mRoot.findViewById(R.id.maker_pen);
        mEraser = (ImageButton) mRoot.findViewById(R.id.eraser);
        color = (ImageButton) mRoot.findViewById(R.id.color);
        mPenSizePg = (SeekBar) mRoot.findViewById(R.id.pen_size_pg);
        mPenSizeIv = (ImageView) mRoot.findViewById(R.id.pen_size);
        mPenAlphaPg = (SeekBar) mRoot.findViewById(R.id.pen_alpha_pg);
        mPenAlphaIv = (ImageView) mRoot.findViewById(R.id.pen_alpha);
        mPaintChoose = (LinearLayout) mRoot.findViewById(R.id.paint_choose);
        mBookPackageIv = (ImageView) mRoot.findViewById(R.id.book_package);
        mGestureIv = (ImageView) mRoot.findViewById(R.id.gesture);
        mPaintDrawIv = (ImageView) mRoot.findViewById(R.id.paint_draw);
        mDrawPicIv = (ImageView) mRoot.findViewById(R.id.draw_pic);
        mLabelIv = (ImageView) mRoot.findViewById(R.id.label);
        mImageIv = (ImageView) mRoot.findViewById(R.id.image);
        mScreenshotIv = (ImageView) mRoot.findViewById(R.id.screenshot);
        mSendIv = (ImageView) mRoot.findViewById(R.id.send);
        mUndoIv = (ImageView) mRoot.findViewById(R.id.undo);
        mRedoIv = (ImageView) mRoot.findViewById(R.id.redo);
        mBookMarkerIv = (ImageView) mRoot.findViewById(R.id.bookmark);
        mDirectoryIv = (ImageView) mRoot.findViewById(R.id.directory);
        mTextbookIv = (ImageView) mRoot.findViewById(R.id.textbook);
        mNotebookIv = (ImageView) mRoot.findViewById(R.id.notebook);
        mExerciseBookIv = (ImageView) mRoot.findViewById(R.id.exercise_book);

        mBookNeedLayout = (LinearLayout) mRoot.findViewById(R.id.book_need_layout);
        mFrameLayout = (MyFrameLayout) mRoot.findViewById(R.id.note_parent);
        mOptionLayout = (LinearLayout) mRoot.findViewById(R.id.opt_layout);
        mScreenShotView = new ScreenShotView(mContext);

        //Seek部分
        mRl_page = (RelativeLayout) mRoot.findViewById(R.id.rl_page);
        mSeekbarPage = (SeekBar) mRoot.findViewById(R.id.seekbar_page);
        mTvPageNumber = (TextView) mRoot.findViewById(R.id.tv_page_number);
        mBackBtn = (Button) mRoot.findViewById(R.id.btn_back_page);

        mStub = (ViewStub) mRoot.findViewById(R.id.view_stub);
        mParentLayout = (FrameLayout) mRoot.findViewById(R.id.parent_layout);

        mBackBtn.setEnabled(false);
        mBookNeedLayout.setVisibility(View.INVISIBLE);
        mUndoIv.setEnabled(false);
        mRedoIv.setEnabled(false);

        mPenSizePg.setProgress(100);
        mPenAlphaPg.setProgress(100);
        mGestureIv.setSelected(true);
    }

    protected void initOtherView() {

    }

    protected View parent;

    public void setParentOfNoteView(View parent) {
        this.parent = parent;
    }

    public void initNoteView() {
        mNoteBookView = (NoteBookView) parent.findViewById(R.id.booknote);
        mNoteBookView.setReDoOptListener(this);
        mNoteBookView.setUndoOptListener(this);
        updatePaintColor(Color.BLACK);
        mFrameLayout = (MyFrameLayout) parent.findViewById(R.id.note_parent);
        parent.setOnTouchListener(this);
    }

    protected void addListener() {
        mPen.setOnClickListener(this);
        mPencil.setOnClickListener(this);
        mOliBlackPen.setOnClickListener(this);
        mMakerPen.setOnClickListener(this);
        mEraser.setOnClickListener(this);
        color.setOnClickListener(this);
        mBookPackageIv.setOnClickListener(this);
        mGestureIv.setOnClickListener(this);
        mPaintDrawIv.setOnClickListener(this);
        mDrawPicIv.setOnClickListener(this);
        mLabelIv.setOnClickListener(this);
        mImageIv.setOnClickListener(this);
        mScreenshotIv.setOnClickListener(this);
        mSendIv.setOnClickListener(this);
        mUndoIv.setOnClickListener(this);
        mRedoIv.setOnClickListener(this);
        mBookMarkerIv.setOnClickListener(this);
        mDirectoryIv.setOnClickListener(this);
        mTextbookIv.setOnClickListener(this);
        mNotebookIv.setOnClickListener(this);
        mExerciseBookIv.setOnClickListener(this);
        mBackBtn.setOnClickListener(this);
        mRoot.setOnTouchListener(this);
        mPenSizePg.setOnSeekBarChangeListener(this);
        mPenAlphaPg.setOnSeekBarChangeListener(this);
    }

    private int chooserHeight;
    private boolean flag = false;//用来标识便签显示是以便签按钮为入口的
    protected View view;

    @Override
    public void onClick(View v) {
        if (null != mScreenShotView && mScreenShotView.isSign()) {
            mScreenShotView.setSign(false);
            resetScreenView();
        }
        mFrameLayout.setInterceptable(false);
        if (null != view) {
            view.setSelected(false);
            Log.e(TAG, "....................................");
        } else {
            mGestureIv.setSelected(false);
        }
        isNeedCutScreen = false;
        switch (v.getId()) {
            case R.id.pen:
                view = mPen;
                updateStatus(Color.BLACK, 2, 255);
                mNoteBookView.setPen();
                break;
            case R.id.pencil:
                //TODO:设置画笔为铅笔
                view = mPencil;
                break;
            case R.id.oli_black_pen:
                view = mOliBlackPen;
                updateStatus(Color.BLACK, 5, 255);
                mNoteBookView.setOilBlackPen();
                break;
            case R.id.maker_pen:
                view = mMakerPen;
                updateStatus(Color.GREEN, 15, 100);
                mNoteBookView.setMakerPen();
                break;
            case R.id.eraser:
                view = mEraser;
                mNoteBookView.useEraser();
                break;
            case R.id.color:
                view = color;
                showColorSelector();
                break;
            case R.id.book_package:
                //TODO：返回书包界面
                view = mBookPackageIv;
                getActivity().finish();
                break;
            case R.id.gesture:
                //TODO:切换为手势控制
                view = mGestureIv;
                break;
            case R.id.paint_draw:
                //TODO：切换为绘制输入
                view = mPaintDrawIv;
                chooserHeight = mPaintChoose.getHeight();
                if (mPaintChoose.getVisibility() == View.VISIBLE) {
                    outAnimator();
                } else {
                    inAnimatior();
                }
                break;
            case R.id.draw_pic:
                //TODO:使用绘图工具
                view = mDrawPicIv;
                break;
            case R.id.label:
                view = mLabelIv;
                flag = true;
                useNote();
                break;
            case R.id.image:
                view = mImageIv;
                useLocalPhoto();
                break;
            case R.id.screenshot:
                view = mScreenshotIv;
                mScreenShotView.setSign(true);
                mFrameLayout.setInterceptable(true);
                mScreenShotView.postInvalidate();
                isNeedCutScreen = true;
                break;
            case R.id.send:
                //TODO:发送至服务器
                view = mSendIv;
                send();
                break;
            case R.id.undo:
                mNoteBookView.undo();
                break;
            case R.id.redo:
                mNoteBookView.redo();
                break;
            case R.id.textbook:
                toTextBookFragment();
                break;
            case R.id.notebook:
                toNoteBookFragment();
                break;
            case R.id.exercise_book:
                toExerciseBookFragment();
                break;
            case R.id.cancel_cut:
                cancelCutOpt();
                break;
            case R.id.copy_cut:
                copyCutOpt();
                break;
            case R.id.send_cut:
                sendCutOpt();
                break;
            case R.id.save_cut:
                saveCutOpt();
                break;

        }
        if (null != view && view != mPaintDrawIv && mPaintChoose.getVisibility() == View.VISIBLE) {
            outAnimator();
        }
        if (null != view && view != mUndoIv && view != mRedoIv) {
            view.setSelected(true);
        }
    }

    private void toTextBookFragment() {
        if (null != switcherListener) {
            switcherListener.switch2TextBookFragment();
        }
    }

    private void toNoteBookFragment() {
        if (null != switcherListener) {
            switcherListener.switch2NoteBookFragment();
        }
    }

    private void toExerciseBookFragment() {
        if (null != switcherListener) {
            switcherListener.switch2ExerciseFragment();
        }
    }

    private void updateStatus(int color, int progress, int alpha) {
        GradientDrawable drawable = (GradientDrawable) mPenSizeIv.getBackground();
        drawable.setColor(color);
        GradientDrawable alphaDrawable = (GradientDrawable) mPenAlphaIv.getBackground();
        alphaDrawable.setColor(color);
        mPenSizePg.setProgress(progress);
        mPenAlphaPg.setProgress(alpha);
    }

    /**
     * 显示颜色选择窗口
     */
    private PopupWindow popWindow;

    private void showColorSelector() {
        if (popWindow == null) {
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.color_layout, null);
            popWindow = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.color_selector);
            final int[] colors = getResources().getIntArray(R.array.colors);
            ColorAdapter adapter = new ColorAdapter(colors, mContext);
            adapter.setOnItemClickListener(new ColorAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    popWindow.dismiss();
                    updatePaintColor(colors[position]);
                }
            });
            recyclerView.setLayoutManager(new GridLayoutManager(mContext, 6));
            recyclerView.addItemDecoration(new DividerGridItemDecoration(mContext));
            recyclerView.setAdapter(adapter);
        }
        // 使其能获得焦点 ，要想监听菜单里控件的事件就必须要调用此方法
        popWindow.setFocusable(true);
        // 设置允许在外点击消失
        popWindow.setOutsideTouchable(true);
        // 设置背景，这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popWindow.setBackgroundDrawable(new BitmapDrawable());
        //软键盘不会挡着popupwindow
        popWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //设置菜单显示的位置
        popWindow.showAtLocation(mPaintChoose, Gravity.TOP, 0, 2 * mPaintChoose.getHeight());
        //监听触屏事件
        popWindow.setTouchInterceptor(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent event) {
                return false;
            }
        });
    }

    /**
     * 改变画笔颜色
     */
    private void updatePaintColor(int color) {
        GradientDrawable drawable = (GradientDrawable) mPenSizeIv.getBackground();
        drawable.setColor(color);
        GradientDrawable alphaDrawable = (GradientDrawable) mPenAlphaIv.getBackground();
        alphaDrawable.setColor(color);
        mNoteBookView.setPaintColor(color);
    }


    /**
     * 画笔选择界面进入动画
     */
    private void inAnimatior() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mPaintChoose, PROPERTY_NAME, 0, chooserHeight);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                mPaintChoose.setVisibility(View.VISIBLE);
            }
        });
        animator.setDuration(500);
        animator.start();
    }

    /**
     * 画笔选择界面退出动画
     */
    private void outAnimator() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mPaintChoose, PROPERTY_NAME, chooserHeight, 0);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mPaintChoose.setVisibility(View.INVISIBLE);
            }
        });
        animator.setDuration(500);
        animator.start();
    }

    /**
     * 从本地相册获取图片
     */
    private void useLocalPhoto() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, 1);
    }

    @Override
    public void disenableReDoView() {
        mRedoIv.setEnabled(false);
    }

    @Override
    public void enableReDoView() {
        mRedoIv.setEnabled(true);
    }

    @Override
    public void disenableUndoView() {
        mUndoIv.setEnabled(false);
    }

    @Override
    public void enableUndoView() {
        mUndoIv.setEnabled(true);
    }

    /**
     * 打开便签界面
     */
    private ShortNoteFragment noteFragment;

    protected void useNote() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (noteFragment == null) {
            noteFragment = new ShortNoteFragment();
            noteFragment.setDeleteLabelListener(this);
            noteFragment.setAddLabelListener(this);
            ft.add(R.id.container, noteFragment);
        } else {
            ft.show(noteFragment);
        }
        ft.commit();
    }

    /**
     * 根据请求代码，得到不同情况下的图片路径，进而进行图片解析和显示
     *
     * @param requestCode 0代表使用系统相机拍照，1代表选择系统相册照片
     */
    @Override
    public void onActivityResult(final int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            new AsyncTask<Void, Void, Bitmap>() {
                @Override
                protected Bitmap doInBackground(Void... params) {
                    String path = "";
                    if (requestCode == 1) {
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        Cursor cursor = mContext.getContentResolver().query(data.getData(), filePathColumn, null, null, null);
                        if (null != cursor) {
                            cursor.moveToFirst();
                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            path = cursor.getString(columnIndex);
                            cursor.close();
                        }
                    }
                    return ImageLoader.loadBmpFromFile(path);
                }

                @Override
                protected void onPostExecute(Bitmap bitmap) {
                    View view = View.inflate(mContext, R.layout.insert_pic_layout, null);
                    Button deleteBtn = (Button) view.findViewById(R.id.delete_pic);
                    ImageView imagebtn = (ImageView) view.findViewById(R.id.insert_pic);
                    FrameLayout.LayoutParams params = createLayoutParams();
                    imagebtn.setImageBitmap(bitmap);
                    params.width = bitmap.getWidth();
                    params.height = bitmap.getHeight();
                    deleteBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mFrameLayout.removeView((View) v.getParent());
                        }
                    });
                    mFrameLayout.addView(view, params);
                    deleteBtn.setVisibility(View.VISIBLE);
                }
            }.execute();
        }
    }

    private FrameLayout.LayoutParams createLayoutParams() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.START | Gravity.TOP;
        params.leftMargin = 100;
        params.topMargin = 100;
        return params;
    }

    @Override
    public void deleteLabel() {
        mFrameLayout.removeView(imageViews.remove(curImgViewPosition));
    }

    @Override
    public void addLabel() {
        if (flag) {
            addLabel2UI();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = 0;
                y = 0;
                width = 0;
                height = 0;
                x = (int) event.getX();
                //纵坐标应减去顶部操作栏的高度
                y = (int) event.getY();
                popX = x;
                popY = y;
                if (mScreenShotView.getParent() != null) {
                    ((ViewGroup) mScreenShotView.getParent()).removeView(mScreenShotView);
                }

                if (mScreenShotView.getParent() == null) {
                    mFrameLayout.addView(mScreenShotView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                }
                break;
            case MotionEvent.ACTION_MOVE:
                right = (int) event.getX();
                bottom = (int) event.getY();
                mScreenShotView.setSeat(x, y, right, bottom);
                mScreenShotView.postInvalidate();
                break;
            case MotionEvent.ACTION_UP:
                width = (int) Math.abs(x - event.getX());
                height = (int) Math.abs(y - event.getY());
                if (event.getX() <= x) {
                    x = (int) event.getX();
                }
                if (event.getY() <= y) {
                    y = (int) event.getY();
                }
                bitmap = getBitmap();
                showScreenCutOptWindow();
                break;
        }
        return true;
    }

    private Bitmap bitmap;
    /**
     * 显示截图操作窗口
     */
    private PopupWindow cutPopWindow;
    private int popX;
    private int popY;

    private void showScreenCutOptWindow() {
        View view = View.inflate(mContext, R.layout.screencut_opt_layout, null);
        Button cancelCut = (Button) view.findViewById(R.id.cancel_cut);
        Button copyCut = (Button) view.findViewById(R.id.copy_cut);
        Button sendCut = (Button) view.findViewById(R.id.send_cut);
        Button saveCut = (Button) view.findViewById(R.id.save_cut);
        cancelCut.setOnClickListener(this);
        copyCut.setOnClickListener(this);
        sendCut.setOnClickListener(this);
        saveCut.setOnClickListener(this);

        Log.e(TAG, "screenview's width is : " + mScreenShotView.getWidth() + " : " + mScreenShotView.getCutWidth());
        cutPopWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        // 使其能获得焦点 ，要想监听菜单里控件的事件就必须要调用此方法
        cutPopWindow.setFocusable(true);
        // 设置允许在外点击消失
        cutPopWindow.setOutsideTouchable(true);
        // 设置背景，这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        cutPopWindow.setBackgroundDrawable(new BitmapDrawable());
        //软键盘不会挡着popupwindow
        cutPopWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        Log.e(TAG, "left value is : " + mScreenShotView.getLeftValue() + ",top value is : " + mScreenShotView.getTopValue());
        Log.e(TAG, "screenview's x is : " + x + ",y is : " + y);
        Log.e(TAG, "popX is : " + popX + ", popY is : " + popY + ",width is : " + width);
        //设置菜单显示的位置
        int[] locations = new int[2];
        mScreenShotView.getLocationInWindow(locations);
        Log.e(TAG, "locations is : " + locations[0] + "," + locations[1]);
        int distance = width - 320;
        Log.e(TAG,"width is : " + width + ",view's width is : " + view.getWidth()+",distance is : " + distance);
        cutPopWindow.showAtLocation(mParentLayout, Gravity.NO_GRAVITY, popX , popY + 20);
        //监听触屏事件
        cutPopWindow.setTouchInterceptor(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent event) {
                return false;
            }
        });
    }

    /**
     * 保存截图到SD卡
     */
    private void saveCutOpt() {
        cutCommenOpt();
        saveCutPic();
        ToastUtil.showToast(mContext, R.string.save_cut_success);
    }

    private void saveCutPic() {
        try {
            FileOutputStream fout = new FileOutputStream("mnt/sdcard/img.png");
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fout);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void cutCommenOpt() {
        cutPopWindow.dismiss();
        mScreenShotView.setSign(true);
        mFrameLayout.setInterceptable(true);
        resetScreenView();
    }

    private void resetScreenView() {
        mScreenShotView.setSeat(0, 0, 0, 0);
        mScreenShotView.postInvalidate();
    }

    /**
     * 发送截图
     */
    private void sendCutOpt() {
        ToastUtil.showToast(mContext, R.string.cut_pic_sent);
        cutCommenOpt();
    }

    /**
     * 拷贝截图
     */
    private ClipboardManager clipboardManager;
    private ClipData clipData;

    private void copyCutOpt() {
        if (clipboardManager == null) {
            clipboardManager = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        }
        saveCutPic();
        cutCommenOpt();
        clipData = ClipData.newUri(mContext.getContentResolver(), "uri", Uri.fromFile(new File("mnt/sdcard/img.png")));
        clipboardManager.setPrimaryClip(clipData);

    }

    private void send() {

//        screenShotImg.setImageBitmap(null);
//        clipData = clipboardManager.getPrimaryClip();
//        Uri uri = clipData.getItemAt(0).getUri();
//        Bitmap bitmap = BitmapFactory.decodeFile(uri.getPath());
//        screenShotImg.setImageBitmap(bitmap);
    }

    /**
     * 取消截图操作
     */
    private void cancelCutOpt() {
        cutCommenOpt();
    }


    /**
     * 添加便签到界面中
     */
    private Position curImgViewPosition;//当前控件的位置

    private void addLabel2UI() {
        FrameLayout.LayoutParams params = createLayoutParams();
        final MoveImageView imageView = new MoveImageView(mContext);
        imageView.setImageResource(R.drawable.bianqian_img);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = false;
                useNote();
                FrameLayout.LayoutParams tmp = (FrameLayout.LayoutParams) imageView.getLayoutParams();
                curImgViewPosition = new Position(tmp.leftMargin, tmp.topMargin);
            }
        });
        imageView.setUpdateImageViewMapListener(this);
        mFrameLayout.addView(imageView, params);
        imageViews.put(new Position(params.leftMargin, params.topMargin), imageView);
    }

    /**
     * 获取截取区域内的图片
     */
    private Bitmap getBitmap() {
        View view = mFrameLayout;
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        Rect frame = new Rect();
        view.getWindowVisibleDisplayFrame(frame);
        Bitmap bmp = null;
        Log.e(TAG, "width is : " + width + ",height is : " + height + ",x is : " + x + ",y is : " + y + ",bitmap's width is : " + bitmap.getWidth() + ",bitmap's height is : " + bitmap.getHeight());
        if (width > 0 && height > 0 && x + width <= bitmap.getWidth() && y > 0 && (y <= bitmap.getHeight())) {
            bmp = Bitmap.createBitmap(bitmap, x + 1, y + 1, width - 1, height - 1);
        }
        view.setDrawingCacheEnabled(false);
        return bmp;
    }

    @Override
    public void updateImageViewMap(Position position, MoveImageView imageView) {
        imageViews.put(position, imageView);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        float temp = progress / 100.0f;
        if (seekBar == mPenSizePg) {
            if (temp < 0.1) {
                temp = 0.1f;
            }
            ObjectAnimator animatorX = ObjectAnimator.ofFloat(mPenSizeIv, "scaleX", temp);
            ObjectAnimator animatorY = ObjectAnimator.ofFloat(mPenSizeIv, "scaleY", temp);
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(animatorX, animatorY);
            animatorSet.setDuration(100);
            animatorSet.start();
            mNoteBookView.setPaintSize(temp * 30);
        } else {
            mPenAlphaIv.setAlpha(temp);
            mNoteBookView.setPaintAlpha((int) (temp * 255));
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    private OnSwitcherListener switcherListener;

    public void setOnSwitcherListener(OnSwitcherListener switcherListener) {
        this.switcherListener = switcherListener;
    }

    public interface OnSwitcherListener {
        void switch2TextBookFragment();

        void switch2NoteBookFragment();

        void switch2ExerciseFragment();
    }

}
