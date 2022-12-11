package com.example.todaydrink;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GameActivity extends AppCompatActivity {

    String currentUser;

    Button calculateBtn;
    Button numberBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent get_Intent = getIntent();
        currentUser = get_Intent.getStringExtra("currentUser");

        calculateBtn = (Button)findViewById(R.id.game_calculate);
        numberBtn = (Button)findViewById(R.id.game_number);
        calculateBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(GameActivity.this, GameCalculateActivity.class);
                intent.putExtra("currentUser", currentUser);
                startActivity(intent);
            }
        });

        numberBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(GameActivity.this, GameNumberActivity.class);
                intent.putExtra("currentUser", currentUser);
                startActivity(intent);
            }
        });
    }
}