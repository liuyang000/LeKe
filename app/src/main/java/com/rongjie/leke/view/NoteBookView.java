package com.rongjie.leke.view;

/**
 * Created by jiangliang on 2016/6/28.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.rongjie.pdf.utils.Uiutils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiangliang on 2016/6/27.
 */
public class NoteBookView extends View {
    private static final String TAG = "NoteBookView";
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Path mPath;
    private Paint mBitmapPaint;// 画布的画笔
    private Paint mPaint;// 真实的画笔
    private float mX, mY;// 临时点坐标
    private static final float TOUCH_TOLERANCE = 4;
    // 保存Path路径的集合
    private static List<DrawPath> savePath;
    // 记录Path路径的对象
    private DrawPath dp;
    private int screenWidth = 1000, screenHeight = 1000;// 屏幕長寬
    private OnUndoOptListener undoOptListener;
    private OnDoOptListener redoOptListener;
    //该标志用于判断是否需要移除索引后面的操作
    private boolean flag;
    private Context mContext;

    private class DrawPath {
        public Path path;// 路径
        public Paint paint;// 画笔
    }

    public NoteBookView(Context context) {
        super(context);
        mContext = context ;
        init();
    }

    public NoteBookView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context ;
        init();
    }

    public interface OnUndoOptListener {
        void disenableUndoView();

        void enableUndoView();
    }

    public interface OnDoOptListener {
        void disenableReDoView();

        void enableReDoView();
    }

    public void setUndoOptListener(OnUndoOptListener listener) {
        undoOptListener = listener;
    }

    public void setReDoOptListener(OnDoOptListener listener) {
        redoOptListener = listener;
    }

    private void disenableUndoView() {
        if (null != undoOptListener) {
            undoOptListener.disenableUndoView();
        }
    }

    private void enableUndoView() {
        if (null != undoOptListener) {
            undoOptListener.enableUndoView();
        }
    }

    private void disenableRedoView() {
        if (null != redoOptListener) {
            redoOptListener.disenableReDoView();
        }
    }

    private void enableReDoView() {
        if (null != redoOptListener) {
            redoOptListener.enableReDoView();
        }
    }

    private void init() {
        screenWidth= Uiutils.getScreenWidth(mContext) ;
        screenHeight=Uiutils.getScreenHeight(mContext);
        mBitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);
        // 保存一次一次绘制出来的图形
        mCanvas = new Canvas(mBitmap);
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
//        mBitmapPaint.setAntiAlias(true);
//        mBitmapPaint.setFilterBitmap(true);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
//        mPaint.setFilterBitmap(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);// 设置外边缘
        mPaint.setStrokeCap(Paint.Cap.SQUARE);// 形状
        mPaint.setStrokeWidth(5);// 画笔宽度
        savePath = new ArrayList<>();

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.e(TAG,"onDetachedFromWindow............" + this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.e(TAG,"onAttachedToWindow..............." + this);
        setPen();
    }

    @Override
    public void onDraw(Canvas canvas) {
//        canvas.drawColor(0xFFAAAAAA);
        canvas.drawColor(0x00000000);
        // 将前面已经画过得显示出来
        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        if (mPath != null) {
            // 实时的显示
            if (flagOfErase){
                mCanvas.drawPath(mPath,mPaint);
            }else {
                canvas.drawPath(mPath, mPaint);
            }
        }
    }

    private void touch_start(float x, float y) {
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(mY - y);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            // 从x1,y1到x2,y2画一条贝塞尔曲线，更平滑(直接用mPath.lineTo也是可以的)
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
//        mCanvas.drawPath(mPath, mPaint);
    }

    private void touch_up() {
        mPath.lineTo(mX, mY);
        mCanvas.drawPath(mPath, mPaint);
        enableUndoView();
        disenableRedoView();
        //保存路径
        savePath.add(dp);
        mPath = null;// 重新置空
    }


    private int index = -1;

    /**
     * 重新设置画布，相当于清空画布
     */
    private void resetCanvas() {
        mBitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);
        mCanvas.setBitmap(mBitmap);
    }

    /**
     * undo的核心思想就是将画布清空，将索引在保存下来的Path路径中进行递减，重新将索引之前的所有路径画在画布上面。
     */
    public void undo() {
        flag = true;
        resetCanvas();
        if (savePath != null && savePath.size() > 0) {
            //如果没有做过undo操作，则将索引置为集合末位
            if (index == -1) {
                index = savePath.size() - 1;
            }
            index--;
            if (index == -1) {
                disenableUndoView();
            }
            Log.e(TAG, "undo index is : " + index + ", size is : " + savePath.size());
            enableReDoView();
            for (int i = 0; i <= index; i++) {
                DrawPath drawPath = savePath.get(i);
                Log.e(TAG, "undo path is : " + drawPath);
                mCanvas.drawPath(drawPath.path, drawPath.paint);
            }
            invalidate();
        }
    }

    /**
     * redo的核心思想就是索引递增，得到该位置的路径和画笔，画到画布上即可。
     */
    public void redo() {
        flag = true;
        //如果撤销你懂了的话，那就试试重做吧。
        index++;
        if (index == savePath.size() - 1) {
            disenableRedoView();
        }
        enableUndoView();
        DrawPath drawPath = savePath.get(index);
        mCanvas.drawPath(drawPath.path, drawPath.paint);
        invalidate();
    }

    public void clear() {
        index = -1;
        savePath.clear();
        disenableRedoView();
        disenableUndoView();
        resetCanvas();
        invalidate();
    }

    /**
     * 保存图片
     */
    public void save() {
        String fileUrl = Environment.getExternalStorageDirectory().toString() + "/android/data/note.png";
        try {
            FileOutputStream fos = new FileOutputStream(new File(fileUrl));
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private float preX;
    private float preY;
    private float currentX;
    private float currentY;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //如果flag为true，则说明进行了undo、redo操作，这时再进行按下操作时应将索引后面的path清除掉
                if (flag) {
                    int size = savePath.size();
                    for (int i = size - 1; i > index; i--) {
                        savePath.remove(i);
                    }
                }
                flag = false;
                index = -1;
                // 每次down下去重新new一个Path
                mPath = new Path();
                //每一次记录的路径对象是不一样的
                dp = new DrawPath();
                dp.path = mPath;
                dp.paint = new Paint(mPaint);
                preX = x;
                preY = y;
                touch_start(x, y);
                invalidate();
                Log.e(TAG,"action_down..............(x,y) is " + x +"," + y);
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);
                currentX = x;
                currentY = y;
                preX = currentX;
                preY = currentY;
                invalidate();
                Log.e(TAG,"action_move..............(x,y) is " + x +"," + y);
                break;
            case MotionEvent.ACTION_UP:
                touch_up();
                invalidate();
                Log.e(TAG,"action_up..............(x,y) is " + x +"," + y);
                break;
        }
        return true;
    }

    public void setPaintSize(float width){
        mPaint.setStrokeWidth(width);
    }
    public void setPaintAlpha(int value){
        mPaint.setAlpha(value);
    }
    public void setPaintColor(int color){
        mPaint.setColor(color);
        setColorPenAlpha();
    }
    public void setPen() {
        mPaint.setXfermode(null);
        isColorPen = false;
        mPaint.setStrokeWidth(2.0f);
        mPaint.setColor(Color.BLACK);
    }

    public void setOilBlackPen() {
        mPaint.setXfermode(null);
        isColorPen = false;
        mPaint.setStrokeWidth(5.0f);
        mPaint.setColor(Color.BLACK);
    }

    private void setColorPenAlpha() {
        mPaint.setXfermode(null);
        if (isColorPen) {
            mPaint.setAlpha(100);
        }
    }

    private boolean isColorPen;

    public void setMakerPen() {
        isColorPen = true;
        flagOfErase = false;
        mPaint.setXfermode(null);
        mPaint.setStrokeWidth(15.0f);
        mPaint.setColor(Color.GREEN);
        mPaint.setAlpha(100);
    }
    private boolean flagOfErase;
    public void useEraser() {
        flagOfErase = true;
        mPaint.setAntiAlias(true);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        mPaint.setStrokeWidth(20);
    }
}