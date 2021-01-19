package com.mobile.agri10x;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.mobile.agri10x.Connection.GETRequest;
import com.mobile.agri10x.Connection.LoadImage;
import com.mobile.agri10x.Connection.POSTRequest;
import com.mobile.agri10x.SessionManagment.SessionManager;
import com.mobile.agri10x.Model.ErrorLog;
import com.mobile.agri10x.Model.Main;
import com.mobile.agri10x.Model.registrationToken;
import com.mobile.agri10x.Model.SecurityData;
import com.mobile.agri10x.Model.User;

import org.json.JSONException;
import org.json.JSONObject;

public class Employee extends AppCompatActivity {

    TextView nav_username;
    ImageView nav_image;
    SessionManager session;
    static User user_data_intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);
        session = new SessionManager(getApplicationContext());
        nav_username = findViewById(R.id.nav_username);
        nav_image = findViewById(R.id.nav_userImage);
        Intent intent = new Intent(Employee.this, MyService.class);
        startService(intent);
        loadData();
    }

    void loadData(){
        String e = SessionManager.getKEY_Entity_id();
        String s = SessionManager.getUsername();
        //if(SessionManager.getUsername()!=null && user_data_intent.getUsername().equals(SessionManager.getUsername())){
          if(false){
            //do nothing
            if(SessionManager.getImageURI()!=null ){
                final String url = Main.getOldUrl()+ SessionManager.getImageURI();
                //download the image if not present in the sessionManager
                new downloadImage().execute(url);
                String f1 = SessionManager.getFirstName();

                String fullName = SessionManager.getFirstName()+" "+ SessionManager.getLastName();
                nav_username.setText(fullName);

                if(!SessionManager.getKEY_regToken_bool()){
                    registerTokenAgain();
                }
                //show wallet data
            }
        }
        else{
            new LoadEntitiyData().execute(Main.getIp()+"/UserInfo");
        }
    }


    void CreateNewSession(User u){
        if(u!=null) {
            SessionManager.createLoginSession(u);
            registerTokenAgain();
            final String url = Main.getOldUrl() + u.getImgUrl();
            new downloadImage().execute(url);
            //new LoadWalletBalance().execute(Main.getIp() + "/getBalance");
        }
    }

    void registerTokenAgain(){
        final String[] token_string = new String[1];
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }else {

                            token_string[0] = task.getResult().getToken();
                            String UUID = Main.id(Employee.this);

                            if(UUID!=null) {
                                registrationToken tokenToServer = new registrationToken(SessionManager.getKEY_Entity_id(), SessionManager.getRole(), UUID, token_string[0]);
                                new registerToken().execute(Main.getIp()+"/getDetails", new Gson().toJson(tokenToServer));
                            }

                        }
                    }
                });
    }

    class registerToken extends AsyncTask<String, Integer, String> {

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s!=null) {
                if (s.equals("network")) {
                    new Alert().alert("Network !!!", getResources().getString(R.string.network_error_message));
                } else {
                    SessionManager.setKEY_regToken_bool(true);
                }
            }

        }

        @Override
        protected String doInBackground(String... strings) {
            String str;
            try {
                str = POSTRequest.fetchUserData(strings[0],strings[1],Employee.this);
            } catch (Exception e) {
                return "network";
            }
            return str;
        }
    }

    class LoadEntitiyData extends AsyncTask<String, Integer, String> {
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
        protected void onPostExecute(String s) {        //session Expired = null , "network" (Due to exception) , {...} data
            super.onPostExecute(s);
            dialog.dismiss();
            if(s!=null) {
                if (s.equals("network")) {
                    new Alert().alert("Network !!!", getResources().getString(R.string.network_error_message));
                } else {
                    User u = User.extractFeatureFromJson(s);

                    if(u.getFirstname()!=null )
                    {
                        String name = u.getFirstname();
                        if(u.getLastname()!=null)
                            name +=u.getLastname();
                        if(name!=null)
                            nav_username.setText(name);

                    }
                    else{
                        ErrorLog errorLog = new ErrorLog(Main.getIp()+"/UserInfo","Firstname","String",null,"Employee.LoadEntitiyData->onPostExecute()",getResources().getString(R.string.DeviceName),getClass().getSimpleName());
                        Main.addErrorReportRequest(errorLog,Employee.this);
                    }
                    CreateNewSession(u);
                }
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String str;
            try {
                str = GETRequest.fetchUserData(strings[0], Employee.this);
            } catch (Exception e) {
                return "network";
            }
            return str;
        }
    } // okey

    class downloadImage extends AsyncTask<String, Integer, Bitmap> {

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if(bitmap!=null){
                nav_image.setImageBitmap(bitmap);
//                session.setImage(bitmap);
                SecurityData.setUserImage(bitmap);
            }
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bitmap;
            try {
                bitmap = LoadImage.fetchUserData(strings[0], Employee.this);
            } catch (Exception e) {
                return null;
            }
            return bitmap;
        }
    }

    class LoadWalletBalance extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s!=null) {
                if (s.equals("network")) {
                    new Alert().alert("Network !!!", getResources().getString(R.string.network_error_message));
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        SecurityData.setClosingBalance(jsonObject.getString("Closing"));
                        SecurityData.setWithdthrawBalance(jsonObject.getString("Widthraw"));
                        SecurityData.setPendingBalance(jsonObject.getString("Pending"));
                        if(SecurityData.getWithdthrawBalance()!=null && SecurityData.getClosingBalance()!=null) {
                            //nav_wallet_balance.setText("Withdrawable: " + Main.getIndian_currency() + " " + SecurityData.getWithdthrawBalance());
                            //nav_closing_bal.setText("Closing: " + Main.getIndian_currency() + " " + SecurityData.getClosingBalance());
                        }else{
                            ErrorLog errorLog = new ErrorLog(Main.getIp()+"/getBalance","Widthraw || Closing","String",null,"Employee.LoadWalletBalance->onPostExecute()",getResources().getString(R.string.DeviceName),getClass().getSimpleName());
                            Main.addErrorReportRequest(errorLog,Employee.this);
                        }
                    } catch (JSONException e) {
                        SecurityData.setWithdthrawBalance("0");
                        SecurityData.setClosingBalance("0");
                        //nav_wallet_balance.setText("Withdrawable: " +Main.getIndian_currency() + " " + SecurityData.getWithdthrawBalance());
                        //nav_closing_bal.setText("Closing: "+Main.getIndian_currency()+" "+SecurityData.getClosingBalance());
                    }
                }
            }
            else{
                SecurityData.setWithdthrawBalance("0");
                SecurityData.setClosingBalance("0");
                //nav_wallet_balance.setText("Withdrawable: " +Main.getIndian_currency() + " " + SecurityData.getWithdthrawBalance());
                //nav_closing_bal.setText("Closing: "+Main.getIndian_currency()+" "+SecurityData.getClosingBalance());
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String str;
            try {
                str = GETRequest.fetchUserData(strings[0], Employee.this);
            } catch (Exception e) {
                return "network";
            }
            return str;
        }
    } //okey

    @Override
    public void onBackPressed() {
            super.onBackPressed();
    }

    public class Alert {
        public void alert( String title, String body) {
            final AlertDialog.Builder Alert = new AlertDialog.Builder(Employee.this);
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
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(Employee.this);
            View mView = getLayoutInflater().inflate(R.layout.alert_progress_spinning, null);
            ProgressBar pb = mView.findViewById(R.id.progressBar);
            mBuilder.setView(mView);
            mBuilder.setCancelable(false);
            final AlertDialog dialog = mBuilder.create();
            dialog.show();
            return dialog;
        }

        public void ChangeIP(){
            final AlertDialog.Builder mBuilder = new AlertDialog.Builder(Employee.this);
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
                        Toast.makeText(Employee.this,"ip changed to : "+Main.getIp(),Toast.LENGTH_LONG).show();
                        dialog.cancel();
                    }
                    else
                    {
                        Toast.makeText(Employee.this,"Enter the ip first",Toast.LENGTH_LONG).show();
                    }

                }
            });
        }
    }
}
