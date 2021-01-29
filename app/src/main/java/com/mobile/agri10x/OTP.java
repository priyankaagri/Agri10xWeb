package com.mobile.agri10x;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.ArrayMap;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.agri10x.models.GetOTP;
import com.mobile.agri10x.models.resendOTP;
import com.mobile.agri10x.models.verfyOTP;
import com.mobile.agri10x.retrofit.AgriInvestor;
import com.mobile.agri10x.retrofit.ApiHandler;
import com.mukesh.OnOtpCompletionListener;

import org.json.JSONObject;

import java.util.Map;

import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OTP extends AppCompatActivity  {
TextView timer,mobilenumber;
    CountDownTimer cTimer = null;
    private OtpTextView otpTextView;
    String strmobilenumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

timer = findViewById(R.id.timer);
        mobilenumber = findViewById(R.id.mobilenumber);
        otpTextView = findViewById(R.id.otp_view);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            strmobilenumber = extras.getString("mobilenumber");
        }

        otpTextView.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {
                // fired when user types something in the Otpbox
            }
            @Override
            public void onOTPComplete(String otp) {
                // fired when user has entered the OTP fully.
               callverifyapi(otp);
            }
        });

timer.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        callresendotp();
    }
});
startTimer();
    }

    private void callresendotp() {
        Map<String, Object> jsonParams = new ArrayMap<>();
//put something inside the map, could be null
        jsonParams.put("mobileNo", strmobilenumber);

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),(new JSONObject(jsonParams)).toString());

        AgriInvestor apiService = ApiHandler.getApiService();
        final Call<resendOTP> loginCall = apiService.wsResendOTP(
                "123456",body);
        loginCall.enqueue(new Callback<resendOTP>() {
            @SuppressLint("WrongConstant")
            @Override
            public void onResponse(Call<resendOTP> call,
                                   Response<resendOTP> response) {
                Log.d("GetTradeVariety",response.toString());
                if (response.isSuccessful()) {


                    timer.setClickable(false);
                    timer.setFocusable(false);
                    startTimer();


                }
                else {
                    Toast.makeText(OTP.this,"Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<resendOTP> call,
                                  Throwable t) {
                Toast.makeText(OTP.this,"Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void callverifyapi(String otp) {
        Map<String, Object> jsonParams = new ArrayMap<>();
//put something inside the map, could be null
        jsonParams.put("mobileNo", strmobilenumber);
        jsonParams.put("OTP",otp);

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),(new JSONObject(jsonParams)).toString());

        AgriInvestor apiService = ApiHandler.getApiService();
        final Call<verfyOTP> loginCall = apiService.wsverifyOTP(
                "123456",body);
        loginCall.enqueue(new Callback<verfyOTP>() {
            @SuppressLint("WrongConstant")
            @Override
            public void onResponse(Call<verfyOTP> call,
                                   Response<verfyOTP> response) {
                Log.d("verfyOTP",response.toString());
                if (response.isSuccessful()) {





                }
                else {
                    Toast.makeText(OTP.this,"Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<verfyOTP> call,
                                  Throwable t) {
                Toast.makeText(OTP.this,"Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }


    //start timer function
    void startTimer() {
        cTimer = new CountDownTimer(30000, 1000)
        {
            public void onTick(long millisUntilFinished) {
                timer.setText( "00:"+String.valueOf(millisUntilFinished / 1000) +"  seconds remaining..." );
            }
            public void onFinish() {
                timer.setClickable(true);
                timer.setFocusable(true);
                timer.setText("Didn`t receive the code ? Resend now");
            }
        };
        cTimer.start();
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelTimer();
    }

    //cancel timer
    void cancelTimer() {
        if(cTimer!=null)
            cTimer.cancel();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}


//    private void callapi(String mobilenumber) {
//        Map<String, Object> jsonParams = new ArrayMap<>();
////put something inside the map, could be null
//        jsonParams.put("mobileNo", mobilenumber);
//
//        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),(new JSONObject(jsonParams)).toString());
//
//        AgriInvestor apiService = ApiHandler.getApiService();
//        final Call<GetOTP> loginCall = apiService.wsgetOTP(
//                "123456",body);
//        loginCall.enqueue(new Callback<GetOTP>() {
//            @SuppressLint("WrongConstant")
//            @Override
//            public void onResponse(Call<GetOTP> call,
//                                   Response<GetOTP> response) {
//                Log.d("GetTradeVariety",response.toString());
//                if (response.isSuccessful()) {
//
//
//                    Intent i = new Intent(LoginActivity.this,OTP.class);
//                    i.putExtra("mobilenumber",mobilenumber);
//                    startActivity(i);
//
//
//                }
//                else {
//                    new LoginActivity.Alert().SignUp("UnRegistered User!!","First Register Yourself For Our Service");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<GetOTP> call,
//                                  Throwable t) {
//                Toast.makeText(LoginActivity.this,"Something went wrong", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }