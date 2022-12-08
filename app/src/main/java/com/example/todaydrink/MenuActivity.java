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

    String leader;
    int groupNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Intent intent = getIntent();
        leader = intent.getStringExtra("currentUser");
        groupNumber = intent.getIntExtra("groupNumber", 1);

        dutchPayBtn = (Button)findViewById(R.id.menu_pay);
        participantBtn = (Button)findViewById(R.id.menu_participant);
        timeBtn = (Button)findViewById(R.id.menu_time);
        dutchPayBtn.setOnClickListener(this);
        participantBtn.setOnClickListener(this);
        timeBtn.setOnClickListener(this);
    }

    public void onClick(View view){
        if(view == dutchPayBtn){
            Intent intent = new Intent(this, DutchPayPictureActivity.class);
            intent.putExtra("currentUser", leader);
            intent.putExtra("groupNumber", groupNumber);
            startActivity(intent);
        }
        else if(view == timeBtn){
            Intent intent = new Intent(this, DutchPayPictureActivity.class);
            intent.putExtra("currentUser", leader);
            intent.putExtra("groupNumber", groupNumber);
            startActivity(intent);
        }
    }
}