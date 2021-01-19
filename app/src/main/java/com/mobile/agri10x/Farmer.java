package com.mobile.agri10x;

import android.annotation.SuppressLint;
import android.content.Context;
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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.mobile.agri10x.Connection.LoadImage;
import com.mobile.agri10x.Connection.POSTRequest;
import com.mobile.agri10x.Model.ErrorLog;
import com.mobile.agri10x.Model.GetBidStatus;
import com.mobile.agri10x.Model.LogOut;
import com.mobile.agri10x.Model.Main;
import com.mobile.agri10x.Model.Product;
import com.mobile.agri10x.Model.ProductAdapter;
import com.mobile.agri10x.Model.SecurityData;
import com.mobile.agri10x.Model.SwipeToDeleteCallback;
import com.mobile.agri10x.Model.User;
import com.mobile.agri10x.Model.UserId;
import com.mobile.agri10x.Model.registrationToken;
import com.mobile.agri10x.SessionManagment.SessionManager;
import com.mobile.agri10x.models.GetRequestedCommodity;
import com.mobile.agri10x.models.GetTradeCommodity;
import com.mobile.agri10x.retrofit.AgriInvestor;
import com.mobile.agri10x.retrofit.ApiHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Farmer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public boolean doubleBackToExitPressedOnce = false;
    View v;
    TextView nav_username,nav_wallet_balance;
    static User user_data_intent;
    ImageView nav_image;
    ImageButton refresh_button;
    SessionManager session;
    DrawerLayout drawer;
    Button tempactive;
    List<GetTradeCommodity> getTradeCommodityArrayList = new ArrayList<>();
    List<GetRequestedCommodity> getRequestedCommodityArrayList = new ArrayList<>();
    //Recycler View
    public static List<Product> productList;
    public static Context context;
    //the recyclerview
    public static RecyclerView recyclerView;
    public static ProductAdapter adapter;
    static String redirection;
    TextView nav_closing_bal;
    TextView no_data_to_show,check1;
    public String city_name;
    User profile;
    private UserId userId;
    public AppCompatImageButton my_stock,add_stock,card_weather,card_request,card_kyc,card_settings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer2);
        Toolbar toolbar = findViewById(R.id.toolbarf);
        setSupportActionBar(toolbar);
        session = new SessionManager(getApplicationContext());
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        v = navigationView.getHeaderView(0);
        nav_username = v.findViewById(R.id.nav_username);
        nav_wallet_balance = v.findViewById(R.id.nav_wallet_balance_farmer);
        nav_image = v.findViewById(R.id.nav_userImage);
        nav_closing_bal = v.findViewById(R.id.nav_closing_bal_farmer);
        refresh_button = v.findViewById(R.id.nav_refresh_button);

        my_stock=findViewById(R.id.my_stock);
        add_stock=findViewById(R.id.add_stock);
        card_weather=findViewById(R.id.card_weather);
        card_request=findViewById(R.id.card_request);
        card_kyc=findViewById(R.id.card_kyc);
        card_settings = findViewById(R.id.settings);

        //tempactive = findViewById(R.id.tempactive);

        navigationView.setNavigationItemSelectedListener(this);
        user_data_intent = (User) getIntent().getSerializableExtra("User_data");
        city_name = user_data_intent.getCity();
        userId = new UserId();
        userId.setUserid(user_data_intent.get_id());
        //city_name = SessionManager.getFirstName();
        //Toast.makeText(this,city_name, Toast.LENGTH_LONG).show();
        nav_username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(Gravity.LEFT);
                //show the profile page ..
                Intent i = new Intent(Farmer.this,ProfilePage.class);
                i.putExtra("myUser",profile);
                startActivity(i);
            }
        });
        nav_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(Gravity.LEFT);
                //show the profile page ..
                Intent i = new Intent(Farmer.this,ProfilePage.class);
                i.putExtra("myUser",profile);
                startActivity(i);
            }
        });
        refresh_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LoadBalanceOnRefreshClick().execute(Main.getIp()+"/getBalance",new Gson().toJson(userId));
            }
        });


        //Recycler View
        context = getApplicationContext();
        //getting the recyclerview from xml
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        productList = new ArrayList<>();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation aniSlide = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.anim_slide_in_right);
                my_stock.startAnimation(aniSlide);
                add_stock.startAnimation(aniSlide);
                card_weather.startAnimation(aniSlide);
                card_request.startAnimation(aniSlide);
                card_kyc.startAnimation(aniSlide);
                card_settings.startAnimation(aniSlide);
            }
        }, 500);

        my_stock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Farmer.this,AllStockList.class);
                i.putExtra("Userid",user_data_intent.get_id());
                startActivity(i);
            }
        });

        add_stock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(SecurityData.getCommodity()==null) {
//                    //new getComodity().execute(Main.getOldUrl() + "/getComm");
//                    callapi();
//                 //   new Farmer.getComodity().execute(Main.getOldUrl() + "/getTradeCommodity", new Gson().toJson(userId));
//                }else{
                    Intent i = new Intent(Farmer.this,AddStock.class);
                    i.putExtra("Userid",user_data_intent.get_id());
                    startActivity(i);
               // }
            }
        });

        card_weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(getApplicationContext(),"Weather",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Farmer.this,Weather.class);
                intent.putExtra("cityname",city_name);
                intent.putExtra("Userid",user_data_intent.get_id());
                startActivity(intent);
            }
        });

        card_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(SecurityData.getCommodity()==null)
//                    new getReqStockComm().execute(Main.getOldUrl() + "/getRequestedCommodity", new Gson().toJson(userId));
//                else{
                    Intent i = new Intent(Farmer.this,RequestStock.class);
                    i.putExtra("Userid",user_data_intent.get_id());
                    i.putExtra("user_type",user_data_intent.getRole());
                    startActivity(i);
             //   }
            }
        });
        card_kyc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent i = new Intent(Farmer.this, UploadDocument.class);
                    i.putExtra("Userid",user_data_intent.get_id());
                    startActivity(i);
            }
        });
        card_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Farmer.this,SettingsActivity.class));
            }
        });

//        tempactive.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent atintent = new Intent(Farmer.this,ActiveTrade.class);
//                atintent.putExtra("Userid",user_data_intent.get_id());
//                startActivity(atintent);
//            }
//        });

        loadData();
        //CreateNewSession(user_data_intent);



//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                GetBidStatus getBidStatus=new GetBidStatus();
//                getBidStatus.setUserid(user_data_intent.get_id());
//                new Farmer.loadStockData().execute(Main.getIp()+"/getFarmerBidStatus",new Gson().toJson(getBidStatus));
//                //new LoadEntitiyData().execute(Main.getIp()+"/UserInfo",new Gson().toJson(getBidStatus));
//            }
//        }, 1200);


    }
    protected void onStart() {
        super.onStart();

        redirection  = getIntent().getStringExtra("notification data");
        if(redirection!=null){
            Intent i = new Intent(Farmer.this,Notification.class);
            i.putExtra("notification data",redirection);
            startActivity(i);
        }
    }

    void loadData(){
        //no need to load data no change in the session manager

        String e = SessionManager.getKEY_Entity_id();
        String s =SessionManager.getUsername();
        //if(SessionManager.getUsername()!=null && user_data_intent.getUsername().equals(SessionManager.getUsername())){
          if(false){
            //do nothing
            if(SessionManager.getImageURI()!=null ){
                final String url = Main.getOldUrl()+ SessionManager.getImageURI();
                //download the image if not present in the sessionManager
                //new downloadImage().execute(url);
                String f1 = SessionManager.getFirstName();

                String fullName = SessionManager.getFirstName()+" "+ SessionManager.getLastName();
                nav_username.setText(fullName);

                if(!SessionManager.getKEY_regToken_bool()){
                    registerTokenAgain();
                }
                //show wallet data
            }
            if(SecurityData.getWithdthrawBalance()==null) {
                //new Farmer.LoadWalletBalance().execute(Main.getIp() + "/getBalance",new Gson().toJson(userId));
            }
            else
            {
                nav_wallet_balance.setText("Withdrawable: "+Main.getIndian_currency()+SecurityData.getWithdthrawBalance());
                nav_closing_bal.setText("Closing: "+Main.getIndian_currency()+SecurityData.getClosingBalance());
            }
        }
        else{
              GetBidStatus getBidStatus=new GetBidStatus();
              getBidStatus.setUserid(user_data_intent.get_id());
              new Farmer.LoadEntitiyData().execute(Main.getIp()+"/UserInfo",new Gson().toJson(getBidStatus));
            //new LoadEntitiyData().execute(Main.getIp()+"/UserInfo");
        }
    }

    public static void setUp(ProductAdapter adapter) {

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new SwipeToDeleteCallback(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    class loadStockData extends AsyncTask<String, Integer, String> {
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
            //check1.setText(s);
            if(s!=null) {
                if(s.equals("network")){
                    new Alert().alert("Network !!!", getResources().getString(R.string.network_error_message));
                }else {
                    Product[] p = Product.extractFeatureFromJson(s);
                    if(p.length>0) {
                        productList.addAll(Arrays.asList(p));
                        adapter = new ProductAdapter(Farmer.this, productList);
                        setUp(adapter);
                    }
                    if (p.length < 1) {
                           Toast.makeText(Farmer.this, "No active trades", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String str;
            try {
                str = POSTRequest.fetchUserData(strings[0],strings[1],Farmer.this);
            } catch (Exception e) {
                return "network";
            }
            return str;
        }
    } // okey

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
            Log.d("loaddata",s);
            dialog.dismiss();
            if(s!=null) {
                if (s.equals("network")) {
                    new Alert().alert("Network !!!", getResources().getString(R.string.network_error_message));
                } else {
                    User u = User.extractFeatureFromJson(s);
                    profile = u;
                    if(u.getFirstname()!=null )
                    {
                        String name = u.getFirstname();
                        if(u.getLastname()!=null)
                            name +=" ";
                            name +=u.getLastname();
                        if(name!=null)
                            nav_username.setText(name);
                    }
                    else{
                        ErrorLog errorLog = new ErrorLog(Main.getIp()+"/UserInfo","Firstname","String",null,"Farmer.LoadEntitiyData->onPostExecute()",getResources().getString(R.string.DeviceName),getClass().getSimpleName());
                        Main.addErrorReportRequest(errorLog,Farmer.this);
                    }
                    //new Farmer.downloadImage().execute(Main.getBaseUrl()+u.getImgUrl());
                    new DownloadImageTask(nav_image).execute(Main.getBaseUrl()+u.getImgUrl());
                    //CreateNewSession(u);
                }
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String str;
            try {
                str = POSTRequest.fetchUserData(strings[0],strings[1], Farmer.this);
            } catch (Exception e) {
                return "network";
            }
            return str;
        }
    } // okey

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
            new Farmer.LoadWalletBalance().execute(Main.getIp() + "/getBalance",new Gson().toJson(userId));
        }
    }

    void CreateNewSession(User u){
        if(u!=null) {
            SessionManager.createLoginSession(u);
            //registerTokenAgain();
            //final String url = Main.getOldUrl() + u.getImgUrl();
            //new downloadImage().execute(url);
            //new LoadWalletBalance().execute(Main.getIp() + "/getBalance");
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
                    String UUID = Main.id(Farmer.this);

                    if(UUID!=null) {
                        registrationToken tokenToServer = new registrationToken(user_data_intent.get_id(), user_data_intent.getRole(), UUID, token_string[0]);
                        new registerToken().execute(Main.getIp()+"/getDetails", new Gson().toJson(tokenToServer));
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
                str = POSTRequest.fetchUserData(strings[0],strings[1],Farmer.this);
            } catch (Exception e) {
                return "network";
            }
            return str;
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
                nav_image.setImageBitmap(bitmap);
                //session.setImage(bitmap);
                SecurityData.setUserImage(bitmap);
            }
            new Farmer.LoadWalletBalance().execute(Main.getIp() + "/getBalance",new Gson().toJson(userId));
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bitmap;
            try {
                bitmap = LoadImage.fetchUserData(strings[0], Farmer.this);
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
                            ErrorLog errorLog = new ErrorLog(Main.getIp()+"/getBalance","Widthraw || Closing","String",null,"Farmer.LoadWalletBalance->onPostExecute()",getResources().getString(R.string.DeviceName),getClass().getSimpleName());
                            Main.addErrorReportRequest(errorLog,Farmer.this);
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
                str = POSTRequest.fetchUserData(strings[0],strings[1], Farmer.this);
            } catch (Exception e) {
                return "network";
            }
            return str;
        }
    } //okey

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
                            ErrorLog errorLog = new ErrorLog(Main.getIp()+"/getBalance","Widthraw || Closing","String",null,"Farmer.LoadBalanceOnRefreshClick->onPostExecute()",getResources().getString(R.string.DeviceName),getClass().getSimpleName());
                            Main.addErrorReportRequest(errorLog,Farmer.this);
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
                str = POSTRequest.fetchUserData(strings[0],strings[1], Farmer.this);
            } catch (Exception e) {
                return "network";
            }
            return str;
        }
    } //okey

    class networkPOST extends AsyncTask<String, Integer, String> {
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
                    //Toast.makeText(Farmer.this, "Data from server: " + s, Toast.LENGTH_LONG).show();
                }
            }


        }

        @Override
        protected String doInBackground(String... strings) {
            String str;
            try{
                str = POSTRequest.fetchUserData(strings[0], strings[1],Farmer.this);
            } catch (Exception e) {
                return "network";
            }
            return str;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
//        if (doubleBackToExitPressedOnce) {
//            Intent a = new Intent(Intent.ACTION_MAIN);
//            a.addCategory(Intent.CATEGORY_HOME);
//            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(a);
//            return;
//        }
//
//        this.doubleBackToExitPressedOnce = true;
//        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
//
//        new Handler().postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//
//                doubleBackToExitPressedOnce=false;
//            }
//        }, 2000);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.farmer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id==R.id.notification_button_farmer){
            Intent i = new Intent(Farmer.this,Notification.class);
            i.putExtra("Userid",user_data_intent.get_id());
            i.putExtra("role",user_data_intent.getRole());
            startActivity(i);
        }

        if(id==R.id.request_stock_farmer){
            if(SecurityData.getCommodity()==null)
                new getReqStockComm().execute(Main.getOldUrl() + "/getRequestCommodity", new Gson().toJson(userId));
            else{
                Intent i = new Intent(Farmer.this,RequestStock.class);
                i.putExtra("Userid",user_data_intent.get_id());
                i.putExtra("user_type",user_data_intent.getRole());
                startActivity(i);
            }
        }


        else if(id==R.id.log_out){
            if(SessionManager.getKEY_regToken_bool()) {
                LogOut out = new LogOut(Main.id(Farmer.this));
                new deleteToken().execute(Main.getIp() + "/deleteToken", new Gson().toJson(out));
            }else
            {
                SessionManager.logoutUser();
            }
            return true;
        }
        else if(id==R.id.action_settings){
//            Intent i = new Intent(Farmer.this, LoginActivity.class);
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            i.putExtra("session expired","true");
//            startActivity(i);
            startActivity(new Intent(Farmer.this,SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.mystockfarmer) {
            Intent i = new Intent(Farmer.this, AllStockList.class);
            i.putExtra("Userid",user_data_intent.get_id());
            startActivity(i);

        }
        else if (id == R.id.addStock) {
            if(SecurityData.getCommodity()==null){
                callapi();
                new Farmer.getComodity().execute(Main.getOldUrl() + "/getTradeCommodity", new Gson().toJson(userId));
            }

            else{
                Intent i = new Intent(Farmer.this,AddStock.class);
                i.putExtra("Userid",user_data_intent.get_id());
                startActivity(i);
            }
        }
        else if(id==R.id.request_stock_farmer){
            if(SecurityData.getCommodity()==null){
                callapi2();
                new getReqStockComm().execute(Main.getOldUrl() + "/getRequestedCommodity", new Gson().toJson(userId));   
            }
            
            else{
                Intent i = new Intent(Farmer.this,RequestStock.class);
                i.putExtra("Userid",user_data_intent.get_id());
                i.putExtra("user_type",user_data_intent.getRole());
                startActivity(i);
            }
        }
        else if (id == R.id.weather) {
            //city_name = user_data_intent.getCity();
            Toast.makeText(getApplicationContext(),"Going to Weather",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Farmer.this,Weather.class);
            intent.putExtra("cityname",city_name);
            intent.putExtra("Userid",user_data_intent.get_id());
            startActivity(intent);

        }
         else if (id == R.id.about_farmer) {
            Intent intent = new Intent(Farmer.this,About.class);
            startActivity(intent);

        }
         else if(id == R.id.tnc_farmer)
        {
            Intent intent = new Intent(Farmer.this,TermsAndConditions.class);
            startActivity(intent);
        }
         else if(id == R.id.atrade){
            GetBidStatus getBidStatus=new GetBidStatus();
            getBidStatus.setUserid(userId.getUserid());
            new Farmer.loadStockData().execute(Main.getIp()+"/getFarmerBidStatus",new Gson().toJson(getBidStatus));
        }
        if(id==R.id.farmer_upload_documents){
            Intent i = new Intent(Farmer.this,UploadDocument.class);
            i.putExtra("Userid",user_data_intent.get_id());
            startActivity(i);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    private void callapi2() {
        AgriInvestor apiService = ApiHandler.getApiService();
        final Call<List<GetRequestedCommodity>> loginCall = apiService.wsgetRequestedCommodity(
                "123456");
        loginCall.enqueue(new Callback<List<GetRequestedCommodity>>() {
            @SuppressLint("WrongConstant")
            @Override
            public void onResponse(Call<List<GetRequestedCommodity>> call,
                                   Response<List<GetRequestedCommodity>> response) {
                Log.d("GetReqTradeCommodity",response.toString());
                if (response.isSuccessful()) {
                    getRequestedCommodityArrayList = response.body();
                    Log.d("getresponsereq", String.valueOf(getRequestedCommodityArrayList.size()));

                    // getTradeCommodityArrayList.add(getTradeCommodity.getCommodity());


                }
            }

            @Override
            public void onFailure(Call<List<GetRequestedCommodity>> call,
                                  Throwable t) {
                Toast.makeText(Farmer.this,"Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void callapi() {



        AgriInvestor apiService = ApiHandler.getApiService();
        final Call<List<GetTradeCommodity>> loginCall = apiService.wsgetTradeCommodity(
                "123456");
        loginCall.enqueue(new Callback<List<GetTradeCommodity>>() {
            @SuppressLint("WrongConstant")
            @Override
            public void onResponse(Call<List<GetTradeCommodity>> call,
                                   Response<List<GetTradeCommodity>> response) {
Log.d("GetTradeCommodity",response.toString());
                if (response.isSuccessful()) {
                    getTradeCommodityArrayList = response.body();
Log.d("getresponse", String.valueOf(getTradeCommodityArrayList.size()));

                   // getTradeCommodityArrayList.add(getTradeCommodity.getCommodity());


                }
            }

            @Override
            public void onFailure(Call<List<GetTradeCommodity>> call,
                                  Throwable t) {
                Toast.makeText(Farmer.this,"Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

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
                str = POSTRequest.fetchUserData(strings[0], strings[1],Farmer.this);
            } catch (Exception e) {
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
                        Intent i = new Intent(Farmer.this, AddStock.class);
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
                str = POSTRequest.fetchUserData(strings[0],strings[1], Farmer.this);
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
                        Intent i = new Intent(Farmer.this, RequestStock.class);
                        i.putExtra("Userid",user_data_intent.get_id());
                        i.putExtra("user_type",user_data_intent.getRole());
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
                str = POSTRequest.fetchUserData(strings[0],strings[1], Farmer.this);

            } catch (Exception e) {
                return "network";
            }
            return str;
        }
    }
    public class Alert {
            public void alert( String title, String body) {
                final AlertDialog.Builder Alert = new AlertDialog.Builder(Farmer.this);
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
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(Farmer.this);
                View mView = getLayoutInflater().inflate(R.layout.alert_progress_spinning, null);
                ProgressBar pb = mView.findViewById(R.id.progressBar);
                mBuilder.setView(mView);
                mBuilder.setCancelable(false);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();
                return dialog;
            }

            public void ChangeIP(){
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(Farmer.this);
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
                            Toast.makeText(Farmer.this,"ip changed to : "+Main.getIp(),Toast.LENGTH_LONG).show();
                            dialog.cancel();
                        }
                        else
                        {
                            Toast.makeText(Farmer.this,"Enter the ip first",Toast.LENGTH_LONG).show();
                        }

                    }
                });
            }
        }
}
