package com.mobile.agri10x;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.mobile.agri10x.Connection.GETRequest;
import com.mobile.agri10x.Connection.LoadImage;
import com.mobile.agri10x.Connection.POSTRequest;
import com.mobile.agri10x.Model.ErrorLog;
import com.mobile.agri10x.Model.GetBidStatus;
import com.mobile.agri10x.Model.LogOut;
import com.mobile.agri10x.Model.Main;
import com.mobile.agri10x.Model.SecurityData;
import com.mobile.agri10x.Model.UnverifiedUserModels.DemoFragmentCollectionAdapter;
import com.mobile.agri10x.Model.UnverifiedUserModels.Information;
import com.mobile.agri10x.Model.UnverifiedUserModels.Information_Adapter;
import com.mobile.agri10x.Model.User;
import com.mobile.agri10x.Model.UserId;
import com.mobile.agri10x.SessionManagment.SessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

//import com.here.android.mpa.common.GeoCoordinate;


public class Unvrified extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    static View v;
    TextView nav_username;
    ImageView nav_image;
    public static Context context;
    public static RecyclerView recyclerView;
    public static List<Information> productList;
    public static Information_Adapter adapter_recyclerview;
    private ViewPager viewPager;
    private DemoFragmentCollectionAdapter adapter;
    DrawerLayout drawer;
    User user_data_intent,profile;
    public String city_name;
    LinearLayout requestlayout,kyclayout;
    public AppCompatImageButton ucard_request,ucard_kyc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unvrified_new);//activity_unvrified
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView  = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        v = navigationView.getHeaderView(0);
        nav_username = v.findViewById(R.id.nav_unverified_username);
        nav_image = v.findViewById(R.id.nav_unverified_image);


        requestlayout=findViewById(R.id.requestlayout);
        kyclayout=findViewById(R.id.kyclayout);

        ucard_kyc=findViewById(R.id.ucard_kyc);
        ucard_request=findViewById(R.id.ucard_request);
        context = Unvrified.this;
        user_data_intent = (User) getIntent().getSerializableExtra("User_data");
        UserId userId=new UserId();
        userId.setUserid(user_data_intent.get_id());
        city_name = user_data_intent.getCity();

        requestlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Unvrified.this,RequestStock.class);
                i.putExtra("Userid",user_data_intent.get_id());
                i.putExtra("user_type",user_data_intent.getRole());
                startActivity(i);
            }
        });
        kyclayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Unvrified.this,UploadDocument.class);
                i.putExtra("Userid",userId.getUserid());
                startActivity(i);
            }
        });
        ucard_kyc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Unvrified.this,UploadDocument.class);
                i.putExtra("Userid",userId.getUserid());
                startActivity(i);
            }
        });


        ucard_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(SecurityData.getCommodity()==null) {
//                    new Unvrified.getComodity().execute(Main.getOldUrl()+"/getComm",new Gson().toJson(userId));
//                    //new getComodity().execute(Main.getOldUrl()+"/getComm");
//                }else{
                    Intent i = new Intent(Unvrified.this,RequestStock.class);
                    i.putExtra("Userid",user_data_intent.get_id());
                    i.putExtra("user_type",user_data_intent.getRole());
                    startActivity(i);
               // }
            }
        });

        nav_username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(Gravity.LEFT);
                Intent i = new Intent(Unvrified.this,ProfilePage.class);
                i.putExtra("myUser",profile);
                startActivity(i);
            }
        });

        nav_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(Gravity.LEFT);
                //show the profile page ..
                Intent i = new Intent(Unvrified.this,ProfilePage.class);
                i.putExtra("myUser",profile);
                startActivity(i);
            }
        });
        /*viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                // Toast.makeText(MainActivity.this,i+"",Toast.LENGTH_LONG).show();
                if(i == 0)
                {
                    //Recycler View
                    //initializing the productlist
                    //initializing the productlist
                    productList = new ArrayList<>();
                    productList.add(new Information(
                            "Trade",
                            "Welcome to Agri10x.\nWe will provide you reliable trade.\nWe will take you to new heights."
                    ));

                    productList.add(new Information(
                            "Specifications",
                            "1.Trade between Farmers and Traders.\n2.Trade between Traders and Traders."
                    ));

                    adapter_recyclerview = new Information_Adapter(Unvrified.context, productList);
                    Unvrified.recyclerView.setAdapter(adapter_recyclerview);
                    Unvrified.recyclerView.setLayoutManager(new LinearLayoutManager(Unvrified.context));


                }
                if(i==1)
                {
                    //initializing the productlist
                    productList = new ArrayList<>();
                    productList.add(new Information(
                            "Payment",
                            "Welcome to Agri10x.\nWe will provide you gateway for payment."
                    ));

                    productList.add(new Information(
                            "Specification",
                            "1.We will provide you payment system.\n2.Use our Payment Gateway"
                    ));


                    adapter_recyclerview = new Information_Adapter(Unvrified.context, productList);
                    Unvrified.recyclerView.setAdapter(adapter_recyclerview);
                    Unvrified.recyclerView.setLayoutManager(new LinearLayoutManager(Unvrified.context));

                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });*/

        loadData();
    }



    void loadData(){
        GetBidStatus getBidStatus=new GetBidStatus();
        getBidStatus.setUserid(user_data_intent.get_id());
        new LoadEntitiyData().execute(Main.getIp()+"/UserInfo",new Gson().toJson(getBidStatus));
        //new LoadEntitiyData().execute(Main.getIp()+"/UserInfo");
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
                    new Alert().alert("Image", getResources().getString(R.string.network_error_message));
                } else {
                    User u = User.extractFeatureFromJson(s);
                    profile = u;
                    String name="";
                    if(u.getFirstname()!=null )
                    {
                        name += u.getFirstname();
                        if(u.getLastname()!=null)
                            name += " " + u.getLastname();
                    }
                    else{
                        ErrorLog errorLog = new ErrorLog(Main.getIp()+"/UserInfo","Firstname","String",null,"Unvrified.LoadEntitiyData->onPostExecute()",getResources().getString(R.string.DeviceName),getClass().getSimpleName());
                        Main.addErrorReportRequest(errorLog,Unvrified.this);
                    }
                    if(name!=null)
                        nav_username.setText(name);
                    if(u.getImgUrl().length()>5){
                        Picasso.with(Unvrified.this).load(Main.getBaseUrl()+u.getImgUrl()).into(nav_image);
                       // new DownloadImageTask(nav_image).execute(Main.getBaseUrl()+u.getImgUrl());
                    }

                    //new downloadImage().execute(Main.getImageUrl()+u.getImgUrl());
                    /*try {
                        Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(Main.getImageUrl()+u.getImgUrl()).getContent() );
                        if(bitmap!=null){
                            nav_image.setImageBitmap(bitmap);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Bitmap bitmap;
                    try {
                        URL url = new URL(Main.getImageUrl()+u.getImgUrl());
                        InputStream inputStream = new BufferedInputStream(url.openStream());
                        bitmap=BitmapFactory.decodeStream(inputStream);
                        nav_image.setImageBitmap(bitmap);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/
                }
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String str;
            try {
                str = POSTRequest.fetchUserData(strings[0],strings[1], Unvrified.this);
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
        }
    }


    /*class downloadImage extends AsyncTask<String, Integer, Bitmap> {

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
            }
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bitmap;
            try {
                bitmap = LoadImage.fetchUserData(strings[0], Unvrified.this);
            } catch (Exception e) {
                return null;
            }
            return bitmap;
        }
    }*/


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.unvrified, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.nav_logout_unverified) {
            Intent i = new Intent(Unvrified.this,WebPage.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            return true;
        }
        else if(id==R.id.action_settings){
            startActivity(new Intent(Unvrified.this,SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_unverified_weather) {
//            startActivity(new Intent(Unvrified.this,Weather.class));
            Intent intent = new Intent(Unvrified.this,Weather.class);
            Log.d("getparams",city_name+" "+user_data_intent.get_id());
            intent.putExtra("cityname",city_name);
            intent.putExtra("Userid",user_data_intent.get_id());
            startActivity(intent);
        } else if (id == R.id.nav_about) {
            startActivity(new Intent(Unvrified.this,About.class));
        } else if (id == R.id.nav_tnc) {
            startActivity(new Intent(Unvrified.this,TermsAndConditions.class));
        } else if(id==R.id.unverified_upload_documents) {
            startActivity(new Intent(Unvrified.this,UploadDocument.class));
        }
        else  if(id == R.id.logout){
            Intent i = new Intent(Unvrified.this,WebPage.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public class Alert {
        public void alert( String title, String body) {
            final AlertDialog.Builder Alert = new AlertDialog.Builder(Unvrified.this);
            Alert.setCancelable(false)
                    .setTitle(title)
                    .setMessage(body);
            Alert.setNegativeButton("Okay", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            Alert.create().show();
        }

        public AlertDialog pleaseWait() {
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(Unvrified.this);
            View mView = getLayoutInflater().inflate(R.layout.alert_progress_spinning, null);
            ProgressBar pb = mView.findViewById(R.id.progressBar);
            mBuilder.setView(mView);
            mBuilder.setCancelable(false);
            final AlertDialog dialog = mBuilder.create();
            dialog.show();
            return dialog;
        }

        public void ChangeIP(){
            final AlertDialog.Builder mBuilder = new AlertDialog.Builder(Unvrified.this);
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
                        Toast.makeText(Unvrified.this,"ip changed to : "+Main.getIp(),Toast.LENGTH_LONG).show();
                        dialog.cancel();
                    }
                    else
                    {
                        Toast.makeText(Unvrified.this,"Enter the ip first",Toast.LENGTH_LONG).show();
                    }
                }
            });
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
                        Intent i = new Intent(Unvrified.this, RequestStock.class);
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
            String str      ;
            try {
                str = POSTRequest.fetchUserData(strings[0], strings[1],Unvrified.this);
            } catch (Exception e) {
                return "network";
            }
            return str;
        }
    }


}
