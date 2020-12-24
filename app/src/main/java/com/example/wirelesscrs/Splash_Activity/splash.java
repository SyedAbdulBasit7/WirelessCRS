package com.example.wirelesscrs.Splash_Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.wirelesscrs.R;
import com.example.wirelesscrs.Auth_Controller.login;
import com.wang.avi.AVLoadingIndicatorView;

public class splash extends AppCompatActivity {

    private AVLoadingIndicatorView avLoadingIndicatorView;

    /** Called when the activity is first created. */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        avLoadingIndicatorView = findViewById(R.id.avi);
        avLoadingIndicatorView.setVisibility(View.VISIBLE);
        avLoadingIndicatorView.smoothToShow();
        Animation anim = AnimationUtils.loadAnimation(this,R.anim.alpha);
        anim.reset();
        ConstraintLayout l = findViewById(R.id.lin_lay);
        l.clearAnimation();
        anim=AnimationUtils.loadAnimation(this,R.anim.translate);
        anim.reset();

        //fillableLoaders.setProgress(40,1000);
        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/

        /** Duration of wait **/
        int SPLASH_DISPLAY_LENGTH = 3000;
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(splash.this, login.class);
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                splash.this.startActivity(mainIntent);
                avLoadingIndicatorView.smoothToHide();
                splash.this.finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        }, SPLASH_DISPLAY_LENGTH);
//        Thread xyz=new Thread()
//        {
//            public void run()
//            {
//                try{
//
//                    sleep(3000);
//                }
//                catch(Exception e)
//                {
//                    avLoadingIndicatorView.setVisibility(View.GONE);
//                    avLoadingIndicatorView.smoothToHide();
//                    e.printStackTrace();
//                }
//                finally{
//                    Intent mainIntent = new Intent(splash.this, login.class);
//                    startActivity(mainIntent);
//                    finish();
//                }
//            }
//        };
//        xyz.start();
//    }
//    @Override
//    protected void onPause() {
//        super.onPause();
//        finish();
    }
}


