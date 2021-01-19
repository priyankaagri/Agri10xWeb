package com.mobile.agri10x;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mobile.agri10x.Connection.POSTRequest;
import com.mobile.agri10x.Model.ForgotPasswordModel;
import com.mobile.agri10x.Model.Main;

public class ForgotPassword extends AppCompatActivity {
    private EditText email,password,confirm_password,phone_number;
    private Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        email = findViewById(R.id.forgot_email);
        phone_number = findViewById(R.id.forgot_phone);
        password = findViewById(R.id.new_password);
        confirm_password = findViewById(R.id.confirm_new_password);
        submit = findViewById(R.id.forgot_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Email = email.getText().toString();
                String Phone_number = phone_number.getText().toString();
                String Password = password.getText().toString();
                String Confirm_password = confirm_password.getText().toString();
                if ((Email.trim().length() > 0) && (Phone_number.trim().length() > 0) && (Password.trim().length() > 0) && (Confirm_password.trim().length() > 0)) {
                    if(Password.equals(Confirm_password)) {
                        ForgotPasswordModel forgotPasswordModel = new ForgotPasswordModel(Email, Phone_number, Password);
                        new ForgotPassword.networkPOST().execute(Main.getIp()+"/forgot", new Gson().toJson(forgotPasswordModel));
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Give Proper inputs!!!", Toast.LENGTH_LONG).show();
                }
            }
        });
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
                    new Alert().alert("", getResources().getString(R.string.network_error_message));
                }
                else if(s.equals("Sorry, Data provided does not match with our records")){
                    new Alert().alert("","Sorry, Data provided does not match with our records");
                }
                else if(s.equals("Server Busy. Try After some time")){
                    new Alert().alert("","Server Busy. Try After some time");
                }
                else if (s.contains("Password changed")){
                    new Alert().alert("","Password Changed Successfully.");
                    ForgotPassword.this.finish();
                }
                email.setText("");
                phone_number.setText("");
                password.setText("");
                confirm_password.setText("");
            }

        }

        @Override
        protected String doInBackground(String... strings) {
            String str;
            try {
                str = POSTRequest.fetchUserData(strings[0],strings[1],ForgotPassword.this);
            } catch (Exception e) {
                return "network";
            }
            return str;
        }
    }

    public class Alert {
        public void alert(String title, String body) {
            final AlertDialog.Builder Alert = new AlertDialog.Builder(ForgotPassword.this);
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

        public AlertDialog pleaseWait() {
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(ForgotPassword.this);
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
