package com.mobile.agri10x;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.mobile.agri10x.Connection.POSTRequest;
import com.mobile.agri10x.Interface.ClickHandler;
import com.mobile.agri10x.Model.Interested;
import com.mobile.agri10x.Model.Main;
import com.mobile.agri10x.Model.Notification_Product;
import com.mobile.agri10x.Model.Notification_Product_Adapter;
import com.mobile.agri10x.Model.UserId;
import com.mobile.agri10x.SessionManagment.SessionManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Notification extends AppCompatActivity {

    Toolbar toolbar;

    //Recycler View
    public List<Notification_Product> productList;
    public  Context context;
    public  RecyclerView recyclerView;
    public  Notification_Product_Adapter adapter;
    static Notification_Product[] p;
    SessionManager session;
    Button Interestedbutton;
    int currentInterestedPosition;
    TextView no_data_to_show;
    private UserId userId;
    private String role="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        session = new SessionManager(getApplicationContext());

        no_data_to_show = findViewById(R.id.notification_no_data_message);
        //toolbar
        toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_name );
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userId=new UserId();
            userId.setUserid(extras.getString("Userid"));
            role=extras.getString("role");
        }

        //Recycler View
        context = getApplicationContext();
        recyclerView = findViewById(R.id.recyclerView_notification);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productList = new ArrayList<>();
    }

    protected void onStart(){
        super.onStart();
        Interested intr = new Interested(null, SessionManager.getKEY_Entity_id(), SessionManager.getRole(),false,userId.getUserid());
        intr.setMyInterest(false);
        new ReloadNewNotificationList().execute(Main.getNegiUrl()+"/notificationData",new Gson().toJson(intr));
    }

    //notificationData
    class ReloadNewNotificationList extends AsyncTask<String , Void, String >{
        AlertDialog alert;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            alert = new Alert().pleaseWait();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            alert.dismiss();
            if(s!=null) {
                if (s.equals("network")) {
                    new Alert().alert("Network !!!", getResources().getString(R.string.network_error_message));
                } else {
                    p = Notification_Product.extractFromJson(s);
                    if(p.length>0) {
                        productList.clear();
                        productList.addAll(Arrays.asList(p));
                        adapter = new Notification_Product_Adapter(Notification.this, productList, new ClickHandler() {
                            @Override
                            public void onInterestedClick(View v, int position) {
                                final Notification_Product notification_item = adapter.getItem(position);
                                Interestedbutton = v.findViewById(R.id.Interested_button);
                                currentInterestedPosition = position;
                                Interested intr = new Interested(notification_item.get_id(), SessionManager.getKEY_Entity_id(), role, true,userId.getUserid());
                                new InterestedCard().execute(Main.getNegiUrl() + "/interest", new Gson().toJson(intr));
                            }

                        }, null);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    }
                    else{
                        no_data_to_show.setText("No Data Notifications");
//                        Toast.makeText(Notification.this,"No Current Bid",Toast.LENGTH_LONG).show();
                    }
                }
            }
        }


        @Override
        protected String doInBackground(String... strings) {
            String str;
            try {
                str = POSTRequest.fetchUserData(strings[0],strings[1],Notification.this);
            } catch (Exception e){
                return "network";
            }
            return str;
        }
    }

    class InterestedCard extends  AsyncTask<String,Void,String>{
        AlertDialog alert;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            alert = new Alert().pleaseWait();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            alert.dismiss();
            if(s!=null){
                if(s.equals("network")){
                    new Alert().alert("Network !!!", getResources().getString(R.string.network_error_message));
                }
                else{
                    productList.remove(currentInterestedPosition);
                    adapter.notifyItemRemoved(currentInterestedPosition);
                    adapter.notifyItemRangeChanged(currentInterestedPosition,productList.size());
                }
            }
            else {
                Interestedbutton.setText("Show Interest");
                Interestedbutton.setBackgroundResource(R.drawable.btn_round);
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String str;
            try{
                str = POSTRequest.fetchUserData(strings[0],strings[1],Notification.this);
            }catch (Exception e){
                return "network";
            }
            return str;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.notification_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.my_interest) {
            Intent i = new Intent(Notification.this,MyInterest.class);
            i.putExtra("Userid",userId.getUserid());
            i.putExtra("role",role);
            startActivity(i);

        }
        return super.onOptionsItemSelected(item);
    }

     class Alert {
        public void alert( String title, String body) {
            final AlertDialog.Builder Alert = new AlertDialog.Builder(Notification.this);
            Alert.setCancelable(false)
                    .setTitle(title)
                    .setMessage(body);
            Alert.setNegativeButton("Okay", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                    dialogInterface.cancel();
                }
            });
            Alert.create().show();
        }

        public AlertDialog pleaseWait() {
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(Notification.this);
            View mView = getLayoutInflater().inflate(R.layout.alert_progress_spinning, null);
            ProgressBar pb = mView.findViewById(R.id.progressBar);
            mBuilder.setView(mView);
            mBuilder.setCancelable(false);
            final AlertDialog dialog = mBuilder.create();
            dialog.show();
            return dialog;
        }

    }
}
