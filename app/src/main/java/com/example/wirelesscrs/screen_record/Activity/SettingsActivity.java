package com.example.wirelesscrs.screen_record.Activity;

import android.app.ActivityOptions;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wirelesscrs.R;

public class SettingsActivity extends AppCompatActivity {

    public LinearLayout quality, rSettings, controls;
    public TextView qua, con;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        toolbar = findViewById(R.id.srv_toolbar2);
        toolbar.setTitle("Settings");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        con = findViewById(R.id.srv_con);
        qua = findViewById(R.id.srv_qua);
        quality = findViewById(R.id.srv_quality);
        rSettings = findViewById(R.id.srv_rSetting);
        controls = findViewById(R.id.srv_controls);


        quality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**Shared Element Code*/
//                Intent in = new Intent(SettingsActivity.this, QualityActivity.class);
////                ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(SettingsActivity.this, quality,"qua");
//
//                Pair[] pairs = new Pair[1];
//                pairs[0] = new Pair<View, String>(quality, "quality");
//                pairs[0] = new Pair<View, String>(qua, "qua");
//                ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(SettingsActivity.this, pairs);
//                startActivity(in,activityOptions.toBundle());
//
                Intent myIntent = new Intent(SettingsActivity.this, QualityActivity.class);
                startActivity(myIntent);
                overridePendingTransition(0, 0);

            }
        });

        rSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(SettingsActivity.this, RecordingActivity.class);
                startActivity(myIntent);
                overridePendingTransition(0, 0);

            }
        });

        controls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(SettingsActivity.this, ControlsActivity.class);
                startActivity(myIntent);
                overridePendingTransition(0, 0);

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
