package com.example.todaydrink;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener{

    Button dutchPayBtn;
    Button participantBtn;
    Button timeBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        dutchPayBtn = (Button)findViewById(R.id.menu_pay);
        participantBtn = (Button)findViewById(R.id.menu_participant);
        timeBtn = (Button)findViewById(R.id.menu_time);
        dutchPayBtn.setOnClickListener(this);
        participantBtn.setOnClickListener(this);
        timeBtn.setOnClickListener(this);
    }

    public void onClick(View view){
        if(view == dutchPayBtn) {
            startActivity(new Intent(this, DutchPayPictureActivity.class));
        }
    }
}