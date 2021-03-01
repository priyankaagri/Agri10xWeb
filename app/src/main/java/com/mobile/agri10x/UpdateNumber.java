package com.mobile.agri10x;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.agri10x.Model.LoginUser;
import com.mobile.agri10x.Model.Main;
import com.mobile.agri10x.models.CheckPhoneExits;
import com.mobile.agri10x.models.GetOTP;
import com.mobile.agri10x.models.GetUpdateContact;
import com.mobile.agri10x.retrofit.AgriInvestor;
import com.mobile.agri10x.retrofit.ApiHandler;

import org.json.JSONObject;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateNumber extends AppCompatActivity {
String mobilenumber,userid;
TextView sendotp;
EditText mobilenumberedt;
    AlertDialog dialog;
    ImageView img_arrow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_number);

        sendotp = findViewById(R.id.sendotp);
        mobilenumberedt = findViewById(R.id.mobilenumberedt);
        img_arrow = findViewById(R.id.img_arrow);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            userid = extras.getString("Userid");

        }
        Log.d("Loguserid", userid);
        sendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mobilenumber = mobilenumberedt.getText().toString();
                Log.d("Mobilenumber",mobilenumber);
                if(validateMobileNo()){
//                    InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
//                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    InputMethodManager imm = (InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);

                    if (imm.isAcceptingText()) {
                        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    } else {
                        // writeToLog("Software Keyboard was not shown");
                    }
                    callcheckphone(mobilenumber);


                }
            }
        });

        img_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }

    private boolean validateMobileNo() {

        if (mobilenumber.isEmpty() || mobilenumber.length() < 10 ) {
            Toast.makeText(UpdateNumber.this,
                    "Invalid Mobile Number", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;

    }

    private void callcheckphone(String strmobilenumber) {
        dialog = new UpdateNumber.Alert().pleaseWait();
        Map<String, Object> jsonParams = new ArrayMap<>();
//put something inside the map, could be null
        jsonParams.put("tp", "91"+strmobilenumber);

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
                String checkresponse =String.valueOf(response.body().getExist());
                Log.d("checkphone",checkresponse);
                if (checkresponse.equals("true")) {
dialog.dismiss();
                    Toast.makeText(UpdateNumber.this,"Number already exits",Toast.LENGTH_SHORT).show();



                }
                if(checkresponse.equals("false")) {
                    callupdatemobile(strmobilenumber);

                }
            }

            @Override
            public void onFailure(Call<CheckPhoneExits> call,
                                  Throwable t) {
                Toast.makeText(UpdateNumber.this,"Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void callupdatemobile(String number) {
        Map<String, Object> jsonParams = new ArrayMap<>();
//put something inside the map, could be null
        jsonParams.put("Tel", "91"+number);
        jsonParams.put("usrid",userid);

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),(new JSONObject(jsonParams)).toString());

        AgriInvestor apiService = ApiHandler.getApiService();
        final Call<GetUpdateContact> loginCall = apiService.wsGetUpdateContact(
                "123456",body);
        loginCall.enqueue(new Callback<GetUpdateContact>() {
            @SuppressLint("WrongConstant")
            @Override
            public void onResponse(Call<GetUpdateContact> call,
                                   Response<GetUpdateContact> response) {
                dialog.dismiss();
                Log.d("updatenumber",response.body().getMessage());
                if (response.body().getMessage().equals("Updated")) {

                    dialog.dismiss();
//                    Intent i = new Intent(UpdateNumber.this,LoginActivity.class);
//                    startActivity(i);

                    new UpdateNumber.Alert().SuccessfulSignUP();
                }
                else {
                    dialog.dismiss();
                    Toast.makeText(UpdateNumber.this,"Number not updated",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetUpdateContact> call,
                                  Throwable t) {
                dialog.dismiss();
                Toast.makeText(UpdateNumber.this,"Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public class Alert {
        public void alert(String title, String body) {
            final AlertDialog.Builder Alert = new AlertDialog.Builder(UpdateNumber.this);
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
        public void SuccessfulSignUP() {
            final AlertDialog.Builder Alert = new AlertDialog.Builder(UpdateNumber.this);
            Alert.setCancelable(false)
                    .setTitle("Update Successfully")
                    .setMessage("Mobile number updated successfully.\n Please login with updated mobile number. \n-With Regards Agri10x Team");
            Alert.setNegativeButton("Okay", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                    finish();


                    Intent intent = new Intent(UpdateNumber.this, LoginActivity.class);
                    startActivity(intent);
                }
            });

            Alert.create().show();
        }
        public void SignUp(String title, String body) {
            final AlertDialog.Builder Alert = new AlertDialog.Builder(UpdateNumber.this);
            Alert.setCancelable(true)
                    .setTitle(title)
                    .setMessage(body);
            Alert.setNegativeButton("Sign Up", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    startActivity(new Intent(UpdateNumber.this,Sign_up.class));
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
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(UpdateNumber.this);
            View mView = getLayoutInflater().inflate(R.layout.alert_progress_spinning, null);
            ProgressBar pb = mView.findViewById(R.id.progressBar);
            mBuilder.setView(mView);
            mBuilder.setCancelable(false);
            final AlertDialog dialog = mBuilder.create();
            dialog.show();
            return dialog;
        }

        public void ChangeIP() {
            final AlertDialog.Builder mBuilder = new AlertDialog.Builder(UpdateNumber.this);
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
                        Toast.makeText(UpdateNumber.this, "ip changed to : " + Main.getIp(), Toast.LENGTH_LONG).show();
                        dialog.cancel();
                    } else {
                        Toast.makeText(UpdateNumber.this, "Enter the ip first", Toast.LENGTH_LONG).show();
                    }

                }
            });
        }
    }

    @Override
    public void onBackPressed() {

    }
}