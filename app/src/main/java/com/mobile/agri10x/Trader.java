package com.mobile.agri10x;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.mobile.agri10x.Connection.GETRequest;
import com.mobile.agri10x.Connection.LoadImage;
import com.mobile.agri10x.Connection.POSTRequest;
import com.mobile.agri10x.Model.ErrorLog;
import com.mobile.agri10x.Model.LogOut;
import com.mobile.agri10x.Model.Main;
import com.mobile.agri10x.Model.SecurityData;
import com.mobile.agri10x.Model.User;
import com.mobile.agri10x.Model.UserId;
import com.mobile.agri10x.Model.registrationToken;
import com.mobile.agri10x.SessionManagment.SessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

public class Trader extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public boolean doubleBackToExitPressedOnce = false;
    SessionManager session;
    View v;
    static User user_data_intent,profiletrader;
    ImageButton refresh_button;
    ImageView farmimg;
    TextView nav_username,nav_wallet_balance;
  //  ImageView nav_image;
    DrawerLayout drawer;
    String city_name="",myrole,myfirstname="";
    static String redirection;
    TextView nav_closing_bal,no_data_to_show;
    UserId userId;
    LinearLayout addstocklayout,kyclayout,weatherlayout,requestlayout,settinglayout,mystocklayout;
    public AppCompatImageButton tadd_stock,tcard_weather,tcard_request,tcard_kyc,card_settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trader_new);//activity_trader
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        session = new SessionManager(Trader.this);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(false);
        toggle.syncState();


        NavigationView navigationView = findViewById(R.id.nav_view);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        navigationView.setNavigationItemSelectedListener(this);
        v = navigationView.getHeaderView(0);
        nav_username = v.findViewById(R.id.nav_trader_username);
        nav_closing_bal = v.findViewById(R.id.nav_closing_bal_trader);
        nav_wallet_balance = v.findViewById(R.id.nav_wallet_balance_trader);
       // nav_image = v.findViewById(R.id.nav_trader_userImage);
        refresh_button = v.findViewById(R.id.nav_refresh_button);

        farmimg = findViewById(R.id.farmimg);
        tadd_stock=findViewById(R.id.tadd_stock);
        tcard_weather=findViewById(R.id.tcard_weather);
        tcard_request=findViewById(R.id.tcard_request);
        tcard_kyc=findViewById(R.id.tcard_kyc);
        card_settings = findViewById(R.id.settings);


        addstocklayout = findViewById(R.id.addstocklayout);
        kyclayout = findViewById(R.id.kyclayout);
        weatherlayout = findViewById(R.id.weatherlayout);
        requestlayout = findViewById(R.id.requestlayout);
        settinglayout = findViewById(R.id.settinglayout);
        mystocklayout = findViewById(R.id.mystocklayout);

        mystocklayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Trader.this,AllStockList.class);
                i.putExtra("Userid",user_data_intent.get_id());
                startActivity(i);
            }
        });
        addstocklayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Trader.this,AddStock.class);
                i.putExtra("Userid",user_data_intent.get_id());
                startActivity(i);
            }
        });
        kyclayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Trader.this,UploadDocument.class);
                i.putExtra("Userid",user_data_intent.get_id());
                startActivity(i);
            }
        });
        weatherlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Trader.this, Weather.class);
                i.putExtra("cityname",city_name);
                i.putExtra("Userid",user_data_intent.get_id());
                startActivity(i);
            }
        });
        requestlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Trader.this,RequestStock.class);
                i.putExtra("user_type",user_data_intent.getRole());
                i.putExtra("Userid",user_data_intent.get_id());
                startActivity(i);
            }
        });

        farmimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Trader.this,OnlyWebPage.class);
                startActivity(i);

            }
        });
        settinglayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Trader.this,SettingsActivity.class));
            }
        });


        user_data_intent = (User) getIntent().getSerializableExtra("User_data");

        userId = new UserId();
        city_name = user_data_intent.getCity();
        userId.setUserid(user_data_intent.get_id());
                myrole = user_data_intent.getRole();
                Log.d("checkmyrole",myrole);

        myfirstname = user_data_intent.getUsername();
        if(myfirstname  != null){
            getSupportActionBar().setTitle(myfirstname);
        }


                if(myrole.equals("PTrader")){
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(Trader.this);
                    builder1.setMessage("Your KYC is pending");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    builder1.setNegativeButton(
                            "",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }


        nav_username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(Gravity.LEFT);
                //show the profile page ..
                Intent i = new Intent(Trader.this, ProfilePage.class);
                i.putExtra("myUser",profiletrader);
                startActivity(i);
            }
        });
//        nav_image.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                drawer.closeDrawer(Gravity.LEFT);
//                //show the profile page ..
//                Intent i = new Intent(Trader.this, ProfilePage.class);
//                i.putExtra("myUser",profiletrader);
//                startActivity(i);
//            }
//        });
        refresh_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Trader.LoadBalanceOnRefreshClick().execute(Main.getIp()+"/getBalance",new Gson().toJson(userId));
                //new LoadBalanceOnRefreshClick().execute(Main.getIp()+"/getBalance");
            }
        });

        tadd_stock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(SecurityData.getCommodity()==null)
//                    //new getComodity().execute(Main.getOldUrl()+"/getComm");
//                    new Trader.getComodity().execute(Main.getOldUrl() + "/getTradeCommodity", new Gson().toJson(userId));
//                else{
                    Intent i = new Intent(Trader.this,AddStock.class);
                    i.putExtra("Userid",user_data_intent.get_id());
                    startActivity(i);
             //   }
            }
        });

        tcard_weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Trader.this, Weather.class);
                i.putExtra("cityname",city_name);
                i.putExtra("Userid",user_data_intent.get_id());
                startActivity(i);
            }
        });

        tcard_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(SecurityData.getCommodity()==null)
//                    new getReqStockComm().execute(Main.getOldUrl() + "/getRequestedCommodity", new Gson().toJson(userId));
//                    //new getComodity().execute(Main.getOldUrl()+"/getComm");
//                else{
                    Intent i = new Intent(Trader.this,RequestStock.class);
                    i.putExtra("user_type",user_data_intent.getRole());
                    i.putExtra("Userid",user_data_intent.get_id());
                    startActivity(i);
            //    }
            }
        });
        tcard_kyc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Trader.this,UploadDocument.class);
                i.putExtra("Userid",user_data_intent.get_id());
                startActivity(i);
            }
        });

        card_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Trader.this,SettingsActivity.class));
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation aniSlide = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.anim_slide_in_right);
                tadd_stock.startAnimation(aniSlide);
                tcard_weather.startAnimation(aniSlide);
                tcard_request.startAnimation(aniSlide);
                tcard_kyc.startAnimation(aniSlide);
                card_settings.startAnimation(aniSlide);
            }
        }, 600);

        loadData();
    }

    protected  void onStart() {
        super.onStart();

        redirection  = getIntent().getStringExtra("notification data");
        if(redirection!=null){
            Intent i = new Intent(Trader.this,Notification.class);
            i.putExtra("notification data",redirection);
            startActivity(i);
        }
    }

    void loadData(){
        //no need to load data no change in the session manager
        //if(SessionManager.getUsername()!=null && user_data_intent.getUsername().equals(SessionManager.getUsername())){
        if(false){
            //do nothing
            if(SessionManager.getImageURI()!=null ){
                final String url = Main.getOldUrl()+ SessionManager.getImageURI();
                //download the image if not present in the sessionManager
                new downloadImage().execute(url);
                String fullName = SessionManager.getFirstName()+" "+ SessionManager.getLastName();
                nav_username.setText(fullName);
                boolean xx= SessionManager.getKEY_regToken_bool();
                if(!SessionManager.getKEY_regToken_bool()){
                    registerTokenAgain();
                }
            }
            if(SecurityData.getWithdthrawBalance()==null) {
                new LoadWalletBalance().execute(Main.getIp() + "/getBalance");
            }
            else
            {
                nav_wallet_balance.setText("Withdrawable: "+Main.getIndian_currency()+SecurityData.getWithdthrawBalance());
                nav_closing_bal.setText("Closing: "+Main.getIndian_currency()+SecurityData.getClosingBalance());
            }
        }
        else{
            new Trader.LoadEntitiyData().execute(Main.getIp()+"/UserInfo",new Gson().toJson(userId));
        }
    }

    class downloadImage extends AsyncTask<String, Integer, Bitmap> {

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if(bitmap!=null){
               // nav_image.setImageBitmap(bitmap);
                SecurityData.setUserImage(bitmap);
            }
            new Trader.LoadWalletBalance().execute(Main.getIp() + "/getBalance",new Gson().toJson(userId));
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bitmap;
            try {
                bitmap = LoadImage.fetchUserData(strings[0], Trader.this);
            } catch (Exception e) {
                return null;
            }
            return bitmap;
        }
    }

    class LoadWalletBalance extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s!=null) {
                if (s.equals("network")) {
                    new Alert().alert("Network !!!", getResources().getString(R.string.network_error_message));
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        SecurityData.setClosingBalance(jsonObject.getString("Closing"));
                        SecurityData.setWithdthrawBalance(jsonObject.getString("Widthraw"));
                        SecurityData.setPendingBalance(jsonObject.getString("Pending"));
                        if(SecurityData.getWithdthrawBalance()!=null && SecurityData.getClosingBalance()!=null) {
                            nav_wallet_balance.setText("Withdrawable: " + Main.getIndian_currency() + " " + SecurityData.getWithdthrawBalance());
                            nav_closing_bal.setText("Closing: " + Main.getIndian_currency() + " " + SecurityData.getClosingBalance());
                        }else{
                            ErrorLog errorLog = new ErrorLog(Main.getIp()+"/getBalance","Widthraw || Closing","String",null,"Trader.LoadWalletBalance->onPostExecute()",getResources().getString(R.string.DeviceName),getClass().getSimpleName());
                            Main.addErrorReportRequest(errorLog,Trader.this);
                        }
                    } catch (JSONException e) {
                        SecurityData.setWithdthrawBalance("0");
                        SecurityData.setClosingBalance("0");
                        nav_wallet_balance.setText("Withdrawable: " +Main.getIndian_currency() + " " + SecurityData.getWithdthrawBalance());
                        nav_closing_bal.setText("Closing: "+Main.getIndian_currency()+" "+SecurityData.getClosingBalance());
                    }
                }
            }
            else{
                SecurityData.setWithdthrawBalance("0");
                SecurityData.setClosingBalance("0");
                nav_wallet_balance.setText("Withdrawable: " +Main.getIndian_currency() + " " + SecurityData.getWithdthrawBalance());
                nav_closing_bal.setText("Closing: "+Main.getIndian_currency()+" "+SecurityData.getClosingBalance());
            }
            registerTokenAgain();
        }

        @Override
        protected String doInBackground(String... strings) {
            String str;
            try {
                str = POSTRequest.fetchUserData(strings[0],strings[1], Trader.this);
            } catch (Exception e) {
                return "network";
            }
            return str;
        }
    }

    class LoadEntitiyData extends AsyncTask<String, Integer, String> {
        AlertDialog dialog;

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new Alert().pleaseWait();
        }

        @Override
        protected void onPostExecute(String s) {        //session Expired = null , "network" (Due to exception) , {...} data
            super.onPostExecute(s);
            dialog.dismiss();
            if(s!=null) {
                if (s.equals("network")) {
                    new Alert().alert("Network !!!", getResources().getString(R.string.network_error_message));
                } else {
                    User u = User.extractFeatureFromJson(s);
                    profiletrader = User.extractFeatureFromJson(s);
                    if(u.getFirstname()!=null){
                        String name =u.getFirstname();
                        if(u.getLastname()!=null){
                            name = u.getFirstname()+" "+u.getLastname();
                        }
                        if(name!=null)
                            nav_username.setText(name);
                    }
                    else{
                        ErrorLog errorLog = new ErrorLog(Main.getIp()+"/UserInfo","Firstname","String",null,"Trader.LoadEntitiyData->onPostExecute()",getResources().getString(R.string.DeviceName),getClass().getSimpleName());
                        Main.addErrorReportRequest(errorLog,Trader.this);
                    }
                //    Picasso.with(Trader.this).load(Main.getBaseUrl()+u.getImgUrl()).into(nav_image);
                    //new Trader.downloadImage().execute(Main.getBaseUrl()+u.getImgUrl());
                    //  new Trader.DownloadImageTask(nav_image).execute(Main.getBaseUrl()+u.getImgUrl());
                    //CreateNewSession(u);
                }
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String str;
            try {
                str = POSTRequest.fetchUserData(strings[0],strings[1] ,Trader.this);
            } catch (Exception e) {
                return "network";
            }
            return str;
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap bmp = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                bmp = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return bmp;
        }
        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
            new Trader.LoadWalletBalance().execute(Main.getIp() + "/getBalance",new Gson().toJson(userId));
        }
    }

    class LoadBalanceOnRefreshClick extends AsyncTask<String, Integer, String> {

        AlertDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new Alert().pleaseWait();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            if(s!=null) {
                if (s.equals("network")) {
                    new Alert().alert("Network !!!", getResources().getString(R.string.network_error_message));
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        SecurityData.setClosingBalance(jsonObject.getString("Closing"));
                        SecurityData.setWithdthrawBalance(jsonObject.getString("Widthraw"));
                        SecurityData.setPendingBalance(jsonObject.getString("Pending"));
                        if(SecurityData.getWithdthrawBalance()!=null && SecurityData.getClosingBalance()!=null) {
                            nav_wallet_balance.setText("Withdrawable: " + Main.getIndian_currency() + " " + SecurityData.getWithdthrawBalance());
                            nav_closing_bal.setText("Closing: " + Main.getIndian_currency() + " " + SecurityData.getClosingBalance());
                        }else{
                            ErrorLog errorLog = new ErrorLog(Main.getIp()+"/getBalance","Widthraw || Closing","String",null,"Trader.LoadBalanceOnRefreshClick->onPostExecute()",getResources().getString(R.string.DeviceName),getClass().getSimpleName());
                            Main.addErrorReportRequest(errorLog,Trader.this);
                        }
                    } catch (JSONException e) {
                        SecurityData.setWithdthrawBalance("0");
                        SecurityData.setClosingBalance("0");
                        nav_wallet_balance.setText("Withdrawable: " +Main.getIndian_currency() + " " + SecurityData.getWithdthrawBalance());
                        nav_closing_bal.setText("Closing: "+Main.getIndian_currency()+" "+SecurityData.getClosingBalance());
                    }
                }
            }
            else{
                SecurityData.setWithdthrawBalance("0");
                SecurityData.setClosingBalance("0");
                nav_wallet_balance.setText("Withdrawable: " +Main.getIndian_currency() + " " + SecurityData.getWithdthrawBalance());
                nav_closing_bal.setText("Closing: "+Main.getIndian_currency()+" "+SecurityData.getClosingBalance());
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String str;
            try {
                str = POSTRequest.fetchUserData(strings[0],strings[1], Trader.this);
            } catch (Exception e) {
                return "network";
            }
            return str;
        }
    } //okey

    void CreateNewSession(User u){
        if(u!=null) {
            SessionManager.createLoginSession(u);
            registerTokenAgain();
            final String url = Main.getOldUrl() + u.getImgUrl();
            new downloadImage().execute(url);
            new LoadWalletBalance().execute(Main.getIp() + "/getBalance");
        }
    }

    void registerTokenAgain(){
        final String[] token_string = new String[1];
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }else {
                            token_string[0] = task.getResult().getToken();
                            String UUID = Main.id(Trader.this);
                            if(UUID!=null) {
                                registrationToken tokenToServer = new registrationToken(SessionManager.getKEY_Entity_id(), SessionManager.getRole(), UUID, token_string[0]);
                                new registerToken().execute(Main.getIp() + "/getDetails", new Gson().toJson(tokenToServer));
                            }
                        }
                    }
                });
    }

    class registerToken extends AsyncTask<String, Integer, String> {

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s!=null) {
                if (s.equals("network")) {
                    new Alert().alert("Network !!!", getResources().getString(R.string.network_error_message));
                } else {
                    SessionManager.setKEY_regToken_bool(true);
                }
            }

        }

        @Override
        protected String doInBackground(String... strings) {
            String str;
            try {
                str = POSTRequest.fetchUserData(strings[0],strings[1], Trader.this);
            } catch (Exception e) {
                e.printStackTrace();
                return "network";
            }
            return str;
        }
    }

    class getComodity extends AsyncTask<String, Integer, String> {
        AlertDialog dialog;

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new Alert().pleaseWait();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            dialog.dismiss();

            if(s!=null) {
                if (s.equals("network")) {
                    new Alert().alert("Network !!!", getResources().getString(R.string.network_error_message));
                } else {
                    JSONArray jsonArray;
                    String[] Commodity = new String[0];
                    try {
                        jsonArray = new JSONArray(s);
                        if (jsonArray != null) {
                            int len = jsonArray.length();
                            Commodity = new String[len];
                            for (int i = 0; i < len; i++) {
                                Commodity[i] = (jsonArray.get(i).toString());
                            }
                        }
                        SecurityData.setCommodity(Commodity);
                        Intent i = new Intent(Trader.this, AddStock.class);
                        i.putExtra("Userid",user_data_intent.get_id());
                        startActivity(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String str      ;
            try {
                str = POSTRequest.fetchUserData(strings[0],strings[1], Trader.this);
                //str = GETRequest.fetchUserData(strings[0], Trader.this);
            } catch (Exception e) {
                return "network";
            }
            return str;
        }
    }

    class getReqStockComm extends AsyncTask<String, Integer, String> {
        AlertDialog dialog;

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new Alert().pleaseWait();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            dialog.dismiss();

            if(s!=null) {
                if (s.equals("network")) {
                    new Alert().alert("Network !!!", getResources().getString(R.string.network_error_message));
                } else {
                    JSONArray jsonArray;
                    String[] Commodity = new String[0];
                    try {
                        jsonArray = new JSONArray(s);
                        if (jsonArray != null) {
                            int len = jsonArray.length();
                            Commodity = new String[len];
                            for (int i = 0; i < len; i++) {
                                Commodity[i] = (jsonArray.get(i).toString());
                            }
                        }
                        SecurityData.setCommodity(Commodity);
                        Intent i = new Intent(Trader.this, RequestStock.class);
                        i.putExtra("user_type",user_data_intent.getRole());
                        i.putExtra("Userid",user_data_intent.get_id());
                        startActivity(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String str;
            try {
                str = POSTRequest.fetchUserData(strings[0],strings[1], Trader.this);

            } catch (Exception e) {
                return "network";
            }
            return str;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.trader, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id==R.id.notification_button_farmer){
            Intent i = new Intent(Trader.this,Notification.class);
            i.putExtra("Userid",user_data_intent.get_id());
            i.putExtra("role",user_data_intent.getRole());
            startActivity(i);
        }

        else if(id== R.id.log_out){
            if(SessionManager.getKEY_regToken_bool()) {
                LogOut out = new LogOut(Main.id(Trader.this));
                new deleteToken().execute(Main.getIp() + "/deleteToken", new Gson().toJson(out));
            }else
            {
                SessionManager.logoutUser();
            }
            return true;
        }
        else if(id== R.id.action_settings){
            startActivity(new Intent(Trader.this,SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.trader_wallet) {
            Intent i = new Intent(Trader.this, Wallet.class);
            i.putExtra("Userid",user_data_intent.get_id());
            startActivity(i);

        }

        if (id == R.id.mystockfarmer) {
            Intent i=new Intent(Trader.this,AllStockList.class);
            i.putExtra("Userid",user_data_intent.get_id());
            startActivity(i);

        }
        if(id == R.id.request_stock_trader){
//            if(SecurityData.getCommodity()==null)
//                new getReqStockComm().execute(Main.getOldUrl() + "/getRequestedCommodity", new Gson().toJson(userId));
//            else{
                Intent i = new Intent(Trader.this,RequestStock.class);
                i.putExtra("user_type",user_data_intent.getRole());
                i.putExtra("Userid",user_data_intent.get_id());
                startActivity(i);
         //   }
        }
        if(id == R.id.add_stock_trader)
        {
//            if(SecurityData.getCommodity()==null)
//                new Trader.getComodity().execute(Main.getOldUrl() + "/getTradeCommodity", new Gson().toJson(userId));
//            else{
                Intent i = new Intent(Trader.this,AddStock.class);
                i.putExtra("Userid",user_data_intent.get_id());
                startActivity(i);
           // }
        }
        if (id == R.id.weather) {
            Intent i = new Intent(Trader.this, Weather.class);
            Log.d("params",city_name+" "+user_data_intent.get_id());
            i.putExtra("cityname",city_name);
            i.putExtra("Userid",user_data_intent.get_id());
            startActivity(i);
        }
        else if (id == R.id.about_trader) {
            Intent intent = new Intent(Trader.this,About.class);
            startActivity(intent);
        }
        else if(id == R.id.tnc_trader)
        {
            Intent intent = new Intent(Trader.this,TermsAndConditions.class);
            startActivity(intent);
        }
        if(id==R.id.trader_upload_documents){
            Intent i = new Intent(Trader.this,UploadDocument.class);
            i.putExtra("Userid",user_data_intent.get_id());
            startActivity(i);
        }

        else  if(id == R.id.logout){
            if(SessionManager.getKEY_regToken_bool()) {
                LogOut out = new LogOut(Main.id(Trader.this));
                new Trader.deleteToken().execute(Main.getIp() + "/deleteToken", new Gson().toJson(out));
            }else
            {
                SessionManager.logoutUser();
            }
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    class deleteToken extends AsyncTask<String, Integer, String> {
        AlertDialog dialog;

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new Alert().pleaseWait();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            dialog.dismiss();
            if(s!=null) {
                if (s.equals("network")) {
                    new Alert().alert("Network !!!", getResources().getString(R.string.network_error_message));
                } else {
//                    Toast.makeText(Farmer.this, "Data from server: " + s, Toast.LENGTH_LONG).show();
                    SessionManager.logoutUser();
                }
            }

        }

        @Override
        protected String doInBackground(String... strings) {
            String str;
            try{
                str = POSTRequest.fetchUserData(strings[0], strings[1],Trader.this);
            } catch (Exception e) {
                return "network";
            }
            return str;
        }
    }

    public class Alert {
        public void alert( String title, String body) {
            final AlertDialog.Builder Alert = new AlertDialog.Builder(Trader.this);
            Alert.setCancelable(false)
                    .setTitle(title)
                    .setMessage(body);
            Alert.setNegativeButton("Okey", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            Alert.create().show();
        }

        public AlertDialog pleaseWait() {
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(Trader.this);
            View mView = getLayoutInflater().inflate(R.layout.alert_progress_spinning, null);
            ProgressBar pb = mView.findViewById(R.id.progressBar);
            mBuilder.setView(mView);
            mBuilder.setCancelable(false);
            final AlertDialog dialog = mBuilder.create();
            dialog.show();
            return dialog;
        }

        public void ChangeIP(){
            final AlertDialog.Builder mBuilder = new AlertDialog.Builder(Trader.this);
            View mView = getLayoutInflater().inflate(R.layout.change_ip, null);
            final EditText ip = mView.findViewById(R.id.ip);
            Button mOkey = mView.findViewById(R.id.enter);
            mBuilder.setView(mView);
            mBuilder.setCancelable(true);
            final AlertDialog dialog = mBuilder.create();
            dialog.show();
            ip.setText(Main.getIp());
            mOkey.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String str = ip.getText().toString().trim();
                    if(str.length()>0)
                    {
                        Main.setIp(str);
                        Toast.makeText(Trader.this,"ip changed to : "+ Main.getIp(),Toast.LENGTH_LONG).show();
                        dialog.cancel();
                    }
                    else
                    {
                        Toast.makeText(Trader.this,"Enter the ip first",Toast.LENGTH_LONG).show();
                    }

                }
            });
        }
    }
}
