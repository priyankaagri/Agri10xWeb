package com.mobile.agri10x;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.mobile.agri10x.Model.Main;
import com.mobile.agri10x.Model.UserId;
import com.mobile.agri10x.models.GetProfile;
import com.mobile.agri10x.models.GetUserInfo;
import com.mobile.agri10x.models.SetAddressO;
import com.mobile.agri10x.models.SetUserO;
import com.mobile.agri10x.retrofit.AgriInvestor;
import com.mobile.agri10x.retrofit.ApiHandler;

import org.json.JSONObject;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateProfileUser extends AppCompatActivity {
    EditText mobilenumber,first_name, last_name, emailaddress, address, address1, address2, city, country;
    Button btn_save;
    String userid;
    ImageView img_arrow;
    AlertDialog dialog, dialog2;
    String rople;
    TextView change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile_user);


        change = findViewById(R.id.change);
        mobilenumber = findViewById(R.id.mobilenumber);
        img_arrow = findViewById(R.id.img_arrow);
        first_name = findViewById(R.id.first_name);
        last_name = findViewById(R.id.last_name);
        emailaddress = findViewById(R.id.emailaddress);
        address = findViewById(R.id.address);
        address1 = findViewById(R.id.address1);
        address2 = findViewById(R.id.address2);
        city = findViewById(R.id.city);
        country = findViewById(R.id.country);
        btn_save = findViewById(R.id.btn_save);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            userid = extras.getString("Userid");

        }
        Log.d("Loguserid", userid);
        dialog = new UpdateProfileUser.Alert().pleaseWait();
        callGetDAta();

        img_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

               /* String first = first_name.getText().toString();
                String last = last_name.getText().toString();
                String wholename = first + " " + last;
                if (rople != null || !rople.isEmpty()) {
                    Intent i = new Intent(UpdateProfileUser.this, SettingActivityNew.class);
                    i.putExtra("Userid", userid);
                    i.putExtra("role", rople);
                    i.putExtra("username", wholename);
                    startActivity(i);
                } else {
                    finish();
                }*/



            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String first_name_string = first_name.getText().toString().trim();
                String last_name_string = last_name.getText().toString().trim();
                if (first_name_string.length() > 0 && last_name_string.length() > 0) {
                    dialog2 = new UpdateProfileUser.Alert().pleaseWait();
                    callupdateapi();
                } else {
                    Toast.makeText(UpdateProfileUser.this, "Please Enter First Name and Last Name", Toast.LENGTH_SHORT).show();
                }

            }
        });


        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent updatenumber = new Intent(UpdateProfileUser.this,UpdateNumber.class);
                 updatenumber.putExtra("Userid",userid);
                startActivity(updatenumber);

            }
        });
    }

    private void callupdateapi() {

        SetAddressO setAddressO = new SetAddressO();
        setAddressO.setAddress1(address.getText().toString());
        setAddressO.setAddress2(address1.getText().toString());
        setAddressO.setAddress3(address2.getText().toString());
        setAddressO.setAddress4("");
        setAddressO.setCity(city.getText().toString());
        setAddressO.setCountry(country.getText().toString());

        SetUserO setUserO = new SetUserO();
        setUserO.setAddress(setAddressO);
// setUserO.getAddress().getAddress2();
// setUserO.getAddress().getAddress3();
// setUserO.getAddress().getAddress4();
// setUserO.getAddress().getCity();
// setUserO.getAddress().getCountry();
        setUserO.setUid(userid);
        setUserO.setEmail(emailaddress.getText().toString());
        setUserO.setFn(first_name.getText().toString());
        setUserO.setLn(last_name.getText().toString());

// RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),(new JSONObject(jsonParams)).toString());

        AgriInvestor apiService = ApiHandler.getApiService();
        final Call<GetUserInfo> loginCall = apiService.wssetuserinfo(
                "123456", setUserO);
        loginCall.enqueue(new Callback<GetUserInfo>() {
            @SuppressLint("WrongConstant")
            @Override
            public void onResponse(Call<GetUserInfo> call,
                                   Response<GetUserInfo> response) {
                dialog2.dismiss();
                Log.d("resendotpres", response.toString());
                if (response.isSuccessful()) {
                    String strupdate = response.body().getUpdate().toString();
                    callGetDAta();
                    Toast.makeText(UpdateProfileUser.this, "Data Updated Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    dialog2.dismiss();
                }
            }

            @Override
            public void onFailure(Call<GetUserInfo> call,
                                  Throwable t) {
                dialog2.dismiss();
                Toast.makeText(UpdateProfileUser.this, "Date Not Updated", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void callGetDAta() {
        Map<String, Object> jsonParams = new ArrayMap<>();
//put something inside the map, could be null


        jsonParams.put("userID", userid);

        Log.d("params", userid);

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams)).toString());

        AgriInvestor apiService = ApiHandler.getApiService();
        final Call<GetProfile> loginCall = apiService.wsGetProfileData(
                "123456", body);
        loginCall.enqueue(new Callback<GetProfile>() {
            @SuppressLint("WrongConstant")
            @Override
            public void onResponse(Call<GetProfile> call,
                                   Response<GetProfile> response) {
                dialog.dismiss();
                Log.d("verifyOTP", response.toString());
                if (response.isSuccessful()) {

                    if(!response.body().getTelephone().isEmpty() || response.body().getTelephone() != null){
                        String strgetmob = response.body().getTelephone();
                        strgetmob = strgetmob.substring(2);
                        Log.d("getmob",strgetmob);
                        mobilenumber.setText(strgetmob);
                    }else{
                        mobilenumber.setHint("Mobile Number");
                    }
                    if (!response.body().getFirstname().isEmpty() || response.body().getFirstname() != null) {
                        first_name.setText(response.body().getFirstname());
                    } else {
                        first_name.setHint("First Name");
                    }
                    if (!response.body().getLastname().isEmpty() || response.body().getLastname() != null) {
                        last_name.setText(response.body().getLastname());
                    } else {
                        last_name.setHint("Last Name");
                    }
                    if (!response.body().getEmail().isEmpty() || response.body().getEmail() != null) {
                        emailaddress.setText(response.body().getEmail());
                    } else {
                        emailaddress.setHint("Email");
                    }
                    if (!response.body().getAddress().getAddress1().isEmpty() || response.body().getAddress().getAddress1() != null) {
                        address.setText(response.body().getAddress().getAddress1());
                    } else {
                        address.setHint("Address");
                    }
                    if (!response.body().getAddress().getAddress2().isEmpty() || response.body().getAddress().getAddress2() != null) {
                        address1.setText(response.body().getAddress().getAddress2());
                    } else {
                        address1.setHint("Address1");
                    }
                    if (!response.body().getAddress().getAddress3().isEmpty() || response.body().getAddress().getAddress3() != null) {
                        address2.setText(response.body().getAddress().getAddress3());
                    } else {
                        address2.setHint("Address2");
                    }
                    if (!response.body().getAddress().getCity().isEmpty() || response.body().getAddress().getCity() != null) {
                        city.setText(response.body().getAddress().getCity());
                    } else {
                        city.setHint("City");
                    }
                    if (!response.body().getAddress().getCountry().isEmpty() || response.body().getAddress().getCountry() != null) {
                        country.setText(response.body().getAddress().getCountry());
                    } else {
                        country.setHint("Country");
                    }
                    rople = response.body().getRole();


                } else {
                    dialog.dismiss();
                    Toast.makeText(UpdateProfileUser.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetProfile> call,
                                  Throwable t) {
                dialog.dismiss();
                Toast.makeText(UpdateProfileUser.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {

    }


    public class Alert {


        public AlertDialog pleaseWait() {
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(UpdateProfileUser.this);
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