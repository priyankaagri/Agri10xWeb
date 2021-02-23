package com.mobile.agri10x;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.mobile.agri10x.Model.LoginUser;
import com.mobile.agri10x.Model.Main;
import com.mobile.agri10x.models.CheckPhoneExits;
import com.mobile.agri10x.models.GetAddMoney;
import com.mobile.agri10x.retrofit.AgriInvestor;
import com.mobile.agri10x.retrofit.ApiHandler;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentGateway extends AppCompatActivity implements PaymentResultListener {
ImageView img_arrow;
Button addmoney ;
TextInputEditText add_money_towallet;
String userid,orderidfromres,amountfromres;
    AlertDialog dialog;
TextView benificary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_gateway);

        img_arrow = findViewById(R.id.img_arrow);
        addmoney = findViewById(R.id.addmoney);
        add_money_towallet = findViewById(R.id.add_money_towallet);
        benificary= findViewById(R.id.benificary);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            userid = extras.getString("Userid");
            benificary.setText("AGRI10"+userid);
        }
        Log.d("Loguserid",userid);
        img_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        addmoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           String getamt = add_money_towallet.getText().toString();
           Log.d("getamt",getamt);
                if(getamt == null || getamt.isEmpty() || getamt.equals("") ){

                    Toast.makeText(PaymentGateway.this,"Please Enter Amount",Toast.LENGTH_SHORT).show();
                }else{
                    if( userid != null || !userid.isEmpty()) {
                        dialog = new PaymentGateway.Alert().pleaseWait();
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
        jsonParams.put("Userid", userid);
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
                Toast.makeText(PaymentGateway.this,"Something went wrong", Toast.LENGTH_SHORT).show();
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
//
//            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    @Override
    public void onPaymentSuccess(String s) {
// payment successfull pay_DGU19rDsInjcF2
        Log.e("statuspayment", " payment successfull " + s.toString());
        Toast.makeText(this, "Payment successfully done! " + s, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onPaymentError(int i, String s) {
        Log.e("statuspayment", "error code "+String.valueOf(i)+" -- Payment failed "+s.toString() );
        try {
            Toast.makeText(this, "Payment error please try again", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("OnPaymentError", "Exception in onPaymentError", e);
        }
    }

    public class Alert {
        public void alert(String title, String body) {
            final AlertDialog.Builder Alert = new AlertDialog.Builder(PaymentGateway.this);
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
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(PaymentGateway.this);
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