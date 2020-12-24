package com.example.wirelesscrs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wirelesscrs.Auth_Controller.login;
import com.example.wirelesscrs.Model.SUser;
import com.example.wirelesscrs.Splash_Activity.SplashActivity;
import com.example.wirelesscrs.Wi_Lect.activities.file.FileActivity;
import com.example.wirelesscrs.draw.easy_paint;
import com.example.wirelesscrs.reminder.wics_reminder;
import com.example.wirelesscrs.screen_record.Activity.MainActivity;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class dashboard_wics extends AppCompatActivity {

    ImageView cast_fea,draw_fea,reminder_fea,lecture_fea,quiz_fea,record_fea;
    Button btn_logout;
    ImageView img1;
    TextView dashboard_txt;
    private boolean isAdmin=false;
    private FirebaseAuth auth;
    private DatabaseReference myRef;
    private FirebaseDatabase database;
    AVLoadingIndicatorView avLoadingIndicatorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_wics);

        cast_fea=findViewById(R.id.cast_fea);
        draw_fea=findViewById(R.id.draw_fea);
        reminder_fea=findViewById(R.id.reminder_fea);
        lecture_fea=findViewById(R.id.lecture_fea);
        quiz_fea=findViewById(R.id.quiz_fea);
        record_fea=findViewById(R.id.record_fea);
        btn_logout=findViewById(R.id.btn_logout);
        dashboard_txt=findViewById(R.id.dashboard_txt);
        img1=findViewById(R.id.cast_fea);
        auth = FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance();
        myRef=database.getReference();
        avLoadingIndicatorView =  findViewById(R.id.avi_dashboard);
        checkForAdmin();

        cast_fea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isAdmin && isNetworkAvailable(dashboard_wics.this)){
                    try {
                        startActivity(new Intent("android.settings.CAST_SETTINGS"));
                    }
                    catch (ActivityNotFoundException activityNotFoundException) {
                        activityNotFoundException.printStackTrace();
                        Toasty.success(dashboard_wics.this, "Mirroring is not support your device", Toast.LENGTH_SHORT).show();
                    }
                }
                else if (isNetworkAvailable(dashboard_wics.this))
                    Toasty.error(getApplicationContext(), "You are not Admin!", Toasty.LENGTH_SHORT).show();
                else
                    alertNoConnection();
            }
        });
        draw_fea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(dashboard_wics.this, easy_paint.class);
                startActivity(intent);
                finish();
            }
        });
        reminder_fea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(dashboard_wics.this, wics_reminder.class);
                startActivity(intent);
                finish();
            }
        });
        lecture_fea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(dashboard_wics.this, FileActivity.class);
                startActivity(intent);
                finish();
            }
        });
        quiz_fea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(dashboard_wics.this, SplashActivity.class);
                startActivity(intent);
                finish();
            }
        });
        record_fea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(dashboard_wics.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Intent intent=new Intent(dashboard_wics.this, login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }
    public void checkForAdmin() {

        myRef.child("admins").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(Objects.requireNonNull(auth.getUid()))
                        .exists()&& Objects.requireNonNull(dataSnapshot.child(auth.getUid())
                        .getValue()).toString().equals("true")){
                    isAdmin=true;
//                    Toasty.info(getApplicationContext(),"Hello Admin Sir", Toasty.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null
                && connectivityManager.getActiveNetworkInfo().isConnected();
    }
    public void alertNoConnection() {

        /*final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setIcon(R.drawable.nowifi);
        builder.setCancelable(true);
        builder.setTitle("No Connection Available!");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();*/

        Dialog builder = new Dialog(this);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                //nothing;
            }
        });

        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.nowifi);
//        imageView.getLayoutParams().height = 100;
//        imageView.getLayoutParams().width = 100;
//        imageView.requestLayout();
        //imageView.setImage(R.drawable.nowifi);
        builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                400,
                400));
        builder.show();
    }
    @Override
    public void onBackPressed() {
//        if (doubleBackToExitPressedOnce) {
        super.onBackPressed();
        finish();
    }
//        }
@Override
protected void onStart(){
    super.onStart();

    try{
        final FirebaseUser user = auth.getCurrentUser();
        avLoadingIndicatorView.setVisibility(View.VISIBLE);
        avLoadingIndicatorView.smoothToShow();
        myRef.child("SUser")
                .child(user.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        avLoadingIndicatorView.setVisibility(View.GONE);
                        avLoadingIndicatorView.smoothToHide();
                        SUser user1 = dataSnapshot.getValue(SUser.class);
                        dashboard_txt.setText(user1.name.toUpperCase());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
//                    Toasty.error(dashboard_wics.this,databaseError.getCode(),Toasty.LENGTH_SHORT).show();
                    }
                });
    }

    catch (Exception e){
        e.printStackTrace();
    }

}
}
