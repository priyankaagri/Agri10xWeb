package com.mobile.agri10x;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TermsAndConditions extends AppCompatActivity {

    Toolbar toolbar;
    FloatingActionButton fab;
    private TextView termscondition1,termscondition2,termscondition3,termscondition4,termscondition5,termscondition6,termscondition7,termscondition8,termscondition9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_conditions);
        termscondition1 = findViewById(R.id.termsconditions1);
        termscondition1.setText("\n\u2022 When you use our Application, we collect and store your personal information which is provided by you from time to time. Our primary goal in doing so is to provide you a safe, efficient, smooth and customized experience.\n");
        termscondition2 = findViewById(R.id.termsconditions2);
        termscondition2.setText("\u2022 If you transact with us, we collect some additional information, such as a billing address, a credit / debit card number and a credit / debit card expiration date and/ or other payment instrument details and tracking information from cheques or money orders.\n");
        termscondition3 = findViewById(R.id.termsconditions3);
        termscondition3.setText("\u2022 We use personal information to provide the services you request. To the extent we use your personal information to market to you, we will provide you the ability to opt-out of such uses.\n");
        termscondition4 = findViewById(R.id.termsconditions4);
        termscondition4.setText("\u2022 Our Application links to other domains that may collect personally identifiable information about you. Agri10x is not responsible for the privacy practices or the content of those linked Applications.\n");
        termscondition4 = findViewById(R.id.termsconditions5);
        termscondition4.setText("\u2022 Our Application has stringent security measures in place to protect the loss, misuse, and alteration of the information under our control. Once your information is in our possession we adhere to strict security guidelines, protecting it against unauthorized access.\n");
        termscondition4 = findViewById(R.id.termsconditions6);
        termscondition4.setText("\u2022 We provide all users with the opportunity to opt-out of receiving non-essential (promotional, marketing-related) communications from us on behalf of our partners, and from us in general, after setting up an account\n");
        termscondition4 = findViewById(R.id.termsconditions7);
        termscondition4.setText("\u2022 If we decide to change our privacy policy, we will post those changes on this page so that you are always aware of what information we collect, how we use it, and under what circumstances we disclose it.");
        //toolbar
        toolbar = findViewById(R.id.toolbar_termsandconditions);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_name );
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
