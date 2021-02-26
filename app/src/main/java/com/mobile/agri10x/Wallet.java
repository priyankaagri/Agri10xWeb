package com.mobile.agri10x;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.mobile.agri10x.Connection.GETRequest;
import com.mobile.agri10x.Connection.POSTRequest;
import com.mobile.agri10x.Model.ErrorLog;
import com.mobile.agri10x.Model.Main;
import com.mobile.agri10x.Model.SecurityData;
import com.mobile.agri10x.Model.UserId;

import org.json.JSONException;
import org.json.JSONObject;

public class Wallet extends AppCompatActivity {

    TextView withdrawableBalance,ClosingBalance;
    Toolbar toolbar;
    String Userid;
    ImageView but_back;
UserId userIdo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

//        //toolbar
//        toolbar = findViewById(R.id.toolbar1);
//        setSupportActionBar(toolbar);
//        toolbar.setNavigationIcon(R.drawable.ic_action_name );
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
        Bundle extras = getIntent().getExtras();
        Userid = extras.getString("Userid");
        Log.d("getuserid",Userid);
        userIdo = new UserId();
        userIdo.setUserid(Userid);
        but_back = findViewById(R.id.but_back);
        withdrawableBalance = findViewById(R.id.wallet_balance_withdrawable);
        ClosingBalance = findViewById(R.id.wallet_balance_closing);

        but_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        new LoadBalanceOnRefreshClick().execute(Main.getIp()+"/getBalance",new Gson().toJson(userIdo));
//        if(SecurityData.getWithdthrawBalance()==null) {
//            new LoadWalletBalance().execute(Main.getIp() + "/getBalance",new Gson().toJson(Userid));
//        }
//        else{
//            withdrawableBalance.setText("Withdrawable Balance: "+Main.getIndian_currency()+" "+SecurityData.getWithdthrawBalance());
//            ClosingBalance.setText("Closing Balance: "+Main.getIndian_currency()+" "+SecurityData.getWithdthrawBalance());
//        }
    }
    class LoadBalanceOnRefreshClick extends AsyncTask<String, Integer, String> {

        AlertDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new Wallet.Alert().pleaseWait();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            if(s!=null) {
                if (s.equals("network")) {
                    new Wallet.Alert().alert("Network !!!", getResources().getString(R.string.network_error_message));
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        SecurityData.setClosingBalance(jsonObject.getLong("Closing"));
                        SecurityData.setWithdthrawBalance(jsonObject.getLong("Widthraw"));
                        SecurityData.setPendingBalance(jsonObject.getString("Pending"));
                        if(SecurityData.getWithdthrawBalance()!=null && SecurityData.getClosingBalance()!=null) {
                            withdrawableBalance.setText("Withdrawable: " + Main.getIndian_currency() + " " + SecurityData.getWithdthrawBalance());
                            ClosingBalance.setText("Closing: " + Main.getIndian_currency() + " " + SecurityData.getClosingBalance());
                        }else{
                            ErrorLog errorLog = new ErrorLog(Main.getIp()+"/getBalance","Widthraw || Closing","String",null,"Farmer.LoadBalanceOnRefreshClick->onPostExecute()",getResources().getString(R.string.DeviceName),getClass().getSimpleName());
                            Main.addErrorReportRequest(errorLog,Wallet.this);
                        }
                    } catch (JSONException e) {
                        SecurityData.setWithdthrawBalance((long) 0.0);
                        SecurityData.setClosingBalance((long) 0.0);
                        withdrawableBalance.setText("Withdrawable: " +Main.getIndian_currency() + " " + SecurityData.getWithdthrawBalance());
                        ClosingBalance.setText("Closing: "+Main.getIndian_currency()+" "+SecurityData.getClosingBalance());
                    }
                }
            }
            else{
                SecurityData.setWithdthrawBalance((long) 0.0);
                SecurityData.setClosingBalance((long) 0.0);
                withdrawableBalance.setText("Withdrawable: " +Main.getIndian_currency() + " " + SecurityData.getWithdthrawBalance());
                ClosingBalance.setText("Closing: "+Main.getIndian_currency()+" "+SecurityData.getClosingBalance());
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String str;
            try {
                str = POSTRequest.fetchUserData(strings[0],strings[1], Wallet.this);
            } catch (Exception e) {
                return "network";
            }
            return str;
        }
    }
    //okey
//    class LoadWalletBalance extends AsyncTask<String, Integer, String> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            if(s!=null) {
//                if (s.equals("network")) {
//                    new Alert().alert("Network !!!", getResources().getString(R.string.network_error_message));
//                } else if (s != null) {
//                    try {
//                        JSONObject jsonObject = new JSONObject(s);
//                        SecurityData.setClosingBalance(jsonObject.getString("Closing"));
//                        SecurityData.setWithdthrawBalance(jsonObject.getString("Widthraw"));
//                        SecurityData.setPendingBalance(jsonObject.getString("Pending"));
//                        withdrawableBalance.setText("Withdrawable Balance: "+Main.getIndian_currency()+" "+SecurityData.getWithdthrawBalance());
//                        ClosingBalance.setText("Closing Balance: "+Main.getIndian_currency()+" "+SecurityData.getWithdthrawBalance());
//                    }catch (Exception e){
//                        SecurityData.setWithdthrawBalance("0");
//                        SecurityData.setClosingBalance("0");
//                        withdrawableBalance.setText("Withdrawable Balance: "+Main.getIndian_currency()+" "+SecurityData.getWithdthrawBalance());
//                        ClosingBalance.setText("Closing Balance: "+Main.getIndian_currency()+" "+SecurityData.getWithdthrawBalance());
//                    }
//                }
//                else{
//                    SecurityData.setWithdthrawBalance("0");
//                    SecurityData.setClosingBalance("0");
//                    withdrawableBalance.setText("Withdrawable Balance: "+Main.getIndian_currency()+" "+SecurityData.getWithdthrawBalance());
//                    ClosingBalance.setText("Closing Balance: "+Main.getIndian_currency()+" "+SecurityData.getWithdthrawBalance());
//                }
//            }
//        }
//
//        @Override
//        protected String doInBackground(String... strings) {
//            String str;
//            try {
//                str = GETRequest.fetchUserData(strings[0], Wallet.this);
//            } catch (Exception e) {
//                return "network";
//            }
//            return str;
//        }
//    }


    public class Alert {
        public void alert( String title, String body) {
            final AlertDialog.Builder Alert = new AlertDialog.Builder(Wallet.this);
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
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(Wallet.this);
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

