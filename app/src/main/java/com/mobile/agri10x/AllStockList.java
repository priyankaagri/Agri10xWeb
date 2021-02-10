package com.mobile.agri10x;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.mobile.agri10x.Connection.GETRequest;
import com.mobile.agri10x.Connection.POSTRequest;
import com.mobile.agri10x.Model.Main;
import com.mobile.agri10x.Model.StockList;
import com.mobile.agri10x.Model.StockListAdapter;
import com.mobile.agri10x.Model.UserId;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AllStockList extends AppCompatActivity {

  //  Toolbar toolbar;

    //Recycler View
    public static List<StockList> productList;
    public static Context context;
    //the recyclerview
    public static RecyclerView recyclerView;
    public static StockListAdapter adapter;
ImageView but_back;
    static StockList[] p;
    private UserId userId;
    Button addmorestock;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_list2);

//        //toolbar
//        toolbar = findViewById(R.id.toolbar1);
//        setSupportActionBar(toolbar);
//        toolbar.setNavigationIcon(R.drawable.ic_action_name );
        but_back = findViewById(R.id.but_back);
        but_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userId=new UserId();
            userId.setUserid(extras.getString("Userid"));
        }
     //   Log.d("getuserid",extras.getString("Userid"));

        context = getApplicationContext();
        //getting the recyclerview from xml
        addmorestock = findViewById(R.id.addmorestock);
        recyclerView = findViewById(R.id.recyclerView_stocklist);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //initializing the productlist
        productList = new ArrayList<>();
        addmorestock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllStockList.this,AddStock.class);
                intent.putExtra("Userid",userId.getUserid());
                startActivity(intent);
                finish();
            }
        });
        new DownloadMyStock().execute(Main.getOldUrl()+"/getMyStockList",new Gson().toJson(userId));

    }

    class DownloadMyStock extends AsyncTask<String , Void, String > {
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
            p = StockList.extractFromJson(s);
            productList.addAll(Arrays.asList(p));
            if(productList.size()>0) {
                adapter = new StockListAdapter(AllStockList.this, productList);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            }
            else{
                Toast.makeText(AllStockList.this,"No data to Show !!!",Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String str;
            try {
                str = POSTRequest.fetchUserData(strings[0],strings[1],AllStockList.this);
            } catch (Exception e){
                return "network";
            }
            return str;
        }
    }

    public static void setUp(StockListAdapter adapter) {

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

    }


    public class Alert {
        public void alert( String title, String body) {
            final AlertDialog.Builder Alert = new AlertDialog.Builder(AllStockList.this);
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
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(AllStockList.this);
            View mView = getLayoutInflater().inflate(R.layout.alert_progress_spinning, null);
            ProgressBar pb = mView.findViewById(R.id.progressBar);
            mBuilder.setView(mView);
            mBuilder.setCancelable(false);
            final AlertDialog dialog = mBuilder.create();
            dialog.show();
            return dialog;
        }

        public void ChangeIP(){
            final AlertDialog.Builder mBuilder = new AlertDialog.Builder(AllStockList.this);
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
                        Toast.makeText(AllStockList.this,"ip changed to : "+Main.getIp(),Toast.LENGTH_LONG).show();
                        dialog.cancel();
                    }
                    else
                    {
                        Toast.makeText(AllStockList.this,"Enter the ip first",Toast.LENGTH_LONG).show();
                    }

                }
            });
        }
    }

    @Override
    public void onBackPressed() {

    }
}
