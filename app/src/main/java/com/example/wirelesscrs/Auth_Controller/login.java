package com.example.wirelesscrs.Auth_Controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.example.wirelesscrs.R;
import com.example.wirelesscrs.dashboard_wics;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.Date;

import es.dmoral.toasty.Toasty;

public class login extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;
    EditText edt_email,edt_pass;
    Button btn_register,btn_login;
    TextView forget_pass;
    CheckBox rember_me;
    AwesomeValidation awesomeValidation;
    FirebaseAuth firebaseAuth;
    SharedPreferences preferences;
    static final String PREFS_Name="PrefsFile";
    AVLoadingIndicatorView avLoadingIndicatorView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {

            if(firebaseAuth.getCurrentUser().isEmailVerified()) {
                Intent intent = new Intent(login.this, dashboard_wics.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }else {
                Toasty.warning(getApplicationContext(), R.string.email_unverified, Toasty.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();
                finish();
            }
        }

        setContentView(R.layout.activity_login);


        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_signup);
        rember_me=findViewById(R.id.remember);
        edt_email = findViewById(R.id.email_id);
        edt_pass = findViewById(R.id.pass_id);
        forget_pass=findViewById(R.id.forget_pass);
        avLoadingIndicatorView =  findViewById(R.id.loader1);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        firebaseAuth = FirebaseAuth.getInstance();


        //shared prefernces
        preferences=getSharedPreferences(PREFS_Name,MODE_PRIVATE);
        getPreferenceData();

        awesomeValidation=new AwesomeValidation(ValidationStyle.BASIC);
        String regexPassword = "(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[~`!@#\\$%\\^&\\*\\(\\)\\-_\\+=\\{\\}\\[\\]\\|\\;:\"<>,./\\?]).{8,}";
        awesomeValidation.addValidation(login.this,R.id.email_id, Patterns.EMAIL_ADDRESS,R.string.Email_Address);
        awesomeValidation.addValidation(login.this,R.id.pass_id,regexPassword,R.string.Password);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("Button_Login", "Click");
                bundle.putString("dateTime", (new Date()).toString());
                mFirebaseAnalytics.logEvent("Login", bundle);
                String email=edt_email.getText().toString().trim();
                String password=edt_pass.getText().toString().trim();

                if(awesomeValidation.validate())
                {
                    avLoadingIndicatorView.setVisibility(View.VISIBLE);
                    avLoadingIndicatorView.smoothToShow();
                    firebaseAuth.signInWithEmailAndPassword(email,password)
                            .addOnCompleteListener(login.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    avLoadingIndicatorView.setVisibility(View.GONE);
                                    avLoadingIndicatorView.smoothToHide();
                                    if(!task.isSuccessful()){
                                        Toasty.warning(login.this,getString(R.string.auth_failed), Toasty.LENGTH_LONG).show();
                                    }
                                    else{
                                        if(firebaseAuth.getCurrentUser().isEmailVerified()){
                                            Remember_Me();
                                            Intent intent = new Intent(login.this, dashboard_wics.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                            finish();
                                            Toasty.success(getApplicationContext(),"Login Successfully", Toasty.LENGTH_SHORT).show();
                                        }
                                        else {
                                            Toasty.error(login.this,R.string.email_unverified, Toasty.LENGTH_SHORT).show();
                                            FirebaseAuth.getInstance().signOut();
                                        }
                                    }
                                }
                            });

                }
                else
                {
                    Toasty.warning(login.this, "Fill Correctly", Toast.LENGTH_SHORT).show();
                }

            }
        });


        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("user_id", 1001);
                bundle.putString("user_city", "Karachi");
                bundle.putString("dateTime", (new Date()).toString());
                mFirebaseAnalytics.logEvent("SIGN_UP", bundle);
                Intent intent = new Intent(login.this, signup.class);
                startActivity(intent);
            }
        });

        forget_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this, reset_password.class);
                startActivity(intent);
            }
        });


    }

    // shared preference start here
    public void Remember_Me(){
        if(rember_me.isChecked()){
            Boolean boolIsChecked=rember_me.isChecked();
            SharedPreferences.Editor editor=preferences.edit();
            editor.putString("pref_name",edt_email.getText().toString());
            editor.putString("pref_pass",edt_pass.getText().toString());
            editor.putBoolean("pref_check",boolIsChecked);
            editor.commit();
        }
        else{
            preferences.edit().clear().apply();
        }
    }
    public void getPreferenceData(){
        SharedPreferences sp=getSharedPreferences(PREFS_Name,MODE_PRIVATE);
        if(sp.contains("pref_name")){
            String u=sp.getString("pref_name","not found.");
            edt_email.setText(u.toString());
        }
        if(sp.contains("pref_pass")){
            String p=sp.getString("pref_pass","not found.");
            edt_pass.setText(p.toString());
        }
        if(sp.contains("pref_check")){
            Boolean b=sp.getBoolean("pref_check",false);
            rember_me.setChecked(b);
        }
    }
}