package com.mobile.agri10x;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.mobile.agri10x.Connection.POSTRequest;
import com.mobile.agri10x.Model.ErrorLog;
import com.mobile.agri10x.Model.Main;
import com.mobile.agri10x.Model.SecurityData;
import com.mobile.agri10x.Model.UserId;
import com.mobile.agri10x.models.GetAddMoney;
import com.mobile.agri10x.models.GetCheckOutHandle;
import com.mobile.agri10x.retrofit.AgriInvestor;
import com.mobile.agri10x.retrofit.ApiHandler;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentBalance extends AppCompatActivity implements PaymentResultWithDataListener {
    TextView withdrawableBalance,ClosingBalance;
    Toolbar toolbar;
    String Userid;
    ImageView ic_refresh;
    UserId userIdo;

    ImageView img_arrow,arrow_back;
    Button addmoney ;
    TextInputEditText add_money_towallet;
    String userid,orderidfromres,amountfromres,order_id, payment_id, signature;
    AlertDialog dialog;
    TextView benificary;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_balance);



        ic_refresh= findViewById(R.id.ic_refresh);
        arrow_back = findViewById(R.id.arrow_back);
        withdrawableBalance = findViewById(R.id.wallet_balance_withdrawable);
        img_arrow = findViewById(R.id.img_arrow);
        addmoney = findViewById(R.id.addmoney);
        add_money_towallet = findViewById(R.id.add_money_towallet);
        benificary= findViewById(R.id.benificary);



        Bundle extras = getIntent().getExtras();
        Userid = extras.getString("Userid");
        if (extras != null) {

            userid = extras.getString("Userid");
            Log.d("getuserid",userid);
            benificary.setText("AGRI10"+userid);
        }

        userIdo = new UserId();
        userIdo.setUserid(Userid);



        arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        new PaymentBalance.LoadBalanceOnRefreshClick().execute(Main.getIp()+"/getBalance",new Gson().toJson(userIdo));

        ic_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PaymentBalance.LoadBalanceOnRefreshClick().execute(Main.getIp()+"/getBalance",new Gson().toJson(userIdo));
            }
        });
        addmoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getamt = add_money_towallet.getText().toString();
                Log.d("getamt",getamt);
                if(getamt == null || getamt.isEmpty() || getamt.equals("") ){

                    Toast.makeText(PaymentBalance.this,"Please Enter Amount",Toast.LENGTH_SHORT).show();
                }else{
                    if( userid != null || !userid.isEmpty()) {
                        dialog = new PaymentBalance.Alert().pleaseWait();
                        callapi(userid,getamt);
                        add_money_towallet.setText("");

                    }
                }
            }
        });
    }
    private void callapi(String userid, String getamt) {

        Map<String, Object> jsonParams = new ArrayMap<>();
//put something inside the map, could be null
        jsonParams.put("Userid",userid);
        jsonParams.put("Mon",getamt);

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),(new JSONObject(jsonParams)).toString());

        AgriInvestor apiService = ApiHandler.getApiService();
        final Call<GetAddMoney> loginCall = apiService.wsGetAddMoney(
                "123456",body);
        loginCall.enqueue(new Callback<GetAddMoney>() {
            @SuppressLint("WrongConstant")
            @Override
            public void onResponse(Call<GetAddMoney> call,
                                   Response<GetAddMoney> response) {
                Log.d("checkphone",response.toString());

                if (response.isSuccessful()) {
                    dialog.dismiss();
                    Log.d("responsecheck", String.valueOf(response.body()));
                    String checkresponse =String.valueOf(response.body());
                    Log.d("checkingexist",checkresponse);

                    orderidfromres = response.body().getOrderid();
                    amountfromres = String.valueOf(response.body().getAmount());

                    startpayment(orderidfromres,amountfromres);

                }
                else {
                    dialog.dismiss();
                    //    new LoginActivity.Alert().SignUp("UnRegistered User!!","First Register Yourself For Our Service");
                }
            }

            @Override
            public void onFailure(Call<GetAddMoney> call,
                                  Throwable t) {
                Toast.makeText(PaymentBalance.this,"Something went wrong", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    private void startpayment(String orderidfromres, String amountfromres) {
        /**
         * You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;

        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Agri10x");
            options.put("description", "(ICognitive Global Pvt Ltd)");
//You can omit the image option to fetch the image from dashboard
            options.put("image", "https://data.agri10x.com/images/Icognitive%20logo2.png");
            options.put("currency", "INR");
            options.put("theme.color", "#5FA30F");
            options.put("order_id", orderidfromres);
            String payment =amountfromres;        //orderamount.getText().toString();
// amount is in paise so please multiple it by 100
//Payment failed Invalid amount (should be passed in integer paise. Minimum value is 100 paise, i.e. ₹ 1)
            double total = Double.parseDouble(payment);
//            total = total * 100;
            options.put("amount", total);

//            JSONObject preFill = new JSONObject();
//            preFill.put("email", "kamal.bunkar07@gmail.com");
//            preFill.put("contact", "9144040888");

//            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    class LoadBalanceOnRefreshClick extends AsyncTask<String, Integer, String> {

        AlertDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new PaymentBalance.Alert().pleaseWait();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            if(s!=null) {
                if (s.equals("network")) {
                    new PaymentBalance.Alert().alert("Network !!!", getResources().getString(R.string.network_error_message));
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        SecurityData.setClosingBalance(jsonObject.getLong("Closing"));
                        SecurityData.setWithdthrawBalance(jsonObject.getLong("Widthraw"));
                        SecurityData.setPendingBalance(jsonObject.getString("Pending"));
                        if(SecurityData.getWithdthrawBalance()!=null && SecurityData.getClosingBalance()!=null) {
                            withdrawableBalance.setText("Credit : " + Main.getIndian_currency() + " " + SecurityData.getWithdthrawBalance());

                        }else{
                            ErrorLog errorLog = new ErrorLog(Main.getIp()+"/getBalance","Widthraw || Closing","String",null,"Farmer.LoadBalanceOnRefreshClick->onPostExecute()",getResources().getString(R.string.DeviceName),getClass().getSimpleName());
                            Main.addErrorReportRequest(errorLog,PaymentBalance.this);
                        }
                    } catch (JSONException e) {
                        SecurityData.setWithdthrawBalance((long) 0.0);
                        SecurityData.setClosingBalance((long) 0.0);
                        withdrawableBalance.setText("Credit : " +Main.getIndian_currency() + " " + SecurityData.getWithdthrawBalance());

                    }
                }
            }
            else{
                SecurityData.setWithdthrawBalance((long) 0.0);
                SecurityData.setClosingBalance((long) 0.0);
                withdrawableBalance.setText("Credit : " +Main.getIndian_currency() + " " + SecurityData.getWithdthrawBalance());
//                ClosingBalance.setText("Closing: "+Main.getIndian_currency()+" "+SecurityData.getClosingBalance());
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String str;
            try {
                str = POSTRequest.fetchUserData(strings[0],strings[1], PaymentBalance.this);
            } catch (Exception e) {
                return "network";
            }
            return str;
        }
    }

    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        order_id = paymentData.getOrderId();
        payment_id = paymentData.getPaymentId();
        signature = paymentData.getSignature();

        callcheckouthandle(order_id,payment_id,signature);

        Log.d("mainresponse",order_id+ " "+ payment_id+ " "+signature);
    }


    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {

    }

    private void callcheckouthandle(String order_id, String payment_id, String signature) {
        Map<String, Object> jsonParams = new ArrayMap<>();
//put something inside the map, could be null
        jsonParams.put("razorpay_payment_id", payment_id);
        jsonParams.put("razorpay_order_id",order_id);
        jsonParams.put("razorpay_signature",signature);
        jsonParams.put("Userid",userid);

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),(new JSONObject(jsonParams)).toString());

        AgriInvestor apiService = ApiHandler.getApiService();
        final Call<GetCheckOutHandle> loginCall = apiService.wsGetCheckoutHandle(
                "123456",body);
        loginCall.enqueue(new Callback<GetCheckOutHandle>() {
            @SuppressLint("WrongConstant")
            @Override
            public void onResponse(Call<GetCheckOutHandle> call,
                                   Response<GetCheckOutHandle> response) {
                Log.d("checkphone",response.toString());

                if (response.isSuccessful()) {
                    dialog.dismiss();
                    Log.d("responsecheck", String.valueOf(response.body()));
                    String checkresponse =String.valueOf(response.body());
                    Log.d("checkingexist",checkresponse);

                    Toast.makeText(PaymentBalance.this,response.body().getStatus(),Toast.LENGTH_SHORT).show();

                }
                else {
                    dialog.dismiss();
                    //    new LoginActivity.Alert().SignUp("UnRegistered User!!","First Register Yourself For Our Service");
                }
            }

            @Override
            public void onFailure(Call<GetCheckOutHandle> call,
                                  Throwable t) {
                dialog.dismiss();
                Toast.makeText(PaymentBalance.this,"Payment Fail",Toast.LENGTH_SHORT).show();

            }
        });

    }
    public class Alert {
        public void alert( String title, String body) {
            final AlertDialog.Builder Alert = new AlertDialog.Builder(PaymentBalance.this);
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
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(PaymentBalance.this);
            View mView = getLayoutInflater().inflate(R.layout.alert_progress_spinning, null);
            ProgressBar pb = mView.findViewById(R.id.progressBar);
            mBuilder.setView(mView);
            mBuilder.setCancelable(false);
            final AlertDialog dialog = mBuilder.create();
            dialog.show();
            return dialog;
        }
    }

    @Override
    public void onBackPressed() {

    }
}