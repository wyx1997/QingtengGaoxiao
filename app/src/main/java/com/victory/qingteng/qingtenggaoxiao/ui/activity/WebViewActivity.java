package com.victory.qingteng.qingtenggaoxiao.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.victory.qingteng.qingtenggaoxiao.MyApplication;
import com.victory.qingteng.qingtenggaoxiao.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class WebViewActivity extends BaseActivity implements View.OnTouchListener{

    public static final String TAG = "WebViewActivity";
    public static final String INTENT_DETAILS = "intent_details";

    @BindView(R.id.web_view_toolbar)
    Toolbar toolbar;

    @BindView(R.id.web_view_progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.details_refresh_layout)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.web_view_activity_container)
    ConstraintLayout containerLayout;

    @BindView(R.id.web_view_container)
    FrameLayout frameLayout;

    private String url;

    private boolean isStop = false;

    private View videoView;

    private IX5WebChromeClient.CustomViewCallback callback;

    private boolean isLoading = false;

    WebViewClient viewClient = new WebViewClient(){
        @Override
        public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
            if(!isStop){
                isLoading = true;
                progressBar.setVisibility(View.VISIBLE);
                refreshLayout.setRefreshing(false);
            }
        }

        @Override
        public void onPageFinished(WebView webView, String s) {
            if(!isStop){
                isLoading = false;
                progressBar.setVisibility(View.GONE);
            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String s) {
            if (isLoading) {
                webView.loadUrl(s);
            } else {
                addWebView(s);
            }
            return true;
        }
    };

    WebChromeClient chromeClient = new WebChromeClient(){

        @Override
        public void onReceivedTitle(WebView webView, String s) {
            if(!isStop){
                actionBar.setTitle(s);
            }
        }

        @Override
        public void onProgressChanged(WebView webView, int i) {
            if(!isStop){
                progressBar.setProgress(i);
            }
        }

        @Override
        public void onShowCustomView(View view, IX5WebChromeClient.CustomViewCallback customViewCallback) {
            ViewGroup viewGroup = (ViewGroup) containerLayout.getParent();
            viewGroup.removeView(containerLayout);
            viewGroup.addView(view);
            videoView = view;
            callback = customViewCallback;
        }

        @Override
        public void onHideCustomView() {
            if(null != callback){
                callback.onCustomViewHidden();
                callback = null;
            }
            if(null != videoView){
                ViewGroup viewGroup = (ViewGroup) containerLayout.getParent();
                viewGroup.removeView(videoView);
                viewGroup.addView(containerLayout);
            }
        }
    };

    public static void open(Context context, String url){
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(INTENT_DETAILS, url);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.dividerColor));
        }
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        super.onCreate(savedInstanceState);
        addWebView(url);
        setRefreshLayout();
    }

    @Override
    protected void onStart() {
        super.onStart();
        isStop = false;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_web_view;
    }

    @Override
    protected void init() {
        initToolBar();
    }

    @Override
    public void onBackPressed() {
        int count = frameLayout.getChildCount();
        if(count > 1){
            frameLayout.removeView(frameLayout.getChildAt(count-1));
        } else if(((WebView)frameLayout.getChildAt(0)).canGoBack()){
            ((WebView)frameLayout.getChildAt(0)).goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void handleIntent(Intent intent) {
        url = getIntent().getStringExtra(INTENT_DETAILS);
    }

    @Override
    protected void onStop() {
        super.onStop();
        isStop = true;
    }

    @Override
    protected void onDestroy() {
        closeWebView();
        super.onDestroy();
        MyApplication.get().getRefWatcher(this).watch(this);
    }

    private void initToolBar(){
        super.initToolbar(toolbar);
        actionBar.setTitle(R.string.web_view_loading);
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setRefreshLayout(){
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ((WebView)frameLayout.getChildAt(frameLayout.getChildCount()-1)).reload();
            }
        });
        refreshLayout.setOnChildScrollUpCallback(new SwipeRefreshLayout.OnChildScrollUpCallback() {
            @Override
            public boolean canChildScrollUp(@NonNull SwipeRefreshLayout parent, @Nullable View child) {
                return (((WebView)frameLayout.getChildAt(frameLayout.getChildCount()-1))).getWebScrollY() > 0;
            }
        });
    }

    private void closeWebView(){
        for(int i=frameLayout.getChildCount(); i>=0; i--){
            WebView webView = (WebView) frameLayout.getChildAt(i);
            if(null != webView){
                frameLayout.removeView(webView);
                webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
                webView.stopLoading();
                webView.clearHistory();
                webView.clearCache(false);
                webView.destroy();
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void addWebView(String url){
        WebView webView = new WebView(this);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT); //缓存
        webSettings.setSupportZoom(true); //支持缩放
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setUseWideViewPort(true); //屏幕调整到WebView大小
        webSettings.setLoadWithOverviewMode(true); //缩放至屏幕大小
        webView.setWebChromeClient(chromeClient);
        webView.setWebViewClient(viewClient);
        webView.loadUrl(url);
        frameLayout.addView(webView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return !isLoading;
    }

    @Override
    public void showData(Object data) {

    }
}
