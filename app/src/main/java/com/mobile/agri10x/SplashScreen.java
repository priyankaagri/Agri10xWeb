package com.mobile.agri10x;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.mobile.agri10x.Connection.WebkitCookieManagerProxy;

public class SplashScreen extends AppCompatActivity {

    private static final int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ManageCookie();

        //#splashScreen
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(SplashScreen.this,WebPage.class);
                startActivity(intent);
                Animatoo.animateZoom(SplashScreen.this);
                finish();
            }
        },SPLASH_TIME_OUT);

    }
    void ManageCookie(){
        android.webkit.CookieSyncManager.createInstance(getApplicationContext());
        // unrelated, just make sure cookies are generally allowed
        android.webkit.CookieManager.getInstance().setAcceptCookie(true);

        // magic starts here
        WebkitCookieManagerProxy coreCookieManager = new WebkitCookieManagerProxy(null, java.net.CookiePolicy.ACCEPT_ALL);
        java.net.CookieHandler.setDefault(coreCookieManager);
    }
}
