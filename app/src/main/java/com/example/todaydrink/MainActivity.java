package com.example.todaydrink;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    Button btn_addgroup, btn_profile;
    ImageButton ib_home, ib_measure, ib_statistics, ib_board;
    public int groupNumber = 1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent get_Intent = getIntent();
        String currentUser = get_Intent.getStringExtra("currentUser");

        btn_addgroup = findViewById(R.id.btn_addgroup);
        ib_home = (ImageButton) findViewById(R.id.ib_home);
        ib_measure = (ImageButton) findViewById(R.id.ib_measure);
        ib_statistics = (ImageButton) findViewById(R.id.ib_statistics);
        ib_board = (ImageButton) findViewById(R.id.ib_board);
        btn_profile = findViewById(R.id.btn_profile);

        btn_addgroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddGroupActivity.class);
                intent.putExtra("currentUser", currentUser);
                startActivity(intent);
            }
        });

        ib_measure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, InputDrinkActivity.class);
                intent.putExtra("currentUser", currentUser);
                startActivity(intent);
                finish();
            }
        });

        ib_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        ib_board.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BoardActivity.class);
                startActivity(intent);
            }
        });


        ib_statistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, StatisticsActivity.class);
                intent.putExtra("currentUser", currentUser);
                startActivity(intent);
            }
        });

        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                intent.putExtra("currentUser", currentUser);
                startActivity(intent);
            }
        });
    }
}