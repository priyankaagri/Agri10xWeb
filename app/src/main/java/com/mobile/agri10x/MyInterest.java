package com.mobile.agri10x;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
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

public class MyInterest extends AppCompatActivity {

    Toolbar toolbar;

    //Recycler View
    public List<Notification_Product> productList;
    public Context context;
    public RecyclerView recyclerView;
    public Notification_Product_Adapter adapter;
    static Notification_Product[] p;
    SessionManager session;
    Button Interestedbutton;
    int currentInterestedPosition;
    TextView no_data_message;
    private UserId userId;
    private String role="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_interest);
        session = new SessionManager(getApplicationContext());

        //toolbar
        toolbar = findViewById(R.id.my_interest_toolbar);
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
        no_data_message = findViewById(R.id.myinterest_no_data_message);
        //Recycler View
        context = getApplicationContext();
        recyclerView = findViewById(R.id.recyclerView_my_interest);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productList = new ArrayList<>();
        Interested intr = new Interested(null, SessionManager.getKEY_Entity_id(), role,false,userId.getUserid());
        intr.setMyInterest(true);
        new MyInterestedThings().execute(Main.getNegiUrl()+"/notificationData",new Gson().toJson(intr));

    }

    protected void onStart() {

        super.onStart();

    }

    //notificationData
    class MyInterestedThings extends AsyncTask<String , Void, String > {
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
                    p = Notification_Product.extractFromJson(s);
                    if(p.length>0) {
                        productList.addAll(Arrays.asList(p));
                        adapter = new Notification_Product_Adapter(MyInterest.this, productList, new ClickHandler() {
                            @Override
                            public void onInterestedClick(View v, int position) {
                                final Notification_Product notification_item = adapter.getItem(position);
                                Interestedbutton = v.findViewById(R.id.Interested_button);
                                currentInterestedPosition = position;
                                new Alert().Confirm(notification_item);
                            }

                        }, "MyInterest");
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    }
                    else{
                        no_data_message.setText("Currently you are Not Interested in any Request Or Stock");

                    }
                }
            }

        }


        @Override
        protected String doInBackground(String... strings) {
            String str;
            try {
                str = POSTRequest.fetchUserData(strings[0],strings[1],MyInterest.this);
            } catch (Exception e){
                return "network";
            }
            return str;
        }
    }

    class RemoveInterest extends  AsyncTask<String,Void,String>{
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
                Interestedbutton.setBackgroundResource(R.drawable.buttonshape);
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String str;
            try{
                str = POSTRequest.fetchUserData(strings[0],strings[1],MyInterest.this);
            }catch (Exception e){
                return "network";
            }
            return str;
        }
    }


    public class Alert {
        public void alert( String title, String body) {
            final AlertDialog.Builder Alert = new AlertDialog.Builder(MyInterest.this);
            Alert.setCancelable(false)
                    .setTitle(title)
                    .setMessage(body);
            Alert.setNegativeButton("Okey", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                    dialogInterface.cancel();
                }
            });
            Alert.create().show();
        }

        public void Confirm(final Notification_Product product) {
            final AlertDialog.Builder Alert = new AlertDialog.Builder(MyInterest.this);
            Alert.setCancelable(true)
                    .setTitle("Alert !!")
                    .setMessage("Do you Really Want To Remove this from your MyInterest");
            Alert.setNegativeButton("GO",  new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //call delete request
                    Interested intr= new Interested(product.get_id(), SessionManager.getKEY_Entity_id(), role,false,userId.getUserid());
                    new RemoveInterest().execute(Main.getNegiUrl()+"/interest",new Gson().toJson(intr));
                    dialogInterface.cancel();
                }
            });
            Alert.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            Alert.create().show();
        }

        public AlertDialog pleaseWait() {
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(MyInterest.this);
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
