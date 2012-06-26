package com.company;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import com.apx.speakersknowledge.R;

public class ContentActivity extends Activity {

    private static final String TAG = ContentActivity.class.getSimpleName();
    public static final String CONTENT_URL_KEY = "com.company.CONTENT_URL";

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content);
        webView = (WebView) findViewById(R.id.webview);
        Button saveButton = (Button) findViewById(R.id.save_button);
        saveButton.setOnClickListener(onClickGoBack());
        Button notNowButton = (Button) findViewById(R.id.not_now_button);
        notNowButton.setOnClickListener(onClickGoBack());
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
    }

    private OnClickListener onClickGoBack() {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        String url = intent.getStringExtra(CONTENT_URL_KEY);
        Log.i(TAG, String.format("Loading url %s", url));
        webView.loadUrl(url);
    }

}
