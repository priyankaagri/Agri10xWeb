package com.mobile.agri10x;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.security.keystore.UserNotAuthenticatedException;
import android.text.Html;
import android.util.ArrayMap;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.mobile.agri10x.Connection.POSTRequest;
import com.mobile.agri10x.Model.LoginUser;
import com.mobile.agri10x.Model.Main;
import com.mobile.agri10x.Model.User;
import com.mobile.agri10x.SessionManagment.SessionManager;
import com.mobile.agri10x.models.CheckPhoneExits;
import com.mobile.agri10x.models.GetOTP;
import com.mobile.agri10x.retrofit.AgriInvestor;
import com.mobile.agri10x.retrofit.ApiHandler;

import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.security.keystore.KeyProperties.PURPOSE_ENCRYPT;

public class LoginActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    AlertDialog dialog;
    public Gson gson = new Gson();
    public static EditText mobilenumber, Password;
    String strmobilenumber;
    //  public static Button Login;
    private TextView tv_forgot_password,sendotp;
    private static String responce = null;
    public boolean doubleBackToExitPressedOnce = false;
    ImageView call,img_arrow;
    //private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 1, PERMISSIONS_REQUEST_WRITE_CONTACTS = 2, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 3,PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 4,PERMISSIONS_REQUEST_CAMERA = 5,PERMISSIONS_RECEIVE_SMS = 6;
    private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;

    private static final String[] REQUIRED_SDK_PERMISSIONS = new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission.ACCESS_NETWORK_STATE,Manifest.permission.CHANGE_NETWORK_STATE,Manifest.permission.ACCESS_WIFI_STATE,Manifest.permission.CHANGE_NETWORK_STATE};

    SessionManager session;
    Button signup;
    static String redirection;
    String notification_data;
    private static final String KEY_NAME = "AUTHENTICATION";
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    private final String plaintextstring="data";
    private final String key = "2014Jan01$";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_activity_login); //activity_login

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        checkPermissions();
        img_arrow = findViewById(R.id.img_arrow);
        call= findViewById(R.id.call);
        sendotp = findViewById(R.id.sendotp);
        mobilenumber = findViewById(R.id.mobilenumber);
        //        tv_forgot_password = findViewById(R.id.tv_forgot_password);
//        Password = findViewById(R.id.login_password);
        signup = findViewById(R.id.login_sign_up);
        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(LoginActivity.this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
//                Toast.makeText(getApplicationContext(),"Enter username for Login !!", Toast.LENGTH_SHORT)
//                        .show();
            }

            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(),
                        "Authentication succeeded!", Toast.LENGTH_SHORT).show();
                SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
                String pu = pref.getString("username","defaultValue");
                String pp = pref.getString("password","defaultValue");
                LoginUser user = new LoginUser();
                user.setUser(pu);
             //   user.setPwd(pp);
                new LoginActivity.networkPOST().execute(Main.getIp()+"/login", gson.toJson(user));
                //restorePrefData();
                try {
                    encryptSecretInformation();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (UnrecoverableKeyException e) {
                    e.printStackTrace();
                } catch (CertificateException e) {
                    e.printStackTrace();
                } catch (KeyStoreException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Authentication failed",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });
        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("TouchID for Agri10x")
                .setNegativeButtonText("Cancel")
                .setConfirmationRequired(true)
                //.setNegativeButtonText("Use account password")
                //.setDeviceCredentialAllowed(true)
                .build();
        try {
            generateSecretKey(new KeyGenParameterSpec.Builder(
                    KEY_NAME,
                    PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .setUserAuthenticationRequired(true)
                    .setUserAuthenticationValidityDurationSeconds(5000)
                    .build());
            //KeyPairGenerator.getInstance("RSA","AndroidKeyStore");
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }

//        Button biometricLoginButton = findViewById(R.id.biometric_login);
//        biometricLoginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                      new BiometricPrompt.CryptoObject(cipher));
//                biometricPrompt.authenticate(promptInfo);
//            }
//        });

//        if(restorePref()){
//            biometricPrompt.authenticate(promptInfo);
//        }
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if(preferences.getBoolean("sync",false)) {
            biometricPrompt.authenticate(promptInfo);
        }


        session = new SessionManager(getApplicationContext());  //only once through out the application
        GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(LoginActivity.this)
                .addOnConnectionFailedListener(LoginActivity.this)
                .addApi(Auth.CREDENTIALS_API)
                .build();

        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
        HintRequest hintRequest = new HintRequest.Builder()
                .setPhoneNumberIdentifierSupported(true)
                .build();

        PendingIntent intent = Auth.CredentialsApi.getHintPickerIntent(mGoogleApiClient, hintRequest);
        try {
            startIntentSenderForResult(intent.getIntentSender(), 1008, null, 0, 0, 0, null);
        } catch (IntentSender.SendIntentException e) {
            Log.e("", "Could not start hint picker Intent", e);
        }
//        tv_forgot_password.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(LoginActivity.this,ForgotPassword.class));
//            }
//        });
        //       Login = findViewById(R.id.login);
//        Login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                String username = Username.getText().toString();
//                String password = Password.getText().toString();
//                if (username.trim().length() > 0 && password.trim().length() > 0) {
//                    LoginUser user = new LoginUser();
//                    user.setUser(username);
//                    user.setPwd(password);
//                    SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
//                    SharedPreferences.Editor editor = pref.edit();
//                    editor.putString("username",user.getUser());
//                    editor.putString("password",user.getPwd());
//                    editor.commit();
////                    try {
////                        user.setUser(AESCrypt.encrypt(key,username));
////                    } catch (GeneralSecurityException e) {
////                        e.printStackTrace();
////                    }
////                    try {
////                        user.setPwd(AESCrypt.encrypt(key,password));
////                    } catch (GeneralSecurityException e) {
////                        e.printStackTrace();
////                    }
//                    new LoginActivity.networkPOST().execute(Main.getIp()+"/login", gson.toJson(user));
//                } else {
//                    Toast.makeText(getApplicationContext(), "Give Proper inputs :)", Toast.LENGTH_LONG).show();
//                }
//            }
//        });
//        Login.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                Intent i = new Intent(LoginActivity.this,Trader.class);
//                startActivity(i);
//                finish();
//                return true;
//            }
//        });
//        signup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(LoginActivity.this, Sign_up.class);
//                startActivity(i);
//            }
//        });
        img_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this,WebPage.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPhoneNumber();
            }
        });

        sendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strmobilenumber = mobilenumber.getText().toString();
                Log.d("Mobilenumber",strmobilenumber);
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
                    callcheckphone(strmobilenumber);


                }
            }
        });


    }

    private void callcheckphone(String strmobilenumber) {
        dialog = new Alert().pleaseWait();
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
                if (response.isSuccessful()) {

                    callotp(strmobilenumber);



                }
                else {
                    //    new LoginActivity.Alert().SignUp("UnRegistered User!!","First Register Yourself For Our Service");
                }
            }

            @Override
            public void onFailure(Call<CheckPhoneExits> call,
                                  Throwable t) {
                Toast.makeText(LoginActivity.this,"Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void callotp(String number) {
        Map<String, Object> jsonParams = new ArrayMap<>();
//put something inside the map, could be null
        jsonParams.put("mobileNo", "91"+number);

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),(new JSONObject(jsonParams)).toString());

        AgriInvestor apiService = ApiHandler.getApiService();
        final Call<GetOTP> loginCall = apiService.wsgetOTP(
                "123456",body);
        loginCall.enqueue(new Callback<GetOTP>() {
            @SuppressLint("WrongConstant")
            @Override
            public void onResponse(Call<GetOTP> call,
                                   Response<GetOTP> response) {
                dialog.dismiss();
                Log.d("getotp",response.toString());
                if (response.isSuccessful()) {

                    dialog.dismiss();
                    Intent i = new Intent(LoginActivity.this,OTP.class);
                    i.putExtra("mobilenumber",strmobilenumber);
                    startActivity(i);


                }
                else {
                    dialog.dismiss();
                    new LoginActivity.Alert().SignUp("","Try Again Later");
                }
            }

            @Override
            public void onFailure(Call<GetOTP> call,
                                  Throwable t) {
                dialog.dismiss();
                Toast.makeText(LoginActivity.this,"Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateMobileNo() {

        if (strmobilenumber.isEmpty() || strmobilenumber.length() < 10 ) {
            Toast.makeText(LoginActivity.this,
                    "Invalid Mobile Number", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;

    }
    protected void onStart() {
        super.onStart();

        String sessionData= getIntent().getStringExtra("session expired");
        if(sessionData!=null && sessionData.equals("true")){
            View view = findViewById(android.R.id.content);
            Snackbar.make(view, "Session Expired Login Again !!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.change_ip) {
            new Alert().ChangeIP();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
//        if (doubleBackToExitPressedOnce) {
//            Intent a = new Intent(Intent.ACTION_MAIN);
//            a.addCategory(Intent.CATEGORY_HOME);
//            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(a);
//            return;
//        }
//
//        this.doubleBackToExitPressedOnce = true;
//        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                doubleBackToExitPressedOnce=false;
//            }
//        }, 2000);

        Intent i = new Intent(LoginActivity.this,WebPage.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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
            responce = s;
            Log.d("loginres",responce);
            dialog.dismiss();

            if(s!=null) {
                if (s.equals("network")) {
                    new Alert().alert("Network !!!", getResources().getString(R.string.network_error_message));
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
                    if(u.getRole().equals("Farmer") || u.getRole().equals("Admin")) {
                        Intent i = new Intent(LoginActivity.this, Farmer.class);
                        i.putExtra("User_data", u);
//                            i.putExtra("notification data",notification_data);
//                            Log.e("notification","Going to Notification");
                        startActivity(i);
                        finish();
                    } else if (u.getRole().equals("Trader")) {
                        Intent i = new Intent(LoginActivity.this, Trader.class);
                        i.putExtra("User_data", u);
//                            i.putExtra("notification data",notification_data);
                        startActivity(i);
                        finish();
                    } else if (u.getRole().equals("QC")) {
                        Intent i = new Intent(LoginActivity.this, QualityCheck.class);
                        i.putExtra("User_data", u);
//                            i.putExtra("notification data",notification_data);
                        startActivity(i);
                        finish();
                    } else {
                        if(u.getRole()!=null){
                            Intent i = new Intent(LoginActivity.this,Unvrified.class);
                            i.putExtra("User_data", u);
                            startActivity(i);
                            finish();
                        }
                    }
                }
                else {
                   // new Alert().SignUp("UnRegistered User!!","First Register Yourself For Our Service");
                }
            }

        }

        @Override
        protected String doInBackground(String... strings) {
            String str;
            try {
                str = POSTRequest.fetchUserData(strings[0],strings[1],LoginActivity.this);
            } catch (Exception e) {
                Log.d("exception",e.getMessage());
                return "network";
            }
            return str;
        }
    }

    public class Alert {
        public void alert(String title, String body) {
            final AlertDialog.Builder Alert = new AlertDialog.Builder(LoginActivity.this);
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
            final AlertDialog.Builder Alert = new AlertDialog.Builder(LoginActivity.this);
            Alert.setCancelable(true)
                    .setTitle(title)
                    .setMessage(body);
            Alert.setNegativeButton("Sign Up", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    startActivity(new Intent(LoginActivity.this,Sign_up.class));
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
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(LoginActivity.this);
            View mView = getLayoutInflater().inflate(R.layout.alert_progress_spinning, null);
            ProgressBar pb = mView.findViewById(R.id.progressBar);
            mBuilder.setView(mView);
            mBuilder.setCancelable(false);
            final AlertDialog dialog = mBuilder.create();
            dialog.show();
            return dialog;
        }

        public void ChangeIP() {
            final AlertDialog.Builder mBuilder = new AlertDialog.Builder(LoginActivity.this);
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
                        Toast.makeText(LoginActivity.this, "ip changed to : " + Main.getIp(), Toast.LENGTH_LONG).show();
                        dialog.cancel();
                    } else {
                        Toast.makeText(LoginActivity.this, "Enter the ip first", Toast.LENGTH_LONG).show();
                    }

                }
            });
        }
    }

    /*protected void checkPermissions() {
        final List<String> missingPermissions = new ArrayList<String>();
        // check all required dynamic permissions
        for (final String permission : REQUIRED_SDK_PERMISSIONS) {
            final int result = ContextCompat.checkSelfPermission(this, permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(permission);
            }
        }
        if (!missingPermissions.isEmpty()) {
            // request all missing permissions
            final String[] permissions = missingPermissions
                    .toArray(new String[missingPermissions.size()]);
            ActivityCompat.requestPermissions(this, permissions, PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {
            final int[] grantResults = new int[REQUIRED_SDK_PERMISSIONS.length];
            Arrays.fill(grantResults, PackageManager.PERMISSION_GRANTED);
            onRequestPermissionsResult(PERMISSIONS_REQUEST_READ_CONTACTS, REQUIRED_SDK_PERMISSIONS,
                    grantResults);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {

            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION:
                for (int index = permissions.length - 1; index >= 0; --index) {
                    if (grantResults[index] != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                }
            case PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                for (int index = permissions.length - 1; index >= 0; --index) {
                    if (grantResults[index] != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                }
            case PERMISSIONS_REQUEST_CAMERA:
                for (int index = permissions.length - 1; index >= 0; --index) {
                    if (grantResults[index] != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                }
            default:
        }
    }*/
    protected void checkPermissions() {
        final List<String> missingPermissions = new ArrayList<String>();
        // check all required dynamic permissions
        for (final String permission : REQUIRED_SDK_PERMISSIONS) {
            final int result = ContextCompat.checkSelfPermission(this, permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(permission);
            }
        }
        if (!missingPermissions.isEmpty()) {
            // request all missing permissions
            final String[] permissions = missingPermissions
                    .toArray(new String[missingPermissions.size()]);
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_ASK_PERMISSIONS);
        } else {
            final int[] grantResults = new int[REQUIRED_SDK_PERMISSIONS.length];
            Arrays.fill(grantResults, PackageManager.PERMISSION_GRANTED);
            onRequestPermissionsResult(REQUEST_CODE_ASK_PERMISSIONS, REQUIRED_SDK_PERMISSIONS,
                    grantResults);
        }
    }

//    private void savePrefsData() {
//        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
//        SharedPreferences.Editor editor = pref.edit();
//        editor.putString("username",user.getUser());
//        editor.putString("password",user.getPwd());
//        editor.commit();
//    }
//    private void restorePrefData() {
//        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
//        String pu = pref.getString("username","defaultValue");
//        String pp = pref.getString("password","defaultValue");
//        user = new LoginUser();
//        user.setUser(pu);
//        user.setPwd(pp);
//        new LoginActivity.networkPOST().execute(Main.getIp()+"/login", gson.toJson(user));
//
//    }

    private void encryptSecretInformation() throws NoSuchAlgorithmException, NoSuchPaddingException, UnrecoverableKeyException, CertificateException, KeyStoreException, IOException {
        Cipher cipher = getCipher();
        SecretKey secretKey = getSecretKey();
        byte[] encryptedInfo = new byte[0];
        try {
            // NullPointerException is unhandled; use Objects.requireNonNull().
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            encryptedInfo = cipher.doFinal(
                    plaintextstring.getBytes(Charset.defaultCharset()));
            Log.d("MY_APP_TAG", "Encrypted information: " +
                    Arrays.toString(encryptedInfo));
            Log.d("MY_APP_TAG", "Encrypted information: " +
                    new String(encryptedInfo));
        } catch (UserNotAuthenticatedException e) {
            Log.d("MY_APP_TAG", "The key's validity timed out.");
            //biometricPrompt.authenticate(promptInfo);
        } catch (InvalidKeyException e) {
            Log.e("MY_APP_TAG", "Key is invalid.");
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }

        Cipher cipher1 = getCipher();
        SecretKey secretKey1 = getSecretKey();
        try {
            // NullPointerException is unhandled; use Objects.requireNonNull().
            cipher1.init(Cipher.DECRYPT_MODE, secretKey1,new IvParameterSpec(cipher.getIV()));
            byte[] decryptedInfo = cipher1.doFinal(encryptedInfo);
            Log.d("MY_APP_TAG", "Decrypted information: " +
                    Arrays.toString(decryptedInfo));
            Log.d("MY_APP_TAG", "Decrypted information: " +
                    new String(decryptedInfo));
        } catch (UserNotAuthenticatedException e) {
            Log.d("MY_APP_TAG", "The key's validity timed out.");
            //biometricPrompt.authenticate(promptInfo);
        } catch (InvalidKeyException e) {
            Log.e("MY_APP_TAG", "Key is invalid.");
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }

    }

    private void generateSecretKey(KeyGenParameterSpec keyGenParameterSpec) throws NoSuchProviderException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
        keyGenerator.init(keyGenParameterSpec);
        keyGenerator.generateKey();
    }

    private SecretKey getSecretKey() throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException, UnrecoverableKeyException {
        KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");

        // Before the keystore can be accessed, it must be loaded.
        keyStore.load(null);
        return ((SecretKey)keyStore.getKey(KEY_NAME, null));
    }

    private Cipher getCipher() throws NoSuchPaddingException, NoSuchAlgorithmException {
        return Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
                + KeyProperties.BLOCK_MODE_CBC + "/"
                + KeyProperties.ENCRYPTION_PADDING_PKCS7);
    }


    //    private boolean restorePref() {
//        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
//        Boolean isIntroActivityOpnendBefore = pref.getBoolean("isFirst",false);
//        return  isIntroActivityOpnendBefore;
//    }
//
//    private void savePrefs() {
//        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
//        SharedPreferences.Editor editor = pref.edit();
//        editor.putBoolean("isFirst",true);
//        editor.commit();
//    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults)
    {
        if(requestCode == 101)
        {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                callPhoneNumber();
            }
            else
            {
                Log.e("TAG", "Permission not Granted");
            }
        }
    }

    private void callPhoneNumber()
    {
        try
        {
            if(Build.VERSION.SDK_INT > 22)
            {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling

                    ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 101);

                    return;
                }

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + "18001212243"));
                startActivity(callIntent);

            }
            else {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + "18001212243"));
                startActivity(callIntent);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1008:
                if (resultCode == RESULT_OK) {

                    Credential cred = data.getParcelableExtra(Credential.EXTRA_KEY);
                    Credential credd = data.getParcelableExtra(Credential.EXTRA_KEY);
// cred.getId====: ====+919*******
                   // Log.e("cred.getId", cred.getId());
// userMob = cred.getId();
                //    Toast.makeText(LoginActivity.this, ""+cred.getId(), Toast.LENGTH_SHORT).show();


                } else {
// Sim Card not found!
                   // Log.e("cred.getId", "1008 else");

                    return;
                }


                break;
        }
    }
}


