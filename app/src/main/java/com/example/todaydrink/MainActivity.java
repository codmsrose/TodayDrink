package com.example.todaydrink;

import androidx.appcompat.app.AppCompatActivity;

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
    Button btn_addgroup = findViewById(R.id.btn_addgroup);
    ImageButton ib_home = (ImageButton) findViewById(R.id.ib_home);
    ImageButton ib_measure = (ImageButton) findViewById(R.id.ib_measure);
    ImageButton ib_statistics = (ImageButton) findViewById(R.id.ib_statistics);
    ImageButton ib_board = (ImageButton) findViewById(R.id.ib_board);

    public void homeMove(Context context) {
        ib_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void boardMove(Context context) {
        ib_board.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BoardActivity.class);
                startActivity(intent);
            }
        });
    }

    public void statisticsMove(Context context) {
        ib_statistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, StatisticsActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAuth = FirebaseAuth.getInstance();

        btn_addgroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddGroupActivity.class);
                startActivity(intent);
            }
        });

        ib_measure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MeasureActivity.class);
                startActivity(intent);
            }
        });
    }
}