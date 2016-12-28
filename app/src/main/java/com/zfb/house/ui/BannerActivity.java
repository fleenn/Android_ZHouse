package com.zfb.house.ui;

import android.content.Intent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lemon.LemonActivity;
import com.lemon.annotation.FieldView;
import com.lemon.annotation.Layout;
import com.zfb.house.R;

/**
 * Created by Snekey on 2016/10/14.
 */
@Layout(id = R.layout.activity_banner)
public class BannerActivity extends LemonActivity {

    @FieldView(id = R.id.webView)
    private WebView webView;

    @Override
    protected void initView() {
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        webView.loadUrl(url);
    }

    @Override
    public void onBackPressed() {
        webView.reload();
        super.onBackPressed();
    }
}
