package com.mobile.agri10x;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.mobile.agri10x.Model.Main;
import com.rilixtech.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class GuestActivity extends AppCompatActivity {

    FirebaseAuth auth;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    CountryCodePicker ccp;
    CountDownTimer mCountDownTimer;
    private EditText guest_contact_no,guest_otp;
    private TextView resendCode,timer;
    private Button login;
    private String verification_code;
    boolean temp = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest);
        guest_contact_no = findViewById(R.id.guest_contact_no);
        guest_otp = findViewById(R.id.guest_otp);
        ccp = findViewById(R.id.guest_ccp);
        timer = findViewById(R.id.timer);
        resendCode = findViewById(R.id.resend_code);
        resendCode.setEnabled(false);

        auth = FirebaseAuth.getInstance();
        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                String code = phoneAuthCredential.getSmsCode();
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(GuestActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verification_code = s;
                Toast.makeText(GuestActivity.this, "code sent to number", Toast.LENGTH_SHORT).show();
                login.setText("Login");
            }
        };

        resendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                temp = false;
                sent_sms();
                Toast.makeText(GuestActivity.this, "Code resent", Toast.LENGTH_SHORT).show();
            }
        });

        mCountDownTimer=new CountDownTimer(60000,1000) {

            @Override
            public void onTick(long m) {
                long minutes = (m / 1000) / 60;
                long seconds = (m / 1000) % 60;
                timer.setText(minutes +" : "+ seconds);
            }

            @Override
            public void onFinish() {
                resendCode.setEnabled(true);
            }
        };
        login = findViewById(R.id.guest_login);
        login.setText("Send OTP");
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(temp){
                if(guest_contact_no.length()==10){
                    sent_sms();
                    //String inputcode = guest_otp.getText().toString().trim();
                    //verifyPhoneNumber(verification_code,inputcode);
                    temp = false;
                    }
                }
                else if(!temp&&(guest_otp.getText().toString().trim().length()==6)){
                    String inputcode = guest_otp.getText().toString().trim();
                    verifyPhoneNumber(verification_code,inputcode);
                }
            }
        });
    }

    public void sent_sms()
    {
        mCountDownTimer.start();
        //Toast.makeText(this,"sending code ",Toast.LENGTH_LONG).show();
        String number = ccp.getFullNumberWithPlus() + guest_contact_no.getText().toString();
        Toast.makeText(this,number,Toast.LENGTH_LONG).show();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,60, TimeUnit.SECONDS,this,mCallback);
    }

    public void  verifyPhoneNumber(String verification_code,String input_code)
    {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verification_code,input_code);
        signInWithPhone(credential);
    }

    public void signInWithPhone(PhoneAuthCredential credential)
    {
        auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(GuestActivity.this,"OTP",Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(GuestActivity.this,"OTP is Wrong!!!",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
