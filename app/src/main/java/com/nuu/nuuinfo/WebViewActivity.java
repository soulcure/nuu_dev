package com.nuu.nuuinfo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;



/**
 * WebView
 *
 * @author modify by YW
 * @version V1.0
 * @date 2016年8月13日
 */
public class WebViewActivity extends BaseActivity {

    private static final String HTTP_PARM = "http://";
    private static final String HTTPS_PARM = "https://";

    public static final String INTENT_TITLE = "INTENT_TITLE";
    public static final String INTENT_URL = "INTENT_URL";

    private WebView webView;
    private ProgressBar progressBar;
    private TextView tvTitle;
    private TextView tv_back;


    // logic
    private String title;
    private String url;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view);

        if (savedInstanceState == null) {
            title = getIntent().getStringExtra(INTENT_TITLE);
            url = getIntent().getStringExtra(INTENT_URL);
        } else {
            title = savedInstanceState.getString(INTENT_TITLE);
            url = savedInstanceState.getString(INTENT_URL);
        }

        if (url.startsWith("www.")) { //没有http://加载不出来
            url = HTTP_PARM + url;
        }

        initView();
        setListener();

        if (!TextUtils.isEmpty(title)) {
            tvTitle.setText(title);
        }

        if (!TextUtils.isEmpty(url)) {
            webView.loadUrl(url);
        }

    }


    public void initView() {
        tv_back = (TextView) findViewById(R.id.tv_back);
        tvTitle = (TextView) findViewById(R.id.tv_title);

        progressBar = (ProgressBar) findViewById(R.id.pb_progress);
        webView = (WebView) findViewById(R.id.web_view);

        // 启用支持javascript
        WebSettings settings = webView.getSettings();
        // 设置可以支持缩放
        settings.setSupportZoom(true);
        settings.setJavaScriptEnabled(true);
        //设置 缓存模式
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        // 开启 DOM storage API 功能
        settings.setDomStorageEnabled(true);

        //todo start
        //设置加载进来的页面自适应手机屏幕
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        //todo end


        // 设置出现缩放工具
        settings.setBuiltInZoomControls(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                return false;
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (progressBar == null)
                    return;

                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(newProgress);
                }
            }
        });


    }


    private void setListener() {
        tv_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(INTENT_TITLE, title);
        outState.putString(INTENT_URL, url);
    }


    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack(); //goBack()表示返回WebView的上一页面
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        webView.removeAllViews();
    }
}
