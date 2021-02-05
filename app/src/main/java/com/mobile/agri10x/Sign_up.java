package com.mobile.agri10x;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.ViewGroup.LayoutParams;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.gson.Gson;
import com.mobile.agri10x.Connection.POSTRequest;
import com.mobile.agri10x.Model.Main;
import com.mobile.agri10x.Model.SignUpUser;
import com.rilixtech.CountryCodePicker;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.TimeUnit;

public class Sign_up extends AppCompatActivity {
    private PopupWindow mPopupWindow;
    ConstraintLayout con;
    private static final String[] roles = new String[] {
            "Farmer", "Trader","Quality Check"
    };
    EditText first_name,last_name,email,password,confirm_passwod,wallet_password,confirm_wallet_password;
    EditText contact_no;
    CountryCodePicker ccp;
    Button SignUp;
    String first_name_string="",last_name_string="",email_string="",contact_no_string="",password_string="",confirm_passwod_string="",role_string="",wallet_password_string="",confirm_wallet_password_string="",cpp_string="";
    ImageView profile_image,img_arrow;
    Bitmap bitmap;
    ScrollView parentView;
    // MaterialBetterSpinner role_spinner;
    RadioButton radioButton,radioButton_farmer,radioButtontrader;
    RadioGroup radioGroup;
    TextView termsAndConPage,sendotp;
    AlertDialog pleaseWait,codealert;
    //otp
    FirebaseAuth auth;
    TextView callnumber;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    String verification_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_activity_sign_up); //activity_sign_up

        /*//otp
        auth = FirebaseAuth.getInstance();

        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                //Getting the code sent by SMS
                String code = phoneAuthCredential.getSmsCode();

             /*   if (code != null) {
                    verify.setText(code);
                    //verifying the code
                    verifyVerificationCode(code);
                }
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(Sign_up.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);

                verification_code = s;
                Toast.makeText(getApplicationContext(),"code sent to the number",Toast.LENGTH_LONG).show();
            }
        };*/

        img_arrow = findViewById(R.id.img_arrow);
        con = findViewById(R.id.con);
        first_name = findViewById(R.id.first_name);
        last_name = findViewById(R.id.last_name);
        email = findViewById(R.id.sup_email);
        contact_no = findViewById(R.id.contact_no);
        password = findViewById(R.id.sup_password);
        confirm_passwod = findViewById(R.id.sup_confirm_password);
        sendotp = findViewById(R.id.sendotp);
        callnumber = findViewById(R.id.callnumber);

        //wallet_password = findViewById(R.id.wallet_password);
        //confirm_wallet_password = findViewById(R.id.confirm_wallet_password);

        SignUp = findViewById(R.id.signin_signup_btn);
        profile_image = findViewById(R.id.circleImageView);
        parentView = findViewById(R.id.parentScrollView);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_dropdown_item_1line, roles);
        //role_spinner = findViewById(R.id.role_spiner);
//        role_spinner.setAdapter(adapter);
        //     termsAndConPage = findViewById(R.id.termsconditionpage);
        radioButton = findViewById(R.id.tandcbtn);
        radioButton_farmer=findViewById(R.id.radio_farmer);
        radioGroup=findViewById(R.id.radioGroup);
        radioButtontrader=findViewById(R.id.radio_trader);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                MaterialRadioButton rb = (MaterialRadioButton) group.findViewById(checkedId);
                if (rb.isChecked()) {
                    role_string = rb.getText().toString();
                }
            }
        });

        ccp = findViewById(R.id.ccp);
//        termsAndConPage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(Sign_up.this,TermsAndConditions.class));
//            }
//        });
        img_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Sign_up.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        sendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openpopup();
            }
        });
        callnumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPhoneNumber();
            }
        });
        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                first_name_string= first_name.getText().toString().trim();
                last_name_string= last_name.getText().toString().trim();
                //   email_string= email.getText().toString().trim();
                contact_no_string= contact_no.getText().toString().trim();
                email_string = contact_no_string+"@agri10x.com";
                password_string= password.getText().toString().trim();
                cpp_string = '+' + ccp.getSelectedCountryCode().toString();

                confirm_passwod_string = confirm_passwod.getText().toString().trim();
                wallet_password_string="";


                confirm_wallet_password_string= "";
                // role_string = role_spinner.getText().toString().trim();
                if( first_name_string.length()>0 && last_name_string.length()>0 &&
                        contact_no_string.length()>0 &&
                        role_string.length()>0) {// password_string.length()>0 && confirm_passwod_string.length()>0
//                    if (bitmap == null) {
//                        Toast.makeText(Sign_up.this,"Enter your image",Toast.LENGTH_LONG).show();
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                            profile_image.getDefaultFocusHighlightEnabled();
//                        }
//                        parentView.scrollTo(0, parentView.getTop());
//                    }
//                    else if(!radioButton.isChecked()){
//                        Toast.makeText(Sign_up.this,"Check the Terms & Conditions !!!",Toast.LENGTH_LONG).show();
//                    }
//                    else{
//                        if (confirm_passwod_string.equals(password_string)) {
//
//                            //sent_sms();
//                            //inflateEnterOTP();
                    //                          contact_no_string = cpp_string + contact_no_string;
          //          new networkPOST().execute(Main.getOldUrl()+"/CreateUser");
//
//
//                        } else {
//                            Toast.makeText(Sign_up.this, "password does not match!!", Toast.LENGTH_LONG).show();
//                        }
                    //        }
                }
                else{
                    Toast.makeText(Sign_up.this,"Fill all data",Toast.LENGTH_LONG).show();
                }
            }
        });
//        profile_image.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                PickImageDialog.build(new PickSetup())
//                        .setOnPickResult(new IPickResult() {
//                            @Override
//                            public void onPickResult(PickResult r) {
//                                bitmap = r.getBitmap();
//                                profile_image.setImageBitmap(r.getBitmap());
//
//                            }
//                        })
//                        .setOnPickCancel(new IPickCancel() {
//                            @Override
//                            public void onCancelClick() {
//                            }
//                        }).show(getSupportFragmentManager());
//            }
//        });
    }

    public void callPhoneNumber()
    {
        try
        {
            if(Build.VERSION.SDK_INT > 22)
            {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling

                    ActivityCompat.requestPermissions(Sign_up.this, new String[]{Manifest.permission.CALL_PHONE}, 101);

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

    private void openpopup() {
        // Initialize a new instance of LayoutInflater service
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        // Inflate the custom layout/view
        View customView = inflater.inflate(R.layout.otp_layout,null);

                /*
                    public PopupWindow (View contentView, int width, int height)
                        Create a new non focusable popup window which can display the contentView.
                        The dimension of the window must be passed to this constructor.

                        The popup does not provide any background. This should be handled by
                        the content view.

                    Parameters
                        contentView : the popup's content
                        width : the popup's width
                        height : the popup's height
                */
        // Initialize a new instance of popup window
        mPopupWindow = new PopupWindow(
                customView,
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        );

        // Set an elevation value for popup window
        // Call requires API level 21
        if(Build.VERSION.SDK_INT>=21){
            mPopupWindow.setElevation(5.0f);
        }

        // Get a reference for the custom view close button
        ImageButton closeButton = (ImageButton) customView.findViewById(R.id.ib_close);

        // Set a click listener for the popup window close button
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Dismiss the popup window
                mPopupWindow.dismiss();
            }
        });

                /*
                    public void showAtLocation (View parent, int gravity, int x, int y)
                        Display the content view in a popup window at the specified location. If the
                        popup window cannot fit on screen, it will be clipped.
                        Learn WindowManager.LayoutParams for more information on how gravity and the x
                        and y parameters are related. Specifying a gravity of NO_GRAVITY is similar
                        to specifying Gravity.LEFT | Gravity.TOP.

                    Parameters
                        parent : a parent view to get the getWindowToken() token from
                        gravity : the gravity which controls the placement of the popup window
                        x : the popup's x location offset
                        y : the popup's y location offset
                */
        // Finally, show the popup window at the center location of root relative layout
        mPopupWindow.showAtLocation(con, Gravity.CENTER,0,0);
    }

    void inflateEnterOTP(){
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(Sign_up.this);
        View mView = getLayoutInflater().inflate(R.layout.code_alert, null);
        final EditText otp = mView.findViewById(R.id.otptxt);
        Button mOkey = mView.findViewById(R.id.otp_enter);
        Button OTPCancel = mView.findViewById(R.id.otp_cancel);
        mBuilder.setView(mView);
        mBuilder.setCancelable(false);
        codealert = mBuilder.create();
        codealert.show();
        mOkey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = otp.getText().toString().trim();
                if(str.length()>0)
                {
                    String inputcode = otp.getText().toString().trim();
                    verifyPhoneNumber(verification_code,inputcode);
                    pleaseWait = new Alert().pleaseWait();
                }
            }
        });
        OTPCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codealert.dismiss();
            }
        });
    }
//
//    class networkPOST extends AsyncTask<String, Integer, String> {
//        AlertDialog dialog;
//
//        @Override
//        protected void onProgressUpdate(Integer... values) {
//            super.onProgressUpdate(values);
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            dialog = new Alert().pleaseWait();
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            dialog.dismiss();
//            if(s!=null) {
//                if (s.equals("network")) {
//                    new Alert().alert("Network !!!", getResources().getString(R.string.network_error_message));
//                }else {
//                    if(s.equals("1")){
//                        new Alert().alert("Error !!","Email ID Already Exist");
//                    }else if(s.equals("2")){
//                        new Alert().alert("Error !!","Telephone number Already Exist");
//                    }else if(s.equals("3")){
//                        new Alert().alert("Error !!","Telephone number and Email ID Already Exist");
//                    }else{
//                        first_name.setText("");
//                        last_name.setText("");
//                        email.setText("");
//                        contact_no.setText("");
////                        password.setText("");
////                        confirm_passwod.setText("");
//                        //  role_spinner.setText("");
//                        //   wallet_password.setText("");
//                        //confirm_wallet_password.setText("");
////                        String uri = "@drawable/profile_final";
////                        int imageResource = getResources().getIdentifier(uri, null, getPackageName());
////                        Drawable res = getResources().getDrawable(imageResource);
////                        profile_image.setImageDrawable(res);
//                        new Alert().SuccessfulSignUP();
//                    }
//                }
//            }
//        }
//
//        @Override
//        protected String doInBackground(String... strings) {
//            String str;
//            try {
//                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
//                byte[] byteArray = byteArrayOutputStream .toByteArray();
//                String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
////                if(role_string.equals("Quality Check")){
////                    role_string = "QC";
////                }
//                SignUpUser newUser = new SignUpUser(email_string,first_name_string,last_name_string,encoded,"P"+role_string,contact_no_string);
//                str = POSTRequest.fetchUserData(strings[0],new Gson().toJson(newUser),Sign_up.this);
//            } catch (Exception e) {
//                return "network";
//            }
//            return str;
//        }
//
//    }

    public class Alert {

        public void alert(String title, String body) {
            final AlertDialog.Builder Alert = new AlertDialog.Builder(Sign_up.this);
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
            final AlertDialog.Builder Alert = new AlertDialog.Builder(Sign_up.this);
            Alert.setCancelable(false)
                    .setTitle("Successful SignUP")
                    .setMessage("Thank you for signing up.\n It will take some time to activate your account try to login after some time. \n-With Regards Agri10x Team");
            Alert.setNegativeButton("Okay", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                    finish();
                    Intent intent = new Intent(Sign_up.this, LoginActivity.class);
                    startActivity(intent);
                }
            });

            Alert.create().show();
        }

        public AlertDialog pleaseWait() {
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(Sign_up.this);
            View mView = getLayoutInflater().inflate(R.layout.alert_progress_spinning, null);
            ProgressBar pb = mView.findViewById(R.id.progressBar);
            mBuilder.setView(mView);
            mBuilder.setCancelable(false);
            final AlertDialog dialog = mBuilder.create();
            dialog.show();
            return dialog;
        }
    }


    public void sent_sms()
    {
        //Toast.makeText(this,"sending code ",Toast.LENGTH_LONG).show();
        String number = ccp.getFullNumberWithPlus() + contact_no.getText().toString();
        Toast.makeText(this,number,Toast.LENGTH_LONG).show();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,60, TimeUnit.SECONDS,this,mCallback
        );
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
                    pleaseWait.dismiss();
                    codealert.dismiss();
                 //   new networkPOST().execute(Main.getOldUrl()+"/CreateUser");
                }
                else
                {
                    codealert.dismiss();
                    pleaseWait.dismiss();
                    Toast.makeText(Sign_up.this,"OTP is Wrong!!!",Toast.LENGTH_LONG).show();
                    inflateEnterOTP();
                }
            }
        });
    }
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

    @Override
    public void onBackPressed() {

    }
}
