package com.example.wirelesscrs.screen_record.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.example.wirelesscrs.screen_record.Activity.MainActivity;
import com.example.wirelesscrs.R;
import com.example.wirelesscrs.screen_record.SharedPref;

public class CountDown {

    static SharedPref sharedPref;
    private static int sec = 60 * 2;  // this is for 2 for min TODO replace your sec

    public static void startTimer(Context context, final MainActivity mainActivity) {
        sharedPref = new SharedPref(context);
        final Dialog dialog = new Dialog(context);
        View mylayout = LayoutInflater.from(context).inflate(R.layout.layout_countdown, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(mylayout);

        int i = sharedPref.loadTimerText() + 1;
        final TextView tim = dialog.findViewById(R.id.srv_tim);
//        Button cancel = dialog.findViewById(R.id.cancel);

        final CountDownTimer downTimer = new CountDownTimer(1000 * i, 1000) {

            public void onTick(long millisUntilFinished) {
//                String v = String.format("%02d", millisUntilFinished / 60000);
                int va = (int) ((millisUntilFinished % 60000) / 1000);
                tim.setText("" + String.format("%01d", va));
            }

            public void onFinish() {
                mainActivity.notification();
                mainActivity.activityStart();
                dialog.dismiss();
            }
        };
        downTimer.start();

        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation3;
        dialog.show();

    }

}
