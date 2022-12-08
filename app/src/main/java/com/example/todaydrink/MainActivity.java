package com.example.todaydrink;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
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
    Button btn_addgroup, btn_profile, btn_group1, btn_group2;
    ImageButton ib_home, ib_measure, ib_statistics, ib_board;
    public static int REQUEST_CODE = 0;

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
        btn_group1 = findViewById(R.id.btn_group1);
        btn_group2 = findViewById(R.id.btn_group2);

        btn_group1.setVisibility(View.GONE);
        btn_group2.setVisibility(View.GONE);

        btn_addgroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddGroupActivity.class);
                intent.putExtra("currentUser", currentUser);
                REQUEST_CODE++;
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        ib_measure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CheckActivity.class);
                intent.putExtra("currentUser", currentUser);
                startActivity(intent);
            }
        });

        ib_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
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

        btn_group1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                intent.putExtra("currentUser", currentUser);
                intent.putExtra("groupNumber", 1);
                startActivity(intent);
            }
        });

        btn_group2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                intent.putExtra("currentUser", currentUser);
                intent.putExtra("groupNumber", 2);
                startActivity(intent);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                btn_group1.setVisibility(View.VISIBLE);
            }
        }
        else if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                btn_group2.setVisibility(View.VISIBLE);
            }
        }
    }
}