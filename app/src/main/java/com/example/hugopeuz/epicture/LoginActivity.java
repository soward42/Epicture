package com.example.hugopeuz.epicture;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static android.support.v4.content.ContextCompat.startActivity;

class MyWebViewClient extends WebViewClient {
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        Uri uri = Uri.parse(url);
        if (url.contains("app.getpostman.com")) {
            Intent intent = new Intent(view.getContext(), TrendingActivity.class);
            String infos = uri.getEncodedFragment();
            Map<String, String> datas = parseUrlInfos(infos);
            intent.putExtra("USER", new User(datas));
            view.getContext().startActivity(intent);
            return false;
        }
        view.loadUrl(url);
        return false;
    }

    public Map<String, String> parseUrlInfos(String Infos) {
        String splitData[] = Infos.split("&");
        Map<String, String> mapValue = new HashMap<String, String>();
        for (String S : splitData) {
            String tmp[] = S.split("=");
            mapValue.put(tmp[0], tmp[1]);
        }
        return mapValue;
    }
}

public class LoginActivity extends AppCompatActivity {

    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mWebView = findViewById(R.id.LoginWebView);
        mWebView.setWebViewClient(new MyWebViewClient());
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.loadUrl("https://api.imgur.com/oauth2/authorize?client_id=e3d930a3246a674&response_type=token");
    }
}