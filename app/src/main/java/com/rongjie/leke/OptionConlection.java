package com.rongjie.leke;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;

import com.rongjie.leke.adapter.ColorAdapter;
import com.rongjie.leke.decoration.DividerGridItemDecoration;
import com.rongjie.leke.fragment.ShortNoteFragment;
import com.rongjie.leke.util.ImageLoader;
import com.rongjie.leke.util.ToastUtil;
import com.rongjie.leke.view.MoveImageView;
import com.rongjie.leke.view.MyFrameLayout;
import com.rongjie.leke.view.NoteBookView;
import com.rongjie.leke.view.ScreenShotView;
import com.rongjie.pdf.View.MuPDFReaderView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jiangliang on 2016/7/18.
 */
public class OptionConlection implements View.OnClickListener, NoteBookView.OnDoOptListener, NoteBookView.OnUndoOptListener, View.OnTouchListener, SeekBar.OnSeekBarChangeListener, MoveImageView.UpdateImageViewMapListener, ShortNoteFragment.DeleteLabelListener, ShortNoteFragment.AddLabelListener {

    public OptionConlection(Activity context) {
        this.context = context;
    }

    private Button pen;
    private Button pencil;
    private Button oliBlackPen;
    private Button makerPen;
    private Button eraser;
    private Button color;
    private SeekBar penSizePg;
    private ImageView penSize;
    private SeekBar penAlphaPg;
    private ImageView penAlpha;
    private ImageView bookPackage;
    private ImageView gesture;
    private ImageView paintDraw;
    private ImageView drawPic;
    private ImageView label;
    private ImageView image;
    private ImageView screenshot;
    private ImageView send;
    private ImageView undo;
    private ImageView redo;
    private ImageView textbook;
    private ImageView notebook;
    private ImageView exerciseBook;
    private ImageView bookMarker;
    private ImageView directory;
    private ImageView screenShotImg;
    private NoteBookView booknote;
    private LinearLayout paintChoose;
    private LinearLayout bookNeedLayout;
    private LinearLayout optionLayout;
    private ScreenShotView screenShotView;
    private MyFrameLayout frameLayout;
    private Activity context;
    private View root;
    private int x;//绘画开始的横坐标
    private int y;//绘画开始的纵坐标
    private int right;//绘画结束的横坐标
    private int bottom;//绘画结束的纵坐标
    private int width;//绘画的宽度
    private int height;//绘画的高度
    private static final String PROPERTY_NAME = "translationY";
    private static final String TAG = "BaseFragment";
    private Map<Position, MoveImageView> imageViews;//用来存放位置和图标的对应关系
    private MuPDFReaderView pdfReaderView;

    public void setMuPDFReaderView(MuPDFReaderView pdfReaderView) {
        this.pdfReaderView = pdfReaderView;
    }

    public void init() {
        initDatas();
        initViews();
        addListener();
    }

    public void setRootView(View view) {
        root = view;
    }

    private void initDatas() {
        imageViews = new HashMap<>();
    }

    private void initViews() {
        pen = (Button) context.findViewById(R.id.pen);
        pencil = (Button) context.findViewById(R.id.pencil);
        oliBlackPen = (Button) context.findViewById(R.id.oli_black_pen);
        makerPen = (Button) context.findViewById(R.id.maker_pen);
        eraser = (Button) context.findViewById(R.id.eraser);
        color = (Button) context.findViewById(R.id.color);
        penSizePg = (SeekBar) context.findViewById(R.id.pen_size_pg);
        penSize = (ImageView) context.findViewById(R.id.pen_size);
        penAlphaPg = (SeekBar) context.findViewById(R.id.pen_alpha_pg);
        penAlpha = (ImageView) context.findViewById(R.id.pen_alpha);
        paintChoose = (LinearLayout) context.findViewById(R.id.paint_choose);
        bookPackage = (ImageView) context.findViewById(R.id.book_package);
        gesture = (ImageView) context.findViewById(R.id.gesture);
        paintDraw = (ImageView) context.findViewById(R.id.paint_draw);
        drawPic = (ImageView) context.findViewById(R.id.draw_pic);
        label = (ImageView) context.findViewById(R.id.label);
        image = (ImageView) context.findViewById(R.id.image);
        screenshot = (ImageView) context.findViewById(R.id.screenshot);
        send = (ImageView) context.findViewById(R.id.send);
        undo = (ImageView) context.findViewById(R.id.undo);
        redo = (ImageView) context.findViewById(R.id.redo);
        bookMarker = (ImageView) context.findViewById(R.id.bookmark);
        directory = (ImageView) context.findViewById(R.id.directory);
        textbook = (ImageView) context.findViewById(R.id.textbook);
        notebook = (ImageView) context.findViewById(R.id.notebook);
        exerciseBook = (ImageView) context.findViewById(R.id.exercise_book);

        bookNeedLayout = (LinearLayout) context.findViewById(R.id.book_need_layout);
        optionLayout = (LinearLayout) context.findViewById(R.id.opt_layout);
        screenShotView = new ScreenShotView(context);
        bookNeedLayout.setVisibility(View.INVISIBLE);
        undo.setEnabled(false);
        redo.setEnabled(false);

        penSizePg.setProgress(100);
        penAlphaPg.setProgress(100);

    }

    public void initNoteView() {
        screenShotImg = (ImageView) context.findViewById(R.id.screenshot_img);
        booknote = (NoteBookView) root.findViewById(R.id.booknote);
        Log.e("NoteBookView", "view is : " + booknote);
        frameLayout = (MyFrameLayout) root.findViewById(R.id.note_parent);
        booknote.setReDoOptListener(this);
        booknote.setUndoOptListener(this);
        updatePaintColor(Color.BLACK);
        if (null != root) {
            root.setOnTouchListener(this);
        }
    }

    private void addListener() {
        pen.setOnClickListener(this);
        pencil.setOnClickListener(this);
        oliBlackPen.setOnClickListener(this);
        makerPen.setOnClickListener(this);
        eraser.setOnClickListener(this);
        color.setOnClickListener(this);
        bookPackage.setOnClickListener(this);
        gesture.setOnClickListener(this);
        paintDraw.setOnClickListener(this);
        drawPic.setOnClickListener(this);
        label.setOnClickListener(this);
        image.setOnClickListener(this);
        screenshot.setOnClickListener(this);
        send.setOnClickListener(this);
        undo.setOnClickListener(this);
        redo.setOnClickListener(this);
        bookMarker.setOnClickListener(this);
        directory.setOnClickListener(this);
        textbook.setOnClickListener(this);
        notebook.setOnClickListener(this);
        exerciseBook.setOnClickListener(this);
        penSizePg.setOnSeekBarChangeListener(this);
        penAlphaPg.setOnSeekBarChangeListener(this);
    }

    private int chooserHeight;
    private boolean flag = false;//用来标识便签显示是以便签按钮为入口的
    private View view;

    @Override
    public void onClick(View v) {
        if (null != screenShotView && screenShotView.isSign()) {
            screenShotView.setSign(false);
            resetScreenView();
        }
        if (null != view) {
            view.setSelected(false);
        }
        root = pdfReaderView.getCurrentItem();
        pdfReaderView.setInterceptTouch(true);
        initNoteView();
        frameLayout.setInterceptable(false);
        switch (v.getId()) {
            case R.id.pen:
                view = pen;
                updateStatus(Color.BLACK, 2, 255);
                booknote.setPen();
                break;
            case R.id.pencil:
                //TODO:设置画笔为铅笔
                view = pencil;
                break;
            case R.id.oli_black_pen:
                //TODO:设置画笔为油性黑笔
                view = oliBlackPen;
                updateStatus(Color.BLACK, 5, 255);
                booknote.setOilBlackPen();
                break;
            case R.id.maker_pen:
                view = makerPen;
                updateStatus(Color.GREEN, 15, 100);
                booknote.setMakerPen();
                break;
            case R.id.eraser:
                view = eraser;
                booknote.useEraser();
                break;
            case R.id.color:
                view = color;
                showColorSelector();
                break;
            case R.id.book_package:
                //TODO：返回书包界面
                view = bookPackage;
                break;
            case R.id.gesture:
                //TODO:切换为手势控制
                view = gesture;
                pdfReaderView.setInterceptTouch(false);
                break;
            case R.id.paint_draw:
                //TODO：切换为绘制输入
                view = paintDraw;
                chooserHeight = paintChoose.getHeight();
                pdfReaderView.setInterceptTouch(true);
                if (paintChoose.getVisibility() == View.VISIBLE) {
                    outAnimator();
                } else {
                    inAnimatior();
                }
                break;
            case R.id.draw_pic:
                //TODO:使用绘图工具
                view = drawPic;
                break;
            case R.id.label:
                view = label;
                flag = true;
                useNote();
                break;
            case R.id.image:
                view = image;
                useLocalPhoto();
                break;
            case R.id.screenshot:
                Log.e(TAG, "screenshot.....................");
                view = screenshot;
                screenShotView.setSign(true);
                frameLayout.setInterceptable(true);
                screenShotView.postInvalidate();
                break;
            case R.id.send:
                //TODO:发送至服务器
                view = send;
                showCopyCut();
                break;
            case R.id.undo:
                booknote.undo();
                break;
            case R.id.redo:
                booknote.redo();
                break;
            case R.id.bookmark:
                view = bookMarker;
                break;
            case R.id.directory:
                view = directory;
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
        if (null != view && view != undo && view != redo) {
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
        GradientDrawable drawable = (GradientDrawable) penSize.getBackground();
        drawable.setColor(color);
        GradientDrawable alphaDrawable = (GradientDrawable) penAlpha.getBackground();
        alphaDrawable.setColor(color);
        penSizePg.setProgress(progress);
        penAlphaPg.setProgress(alpha);
    }

    /**
     * 显示颜色选择窗口
     */
    private PopupWindow popWindow;

    private void showColorSelector() {
        if (popWindow == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.color_layout, null);
            popWindow = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.color_selector);
            final int[] colors = context.getResources().getIntArray(R.array.colors);
            ColorAdapter adapter = new ColorAdapter(colors, context);
            adapter.setOnItemClickListener(new ColorAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    popWindow.dismiss();
                    updatePaintColor(colors[position]);
                }
            });
            recyclerView.setLayoutManager(new GridLayoutManager(context, 6));
            recyclerView.addItemDecoration(new DividerGridItemDecoration(context));
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
        popWindow.showAtLocation(paintChoose, Gravity.TOP, 0, 2 * paintChoose.getHeight());
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
        GradientDrawable drawable = (GradientDrawable) penSize.getBackground();
        drawable.setColor(color);
        GradientDrawable alphaDrawable = (GradientDrawable) penAlpha.getBackground();
        alphaDrawable.setColor(color);
        booknote.setPaintColor(color);
    }


    /**
     * 画笔选择界面进入动画
     */
    private void inAnimatior() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(paintChoose, PROPERTY_NAME, 0, chooserHeight);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                paintChoose.setVisibility(View.VISIBLE);
            }
        });
        animator.setDuration(500);
        animator.start();
    }

    /**
     * 画笔选择界面退出动画
     */
    private void outAnimator() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(paintChoose, PROPERTY_NAME, chooserHeight, 0);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                paintChoose.setVisibility(View.INVISIBLE);
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
        ((Activity) context).startActivityForResult(intent, 1);
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

    /**
     * 打开便签界面
     */
    private ShortNoteFragment noteFragment;

    protected void useNote() {
        FragmentManager fm = context.getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (noteFragment == null) {
            noteFragment = new ShortNoteFragment();
            noteFragment.setDeleteLabelListener(this);
            noteFragment.setAddLabelListener(this);
            ft.add(R.id.rl_parment, noteFragment);
        } else {
            ft.show(noteFragment);
        }
        ft.commit();
    }

    /**
     * 插入图片至界面
     */
    public void insertPic(final Intent data) {
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                String path = "";
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = context.getContentResolver().query(data.getData(), filePathColumn, null, null, null);
                if (null != cursor) {
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    path = cursor.getString(columnIndex);
                    cursor.close();
                }
                return ImageLoader.loadBmpFromFile(path);
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                WindowManager windowManager = context.getWindowManager();
                DisplayMetrics dm = new DisplayMetrics();
                windowManager.getDefaultDisplay().getMetrics(dm);
                int centerX = dm.widthPixels / 2;
                int centerY = dm.heightPixels / 2;

                View view = View.inflate(context, R.layout.insert_pic_layout, null);
                Button deleteBtn = (Button) view.findViewById(R.id.delete_pic);
                ImageView imagebtn = (ImageView) view.findViewById(R.id.insert_pic);
                FrameLayout.LayoutParams params = createLayoutParams();
                imagebtn.setImageBitmap(bitmap);

                params.height = bitmap.getHeight();
                params.width = bitmap.getWidth();
                params.leftMargin = centerX - params.width / 2;
                params.topMargin = centerY - params.height / 2;

                deleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        frameLayout.removeView((View) v.getParent());
                    }
                });
                frameLayout.addView(view, params);
                deleteBtn.setVisibility(View.VISIBLE);
            }
        }.execute();
    }

    private void getCenterOfScreen() {
        WindowManager windowManager = context.getWindowManager();
        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        int centerX = dm.widthPixels / 2;
        int centerY = dm.heightPixels / 2;
        Log.e(TAG, "centerX is : " + centerX + ",centerY is : " + centerY);
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
        frameLayout.removeView(imageViews.remove(curImgViewPosition));
    }

    @Override
    public void addLabel() {
        if (flag) {
            addLabel2UI();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int optLayoutHeight = optionLayout.getHeight();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = 0;
                y = 0;
                width = 0;
                height = 0;
                x = (int) event.getX();
                //纵坐标应减去顶部操作栏的高度
//                y = (int) event.getY() - optLayoutHeight;
                y = (int) event.getY();
                if (screenShotView.getParent() != null) {
                    ((ViewGroup) screenShotView.getParent()).removeView(screenShotView);
                }

                if (screenShotView.getParent() == null) {
                    frameLayout.addView(screenShotView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                }
                break;
            case MotionEvent.ACTION_MOVE:
                right = (int) event.getX();
//                bottom = (int) event.getY() - optLayoutHeight;
                bottom = (int) event.getY();
                screenShotView.setSeat(x, y, right, bottom);
                screenShotView.postInvalidate();
                break;
            case MotionEvent.ACTION_UP:
                if (event.getX() > x) {
                    width = (int) event.getX() - x;
                } else {
                    width = (int) (x - event.getX());
                    x = (int) event.getX();
                }
                if (event.getY() > y) {
                    height = (int) event.getY() - optLayoutHeight - y;
                } else {
                    height = (int) (y - event.getY() + optLayoutHeight);
                    y = (int) event.getY() - optLayoutHeight;
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

    private void showScreenCutOptWindow() {
        View view = View.inflate(context, R.layout.screencut_opt_layout, null);
        Button cancelCut = (Button) view.findViewById(R.id.cancel_cut);
        Button copyCut = (Button) view.findViewById(R.id.copy_cut);
        Button sendCut = (Button) view.findViewById(R.id.send_cut);
        Button saveCut = (Button) view.findViewById(R.id.save_cut);
        cancelCut.setOnClickListener(this);
        copyCut.setOnClickListener(this);
        sendCut.setOnClickListener(this);
        saveCut.setOnClickListener(this);

        Log.e(TAG, "screenview's width is : " + screenShotView.getWidth() + " : " + screenShotView.getCutWidth());
        cutPopWindow = new PopupWindow(view, screenShotView.getCutWidth(), 40);
        // 使其能获得焦点 ，要想监听菜单里控件的事件就必须要调用此方法
        cutPopWindow.setFocusable(true);
        // 设置允许在外点击消失
        cutPopWindow.setOutsideTouchable(true);
        // 设置背景，这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        cutPopWindow.setBackgroundDrawable(new BitmapDrawable());
        //软键盘不会挡着popupwindow
        cutPopWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        Log.e(TAG, "left value is : " + screenShotView.getLeftValue() + ",top value is : " + screenShotView.getTopValue());
        //设置菜单显示的位置
        int[] locations = new int[2];
        screenShotView.getLocationInWindow(locations);
        cutPopWindow.showAtLocation(screenShotView, Gravity.NO_GRAVITY, screenShotView.getLeftValue(), screenShotView.getTopValue() - 41);
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
        ToastUtil.showToast(context, R.string.save_cut_success);
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
        screenShotView.setSign(true);
        frameLayout.setInterceptable(true);
        resetScreenView();
    }

    private void resetScreenView() {
        screenShotView.setSeat(0, 0, 0, 0);
        screenShotView.postInvalidate();
    }

    /**
     * 发送截图
     */
    private void sendCutOpt() {
        ToastUtil.showToast(context, R.string.cut_pic_sent);
        cutCommenOpt();
    }

    /**
     * 拷贝截图
     */
    private ClipboardManager clipboardManager;
    private ClipData clipData;

    private void copyCutOpt() {
        if (clipboardManager == null) {
            clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        }
        saveCutPic();
        clipData = ClipData.newUri(context.getContentResolver(), "uri", Uri.fromFile(new File("mnt/sdcard/img.png")));
        clipboardManager.setPrimaryClip(clipData);
        cutCommenOpt();
    }

    private void showCopyCut() {
        screenShotImg.setImageBitmap(null);
        clipData = clipboardManager.getPrimaryClip();
        Uri uri = clipData.getItemAt(0).getUri();
        Bitmap bitmap = BitmapFactory.decodeFile(uri.getPath());
        screenShotImg.setImageBitmap(bitmap);
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
        final MoveImageView imageView = new MoveImageView(context);
        imageView.setImageResource(R.drawable.ic_launcher);
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
        frameLayout.addView(imageView, params);
        imageViews.put(new Position(params.leftMargin, params.topMargin), imageView);
    }

    /**
     * 获取截取区域内的图片
     */
    private Bitmap getBitmap() {
        View view = frameLayout;
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        Rect frame = new Rect();
        view.getWindowVisibleDisplayFrame(frame);
        Bitmap bmp = null;
        if (width > 0 && height > 0 && x + width <= bitmap.getWidth() && y > 0 && (y <= bitmap.getHeight()) && (width - 1) > 0 && (height - 1) > 0) {
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
        if (seekBar == penSizePg) {
            if (temp < 0.1) {
                temp = 0.1f;
            }
            ObjectAnimator animatorX = ObjectAnimator.ofFloat(penSize, "scaleX", temp);
            ObjectAnimator animatorY = ObjectAnimator.ofFloat(penSize, "scaleY", temp);
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(animatorX, animatorY);
            animatorSet.setDuration(100);
            animatorSet.start();
            booknote.setPaintSize(temp * 30);
        } else {
            penAlpha.setAlpha(temp);
            booknote.setPaintAlpha((int) (temp * 255));
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
