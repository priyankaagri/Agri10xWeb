package com.mobile.agri10x;

import androidx.annotation.NonNull;
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

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mobile.agri10x.Model.User;
import com.mobile.agri10x.Model.UserId;

public class OnlyWebPage extends AppCompatActivity {
    private WebView webView = null;
    public boolean doubleBackToExitPressedOnce = false;
    Toolbar toolbar;
    ProgressDialog progressDialog;
    String username;
    static User user_data_intent;
    public static BottomNavigationView bottomNavigation;
    String userid,getcity,getrole;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_only_web_page);

//        Bundle extras = getIntent().getExtras();
//        if (extras != null) {
//
//           username = extras.getString("namefarmer");
//           userid =  extras.getString("userid");
//        }

        //toolbar
        toolbar = findViewById(R.id.toolbar_onlyweb);
        setSupportActionBar(toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        user_data_intent = (User) getIntent().getSerializableExtra("User_data");
        userid= user_data_intent.get_id();
        username= user_data_intent.getUsername();
        getcity = user_data_intent.getCity();
        getrole = user_data_intent.getRole();
        if(username.equals("undefined undefined")){
             mTitle.setText("");
        }else{
          mTitle.setText(username);
        }
//        toolbar.setNavigationIcon(R.drawable.ic_action_name );
//
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
        this.webView = (WebView) findViewById(R.id.webview);
        bottomNavigation = findViewById(R.id.nav_view);
        bottomNavigation.setItemIconTintList(null);
        bottomNavigation.getMenu().getItem(0).setChecked(true);
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


        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                bottomNavigation.getMenu().getItem(0).setIcon(R.mipmap.addstockbn);
                bottomNavigation.getMenu().getItem(1).setIcon(R.mipmap.mystock);
                bottomNavigation.getMenu().getItem(2).setIcon(R.mipmap.newpayment);
                bottomNavigation.getMenu().getItem(3).setIcon(R.mipmap.menu);
                switch (menuItem.getItemId()) {
                    case R.id.navigation_addstock:
                        menuItem.setIcon(R.mipmap.addstockbn);
                        Intent intent = new Intent(OnlyWebPage.this, AddStock.class);
                        intent.putExtra("Userid",userid);
                        intent.putExtra("username",username);
                        startActivity(intent);
                        break;
                    case R.id.navigation_mystock:
                        menuItem.setIcon(R.mipmap.mystock);
                        Intent imystock = new Intent(OnlyWebPage.this, AllStockList.class);
                        imystock.putExtra("Userid",userid);
                        imystock.putExtra("username",username);
                        startActivity(imystock);
                        break;
                    case R.id.navigation_payment:
                        menuItem.setIcon(R.mipmap.newpayment);
                        Intent i = new Intent(OnlyWebPage.this, PaymentBalance.class);
                        i.putExtra("Userid",userid);
                        startActivity(i);
                        break;
                    case R.id.navigation_menu:
                        menuItem.setIcon(R.mipmap.menu);
                        Intent intent1 = new Intent(OnlyWebPage.this, MenuActivity.class);
                        intent1.putExtra("Userid",userid);
                        intent1.putExtra("username",username);
                        intent1.putExtra("city", getcity);
                        intent1.putExtra("role",getrole);

                      //  Log.d("params",userid+" "+username+" "+getcity+" "+getcity);
                        startActivity(intent1);
                        break;
                }
                return true;
            }
        });
    }



    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);

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