package com.urbanpoint.UrbanPoint.SubscriptionAuxiliries;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.RelativeLayout;

import com.urbanpoint.UrbanPoint.R;

public class TermsofSale extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    RelativeLayout rlBack;
    public static final String TERMS_OF_SALES="Terms_of_Sale.html";
    WebView webView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.termsofsale);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bindViews();

    }

    private void bindViews()
    {
        webView=(WebView)findViewById(R.id.webview);
        rlBack=(RelativeLayout)findViewById(R.id.app_bar_rl_back);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/" + TERMS_OF_SALES);
        webView.setBackgroundColor(Color.TRANSPARENT);
        rlBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
