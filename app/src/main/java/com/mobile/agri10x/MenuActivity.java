package com.mobile.agri10x;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.mobile.agri10x.Connection.POSTRequest;
import com.mobile.agri10x.Model.LogOut;
import com.mobile.agri10x.Model.Main;
import com.mobile.agri10x.Model.User;
import com.mobile.agri10x.SessionManagment.SessionManager;

public class MenuActivity extends AppCompatActivity {
    ImageView but_back;

    String Userid, role, username,city;
    static User user_data_intent;
    Button abtn_logout;
    TextView txt_profile, txt_payment, txt_add_stock, txt_kyc, txt_request, txt_showinterest, txt_referfriend, txt_contact,txt_weather,txt_offer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Bundle extras = getIntent().getExtras();
        Userid = extras.getString("Userid");
        role = extras.getString("role");
        username = extras.getString("username");
        city = extras.getString("city");



        txt_weather = findViewById(R.id.txt_weather);
        but_back = findViewById(R.id.but_back);
        txt_profile = findViewById(R.id.txt_profile);
        txt_payment = findViewById(R.id.txt_payment);
        txt_add_stock = findViewById(R.id.txt_add_stock);
        txt_kyc = findViewById(R.id.txt_kyc);
        txt_request = findViewById(R.id.txt_request);
        txt_showinterest = findViewById(R.id.txt_showinterest);
        txt_referfriend = findViewById(R.id.txt_referfriend);
        txt_contact = findViewById(R.id.txt_contact);
        abtn_logout = findViewById(R.id.abtn_logout);
        txt_offer = findViewById(R.id.txt_offer);
        but_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        txt_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MenuActivity.this, UpdateProfileUser.class);
                i.putExtra("Userid", Userid);
                startActivity(i);
            }
        });
        txt_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MenuActivity.this, PaymentBalance.class);
                i.putExtra("Userid", Userid);
                startActivity(i);
            }
        });
        txt_add_stock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MenuActivity.this, AddStock.class);
                startActivity(i);
            }
        });
        txt_kyc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MenuActivity.this, UploadDocument.class);
                i.putExtra("Userid",Userid);
                startActivity(i);
            }
        });
        txt_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MenuActivity.this, RequestStock.class);
                i.putExtra("Userid", Userid);
                i.putExtra("user_type", role);
                startActivity(i);
            }
        });
        txt_showinterest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MenuActivity.this, Notification.class);
                i.putExtra("Userid", Userid);
                i.putExtra("role", role);
                startActivity(i);
            }
        });
        txt_referfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Please Download Agri10x App with this URL. \n https://play.google.com/store/apps/details?id=com.mobile.agri10x ";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });
        txt_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        abtn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialogButtonClicked();
            }
        });

        txt_weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this,Weather.class);
                intent.putExtra("cityname",city);
                intent.putExtra("Userid",Userid);
                startActivity(intent);
            }
        });
        txt_offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private void showAlertDialogButtonClicked() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout");
        builder.setMessage(" Are you sure you want to Logout?");
        // add the buttons
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // do something like...
                Logout();
            }
        });

        builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // do something like...
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", null);
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void Logout() {

        if (SessionManager.getKEY_regToken_bool()) {
            LogOut out = new LogOut(Main.id(MenuActivity.this));
            new deleteToken().execute(Main.getIp() + "/deleteToken", new Gson().toJson(out));
        } else {
            SessionManager.logoutUser();
        }
    }

    class deleteToken extends AsyncTask<String, Integer, String> {
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
            if (s != null) {
                if (s.equals("network")) {
                    new Alert().alert("Network !!!", getResources().getString(R.string.network_error_message));
                } else {
//                    Toast.makeText(Farmer.this, "Data from server: " + s, Toast.LENGTH_LONG).show();
                    SessionManager.logoutUser();
                }
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String str;
            try {
                str = POSTRequest.fetchUserData(strings[0], strings[1], MenuActivity.this);
            } catch (Exception e) {
                return "network";
            }
            return str;
        }
    }

    public class Alert {
        public void alert(String title, String body) {
            final AlertDialog.Builder Alert = new AlertDialog.Builder(MenuActivity.this);
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
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(MenuActivity.this);
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