package com.mobile.agri10x;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.ArrayMap;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.mobile.agri10x.Connection.POSTRequest;
import com.mobile.agri10x.Model.LoginUser;
import com.mobile.agri10x.Model.Main;
import com.mobile.agri10x.Model.User;
import com.mobile.agri10x.models.CheckPhoneExits;
import com.mobile.agri10x.models.GetLogin;
import com.mobile.agri10x.models.GetOTP;
import com.mobile.agri10x.models.resendOTP;
import com.mobile.agri10x.models.verfyOTP;
import com.mobile.agri10x.retrofit.AgriInvestor;
import com.mobile.agri10x.retrofit.ApiHandler;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;
import com.stfalcon.smsverifycatcher.OnSmsCatchListener;
import com.stfalcon.smsverifycatcher.SmsVerifyCatcher;


import org.json.JSONObject;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OTP extends AppCompatActivity  {
    AlertDialog dialog,dialog2;
    public Gson gson = new Gson();
    TextView timer,mobilenumber,verifynumber,verifyotp;
    CountDownTimer cTimer = null;
OtpTextView otp_view;
    String strmobilenumber,strotp,strotpfrmmsg;
    ImageView img_arrow;
    EditText enterotp;
    private static String responce = null;
    SmsBroadcastReceiver smsBroadcastReceiver;
    private static final int REQ_USER_CONSENT = 200;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_activity_o_t_p);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        otp_view = findViewById(R.id.otp_view);
        verifyotp = findViewById(R.id.verifyotp);
        enterotp = findViewById(R.id.enterotp);
        verifynumber = findViewById(R.id.verifynumber);
        img_arrow = findViewById(R.id.img_arrow);
        timer = findViewById(R.id.timer);
        mobilenumber = findViewById(R.id.mobilenumber);



        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            strmobilenumber = extras.getString("mobilenumber");
            verifynumber.setText("We Send Verification code to "+strmobilenumber);
        }else{
            verifynumber.setText("");
        }
//        otpTextView.setOtpCompletionListener(new OnOtpCompletionListener() {
//            @Override
//            public void onOtpCompleted(String s) {
//                Log.d("checkotp",s);
//                callverifyapi(s);
//            }
//        });


        otp_view.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {
                // fired when user types something in the Otpbox
            }
            @Override
            public void onOTPComplete(String otp) {
                strotp = otp;
                Log.d("otp",otp);
//                if(validateMobileNo()){
//                    InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
//                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
//                    callverifyapi("91"+strmobilenumber,otp);
//
//
//                }
            }
        });

        verifyotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("otp",strotp);
                if(validateOTP()){

                    InputMethodManager imm = (InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);

                    if (imm.isAcceptingText()) {
                                            InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    } else {
                       // writeToLog("Software Keyboard was not shown");
                    }
                    dialog = new OTP.Alert().pleaseWait();
                   callverifyapi("91"+strmobilenumber,strotp);

Log.d("params","91"+strmobilenumber+" "+strotp);
                }
            }
        });

        timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);

                if (imm.isAcceptingText()) {
                    InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } else {
                    // writeToLog("Software Keyboard was not shown");
                }

                dialog2 = new OTP.Alert().pleaseWait();
                callresendotp();
             //   verifyotp.setVisibility(View.VISIBLE);


            }
        });
        img_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OTP.this,LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });
        startTimer();
        startSmsUserConsent();
    }

    private void startSmsUserConsent() {
        SmsRetrieverClient client = SmsRetriever.getClient(this);
//We can add sender phone number or leave it blank
// I'm adding null here
        client.startSmsUserConsent(null).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private String parseCode(String message) {
        Pattern p = Pattern.compile("\\b\\d{6}\\b");
        Matcher m = p.matcher(message);
        String code = "";
        while (m.find()) {
            code = m.group(0);
        }
        return code;
    }

    private boolean validateOTP() {
        if (strotp.isEmpty()) {
            Toast.makeText(OTP.this,
                    "Invalid OTP", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void callresendotp() {
        Map<String, Object> jsonParams = new ArrayMap<>();
//put something inside the map, could be null
        jsonParams.put("mobileNo", "91"+strmobilenumber);

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),(new JSONObject(jsonParams)).toString());

        AgriInvestor apiService = ApiHandler.getApiService();
        final Call<resendOTP> loginCall = apiService.wsResendOTP(
                "123456",body);
        loginCall.enqueue(new Callback<resendOTP>() {
            @SuppressLint("WrongConstant")
            @Override
            public void onResponse(Call<resendOTP> call,
                                   Response<resendOTP> response) {
                dialog2.dismiss();
                Log.d("resendotpres",response.toString());
                if (response.isSuccessful()) {
                    Intent intent = new Intent(OTP.this,OTP.class);
                    intent.putExtra("mobilenumber",strmobilenumber);
                    startActivity(intent);
                    finish();

//                    timer.setClickable(false);
//                    timer.setFocusable(false);
//                    startTimer();

                    Toast.makeText(OTP.this,"OTP Resend", Toast.LENGTH_SHORT).show();
                }
                else {

                    Toast.makeText(OTP.this,"Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<resendOTP> call,
                                  Throwable t) {
                dialog2.dismiss();
                Toast.makeText(OTP.this,"Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void callverifyapi(String mobilenumber, String otp) {
        Map<String, Object> jsonParams = new ArrayMap<>();
//put something inside the map, could be null


        jsonParams.put("mobileNo", mobilenumber);
        jsonParams.put("otp",otp);
        Log.d("params",mobilenumber+" "+otp);

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),(new JSONObject(jsonParams)).toString());

        AgriInvestor apiService = ApiHandler.getApiService();
        final Call<verfyOTP> loginCall = apiService.wsverifyOTP(
                "123456",body);
        loginCall.enqueue(new Callback<verfyOTP>() {
            @SuppressLint("WrongConstant")
            @Override
            public void onResponse(Call<verfyOTP> call,
                                   Response<verfyOTP> response) {

                Log.d("verifyOTP",response.toString());
                if (response.isSuccessful()) {
                    Log.d("getresponse",response.body().getType());
 if(response.body().getType().equals("success")){
     checkphoneapi("91"+strmobilenumber);


     Log.d("checkphone",strmobilenumber);
 }else if(response.body().getType().equals("error")){
     dialog.dismiss();
     Toast.makeText(OTP.this,"You Entered wrong OTP.", Toast.LENGTH_SHORT).show();
 }




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

    private void checkphoneapi(String strmobilenumber) {
        Map<String, Object> jsonParams = new ArrayMap<>();
//put something inside the map, could be null
        jsonParams.put("tp", strmobilenumber);

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),(new JSONObject(jsonParams)).toString());

        AgriInvestor apiService = ApiHandler.getApiService();
        final Call<CheckPhoneExits> loginCall = apiService.wsCheckPhoneExits(
                "123456",body);
        loginCall.enqueue(new Callback<CheckPhoneExits>() {
            @SuppressLint("WrongConstant")
            @Override
            public void onResponse(Call<CheckPhoneExits> call,
                                   Response<CheckPhoneExits> response) {
                Log.d("checkphone",response.toString());

                if (response.isSuccessful()) {

Log.d("responsecheck", String.valueOf(response.body().getExist()));
String checkresponse =String.valueOf(response.body().getExist());
Log.d("checkingexist",checkresponse);
                   if(checkresponse.equals("true")){

                       SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
                       String pu = pref.getString("username",strmobilenumber);
                       String pp = pref.getString("password","defaultValue");
                       LoginUser user = new LoginUser();
                       user.setUser(pu);
                      // user.setPwd(pp);
                       new OTP.networkPOST().execute(Main.getIp()+"/login", gson.toJson(user));
                   }
                   if(checkresponse.equals("false"))
                    {
                       Intent intent= new Intent (OTP.this,UpdateProfile_New.class);
                       intent.putExtra("mobilenumber",strmobilenumber);
                       startActivity(intent);
                   }



                }
                else {
                    dialog.dismiss();
                    //    new LoginActivity.Alert().SignUp("UnRegistered User!!","First Register Yourself For Our Service");
                }
            }

            @Override
            public void onFailure(Call<CheckPhoneExits> call,
                                  Throwable t) {
                Toast.makeText(OTP.this,"Something went wrong", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
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
                verifyotp.setVisibility(View.GONE);
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

    class networkPOST extends AsyncTask<String, Integer, String> {
//        AlertDialog dialog;

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            dialog = new OTP.Alert().pleaseWait();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            responce = s;
            Log.d("loginres",responce);
            dialog.dismiss();

            if(s!=null) {
                if (s.equals("network")) {
                    new OTP.Alert().alert("Network !!!", getResources().getString(R.string.network_error_message));
                }
                else if (s.length()>6){
                    User u = new User();
                    try {
                        JSONObject json_session_data_form_server = new JSONObject(s);
                        u.setUsername(json_session_data_form_server.getString("username"));
                        u.setRole(json_session_data_form_server.getString("role"));
                        u.set_id(json_session_data_form_server.getString("Userid"));
                        //other data not required
//                        if(!restorePref()){
//                            savePrefsData();
//                            savePrefs();
//                        }
                    } catch (Exception e) {
                        //didnt found the data in the json string
                    }
                    Log.e("usersession",new Gson().toJson(u));
                    if(u.getRole().equals("PFarmer") || u.getRole().equals("Admin") || u.getRole().equals("Farmer") ) {
                        Intent i = new Intent(OTP.this, Farmer.class);
                        i.putExtra("User_data", u);
//                            i.putExtra("notification data",notification_data);
//                            Log.e("notification","Going to Notification");
                        startActivity(i);
                        finish();
                    } else if (u.getRole().equals("PTrader") || u.getRole().equals("Trader") ) {
                        Intent i = new Intent(OTP.this, Trader.class);
                        i.putExtra("User_data", u);
//                            i.putExtra("notification data",notification_data);
                        startActivity(i);
                        finish();
                    } else if (u.getRole().equals("QC")) {
//                        Intent i = new Intent(OTP.this, QualityCheck.class);
//                        i.putExtra("User_data", u);
////                            i.putExtra("notification data",notification_data);
//                        startActivity(i);
//                        finish();
                        Toast.makeText(OTP.this,"You are not currently registerd as Farmer or Trader",Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(OTP.this,LoginActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(OTP.this,"You are not currently registerd as Farmer or Trader",Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(OTP.this,LoginActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();
//                        if(u.getRole()!=null){
//                            Intent i = new Intent(OTP.this,Unvrified.class);
//                            i.putExtra("User_data", u);
//                            startActivity(i);
//                            finish();
//                        }
                    }
                }
                else {
                //    new OTP.Alert().SignUp("UnRegistered User!!","First Register Yourself For Our Service");
                }
            }

        }

        @Override
        protected String doInBackground(String... strings) {
            String str;
            try {
                str = POSTRequest.fetchUserData(strings[0],strings[1],OTP.this);
            } catch (Exception e) {
                Log.d("exception",e.getMessage());
                return "network";
            }
            return str;
        }
    }

    public class Alert {
        public void alert(String title, String body) {
            final AlertDialog.Builder Alert = new AlertDialog.Builder(OTP.this);
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

        public void SignUp(String title, String body) {
            final AlertDialog.Builder Alert = new AlertDialog.Builder(OTP.this);
            Alert.setCancelable(true)
                    .setTitle(title)
                    .setMessage(body);
            Alert.setNegativeButton("Sign Up", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    startActivity(new Intent(OTP.this,Sign_up.class));
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
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(OTP.this);
            View mView = getLayoutInflater().inflate(R.layout.alert_progress_spinning, null);
            ProgressBar pb = mView.findViewById(R.id.progressBar);
            mBuilder.setView(mView);
            mBuilder.setCancelable(false);
            final AlertDialog dialog = mBuilder.create();
            dialog.show();
            return dialog;
        }

        public void ChangeIP() {
            final AlertDialog.Builder mBuilder = new AlertDialog.Builder(OTP.this);
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
                    if (str.length() > 0) {
                        Main.setIp(str);
                        Toast.makeText(OTP.this, "ip changed to : " + Main.getIp(), Toast.LENGTH_LONG).show();
                        dialog.cancel();
                    } else {
                        Toast.makeText(OTP.this, "Enter the ip first", Toast.LENGTH_LONG).show();
                    }

                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_USER_CONSENT) {
            if ((resultCode == RESULT_OK) && (data != null)) {
//That gives all message to us.
// We need to get the code from inside with regex
                String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
                String numberOnly = message.replaceAll("[^0-9]", "");
//numberOnly 10886110

                String a = numberOnly.substring(2);

               strotpfrmmsg= a.substring(0, a.length() - 2);
               if(strotpfrmmsg != null){
                   otp_view.setOTP(strotpfrmmsg);
               }



            }
        }
    }

    private void registerBroadcastReceiver() {
        smsBroadcastReceiver = new SmsBroadcastReceiver();
        smsBroadcastReceiver.smsBroadcastReceiverListener =
                new SmsBroadcastReceiver.SmsBroadcastReceiverListener() {
                    @Override
                    public void onSuccess(Intent intent) {
                        startActivityForResult(intent, REQ_USER_CONSENT);
                    }
                    @Override
                    public void onFailure() {
                    }
                };
        IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
        registerReceiver(smsBroadcastReceiver, intentFilter);
    }
    @Override
    protected void onStart() {
        super.onStart();
        registerBroadcastReceiver();
    }
    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(smsBroadcastReceiver);
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