package com.example.wirelesscrs.Auth_Controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.example.wirelesscrs.Model.SUser;
import com.example.wirelesscrs.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class signup extends AppCompatActivity {

    EditText edt_name,edt_email,edt_pwd,edt_c_pwd,edt_age;
    Spinner spin_gender,spin_depart;
    Button btn_submit;
    AwesomeValidation awesomeValidation;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    private AVLoadingIndicatorView avLoadingIndicatorView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        edt_name=findViewById(R.id.u_name);
        edt_email=findViewById(R.id.u_email);
        edt_pwd=findViewById(R.id.u_pwd);
        edt_c_pwd=findViewById(R.id.u_c_pwd);
        edt_age=findViewById(R.id.u_age);
        avLoadingIndicatorView = findViewById(R.id.loader2);
        spin_gender=findViewById(R.id.u_gender);
        spin_depart=findViewById(R.id.u_depart);

        btn_submit=findViewById(R.id.btn_register);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        String regexPassword = "(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[~`!@#\\$%\\^&\\*\\(\\)\\-_\\+=\\{\\}\\[\\]\\|\\;:\"<>,./\\?]).{8,}";
        awesomeValidation.addValidation(signup.this,R.id.u_name, "[a-zA-Z\\s]+",R.string.Name);
        awesomeValidation.addValidation(signup.this,R.id.u_email, Patterns.EMAIL_ADDRESS,R.string.Email_Address);
        awesomeValidation.addValidation(signup.this,R.id.u_pwd,regexPassword,R.string.Password);
        awesomeValidation.addValidation(signup.this,R.id.u_c_pwd,R.id.u_pwd,R.string.Con_Password);
        awesomeValidation.addValidation(signup.this,R.id.u_age,"^(?:[1-9][1-9]|[2-9][0-9])$",R.string.age);

        databaseReference=firebaseDatabase.getInstance().getReference("SUser");//make database name student
        firebaseAuth = FirebaseAuth.getInstance();

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String name=edt_name.getText().toString().trim();
                final String email=edt_email.getText().toString().trim();
                String password=edt_pwd.getText().toString().trim();
                final String age=edt_age.getText().toString().trim();
                final String gender=spin_gender.getSelectedItem().toString().trim();
                final String department=spin_depart.getSelectedItem().toString().trim();

                if(awesomeValidation.validate())
                {
                    avLoadingIndicatorView.setVisibility(View.VISIBLE);
                    avLoadingIndicatorView.smoothToShow();
                    firebaseAuth.createUserWithEmailAndPassword(email,password)
                            .addOnCompleteListener(signup.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){

                                        SUser u1=new SUser(
                                                name,
                                                email,
                                                Integer.parseInt(age),
                                                gender,
                                                department
                                        );

                                        firebaseDatabase.getInstance().getReference("SUser")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .setValue(u1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                avLoadingIndicatorView.setVisibility(View.GONE);
                                                avLoadingIndicatorView.smoothToHide();
                                                Objects.requireNonNull(firebaseAuth.getCurrentUser()).sendEmailVerification();
                                                Intent intent = new Intent(signup.this, login.class);
                                                startActivity(intent);
                                                finish();
                                                Toasty.success(signup.this,R.string.email_sent,Toasty.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                    else {
                                        avLoadingIndicatorView.setVisibility(View.GONE);
                                        avLoadingIndicatorView.smoothToHide();
                                        if(task.getException() instanceof FirebaseAuthUserCollisionException){

                                            Toasty.error(signup.this, "Email Already Register", Toasty.LENGTH_SHORT).show();
                                        }
                                        else
                                        {
                                            avLoadingIndicatorView.setVisibility(View.GONE);
                                            avLoadingIndicatorView.smoothToHide();
                                            Toasty.error(signup.this,task.getException().getMessage(),Toasty.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });


                }
                else {
                    avLoadingIndicatorView.setVisibility(View.GONE);
                    avLoadingIndicatorView.smoothToHide();
                    Toasty.warning(signup.this, "Fill Correctly", Toasty.LENGTH_SHORT).show();
                }
            }


        });

    }
    @Override
    protected void onResume() {
        super.onResume();
        avLoadingIndicatorView.setVisibility(View.GONE);
        avLoadingIndicatorView.hide();
    }
}