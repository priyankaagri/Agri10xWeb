package com.mobile.agri10x;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.gson.Gson;
import com.mobile.agri10x.Connection.POSTRequest;
import com.mobile.agri10x.Model.LoginUser;
import com.mobile.agri10x.Model.Main;
import com.mobile.agri10x.Model.SignUpUser;
import com.mobile.agri10x.Model.User;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

public class UpdateProfile_New extends AppCompatActivity {
    public Gson gson = new Gson();
    EditText first_name,last_name;
    RadioButton radioButton,radioButton_farmer,radioButtontrader;
    RadioGroup radioGroup;
    String role_string="";
    Button UpdateProfile;
    String first_name_string="",last_name_string="",strmobile;
    TextView callnumber;
    ImageView img_arrow;
    private static String responce = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile__new);
        Findid();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            strmobile = extras.getString("mobilenumber");
            Log.d("strmobile",strmobile);

        }else{

        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                MaterialRadioButton rb = (MaterialRadioButton) group.findViewById(checkedId);
                if (rb.isChecked()) {
                    role_string = rb.getText().toString();
                }
            }
        });


        UpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                first_name_string= first_name.getText().toString().trim();
                last_name_string= last_name.getText().toString().trim();
                if(first_name_string.length()>0 && last_name_string.length()>0 && strmobile.length()>0 && role_string.length()>0){
                    new networkPOST().execute(Main.getOldUrl()+"/CreateUser");
                    // Toast.makeText(UpdateProfile_New.this,"Profile Updated Succesfully!",Toast.LENGTH_LONG).show();

                }else{

                    Toast.makeText(UpdateProfile_New.this,"Please fill the Details!",Toast.LENGTH_LONG).show();

                }
            }
        });

        callnumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                dialIntent.setData(Uri.parse("tel:" + "18001212243"));
                startActivity(dialIntent);
            }
        });

        img_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdateProfile_New.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }





    private void Findid() {

        first_name= findViewById(R.id.first_name);
        last_name= findViewById(R.id.last_name);
        radioButton = findViewById(R.id.tandcbtn);
        radioButton_farmer=findViewById(R.id.radio_farmer);
        radioGroup=findViewById(R.id.radioGroup);
        radioButtontrader=findViewById(R.id.radio_trader);
        UpdateProfile = findViewById(R.id.signin_signup_btn);
        callnumber= findViewById(R.id.callnumber);
        img_arrow= findViewById(R.id.img_arrow);
    }


    @Override
    public void onBackPressed() {

    }
    class networkPOST extends AsyncTask<String, Integer, String> {
        AlertDialog dialog;

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new Alert().pleaseWait();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            if(s!=null) {
                if (s.equals("network")) {
                    new Alert().alert("Network !!!", getResources().getString(R.string.network_error_message));
                }else {
                    if(s.equals("1")){
                        new Alert().alert("Error !!","Email ID Already Exist");
                    }else if(s.equals("2")){
                        new Alert().alert("Error !!","Telephone number Already Exist");
                    }else if(s.equals("3")){
                        new Alert().alert("Error !!","Telephone number and Email ID Already Exist");
                    }else{
                        first_name.setText("");
                        last_name.setText("");

//                        password.setText("");
//                        confirm_passwod.setText("");
                        //  role_spinner.setText("");
                        //   wallet_password.setText("");
                        //confirm_wallet_password.setText("");
//                        String uri = "@drawable/profile_final";
//                        int imageResource = getResources().getIdentifier(uri, null, getPackageName());
//                        Drawable res = getResources().getDrawable(imageResource);
//                        profile_image.setImageDrawable(res);
                        new Alert().SuccessfulSignUP();
                    }
                }
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String str;
            try {
//                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
//                byte[] byteArray = byteArrayOutputStream .toByteArray();
//                String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
//                if(role_string.equals("Quality Check")){
//                    role_string = "QC";
//                }
                Log.d("params",role_string+" "+strmobile);
                SignUpUser newUser = new SignUpUser("P"+role_string,strmobile,first_name_string,last_name_string);//email_string,contact_no_strin,encoded,first_name_string,last_name_string
                str = POSTRequest.fetchUserData(strings[0],new Gson().toJson(newUser),UpdateProfile_New.this);
            } catch (Exception e) {
                return "network";
            }
            return str;
        }

    }
    class networkLogin extends AsyncTask<String, Integer, String> {
        AlertDialog dialog;

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new UpdateProfile_New.Alert().pleaseWait();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            responce = s;
            // Log.d("loginres",responce);
            dialog.dismiss();

            if(s!=null) {
                if (s.equals("network")) {
                    new UpdateProfile_New.Alert().alert("Network !!!", getResources().getString(R.string.network_error_message));
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
                    Intent intent=new Intent(UpdateProfile_New.this,OnlyWebPage.class);
                    intent.putExtra("User_data", u);
                    startActivity(intent);


//                    if(u.getRole().equals("PFarmer") || u.getRole().equals("Admin") || u.getRole().equals("Farmer") ) {
//                        Intent i = new Intent(OTP.this, Farmer.class);
//                        i.putExtra("User_data", u);
//
//
////                            i.putExtra("notification data",notification_data);
////                            Log.e("notification","Going to Notification");
//                        startActivity(i);
//                        finish();
//                    } else if (u.getRole().equals("PTrader") || u.getRole().equals("Trader") ) {
//                        Intent i = new Intent(OTP.this, Trader.class);
//                        i.putExtra("User_data", u);
////                            i.putExtra("notification data",notification_data);
//                        startActivity(i);
//                        finish();
//                    } else if (u.getRole().equals("QC")) {
////                        Intent i = new Intent(OTP.this, QualityCheck.class);
////                        i.putExtra("User_data", u);
//////                            i.putExtra("notification data",notification_data);
////                        startActivity(i);
////                        finish();
//                        Toast.makeText(OTP.this,"You are not currently registerd as Farmer or Trader",Toast.LENGTH_SHORT).show();
//                        Intent i = new Intent(OTP.this,LoginActivity.class);
//                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        startActivity(i);
//                        finish();
//                    } else {
//                        Toast.makeText(OTP.this,"You are not currently registerd as Farmer or Trader",Toast.LENGTH_SHORT).show();
//                        Intent i = new Intent(OTP.this,LoginActivity.class);
//                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        startActivity(i);
//                        finish();
////                        if(u.getRole()!=null){
////                            Intent i = new Intent(OTP.this,Unvrified.class);
////                            i.putExtra("User_data", u);
////                            startActivity(i);
////                            finish();
////                        }
//                    }



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
                str = POSTRequest.fetchUserData(strings[0],strings[1],UpdateProfile_New.this);
            } catch (Exception e) {
                //    Log.d("exception",e.getMessage());
                return "network";
            }
            return str;
        }
    }
    public class Alert {

        public void alert(String title, String body) {
            final AlertDialog.Builder Alert = new AlertDialog.Builder(UpdateProfile_New.this);
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
            final AlertDialog.Builder Alert = new AlertDialog.Builder(UpdateProfile_New.this);
            Alert.setCancelable(false)
                    .setTitle("Successful SignUP")
                    .setMessage("Thank you for signing up.\n It will take some time to activate your account try to login after some time. \n-With Regards Agri10x Team");
            Alert.setNegativeButton("Okay", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                    finish();
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
                    String pu = pref.getString("username",strmobile);
                    String pp = pref.getString("password","defaultValue");
                    LoginUser user = new LoginUser();
                    user.setUser(pu);
                    new UpdateProfile_New.networkLogin().execute(Main.getIp()+"/login", gson.toJson(user));
//                    Intent intent = new Intent(UpdateProfile_New.this, LoginActivity.class);
//                    startActivity(intent);
                }
            });

            Alert.create().show();
        }

        public AlertDialog pleaseWait() {
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(UpdateProfile_New.this);
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