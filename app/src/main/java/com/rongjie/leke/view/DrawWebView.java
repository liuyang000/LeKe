package com.rongjie.leke.view;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Administrator on 2016/7/25.
 */
public class DrawWebView extends WebView {
    private final static String HTTP_SCHEME = "http";
    private final static String FILE_SCHEME = "file";

    private Uri mResourceUri;
    private WebViewClient mWebViewClient;

    public DrawWebView(Context context) {
        this(context, null);
    }

    public DrawWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        WebSettings settings = getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        setWebViewClient(createWebViewClient());
        setWebChromeClient(new WebChromeClient());

    }

    protected WebViewClient createWebViewClient() {
        mWebViewClient = new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        };
        return mWebViewClient;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && canGoBack()) {
            goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void load(String url) {
        mResourceUri = new Uri.Builder().scheme(FILE_SCHEME)
                .encodedAuthority("/android_asset")
                .appendEncodedPath(Uri.encode(url, "/"))
                .build();
        loadUrl(mResourceUri.toString());
    }
}
