package com.rongjie.leke.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import com.rongjie.leke.R;

/**
 * Created by jiangliang on 2016/8/1.
 */
public class WebViewActivity extends Activity{

    private WebView mWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_layout);
        mWebView = (WebView) findViewById(R.id.webview);
    }

    public void drawTriangle(View view){

    }

    public void drawCircle(View view){

    }

    public void drawRectangle(View view){
        
    }


}
