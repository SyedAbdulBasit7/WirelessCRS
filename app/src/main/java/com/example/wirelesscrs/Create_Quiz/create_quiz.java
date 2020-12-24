package com.example.wirelesscrs.Create_Quiz;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.wirelesscrs.Attempt_Quiz_Section.view_de_test;
import com.example.wirelesscrs.R;
import com.example.wirelesscrs.Results_section.view_de_result;
import com.example.wirelesscrs.Splash_Activity.Splash_create_quiz;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

public class create_quiz extends AppCompatActivity {

    private FloatingActionButton fab1;
    private Toolbar toolbar;
    private Button delete_test,delete_result;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main_1);
        toolbar = findViewById(R.id.toolbartst);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.black));
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        delete_result=findViewById(R.id.delete_result);
        delete_test=findViewById(R.id.delete_test);
        fab1 = findViewById(R.id.fab);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(create_quiz.this, Splash_create_quiz.class));
                finish();
            }
        });
        delete_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(create_quiz.this, view_de_test.class);
                startActivity(intent);
                finish();
            }
        });
        delete_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(create_quiz.this, view_de_result.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}