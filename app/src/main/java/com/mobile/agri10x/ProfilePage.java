package com.mobile.agri10x;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.mobile.agri10x.Connection.GETRequest;
import com.mobile.agri10x.Connection.LoadImage;
import com.mobile.agri10x.Model.ErrorLog;
import com.mobile.agri10x.Model.Main;
import com.mobile.agri10x.Model.SecurityData;
import com.mobile.agri10x.Model.User;

import java.io.InputStream;


public class ProfilePage extends AppCompatActivity {

    Toolbar toolbar;
    ImageView role_image,kyc_status_image;
    //    RatingBar user_rating;
    TextView email,phone_no,Address,user_Name,entryDate;
    ImageView userImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        Intent i = getIntent();
        User u=(User)i.getSerializableExtra("myUser");

        role_image= findViewById(R.id.image_role);
        kyc_status_image = findViewById(R.id.kyc_status);
        toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_name );
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        entryDate = findViewById(R.id.entry_date);
        email = findViewById(R.id.user_email);
        phone_no = findViewById(R.id.user_pno);
        user_Name = findViewById(R.id.usernameProfilePage);
        Address = findViewById(R.id.addressdata);
        userImage = findViewById(R.id.userImageProfilePage);

        //new LoadEntitiyData().execute(Main.getIp()+"/UserInfo");
        //Profile image
        new ProfilePage.DownloadImageTask(userImage).execute(Main.getBaseUrl()+u.getImgUrl());
        //new ProfilePage.downloadImage().execute(Main.getBaseUrl()+u.getImgUrl());
        //Username
        if(u.getFirstname()!=null )
        {
            String name = u.getFirstname();
            if(u.getLastname()!=null)
                name +=u.getLastname();
            if(name!=null)
                user_Name.setText(name);
        }
        //Date Created
        if(u.getDateCreated()!=null)
            entryDate.setText(u.getDateCreated());
        //KYC status
        String kyc_status = u.getKyc();
        if (kyc_status != null && kyc_status.equals("Verified")) {
            //verified
            String uri = "@drawable/kyc_status_placeholder";
            int imageResource = getResources().getIdentifier(uri, null, getPackageName());
            Drawable res = getResources().getDrawable(imageResource);
            kyc_status_image.setImageDrawable(res);
        } else {   //not verified
            String uri = "@drawable/kyc_not_done";
            int imageResource = getResources().getIdentifier(uri, null, getPackageName());
            Drawable res = getResources().getDrawable(imageResource);
            kyc_status_image.setImageDrawable(res);
        }
        //Email
        email.setText(u.getEmail());
        //Telephone
        if(u.getTelephone()!=null){
            phone_no.setText(u.getTelephone());
        }
        //Address
        String address="";
        if(u.getAddress().getAddress1()!=null){
            address += u.getAddress().getAddress1();
            if(u.getCity()!=null)
            {
                address+="\n"+u.getCity();
                if(u.getCountry()!=null)
                    address+="\n"+u.getCountry();
            }
            Address.setText(address);
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap bmp = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                bmp = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return bmp;
        }
        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    /*class LoadEntitiyData extends AsyncTask<String, Integer, String> {
        AlertDialog dialog;

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProfilePage.Alert().pleaseWait();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            if(s!=null) {
                if (s.equals("network")) {
                    new Alert().alert("Network !!!", getResources().getString(R.string.network_error_message));
                }else {
                    User u = User.extractFeatureFromJson(s);
                    if(u!=null) {
                        if(u.getFirstname()!=null )
                        {
                            String name = u.getFirstname();
                            if(u.getLastname()!=null)
                                name +=u.getLastname();
                            if(name!=null)
                                user_Name.setText(name);
                        }
                        else{
                            ErrorLog errorLog = new ErrorLog(Main.getIp()+"/UserInfo","Firstname","String",null,"ProfilePage.LoadEntitiyData->onPostExecute()",getResources().getString(R.string.DeviceName),getClass().getSimpleName());
                            Main.addErrorReportRequest(errorLog, ProfilePage.this);
                        }
                        if (u.getEmail() != null) {
                            email.setText(u.getEmail());
                        } else {
                            email.setText("xyz@gmail.com");
                            ErrorLog errorLog = new ErrorLog(Main.getIp()+"/UserInfo","Email","String",null,"ProfilePage.LoadEntitiyData->onPostExecute()",getResources().getString(R.string.DeviceName),getClass().getSimpleName());
                            Main.addErrorReportRequest(errorLog, ProfilePage.this);
                        }
                        String address="";
                        if(u.getAddress().getAddress1()!=null){
                            address += u.getAddress().getAddress1();
                            if(u.getCity()!=null)
                            {
                                address+="\n"+u.getCity();
                                if(u.getCountry()!=null)
                                    address+="\n"+u.getCountry();
                            }
                            Address.setText(address);
                        }
                        else{
                            ErrorLog errorLog = new ErrorLog(Main.getIp()+"/UserInfo","address.address1","String",null,"ProfilePage.LoadEntitiyData->onPostExecute()",getResources().getString(R.string.DeviceName),getClass().getSimpleName());
                            Main.addErrorReportRequest(errorLog, ProfilePage.this);
                        }

                        if(u.getTelephone()!=null){
                            phone_no.setText(u.getTelephone());
                        }
                        else{
                            ErrorLog errorLog = new ErrorLog(Main.getIp()+"/UserInfo","Telephone","String",null,"ProfilePage.LoadEntitiyData->onPostExecute()",getResources().getString(R.string.DeviceName),getClass().getSimpleName());
                            Main.addErrorReportRequest(errorLog, ProfilePage.this);
                        }

                        if(u.getDateCreated()!=null)
                            entryDate.setText(u.getDateCreated());
                        else{
                            ErrorLog errorLog = new ErrorLog(Main.getIp()+"/UserInfo","DateCreated","String",null,"ProfilePage.LoadEntitiyData->onPostExecute()",getResources().getString(R.string.DeviceName),getClass().getSimpleName());
                            Main.addErrorReportRequest(errorLog, ProfilePage.this);
                        }

                        if (u.getRole()!=null && u.getRole().equals("Admin")) {
                            String uri = "@drawable/admin_placehlder";
                            int imageResource = getResources().getIdentifier(uri, null, getPackageName());
                            Drawable res = getResources().getDrawable(imageResource);
                            role_image.setImageDrawable(res);
                        } else {
                            ErrorLog errorLog = new ErrorLog(Main.getIp()+"/UserInfo","role","String",null,"ProfilePage.LoadEntitiyData->onPostExecute()",getResources().getString(R.string.DeviceName),getClass().getSimpleName());
                            Main.addErrorReportRequest(errorLog, ProfilePage.this);
                        }

                        String kyc_status = u.getKyc();
                        if (kyc_status != null && kyc_status.equals("Verified")) {
                            //verified
                            String uri = "@drawable/kyc_status_placeholder";
                            int imageResource = getResources().getIdentifier(uri, null, getPackageName());
                            Drawable res = getResources().getDrawable(imageResource);
                            kyc_status_image.setImageDrawable(res);
                        } else {   //not verified
                            String uri = "@drawable/kyc_not_done";
                            int imageResource = getResources().getIdentifier(uri, null, getPackageName());
                            Drawable res = getResources().getDrawable(imageResource);
                            kyc_status_image.setImageDrawable(res);
                        }
                        if (SecurityData.getUserImage() != null) {
                            userImage.setImageBitmap(SecurityData.getUserImage());
                        } else {
                            new downloadImage().execute(Main.getOldUrl() + u.getImgUrl());
                        }
                    }

                }
            }

        }

        @Override
        protected String doInBackground(String... strings) {
            String str;
            try {
                str = GETRequest.fetchUserData(strings[0], ProfilePage.this);
            } catch (Exception e) {
                return "network";
            }
            return str;
        }
    }*/

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
                userImage.setImageBitmap(bitmap);
            }
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bitmap;
            try {
                bitmap = LoadImage.fetchUserData(strings[0], ProfilePage.this);
            } catch (Exception e) {
                return null;
            }
            return bitmap;
        }
    }

    public class Alert {
        public void alert(String title, String body) {
            final AlertDialog.Builder Alert = new AlertDialog.Builder(ProfilePage.this);
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
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(ProfilePage.this);
            View mView = getLayoutInflater().inflate(R.layout.alert_progress_spinning, null);
            ProgressBar pb = mView.findViewById(R.id.progressBar);
            mBuilder.setView(mView);
            mBuilder.setCancelable(false);
            final AlertDialog dialog = mBuilder.create();
            dialog.show();
            return dialog;
        }
        public void ChangeIP() {
            final AlertDialog.Builder mBuilder = new AlertDialog.Builder(ProfilePage.this);
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
                        Toast.makeText(ProfilePage.this, "ip changed to : " + Main.getIp(), Toast.LENGTH_LONG).show();
                        dialog.cancel();
                    } else {
                        Toast.makeText(ProfilePage.this, "Enter the ip first", Toast.LENGTH_LONG).show();
                    }

                }
            });
        }
    }
}
