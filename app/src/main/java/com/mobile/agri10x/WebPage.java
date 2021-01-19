package com.mobile.agri10x;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.google.gson.Gson;
import com.mobile.agri10x.Model.LogOut;
import com.mobile.agri10x.Model.Main;
import com.mobile.agri10x.Model.SecurityData;
import com.mobile.agri10x.SessionManagment.SessionManager;

public class WebPage extends AppCompatActivity {
    private WebView webView = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_page);


        this.webView = (WebView) findViewById(R.id.webview);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        WebViewClientImpl webViewClient = new WebViewClientImpl(this);
        webView.setWebViewClient(webViewClient);

        webView.loadUrl("https://www.emarket.agri10x.com/");

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.weblogin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id==R.id.loginweb){
            Intent i = new Intent(WebPage.this,LoginActivity.class);

            startActivity(i);
        }





        return super.onOptionsItemSelected(item);
    }

}