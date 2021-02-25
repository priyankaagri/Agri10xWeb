package com.mobile.agri10x;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.agri10x.Model.UserId;

public class OnlyWebPage extends AppCompatActivity {
    private WebView webView = null;
    public boolean doubleBackToExitPressedOnce = false;
    Toolbar toolbar;
    ProgressDialog progressDialog;
    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_only_web_page);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

           username = extras.getString("namefarmer");
        }

        //toolbar
        toolbar = findViewById(R.id.toolbar_onlyweb);
        setSupportActionBar(toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        if(username.equals("undefined undefined")){
             mTitle.setText("");
        }else{
          mTitle.setText(username);
        }
        toolbar.setNavigationIcon(R.drawable.ic_action_name );

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        this.webView = (WebView) findViewById(R.id.webview);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);

        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        progressDialog = new ProgressDialog(OnlyWebPage.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();



        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl("https://emarket.agri10x.com/");
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(OnlyWebPage.this, "Error:" + description, Toast.LENGTH_SHORT).show();

            }
        });
        webView.loadUrl("https://emarket.agri10x.com/");
    }



    @Override
    public void onBackPressed() {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (webView.canGoBack()) {
                        webView.goBack();
                    } else {

                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }



}