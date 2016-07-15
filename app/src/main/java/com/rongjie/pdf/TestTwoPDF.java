package com.rongjie.pdf;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.artifex.mupdfdemo.MuPDFCore;
import com.rongjie.leke.R;
import com.rongjie.pdf.View.MuPDFPageView;
import com.rongjie.pdf.bean.OutlineActivityData;
import com.rongjie.pdf.task.AsyncTask;

/**
 * Created by Administrator on 2016/7/5.
 */
public class TestTwoPDF extends Activity implements View.OnClickListener {

    private Button btnpagre1left;
    private Button btnpagre1right;
    private RelativeLayout rlpager1;
    private Button btnpagre2left;
    private Button btnpagre2right;
    private RelativeLayout rlpager2;
    private MuPDFCore mCore;
    private MuPDFPageView pageView1;
    private MuPDFPageView pageView2;
    private int mPages;
    private Button btn_show;
    private Button btn_hide;
    private ScrollView scrollView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);
        btn_show =  (Button) findViewById(R.id.btn_show);
        btn_show.setOnClickListener(this);
        btn_hide =  (Button) findViewById(R.id.btn_hide);
        btn_hide.setOnClickListener(this);

        this.rlpager2 = (RelativeLayout) findViewById(R.id.rl_pager2);
        this.btnpagre2right = (Button) findViewById(R.id.btn_pagre2_right);
        this.btnpagre2left = (Button) findViewById(R.id.btn_pagre2_left);
        btnpagre2right.setOnClickListener(this);
        btnpagre2left.setOnClickListener(this);


        this.rlpager1 = (RelativeLayout) findViewById(R.id.rl_pager1);
        this.btnpagre1right = (Button) findViewById(R.id.btn_pagre1_right);
        this.btnpagre1left = (Button) findViewById(R.id.btn_pagre1_left);
        btnpagre1right.setOnClickListener(this);
        btnpagre1left.setOnClickListener(this);
        this.scrollView2 = (ScrollView) findViewById(R.id.scrollView2);
        btn_hide.setEnabled(false);
        initPdfView();
    }

    private MuPDFCore openFile(String path) {
        MuPDFCore core = null;
        int lastSlashPos = path.lastIndexOf('/');
        String mFileName = new String(lastSlashPos == -1 ? path : path.substring(lastSlashPos + 1));
        System.out.println("Trying to open " + path);

        try {
            // 解析PDF 核心类
            core = new MuPDFCore(this, path);
            // New file: drop the old outline data
            //删除 PDF 目录 ，需要回复数据
            OutlineActivityData.set(null);
        } catch (Exception e) {
            System.out.println(e);
        }
        return core;
    }
    private void initPdfView(){
        Intent intent = getIntent();
        Uri uri = null;
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            System.out.println("cccccccccccc");
            uri = intent.getData();
        }

        mCore = openFile(Uri.decode(uri.getEncodedPath())) ;
         mPages  = mCore.countPages();
        pageView1 = new MuPDFPageView(this,mCore,new Point(getScreenWidth(),getScreenHeight()));
        pageView2 = new MuPDFPageView(this,mCore,new Point(getScreenWidth(),getScreenHeight()));


        rlpager2.addView(pageView2,0);
        rlpager1.addView(pageView1,0);
//        jumpPager(20 ,pageView2);
        jumpPager(10 ,pageView1);

    }

    private  void jumpPager(final int position , final MuPDFPageView pageView){

        if (position > mPages || position<0){
            return;
        }

        pageView.blank(position);
        AsyncTask<Void,Void,PointF> sizingTask = new AsyncTask<Void,Void,PointF>() {

            @Override
            protected PointF doInBackground(Void... arg0) {
                return mCore.getPageSize(position);
            }

            @Override
            protected void onPostExecute(PointF result) {
                super.onPostExecute(result);

                 pageView.setPage(position, result);
//                pageView.setPage(position,  new  PointF(300, 300));
            }
        };
        sizingTask.execute((Void)null);
    }

    public  int getScreenWidth(){
            DisplayMetrics metric = new DisplayMetrics();
            this.getWindowManager().getDefaultDisplay().getMetrics(metric);
            return metric.widthPixels;     // 屏幕宽度（像素）
    }

    public  int getScreenHeight(){
            DisplayMetrics metric = new DisplayMetrics();
            this.getWindowManager().getDefaultDisplay().getMetrics(metric);
            return metric.heightPixels;     // 屏幕高度（像素）
    }
    @Override
    public void onClick(View v) {

       switch (v.getId()) {
            case R.id.btn_pagre1_right:
                jumpPager( pageView1.getPage()+1,pageView1);
                break;

            case R.id.btn_pagre1_left:
                jumpPager( pageView1.getPage()-1,pageView1);
                break;
            case R.id.btn_pagre2_right:
                jumpPager( pageView2.getPage()+1,pageView2);
                break;
            case R.id.btn_pagre2_left:
                jumpPager( pageView2.getPage()-1,pageView2);
                break;
           case R.id.btn_show:
               scrollView2.setVisibility(View.VISIBLE);
               if (pageView2.getPage() !=pageView1.getPage() ){

                   jumpPager( pageView1.getPage(),pageView2);
               }
               btn_hide.setEnabled(true);
               btn_show.setEnabled(false);
               break;
           case R.id.btn_hide:
               scrollView2.setVisibility(View.GONE);
               btn_hide.setEnabled(false);
               btn_show.setEnabled(true);
/*
               JSONObject obj = new JSONObject();
               try {
                    JSONArray arr = new JSONArray() ;
                   arr.put(0,1) ;
                   arr.put(1,2) ;
                   obj.put("arr",arr);
                   System.out.println(obj.toString());
               } catch (JSONException e) {
                   e.printStackTrace();
               }*/
               break ;

        }
    }
}
