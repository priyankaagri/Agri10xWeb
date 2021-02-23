package com.mobile.agri10x;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.mobile.agri10x.Connection.POSTRequest;
import com.mobile.agri10x.Model.LogOut;
import com.mobile.agri10x.Model.Main;
import com.mobile.agri10x.SessionManagment.SessionManager;

public class SettingActivityNew extends AppCompatActivity {
    ImageView but_back;
    LinearLayout showintrest, EditProfile, logout,wallet,payment;
    TextView Shareapp, referfrnd, termscondition,version;
    String Userid, role;
    String currentVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_new);
        Bundle extras = getIntent().getExtras();
        Userid = extras.getString("Userid");
        role = extras.getString("role");

        Log.d("loguserid",Userid);
        Findid();
        Clicklisner();


    }

    private void Clicklisner() {

        wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SettingActivityNew.this, Wallet.class);
                i.putExtra("Userid",Userid);
                startActivity(i);
            }
        });
        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SettingActivityNew.this, PaymentGateway.class);
                i.putExtra("Userid",Userid);
                startActivity(i);
            }
        });
        EditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SettingActivityNew.this, UpdateProfileUser.class);
                i.putExtra("Userid",Userid);
                startActivity(i);
            }
        });
        but_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        showintrest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SettingActivityNew.this, Notification.class);
                i.putExtra("Userid", Userid);
                i.putExtra("role", role);
                startActivity(i);

            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialogButtonClicked();

            }
        });
        Shareapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Please Download Agri10x App with this URL. \n https://play.google.com/store/apps/details?id=com.mobile.agri10x ";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));

            }
        });
        referfrnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        termscondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivityNew.this, TermsAndConditions.class);
                startActivity(intent);

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
            LogOut out = new LogOut(Main.id(SettingActivityNew.this));
            new deleteToken().execute(Main.getIp() + "/deleteToken", new Gson().toJson(out));
        } else {
            SessionManager.logoutUser();
        }
    }

    private void Findid() {
        but_back = findViewById(R.id.but_back);
        EditProfile = findViewById(R.id.EditProfile);
        logout = findViewById(R.id.logout);
        showintrest = findViewById(R.id.showintrest);
        Shareapp = findViewById(R.id.Shareapp);
        referfrnd = findViewById(R.id.referfrnd);
        termscondition = findViewById(R.id.termscondition);
        version = findViewById(R.id.version);
        wallet= findViewById(R.id.wallet);
        payment = findViewById(R.id.payment);

        try {

            currentVersion = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0).versionName;

            Log.d("Current Version", "::" + currentVersion);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        version.setText( "Version : "+currentVersion);
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
                str = POSTRequest.fetchUserData(strings[0], strings[1], SettingActivityNew.this);
            } catch (Exception e) {
                return "network";
            }
            return str;
        }
    }


    public class Alert {
        public void alert(String title, String body) {
            final AlertDialog.Builder Alert = new AlertDialog.Builder(SettingActivityNew.this);
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
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(SettingActivityNew.this);
            View mView = getLayoutInflater().inflate(R.layout.alert_progress_spinning, null);
            ProgressBar pb = mView.findViewById(R.id.progressBar);
            mBuilder.setView(mView);
            mBuilder.setCancelable(false);
            final AlertDialog dialog = mBuilder.create();
            dialog.show();
            return dialog;
        }

        public void ChangeIP() {
            final AlertDialog.Builder mBuilder = new AlertDialog.Builder(SettingActivityNew.this);
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
                        Toast.makeText(SettingActivityNew.this, "ip changed to : " + Main.getIp(), Toast.LENGTH_LONG).show();
                        dialog.cancel();
                    } else {
                        Toast.makeText(SettingActivityNew.this, "Enter the ip first", Toast.LENGTH_LONG).show();
                    }

                }
            });
        }
    }

    @Override
    public void onBackPressed() {

    }
}