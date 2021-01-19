package com.mobile.agri10x;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mobile.agri10x.Connection.POSTRequest;
import com.mobile.agri10x.Model.GetBidStatus;
import com.mobile.agri10x.Model.Main;
import com.mobile.agri10x.Model.Product;
import com.mobile.agri10x.Model.ProductAdapter;
import com.mobile.agri10x.Model.SwipeToDeleteCallback;
import com.mobile.agri10x.Model.User;
import com.mobile.agri10x.Model.UserId;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ActiveTrade extends AppCompatActivity {
    static User user_data_intent;
    //Recycler View
    public static List<Product> productList;
    public static Context context;
    //the recyclerview
    public static RecyclerView recyclerView;
    public static ProductAdapter adapter;
    UserId userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_trade);
        //Recycler View
        context = getApplicationContext();
        //getting the recyclerview from xml
        recyclerView = findViewById(R.id.recyclerView1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productList = new ArrayList<>();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userId=new UserId();
            userId.setUserid(extras.getString("Userid"));
        }
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                GetBidStatus getBidStatus=new GetBidStatus();
//                getBidStatus.setUserid(user_data_intent.get_id());
//                new ActiveTrade.loadStockData().execute(Main.getIp()+"/getFarmerBidStatus",new Gson().toJson(getBidStatus));
//                //new LoadEntitiyData().execute(Main.getIp()+"/UserInfo",new Gson().toJson(getBidStatus));
//            }
//        }, 500);
        GetBidStatus getBidStatus=new GetBidStatus();
        getBidStatus.setUserid(userId.getUserid());
        new ActiveTrade.loadStockData().execute(Main.getIp()+"/getFarmerBidStatus",new Gson().toJson(getBidStatus));
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
            dialog = new ActiveTrade.Alert().pleaseWait();
        }

        @Override
        protected void onPostExecute(String s) {        //session Expired = null , "network" (Due to exception) , {...} data
            super.onPostExecute(s);
            dialog.dismiss();
            //check1.setText(s);
            if(s!=null) {
                if(s.equals("network")){
                    new ActiveTrade.Alert().alert("Network !!!", getResources().getString(R.string.network_error_message));
                }else {
                    Product[] p = Product.extractFeatureFromJson(s);
                    if(p.length>0) {
                        productList.addAll(Arrays.asList(p));
                        adapter = new ProductAdapter(ActiveTrade.this, productList);
                        setUp(adapter);
                    }
                    if (p.length < 1) {
                        Toast.makeText(ActiveTrade.this, "No active trades", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String str;
            try {
                str = POSTRequest.fetchUserData(strings[0],strings[1],ActiveTrade.this);
            } catch (Exception e) {
                return "network";
            }
            return str;
        }
    }

    public static void setUp(ProductAdapter adapter) {

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new SwipeToDeleteCallback(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
        adapter.notifyDataSetChanged();

    }

    public class Alert {
        public void alert( String title, String body) {
            final AlertDialog.Builder Alert = new AlertDialog.Builder(ActiveTrade.this);
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
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(ActiveTrade.this);
            View mView = getLayoutInflater().inflate(R.layout.alert_progress_spinning, null);
            ProgressBar pb = mView.findViewById(R.id.progressBar);
            mBuilder.setView(mView);
            mBuilder.setCancelable(false);
            final AlertDialog dialog = mBuilder.create();
            dialog.show();
            return dialog;
        }

        public void ChangeIP(){
            final AlertDialog.Builder mBuilder = new AlertDialog.Builder(ActiveTrade.this);
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
                        Toast.makeText(ActiveTrade.this,"ip changed to : "+Main.getIp(),Toast.LENGTH_LONG).show();
                        dialog.cancel();
                    }
                    else
                    {
                        Toast.makeText(ActiveTrade.this,"Enter the ip first",Toast.LENGTH_LONG).show();
                    }

                }
            });
        }
    }

}
