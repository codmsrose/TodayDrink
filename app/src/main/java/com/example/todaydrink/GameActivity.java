package com.example.todaydrink;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GameActivity extends AppCompatActivity {

    Button calculateBtn;
    Button numberBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        calculateBtn = (Button)findViewById(R.id.game_calculate);
        numberBtn = (Button)findViewById(R.id.game_number);
        calculateBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(GameActivity.this, GameCalculateActivity.class));
            }
        });


    }
}